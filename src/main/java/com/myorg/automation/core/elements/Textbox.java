package com.myorg.automation.core.elements;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Textbox element wrapper providing input field functionality
 */
public class Textbox extends BaseElement {
    private static final Logger logger = LoggerFactory.getLogger(Textbox.class);

    public Textbox(String locator, String elementName) {
        super(locator, elementName);
    }

    public Textbox(SelenideElement element, String elementName) {
        super(element, elementName);
    }

    @Step("Type '{text}' in textbox '{elementName}'")
    public Textbox type(String text) {
        logger.info("Typing '{}' in textbox '{}'", text, elementName);
        waitToBeVisible();
        element.sendKeys(text);
        logger.info("Successfully typed '{}' in textbox '{}'", text, elementName);
        return this;
    }

    @Step("Set value '{value}' in textbox '{elementName}'")
    public Textbox setValue(String value) {
        logger.info("Setting value '{}' in textbox '{}'", value, elementName);
        waitToBeVisible();
        element.setValue(value);
        logger.info("Successfully set value '{}' in textbox '{}'", value, elementName);
        return this;
    }

    @Step("Clear textbox '{elementName}'")
    public Textbox clear() {
        logger.info("Clearing textbox '{}'", elementName);
        waitToBeVisible();
        element.clear();
        logger.info("Successfully cleared textbox '{}'", elementName);
        return this;
    }

    @Step("Clear and type '{text}' in textbox '{elementName}'")
    public Textbox clearAndType(String text) {
        logger.info("Clearing and typing '{}' in textbox '{}'", text, elementName);
        clear();
        type(text);
        return this;
    }

    @Step("Get value from textbox '{elementName}'")
    public String getValue() {
        logger.info("Getting value from textbox '{}'", elementName);
        String value = element.getValue();
        logger.info("Value from textbox '{}': '{}'", elementName, value);
        return value;
    }

    @Step("Get placeholder text from textbox '{elementName}'")
    public String getPlaceholder() {
        logger.info("Getting placeholder from textbox '{}'", elementName);
        String placeholder = getAttribute("placeholder");
        logger.info("Placeholder from textbox '{}': '{}'", elementName, placeholder);
        return placeholder;
    }

    @Step("Check if textbox '{elementName}' is empty")
    public boolean isEmpty() {
        String value = getValue();
        boolean empty = value == null || value.trim().isEmpty();
        logger.info("Textbox '{}' is empty: {}", elementName, empty);
        return empty;
    }

    @Step("Check if textbox '{elementName}' is readonly")
    public boolean isReadonly() {
        logger.info("Checking if textbox '{}' is readonly", elementName);
        String readonly = getAttribute("readonly");
        boolean isReadonly = readonly != null && !readonly.isEmpty();
        logger.info("Textbox '{}' is readonly: {}", elementName, isReadonly);
        return isReadonly;
    }

    @Step("Press Enter in textbox '{elementName}'")
    public Textbox pressEnter() {
        logger.info("Pressing Enter in textbox '{}'", elementName);
        element.pressEnter();
        logger.info("Successfully pressed Enter in textbox '{}'", elementName);
        return this;
    }

    @Step("Press Tab in textbox '{elementName}'")
    public Textbox pressTab() {
        logger.info("Pressing Tab in textbox '{}'", elementName);
        element.pressTab();
        logger.info("Successfully pressed Tab in textbox '{}'", elementName);
        return this;
    }

    @Step("Press Escape in textbox '{elementName}'")
    public Textbox pressEscape() {
        logger.info("Pressing Escape in textbox '{}'", elementName);
        element.pressEscape();
        logger.info("Successfully pressed Escape in textbox '{}'", elementName);
        return this;
    }

    @Step("Focus on textbox '{elementName}'")
    public Textbox focus() {
        logger.info("Focusing on textbox '{}'", elementName);
        element.click();
        logger.info("Successfully focused on textbox '{}'", elementName);
        return this;
    }
}