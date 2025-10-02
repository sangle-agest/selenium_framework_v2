package com.myorg.automation.enums;

/**
 * Enum for different sorting options available on search results
 */
public enum SortType {
    PRICE_LOW_TO_HIGH("price_low_high", "Price: Low to High"),
    PRICE_HIGH_TO_LOW("price_high_low", "Price: High to Low"),
    RATING_HIGH_TO_LOW("rating", "Rating: High to Low"),
    POPULARITY("popularity", "Popularity"),
    DISTANCE("distance", "Distance"),
    NEWEST("newest", "Newest First");
    
    private final String value;
    private final String displayName;
    
    SortType(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}