package com.myorg.automation.core.elements;

import com.codeborne.selenide.SelenideElement;
import com.myorg.automation.constants.FrameworkConstants;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Label element wrapper for displaying text content
 */
public class Label extends BaseElement {
    private static final Logger logger = LoggerFactory.getLogger(Label.class);

    /**
     * Constructor for Label
     * @param locator Element locator
     * @param elementName Element name for logging
     */
    public Label(String locator, String elementName) {
        super(locator, elementName);
        logger.debug(FrameworkConstants.ELEMENT_CREATED_LOG, FrameworkConstants.ELEMENT_TYPE_LABEL, locator);
    }

    /**
     * Get the text content of the label
     * @return Text content
     */
    @Step("Get text from label: {this.locator}")
    @Override
    public String getText() {
        try {
            String text = super.getText();
            logger.debug(FrameworkConstants.LABEL_TEXT_RETRIEVED_LOG, text, locator);
            return text;
        } catch (Exception e) {
            logger.error(FrameworkConstants.LABEL_TEXT_ERROR_LOG, locator, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.LABEL_TEXT_ERROR_LOG, locator, e.getMessage()), e);
        }
    }

    /**
     * Verify label contains specific text
     * @param expectedText Expected text to be contained
     * @return true if text is contained, false otherwise
     */
    @Step("Verify label contains text '{expectedText}': {this.locator}")
    public boolean containsText(String expectedText) {
        try {
            String actualText = getText();
            boolean contains = actualText.contains(expectedText);
            logger.info("Label '{}' contains text '{}': {}", locator, expectedText, contains);
            return contains;
        } catch (Exception e) {
            logger.error("Failed to verify if label '{}' contains text '{}': {}", locator, expectedText, e.getMessage());
            throw new RuntimeException(String.format("Failed to verify if label '%s' contains text '%s': %s", 
                locator, expectedText, e.getMessage()), e);
        }
    }

    /**
     * Verify label text equals expected text
     * @param expectedText Expected text
     * @return true if text equals expected, false otherwise
     */
    @Step("Verify label equals text '{expectedText}': {this.locator}")
    public boolean equalsText(String expectedText) {
        try {
            String actualText = getText();
            boolean equals = actualText.equals(expectedText);
            logger.info("Label '{}' equals text '{}': {}", locator, expectedText, equals);
            return equals;
        } catch (Exception e) {
            logger.error("Failed to verify if label '{}' equals text '{}': {}", locator, expectedText, e.getMessage());
            throw new RuntimeException(String.format("Failed to verify if label '%s' equals text '%s': %s", 
                locator, expectedText, e.getMessage()), e);
        }
    }

    /**
     * Verify label text matches pattern
     * @param pattern Regular expression pattern
     * @return true if text matches pattern, false otherwise
     */
    @Step("Verify label matches pattern '{pattern}': {this.locator}")
    public boolean matchesPattern(String pattern) {
        try {
            String actualText = getText();
            boolean matches = actualText.matches(pattern);
            logger.info("Label '{}' matches pattern '{}': {}", locator, pattern, matches);
            return matches;
        } catch (Exception e) {
            logger.error("Failed to verify if label '{}' matches pattern '{}': {}", locator, pattern, e.getMessage());
            throw new RuntimeException(String.format("Failed to verify if label '%s' matches pattern '%s': %s", 
                locator, pattern, e.getMessage()), e);
        }
    }

    /**
     * Get inner HTML of the label
     * @return Inner HTML content
     */
    @Step("Get inner HTML of label: {this.locator}")
    public String getInnerHTML() {
        try {
            SelenideElement element = getElement();
            String innerHTML = element.innerHtml();
            logger.debug("Inner HTML of label '{}': '{}'", locator, innerHTML);
            return innerHTML;
        } catch (Exception e) {
            logger.error("Failed to get inner HTML of label '{}': {}", locator, e.getMessage());
            throw new RuntimeException(String.format("Failed to get inner HTML of label '%s': %s", 
                locator, e.getMessage()), e);
        }
    }

    /**
     * Get text content including hidden elements
     * @return Text content
     */
    @Step("Get text content of label: {this.locator}")
    public String getTextContent() {
        try {
            SelenideElement element = getElement();
            String textContent = element.innerText();
            logger.debug("Text content of label '{}': '{}'", locator, textContent);
            return textContent;
        } catch (Exception e) {
            logger.error("Failed to get text content of label '{}': {}", locator, e.getMessage());
            throw new RuntimeException(String.format("Failed to get text content of label '%s': %s", 
                locator, e.getMessage()), e);
        }
    }

    /**
     * Check if label is clickable
     * @return true if clickable, false otherwise
     */
    @Step("Check if label is clickable: {this.locator}")
    public boolean isClickable() {
        try {
            SelenideElement element = getElement();
            boolean clickable = element.isEnabled() && element.isDisplayed();
            logger.debug("Label '{}' is clickable: {}", locator, clickable);
            return clickable;
        } catch (Exception e) {
            logger.warn("Error checking if label '{}' is clickable: {}", locator, e.getMessage());
            return false;
        }
    }
}