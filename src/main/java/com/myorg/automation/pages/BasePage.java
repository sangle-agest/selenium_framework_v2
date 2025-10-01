package com.myorg.automation.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.myorg.automation.constants.FrameworkConstants;
import com.myorg.automation.utils.JsonLocatorHelper;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * BasePage - Foundation class for all page objects
 * 
 * Provides:
 * - JSON-based locator management
 * - Smart waiting strategies
 * - Common page actions
 * - Element creation methods
 * - No assertions (only actions and verifications)
 * - Reusable utility methods
 */
public abstract class BasePage {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final String pageName;
    
    protected BasePage(String pageName) {
        this.pageName = pageName;
        logger.debug("Initializing page: {}", pageName);
    }
    
    /**
     * Get element using JSON locator configuration
     * @param elementName The element name from JSON
     * @return SelenideElement with smart waiting applied
     */
    protected SelenideElement getElement(String elementName) {
        String locatorValue = JsonLocatorHelper.getLocator(pageName, elementName);
        String locatorType = JsonLocatorHelper.getLocatorType(pageName, elementName);
        String waitType = JsonLocatorHelper.getElementWaitType(pageName, elementName);
        
        By locator = createLocator(locatorType, locatorValue);
        SelenideElement element = $(locator);
        
        // Apply smart waiting based on wait type
        applySmartWait(element, waitType, elementName);
        
        return element;
    }
    
    /**
     * Get multiple elements using JSON locator configuration
     * @param elementName The element name from JSON
     * @return Collection of SelenideElements
     */
    protected com.codeborne.selenide.ElementsCollection getElements(String elementName) {
        String locatorValue = JsonLocatorHelper.getLocator(pageName, elementName);
        String locatorType = JsonLocatorHelper.getLocatorType(pageName, elementName);
        
        By locator = createLocator(locatorType, locatorValue);
        return $$(locator);
    }
    
    /**
     * Create By locator based on type and value
     * @param locatorType The type of locator (id, css, xpath, etc.)
     * @param locatorValue The locator value
     * @return By locator
     */
    private By createLocator(String locatorType, String locatorValue) {
        switch (locatorType.toLowerCase()) {
            case FrameworkConstants.LOCATOR_TYPE_ID:
                return By.id(locatorValue);
            case FrameworkConstants.LOCATOR_TYPE_CSS:
                return By.cssSelector(locatorValue);
            case FrameworkConstants.LOCATOR_TYPE_XPATH:
                return By.xpath(locatorValue);
            case FrameworkConstants.LOCATOR_TYPE_CLASS:
                return By.className(locatorValue);
            case FrameworkConstants.LOCATOR_TYPE_NAME:
                return By.name(locatorValue);
            case FrameworkConstants.LOCATOR_TYPE_TAG:
                return By.tagName(locatorValue);
            case FrameworkConstants.LOCATOR_TYPE_TEXT:
                return By.linkText(locatorValue);
            default:
                logger.warn("Unknown locator type '{}', defaulting to CSS selector", locatorType);
                return By.cssSelector(locatorValue);
        }
    }
    
    /**
     * Apply smart waiting strategy based on wait type
     * @param element The element to wait for
     * @param waitType The type of wait to apply
     * @param elementName The element name for logging
     */
    private void applySmartWait(SelenideElement element, String waitType, String elementName) {
        try {
            switch (waitType.toLowerCase()) {
                case FrameworkConstants.WAIT_TYPE_VISIBLE:
                    element.shouldBe(Condition.visible);
                    break;
                case FrameworkConstants.WAIT_TYPE_CLICKABLE:
                    element.shouldBe(Condition.enabled);
                    break;
                case FrameworkConstants.WAIT_TYPE_PRESENT:
                    element.shouldBe(Condition.exist);
                    break;
                case FrameworkConstants.WAIT_TYPE_INVISIBLE:
                    element.shouldBe(Condition.hidden);
                    break;
                default:
                    element.shouldBe(Condition.visible);
                    break;
            }
            logger.debug("Smart wait applied for element '{}' with wait type '{}'", elementName, waitType);
        } catch (Exception e) {
            logger.warn("Smart wait failed for element '{}' with wait type '{}': {}", elementName, waitType, e.getMessage());
        }
    }
    
    /**
     * Click element with smart waiting
     * @param elementName The element name from JSON
     * @return Current page instance for method chaining
     */
    protected BasePage clickElement(String elementName) {
        SelenideElement element = getElement(elementName);
        String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
        
        logger.info("Clicking element: {}", description);
        element.click();
        
        return this;
    }
    
    /**
     * Type text into element
     * @param elementName The element name from JSON
     * @param text The text to type
     * @return Current page instance for method chaining
     */
    protected BasePage typeText(String elementName, String text) {
        if (text == null || text.trim().isEmpty()) {
            logger.warn("Empty text provided for element: {}", elementName);
            return this;
        }
        
        SelenideElement element = getElement(elementName);
        String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
        
        logger.info("Typing text '{}' into element: {}", text, description);
        element.clear();
        element.setValue(text);
        
        return this;
    }
    
    /**
     * Clear element content
     * @param elementName The element name from JSON
     * @return Current page instance for method chaining
     */
    protected BasePage clearElement(String elementName) {
        SelenideElement element = getElement(elementName);
        String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
        
        logger.info("Clearing element: {}", description);
        element.clear();
        
        return this;
    }
    
