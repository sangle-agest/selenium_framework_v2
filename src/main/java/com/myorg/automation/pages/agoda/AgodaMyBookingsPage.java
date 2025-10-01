package com.myorg.automation.pages.agoda;

import com.myorg.automation.pages.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AgodaMyBookingsPage - Page object for Agoda's my bookings page
 * 
 * Handles:
 * - Booking management
 * - Booking details
 * - Cancellation
 * - Modification requests
 */
public class AgodaMyBookingsPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(AgodaMyBookingsPage.class);
    private static final String PAGE_NAME = "agoda_my_bookings";
    
    public AgodaMyBookingsPage() {
        super(PAGE_NAME);
    }
    
    /**
     * View booking details by index
     * @param bookingIndex The index of the booking
     * @return AgodaMyBookingsPage instance for method chaining
     */
    public AgodaMyBookingsPage viewBookingDetails(int bookingIndex) {
        logger.info("Viewing booking details at index: {}", bookingIndex);
        clickElement("view_booking_" + bookingIndex);
        return this;
    }
    
    /**
     * Cancel booking by index
     * @param bookingIndex The index of the booking
     * @return AgodaMyBookingsPage instance for method chaining
     */
    public AgodaMyBookingsPage cancelBooking(int bookingIndex) {
        logger.info("Canceling booking at index: {}", bookingIndex);
        clickElement("cancel_booking_" + bookingIndex);
        return this;
    }
    
    /**
     * Get booking reference number by index
     * @param bookingIndex The index of the booking
     * @return The booking reference number
     */
    public String getBookingReference(int bookingIndex) {
        String reference = getElementText("booking_reference_" + bookingIndex);
        logger.debug("Booking reference at index {}: {}", bookingIndex, reference);
        return reference;
    }
    
    /**
     * Get hotel name by booking index
     * @param bookingIndex The index of the booking
     * @return The hotel name
     */
    public String getHotelName(int bookingIndex) {
        String hotelName = getElementText("hotel_name_" + bookingIndex);
        logger.debug("Hotel name at index {}: {}", bookingIndex, hotelName);
        return hotelName;
    }
    
    /**
     * Get booking status by index
     * @param bookingIndex The index of the booking
     * @return The booking status
     */
    public String getBookingStatus(int bookingIndex) {
        String status = getElementText("booking_status_" + bookingIndex);
        logger.debug("Booking status at index {}: {}", bookingIndex, status);
        return status;
    }
    
    /**
     * Check if bookings are loaded
     * @return true if bookings are loaded, false otherwise
     */
    public boolean areBookingsLoaded() {
        boolean areLoaded = isElementVisible("bookings_container");
        logger.debug("Bookings loaded: {}", areLoaded);
        return areLoaded;
    }
}