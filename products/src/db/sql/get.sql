SELECT item.id AS id, description, owner, location, status, reservee, collect_appointment,
    item_model.product_type AS type, item_model.brand AS brand, item_model.model AS model,
    distance
FROM 
    (
    SELECT *, 
    ST_DistanceSphere(item.position::geometry, ST_Point($1::FLOAT, $2::FLOAT, 4326)::geometry)::INT / 50 * 50
        AS distance
    FROM item
    )
    AS item
INNER JOIN item_model
ON item.model_id = item_model.id
WHERE item.id = $3
LIMIT 1;
