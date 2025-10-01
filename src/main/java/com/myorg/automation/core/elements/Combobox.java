package com.myorg.automation.core.elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.myorg.automation.constants.FrameworkConstants;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Combobox (dropdown/select) element wrapper providing selection functionality
 */
public class Combobox extends BaseElement {
    private static final Logger logger = LoggerFactory.getLogger(Combobox.class);

    /**
     * Constructor for Combobox
     * @param locator Element locator
     * @param elementName Element name for logging
     */
    public Combobox(String locator, String elementName) {
        super(locator, elementName);
        logger.debug(FrameworkConstants.ELEMENT_CREATED_LOG, FrameworkConstants.ELEMENT_TYPE_COMBOBOX, locator);
    }

    /**
     * Select option by visible text
     * @param optionText Text of the option to select
     * @return this Combobox instance for method chaining
     */
    @Step("Select option '{optionText}' in combobox: {this.elementName}")
    public Combobox selectByText(String optionText) {
        try {
            SelenideElement element = getElement();
            element.selectOption(optionText);
            logger.info(FrameworkConstants.COMBOBOX_OPTION_SELECTED_LOG, optionText, locator);
        } catch (Exception e) {
            logger.error(FrameworkConstants.COMBOBOX_SELECT_ERROR_LOG, optionText, locator, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.COMBOBOX_SELECT_ERROR_LOG, optionText, locator, e.getMessage()), e);
        }
        return this;
    }

    /**
     * Select option by value attribute
     * @param value Value attribute of the option to select
     * @return this Combobox instance for method chaining
     */
    @Step("Select option by value '{value}' in combobox: {this.elementName}")
    public Combobox selectByValue(String value) {
        try {
            SelenideElement element = getElement();
            element.selectOptionByValue(value);
            logger.info(FrameworkConstants.COMBOBOX_OPTION_SELECTED_LOG, value, locator);
        } catch (Exception e) {
            logger.error(FrameworkConstants.COMBOBOX_SELECT_ERROR_LOG, value, locator, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.COMBOBOX_SELECT_ERROR_LOG, value, locator, e.getMessage()), e);
        }
        return this;
    }

    /**
     * Select option by index
     * @param index Index of the option to select (0-based)
     * @return this Combobox instance for method chaining
     */
    @Step("Select option at index {index} in combobox: {this.elementName}")
    public Combobox selectByIndex(int index) {
        try {
            SelenideElement element = getElement();
            element.selectOption(index);
            logger.info(FrameworkConstants.COMBOBOX_OPTION_BY_INDEX_SELECTED_LOG, index, locator);
        } catch (Exception e) {
            logger.error(FrameworkConstants.COMBOBOX_SELECT_BY_INDEX_ERROR_LOG, index, locator, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.COMBOBOX_SELECT_BY_INDEX_ERROR_LOG, index, locator, e.getMessage()), e);
        }
        return this;
    }

    /**
     * Get currently selected option text
     * @return Selected option text
     */
    @Step("Get selected option from combobox: {this.elementName}")
    public String getSelectedOption() {
        try {
            SelenideElement element = getElement();
            String selectedText = element.getSelectedOption().getText();
            logger.debug("Selected option in combobox '{}': '{}'", locator, selectedText);
            return selectedText;
        } catch (Exception e) {
            logger.error(FrameworkConstants.COMBOBOX_GET_SELECTED_ERROR_LOG, locator, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.COMBOBOX_GET_SELECTED_ERROR_LOG, locator, e.getMessage()), e);
        }
    }

    /**
     * Get currently selected option value
     * @return Selected option value
     */
    @Step("Get selected option value from combobox: {this.elementName}")
    public String getSelectedValue() {
        try {
            SelenideElement element = getElement();
            String selectedValue = element.getSelectedOption().getValue();
            logger.debug("Selected value in combobox '{}': '{}'", locator, selectedValue);
            return selectedValue;
        } catch (Exception e) {
            logger.error(FrameworkConstants.COMBOBOX_GET_SELECTED_ERROR_LOG, locator, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.COMBOBOX_GET_SELECTED_ERROR_LOG, locator, e.getMessage()), e);
        }
    }

    /**
     * Get all available options
     * @return List of option texts
     */
    @Step("Get all options from combobox: {this.elementName}")
    public List<String> getAllOptions() {
        try {
            SelenideElement element = getElement();
            ElementsCollection options = element.$$("option");
            List<String> optionTexts = options.texts();
            logger.debug("All options in combobox '{}': {}", locator, optionTexts);
            return optionTexts;
        } catch (Exception e) {
            logger.error(FrameworkConstants.COMBOBOX_GET_OPTIONS_ERROR_LOG, locator, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.COMBOBOX_GET_OPTIONS_ERROR_LOG, locator, e.getMessage()), e);
        }
    }

    /**
     * Check if option exists
     * @param optionText Option text to check
     * @return true if option exists, false otherwise
     */
    @Step("Check if option '{optionText}' exists in combobox: {this.elementName}")
    public boolean hasOption(String optionText) {
        try {
            List<String> options = getAllOptions();
            boolean exists = options.contains(optionText);
            logger.debug("Option '{}' exists in combobox '{}': {}", optionText, locator, exists);
            return exists;
        } catch (Exception e) {
            logger.error("Failed to check if option '{}' exists in combobox '{}': {}", optionText, locator, e.getMessage());
            return false;
        }
    }

    /**
     * Clear selection (if multiple select)
     * @return this Combobox instance for method chaining
     */
    @Step("Clear selection in combobox: {this.elementName}")
    public Combobox clear() {
        try {
            SelenideElement element = getElement();
            element.clear();
            logger.info(FrameworkConstants.COMBOBOX_CLEARED_LOG, locator);
        } catch (Exception e) {
            logger.error(FrameworkConstants.COMBOBOX_CLEAR_ERROR_LOG, locator, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.COMBOBOX_CLEAR_ERROR_LOG, locator, e.getMessage()), e);
        }
        return this;
    }
}