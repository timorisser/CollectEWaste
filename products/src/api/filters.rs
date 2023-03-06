use axum::{extract::Extension, Json};

use axum_extra::extract::Query;

use crate::db::{pg::PgItemRepo, repo::ItemRepo};

use crate::utils::errors::ApiError;

use serde::Deserialize;

pub async fn product_types(
    Extension(repo): Extension<PgItemRepo>,
) -> Result<Json<Vec<String>>, ApiError> {
    Ok(Json(repo.get_types().await?))
}

#[derive(Debug, Deserialize)]
pub struct BrandFiltersParams {
    #[serde(default)]
    types: Vec<String>,
}

pub async fn product_brands(
    Extension(repo): Extension<PgItemRepo>,
    Query(params): Query<BrandFiltersParams>,
) -> Result<Json<Vec<String>>, ApiError> {
    Ok(Json(repo.get_brands(&params.types).await?))
}

#[derive(Debug, Deserialize)]
pub struct ModelFiltersParams {
    #[serde(default)]
    types: Vec<String>,
    #[serde(default)]
    brands: Vec<String>,
}

pub async fn product_models(
    Extension(repo): Extension<PgItemRepo>,
    Query(params): Query<ModelFiltersParams>,
) -> Result<Json<Vec<String>>, ApiError> {
    Ok(Json(repo.get_models(&params.types, &params.brands).await?))
}
