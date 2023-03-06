use axum::async_trait;

use serde::Deserialize;

use crate::item::{Item, ItemModel, ItemState};

use crate::utils::errors::ApiError;

use crate::isc::accounts::Appointment;

#[async_trait]
pub trait ItemRepo {
    // for GET /product/:id
    async fn get_item(&self, id: i64, longitude: f32, latitude: f32) -> Result<Item, ApiError>;
    // for GET /product
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
    ) -> Result<Vec<Item>, ApiError>;
    // for POST
    async fn create_item(
        &self,
        description: String,
        model: ItemModel,
        user_id: i32,
        location: i32,
    ) -> Result<Item, ApiError>;
    // for reserve functions
    async fn reserve_item(
        &self,
        id: i64,
        user_id: i32,
        appointment: &Appointment,
    ) -> Result<(), ApiError>;
    async fn undo_reservation_item(&self, id: i64, user_id: i32) -> Result<(), ApiError>;
    async fn collect_item(&self, id: i64, user_id: i32) -> Result<(), ApiError>;
    // for getting filters
    async fn get_types(&self) -> Result<Vec<String>, ApiError>;
    async fn get_brands(&self, types: &Vec<String>) -> Result<Vec<String>, ApiError>;
    async fn get_models(
        &self,
        types: &Vec<String>,
        brands: &Vec<String>,
    ) -> Result<Vec<String>, ApiError>;
}

#[derive(Debug, Deserialize, Copy, Clone)]
#[serde(rename_all = "camelCase")]
pub enum SearchAssociativity {
    Owner,
    Reservee,
    Search,
}

impl SearchAssociativity {
    pub fn sql_case(&self) -> i32 {
        match self {
            Self::Owner => 1,
            Self::Reservee => 2,
            Self::Search => 3,
        }
    }
}

impl Default for SearchAssociativity {
    fn default() -> Self {
        Self::Search
    }
}

pub struct SearchFilters {
    pub types: Vec<String>,
    pub brands: Vec<String>,
    pub models: Vec<String>,
}

#[derive(Debug, Deserialize, Copy, Clone)]
#[serde(rename_all = "camelCase")]
pub enum Ordering {
    Distance,
    Time,
}

impl Default for Ordering {
    fn default() -> Self {
        Self::Distance
    }
}