    /**
     * Select option from dropdown
     * @param elementName The element name from JSON
     * @param optionText The option text to select
     * @return Current page instance for method chaining
     */
    protected BasePage selectOption(String elementName, String optionText) {
        if (optionText == null || optionText.trim().isEmpty()) {
            logger.warn("Empty option text provided for element: {}", elementName);
            return this;
        }
        
        SelenideElement element = getElement(elementName);
        String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
        
        logger.info("Selecting option '{}' from element: {}", optionText, description);
        element.selectOption(optionText);
        
        return this;
    }
    
    /**
     * Get text from element
     * @param elementName The element name from JSON
     * @return The element text
     */
    public String getElementText(String elementName) {
        SelenideElement element = getElement(elementName);
        String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
        
        String text = element.getText();
        logger.debug("Retrieved text '{}' from element: {}", text, description);
        
        return text;
    }
    
    /**
     * Get attribute value from element
     * @param elementName The element name from JSON
     * @param attributeName The attribute name
     * @return The attribute value
     */
    public String getElementAttribute(String elementName, String attributeName) {
        SelenideElement element = getElement(elementName);
        String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
        
        String attributeValue = element.getAttribute(attributeName);
        logger.debug("Retrieved attribute '{}' value '{}' from element: {}", attributeName, attributeValue, description);
        
        return attributeValue;
    }
    
    /**
     * Check if element is visible
     * @param elementName The element name from JSON
     * @return true if element is visible, false otherwise
     */
    public boolean isElementVisible(String elementName) {
        try {
            SelenideElement element = getElement(elementName);
            boolean isVisible = element.isDisplayed();
            
            String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
            logger.debug("Element '{}' visibility: {}", description, isVisible);
            
            return isVisible;
        } catch (Exception e) {
            logger.debug("Element '{}' is not visible: {}", elementName, e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if element is enabled
     * @param elementName The element name from JSON
     * @return true if element is enabled, false otherwise
     */
    public boolean isElementEnabled(String elementName) {
        try {
            SelenideElement element = getElement(elementName);
            boolean isEnabled = element.isEnabled();
            
            String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
            logger.debug("Element '{}' enabled state: {}", description, isEnabled);
            
            return isEnabled;
        } catch (Exception e) {
            logger.debug("Element '{}' is not enabled: {}", elementName, e.getMessage());
            return false;
        }
    }
    
    /**
     * Wait for element to be visible
     * @param elementName The element name from JSON
     * @return Current page instance for method chaining
     */
    public BasePage waitForElementVisible(String elementName) {
        SelenideElement element = getElement(elementName);
        String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
        
        logger.debug("Waiting for element to be visible: {}", description);
        element.shouldBe(Condition.visible);
        
        return this;
    }
    
    /**
     * Wait for element to be clickable
     * @param elementName The element name from JSON
     * @return Current page instance for method chaining
     */
    public BasePage waitForElementClickable(String elementName) {
        SelenideElement element = getElement(elementName);
        String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
        
        logger.debug("Waiting for element to be clickable: {}", description);
        element.shouldBe(Condition.enabled);
        
        return this;
    }
    
    /**
     * Scroll element into view
     * @param elementName The element name from JSON
     * @return Current page instance for method chaining
     */
    protected BasePage scrollToElement(String elementName) {
        SelenideElement element = getElement(elementName);
        String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
        
        logger.debug("Scrolling to element: {}", description);
        element.scrollTo();
        
        return this;
    }
    
    /**
     * Hover over element
     * @param elementName The element name from JSON
     * @return Current page instance for method chaining
     */
    protected BasePage hoverOverElement(String elementName) {
        SelenideElement element = getElement(elementName);
        String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
        
        logger.debug("Hovering over element: {}", description);
        element.hover();
        
        return this;
    }
    
    /**
     * Double click element
     * @param elementName The element name from JSON
     * @return Current page instance for method chaining
     */
    protected BasePage doubleClickElement(String elementName) {
        SelenideElement element = getElement(elementName);
        String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
        
        logger.info("Double clicking element: {}", description);
        element.doubleClick();
        
        return this;
    }
    
    /**
     * Get the page name for this page object
     * @return The page name
     */
    public String getPageName() {
        return pageName;
    }
    
    /**
     * Get the expected page title from JSON
     * @return The page title
     */
    public String getExpectedPageTitle() {
        return JsonLocatorHelper.getPageTitle(pageName);
    }
    
    /**
     * Get element text using dynamic locator (with placeholders)
     * @param elementName The element name from JSON (should contain placeholders like {index})
     * @param replacements Array of replacement values for placeholders
     * @return The element text
     */
    public String getElementTextDynamic(String elementName, String... replacements) {
        String locatorValue = JsonLocatorHelper.getLocator(pageName, elementName);
        String locatorType = JsonLocatorHelper.getLocatorType(pageName, elementName);
        
        // Replace placeholders in the locator
        String dynamicLocator = replacePlaceholders(locatorValue, replacements);
        
        By locator = createLocator(locatorType, dynamicLocator);
        SelenideElement element = $(locator);
        
        String text = element.getText();
        logger.debug("Retrieved text '{}' from dynamic element: {}", text, dynamicLocator);
        
        return text;
    }
    
    /**
     * Replace placeholders in locator string
     * @param locatorValue The locator with placeholders
     * @param replacements Array of replacement values
     * @return The locator with placeholders replaced
     */
    private String replacePlaceholders(String locatorValue, String... replacements) {
        String result = locatorValue;
        
        // Replace {index} placeholder
        if (replacements.length > 0) {
            result = result.replace("{index}", replacements[0]);
        }
        
        // Add more placeholder types if needed in the future
        // {id}, {class}, etc.
        
        return result;
    }
    
    /**
     * Get the page URL from JSON
     * @return The page URL
     */
    public String getPageUrl() {
        return JsonLocatorHelper.getPageUrl(pageName);
    }
}