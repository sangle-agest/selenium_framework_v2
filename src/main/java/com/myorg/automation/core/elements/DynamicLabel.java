package com.myorg.automation.core.elements;

import com.codeborne.selenide.SelenideElement;
import com.myorg.automation.constants.FrameworkConstants;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dynamic Label element that accepts parameters for locator formatting
 */
public class DynamicLabel extends BaseElement {
    private static final Logger logger = LoggerFactory.getLogger(DynamicLabel.class);

    /**
     * Constructor for DynamicLabel
     * @param locator Element locator template with %s placeholders
     * @param elementName Element name for logging
     */
    public DynamicLabel(String locator, String elementName) {
        super(locator, elementName);
        logger.debug(FrameworkConstants.ELEMENT_CREATED_LOG, FrameworkConstants.ELEMENT_TYPE_DYNAMIC_LABEL, locator);
    }

    /**
     * Get text from dynamic label with parameter
     * @param parameter Parameter to format the locator
     * @return Text content of the label
     */
    @Step("Get text from dynamic label with parameter '{parameter}': {this.elementName}")
    public String getText(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException(FrameworkConstants.DYNAMIC_ELEMENT_PARAMETER_ERROR);
        }
        
        try {
            String dynamicLocator = String.format(locator, parameter);
            logger.debug(FrameworkConstants.DYNAMIC_LOCATOR_CREATED_LOG, dynamicLocator, parameter);
            
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
     * Check if dynamic label contains specific text
     * @param parameter Parameter to format the locator
     * @param expectedText Expected text to be contained
     * @return true if text is contained, false otherwise
     */
    @Step("Verify dynamic label contains text '{expectedText}' with parameter '{parameter}': {this.elementName}")
    public boolean containsText(String parameter, String expectedText) {
        try {
            String actualText = getText(parameter);
            boolean contains = actualText.contains(expectedText);
            logger.info("Dynamic label '{}' with parameter '{}' contains text '{}': {}", 
                locator, parameter, expectedText, contains);
            return contains;
        } catch (Exception e) {
            logger.error("Failed to verify if dynamic label '{}' with parameter '{}' contains text '{}': {}", 
                locator, parameter, expectedText, e.getMessage());
            throw new RuntimeException(String.format("Failed to verify if dynamic label '%s' with parameter '%s' contains text '%s': %s", 
                locator, parameter, expectedText, e.getMessage()), e);
        }
    }

    /**
     * Check if dynamic label text equals expected text
     * @param parameter Parameter to format the locator
     * @param expectedText Expected text
     * @return true if text equals expected, false otherwise
     */
    @Step("Verify dynamic label equals text '{expectedText}' with parameter '{parameter}': {this.elementName}")
    public boolean equalsText(String parameter, String expectedText) {
        try {
            String actualText = getText(parameter);
            boolean equals = actualText.equals(expectedText);
            logger.info("Dynamic label '{}' with parameter '{}' equals text '{}': {}", 
                locator, parameter, expectedText, equals);
            return equals;
        } catch (Exception e) {
            logger.error("Failed to verify if dynamic label '{}' with parameter '{}' equals text '{}': {}", 
                locator, parameter, expectedText, e.getMessage());
            throw new RuntimeException(String.format("Failed to verify if dynamic label '%s' with parameter '%s' equals text '%s': %s", 
                locator, parameter, expectedText, e.getMessage()), e);
        }
    }

    /**
     * Check if dynamic label is displayed
     * @param parameter Parameter to format the locator
     * @return true if displayed, false otherwise
     */
    @Step("Check if dynamic label is displayed with parameter '{parameter}': {this.elementName}")
    public boolean isDisplayed(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException(FrameworkConstants.DYNAMIC_ELEMENT_PARAMETER_ERROR);
        }
        
        try {
            String dynamicLocator = String.format(locator, parameter);
            SelenideElement element = createElement(dynamicLocator);
            boolean displayed = element.isDisplayed();
            logger.debug("Dynamic label '{}' with parameter '{}' is displayed: {}", locator, parameter, displayed);
            return displayed;
        } catch (Exception e) {
            logger.warn("Error checking if dynamic label '{}' with parameter '{}' is displayed: {}", 
                locator, parameter, e.getMessage());
            return false;
        }
    }

    /**
     * Wait for dynamic label to be visible
     * @param parameter Parameter to format the locator
     * @return this DynamicLabel instance for method chaining
     */
    @Step("Wait for dynamic label to be visible with parameter '{parameter}': {this.elementName}")
    public DynamicLabel waitToBeVisible(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException(FrameworkConstants.DYNAMIC_ELEMENT_PARAMETER_ERROR);
        }
        
        try {
            String dynamicLocator = String.format(locator, parameter);
            SelenideElement element = createElement(dynamicLocator);
            element.should(com.codeborne.selenide.Condition.visible);
            logger.debug("Dynamic label '{}' with parameter '{}' is now visible", locator, parameter);
        } catch (Exception e) {
            logger.error("Failed to wait for dynamic label '{}' with parameter '{}' to be visible: {}", 
                locator, parameter, e.getMessage());
            throw new RuntimeException(String.format("Failed to wait for dynamic label '%s' with parameter '%s' to be visible: %s", 
                locator, parameter, e.getMessage()), e);
        }
        return this;
    }
}