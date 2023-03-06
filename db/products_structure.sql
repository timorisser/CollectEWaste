-- imports the postgis extension
CREATE EXTENSION IF NOT EXISTS Postgis;

DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS item_model;
DROP TYPE IF EXISTS ITEM_STATUS;

-- is non-final needed?
-- the reason why I created it was that it is possible to only publish an item when the images are uploaded,
-- but this would add unnecessary complexity. And it also wouldn't be a porblem if the images appear later.
CREATE TYPE ITEM_STATUS AS ENUM ('non-final', 'available', 'reserved', 'sold');

CREATE TABLE item_model (
    id BIGSERIAL PRIMARY KEY,
    product_type VARCHAR(127) NOT NULL, -- TODO: maybe make reference
    brand VARCHAR(127) NOT NULL, -- TODO: maybe make reference
    model VARCHAR(255) NOT NULL
);

CREATE TABLE item (
    id BIGSERIAL PRIMARY KEY,
    description TEXT NOT NULL,

    model_id BIGINT REFERENCES item_model(id) NOT NULL,

    owner BIGINT NOT NULL, -- user id, stored in user service
    location BIGINT NOT NULL, -- users address id, stored in user service

    status ITEM_STATUS NOT NULL DEFAULT 'available',
    -- user id, who has reserved the item
    -- if status is sold, the reservee is the person which got the item
    reservee BIGINT DEFAULT NULL, 
    -- appointment with which the reservee (will collect)/(has collected) the item
    -- also stored in the user service
    collect_appointment BIGINT DEFAULT NULL, 

    position geography(POINT) NOT NULL -- point in postgis
);

-- so companies can see what's in their cart
-- and past "purchases"
CREATE INDEX ON item(reservee); 
CREATE INDEX ON item(owner);
-- for sorting by distance
CREATE INDEX spatial_idx ON item USING GIST (position) WHERE status = 'available';
-- maybe needed
CREATE INDEX ON item(model_id);

-- there is no creation date
-- to sort by creation time, just order by id DESC
