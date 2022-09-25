#!/bin/bash
set -e
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE COLLATION "en_US" (LOCALE = 'en_US.utf-8');
    CREATE COLLATION "hu_HU" (LOCALE = 'hu_HU.utf-8');
EOSQL
