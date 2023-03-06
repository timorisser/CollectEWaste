use axum::{extract::Extension, Json};

use serde::Deserialize;

use crate::db::{pg::PgItemRepo, repo::ItemRepo};

use crate::item::{Item, ItemModel, ItemState};

use crate::utils::{errors::ApiError, token::Token};

use crate::isc::accounts::{AccountClient, AccountService};

#[derive(Debug, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct ItemCreate {
    pub description: String,
    pub product: ItemModel,
    pub location: i32,
}

pub async fn create(
    Extension(repo): Extension<PgItemRepo>,
    Extension(account): Extension<AccountClient>,
    Json(create): Json<ItemCreate>,
    token: Token,
) -> Result<Json<Item>, ApiError> {
    let location = account.get_location(&token, create.location).await?;

    if location.user_id != token.id {
        return Err(ApiError::BadLocation);
    }

    Ok(Json(
        repo.create_item(
            create.description,
            create.product,
            token.id,
            create.location,
        )
        .await?,
    ))
}
