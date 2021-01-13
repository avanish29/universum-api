#!/bin/bash
set -e
set -u
function create_user_and_database_per_service() {
	local servicename=$(echo $1 | tr ',' ' ' | awk  '{print $1}')
	local database=$(echo $1 | tr ',' ' ' | awk  '{print $2}')
	local owner=$(echo $1 | tr ',' ' ' | awk  '{print $3}')	
	echo "Creating user '$owner' and database '$database' for service '$servicename'"
	psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
		CREATE USER $owner;
		CREATE DATABASE $database;
		GRANT ALL PRIVILEGES ON DATABASE $database TO $owner;
	EOSQL
}

if [ -n "$DATABASES_PER_SERVICE" ]; then
	echo "Databases per service requested: $DATABASES_PER_SERVICE"
	for db in $(echo $DATABASES_PER_SERVICE | tr ':' ' '); do
		create_user_and_database_per_service $db
	done
	echo "Databases per service created"
fi