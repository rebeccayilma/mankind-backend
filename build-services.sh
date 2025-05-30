#!/bin/bash

# Function to build and start a service
build_and_start_service() {
    local service=$1
    echo "Building and starting $service..."
    docker-compose build $service
    docker-compose up -d $service
    echo "Waiting for $service to stabilize..."
    sleep 30
}

# Clean up first
echo "Cleaning up Docker system..."
docker-compose down
docker system prune -af

# Build and start services one by one
build_and_start_service "product-service"
build_and_start_service "user-service"
build_and_start_service "cart-service"
build_and_start_service "wishlist-service"

# Show final status
echo "All services have been started. Checking status..."
docker-compose ps 