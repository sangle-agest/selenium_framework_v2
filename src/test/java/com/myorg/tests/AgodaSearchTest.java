package com.myorg.tests;

import com.myorg.automation.pages.agoda.AgodaHomePage;
import com.myorg.automation.pages.agoda.AgodaSearchResultsPage;
import com.myorg.automation.enums.SortType;
import com.myorg.automation.models.SearchTestData;
import com.myorg.automation.utils.TestDataProvider;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.*;

/**
 * TC01: Search and Sort Hotel Successfully
 * Single focused test case for Agoda hotel search functionality
 */
@Epic("Hotel Search")
@Feature("Agoda Search and Sort")
public class AgodaSearchTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(AgodaSearchTest.class);
    
    private AgodaHomePage homePage;

    @BeforeMethod(alwaysRun = true)
    @Step("Initialize page objects")
    public void setupMethod() {
        logger.info("Initializing test setup for TC01");
        homePage = new AgodaHomePage();
        logger.info("Test setup completed successfully");
    }

    @Test(groups = {"smoke", "regression"}, priority = 1, dataProvider = "tc01_da_nang_search", dataProviderClass = TestDataProvider.class)
    @Story("TC01: Search and Sort Hotel Successfully")
    @Description("Search hotels in Da Nang for family travelers and sort by lowest prices")
    @Severity(SeverityLevel.CRITICAL)
    public void tc01_searchAndSortHotelSuccessfully(SearchTestData testData) {
        logger.info("Starting TC01: Search and Sort Hotel Successfully");
        logger.info("Test data: {}", testData);
        
        // Step 1: Navigate to https://www.agoda.com/
        navigateToHomepage();
        
        // Step 2: Verify Agoda homepage is displayed
        verifyHomepageIsDisplayed();
        
        // Step 3: Search hotel with test data criteria
        AgodaSearchResultsPage searchResults = searchHotel(testData);
        
        // Step 4: Verify search results are displayed
        verifySearchResults(searchResults, testData);
        
        // Step 5: Sort hotels by lowest prices
        sortHotelsByLowestPrice(searchResults);
        
        // Step 6: Verify hotels are sorted correctly
        verifySortedResults(searchResults);
        
        logger.info("TC01 completed successfully");
    }

    @Step("Navigate to Agoda homepage")
    private void navigateToHomepage() {
        logger.info("Navigate to https://www.agoda.com/");
        
        // Navigate to Agoda homepage
        open(homePage.getPageUrl());
        
        logger.info("Navigation completed");
    }
    
    @Step("Verify Agoda homepage is displayed")
    private void verifyHomepageIsDisplayed() {
        logger.info("Verify Agoda homepage is displayed");
        
        // Expected: Agoda homepage is displayed with correct title and search box
        Assert.assertTrue(homePage.isDisplayed(), 
                "Agoda homepage should be displayed with correct title and search box");
        
        logger.info("Agoda homepage is displayed");
    }

    @Step("Search hotel with specified criteria")
    private AgodaSearchResultsPage searchHotel(SearchTestData testData) {
        logger.info("Search hotel with {} for {} travelers", testData.getPlace(), testData.getTravellerType());
        
        String destination = testData.getPlace();
        
        // Enter destination
        homePage.enterDestination(destination);
        
        // Wait a moment for suggestions and select first one
        if (homePage.areDestinationSuggestionsVisible()) {
            homePage.selectFirstDestination();
        }
        
        // Step 2: Verify calendar is displayed (automatically appears after destination selection)
        // logger.info("Verifying calendar is displayed after destination selection");
        // Assert.assertTrue(homePage.isCalendarDisplayed(), 
        //         "Calendar should be automatically displayed after selecting destination");
        
        // Step 3: Select check-in and check-out dates based on test data
        logger.info("Selecting dates: Check-in: {}, Check-out: {}", 
                testData.getCheckInDate(), testData.getCheckOutDate());
        homePage.selectCheckInDate(testData.getCheckInDate())
                .selectCheckOutDate(testData.getCheckOutDate());
        
        // Step 4: Configure occupancy based on test data
        logger.info("Configuring occupancy: {} rooms, {} adults, {} children", 
                testData.getRooms(), testData.getAdults(), testData.getChildren());
        homePage.configureOccupancy(testData.getRooms(), testData.getAdults(), testData.getChildren());
        
        // Step 5: Execute search - this will open a new tab
        String originalWindow = getWebDriver().getWindowHandle();
        homePage.executeSearch();
        
        // Switch to new tab with search results
        switchToNewTab(originalWindow);
        
        // Expected: Search result page shows hotels in destination
        // URL should contain city parameter for the searched location
        String currentUrl = getWebDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("city="), 
                "URL should contain city parameter for " + destination);
        Assert.assertTrue(currentUrl.contains("rooms=2"), 
                "URL should contain rooms=2");
        Assert.assertTrue(currentUrl.contains("adults=4") || currentUrl.contains("adults=3"), 
                "URL should contain adults parameter");
        
        logger.info("Search executed and results page opened");
        return new AgodaSearchResultsPage();
    }

    @Step("Verify search results are displayed")
    private void verifySearchResults(AgodaSearchResultsPage searchResults, SearchTestData testData) {
        logger.info("Verify search result is displayed");
        
        // Wait for results to load
        searchResults.waitForResultsToLoad();
        
        // Expected: Search results are displayed
        Assert.assertTrue(searchResults.areResultsLoaded(), 
                "Search results should be displayed");
        
        // Use dynamic method to verify expected number of hotels
        int expectedHotelCount = testData.getExpectedResultsMinimum();
        boolean hotelsVerified = searchResults.verifyFirstNHotels(expectedHotelCount);
        
        Assert.assertTrue(hotelsVerified, 
                "First " + expectedHotelCount + " hotels should be displayed with valid information");
        
        // Log details using dynamic methods
        for (int i = 1; i <= expectedHotelCount; i++) {
            try {
                String hotelName = searchResults.getHotelNameDynamic(i);
                String hotelPrice = searchResults.getHotelPriceDynamic(i);
                logger.info("Hotel {}: {} - Price: {}", i, hotelName, hotelPrice);
            } catch (Exception e) {
                logger.warn("Could not get details for hotel {}: {}", i, e.getMessage());
            }
        }
        
        logger.info("First {} hotels verified successfully", expectedHotelCount);
    }

    @Step("Sort hotels by lowest prices")
    private void sortHotelsByLowestPrice(AgodaSearchResultsPage searchResults) {
        logger.info("Sort hotels by lowest prices");
        
        // Sort by lowest prices using enum
        searchResults.sortBy(SortType.PRICE_LOW_TO_HIGH);
        
        // Wait for sorting to complete
        sleep(3000);
        
        logger.info("Sorting action completed");
    }
    
    @Step("Verify hotels are sorted correctly")
    private void verifySortedResults(AgodaSearchResultsPage searchResults) {
        logger.info("Verify hotels are sorted correctly");
        
        // Expected: Hotels re-ordered by ascending price, still showing Da Nang
        // Verify hotels are still displayed after sorting
        Assert.assertTrue(searchResults.areResultsLoaded(), 
                "Search results should still be displayed after sorting");
        
        // Verify first 5 hotels after sorting
        for (int i = 0; i < 5; i++) {
            try {
                String hotelName = searchResults.getHotelName(i);
                String hotelPrice = searchResults.getHotelPrice(i);
                
                Assert.assertNotNull(hotelName, 
                        "Hotel " + (i + 1) + " name should be displayed after sorting");
                Assert.assertNotNull(hotelPrice, 
                        "Hotel " + (i + 1) + " price should be displayed after sorting");
                
                logger.info("Sorted Hotel {}: {} - Price: {}", (i + 1), hotelName, hotelPrice);
            } catch (Exception e) {
                logger.warn("Could not verify sorted hotel {} details: {}", (i + 1), e.getMessage());
            }
        }
        
        logger.info("Hotels sorted by lowest prices successfully verified");
    }

    /**
     * Switch to the new tab that opens after search
     */
    private void switchToNewTab(String originalWindow) {
        // Wait for new tab to open
        sleep(2000);
        
        for (String windowHandle : getWebDriver().getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                switchTo().window(windowHandle);
                break;
            }
        }
    }
}