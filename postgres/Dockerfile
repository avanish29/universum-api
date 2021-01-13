FROM postgres:13.1-alpine
COPY create_database_per_service.sh /docker-entrypoint-initdb.d/create_database_per_service.sh
RUN chmod +x /docker-entrypoint-initdb.d/create_database_per_service.sh