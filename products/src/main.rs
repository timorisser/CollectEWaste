use axum::{
    extract::Extension,
    routing::{get, patch},
    Router, Server,
};

use std::net::SocketAddr;

use sqlx::postgres::PgPoolOptions;

mod api;
mod db;
mod isc;
mod item;
mod utils;

use api::{
    filters::{product_brands, product_models, product_types},
    get::get_one,
    post::create,
    reserve::{collect, reserve, undo_reservation},
    search::search,
};
use db::pg::PgItemRepo;
use isc::accounts::AccountClient;

#[tokio::main]
async fn main() {
    let port = std::env::var("PORT")
        .ok()
        .and_then(|x| x.parse::<u16>().ok())
        .unwrap_or_else(|| 8000);

    let db_url = std::env::var("DATABASE_URL")
        .unwrap_or_else(|_| "postgres://postgres:12345678@localhost:5432/products".to_string());

    let accounts_url =
        std::env::var("ACCOUNTS_URL").unwrap_or_else(|_| "http://localhost:8080".to_string());

    let pool = PgPoolOptions::new()
        .max_connections(10)
        .connect(&db_url)
        .await
        .expect("connecting to the db");

    let repo = PgItemRepo::new(pool);

    let accounts = AccountClient::new(accounts_url);

    let apiv1 = Router::new()
        .route("/", get(search).post(create))
        .route("/:id", get(get_one))
        .route("/:id/reserve", patch(reserve))
        .route("/:id/undoReservation", patch(undo_reservation))
        .route("/:id/collect", patch(collect))
        .route("/filters/types", get(product_types))
        .route("/filters/brands", get(product_brands))
        .route("/filters/models", get(product_models));

    let router = Router::new()
        .nest("/api/v1/product", apiv1)
        .layer(Extension(repo))
        .layer(Extension(accounts));

    let addr = SocketAddr::from(([0, 0, 0, 0], port));
    Server::bind(&addr)
        .serve(router.into_make_service())
        .await
        .unwrap();
}
