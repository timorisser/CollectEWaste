DROP TABLE IF EXISTS profile_images;
DROP TABLE IF EXISTS product_images;

 CREATE TABLE product_images (
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL, type VARCHAR(100) NOT NULL,
filepath VARCHAR(100) NOT NULL,
pid INT NOT NULL
);

CREATE TABLE profile_images(
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL, type VARCHAR(100) NOT NULL,
filepath VARCHAR(100) NOT NULL,
uid INT NOT NULL UNIQUE
);

