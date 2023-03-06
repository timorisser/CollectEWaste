use axum::{
    extract::{Extension, Path, Query},
    http::StatusCode,
};

use crate::db::{pg::PgItemRepo, repo::ItemRepo};

use serde::Deserialize;

use crate::utils::{errors::ApiError, token::Token};

use crate::isc::accounts::{AccountClient, AccountService};

#[derive(Debug, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct ReserveParams {
    appointment: i32,
}

pub async fn reserve(
    Extension(repo): Extension<PgItemRepo>,
    Extension(account): Extension<AccountClient>,
    Path(id): Path<i64>,
    Query(params): Query<ReserveParams>,
    token: Token,
) -> Result<StatusCode, ApiError> {
    if token.is_company == false {
        return Err(ApiError::InsufficientPermissions);
    }

    let appointment = account.get_appointment(&token, params.appointment).await?;

    repo.reserve_item(id, token.id, &appointment).await?;

    Ok(StatusCode::NO_CONTENT)
}

pub async fn undo_reservation(
    Extension(repo): Extension<PgItemRepo>,
    Path(id): Path<i64>,
    token: Token,
) -> Result<StatusCode, ApiError> {
    if token.is_company == false {
        return Err(ApiError::InsufficientPermissions);
    }

    repo.undo_reservation_item(id, token.id).await?;

    Ok(StatusCode::NO_CONTENT)
}

pub async fn collect(
    Extension(repo): Extension<PgItemRepo>,
    Path(id): Path<i64>,
    token: Token,
) -> Result<StatusCode, ApiError> {
    if token.is_company == false {
        return Err(ApiError::InsufficientPermissions);
    }

    repo.collect_item(id, token.id).await?;

    Ok(StatusCode::NO_CONTENT)
}
