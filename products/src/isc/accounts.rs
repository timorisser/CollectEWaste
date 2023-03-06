// inter service communication with the accounts microservice

use crate::utils::{errors::ApiError, token::Token};
use axum::async_trait;
use hyper::{
    client::{connect::HttpConnector, Client},
    Body, Method, Request, StatusCode,
};
use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct Location {
    #[serde(alias = "location_id")]
    pub id: i32,
    #[serde(alias = "userid")]
    pub user_id: i32,
    pub street: String,
    #[serde(alias = "streetNumber")]
    pub street_number: i32,
    pub door: i32,
    pub stair: i32,
    pub state: String,
    pub postcode: i32,
}

#[derive(Debug, Deserialize, Copy, Clone)]
pub struct Appointment {
    pub id: i32,
    #[serde(alias = "locationid")]
    pub location: i32,
    // TODO: also parse day, startTime, endTime
}

#[async_trait]
pub trait AccountService {
    async fn get_location(&self, token: &Token, location_id: i32) -> Result<Location, ApiError>;
    async fn get_appointment(
        &self,
        token: &Token,
        appointment_id: i32,
    ) -> Result<Appointment, ApiError>;
}

// Clone is recommended according to the hyper::Client docs
// , as it will still share the underlying connection pool.
// (https://docs.rs/hyper/latest/hyper/client/struct.Client.html)
#[derive(Clone)]
pub struct AccountClient {
    client: Client<HttpConnector>,
    url: String,
}

impl AccountClient {
    pub fn new(url: String) -> Self {
        AccountClient {
            client: Client::new(),
            url,
        }
    }
}

#[async_trait]
impl AccountService for AccountClient {
    async fn get_location(&self, token: &Token, location_id: i32) -> Result<Location, ApiError> {
        let req = Request::builder()
            .method(Method::GET)
            .uri(format!(
                "{}/api/v1/account/location/{}/",
                self.url, location_id
            ))
            .header("User-Agent", "Internal(Products Microservice)")
            .header("Authorization", format!("Bearer {}", token.bearer_string))
            // Required, because java can't parse urls with underscores.
            // You might ask why I even need to support such urls, thats because
            // our professor wants us to use a specific nameing scheme for our
            // docker-compose which contains multiple underscores. Thanks, java.
            // Thanks, professor.
            .header("Host", "localhost:8080")
            .body(Body::empty())
            .unwrap();

        let response = self
            .client
            .request(req)
            .await
            .map_err(|_| ApiError::InterServiceError)?;

        // TODO: ask Patrick if the service can return 403 and properly handle it.
        if response.status() != 200 {
            return Err(match response.status() {
                StatusCode::UNAUTHORIZED => ApiError::InvalidToken,
                StatusCode::NOT_FOUND => ApiError::BadLocation,
                _ => ApiError::InterServiceError,
            });
        } else {
            let body_bytes = hyper::body::to_bytes(response.into_body())
                .await
                .map_err(|_| ApiError::InterServiceError)?;
            let location = serde_json::from_slice::<Location>(&body_bytes)
                .map_err(|_| ApiError::InterServiceError)?;
            return Ok(location);
        }
    }

    async fn get_appointment(
        &self,
        token: &Token,
        appointment_id: i32,
    ) -> Result<Appointment, ApiError> {
        let req = Request::builder()
            .method(Method::GET)
            .uri(format!(
                "{}/api/v1/account/appointment/{}",
                self.url, appointment_id
            ))
            .header("User-Agent", "Internal(Products Microservice)")
            .header("Authorization", format!("Bearer {}", token.bearer_string))
            // Same as above ...
            .header("Host", "localhost:8080")
            .body(Body::empty())
            .unwrap();

        let response = self
            .client
            .request(req)
            .await
            .map_err(|_| ApiError::InterServiceError)?;

        if response.status() != 200 {
            return Err(match response.status() {
                StatusCode::UNAUTHORIZED => ApiError::InvalidToken,
                // TODO: this currently does not work, because the account service
                //       will return 400 (Bad Request) when the appointment doesn't
                //       exist. Thankfully, this isn't my problem.
                StatusCode::NOT_FOUND => ApiError::BadAppointment,
                _ => ApiError::InterServiceError,
            });
        } else {
            let body_bytes = hyper::body::to_bytes(response.into_body())
                .await
                .map_err(|_| ApiError::InterServiceError)?;
            let appointment = serde_json::from_slice::<Appointment>(&body_bytes)
                .map_err(|_| ApiError::InterServiceError)?;
            return Ok(appointment);
        }
    }
}

// TODO: stub/fake implementation of AccountService
