#!/bin/bash

# Healenium Database Query Helper Script
# This script provides pre-defined queries to inspect Healenium healing activity

DB_HOST="localhost"
DB_PORT="5432"
DB_NAME="healenium"
DB_USER="healenium_user"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_header() {
    echo -e "${BLUE}==========================================${NC}"
    echo -e "${BLUE}  Healenium Database Query Helper${NC}"
    echo -e "${BLUE}==========================================${NC}"
    echo ""
}

check_db_connection() {
    echo -e "${YELLOW}Checking database connection...${NC}"
    if docker exec healenium-postgres pg_isready -h localhost -p 5432 > /dev/null 2>&1; then
        echo -e "${GREEN}✓ Database is accessible${NC}"
        return 0
    else
        echo -e "${RED}✗ Database is not accessible${NC}"
        echo "Make sure Healenium infrastructure is running:"
        echo "./manage-healenium.sh start"
        return 1
    fi
}

execute_query() {
    local query="$1"
    local description="$2"
    
    echo -e "${BLUE}$description${NC}"
    echo "----------------------------------------"
    docker exec -e PGPASSWORD=healenium_password healenium-postgres \
        psql -h localhost -p 5432 -U "$DB_USER" -d "$DB_NAME" \
        -c "$query" 2>/dev/null || {
        echo -e "${RED}Failed to execute query${NC}"
        return 1
    }
    echo ""
}

show_menu() {
    echo "Available queries:"
    echo "1. Show all healing activity (recent first)"
    echo "2. Show healing statistics summary"
    echo "3. Show recent healing activity (last 24 hours)"
    echo "4. Show successful healings count by selector"
    echo "5. Show failed healing attempts"
    echo "6. Show most healed selectors"
    echo "7. Show healing success rate by test"
    echo "8. Show database table information"
    echo "9. Custom SQL query"
    echo "0. Exit"
    echo ""
}

query_all_healings() {
    execute_query "
        SELECT 
            healing_timestamp,
            original_selector,
            healed_selector,
            success,
            similarity_score,
            test_class,
            test_method
        FROM selector_healing 
        ORDER BY healing_timestamp DESC 
        LIMIT 20;
    " "Recent Healing Activity (Last 20 records)"
}

query_healing_stats() {
    execute_query "
        SELECT 
            COUNT(*) as total_healing_attempts,
            COUNT(CASE WHEN success = true THEN 1 END) as successful_healings,
            COUNT(CASE WHEN success = false THEN 1 END) as failed_healings,
            ROUND(
                (COUNT(CASE WHEN success = true THEN 1 END) * 100.0 / COUNT(*)), 2
            ) as success_rate_percent
        FROM selector_healing;
    " "Overall Healing Statistics"
}

query_recent_activity() {
    execute_query "
        SELECT 
            healing_timestamp,
            original_selector,
            healed_selector,
            similarity_score
        FROM selector_healing 
        WHERE healing_timestamp > NOW() - INTERVAL '24 hours'
        AND success = true
        ORDER BY healing_timestamp DESC;
    " "Recent Successful Healings (Last 24 hours)"
}

query_healing_counts() {
    execute_query "
        SELECT 
            original_selector,
            healed_selector,
            COUNT(*) as healing_count,
            AVG(similarity_score) as avg_similarity
        FROM selector_healing 
        WHERE success = true 
        GROUP BY original_selector, healed_selector
        ORDER BY healing_count DESC;
    " "Successful Healings Count by Selector"
}

query_failed_healings() {
    execute_query "
        SELECT 
            healing_timestamp,
            original_selector,
            failure_reason,
            test_class,
            test_method
        FROM selector_healing 
        WHERE success = false 
        ORDER BY healing_timestamp DESC 
        LIMIT 10;
    " "Recent Failed Healing Attempts"
}

query_most_healed() {
    execute_query "
        SELECT 
            original_selector,
            COUNT(*) as times_healed,
            COUNT(DISTINCT healed_selector) as different_healings,
            AVG(similarity_score) as avg_similarity
        FROM selector_healing 
        WHERE success = true 
        GROUP BY original_selector
        ORDER BY times_healed DESC
        LIMIT 10;
    " "Most Frequently Healed Selectors"
}

query_success_rate_by_test() {
    execute_query "
        SELECT 
            test_class,
            test_method,
            COUNT(*) as total_attempts,
            COUNT(CASE WHEN success = true THEN 1 END) as successful,
            ROUND(
                (COUNT(CASE WHEN success = true THEN 1 END) * 100.0 / COUNT(*)), 2
            ) as success_rate_percent
        FROM selector_healing 
        GROUP BY test_class, test_method
        ORDER BY success_rate_percent DESC;
    " "Healing Success Rate by Test Method"
}

query_table_info() {
    execute_query "
        SELECT 
            table_name,
            column_name,
            data_type,
            is_nullable
        FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name IN ('selector_healing', 'healing_attempts', 'test_execution_stats', 'element_info', 'healing_config')
        ORDER BY table_name, ordinal_position;
    " "Database Table Structure"
}

custom_query() {
    echo -e "${YELLOW}Enter your SQL query (press Enter twice to execute):${NC}"
    query=""
    while IFS= read -r line; do
        if [[ -z "$line" ]]; then
            break
        fi
        query="$query$line "
    done
    
    if [[ -n "$query" ]]; then
        execute_query "$query" "Custom Query Result"
    else
        echo -e "${RED}No query entered${NC}"
    fi
}

# Main script execution
main() {
    print_header
    
    if ! check_db_connection; then
        exit 1
    fi
    
    while true; do
        show_menu
        read -p "Select an option (0-9): " choice
        echo ""
        
        case $choice in
            1) query_all_healings ;;
            2) query_healing_stats ;;
            3) query_recent_activity ;;
            4) query_healing_counts ;;
            5) query_failed_healings ;;
            6) query_most_healed ;;
            7) query_success_rate_by_test ;;
            8) query_table_info ;;
            9) custom_query ;;
            0) 
                echo -e "${GREEN}Goodbye!${NC}"
                exit 0 
                ;;
            *)
                echo -e "${RED}Invalid option. Please try again.${NC}"
                echo ""
                ;;
        esac
        
        read -p "Press Enter to continue..."
        echo ""
    done
}

# Run main function
main "$@"