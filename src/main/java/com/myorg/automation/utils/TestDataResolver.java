package com.myorg.automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for loading and resolving test data from JSON files
 */
public class TestDataResolver {
    private static final Logger logger = LoggerFactory.getLogger(TestDataResolver.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ConcurrentHashMap<String, Map<String, Object>> testDataCache = new ConcurrentHashMap<>();

    // Private constructor to prevent instantiation
    private TestDataResolver() {}

    /**
     * Loads test data from a JSON file
     * 
     * @param jsonFilePath Path to the JSON file (relative to resources folder)
     * @return Map containing all test data
     */
    public static Map<String, Object> loadTestData(String jsonFilePath) {
        logger.info("Loading test data from JSON file: {}", jsonFilePath);
        
        // Check cache first
        if (testDataCache.containsKey(jsonFilePath)) {
            logger.info("Returning cached test data for: {}", jsonFilePath);
            return testDataCache.get(jsonFilePath);
        }
        
        try {
            // Load JSON file from resources
            InputStream inputStream = TestDataResolver.class.getResourceAsStream(jsonFilePath);
            if (inputStream == null) {
                throw new RuntimeException("Test data JSON file not found: " + jsonFilePath);
            }
            
            // Parse JSON to Map
            JsonNode rootNode = objectMapper.readTree(inputStream);
            Map<String, Object> testData = jsonNodeToMap(rootNode);
            
            // Resolve date tokens in the test data
            Map<String, Object> resolvedTestData = resolveTokensInMap(testData);
            
            // Cache the test data
            testDataCache.put(jsonFilePath, resolvedTestData);
            
            logger.info("Successfully loaded and cached test data from: {}", jsonFilePath);
            return resolvedTestData;
            
        } catch (IOException e) {
            logger.error("Failed to load test data from JSON file: {}", jsonFilePath, e);
            throw new RuntimeException("Failed to load test data from JSON file: " + jsonFilePath, e);
        }
    }

    /**
     * Gets test data for a specific test case
     * 
     * @param jsonFilePath Path to the JSON file
     * @param testCaseId Test case identifier
     * @return Map containing test case data
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getTestCaseData(String jsonFilePath, String testCaseId) {
        logger.info("Getting test case data for: {} from {}", testCaseId, jsonFilePath);
        
        Map<String, Object> allTestData = loadTestData(jsonFilePath);
        Object testCaseData = allTestData.get(testCaseId);
        
        if (testCaseData == null) {
            throw new RuntimeException("Test case '" + testCaseId + "' not found in " + jsonFilePath);
        }
        
        if (!(testCaseData instanceof Map)) {
            throw new RuntimeException("Test case data for '" + testCaseId + "' is not a valid object");
        }
        
        return (Map<String, Object>) testCaseData;
    }

    /**
     * Gets a specific value from test case data
     * 
     * @param jsonFilePath Path to the JSON file
     * @param testCaseId Test case identifier
     * @param key Data key
     * @return Value as String
     */
    public static String getValue(String jsonFilePath, String testCaseId, String key) {
        Map<String, Object> testCaseData = getTestCaseData(jsonFilePath, testCaseId);
        Object value = testCaseData.get(key);
        
        if (value == null) {
            logger.warn("Key '{}' not found in test case '{}' from {}", key, testCaseId, jsonFilePath);
            return null;
        }
        
        return value.toString();
    }

    /**
     * Gets a specific value with default fallback
     * 
     * @param jsonFilePath Path to the JSON file
     * @param testCaseId Test case identifier
     * @param key Data key
     * @param defaultValue Default value if key not found
     * @return Value as String or default value
     */
    public static String getValue(String jsonFilePath, String testCaseId, String key, String defaultValue) {
        String value = getValue(jsonFilePath, testCaseId, key);
        return value != null ? value : defaultValue;
    }

    /**
     * Gets an integer value from test case data
     * 
     * @param jsonFilePath Path to the JSON file
     * @param testCaseId Test case identifier
     * @param key Data key
     * @return Integer value
     */
    public static Integer getIntValue(String jsonFilePath, String testCaseId, String key) {
        String value = getValue(jsonFilePath, testCaseId, key);
        if (value == null) {
            return null;
        }
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Failed to parse integer value for key '{}' in test case '{}': {}", key, testCaseId, value);
            throw new RuntimeException("Invalid integer value for key '" + key + "': " + value);
        }
    }

