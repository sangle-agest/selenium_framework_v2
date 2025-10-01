package com.myorg.automation.pages.agoda;

import com.myorg.automation.pages.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AgodaSearchResultsPage - Page object for Agoda's search results
 * 
 * Handles:
 * - Hotel filtering
 * - Search results display
 * - Hotel selection
 * - Sorting options
 * - Pagination
 */
public class AgodaSearchResultsPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(AgodaSearchResultsPage.class);
    private static final String PAGE_NAME = "AgodaSearchResultsPage";
    
    public AgodaSearchResultsPage() {
        super(PAGE_NAME);
    }
    
    /**
     * Apply price range filter
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return AgodaSearchResultsPage instance for method chaining
     */
    public AgodaSearchResultsPage filterByPriceRange(String minPrice, String maxPrice) {
        logger.info("Filtering by price range: {} - {}", minPrice, maxPrice);
        typeText("min_price_input", minPrice);
        typeText("max_price_input", maxPrice);
        clickElement("price_filter_apply");
        return this;
    }
    
    /**
     * Filter by star rating
     * @param starRating The star rating to filter by
     * @return AgodaSearchResultsPage instance for method chaining
     */
    public AgodaSearchResultsPage filterByStarRating(String starRating) {
        logger.info("Filtering by star rating: {}", starRating);
        clickElement("star_rating_" + starRating);
        return this;
    }
    
    /**
     * Filter by hotel facilities
     * @param facility The facility to filter by
     * @return AgodaSearchResultsPage instance for method chaining
     */
    public AgodaSearchResultsPage filterByFacility(String facility) {
        logger.info("Filtering by facility: {}", facility);
        clickElement("facility_" + facility.toLowerCase().replace(" ", "_"));
        return this;
    }
    
    /**
     * Sort results by option
     * @param sortOption The sort option (price_low_high, price_high_low, rating, etc.)
     * @return AgodaSearchResultsPage instance for method chaining
     */
    public AgodaSearchResultsPage sortBy(String sortOption) {
        logger.info("Sorting by: {}", sortOption);
        clickElement("sortDropdown");
        
        // Map sort options to element names
        String sortElementName;
        switch (sortOption) {
            case "price_low_high":
                sortElementName = "sortByPriceLowHigh";
                break;
            case "price_high_low":
                sortElementName = "sortByPriceHighLow";
                break;
            case "rating":
                sortElementName = "sortByRating";
                break;
            default:
                sortElementName = "sortByPriceLowHigh";
        }
        
        clickElement(sortElementName);
        return this;
    }
    
    /**
     * Select hotel by index
     * @param hotelIndex The index of the hotel to select (0-based)
     * @return AgodaHotelDetailsPage instance
     */
    public AgodaHotelDetailsPage selectHotelByIndex(int hotelIndex) {
        logger.info("Selecting hotel at index: {}", hotelIndex);
        clickElement("hotel_result_" + hotelIndex);
        return new AgodaHotelDetailsPage();
    }
    
    /**
     * Select first hotel in results
     * @return AgodaHotelDetailsPage instance
     */
    public AgodaHotelDetailsPage selectFirstHotel() {
        logger.info("Selecting first hotel in results");
        clickElement("first_hotel_result");
        return new AgodaHotelDetailsPage();
    }
    
    /**
     * Get hotel name by index using dynamic locator
     * @param hotelIndex The index of the hotel (1-based for XPath)
     * @return The hotel name
     */
    public String getHotelNameDynamic(int hotelIndex) {
        String hotelName = getElementTextDynamic("hotelName", String.valueOf(hotelIndex));
        logger.debug("Hotel name at index {}: {}", hotelIndex, hotelName);
        return hotelName;
    }
    
    /**
     * Get hotel price by index using dynamic locator
     * @param hotelIndex The index of the hotel (1-based for XPath)
     * @return The hotel price
     */
    public String getHotelPriceDynamic(int hotelIndex) {
        String price = getElementTextDynamic("hotelPrice", String.valueOf(hotelIndex));
        logger.debug("Hotel price at index {}: {}", hotelIndex, price);
        return price;
    }
    
    /**
     * Get hotel rating by index using dynamic locator
     * @param hotelIndex The index of the hotel (1-based for XPath)
     * @return The hotel rating
     */
    public String getHotelRatingDynamic(int hotelIndex) {
        String rating = getElementTextDynamic("hotelRating", String.valueOf(hotelIndex));
        logger.debug("Hotel rating at index {}: {}", hotelIndex, rating);
        return rating;
    }
    
    /**
     * Verify first N hotels are displayed and have valid information
     * @param expectedCount The number of hotels to verify
     * @return true if all hotels have names and prices, false otherwise
     */
    public boolean verifyFirstNHotels(int expectedCount) {
        logger.info("Verifying first {} hotels are displayed", expectedCount);
        
        for (int i = 1; i <= expectedCount; i++) {
            try {
                String hotelName = getHotelNameDynamic(i);
                String hotelPrice = getHotelPriceDynamic(i);
                
                if (hotelName == null || hotelName.trim().isEmpty()) {
                    logger.warn("Hotel name is empty at index {}", i);
                    return false;
                }
                
                if (hotelPrice == null || hotelPrice.trim().isEmpty()) {
                    logger.warn("Hotel price is empty at index {}", i);
                    return false;
                }
                
                logger.debug("Hotel {} verified: {} - {}", i, hotelName, hotelPrice);
            } catch (Exception e) {
                logger.error("Failed to verify hotel at index {}: {}", i, e.getMessage());
                return false;
            }
        }
        
        logger.info("Successfully verified {} hotels", expectedCount);
        return true;
    }
    
    /**
     * Get hotel name by index
     * @param hotelIndex The index of the hotel
     * @return The hotel name
     */
    public String getHotelName(int hotelIndex) {
        String hotelName = getElementText("hotel_name_" + hotelIndex);
        logger.debug("Hotel name at index {}: {}", hotelIndex, hotelName);
        return hotelName;
    }
    
    /**
     * Get hotel price by index
     * @param hotelIndex The index of the hotel
     * @return The hotel price
     */
    public String getHotelPrice(int hotelIndex) {
        String price = getElementText("hotel_price_" + hotelIndex);
        logger.debug("Hotel price at index {}: {}", hotelIndex, price);
        return price;
    }
    
    /**
     * Get hotel rating by index
     * @param hotelIndex The index of the hotel
     * @return The hotel rating
     */
    public String getHotelRating(int hotelIndex) {
        String rating = getElementText("hotel_rating_" + hotelIndex);
        logger.debug("Hotel rating at index {}: {}", hotelIndex, rating);
        return rating;
    }
    
    /**
     * Get total number of search results
     * @return The number of search results
     */
    public String getTotalResults() {
        String totalResults = getElementText("total_results_count");
        logger.debug("Total search results: {}", totalResults);
        return totalResults;
    }
    
    /**
     * Go to next page of results
     * @return AgodaSearchResultsPage instance for method chaining
     */
    public AgodaSearchResultsPage goToNextPage() {
        logger.info("Going to next page of results");
        clickElement("next_page_button");
        return this;
    }
    
    /**
     * Go to previous page of results
     * @return AgodaSearchResultsPage instance for method chaining
     */
    public AgodaSearchResultsPage goToPreviousPage() {
        logger.info("Going to previous page of results");
        clickElement("previous_page_button");
        return this;
    }
    
    /**
     * Go to specific page number
     * @param pageNumber The page number to navigate to
     * @return AgodaSearchResultsPage instance for method chaining
     */
    public AgodaSearchResultsPage goToPage(int pageNumber) {
        logger.info("Going to page: {}", pageNumber);
        clickElement("page_number_" + pageNumber);
        return this;
    }
    
    /**
     * Clear all filters
     * @return AgodaSearchResultsPage instance for method chaining
     */
    public AgodaSearchResultsPage clearAllFilters() {
        logger.info("Clearing all filters");
        clickElement("clear_all_filters");
        return this;
    }
    
    /**
     * Check if next page button is enabled
     * @return true if next page button is enabled, false otherwise
     */
    public boolean isNextPageEnabled() {
        boolean isEnabled = isElementEnabled("next_page_button");
        logger.debug("Next page button enabled: {}", isEnabled);
        return isEnabled;
    }
    
    /**
     * Check if previous page button is enabled
     * @return true if previous page button is enabled, false otherwise
     */
    public boolean isPreviousPageEnabled() {
        boolean isEnabled = isElementEnabled("previous_page_button");
        logger.debug("Previous page button enabled: {}", isEnabled);
        return isEnabled;
    }
    
    /**
     * Check if search results are loaded
     * @return true if results are loaded, false otherwise
     */
    public boolean areResultsLoaded() {
        boolean areLoaded = isElementVisible("searchResultsContainer");
        logger.debug("Search results loaded: {}", areLoaded);
        return areLoaded;
    }
    
    /**
     * Wait for search results to load
     * @return AgodaSearchResultsPage instance for method chaining
     */
    public AgodaSearchResultsPage waitForResultsToLoad() {
        logger.info("Waiting for search results to load");
        waitForElementVisible("searchResultsContainer");
        return this;
    }
}