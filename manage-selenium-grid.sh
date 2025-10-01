#!/bin/bash

# Simple Selenium Grid Management Script
# This script manages a basic Selenium Grid for testing purposes

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
COMPOSE_FILE="$SCRIPT_DIR/selenium-grid-docker-compose.yml"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_message() {
    local color=$1
    local message=$2
    echo -e "${color}${message}${NC}"
}

check_docker() {
    if ! command -v docker &> /dev/null; then
        print_message $RED "Error: Docker is not installed or not in PATH"
        exit 1
    fi
    
    if ! docker info >/dev/null 2>&1; then
        print_message $RED "Error: Docker is not running"
        exit 1
    fi
}

check_docker_compose() {
    if ! command -v docker-compose &> /dev/null; then
        print_message $RED "Error: docker-compose is not installed or not in PATH"
        exit 1
    fi
}

start_grid() {
    print_message $BLUE "Starting Selenium Grid..."
    
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
    print_message $YELLOW "Checking service health..."
    
    if curl -s http://localhost:4444/status > /dev/null; then
        print_message $GREEN "✓ Selenium Hub is healthy"
    else
        print_message $YELLOW "? Selenium Hub might still be starting up"
    fi
    
    print_message $GREEN "Selenium Grid started successfully!"
    
    echo ""
    print_message $BLUE "=== Selenium Grid Information ==="
    echo "Selenium Grid:"
    echo "  - Hub: http://localhost:4444"
    echo "  - Console: http://localhost:4444/ui"
    echo ""
}

stop_grid() {
    print_message $BLUE "Stopping Selenium Grid..."
    
    cd "$SCRIPT_DIR"
    docker-compose -f "$COMPOSE_FILE" down
    
    print_message $GREEN "Selenium Grid stopped successfully!"
}

status_grid() {
    print_message $BLUE "Selenium Grid Status:"
    
    cd "$SCRIPT_DIR"
    docker-compose -f "$COMPOSE_FILE" ps
}

logs_grid() {
    local service=$1
    cd "$SCRIPT_DIR"
    
    if [ -z "$service" ]; then
        docker-compose -f "$COMPOSE_FILE" logs --tail=50
    else
        docker-compose -f "$COMPOSE_FILE" logs --tail=50 "$service"
    fi
}

show_help() {
    echo "Selenium Grid Management Script"
    echo ""
    echo "Usage: $0 {start|stop|status|logs|help}"
    echo ""
    echo "Commands:"
    echo "  start         Start Selenium Grid services"
    echo "  stop          Stop Selenium Grid services"
    echo "  status        Show status of all services"
    echo "  logs [service] Show logs (optionally for specific service)"
    echo "  help          Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 start                    # Start all services"
    echo "  $0 logs selenium-hub        # Show hub logs"
    echo "  $0 logs selenium-chrome     # Show Chrome node logs"
}

# Main script logic
case "${1:-help}" in
    start)
        start_grid
        ;;
    stop)
        stop_grid
        ;;
    status)
        status_grid
        ;;
    logs)
        logs_grid "$2"
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