package com.myorg.automation.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myorg.automation.models.PageDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory class for creating DynamicPage instances from JSON page definitions
 */
public class PageObjectFactory {
    private static final Logger logger = LoggerFactory.getLogger(PageObjectFactory.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ConcurrentHashMap<String, DynamicPage> pageCache = new ConcurrentHashMap<>();
    
    // Private constructor to prevent instantiation
    private PageObjectFactory() {}

    /**
     * Loads a page object from a JSON file and creates a DynamicPage instance
     * 
     * @param jsonFilePath Path to the JSON file (relative to resources folder)
     * @return DynamicPage instance
     * @throws RuntimeException if the JSON file cannot be loaded or parsed
     */
    public static DynamicPage loadPage(String jsonFilePath) {
        logger.info("Loading page from JSON file: {}", jsonFilePath);
        
        // Check cache first
        if (pageCache.containsKey(jsonFilePath)) {
            logger.info("Returning cached page for: {}", jsonFilePath);
            return pageCache.get(jsonFilePath);
        }
        
        try {
            // Load JSON file from resources
            InputStream inputStream = PageObjectFactory.class.getResourceAsStream(jsonFilePath);
            if (inputStream == null) {
                throw new RuntimeException("JSON file not found: " + jsonFilePath);
            }
            
            // Parse JSON to PageDefinition
            PageDefinition pageDefinition = objectMapper.readValue(inputStream, PageDefinition.class);
            logger.info("Successfully parsed page definition: {}", pageDefinition.getPageName());
            
            // Create DynamicPage instance
            DynamicPage dynamicPage = new DynamicPage(pageDefinition);
            
            // Cache the page
            pageCache.put(jsonFilePath, dynamicPage);
            
            logger.info("Successfully loaded and cached page: {} from {}", pageDefinition.getPageName(), jsonFilePath);
            return dynamicPage;
            
        } catch (IOException e) {
            logger.error("Failed to load page from JSON file: {}", jsonFilePath, e);
            throw new RuntimeException("Failed to load page from JSON file: " + jsonFilePath, e);
        }
    }

    /**
     * Loads a page object from a PageDefinition instance
     * 
     * @param pageDefinition PageDefinition instance
     * @return DynamicPage instance
     */
    public static DynamicPage loadPage(PageDefinition pageDefinition) {
        logger.info("Loading page from PageDefinition: {}", pageDefinition.getPageName());
        return new DynamicPage(pageDefinition);
    }

    /**
     * Clears the page cache
     */
    public static void clearCache() {
        logger.info("Clearing page cache");
        pageCache.clear();
    }

    /**
     * Removes a specific page from cache
     * 
     * @param jsonFilePath Path to the JSON file
     */
    public static void removeFromCache(String jsonFilePath) {
        logger.info("Removing page from cache: {}", jsonFilePath);
        pageCache.remove(jsonFilePath);
    }

    /**
     * Gets the cache size
     * 
     * @return Number of cached pages
     */
    public static int getCacheSize() {
        return pageCache.size();
    }

    /**
     * Checks if a page is cached
     * 
     * @param jsonFilePath Path to the JSON file
     * @return true if page is cached, false otherwise
     */
    public static boolean isCached(String jsonFilePath) {
        return pageCache.containsKey(jsonFilePath);
    }

    /**
     * Pre-loads a page into cache without returning it
     * 
     * @param jsonFilePath Path to the JSON file
     */
    public static void preloadPage(String jsonFilePath) {
        logger.info("Pre-loading page: {}", jsonFilePath);
        loadPage(jsonFilePath);
    }

    /**
     * Validates if a JSON file contains valid page definition
     * 
     * @param jsonFilePath Path to the JSON file
     * @return true if valid, false otherwise
     */
    public static boolean validatePageDefinition(String jsonFilePath) {
        try {
            InputStream inputStream = PageObjectFactory.class.getResourceAsStream(jsonFilePath);
            if (inputStream == null) {
                logger.warn("JSON file not found: {}", jsonFilePath);
                return false;
            }
            
            PageDefinition pageDefinition = objectMapper.readValue(inputStream, PageDefinition.class);
            
            // Basic validation
            if (pageDefinition.getPageName() == null || pageDefinition.getPageName().trim().isEmpty()) {
                logger.warn("Page definition missing pageName: {}", jsonFilePath);
                return false;
            }
            
            if (pageDefinition.getElements() == null || pageDefinition.getElements().isEmpty()) {
                logger.warn("Page definition missing elements: {}", jsonFilePath);
                return false;
            }
            
            logger.info("Page definition is valid: {}", jsonFilePath);
            return true;
            
        } catch (Exception e) {
            logger.error("Invalid page definition: {}", jsonFilePath, e);
            return false;
        }
    }
}