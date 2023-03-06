// all errors that can occur
use axum::{
    http::StatusCode,
    response::{IntoResponse, Response},
};

pub enum ApiError {
    InvalidToken,
    InsufficientPermissions,
    DatabaseError,
    InterServiceError,

    // Search specific
    ForbiddenSearch,      // you can only search for available products
    BadUsageOfLastParams, // when not enough last params where suplied

    // Reservation specific
    AlreadyReserved,
    NotReserved,
    BadAppointment,

    // Post specific
    ModelDoesNotExist,
    BadLocation,

    ProductNotFound,
}

impl IntoResponse for ApiError {
    fn into_response(self) -> Response {
        let resp = match self {
            Self::InvalidToken => (StatusCode::UNAUTHORIZED, "Invalid or expired token."),
            Self::InsufficientPermissions => (StatusCode::FORBIDDEN, "You are not allowed to access this resource."),
            Self::DatabaseError => (StatusCode::INTERNAL_SERVER_ERROR, "There seems to be a prblem with the database."),
            Self::InterServiceError => (StatusCode::INTERNAL_SERVER_ERROR, "A problem occured while communicating with another service."),
            Self::ForbiddenSearch => (StatusCode::BAD_REQUEST, "You can only use the search function for available products. You also can't look for available items where you are the reservee"),
            Self::BadUsageOfLastParams => (StatusCode::BAD_REQUEST, "When sorting by distance, you can either provide both after params or none."),
            Self::AlreadyReserved => (StatusCode::FORBIDDEN, "This product has already been reserved."),
            Self::NotReserved => (StatusCode::FORBIDDEN, "You haven't reserved this product."),
            Self::ModelDoesNotExist => (StatusCode::BAD_REQUEST, "This product model does not exist in the database."),
            Self::BadLocation => (StatusCode::BAD_REQUEST, "The location you want to use for the product either does not exist or isn't yours."),
            Self::ProductNotFound => (StatusCode::NOT_FOUND, "Product could not be found in the database"),
            Self::BadAppointment => (StatusCode::BAD_REQUEST, "The appointment you want to use for the reservation either does not exist or isn't for the right location."),
        };

        resp.into_response()
    }
}
