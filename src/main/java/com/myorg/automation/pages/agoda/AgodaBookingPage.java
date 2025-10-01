package com.myorg.automation.pages.agoda;

import com.myorg.automation.pages.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AgodaBookingPage - Page object for Agoda's booking page
 * 
 * Handles:
 * - Guest information
 * - Payment details
 * - Booking confirmation
 * - Special requests
 */
public class AgodaBookingPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(AgodaBookingPage.class);
    private static final String PAGE_NAME = "agoda_booking";
    
    public AgodaBookingPage() {
        super(PAGE_NAME);
    }
    
    /**
     * Enter guest first name
     * @param firstName The first name to enter
     * @return AgodaBookingPage instance for method chaining
     */
    public AgodaBookingPage enterFirstName(String firstName) {
        logger.info("Entering first name: {}", firstName);
        typeText("first_name_input", firstName);
        return this;
    }
    
    /**
     * Enter guest last name
     * @param lastName The last name to enter
     * @return AgodaBookingPage instance for method chaining
     */
    public AgodaBookingPage enterLastName(String lastName) {
        logger.info("Entering last name: {}", lastName);
        typeText("last_name_input", lastName);
        return this;
    }
    
    /**
     * Enter guest email
     * @param email The email to enter
     * @return AgodaBookingPage instance for method chaining
     */
    public AgodaBookingPage enterEmail(String email) {
        logger.info("Entering email: {}", email);
        typeText("email_input", email);
        return this;
    }
    
    /**
     * Enter guest phone number
     * @param phoneNumber The phone number to enter
     * @return AgodaBookingPage instance for method chaining
     */
    public AgodaBookingPage enterPhoneNumber(String phoneNumber) {
        logger.info("Entering phone number: {}", phoneNumber);
        typeText("phone_input", phoneNumber);
        return this;
    }
    
    /**
     * Enter special requests
     * @param requests The special requests to enter
     * @return AgodaBookingPage instance for method chaining
     */
    public AgodaBookingPage enterSpecialRequests(String requests) {
        logger.info("Entering special requests: {}", requests);
        typeText("special_requests_input", requests);
        return this;
    }
    
    /**
     * Complete booking
     * @return AgodaBookingConfirmationPage instance
     */
    public AgodaBookingConfirmationPage completeBooking() {
        logger.info("Completing booking");
        clickElement("complete_booking_button");
        return new AgodaBookingConfirmationPage();
    }
    
    /**
     * Fill guest information
     * @param firstName The first name
     * @param lastName The last name
     * @param email The email
     * @param phoneNumber The phone number
     * @return AgodaBookingPage instance for method chaining
     */
    public AgodaBookingPage fillGuestInformation(String firstName, String lastName, String email, String phoneNumber) {
        logger.info("Filling guest information for: {} {}", firstName, lastName);
        return enterFirstName(firstName)
                .enterLastName(lastName)
                .enterEmail(email)
                .enterPhoneNumber(phoneNumber);
    }
    
    /**
     * Get booking total amount
     * @return The total booking amount
     */
    public String getTotalAmount() {
        String total = getElementText("total_amount");
        logger.debug("Total booking amount: {}", total);
        return total;
    }
    
    /**
     * Check if complete booking button is enabled
     * @return true if button is enabled, false otherwise
     */
    public boolean isCompleteBookingButtonEnabled() {
        boolean isEnabled = isElementEnabled("complete_booking_button");
        logger.debug("Complete booking button enabled: {}", isEnabled);
        return isEnabled;
    }
}