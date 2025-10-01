package com.myorg.automation.core.elements;

import com.codeborne.selenide.SelenideElement;
import com.myorg.automation.constants.FrameworkConstants;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dynamic Button element that accepts parameters for locator formatting
 */
public class DynamicButton extends BaseElement {
    private static final Logger logger = LoggerFactory.getLogger(DynamicButton.class);

    /**
     * Constructor for DynamicButton
     * @param locator Element locator template with %s placeholders
     * @param elementName Element name for logging
     */
    public DynamicButton(String locator, String elementName) {
        super(locator, elementName);
        logger.debug(FrameworkConstants.ELEMENT_CREATED_LOG, FrameworkConstants.ELEMENT_TYPE_DYNAMIC_BUTTON, locator);
    }

    /**
     * Click dynamic button with parameter
     * @param parameter Parameter to format the locator
     * @return this DynamicButton instance for method chaining
     */
    @Step("Click dynamic button with parameter '{parameter}': {this.elementName}")
    public DynamicButton click(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException(FrameworkConstants.DYNAMIC_ELEMENT_PARAMETER_ERROR);
        }
        
        try {
            String dynamicLocator = String.format(locator, parameter);
            logger.debug(FrameworkConstants.DYNAMIC_LOCATOR_CREATED_LOG, dynamicLocator, parameter);
            
            SelenideElement element = createElement(dynamicLocator);
            element.click();
            
            logger.info(FrameworkConstants.DYNAMIC_ELEMENT_CLICKED_LOG, locator, parameter);
        } catch (Exception e) {
            logger.error(FrameworkConstants.DYNAMIC_ELEMENT_CLICK_ERROR_LOG, locator, parameter, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.DYNAMIC_ELEMENT_CLICK_ERROR_LOG, locator, parameter, e.getMessage()), e);
        }
        return this;
    }

    /**
     * Double click dynamic button with parameter
     * @param parameter Parameter to format the locator
     * @return this DynamicButton instance for method chaining
     */
    @Step("Double click dynamic button with parameter '{parameter}': {this.elementName}")
    public DynamicButton doubleClick(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException(FrameworkConstants.DYNAMIC_ELEMENT_PARAMETER_ERROR);
        }
        
        try {
            String dynamicLocator = String.format(locator, parameter);
            SelenideElement element = createElement(dynamicLocator);
            element.doubleClick();
            logger.info("Double clicked dynamic button '{}' with parameter '{}'", locator, parameter);
        } catch (Exception e) {
            logger.error("Failed to double click dynamic button '{}' with parameter '{}': {}", 
                locator, parameter, e.getMessage());
            throw new RuntimeException(String.format("Failed to double click dynamic button '%s' with parameter '%s': %s", 
                locator, parameter, e.getMessage()), e);
        }
        return this;
    }

    /**
     * Right click dynamic button with parameter
     * @param parameter Parameter to format the locator
     * @return this DynamicButton instance for method chaining
     */
    @Step("Right click dynamic button with parameter '{parameter}': {this.elementName}")
    public DynamicButton rightClick(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException(FrameworkConstants.DYNAMIC_ELEMENT_PARAMETER_ERROR);
        }
        
        try {
            String dynamicLocator = String.format(locator, parameter);
            SelenideElement element = createElement(dynamicLocator);
            element.contextClick();
            logger.info("Right clicked dynamic button '{}' with parameter '{}'", locator, parameter);
        } catch (Exception e) {
            logger.error("Failed to right click dynamic button '{}' with parameter '{}': {}", 
                locator, parameter, e.getMessage());
            throw new RuntimeException(String.format("Failed to right click dynamic button '%s' with parameter '%s': %s", 
                locator, parameter, e.getMessage()), e);
        }
        return this;
    }

    /**
     * Check if dynamic button is enabled
     * @param parameter Parameter to format the locator
     * @return true if enabled, false otherwise
     */
    @Step("Check if dynamic button is enabled with parameter '{parameter}': {this.elementName}")
    public boolean isEnabled(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException(FrameworkConstants.DYNAMIC_ELEMENT_PARAMETER_ERROR);
        }
        
        try {
            String dynamicLocator = String.format(locator, parameter);
            SelenideElement element = createElement(dynamicLocator);
            boolean enabled = element.isEnabled();
            logger.debug("Dynamic button '{}' with parameter '{}' is enabled: {}", locator, parameter, enabled);
            return enabled;
        } catch (Exception e) {
            logger.warn("Error checking if dynamic button '{}' with parameter '{}' is enabled: {}", 
                locator, parameter, e.getMessage());
            return false;
        }
    }

    /**
     * Get text from dynamic button
     * @param parameter Parameter to format the locator
     * @return Button text
     */
    @Step("Get text from dynamic button with parameter '{parameter}': {this.elementName}")
    public String getText(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException(FrameworkConstants.DYNAMIC_ELEMENT_PARAMETER_ERROR);
        }
        
        try {
            String dynamicLocator = String.format(locator, parameter);
            SelenideElement element = createElement(dynamicLocator);
            String text = element.getText();
            logger.info(FrameworkConstants.DYNAMIC_ELEMENT_TEXT_RETRIEVED_LOG, text, locator, parameter);
            return text;
        } catch (Exception e) {
            logger.error(FrameworkConstants.DYNAMIC_ELEMENT_TEXT_ERROR_LOG, locator, parameter, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.DYNAMIC_ELEMENT_TEXT_ERROR_LOG, locator, parameter, e.getMessage()), e);
        }
    }

    /**
     * Wait for dynamic button to be clickable
     * @param parameter Parameter to format the locator
     * @return this DynamicButton instance for method chaining
     */
    @Step("Wait for dynamic button to be clickable with parameter '{parameter}': {this.elementName}")
    public DynamicButton waitToBeClickable(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException(FrameworkConstants.DYNAMIC_ELEMENT_PARAMETER_ERROR);
        }
        
        try {
            String dynamicLocator = String.format(locator, parameter);
            SelenideElement element = createElement(dynamicLocator);
            element.should(com.codeborne.selenide.Condition.enabled);
            logger.debug("Dynamic button '{}' with parameter '{}' is now clickable", locator, parameter);
        } catch (Exception e) {
            logger.error("Failed to wait for dynamic button '{}' with parameter '{}' to be clickable: {}", 
                locator, parameter, e.getMessage());
            throw new RuntimeException(String.format("Failed to wait for dynamic button '%s' with parameter '%s' to be clickable: %s", 
                locator, parameter, e.getMessage()), e);
        }
        return this;
    }
}