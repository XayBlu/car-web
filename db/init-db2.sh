#!/bin/bash
set -e
export PGPASSWORD=$APP_DB_PASS;
psql -v ON_ERROR_STOP=1 --username "$APP_DB_USER" --dbname "$APP_DB_NAME" <<-EOSQL
  SET SEARCH_PATH = $APP_DB_NAME;
  CREATE SCHEMA IF NOT EXISTS $SCHEMA AUTHORIZATION $APP_DB_USER;
  GRANT ALL PRIVILEGES ON SCHEMA $SCHEMA TO $APP_DB_USER;
EOSQL
