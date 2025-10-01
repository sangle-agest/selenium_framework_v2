package com.myorg.automation.pages.agoda;

import com.myorg.automation.pages.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AgodaBookingConfirmationPage - Page object for Agoda's booking confirmation page
 * 
 * Handles:
 * - Booking confirmation details
 * - Confirmation number
 * - Email confirmation
 * - Booking summary
 */
public class AgodaBookingConfirmationPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(AgodaBookingConfirmationPage.class);
    private static final String PAGE_NAME = "agoda_booking_confirmation";
    
    public AgodaBookingConfirmationPage() {
        super(PAGE_NAME);
    }
    
    /**
     * Get booking confirmation number
     * @return The confirmation number
     */
    public String getConfirmationNumber() {
        String confirmationNumber = getElementText("confirmation_number");
        logger.info("Booking confirmation number: {}", confirmationNumber);
        return confirmationNumber;
    }
    
    /**
     * Get hotel name from confirmation
     * @return The hotel name
     */
    public String getHotelName() {
        String hotelName = getElementText("hotel_name");
        logger.debug("Confirmed hotel name: {}", hotelName);
        return hotelName;
    }
    
    /**
     * Get check-in date from confirmation
     * @return The check-in date
     */
    public String getCheckInDate() {
        String checkInDate = getElementText("checkin_date");
        logger.debug("Confirmed check-in date: {}", checkInDate);
        return checkInDate;
    }
    
    /**
     * Get check-out date from confirmation
     * @return The check-out date
     */
    public String getCheckOutDate() {
        String checkOutDate = getElementText("checkout_date");
        logger.debug("Confirmed check-out date: {}", checkOutDate);
        return checkOutDate;
    }
    
    /**
     * Get total amount from confirmation
     * @return The total amount
     */
    public String getTotalAmount() {
        String totalAmount = getElementText("total_amount");
        logger.debug("Confirmed total amount: {}", totalAmount);
        return totalAmount;
    }
    
    /**
     * Print booking confirmation
     * @return AgodaBookingConfirmationPage instance for method chaining
     */
    public AgodaBookingConfirmationPage printConfirmation() {
        logger.info("Printing booking confirmation");
        clickElement("print_button");
        return this;
    }
    
    /**
     * Email booking confirmation
     * @return AgodaBookingConfirmationPage instance for method chaining
     */
    public AgodaBookingConfirmationPage emailConfirmation() {
        logger.info("Emailing booking confirmation");
        clickElement("email_button");
        return this;
    }
    
    /**
     * Return to home page
     * @return AgodaHomePage instance
     */
    public AgodaHomePage returnToHome() {
        logger.info("Returning to home page");
        clickElement("home_button");
        return new AgodaHomePage();
    }
    
    /**
     * Check if confirmation details are loaded
     * @return true if details are loaded, false otherwise
     */
    public boolean isConfirmationLoaded() {
        boolean isLoaded = isElementVisible("confirmation_details");
        logger.debug("Confirmation details loaded: {}", isLoaded);
        return isLoaded;
    }
}