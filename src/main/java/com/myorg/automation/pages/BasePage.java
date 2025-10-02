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
     * Click element with smart waiting and retry logic
     * @param elementName The element name from JSON
     * @return Current page instance for method chaining
     */
    protected BasePage clickElement(String elementName) {
        String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
        logger.info("Clicking element: {}", description);
        
        boolean success = retryOperation(() -> {
            try {
                SelenideElement element = getElement(elementName);
                element.click();
                logger.debug("Successfully clicked element: {}", description);
                return true;
            } catch (Exception e) {
                logger.debug("Failed to click element '{}': {}", description, e.getMessage());
                return false;
            }
        });
        
        if (!success) {
            throw new RuntimeException("Failed to click element '" + elementName + "' after 3 attempts");
        }
        
        return this;
    }
    
    /**
     * Type text into element
     * @param elementName The element name from JSON
     * @param text The text to type
     * @return Current page instance for method chaining
     */
    protected BasePage typeText(String elementName, String text) {
        return typeText(elementName, text, false);
    }
    
    /**
     * Type text into element with option for character-by-character typing
     * @param elementName The element name from JSON
     * @param text The text to type
     * @param useCharacterByCharacter Whether to type character by character (for autocomplete)
     * @return Current page instance for method chaining
     */
    protected BasePage typeText(String elementName, String text, boolean useCharacterByCharacter) {
        if (text == null || text.trim().isEmpty()) {
            logger.warn("Empty text provided for element: {}", elementName);
            return this;
        }
        
        SelenideElement element = getElement(elementName);
        String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
        
        logger.info("Typing text '{}' into element: {} (character-by-character: {})", text, description, useCharacterByCharacter);
        element.clear();
        
        if (useCharacterByCharacter) {
            typeCharacterByCharacter(element, text);
        } else {
            element.setValue(text);
        }
        
        return this;
    }
    
    /**
     * Click element first and then type text - Special case for search boxes that need activation
     * @param elementName The element name from JSON
     * @param text The text to type
     * @return Current page instance for method chaining
     */
    protected BasePage clickAndEnter(String elementName, String text) {
        if (text == null || text.trim().isEmpty()) {
            logger.warn("Empty text provided for element: {}", elementName);
            return this;
        }
        
        SelenideElement element = getElement(elementName);
        String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
        
        logger.info("Clicking and entering text '{}' into element: {}", text, description);
        
        // First click to activate the element (important for search boxes)
        element.click();
        
        // Small wait to ensure the element is activated
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Clear any existing content first
        element.clear();
        
        // Type character by character to trigger autocomplete
        typeCharacterByCharacter(element, text);
        
        return this;
    }
    
    /**
     * Type text character by character for autocomplete scenarios
     * @param elementName The element name from JSON  
     * @param text The text to type
     * @return Current page instance for method chaining
     */
    protected BasePage typeTextForAutocomplete(String elementName, String text) {
        return typeText(elementName, text, true);
    }
    
    /**
     * Type text character by character to trigger autocomplete properly
     * @param element The element to type into
     * @param text The text to type
     */
    protected void typeCharacterByCharacter(SelenideElement element, String text) {
        typeCharacterByCharacter(element, text, 100, 1500);
    }
    
    /**
     * Type text character by character with custom delays
     * @param element The element to type into
     * @param text The text to type
     * @param charDelay Delay between characters in milliseconds
     * @param finalWait Wait time after complete text in milliseconds
     */
    protected void typeCharacterByCharacter(SelenideElement element, String text, int charDelay, int finalWait) {
        logger.debug("Typing '{}' character by character with {}ms delay", text, charDelay);
        
        // Type character by character to trigger autocomplete properly
        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));
            element.sendKeys(character);
            
            // Small delay between characters to trigger search events
            try {
                Thread.sleep(charDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Character typing interrupted");
                break;
            }
        }
        
        // Wait a bit more for suggestions to appear after complete text
        try {
            Thread.sleep(finalWait);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Final wait after typing interrupted");
        }
        
        logger.debug("Completed character-by-character typing for: {}", text);
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
     * Get text from element with retry logic
     * @param elementName The element name from JSON
     * @return The element text
     */
    public String getElementText(String elementName) {
        String description = JsonLocatorHelper.getElementDescription(pageName, elementName);
        logger.debug("Getting text from element: {}", description);
        
        final String[] result = {null};
        boolean success = retryOperation(() -> {
            try {
                SelenideElement element = getElement(elementName);
                result[0] = element.getText();
                logger.debug("Retrieved text '{}' from element: {}", result[0], description);
                return true;
            } catch (Exception e) {
                logger.debug("Failed to get text from element '{}': {}", description, e.getMessage());
                return false;
            }
        });
        
        if (!success) {
            logger.warn("Failed to get text from element '{}' after 3 attempts, returning empty string", elementName);
            return "";
        }
        
        return result[0] != null ? result[0] : "";
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
     * Retry operation with specified attempts
     * @param operation The operation to retry
     * @param maxAttempts Maximum number of attempts (default: 3)
     * @param delayBetweenAttempts Delay between attempts in milliseconds (default: 1000)
     * @return true if operation succeeded, false otherwise
     */
    protected boolean retryOperation(java.util.function.Supplier<Boolean> operation, int maxAttempts, long delayBetweenAttempts) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                if (operation.get()) {
                    if (attempt > 1) {
                        logger.debug("Operation succeeded on attempt {}/{}", attempt, maxAttempts);
                    }
                    return true;
                }
            } catch (Exception e) {
                logger.debug("Attempt {}/{} failed: {}", attempt, maxAttempts, e.getMessage());
            }
            
            if (attempt < maxAttempts) {
                try {
                    logger.debug("Waiting {}ms before retry attempt {}/{}", delayBetweenAttempts, attempt + 1, maxAttempts);
                    Thread.sleep(delayBetweenAttempts);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    logger.warn("Sleep interrupted during retry");
                    break;
                }
            }
        }
        
        logger.warn("Operation failed after {} attempts", maxAttempts);
        return false;
    }
    
    /**
     * Retry operation with default settings (3 attempts, 1 second delay)
     * @param operation The operation to retry
     * @return true if operation succeeded, false otherwise
     */
    protected boolean retryOperation(java.util.function.Supplier<Boolean> operation) {
        return retryOperation(operation, 3, 1000);
    }
    
    /**
     * Check if element is visible with retry logic
     * @param elementName The element name from JSON
     * @return true if element is visible, false otherwise
     */
    public boolean isElementVisible(String elementName) {
        logger.debug("Checking visibility of element '{}' with retry", elementName);
        
        return retryOperation(() -> {
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
        });
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