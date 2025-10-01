package com.myorg.automation.pages.agoda;

import com.myorg.automation.pages.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AgodaHotelDetailsPage - Page object for Agoda's hotel details page
 * 
 * Handles:
 * - Hotel information display
 * - Room selection
 * - Booking process
 * - Hotel amenities
 * - Reviews and ratings
 */
public class AgodaHotelDetailsPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(AgodaHotelDetailsPage.class);
    private static final String PAGE_NAME = "agoda_hotel_details";
    
    public AgodaHotelDetailsPage() {
        super(PAGE_NAME);
    }
    
    /**
     * Select room type
     * @param roomIndex The index of the room to select
     * @return AgodaHotelDetailsPage instance for method chaining
     */
    public AgodaHotelDetailsPage selectRoom(int roomIndex) {
        logger.info("Selecting room at index: {}", roomIndex);
        clickElement("room_select_" + roomIndex);
        return this;
    }
    
    /**
     * Select first available room
     * @return AgodaHotelDetailsPage instance for method chaining
     */
    public AgodaHotelDetailsPage selectFirstRoom() {
        logger.info("Selecting first available room");
        clickElement("first_room_select");
        return this;
    }
    
    /**
     * Proceed to booking
     * @return AgodaBookingPage instance
     */
    public AgodaBookingPage proceedToBooking() {
        logger.info("Proceeding to booking");
        clickElement("book_now_button");
        return new AgodaBookingPage();
    }
    
    /**
     * View hotel photos
     * @return AgodaHotelDetailsPage instance for method chaining
     */
    public AgodaHotelDetailsPage viewHotelPhotos() {
        logger.info("Viewing hotel photos");
        clickElement("view_photos_button");
        return this;
    }
    
    /**
     * Read reviews
     * @return AgodaHotelDetailsPage instance for method chaining
     */
    public AgodaHotelDetailsPage readReviews() {
        logger.info("Reading hotel reviews");
        clickElement("reviews_section");
        return this;
    }
    
    /**
     * Check amenities
     * @return AgodaHotelDetailsPage instance for method chaining
     */
    public AgodaHotelDetailsPage checkAmenities() {
        logger.info("Checking hotel amenities");
        clickElement("amenities_section");
        return this;
    }
    
    /**
     * View location on map
     * @return AgodaHotelDetailsPage instance for method chaining
     */
    public AgodaHotelDetailsPage viewLocationOnMap() {
        logger.info("Viewing hotel location on map");
        clickElement("view_map_button");
        return this;
    }
    
    /**
     * Get hotel name
     * @return The hotel name
     */
    public String getHotelName() {
        String hotelName = getElementText("hotel_name");
        logger.debug("Hotel name: {}", hotelName);
        return hotelName;
    }
    
    /**
     * Get hotel rating
     * @return The hotel rating
     */
    public String getHotelRating() {
        String rating = getElementText("hotel_rating");
        logger.debug("Hotel rating: {}", rating);
        return rating;
    }
    
    /**
     * Get hotel address
     * @return The hotel address
     */
    public String getHotelAddress() {
        String address = getElementText("hotel_address");
        logger.debug("Hotel address: {}", address);
        return address;
    }
    
    /**
     * Get room price by index
     * @param roomIndex The index of the room
     * @return The room price
     */
    public String getRoomPrice(int roomIndex) {
        String price = getElementText("room_price_" + roomIndex);
        logger.debug("Room price at index {}: {}", roomIndex, price);
        return price;
    }
    
    /**
     * Get room name by index
     * @param roomIndex The index of the room
     * @return The room name
     */
    public String getRoomName(int roomIndex) {
        String roomName = getElementText("room_name_" + roomIndex);
        logger.debug("Room name at index {}: {}", roomIndex, roomName);
        return roomName;
    }
    
    /**
     * Check if room is available
     * @param roomIndex The index of the room
     * @return true if room is available, false otherwise
     */
    public boolean isRoomAvailable(int roomIndex) {
        boolean isAvailable = isElementEnabled("room_select_" + roomIndex);
        logger.debug("Room at index {} available: {}", roomIndex, isAvailable);
        return isAvailable;
    }
    
    /**
     * Check if booking button is enabled
     * @return true if booking button is enabled, false otherwise
     */
    public boolean isBookingButtonEnabled() {
        boolean isEnabled = isElementEnabled("book_now_button");
        logger.debug("Booking button enabled: {}", isEnabled);
        return isEnabled;
    }
    
    /**
     * Scroll to reviews section
     * @return AgodaHotelDetailsPage instance for method chaining
     */
    public AgodaHotelDetailsPage scrollToReviews() {
        logger.info("Scrolling to reviews section");
        scrollToElement("reviews_section");
        return this;
    }
    
    /**
     * Scroll to amenities section
     * @return AgodaHotelDetailsPage instance for method chaining
     */
    public AgodaHotelDetailsPage scrollToAmenities() {
        logger.info("Scrolling to amenities section");
        scrollToElement("amenities_section");
        return this;
    }
}