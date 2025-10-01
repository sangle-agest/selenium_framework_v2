package com.myorg.automation.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Data class representing a page definition in JSON page objects
 */
public class PageDefinition {
    
    @JsonProperty("pageName")
    private String pageName;
    
    @JsonProperty("url")
    private String url;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("elements")
    private List<ElementDefinition> elements;
    
    @JsonProperty("metadata")
    private Map<String, Object> metadata;
    
    @JsonProperty("timeout")
    private Integer timeout;
    
    @JsonProperty("tags")
    private List<String> tags;

    // Default constructor for Jackson
    public PageDefinition() {}

    // Constructor with required fields
    public PageDefinition(String pageName, String url, List<ElementDefinition> elements) {
        this.pageName = pageName;
        this.url = url;
        this.elements = elements;
    }

    // Getters and Setters
    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ElementDefinition> getElements() {
        return elements;
    }

    public void setElements(List<ElementDefinition> elements) {
        this.elements = elements;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Find element definition by name
     * @param elementName Name of the element
     * @return ElementDefinition if found, null otherwise
     */
    public ElementDefinition findElement(String elementName) {
        if (elements == null) {
            return null;
        }
        return elements.stream()
                .filter(element -> elementName.equals(element.getName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get element definition by name (alias for findElement)
     * @param elementName Name of the element
     * @return ElementDefinition if found, null otherwise
     */
    public ElementDefinition getElementByName(String elementName) {
        return findElement(elementName);
    }

    /**
     * Get list of all element names
     * @return List of element names
     */
    public List<String> getElementNames() {
        if (elements == null) {
            return List.of();
        }
        return elements.stream()
                .map(ElementDefinition::getName)
                .toList();
    }

    /**
     * Check if element exists in this page
     * @param elementName Name of the element
     * @return true if element exists, false otherwise
     */
    public boolean hasElement(String elementName) {
        return findElement(elementName) != null;
    }

    /**
     * Get elements by type
     * @param elementType Type of element (button, textbox, etc.)
     * @return List of elements with the specified type
     */
    public List<ElementDefinition> getElementsByType(String elementType) {
        if (elements == null) {
            return List.of();
        }
        return elements.stream()
                .filter(element -> elementType.equals(element.getType()))
                .toList();
    }

    /**
     * Get elements by tag
     * @param tag Tag to filter by
     * @return List of elements with the specified tag
     */
    public List<ElementDefinition> getElementsByTag(String tag) {
        if (elements == null) {
            return List.of();
        }
        return elements.stream()
                .filter(element -> element.getTags() != null && element.getTags().contains(tag))
                .toList();
    }

    /**
     * Check if page has a specific tag
     * @param tag Tag to check for
     * @return true if page has the tag, false otherwise
     */
    public boolean hasTag(String tag) {
        return tags != null && tags.contains(tag);
    }

    /**
     * Get metadata value by key
     * @param key Metadata key
     * @return Metadata value or null if not found
     */
    public Object getMetadataValue(String key) {
        return metadata != null ? metadata.get(key) : null;
    }

    /**
     * Get metadata value as string
     * @param key Metadata key
     * @param defaultValue Default value if key not found
     * @return Metadata value as string
     */
    public String getMetadataString(String key, String defaultValue) {
        Object value = getMetadataValue(key);
        return value != null ? value.toString() : defaultValue;
    }

    @Override
    public String toString() {
        return "PageDefinition{" +
                "pageName='" + pageName + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", elementsCount=" + (elements != null ? elements.size() : 0) +
                ", timeout=" + timeout +
                ", tags=" + tags +
                '}';
    }
}