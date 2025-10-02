package com.myorg.automation.pages.agoda;

import com.myorg.automation.pages.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.title;

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
     * Verify if Agoda homepage is displayed
     * Checks both page title and search box visibility
     * @return true if homepage is properly displayed, false otherwise
     */
    public boolean isDisplayed() {
        logger.info("Verifying Agoda homepage is displayed");
        
        try {
            // Check page title contains "Agoda Official Site"
            String pageTitle = title();
            boolean titleCorrect = pageTitle != null && pageTitle.contains("Agoda Official Site");
            logger.debug("Page title: '{}', contains 'Agoda Official Site': {}", pageTitle, titleCorrect);
            
            // Check search box is visible
            boolean searchBoxVisible = isElementVisible("searchBox");
            logger.debug("Search box visible: {}", searchBoxVisible);
            
            boolean isDisplayed = titleCorrect && searchBoxVisible;
            logger.info("Homepage displayed: {} (title: {}, searchBox: {})", isDisplayed, titleCorrect, searchBoxVisible);
            
            return isDisplayed;
            
        } catch (Exception e) {
            logger.error("Error verifying homepage display: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Enter destination in the search box
     * @param destination The destination to search for
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage enterDestination(String destination) {
        logger.info("Entering destination: {}", destination);
        clickAndEnter("searchBox", destination);
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
        boolean isVisible = isElementVisible("destinationSuggestions");
        logger.debug("Destination suggestions visible: {}", isVisible);
        return isVisible;
    }
    
    /**
     * Click on check-in date box
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage openCheckInDatePicker() {
        logger.info("Opening check-in date picker");
        clickElement("checkInDatePicker");
        return this;
    }
    
    /**
     * Click on check-out date box
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage openCheckOutDatePicker() {
        logger.info("Opening check-out date picker");
        clickElement("checkOutDatePicker");
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
     * Decrease adult count - Note: Element not defined in JSON yet
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage decreaseAdultCount() {
        logger.info("Decreasing adult count");
        // clickElement("adult_minus_button"); // TODO: Add to JSON
        logger.warn("Element 'adult_minus_button' not defined in JSON");
        return this;
    }

    /**
     * Increase children count - Note: Element not defined in JSON yet
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage increaseChildrenCount() {
        logger.info("Increasing children count");
        // clickElement("children_plus_button"); // TODO: Add to JSON
        logger.warn("Element 'children_plus_button' not defined in JSON");
        return this;
    }

    /**
     * Decrease children count - Note: Element not defined in JSON yet
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage decreaseChildrenCount() {
        logger.info("Decreasing children count");
        // clickElement("children_minus_button"); // TODO: Add to JSON
        logger.warn("Element 'children_minus_button' not defined in JSON");
        return this;
    }    /**
     * Increase room count
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage increaseRoomCount() {
        logger.info("Increasing room count");
        clickElement("roomPlusButton");
        return this;
    }
    
    /**
     * Decrease room count - Note: Element not defined in JSON yet
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage decreaseRoomCount() {
        logger.info("Decreasing room count");
        // clickElement("room_minus_button"); // TODO: Add to JSON
        logger.warn("Element 'room_minus_button' not defined in JSON");
        return this;
    }
    
    /**
     * Confirm guest and room selection
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage confirmGuestSelection() {
        logger.info("Confirming guest and room selection");
        clickElement("applyButton");
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
        String destination = getElementText("searchBox");
        logger.debug("Current destination: {}", destination);
        return destination;
    }
    
    /**
     * Get current check-in date text - Note: Using date picker element
     * @return The check-in date text
     */
    public String getCheckInDateText() {
        String checkInDate = getElementText("checkInDatePicker");
        logger.debug("Current check-in date: {}", checkInDate);
        return checkInDate;
    }

    /**
     * Get current check-out date text - Note: Using date picker element  
     * @return The check-out date text
     */
    public String getCheckOutDateText() {
        String checkOutDate = getElementText("checkOutDatePicker");
        logger.debug("Current check-out date: {}", checkOutDate);
        return checkOutDate;
    }

    /**
     * Get current guest configuration text - Note: Using occupancy selector
     * @return The guest configuration text
     */
    public String getGuestConfigurationText() {
        String guestConfig = getElementText("guestsAndRoomsSelector");
        logger.debug("Current guest configuration: {}", guestConfig);
        return guestConfig;
    }    /**
     * Check if search button is enabled
     * @return true if search button is enabled, false otherwise
     */
    public boolean isSearchButtonEnabled() {
        boolean isEnabled = isElementEnabled("searchButton");
        logger.debug("Search button enabled: {}", isEnabled);
        return isEnabled;
    }
    
    /**
     * Check if date picker is open - Note: Element not defined in JSON yet
     * @return true if date picker is open, false otherwise
     */
    public boolean isDatePickerOpen() {
        // boolean isOpen = isElementVisible("date_picker_calendar"); // TODO: Add to JSON
        logger.warn("Element 'date_picker_calendar' not defined in JSON");
        return false; // Default return for now
    }

    /**
     * Check if guest selector is open - Note: Element not defined in JSON yet
     * @return true if guest selector is open, false otherwise
     */
    public boolean isGuestSelectorOpen() {
        // boolean isOpen = isElementVisible("guest_selector_panel"); // TODO: Add to JSON
        logger.warn("Element 'guest_selector_panel' not defined in JSON");
        return false; // Default return for now
    }

    /**
     * Check if calendar is displayed after destination selection
     * @return true if calendar is visible, false otherwise
     */
    public boolean isCalendarDisplayed() {
        logger.info("Checking if calendar is displayed");
        boolean isDisplayed = isElementVisible("calendarButton");
        logger.debug("Calendar displayed: {}", isDisplayed);
        return isDisplayed;
    }

    /**
     * Select check-in date from the calendar
     * @param date The check-in date to select
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage selectCheckInDate(String date) {
        logger.info("Selecting check-in date: {}", date);
        
        try {
            // Click on check-in date selector to open calendar
            clickElement("checkInDateSelector");
            
            // Wait a moment for calendar to open
            Thread.sleep(1000);
            
            // For now, we'll use a simple approach - in real implementation,
            // you would need to navigate the calendar based on the date
            // This is a placeholder that clicks a generic date selector
            logger.info("Date selection logic would be implemented here for date: {}", date);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted while selecting check-in date");
        }
        
        return this;
    }

    /**
     * Select check-out date from the calendar
     * @param date The check-out date to select
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage selectCheckOutDate(String date) {
        logger.info("Selecting check-out date: {}", date);
        
        try {
            // Click on check-out date selector
            clickElement("checkOutDateSelector");
            
            // Wait a moment for calendar to open
            Thread.sleep(1000);
            
            // For now, we'll use a simple approach - in real implementation,
            // you would need to navigate the calendar based on the date
            logger.info("Date selection logic would be implemented here for date: {}", date);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted while selecting check-out date");
        }
        
        return this;
    }

    /**
     * Configure occupancy settings (rooms, adults, children)
     * @param rooms Number of rooms
     * @param adults Number of adults
     * @param children Number of children
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage configureOccupancy(int rooms, int adults, int children) {
        logger.info("Configuring occupancy - Rooms: {}, Adults: {}, Children: {}", rooms, adults, children);
        
        try {
            // Open occupancy panel first
            clickElement("occupancyPanel");
            Thread.sleep(1000);
            
            // Configure rooms
            int currentRooms = getCurrentRoomCount();
            while (currentRooms < rooms) {
                clickElement("roomPlusButton");
                currentRooms++;
                Thread.sleep(500);
            }
            
            // Configure adults
            int currentAdults = getCurrentAdultCount();
            while (currentAdults < adults) {
                clickElement("adultPlusButton");
                currentAdults++;
                Thread.sleep(500);
            }
            
            // Configure children
            int currentChildren = getCurrentChildrenCount();
            while (currentChildren < children) {
                clickElement("childrenPlusButton");
                currentChildren++;
                Thread.sleep(500);
            }
            
            logger.info("Occupancy configuration completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted while configuring occupancy");
        }
        
        return this;
    }

    /**
     * Get current room count from the occupancy display
     * @return current number of rooms
     */
    private int getCurrentRoomCount() {
        try {
            String roomText = getElementText("roomValue");
            return Integer.parseInt(roomText.trim());
        } catch (Exception e) {
            logger.warn("Could not get current room count, defaulting to 1");
            return 1;
        }
    }

    /**
     * Get current adult count from the occupancy display
     * @return current number of adults
     */
    private int getCurrentAdultCount() {
        try {
            String adultText = getElementText("adultValue");
            return Integer.parseInt(adultText.trim());
        } catch (Exception e) {
            logger.warn("Could not get current adult count, defaulting to 2");
            return 2;
        }
    }

    /**
     * Get current children count from the occupancy display
     * @return current number of children
     */
    private int getCurrentChildrenCount() {
        try {
            String childrenText = getElementText("childrenValue");
            return Integer.parseInt(childrenText.trim());
        } catch (Exception e) {
            logger.warn("Could not get current children count, defaulting to 0");
            return 0;
        }
    }

    /**
     * Execute the final search after all parameters are set
     * @return AgodaHomePage instance for method chaining
     */
    public AgodaHomePage executeSearch() {
        logger.info("Executing final search");
        try {
            clickElement("searchButtonFinal");
            Thread.sleep(2000); // Wait for search results to load
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted while executing search");
        }
        return this;
    }

    /**
     * Navigate to sign in page - Note: Element not defined in JSON yet
     * @return AgodaSignInPage instance
     */
    /*
    public AgodaSignInPage goToSignIn() {
        logger.info("Navigating to sign in page");
        clickElement("signin_link"); // TODO: Add to JSON
        return new AgodaSignInPage();
    }
    */

    /**
     * Open user menu - Note: Element not defined in JSON yet
     * @return AgodaHomePage instance for method chaining
     */
    /*
    public AgodaHomePage openUserMenu() {
        logger.info("Opening user menu");
        clickElement("user_menu"); // TODO: Add to JSON
        return this;
    }
    */

    /**
     * Navigate to my bookings - Note: Element not defined in JSON yet
     * @return AgodaMyBookingsPage instance
     */
    /*
    public AgodaMyBookingsPage goToMyBookings() {
        logger.info("Navigating to my bookings");
        clickElement("my_bookings_link"); // TODO: Add to JSON
        return new AgodaMyBookingsPage();
    }
    */

    /**
     * Change language - Note: Element not defined in JSON yet
     * @param language The language to select
     * @return AgodaHomePage instance for method chaining
     */
    /*
    public AgodaHomePage changeLanguage(String language) {
        logger.info("Changing language to: {}", language);
        clickElement("language_selector"); // TODO: Add to JSON
        // Note: This would need dynamic locator handling for specific language
        return this;
    }
    */

    /**
     * Change currency - Note: Element not defined in JSON yet
     * @param currency The currency to select
     * @return AgodaHomePage instance for method chaining
     */
    /*
    public AgodaHomePage changeCurrency(String currency) {
        logger.info("Changing currency to: {}", currency);
        clickElement("currency_selector"); // TODO: Add to JSON
        // Note: This would need dynamic locator handling for specific currency
        return this;
    }
    */
}