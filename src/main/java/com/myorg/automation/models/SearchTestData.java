package com.myorg.automation.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SearchTestData - POJO model for hotel search test data
 * 
 * Maps JSON test data structure to Java object for type-safe access
 * Used with TestDataProvider to supply test data via @DataProvider
 */
public class SearchTestData {
    
    @JsonProperty("place")
    private String place;
    
    @JsonProperty("checkInDate")
    private String checkInDate;
    
    @JsonProperty("checkOutDate")
    private String checkOutDate;
    
    @JsonProperty("travellerType")
    private String travellerType;
    
    @JsonProperty("rooms")
    private int rooms;
    
    @JsonProperty("adults")
    private int adults;
    
    @JsonProperty("children")
    private int children;
    
    @JsonProperty("expectedResultsMinimum")
    private int expectedResultsMinimum;
    
    @JsonProperty("maxPriceFilter")
    private double maxPriceFilter;
    
    @JsonProperty("minRating")
    private double minRating;
    
    @JsonProperty("searchTimeout")
    private int searchTimeout;
    
    // Default constructor for Jackson
    public SearchTestData() {}
    
    // Constructor for manual creation
    public SearchTestData(String place, String checkInDate, String checkOutDate, 
                         String travellerType, int rooms, int adults, int children,
                         int expectedResultsMinimum, double maxPriceFilter, 
                         double minRating, int searchTimeout) {
        this.place = place;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.travellerType = travellerType;
        this.rooms = rooms;
        this.adults = adults;
        this.children = children;
        this.expectedResultsMinimum = expectedResultsMinimum;
        this.maxPriceFilter = maxPriceFilter;
        this.minRating = minRating;
        this.searchTimeout = searchTimeout;
    }
    
    // Getters
    public String getPlace() {
        return place;
    }
    
    public String getCheckInDate() {
        return checkInDate;
    }
    
    public String getCheckOutDate() {
        return checkOutDate;
    }
    
    public String getTravellerType() {
        return travellerType;
    }
    
    public int getRooms() {
        return rooms;
    }
    
    public int getAdults() {
        return adults;
    }
    
    public int getChildren() {
        return children;
    }
    
    public int getExpectedResultsMinimum() {
        return expectedResultsMinimum;
    }
    
    public double getMaxPriceFilter() {
        return maxPriceFilter;
    }
    
    public double getMinRating() {
        return minRating;
    }
    
    public int getSearchTimeout() {
        return searchTimeout;
    }
    
    // Setters
    public void setPlace(String place) {
        this.place = place;
    }
    
    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }
    
    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    
    public void setTravellerType(String travellerType) {
        this.travellerType = travellerType;
    }
    
    public void setRooms(int rooms) {
        this.rooms = rooms;
    }
    
    public void setAdults(int adults) {
        this.adults = adults;
    }
    
    public void setChildren(int children) {
        this.children = children;
    }
    
    public void setExpectedResultsMinimum(int expectedResultsMinimum) {
        this.expectedResultsMinimum = expectedResultsMinimum;
    }
    
    public void setMaxPriceFilter(double maxPriceFilter) {
        this.maxPriceFilter = maxPriceFilter;
    }
    
    public void setMinRating(double minRating) {
        this.minRating = minRating;
    }
    
    public void setSearchTimeout(int searchTimeout) {
        this.searchTimeout = searchTimeout;
    }
    
    @Override
    public String toString() {
        return String.format("SearchTestData{place='%s', checkInDate='%s', checkOutDate='%s', " +
                           "travellerType='%s', rooms=%d, adults=%d, children=%d, " +
                           "expectedResultsMinimum=%d, maxPriceFilter=%.1f, minRating=%.1f, " +
                           "searchTimeout=%d}",
                           place, checkInDate, checkOutDate, travellerType, rooms, adults, 
                           children, expectedResultsMinimum, maxPriceFilter, minRating, searchTimeout);
    }
}