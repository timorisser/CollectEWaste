use axum::{
    extract::{Extension, Path, Query},
    Json,
};

use serde::Deserialize;

use crate::db::{pg::PgItemRepo, repo::ItemRepo};

use crate::utils::{errors::ApiError, token::Token};

use crate::item::{Item, ItemState};

#[derive(Debug, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct GetParams {
    // will have 2 elements if coordinates are provided
    // or is empty if not
    #[serde(default)]
    location: Vec<f32>,
}

pub async fn get_one(
    Extension(repo): Extension<PgItemRepo>,
    Path(id): Path<i64>,
    Query(mut params): Query<GetParams>,
    token: Token,
) -> Result<Json<Item>, ApiError> {
    // TODO: get the location from the accounts service
    if params.location.len() != 2 {
        // TODO: ask the accounts service for the main location of the user
        params.location = vec![16.373105, 48.208502]; // Vienna (Stephansdom)
    }

    let result = repo
        .get_item(id, params.location[0], params.location[1])
        .await?;

    match (
        result.status,
        result.owner,
        result.reservee,
        token.id,
        token.is_company,
    ) {
        (ItemState::Available, _, _, _, true) => Ok(()),
        (_, x, y, z, _) => {
            if x == z as i64 || y == Some(z as i64) {
                Ok(())
            } else {
                Err(ApiError::InsufficientPermissions)
            }
        }
    }?;

    Ok(Json(result))
}
