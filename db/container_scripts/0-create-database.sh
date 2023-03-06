#!/bin/bash

set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE DATABASE account;
	CREATE DATABASE products;
	CREATE DATABASE imagedb;
EOSQL
