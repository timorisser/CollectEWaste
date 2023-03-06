SELECT item.id AS id, description, owner, location, status, reservee, collect_appointment,
    item_model.product_type AS type, item_model.brand AS brand, item_model.model AS model,
    distance
FROM 
    (
    SELECT *, 
    ST_DistanceSphere(item.position::geometry, ST_Point($8::FLOAT, $9::FLOAT, 4326)::geometry)::INT / 50 * 50
        AS distance
    FROM item
    )
    AS item
INNER JOIN item_model
ON item.model_id = item_model.id
WHERE status = $1::ITEM_STATUS
    AND 
    (
        item.id < COALESCE($11::BIGINT, 9223372036854775807)
        AND distance <= COALESCE($10::BIGINT, 9223372036854775807)
    )
    AND 
    (
        CASE WHEN cardinality(($4)) != 0 THEN item_model.product_type = ANY ($4) ELSE TRUE END
        AND CASE WHEN cardinality(($5)) != 0 THEN brand = ANY ($5) ELSE TRUE END
        AND CASE WHEN cardinality(($6)) != 0 THEN model = ANY ($6) ELSE TRUE END
    )
    AND
    (
       CASE WHEN $7 = 1 THEN owner = $2
        WHEN $7 = 2 THEN reservee = $2
        ELSE TRUE END
    )
ORDER BY item.id DESC
LIMIT $3::BIGINT
