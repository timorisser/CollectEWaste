DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_location;
DROP TABLE IF EXISTS appointment;

-- user -> user_location 1 zu m
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    firstname VARCHAR(127) NOT NULL,
    lastname VARCHAR(127) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phonenumber VARCHAR(127) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_company BOOL NOT NULL,
    companyname VARCHAR(255),
    enabled BOOL NOT NULL,
    register_token VARCHAR(255)
);

CREATE INDEX ON users(email);

-- user-location -> appointment 1 zu m
CREATE TABLE user_location (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    location VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    street_number INT NOT NULL,
    stair INT,
    door INT,
    state VARCHAR(255) NOT NULL,
    postcode INT NOT NULL
    --country
);

CREATE INDEX ON user_location(user_id);

CREATE TABLE appointment(
    id SERIAL PRIMARY KEY,
    user_location_id INT REFERENCES user_location(id),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

CREATE INDEX ON appointment(start_time);
CREATE INDEX ON appointment(user_location_id);
