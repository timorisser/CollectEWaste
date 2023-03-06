#!/bin/bash

set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	\c account;
	\i account_structure.sql;

	\c products;
	\i products_structure.sql;
	
	\c imagedb;
	\i images_structure.sql;
EOSQL
