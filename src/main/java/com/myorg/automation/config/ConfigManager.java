package com.myorg.automation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager for the automation framework
 * Loads and manages configuration properties from multiple sources:
 * 1. framework.properties file
 * 2. System properties (-D parameters)
 * 3. Environment variables
 * 
 * Priority order: System Properties > Environment Variables > Properties File > Default Values
 */
public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static final String PROPERTIES_FILE = "framework.properties";
    private static final Properties properties = new Properties();
    private static volatile boolean initialized = false;
    
    static {
        initialize();
    }
    
    /**
     * Initialize the configuration by loading properties from file
     */
    private static synchronized void initialize() {
        if (initialized) {
            return;
        }
        
        try (InputStream inputStream = ConfigManager.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {
            
            if (inputStream != null) {
                properties.load(inputStream);
                logger.info("Configuration loaded from {}", PROPERTIES_FILE);
            } else {
                logger.warn("Configuration file {} not found, using default values", PROPERTIES_FILE);
            }
            
        } catch (IOException e) {
            logger.error("Error loading configuration file: {}", e.getMessage());
        }
        
        initialized = true;
        logConfiguration();
    }
    
    /**
     * Get string property value with fallback hierarchy
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value
     */
    public static String getProperty(String key, String defaultValue) {
        // 1. Check system properties first (highest priority)
        String value = System.getProperty(key);
        if (value != null && !value.trim().isEmpty()) {
            return value;
        }
        
        // 2. Check environment variables (convert key to env format)
        String envKey = key.toUpperCase().replace('.', '_');
        value = System.getenv(envKey);
        if (value != null && !value.trim().isEmpty()) {
            return value;
        }
        
        // 3. Check properties file
        value = properties.getProperty(key);
        if (value != null && !value.trim().isEmpty()) {
            return value;
        }
        
        // 4. Return default value
        return defaultValue;
    }
    
    /**
     * Get string property value
     * @param key Property key
     * @return Property value or null if not found
     */
    public static String getProperty(String key) {
        return getProperty(key, null);
    }
    
    /**
     * Get integer property value
     * @param key Property key
     * @param defaultValue Default value if property not found or invalid
     * @return Property value as integer
     */
    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                logger.warn("Invalid integer value '{}' for property '{}', using default: {}", 
                    value, key, defaultValue);
            }
        }
        return defaultValue;
    }
    
    /**
     * Get boolean property value
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value as boolean
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }
    
    /**
     * Get long property value
     * @param key Property key
     * @param defaultValue Default value if property not found or invalid
     * @return Property value as long
     */
    public static long getLongProperty(String key, long defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                logger.warn("Invalid long value '{}' for property '{}', using default: {}", 
                    value, key, defaultValue);
            }
        }
        return defaultValue;
    }
    
    /**
     * Get double property value
     * @param key Property key
     * @param defaultValue Default value if property not found or invalid
     * @return Property value as double
     */
    public static double getDoubleProperty(String key, double defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                logger.warn("Invalid double value '{}' for property '{}', using default: {}", 
                    value, key, defaultValue);
            }
        }
        return defaultValue;
    }
    
    // ===================================
    // Browser Configuration
    // ===================================
    
    public static String getBrowser() {
        return getProperty("browser", "chrome");
    }
    
    public static boolean isHeadless() {
        return getBooleanProperty("headless", false);
    }
    
    public static String getBrowserSize() {
        return getProperty("browser.size", "1920x1080");
    }
    
    public static long getBrowserTimeout() {
        return getLongProperty("browser.timeout", 10000);
    }
    
    public static long getPageLoadTimeout() {
        return getLongProperty("browser.page.load.timeout", 30000);
    }
    
    public static boolean shouldTakeScreenshots() {
        return getBooleanProperty("browser.screenshots", true);
    }
    
    public static boolean shouldSavePageSource() {
        return getBooleanProperty("browser.save.page.source", false);
    }
    
    public static String getReportsFolder() {
        return getProperty("browser.reports.folder", "target/selenide-screenshots");
    }
    
    public static boolean shouldUseFastSetValue() {
        return getBooleanProperty("browser.fast.set.value", true);
    }
    
    public static boolean shouldClickViaJS() {
        return getBooleanProperty("browser.click.via.js", false);
    }
    
    // ===================================
    // Application Configuration
    // ===================================
    
    public static String getBaseUrl() {
        String environment = getEnvironment();
        String envSpecificUrl = getProperty(environment + ".base.url");
        if (envSpecificUrl != null) {
            return envSpecificUrl;
        }
        return getProperty("base.url", "https://www.agoda.com");
    }
    
    public static String getApiBaseUrl() {
        return getProperty("api.base.url", "https://api.agoda.com");
    }
    
    public static String getEnvironment() {
        return getProperty("application.environment", "test");
    }
    
    public static String getLocale() {
        return getProperty("application.locale", "en-US");
    }
    
    public static String getCurrency() {
        return getProperty("application.currency", "USD");
    }
    
    public static String getTimezone() {
        return getProperty("application.timezone", "UTC");
    }
    
    // ===================================
    // Test Execution Configuration
    // ===================================
    
    public static int getRetryAttempts() {
        return getIntProperty("test.retry.attempts", 3);
    }
    
    public static long getRetryDelay() {
        return getLongProperty("test.retry.delay", 2000);
    }
    
    public static int getParallelThreads() {
        return getIntProperty("test.parallel.threads", 3);
    }
    
    public static long getShortTimeout() {
        return getLongProperty("test.timeout.short", 2000);
    }
    
    public static long getMediumTimeout() {
        return getLongProperty("test.timeout.medium", 5000);
    }
    
    public static long getLongTimeout() {
        return getLongProperty("test.timeout.long", 10000);
    }
    
    public static long getExtraLongTimeout() {
        return getLongProperty("test.timeout.extra.long", 30000);
    }
    
    // ===================================
    // Reporting Configuration
    // ===================================
    
    public static String getAllureResultsDirectory() {
        return getProperty("allure.results.directory", "target/allure-results");
    }
    
    public static String getAllureReportDirectory() {
        return getProperty("allure.report.directory", "target/allure-report");
    }
    
    public static String getAllureCategoriesFile() {
        return getProperty("allure.categories.file", "allure-categories.json");
    }
    
    public static boolean shouldCleanAllureResults() {
        return getBooleanProperty("allure.clean.results", true);
    }
    
    public static String getAllureStepMode() {
        return getProperty("allure.step.mode", "strict");
    }
    
    public static String getAllureAttachMode() {
        return getProperty("allure.attach.mode", "on_failure");
    }
    
    // ===================================
    // Healenium Configuration
    // ===================================
    
    public static String getHealeniumServerUrl() {
        return getProperty("healenium.server.url", "http://localhost:7878");
    }
    
    public static String getHealeniumServerUser() {
        return getProperty("healenium.server.user", "user");
    }
    
    public static String getHealeniumServerPassword() {
        return getProperty("healenium.server.password", "password");
    }
    
    public static boolean isHealeniumRecoveryEnabled() {
        return getBooleanProperty("healenium.recovery.enabled", true);
    }
    
    public static int getHealeniumRecoveryTimeout() {
        return getIntProperty("healenium.recovery.timeout", 10);
    }
    
    public static double getHealeniumRestoreRatio() {
        return getDoubleProperty("healenium.restore.ratio", 0.8);
    }
    
    public static double getHealeniumScoreThreshold() {
        return getDoubleProperty("healenium.score.threshold", 0.5);
    }
    
    public static boolean isHealeniumHealEnabled() {
        return getBooleanProperty("healenium.heal.enabled", true);
    }
    
    public static boolean isHealeniumReportEnabled() {
        return getBooleanProperty("healenium.report.enabled", true);
    }
    
    public static String getHealeniumReportPath() {
        return getProperty("healenium.report.path", "target/healenium-reports");
    }
    
    public static String getHealeniumScreenshotPath() {
        return getProperty("healenium.screenshot.path", "target/healenium-screenshots");
    }
    
    // ===================================
    // Database Configuration
    // ===================================
    
    public static String getDatabaseUrl() {
        String environment = getEnvironment();
        String envSpecificUrl = getProperty(environment + ".database.url");
        if (envSpecificUrl != null) {
            return envSpecificUrl;
        }
        return getProperty("database.url", "jdbc:postgresql://localhost:5432/healenium");
    }
    
    public static String getDatabaseUser() {
        return getProperty("database.user", "healenium_user");
    }
    
    public static String getDatabasePassword() {
        return getProperty("database.password", "healenium_pass");
    }
    
    public static String getDatabaseSchema() {
        return getProperty("database.schema", "healenium");
    }
    
    // ===================================
    // Utility Methods
    // ===================================
    
    /**
     * Set a property programmatically
     * @param key Property key
     * @param value Property value
     */
    public static void setProperty(String key, String value) {
        System.setProperty(key, value);
        logger.debug("Property '{}' set to '{}'", key, value);
    }
    
    /**
     * Check if a property is set
     * @param key Property key
     * @return true if property exists, false otherwise
     */
    public static boolean hasProperty(String key) {
        return getProperty(key) != null;
    }
    
    /**
     * Get all properties starting with a prefix
     * @param prefix Property prefix
     * @return Properties object with matching properties
     */
    public static Properties getPropertiesWithPrefix(String prefix) {
        Properties result = new Properties();
        
        // Check system properties
        System.getProperties().forEach((key, value) -> {
            if (key.toString().startsWith(prefix)) {
                result.setProperty(key.toString(), value.toString());
            }
        });
        
        // Check loaded properties
        properties.forEach((key, value) -> {
            if (key.toString().startsWith(prefix) && !result.containsKey(key)) {
                result.setProperty(key.toString(), value.toString());
            }
        });
        
        return result;
    }
    
    /**
     * Log current configuration for debugging
     */
    private static void logConfiguration() {
        if (logger.isDebugEnabled()) {
            logger.debug("=== Framework Configuration ===");
            logger.debug("Browser: {}", getBrowser());
            logger.debug("Headless: {}", isHeadless());
            logger.debug("Base URL: {}", getBaseUrl());
            logger.debug("Environment: {}", getEnvironment());
            logger.debug("Timeout: {}ms", getBrowserTimeout());
            logger.debug("Parallel Threads: {}", getParallelThreads());
            logger.debug("Healenium Enabled: {}", isHealeniumHealEnabled());
            logger.debug("==============================");
        }
    }
    
    /**
     * Reset configuration (mainly for testing)
     */
    public static synchronized void reset() {
        properties.clear();
        initialized = false;
        initialize();
    }
}