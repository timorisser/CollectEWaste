use sqlx::{
    postgres::{PgPool, PgRow},
    Row,
};

use crate::db::repo::{ItemRepo, Ordering, SearchAssociativity, SearchFilters};

use axum::async_trait;

use crate::item::{Item, ItemModel, ItemState};

use crate::utils::errors::ApiError;

use crate::isc::accounts::Appointment;

#[derive(Clone)]
pub struct PgItemRepo {
    pool: PgPool,
}

impl PgItemRepo {
    pub fn new(pool: PgPool) -> Self {
        PgItemRepo { pool }
    }
}

impl PgItemRepo {
    async fn get_simple_item(&self, id: i64) -> Result<SimpleItem, ApiError> {
        let item = sqlx::query_as::<_, SimpleItem>(
            "SELECT owner, location, status, reservee, collect_appointment
            FROM item WHERE id = $1
            FOR UPDATE;",
        )
        .bind(id)
        .fetch_one(&self.pool)
        .await
        .map_err(|x| {
            println!("{:?}", x);
            if let sqlx::Error::RowNotFound = x {
                // product does not exist
                return ApiError::ProductNotFound;
            }
            ApiError::DatabaseError
        })?;

        Ok(item)
    }

    async fn update_item(
        &self,
        id: i64,
        status: ItemState,
        reservee: Option<i32>,
        appointment: Option<&Appointment>,
    ) -> Result<(), ApiError> {
        sqlx::query(
            "UPDATE item 
            SET status = $2, reservee = $3, collect_appointment = $4
            WHERE id = $1;
            ",
        )
        .bind(id)
        .bind(status)
        .bind(reservee)
        .bind(appointment.map(|x| x.id))
        .execute(&self.pool)
        .await
        .map_err(|_| ApiError::DatabaseError)?;

        Ok(())
    }
}

// used because Item is unnecessarily complex for this use case.
#[derive(sqlx::FromRow, Debug)]
#[allow(dead_code)]
struct SimpleItem {
    pub owner: i64,
    pub location: i64,
    pub status: ItemState,
    pub reservee: Option<i64>,
    pub collect_appointment: Option<i64>,
}

#[async_trait]
impl ItemRepo for PgItemRepo {
    async fn get_item(&self, id: i64, longitude: f32, latitude: f32) -> Result<Item, ApiError> {
        Ok(sqlx::query_as::<_, Item>(include_str!("sql/get.sql"))
            .bind(longitude)
            .bind(latitude)
            .bind(id)
            .fetch_one(&self.pool)
            .await
            .map_err(|x| {
                if let sqlx::Error::RowNotFound = x {
                    return ApiError::ProductNotFound;
                }
                ApiError::DatabaseError
            })?)
    }

    async fn search(
        &self,
        user_id: i32,
        state: ItemState,
        associativity: SearchAssociativity,
        ordering: Ordering,
        filters: SearchFilters,
        longitude: f32,
        latitude: f32,
        max_distance: Option<i64>,
        limit: i8,
        after_id: Option<i64>,
        after_distance: Option<i64>,
    ) -> Result<Vec<Item>, ApiError> {
        let result = if let Ordering::Time = ordering {
            sqlx::query_as::<_, Item>(include_str!("sql/search_order_id.sql"))
        } else {
            sqlx::query_as::<_, Item>(include_str!("sql/search_order_distance.sql"))
        }
        .bind(state)
        .bind(user_id)
        .bind(limit as i64)
        .bind(filters.types)
        .bind(filters.brands)
        .bind(filters.models)
        .bind(associativity.sql_case())
        .bind(longitude)
        .bind(latitude)
        .bind(max_distance)
        .bind(after_id)
        .bind(after_distance)
        .fetch_all(&self.pool)
        .await
        .map_err(|x| {
            println!("{:?}", x);
            ApiError::DatabaseError
        })?;
        Ok(result)
    }

