#!/bin/bash

# Healenium Infrastructure Management Script
# This script helps manage the Healenium Docker infrastructure

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
COMPOSE_FILE="$SCRIPT_DIR/docker-compose.yml"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_message() {
    local color=$1
    local message=$2
    echo -e "${color}${message}${NC}"
}

# Function to check if Docker is running
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        print_message $RED "Error: Docker is not running. Please start Docker first."
        exit 1
    fi
}

# Function to check if docker-compose is available
check_docker_compose() {
    if ! command -v docker-compose &> /dev/null; then
        print_message $RED "Error: docker-compose is not installed or not in PATH."
        exit 1
    fi
}

# Function to start Healenium infrastructure
start_infrastructure() {
    print_message $BLUE "Starting Healenium infrastructure..."
    
    check_docker
    check_docker_compose
    
    cd "$SCRIPT_DIR"
    
    # Pull latest images
    print_message $YELLOW "Pulling latest Docker images..."
    docker-compose -f "$COMPOSE_FILE" pull
    
    # Start services
    print_message $YELLOW "Starting services..."
    docker-compose -f "$COMPOSE_FILE" up -d
    
    # Wait for services to be ready
    print_message $YELLOW "Waiting for services to be ready..."
    sleep 10
    
    # Check service health
    check_services_health
    
    print_message $GREEN "Healenium infrastructure started successfully!"
    print_services_info
}

# Function to stop Healenium infrastructure
stop_infrastructure() {
    print_message $BLUE "Stopping Healenium infrastructure..."
    
    cd "$SCRIPT_DIR"
    docker-compose -f "$COMPOSE_FILE" down
    
    print_message $GREEN "Healenium infrastructure stopped successfully!"
}

# Function to restart Healenium infrastructure
restart_infrastructure() {
    print_message $BLUE "Restarting Healenium infrastructure..."
    stop_infrastructure
    sleep 5
    start_infrastructure
}

# Function to check services health
check_services_health() {
    print_message $YELLOW "Checking service health..."
    
    # Check PostgreSQL
    if docker-compose -f "$COMPOSE_FILE" exec -T postgres pg_isready -U healenium_user -d healenium > /dev/null 2>&1; then
        print_message $GREEN "✓ PostgreSQL is healthy"
    else
        print_message $RED "✗ PostgreSQL is not responding"
    fi
    
    # Check Healenium Backend
    sleep 5
    if curl -f http://localhost:7878/health > /dev/null 2>&1; then
        print_message $GREEN "✓ Healenium Backend is healthy"
    else
        print_message $YELLOW "? Healenium Backend might still be starting up"
    fi
    
    # Check Selenium Hub
    if curl -f http://localhost:4444/status > /dev/null 2>&1; then
        print_message $GREEN "✓ Selenium Hub is healthy"
    else
        print_message $YELLOW "? Selenium Hub might still be starting up"
    fi
}

# Function to show services information
print_services_info() {
    print_message $BLUE "\n=== Healenium Services Information ==="
    print_message $YELLOW "PostgreSQL Database:"
    echo "  - Host: localhost:5432"
    echo "  - Database: healenium"
    echo "  - Username: healenium_user"
    echo "  - Password: healenium_password"
    
    print_message $YELLOW "\nHealenium Backend:"
    echo "  - URL: http://localhost:7878"
    echo "  - Health Check: http://localhost:7878/health"
    
    print_message $YELLOW "\nHealenium Frontend:"
    echo "  - URL: http://localhost:3000"
    
    print_message $YELLOW "\nSelenium Grid:"
    echo "  - Hub: http://localhost:4444"
    echo "  - Console: http://localhost:4444/ui"
    
    print_message $BLUE "\n=== Database Connection Commands ==="
    echo "Connect via psql:"
    echo "  psql -h localhost -p 5432 -U healenium_user -d healenium"
    echo ""
    echo "Connect via Docker:"
    echo "  docker-compose exec postgres psql -U healenium_user -d healenium"
}

# Function to show logs
show_logs() {
    local service=$1
    cd "$SCRIPT_DIR"
    
    if [ -z "$service" ]; then
        print_message $BLUE "Showing logs for all services..."
        docker-compose -f "$COMPOSE_FILE" logs -f
    else
        print_message $BLUE "Showing logs for service: $service"
        docker-compose -f "$COMPOSE_FILE" logs -f "$service"
    fi
}

# Function to show status
show_status() {
    print_message $BLUE "Healenium Infrastructure Status:"
    cd "$SCRIPT_DIR"
    docker-compose -f "$COMPOSE_FILE" ps
}

# Function to access database
access_database() {
    print_message $BLUE "Accessing Healenium database..."
    cd "$SCRIPT_DIR"
    docker-compose -f "$COMPOSE_FILE" exec postgres psql -U healenium_user -d healenium
}

# Function to clean up everything (including volumes)
clean_all() {
    print_message $YELLOW "Warning: This will remove all Healenium data including database!"
    read -p "Are you sure you want to continue? (y/N): " -n 1 -r
    echo
    
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        print_message $BLUE "Cleaning up all Healenium data..."
        cd "$SCRIPT_DIR"
        docker-compose -f "$COMPOSE_FILE" down -v
        docker system prune -f
        print_message $GREEN "Cleanup completed!"
    else
        print_message $YELLOW "Cleanup cancelled."
    fi
}

# Function to show help
show_help() {
    echo "Healenium Infrastructure Management Script"
    echo ""
    echo "Usage: $0 [COMMAND]"
    echo ""
    echo "Commands:"
    echo "  start     - Start Healenium infrastructure"
    echo "  stop      - Stop Healenium infrastructure"
    echo "  restart   - Restart Healenium infrastructure"
    echo "  status    - Show infrastructure status"
    echo "  logs      - Show logs for all services"
    echo "  logs [service] - Show logs for specific service"
    echo "  health    - Check services health"
    echo "  info      - Show services information"
    echo "  db        - Access PostgreSQL database"
    echo "  clean     - Clean up all data (WARNING: destructive)"
    echo "  help      - Show this help message"
    echo ""
    echo "Available services for logs:"
    echo "  postgres, healenium-backend, healenium-frontend"
    echo "  selenium-hub, selenium-chrome, selenium-firefox"
}

# Main script logic
case "${1:-help}" in
    start)
        start_infrastructure
        ;;
    stop)
        stop_infrastructure
        ;;
    restart)
        restart_infrastructure
        ;;
    status)
        show_status
        ;;
    logs)
        show_logs "$2"
        ;;
    health)
        check_services_health
        ;;
    info)
        print_services_info
        ;;
    db)
        access_database
        ;;
    clean)
        clean_all
        ;;
    help|--help|-h)
        show_help
        ;;
    *)
        print_message $RED "Unknown command: $1"
        echo ""
        show_help
        exit 1
        ;;
esac