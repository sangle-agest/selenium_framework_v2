package com.myorg.automation.utils;

import com.myorg.automation.models.SearchTestData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * TestDataProvider - Centralized test data provider for TestNG tests
 * 
 * Provides test data from JSON files using TestNG's @DataProvider annotation
 * Supports multiple test scenarios and type-safe data models
 */
public class TestDataProvider {
    private static final Logger logger = LoggerFactory.getLogger(TestDataProvider.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Provides search test data for TC01 Da Nang family search
     * @return Object[][] containing SearchTestData for TC01
     */
    @DataProvider(name = "tc01_da_nang_search")
    public static Object[][] provideTc01DaNangSearchData() {
        logger.info("Loading test data for TC01: Da Nang family search");
        
        try {
            SearchTestData testData = loadSearchTestData("tc01_da_nang_search");
            return new Object[][] { { testData } };
            
        } catch (Exception e) {
            logger.error("Failed to load TC01 test data: {}", e.getMessage());
            throw new RuntimeException("TC01 test data loading failed", e);
        }
    }
    
    /**
     * Provides search test data for all test cases
     * @return Object[][] containing SearchTestData for all test cases
     */
    @DataProvider(name = "all_search_test_data")
    public static Object[][] provideAllSearchTestData() {
        logger.info("Loading all search test data");
        
        try {
            InputStream inputStream = TestDataProvider.class.getClassLoader()
                    .getResourceAsStream("testdata/agoda_search_data.json");
            
            if (inputStream == null) {
                throw new RuntimeException("Test data file not found: testdata/agoda_search_data.json");
            }
            
            JsonNode rootNode = objectMapper.readTree(inputStream);
            
            // Count test cases
            int testCaseCount = 0;
            Iterator<String> fieldNames = rootNode.fieldNames();
            while (fieldNames.hasNext()) {
                fieldNames.next();
                testCaseCount++;
            }
            
            // Create data array
            Object[][] testDataArray = new Object[testCaseCount][1];
            Iterator<String> fields = rootNode.fieldNames();
            int index = 0;
            
            while (fields.hasNext()) {
                String testCaseName = fields.next();
                JsonNode testCaseNode = rootNode.get(testCaseName);
                SearchTestData testData = objectMapper.treeToValue(testCaseNode, SearchTestData.class);
                testDataArray[index][0] = testData;
                index++;
                logger.debug("Loaded test data for: {}", testCaseName);
            }
            
            logger.info("Successfully loaded {} test cases", testCaseCount);
            return testDataArray;
            
        } catch (Exception e) {
            logger.error("Failed to load all test data: {}", e.getMessage());
            throw new RuntimeException("All test data loading failed", e);
        }
    }
    
    /**
     * Provides search test data for specific test case
     * @return Object[][] containing SearchTestData for specified test case
     */
    @DataProvider(name = "specific_search_test_data")
    public static Object[][] provideSpecificSearchTestData(String testCaseName) {
        logger.info("Loading test data for: {}", testCaseName);
        
        try {
            SearchTestData testData = loadSearchTestData(testCaseName);
            return new Object[][] { { testData } };
            
        } catch (Exception e) {
            logger.error("Failed to load test data for {}: {}", testCaseName, e.getMessage());
            throw new RuntimeException("Test data loading failed for " + testCaseName, e);
        }
    }
    
    /**
     * Load SearchTestData for specific test case name
     * @param testCaseName The test case name to load
     * @return SearchTestData object
     */
    public static SearchTestData loadSearchTestData(String testCaseName) {
        try {
            // Use TestDataResolver to load and resolve date tokens
            Map<String, Object> allTestData = TestDataResolver.loadTestData("/testdata/agoda_search_data.json");
            
            @SuppressWarnings("unchecked")
            Map<String, Object> testCaseData = (Map<String, Object>) allTestData.get(testCaseName);
            
            if (testCaseData == null) {
                throw new RuntimeException("Test case not found in JSON: " + testCaseName);
            }
            
            // Convert the resolved map to SearchTestData object
            String jsonString = objectMapper.writeValueAsString(testCaseData);
            SearchTestData testData = objectMapper.readValue(jsonString, SearchTestData.class);
            
            logger.info("Successfully loaded test data for: {}", testCaseName);
            logger.debug("Test data: {}", testData);
            
            return testData;
            
        } catch (Exception e) {
            logger.error("Failed to load test data for {}: {}", testCaseName, e.getMessage());
            throw new RuntimeException("Test data loading failed for " + testCaseName, e);
        }
    }
    
    /**
     * Load test data from custom JSON file
     * @param fileName The JSON file name in resources/testdata/
     * @param testCaseName The test case name within the JSON
     * @return SearchTestData object
     */
    public static SearchTestData loadTestDataFromFile(String fileName, String testCaseName) {
        try {
            InputStream inputStream = TestDataProvider.class.getClassLoader()
                    .getResourceAsStream("testdata/" + fileName);
            
            if (inputStream == null) {
                throw new RuntimeException("Test data file not found: testdata/" + fileName);
            }
            
            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode testCaseNode = rootNode.get(testCaseName);
            
            if (testCaseNode == null) {
                throw new RuntimeException("Test case not found in JSON: " + testCaseName);
            }
            
            SearchTestData testData = objectMapper.treeToValue(testCaseNode, SearchTestData.class);
            logger.info("Successfully loaded test data from {} for: {}", fileName, testCaseName);
            
            return testData;
            
        } catch (Exception e) {
            logger.error("Failed to load test data from {} for {}: {}", fileName, testCaseName, e.getMessage());
            throw new RuntimeException("Test data loading failed", e);
        }
    }
    
    /**
     * Get available test case names from default test data file
     * @return String[] containing all test case names
     */
    public static String[] getAvailableTestCases() {
        try {
            InputStream inputStream = TestDataProvider.class.getClassLoader()
                    .getResourceAsStream("testdata/agoda_search_data.json");
            
            if (inputStream == null) {
                throw new RuntimeException("Test data file not found: testdata/agoda_search_data.json");
            }
            
            JsonNode rootNode = objectMapper.readTree(inputStream);
            Iterator<String> fieldNames = rootNode.fieldNames();
            
            // Convert iterator to array
            java.util.List<String> testCaseNames = new java.util.ArrayList<>();
            while (fieldNames.hasNext()) {
                String testCaseName = fieldNames.next();
                testCaseNames.add(testCaseName);
                logger.debug("Available test case: {}", testCaseName);
            }
            
            return testCaseNames.toArray(new String[0]);
                    
        } catch (Exception e) {
            logger.error("Failed to get available test cases: {}", e.getMessage());
            return new String[0];
        }
    }
}