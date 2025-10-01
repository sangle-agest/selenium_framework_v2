-- Healenium Database Initialization Script
-- This script creates the necessary tables and indexes for Healenium

-- Ensure we're using the correct database
\c healenium;

-- Create healenium schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS healenium;

-- Set search path to include healenium schema
SET search_path TO healenium, public;

-- Create table for storing selector healing history
CREATE TABLE IF NOT EXISTS selector_healing (
    id BIGSERIAL PRIMARY KEY,
    class_name VARCHAR(255) NOT NULL,
    method_name VARCHAR(255) NOT NULL,
    selector_type VARCHAR(50) NOT NULL,
    old_selector TEXT NOT NULL,
    new_selector TEXT NOT NULL,
    healing_timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    test_run_id VARCHAR(255),
    score DECIMAL(5,3),
    restore_ratio DECIMAL(5,3),
    healing_enabled BOOLEAN DEFAULT TRUE,
    screenshot_path TEXT,
    page_url TEXT,
    browser VARCHAR(50),
    browser_version VARCHAR(50),
    session_id VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create table for storing element healing attempts
CREATE TABLE IF NOT EXISTS healing_attempts (
    id BIGSERIAL PRIMARY KEY,
    selector_healing_id BIGINT REFERENCES selector_healing(id) ON DELETE CASCADE,
    attempt_number INTEGER NOT NULL,
    candidate_selector TEXT NOT NULL,
    similarity_score DECIMAL(5,3),
    healing_result VARCHAR(20) CHECK (healing_result IN ('SUCCESS', 'FAILURE', 'PENDING')),
    error_message TEXT,
    attempt_timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create table for storing test execution statistics
CREATE TABLE IF NOT EXISTS test_execution_stats (
    id BIGSERIAL PRIMARY KEY,
    test_run_id VARCHAR(255) NOT NULL,
    test_class VARCHAR(255) NOT NULL,
    test_method VARCHAR(255) NOT NULL,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE,
    status VARCHAR(20) CHECK (status IN ('PASSED', 'FAILED', 'SKIPPED')) NOT NULL,
    healings_count INTEGER DEFAULT 0,
    total_elements INTEGER DEFAULT 0,
    browser VARCHAR(50),
    browser_version VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create table for storing element information
CREATE TABLE IF NOT EXISTS element_info (
    id BIGSERIAL PRIMARY KEY,
    selector_healing_id BIGINT REFERENCES selector_healing(id) ON DELETE CASCADE,
    tag_name VARCHAR(50),
    element_text TEXT,
    element_attributes JSONB,
    element_xpath TEXT,
    element_css_selector TEXT,
    parent_element_info JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create table for storing healing configuration
CREATE TABLE IF NOT EXISTS healing_config (
    id BIGSERIAL PRIMARY KEY,
    config_key VARCHAR(100) NOT NULL UNIQUE,
    config_value TEXT NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Insert default configuration values
INSERT INTO healing_config (config_key, config_value, description) VALUES
    ('healing.enabled', 'true', 'Enable/disable Healenium healing functionality'),
    ('recovery.enabled', 'true', 'Enable/disable element recovery'),
    ('restore.ratio', '0.8', 'Minimum similarity ratio for element restoration'),
    ('score.threshold', '0.5', 'Minimum score threshold for healing acceptance'),
    ('screenshot.enabled', 'true', 'Enable/disable screenshot collection during healing'),
    ('max.healing.attempts', '3', 'Maximum number of healing attempts per element'),
    ('healing.timeout', '10', 'Timeout in seconds for healing operations')
ON CONFLICT (config_key) DO NOTHING;

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_selector_healing_class_method 
    ON selector_healing(class_name, method_name);

CREATE INDEX IF NOT EXISTS idx_selector_healing_timestamp 
    ON selector_healing(healing_timestamp);

CREATE INDEX IF NOT EXISTS idx_selector_healing_test_run 
    ON selector_healing(test_run_id);

CREATE INDEX IF NOT EXISTS idx_healing_attempts_selector_id 
    ON healing_attempts(selector_healing_id);

CREATE INDEX IF NOT EXISTS idx_test_execution_stats_run_id 
    ON test_execution_stats(test_run_id);

CREATE INDEX IF NOT EXISTS idx_test_execution_stats_class_method 
    ON test_execution_stats(test_class, test_method);

CREATE INDEX IF NOT EXISTS idx_element_info_selector_id 
    ON element_info(selector_healing_id);

-- Create a view for healing statistics
CREATE OR REPLACE VIEW healing_statistics AS
SELECT 
    sh.class_name,
    sh.method_name,
    COUNT(*) as total_healings,
    COUNT(CASE WHEN ha.healing_result = 'SUCCESS' THEN 1 END) as successful_healings,
    COUNT(CASE WHEN ha.healing_result = 'FAILURE' THEN 1 END) as failed_healings,
    ROUND(AVG(sh.score), 3) as avg_score,
    ROUND(AVG(sh.restore_ratio), 3) as avg_restore_ratio,
    MIN(sh.healing_timestamp) as first_healing,
    MAX(sh.healing_timestamp) as last_healing
FROM selector_healing sh
LEFT JOIN healing_attempts ha ON sh.id = ha.selector_healing_id
GROUP BY sh.class_name, sh.method_name
ORDER BY total_healings DESC;

-- Create a view for recent healing activity
CREATE OR REPLACE VIEW recent_healing_activity AS
SELECT 
    sh.id,
    sh.class_name,
    sh.method_name,
    sh.old_selector,
    sh.new_selector,
    sh.score,
    sh.healing_timestamp,
    ha.healing_result,
    sh.page_url,
    sh.browser
FROM selector_healing sh
LEFT JOIN healing_attempts ha ON sh.id = ha.selector_healing_id 
    AND ha.attempt_number = (
        SELECT MAX(attempt_number) 
        FROM healing_attempts ha2 
        WHERE ha2.selector_healing_id = sh.id
    )
ORDER BY sh.healing_timestamp DESC
LIMIT 100;

-- Create function to clean old healing data (optional)
CREATE OR REPLACE FUNCTION cleanup_old_healing_data(days_to_keep INTEGER DEFAULT 30)
RETURNS INTEGER AS $$
DECLARE
    deleted_count INTEGER;
BEGIN
    DELETE FROM selector_healing 
    WHERE healing_timestamp < NOW() - INTERVAL '1 day' * days_to_keep;
    
    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    
    -- Also clean up test execution stats
    DELETE FROM test_execution_stats 
    WHERE created_at < NOW() - INTERVAL '1 day' * days_to_keep;
    
    RETURN deleted_count;
END;
$$ LANGUAGE plpgsql;

-- Grant permissions to healenium user
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA healenium TO healenium_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA healenium TO healenium_user;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA healenium TO healenium_user;

-- Set default privileges for future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA healenium 
    GRANT ALL PRIVILEGES ON TABLES TO healenium_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA healenium 
    GRANT ALL PRIVILEGES ON SEQUENCES TO healenium_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA healenium 
    GRANT EXECUTE ON FUNCTIONS TO healenium_user;

-- Insert some sample data for demonstration (optional)
-- This will be useful for testing database connectivity
INSERT INTO test_execution_stats (
    test_run_id, test_class, test_method, start_time, end_time, status, browser
) VALUES (
    'demo-run-001', 
    'HealeniumDemoTest', 
    'testFormSubmission', 
    NOW() - INTERVAL '1 hour', 
    NOW() - INTERVAL '59 minutes', 
    'PASSED', 
    'chrome'
);

COMMIT;