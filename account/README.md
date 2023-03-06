# Accountmanagement

## Implementationsdetails

### Datenbank -> PostgresSQL

Benutzer
* ID
* Vor- Nachname oder Firmenname
* E-Mail
* Telefonnummer
* Password hash
* Bild -> Aleksander

Wohnort
* ID
* Benutzer ID  (FOREIGN KEY)
* Straße
* Hausnummer
* PLZ
* Land (zukünftig)

Termine
* ID
* Wohnort ID (FOREIGN KEY)
* Von: DATETIME
* Bis: DATETIME

```sql
DELETE TABLE IF EXISTS users;
DELETE TABLE IF EXISTS user_location;
DELETE TABLE IF EXISTS appointment;
-- user -> user_location 1 zu m
CREATE TABLE users (
	id SERIAL PRIMARY KEY,
    	firstname VARCHAR(127) NOT NULL,
    	lastname VARCHAR(127) NOT NULL,
    	email VARCHAR(255) NOT NULL UNIQUE , --make email unique that no other user has the same email as this user
    	phonenumber VARCHAR(127) NOT NULL,
    	password VARCHAR(255) NOT NULL,
    	--profileimage ??,
    	is_company BOOL NOT NULL,
    	companyname VARCHAR(255),
    	register_token VARCHAR(255),
        enabled BOOL NOT NULL
);
CREATE INDEX ON users(email);

-- user-location -> appointment 1 zu m
CREATE TABLE user_location (
	id SERIAL PRIMARY KEY,
    	user_id INT REFERENCES users(id),
    	street VARCHAR(255) NOT NULL,
    	street_number INT NOT NULL,
    	stair INT,
    	door INT,
    	state states NOT NULL,
    	postcode INT NOT NULL,
    	--country  
);
CREATE INDEX ON user_location(user_id);
CREATE TABLE appointment(
	id SERIAL PRIMARY KEY,
    	user_location_id INT REFERENCES user_location(id),
    	day VARCHAR(255) NOT NULL,
    	start_time TIME NOT NULL,
    	end_time TIME NOT NULL
);
CREATE INDEX ON appointment(start_time);
CREATE INDEX ON appointment(user_location_id);
```

### API Dokumentation

[Doku](https://github.com/pschadauer-tgm/collew/tree/main/api_specification) in GitHub OPENAPI Spezifikation

## Docker

**Docker Container for Spring:**
docker run -d --name collew_db -e POSTGRES_PASSOWRD=1220 -v D:/collew_db/:/var/lib/postgresql/data -p 5432:5432 ghcr.io/pschadauer-tgm/proj2223_collew_db:main
