package com.myorg.automation.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Data class representing an element definition in JSON page objects
 */
public class ElementDefinition {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("locator")
    private String locator;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("timeout")
    private Integer timeout;
    
    @JsonProperty("required")
    private Boolean required = true;
    
    @JsonProperty("tags")
    private List<String> tags;

    // Default constructor for Jackson
    public ElementDefinition() {}

    // Constructor with required fields
    public ElementDefinition(String name, String locator, String type) {
        this.name = name;
        this.locator = locator;
        this.type = type;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Check if element has a specific tag
     * @param tag Tag to check for
     * @return true if element has the tag, false otherwise
     */
    public boolean hasTag(String tag) {
        return tags != null && tags.contains(tag);
    }

    /**
     * Check if element is required
     * @return true if element is required, false otherwise
     */
    public boolean isRequired() {
        return required != null ? required : true;
    }

    @Override
    public String toString() {
        return "ElementDefinition{" +
                "name='" + name + '\'' +
                ", locator='" + locator + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", timeout=" + timeout +
                ", required=" + required +
                ", tags=" + tags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementDefinition that = (ElementDefinition) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (locator != null ? !locator.equals(that.locator) : that.locator != null) return false;
        return type != null ? type.equals(that.type) : that.type == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (locator != null ? locator.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}