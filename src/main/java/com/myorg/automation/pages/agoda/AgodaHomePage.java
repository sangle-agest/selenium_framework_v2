package com.myorg.automation.pages.agoda;

import com.myorg.automation.pages.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.*;

/**
 * AgodaHomePage - Page object for Agoda's homepage
 * 
 * Handles:
 * - Hotel search functionality
 * - Location input
 * - Date selection
 * - Guest configuration
 * - Search execution
 * - Navigation elements
 */
public class AgodaHomePage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(AgodaHomePage.class);
    private static final String PAGE_NAME = "AgodaHomePage";
    
    public AgodaHomePage() {
        super(PAGE_NAME);
    }
    
    /**
     * Enter destination in the search box
     * @param destination The destination to search for
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage enterDestination(String destination) {
        logger.info("Entering destination: {}", destination);
        typeText("searchBox", destination);
        return this;
    }
    
    /**
     * Select first destination suggestion from dropdown
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage selectFirstDestination() {
        logger.info("Selecting first destination suggestion");
        
        // Wait for suggestions to appear
        waitForElementVisible("destinationSuggestions");
        
        // Click first suggestion
        clickElement("firstDestinationSuggestion");
        return this;
    }
    
    /**
     * Check if destination suggestions are visible
     * @return true if suggestions are visible, false otherwise
     */
    public boolean areDestinationSuggestionsVisible() {
        boolean isVisible = isElementVisible("destination_suggestions_list");
        logger.debug("Destination suggestions visible: {}", isVisible);
        return isVisible;
    }
    
    /**
     * Click on check-in date box
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage openCheckInDatePicker() {
        logger.info("Opening check-in date picker");
        clickElement("checkin_date_box");
        return this;
    }
    
    /**
     * Click on check-out date box
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage openCheckOutDatePicker() {
        logger.info("Opening check-out date picker");
        clickElement("checkout_date_box");
        return this;
    }
    
    /**
     * Click on occupancy box to open guest selector
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage openOccupancySelector() {
        logger.info("Opening occupancy selector");
        clickElement("guestsAndRoomsSelector");
        return this;
    }
    
    /**
     * Increase adult count
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage increaseAdultCount() {
        logger.info("Increasing adult count");
        clickElement("adultPlusButton");
        return this;
    }
    
    /**
     * Decrease adult count
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage decreaseAdultCount() {
        logger.info("Decreasing adult count");
        clickElement("adult_minus_button");
        return this;
    }
    
    /**
     * Increase children count
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage increaseChildrenCount() {
        logger.info("Increasing children count");
        clickElement("children_plus_button");
        return this;
    }
    
    /**
     * Decrease children count
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage decreaseChildrenCount() {
        logger.info("Decreasing children count");
        clickElement("children_minus_button");
        return this;
    }
    
    /**
     * Increase room count
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage increaseRoomCount() {
        logger.info("Increasing room count");
        clickElement("room_plus_button");
        return this;
    }
    
    /**
     * Decrease room count
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage decreaseRoomCount() {
        logger.info("Decreasing room count");
        clickElement("room_minus_button");
        return this;
    }
    
    /**
     * Confirm guest and room selection
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage confirmGuestSelection() {
        logger.info("Confirming guest and room selection");
        clickElement("guest_confirm_button");
        return this;
    }
    
    /**
     * Execute the hotel search
     * @return AgodaSearchResultsPage instance
     */
    public AgodaSearchResultsPage searchHotels() {
        logger.info("Executing hotel search");
        clickElement("searchButton");
        return new AgodaSearchResultsPage();
    }
    
    /**
     * Get current destination text
     * @return The destination text
     */
    public String getDestinationText() {
        String destination = getElementText("destination_input");
        logger.debug("Current destination: {}", destination);
        return destination;
    }
    
    /**
     * Get current check-in date text
     * @return The check-in date text
     */
    public String getCheckInDateText() {
        String checkInDate = getElementText("checkin_date_display");
        logger.debug("Current check-in date: {}", checkInDate);
        return checkInDate;
    }
    
    /**
     * Get current check-out date text
     * @return The check-out date text
     */
    public String getCheckOutDateText() {
        String checkOutDate = getElementText("checkout_date_display");
        logger.debug("Current check-out date: {}", checkOutDate);
        return checkOutDate;
    }
    
    /**
     * Get current guest configuration text
     * @return The guest configuration text
     */
    public String getGuestConfigurationText() {
        String guestConfig = getElementText("guest_display");
        logger.debug("Current guest configuration: {}", guestConfig);
        return guestConfig;
    }
    
    /**
     * Check if search button is enabled
     * @return true if search button is enabled, false otherwise
     */
    public boolean isSearchButtonEnabled() {
        boolean isEnabled = isElementEnabled("search_button");
        logger.debug("Search button enabled: {}", isEnabled);
        return isEnabled;
    }
    
    /**
     * Check if date picker is open
     * @return true if date picker is open, false otherwise
     */
    public boolean isDatePickerOpen() {
        boolean isOpen = isElementVisible("date_picker_calendar");
        logger.debug("Date picker open: {}", isOpen);
        return isOpen;
    }
    
    /**
     * Check if guest selector is open
     * @return true if guest selector is open, false otherwise
     */
    public boolean isGuestSelectorOpen() {
        boolean isOpen = isElementVisible("guest_selector_panel");
        logger.debug("Guest selector open: {}", isOpen);
        return isOpen;
    }
    
    /**
     * Navigate to sign in page
     * @return AgodaSignInPage instance
     */
    public AgodaSignInPage goToSignIn() {
        logger.info("Navigating to sign in page");
        clickElement("signin_link");
        return new AgodaSignInPage();
    }
    
    /**
     * Open user menu
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage openUserMenu() {
        logger.info("Opening user menu");
        clickElement("user_menu");
        return this;
    }
    
    /**
     * Navigate to my bookings
     * @return AgodaMyBookingsPage instance
     */
    public AgodaMyBookingsPage goToMyBookings() {
        logger.info("Navigating to my bookings");
        clickElement("my_bookings_link");
        return new AgodaMyBookingsPage();
    }
    
    /**
     * Change language
     * @param language The language to select
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage changeLanguage(String language) {
        logger.info("Changing language to: {}", language);
        clickElement("language_selector");
        // Note: This would need dynamic locator handling for specific language
        return this;
    }
    
    /**
     * Change currency
     * @param currency The currency to select
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage changeCurrency(String currency) {
        logger.info("Changing currency to: {}", currency);
        clickElement("currency_selector");
        // Note: This would need dynamic locator handling for specific currency
        return this;
    }
}