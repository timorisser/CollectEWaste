// these types are in a seperate file, because multiple api endpoints require them
use serde::{Deserialize, Serialize};

#[derive(Debug, Deserialize, Serialize, Copy, Clone, PartialEq)]
#[serde(rename_all = "kebab-case")]
#[derive(sqlx::Type)]
#[sqlx(type_name = "ITEM_STATUS", rename_all = "kebab-case")]
pub enum ItemState {
    Available,
    Reserved,
    Sold,
}

impl Default for ItemState {
    fn default() -> Self {
        Self::Available
    }
}

#[derive(sqlx::FromRow, Serialize, Debug, Deserialize)]
pub struct ItemModel {
    pub r#type: String,
    pub brand: String,
    pub model: String,
}

#[derive(sqlx::FromRow, Serialize)]
pub struct Item {
    pub id: i64,
    pub description: String,
    #[sqlx(flatten)]
    pub product: ItemModel,
    pub owner: i64,
    pub location: i64,
    pub status: ItemState,
    pub reservee: Option<i64>,
    pub collect_appointment: Option<i64>,
    pub distance: i32,
}