    /**
     * Gets a boolean value from test case data
     * 
     * @param jsonFilePath Path to the JSON file
     * @param testCaseId Test case identifier
     * @param key Data key
     * @return Boolean value
     */
    public static Boolean getBooleanValue(String jsonFilePath, String testCaseId, String key) {
        String value = getValue(jsonFilePath, testCaseId, key);
        if (value == null) {
            return null;
        }
        
        return Boolean.parseBoolean(value);
    }

    /**
     * Resolves date tokens in all string values within a map
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> resolveTokensInMap(Map<String, Object> data) {
        Map<String, Object> resolvedData = new HashMap<>();
        
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Object value = entry.getValue();
            
            if (value instanceof String) {
                // Resolve date tokens in string values
                String resolvedValue = DateTimeUtils.resolve((String) value);
                resolvedData.put(entry.getKey(), resolvedValue);
            } else if (value instanceof Map) {
                // Recursively resolve nested maps
                resolvedData.put(entry.getKey(), resolveTokensInMap((Map<String, Object>) value));
            } else {
                // Keep other types as-is
                resolvedData.put(entry.getKey(), value);
            }
        }
        
        return resolvedData;
    }

    /**
     * Converts JsonNode to Map<String, Object>
     */
    private static Map<String, Object> jsonNodeToMap(JsonNode node) {
        Map<String, Object> map = new HashMap<>();
        
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                map.put(field.getKey(), jsonNodeToValue(field.getValue()));
            }
        }
        
        return map;
    }

    /**
     * Converts JsonNode to appropriate Java object
     */
    private static Object jsonNodeToValue(JsonNode node) {
        if (node.isTextual()) {
            return node.asText();
        } else if (node.isInt()) {
            return node.asInt();
        } else if (node.isLong()) {
            return node.asLong();
        } else if (node.isDouble()) {
            return node.asDouble();
        } else if (node.isBoolean()) {
            return node.asBoolean();
        } else if (node.isNull()) {
            return null;
        } else if (node.isArray()) {
            java.util.List<Object> list = new java.util.ArrayList<>();
            for (JsonNode item : node) {
                list.add(jsonNodeToValue(item));
            }
            return list;
        } else if (node.isObject()) {
            return jsonNodeToMap(node);
        }
        
        return node.asText();
    }

    /**
     * Clears the test data cache
     */
    public static void clearCache() {
        logger.info("Clearing test data cache");
        testDataCache.clear();
    }

    /**
     * Removes specific test data from cache
     * 
     * @param jsonFilePath Path to the JSON file
     */
    public static void removeFromCache(String jsonFilePath) {
        logger.info("Removing test data from cache: {}", jsonFilePath);
        testDataCache.remove(jsonFilePath);
    }

    /**
     * Gets the cache size
     * 
     * @return Number of cached test data files
     */
    public static int getCacheSize() {
        return testDataCache.size();
    }

    /**
     * Checks if test data is cached
     * 
     * @param jsonFilePath Path to the JSON file
     * @return true if cached, false otherwise
     */
    public static boolean isCached(String jsonFilePath) {
        return testDataCache.containsKey(jsonFilePath);
    }

    /**
     * Validates if a JSON file contains valid test data
     * 
     * @param jsonFilePath Path to the JSON file
     * @return true if valid, false otherwise
     */
    public static boolean validateTestData(String jsonFilePath) {
        try {
            loadTestData(jsonFilePath);
            logger.info("Test data is valid: {}", jsonFilePath);
            return true;
        } catch (Exception e) {
            logger.error("Invalid test data: {}", jsonFilePath, e);
            return false;
        }
    }
}