package com.myorg.automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON Locator Helper - Utility for reading page object locators from clean JSON files
 * 
 * Features:
 * - Load locators from JSON files in array format
 * - Get locator values and types
 * - Support for dynamic locators with {index} placeholders
 * - Cache JSON data for performance
 * - Clean XPath selectors with xpath= prefix support
 */
public class JsonLocatorHelper {
    private static final Logger logger = LoggerFactory.getLogger(JsonLocatorHelper.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, JsonNode> cache = new HashMap<>();
    
    private JsonLocatorHelper() {
        // Utility class - private constructor
    }
    
    /**
     * Load JSON file for a specific page
     * @param pageName The name of the page (e.g., "AgodaHomePage", "AgodaSearchResultsPage")
     * @return JsonNode containing the page configuration
     */
    public static JsonNode loadPageJson(String pageName) {
        String cacheKey = pageName.toLowerCase();
        
        // Check cache first
        if (cache.containsKey(cacheKey)) {
            logger.debug("Loading page JSON from cache: {}", pageName);
            return cache.get(cacheKey);
        }
        
        String jsonFileName = String.format("pages/%s.json", pageName);
        
        try (InputStream inputStream = JsonLocatorHelper.class.getClassLoader().getResourceAsStream(jsonFileName)) {
            if (inputStream == null) {
                logger.error("JSON file not found: {}", jsonFileName);
                throw new RuntimeException("Page JSON file not found: " + jsonFileName);
            }
            
            JsonNode pageJson = objectMapper.readTree(inputStream);
            cache.put(cacheKey, pageJson);
            
            logger.debug("Loaded page JSON: {}", pageName);
            return pageJson;
            
        } catch (IOException e) {
            logger.error("Failed to load page JSON: {}", jsonFileName, e);
            throw new RuntimeException("Failed to load page JSON: " + jsonFileName, e);
        }
    }
    
    /**
     * Get locator value for a specific element
     * @param pageName The page name
     * @param elementName The element name
     * @return The locator value (cleaned, without prefix)
     */
    public static String getLocator(String pageName, String elementName) {
        JsonNode pageJson = loadPageJson(pageName);
        JsonNode elementsArray = pageJson.get("elements");
        
        if (elementsArray == null || !elementsArray.isArray()) {
            throw new RuntimeException("No elements array found in page JSON: " + pageName);
        }
        
        // Search through the elements array
        for (JsonNode elementNode : elementsArray) {
            JsonNode nameNode = elementNode.get("name");
            if (nameNode != null && nameNode.asText().equals(elementName)) {
                JsonNode locatorNode = elementNode.get("locator");
                if (locatorNode == null) {
                    throw new RuntimeException(String.format("Locator not found for element '%s' in page '%s'", elementName, pageName));
                }
                
                String locator = locatorNode.asText();
                // Remove prefix if present (xpath=, css=, id=)
                if (locator.contains("=")) {
                    return locator.substring(locator.indexOf("=") + 1);
                }
                return locator;
            }
        }
        
        throw new RuntimeException(String.format("Element '%s' not found in page '%s'", elementName, pageName));
    }
    
    /**
     * Get locator type for a specific element
     * @param pageName The page name
     * @param elementName The element name
     * @return The locator type (xpath, css, id, etc.)
     */
    public static String getLocatorType(String pageName, String elementName) {
        JsonNode pageJson = loadPageJson(pageName);
        JsonNode elementsArray = pageJson.get("elements");
        
        if (elementsArray == null || !elementsArray.isArray()) {
            throw new RuntimeException("No elements array found in page JSON: " + pageName);
        }
        
        // Search through the elements array
        for (JsonNode elementNode : elementsArray) {
            JsonNode nameNode = elementNode.get("name");
            if (nameNode != null && nameNode.asText().equals(elementName)) {
                JsonNode locatorNode = elementNode.get("locator");
                if (locatorNode != null) {
                    String locator = locatorNode.asText();
                    // Determine type from locator prefix
                    if (locator.startsWith("xpath=")) {
                        return "xpath";
                    } else if (locator.startsWith("css=")) {
                        return "css";
                    } else if (locator.startsWith("id=")) {
                        return "id";
                    } else {
                        // Default to xpath if no prefix
                        return "xpath";
                    }
                }
            }
        }
        
        // Default to xpath
        return "xpath";
    }
    
    /**
     * Get element description
     * @param pageName The page name
     * @param elementName The element name
     * @return The element description
     */
    public static String getElementDescription(String pageName, String elementName) {
        JsonNode pageJson = loadPageJson(pageName);
        JsonNode elementsArray = pageJson.get("elements");
        
        if (elementsArray == null || !elementsArray.isArray()) {
            return "No description available";
        }
        
        // Search through the elements array
        for (JsonNode elementNode : elementsArray) {
            JsonNode nameNode = elementNode.get("name");
            if (nameNode != null && nameNode.asText().equals(elementName)) {
                JsonNode descriptionNode = elementNode.get("description");
                return descriptionNode != null ? descriptionNode.asText() : "No description available";
            }
        }
        
        return "Element not found";
    }
    
    /**
     * Get element wait type (default to visible for this format)
     * @param pageName The page name
     * @param elementName The element name
     * @return The wait type
     */
    public static String getElementWaitType(String pageName, String elementName) {
        // For the clean format, we'll use smart defaults based on element type
        JsonNode pageJson = loadPageJson(pageName);
        JsonNode elementsArray = pageJson.get("elements");
        
        if (elementsArray == null || !elementsArray.isArray()) {
            return "visible";
        }
        
        // Search through the elements array
        for (JsonNode elementNode : elementsArray) {
            JsonNode nameNode = elementNode.get("name");
            if (nameNode != null && nameNode.asText().equals(elementName)) {
                JsonNode typeNode = elementNode.get("type");
                if (typeNode != null) {
                    String type = typeNode.asText().toLowerCase();
                    // Return appropriate wait type based on element type
                    if (type.equals("button") || type.equals("dropdown") || type.equals("option")) {
                        return "clickable";
                    } else if (type.equals("textbox")) {
                        return "visible";
                    } else {
                        return "visible";
                    }
                }
            }
        }
        
        return "visible";
    }
    
    /**
     * Get page URL
     * @param pageName The page name
     * @return The page URL
     */
    public static String getPageUrl(String pageName) {
        JsonNode pageJson = loadPageJson(pageName);
        JsonNode urlNode = pageJson.get("url");
        return urlNode != null ? urlNode.asText() : "";
    }
    
    /**
     * Get page title
     * @param pageName The page name
     * @return The page title
     */
    public static String getPageTitle(String pageName) {
        JsonNode pageJson = loadPageJson(pageName);
        JsonNode titleNode = pageJson.get("pageName");
        return titleNode != null ? titleNode.asText() : pageName;
    }
    
    /**
     * Clear the cache
     */
    public static void clearCache() {
        cache.clear();
        logger.info("JSON locator cache cleared");
    }
}