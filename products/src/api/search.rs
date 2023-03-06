use axum::{extract::Extension, Json};

use axum_extra::extract::Query;

use serde::Deserialize;

use crate::item::{Item, ItemState};

use crate::utils::{errors::ApiError, token::Token};

use crate::db::{
    pg::PgItemRepo,
    repo::{ItemRepo, Ordering, SearchAssociativity, SearchFilters},
};

// TODO: find a better way
fn default_limit() -> i8 {
    100
}

#[derive(Debug, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct SearchParams {
    #[serde(default)]
    state: ItemState,
    #[serde(default)]
    associativity: SearchAssociativity,
    #[serde(default)]
    order_by: Ordering,
    #[serde(default = "default_limit")]
    limit: i8,
    after_id: Option<i64>,
    after_distance: Option<i64>,
    // will have 2 elements if coordinates are provided
    // or is empty if not
    #[serde(default)]
    location: Vec<f32>,
    #[serde(default)]
    types: Vec<String>,
    #[serde(default)]
    brands: Vec<String>,
    #[serde(default)]
    models: Vec<String>,
    // TODO: implement it
    max_distance: Option<i64>,
}

pub async fn search(
    Extension(repo): Extension<PgItemRepo>,
    Query(mut params): Query<SearchParams>,
    token: Token,
) -> Result<Json<Vec<Item>>, ApiError> {
    match (params.associativity, params.state, token.is_company) {
        // only companies can search or reserve products
        (SearchAssociativity::Search, _, false) => Err(ApiError::InsufficientPermissions),
        (SearchAssociativity::Reservee, _, false) => Err(ApiError::InsufficientPermissions),
        // a reserved item can't be available
        (SearchAssociativity::Reservee, ItemState::Available, true) => {
            Err(ApiError::ForbiddenSearch)
        }
        // you can only search for available products
        (SearchAssociativity::Search, ItemState::Reserved | ItemState::Sold, true) => {
            Err(ApiError::ForbiddenSearch)
        }
        _ => Ok(()),
    }?;

    // ordering by distance requires either no previous values
    // or both (last id and distance)
    match (params.order_by, params.after_id, params.after_distance) {
        (Ordering::Distance, Some(_), None) | (Ordering::Distance, None, Some(_)) => {
            Err(ApiError::BadUsageOfLastParams)
        }
        _ => Ok(()),
    }?;

    if params.location.len() != 2 {
        // TODO: ask the accounts service for the main location of the user
        params.location = vec![16.373105, 48.208502]; // Vienna (Stephansdom)
    }

    let filters = SearchFilters {
        types: params.types,
        brands: params.brands,
        models: params.models,
    };

    let result = repo
        .search(
            token.id,
            params.state,
            params.associativity,
            params.order_by,
            filters,
            params.location[0],
            params.location[1],
            params.max_distance,
            params.limit,
            params.after_id,
            params.after_distance,
        )
        .await?;

    Ok(Json(result))
}
