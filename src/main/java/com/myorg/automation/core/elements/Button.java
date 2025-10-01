package com.myorg.automation.core.elements;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Button element wrapper providing button-specific functionality
 */
public class Button extends BaseElement {
    private static final Logger logger = LoggerFactory.getLogger(Button.class);

    public Button(String locator, String elementName) {
        super(locator, elementName);
    }

    public Button(SelenideElement element, String elementName) {
        super(element, elementName);
    }

    @Step("Click button '{elementName}'")
    public Button click() {
        logger.info("Clicking button '{}'", elementName);
        waitToBeClickable();
        element.click();
        logger.info("Successfully clicked button '{}'", elementName);
        return this;
    }

    @Step("Double click button '{elementName}'")
    public Button doubleClick() {
        logger.info("Double clicking button '{}'", elementName);
        waitToBeClickable();
        element.doubleClick();
        logger.info("Successfully double clicked button '{}'", elementName);
        return this;
    }

    @Step("Right click button '{elementName}'")
    public Button rightClick() {
        logger.info("Right clicking button '{}'", elementName);
        waitToBeClickable();
        element.contextClick();
        logger.info("Successfully right clicked button '{}'", elementName);
        return this;
    }

    @Step("Click button '{elementName}' using JavaScript")
    public Button clickJS() {
        logger.info("Clicking button '{}' using JavaScript", elementName);
        element.click(com.codeborne.selenide.ClickOptions.usingJavaScript());
        logger.info("Successfully clicked button '{}' using JavaScript", elementName);
        return this;
    }

    @Step("Check if button '{elementName}' is enabled")
    public boolean isEnabled() {
        logger.info("Checking if button '{}' is enabled", elementName);
        boolean enabled = element.isEnabled();
        logger.info("Button '{}' is enabled: {}", elementName, enabled);
        return enabled;
    }

    @Step("Check if button '{elementName}' is disabled")
    public boolean isDisabled() {
        return !isEnabled();
    }

    @Step("Hover over button '{elementName}'")
    public Button hover() {
        logger.info("Hovering over button '{}'", elementName);
        element.hover();
        logger.info("Successfully hovered over button '{}'", elementName);
        return this;
    }

    @Step("Get button '{elementName}' CSS value for property '{propertyName}'")
    public String getCssValue(String propertyName) {
        logger.info("Getting CSS value '{}' from button '{}'", propertyName, elementName);
        String cssValue = element.getCssValue(propertyName);
        logger.info("CSS value '{}' from button '{}': '{}'", propertyName, elementName, cssValue);
        return cssValue;
    }
}