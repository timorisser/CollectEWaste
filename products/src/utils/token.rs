use axum::{
    async_trait,
    extract::{FromRequest, RequestParts, TypedHeader},
    headers::{authorization::Bearer, Authorization},
};

use serde::{Deserialize, Serialize};

use jsonwebtoken::{decode, DecodingKey, Validation};

use crate::utils::errors::ApiError;

#[derive(Debug, Serialize, Deserialize)]
struct Claims {
    sub: String,
    exp: usize,
}

#[derive(Debug)]
pub struct Token {
    pub id: i32,
    pub email: String,
    pub is_company: bool,
    pub bearer_string: String,
}

#[async_trait]
impl<B> FromRequest<B> for Token
where
    B: Send,
{
    type Rejection = ApiError;

    // TODO: actually call the account service to verify the token
    async fn from_request(req: &mut RequestParts<B>) -> Result<Self, Self::Rejection> {
        let TypedHeader(Authorization(bearer)) =
            TypedHeader::<Authorization<Bearer>>::from_request(req)
                .await
                .map_err(|_| ApiError::InvalidToken)?;

        let mut val = Validation::default();
        val.insecure_disable_signature_validation();

        let token_data = decode::<Claims>(
            bearer.token(),
            &DecodingKey::from_secret("secret".as_ref()),
            &val,
        )
        .map_err(|x| {
            println!("{}", x);
            ApiError::InvalidToken
        })?;

        let split: Vec<&str> = token_data.claims.sub.split(",").collect();

        if split.len() != 3 {
            return Err(ApiError::InvalidToken);
        }

        let id = split[0]
            .parse::<i32>()
            .map_err(|_| ApiError::InvalidToken)?;

        let is_company = split[2];

        let is_company = if is_company == "true" {
            true
        } else if is_company == "false" {
            false
        } else {
            return Err(ApiError::InvalidToken);
        };

        Ok(Token {
            id,
            email: String::from(split[1]),
            is_company,
            bearer_string: bearer.token().to_string(),
        })
    }
}
