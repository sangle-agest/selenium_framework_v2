package com.myorg.automation.core;

import com.myorg.automation.models.ElementDefinition;
import com.myorg.automation.models.PageDefinition;
import com.myorg.automation.core.elements.BaseElement;
import com.myorg.automation.core.elements.Button;
import com.myorg.automation.core.elements.Textbox;
import com.myorg.automation.core.elements.Label;
import com.myorg.automation.core.elements.Combobox;
import com.myorg.automation.core.elements.Checkbox;
import com.myorg.automation.core.elements.Collection;
import com.myorg.automation.core.elements.DynamicLabel;
import com.myorg.automation.core.elements.DynamicButton;
import com.myorg.automation.core.elements.DynamicLink;
import com.myorg.automation.constants.FrameworkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Dynamic page object that loads elements from JSON definition
 */
public class DynamicPage {
    private static final Logger logger = LoggerFactory.getLogger(DynamicPage.class);
    
    private final PageDefinition pageDefinition;
    private final Map<String, BaseElement> elementCache;

    public DynamicPage(PageDefinition pageDefinition) {
        this.pageDefinition = pageDefinition;
        this.elementCache = new HashMap<>();
        logger.info("Created DynamicPage for: {}", pageDefinition.getPageName());
    }

    /**
     * Get an element by name and type
     * 
     * @param elementName Name of the element in JSON definition
     * @param elementType Expected type of the element
     * @return Element instance of the specified type
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseElement> T el(String elementName, Class<T> elementType) {
        String cacheKey = elementName + "_" + elementType.getSimpleName();
        
        if (elementCache.containsKey(cacheKey)) {
            return (T) elementCache.get(cacheKey);
        }

        ElementDefinition elementDef = pageDefinition.getElementByName(elementName);
        if (elementDef == null) {
            throw new RuntimeException(String.format(FrameworkConstants.ELEMENT_NOT_FOUND_ERROR, elementName, pageDefinition.getPageName()));
        }

        try {
            // Get constructor that takes locator and elementName
            Constructor<T> constructor = elementType.getConstructor(String.class, String.class);
            T element = constructor.newInstance(elementDef.getLocator(), elementName);
            
            elementCache.put(cacheKey, element);
            logger.debug("Created and cached {} element '{}' with locator: {}", 
                elementType.getSimpleName(), elementName, elementDef.getLocator());
            
            return element;
        } catch (Exception e) {
            throw new RuntimeException(String.format(FrameworkConstants.ELEMENT_CREATION_ERROR, 
                elementType.getSimpleName() + " " + elementName + ": " + e.getMessage()), e);
        }
    }    /**
     * Creates an element instance based on the element definition and type
     */
    /**
     * Gets the page URL
     * 
     * @return Page URL
     */
    public String getUrl() {
        return pageDefinition.getUrl();
    }

    /**
     * Gets the page name
     * 
     * @return Page name
     */
    public String getPageName() {
        return pageDefinition.getPageName();
    }

    /**
     * Gets the page description
     * 
     * @return Page description
     */
    public String getDescription() {
        return pageDefinition.getDescription();
    }

    /**
     * Gets the page definition
     * 
     * @return PageDefinition
     */
    public PageDefinition getPageDefinition() {
        return pageDefinition;
    }

    /**
     * Checks if an element exists in the page definition
     * 
     * @param elementName Element name
     * @return true if element exists, false otherwise
     */
    public boolean hasElement(String elementName) {
        return pageDefinition.hasElement(elementName);
    }

    /**
     * Gets all element names defined in the page
     * 
     * @return List of element names
     */
    public java.util.List<String> getElementNames() {
        return pageDefinition.getElementNames();
    }

    /**
     * Clears the element cache
     */
    public void clearElementCache() {
        logger.info("Clearing element cache for page: {}", pageDefinition.getPageName());
        elementCache.clear();
    }

    /**
     * Gets the size of element cache
     * 
     * @return Cache size
     */
    public int getCacheSize() {
        return elementCache.size();
    }

    /**
     * Helper method to get Button element
     */
    public Button button(String elementName) {
        return el(elementName, Button.class);
    }

    /**
     * Helper method to get Textbox element
     */
    public Textbox textbox(String elementName) {
        return el(elementName, Textbox.class);
    }

    /**
     * Helper method to get Label element
     */
    public Label label(String elementName) {
        return el(elementName, Label.class);
    }

    /**
     * Helper method to get Combobox element
     */
    public Combobox combobox(String elementName) {
        return el(elementName, Combobox.class);
    }

    /**
     * Helper method to get Checkbox element
     */
    public Checkbox checkbox(String elementName) {
        return el(elementName, Checkbox.class);
    }

    /**
     * Helper method to get Collection element
     */
    public Collection collection(String elementName) {
        return el(elementName, Collection.class);
    }

    /**
     * Helper method to get DynamicLabel element
     */
    public DynamicLabel dynamicLabel(String elementName) {
        return el(elementName, DynamicLabel.class);
    }

    /**
     * Helper method to get DynamicButton element
     */
    public DynamicButton dynamicButton(String elementName) {
        return el(elementName, DynamicButton.class);
    }

    /**
     * Helper method to get DynamicLink element
     */
    public DynamicLink dynamicLink(String elementName) {
        return el(elementName, DynamicLink.class);
    }

    @Override
    public String toString() {
        return "DynamicPage{" +
                "pageName='" + pageDefinition.getPageName() + '\'' +
                ", url='" + pageDefinition.getUrl() + '\'' +
                ", elements=" + pageDefinition.getElements().size() +
                ", cachedElements=" + elementCache.size() +
                '}';
    }
}