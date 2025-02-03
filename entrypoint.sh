#!/bin/bash

# Start MySQL
service mysql start

# Start Redis in the background
redis-server --requirepass $REDIS_PASSWORD &

# Wait for MySQL to be ready
until mysqladmin ping -h localhost --silent; do
    echo "Waiting for MySQL to start..."
    sleep 2
done

# Run the Spring Boot application
exec java -jar /app.jar