    async fn create_item(
        &self,
        description: String,
        model: ItemModel,
        user_id: i32,
        location: i32,
    ) -> Result<Item, ApiError> {
        let model_id = sqlx::query(
            "
            SELECT id FROM item_model WHERE product_type = $1 AND brand = $2 AND model = $3",
        )
        .bind(&model.r#type)
        .bind(&model.brand)
        .bind(&model.model)
        .fetch_one(&self.pool)
        .await
        .map_err(|x| {
            println!("{:?}", x);
            if let sqlx::Error::RowNotFound = x {
                // product does not exist
                return ApiError::ModelDoesNotExist;
            }
            ApiError::DatabaseError
        })?
        .get::<i64, usize>(0);

        // TODO: don't hard code position
        let new_item : (i64,) = sqlx::query_as("
            INSERT INTO item (description, model_id, owner, location, reservee, collect_appointment, position) VALUES
            ($1, $2, $3, $4, NULL, NULL, ST_Point( -71.104, 42.315, 4326))
            RETURNING item.id")
            .bind(&description)
            .bind(model_id)
            .bind(user_id)
            .bind(location)
            .fetch_one(&self.pool)
            .await
            .map_err(|x| {
                println!("{:?}", x);
                ApiError::DatabaseError
            })?;

        Ok(Item {
            id: new_item.0,
            description: description,
            product: model,
            owner: user_id as i64,
            location: location as i64,
            status: ItemState::Available,
            reservee: None,
            collect_appointment: None,
            distance: 0,
        })
    }

    async fn reserve_item(
        &self,
        id: i64,
        user_id: i32,
        appointment: &Appointment,
    ) -> Result<(), ApiError> {
        let item = self.get_simple_item(id).await?;

        if item.status != ItemState::Available {
            return Err(ApiError::AlreadyReserved);
        }

        if appointment.location as i64 != item.location {
            return Err(ApiError::BadAppointment);
        }

        self.update_item(id, ItemState::Reserved, Some(user_id), Some(&appointment))
            .await?;

        Ok(())
    }

    async fn undo_reservation_item(&self, id: i64, user_id: i32) -> Result<(), ApiError> {
        let item = self.get_simple_item(id).await?;

        if item.status != ItemState::Reserved {
            return Err(ApiError::NotReserved);
        }

        // the person, which wants to cancel the reservation didn't reserve the item
        if item.reservee != Some(user_id as i64) {
            return Err(ApiError::InsufficientPermissions);
        }

        self.update_item(id, ItemState::Available, None, None)
            .await?;

        Ok(())
    }

    async fn collect_item(&self, id: i64, user_id: i32) -> Result<(), ApiError> {
        let item = self.get_simple_item(id).await?;

        if item.status != ItemState::Reserved {
            return Err(ApiError::NotReserved);
        }

        // the person, which wants to cancel the reservation didn't reserve the item
        if item.reservee != Some(user_id as i64) {
            return Err(ApiError::InsufficientPermissions);
        }

        sqlx::query(
            "UPDATE item 
            SET status = 'sold'
            WHERE id = $1;
            ",
        )
        .bind(id)
        .execute(&self.pool)
        .await
        .map_err(|_| ApiError::DatabaseError)?;

        Ok(())
    }

    async fn get_types(&self) -> Result<Vec<String>, ApiError> {
        Ok(sqlx::query(
            "
            SELECT DISTINCT product_type FROM item_model;
            ",
        )
        .map(|row: PgRow| row.get::<String, usize>(0))
        .fetch_all(&self.pool)
        .await
        .map_err(|_| ApiError::DatabaseError)?)
    }

    async fn get_brands(&self, types: &Vec<String>) -> Result<Vec<String>, ApiError> {
        Ok(sqlx::query(
            "
            SELECT DISTINCT brand FROM item_model
            WHERE CASE WHEN cardinality(($1)) != 0 THEN 
                product_type = ANY ($1) 
                ELSE TRUE 
            END;
            ",
        )
        .bind(&types)
        .map(|row: PgRow| row.get::<String, usize>(0))
        .fetch_all(&self.pool)
        .await
        .map_err(|_| ApiError::DatabaseError)?)
    }

    async fn get_models(
        &self,
        types: &Vec<String>,
        brands: &Vec<String>,
    ) -> Result<Vec<String>, ApiError> {
        Ok(sqlx::query(
            "
            SELECT DISTINCT model FROM item_model
            WHERE CASE WHEN cardinality(($1)) != 0 THEN 
                product_type = ANY ($1) 
                ELSE TRUE 
            END
            AND
            CASE WHEN cardinality(($2)) != 0 THEN 
                brand = ANY ($2) 
                ELSE TRUE 
            END
            ",
        )
        .bind(types)
        .bind(brands)
        .map(|row: PgRow| row.get::<String, usize>(0))
        .fetch_all(&self.pool)
        .await
        .map_err(|_| ApiError::DatabaseError)?)
    }
}
