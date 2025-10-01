package com.myorg.automation.core.elements;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;

/**
 * Base element class that provides common functionality for all web elements.
 * All custom elements should extend this class.
 */
public abstract class BaseElement {
    protected static final Logger logger = LoggerFactory.getLogger(BaseElement.class);
    
    protected SelenideElement element;
    protected String elementName;
    protected String locator;

    public BaseElement(String locator, String elementName) {
        this.locator = locator;
        this.elementName = elementName;
        this.element = createElement(locator);
    }

    public BaseElement(SelenideElement element, String elementName) {
        this.element = element;
        this.elementName = elementName;
        this.locator = "SelenideElement";
    }

    /**
     * Creates a SelenideElement from a locator string
     * Supports various locator types: xpath, css, id, class, etc.
     */
    protected SelenideElement createElement(String locatorString) {
        if (locatorString.startsWith("xpath=")) {
            return $x(locatorString.substring(6));
        } else if (locatorString.startsWith("css=")) {
            return $(locatorString.substring(4));
        } else if (locatorString.startsWith("id=")) {
            return $("#" + locatorString.substring(3));
        } else if (locatorString.startsWith("class=")) {
            return $("." + locatorString.substring(6));
        } else if (locatorString.startsWith("name=")) {
            return $("[name='" + locatorString.substring(5) + "']");
        } else {
            // Default to CSS selector
            return $(locatorString);
        }
    }

    /**
     * XPath helper method
     */
    protected SelenideElement $x(String xpath) {
        return com.codeborne.selenide.Selenide.$x(xpath);
    }

    @Step("Check if element '{elementName}' is displayed")
    public boolean isDisplayed() {
        try {
            logger.info("Checking if element '{}' is displayed", elementName);
            boolean displayed = element.isDisplayed();
            logger.info("Element '{}' is displayed: {}", elementName, displayed);
            return displayed;
        } catch (Exception e) {
            logger.warn("Element '{}' is not displayed: {}", elementName, e.getMessage());
            return false;
        }
    }

    @Step("Check if element '{elementName}' exists")
    public boolean exists() {
        try {
            logger.info("Checking if element '{}' exists", elementName);
            boolean exists = element.exists();
            logger.info("Element '{}' exists: {}", elementName, exists);
            return exists;
        } catch (Exception e) {
            logger.warn("Element '{}' does not exist: {}", elementName, e.getMessage());
            return false;
        }
    }

    @Step("Wait for element '{elementName}' to be visible")
    public BaseElement waitToBeVisible() {
        logger.info("Waiting for element '{}' to be visible", elementName);
        element.shouldBe(com.codeborne.selenide.Condition.visible);
        logger.info("Element '{}' is now visible", elementName);
        return this;
    }

    @Step("Wait for element '{elementName}' to be clickable")
    public BaseElement waitToBeClickable() {
        logger.info("Waiting for element '{}' to be clickable", elementName);
        element.shouldBe(com.codeborne.selenide.Condition.enabled);
        logger.info("Element '{}' is now clickable", elementName);
        return this;
    }

    @Step("Get text from element '{elementName}'")
    public String getText() {
        logger.info("Getting text from element '{}'", elementName);
        String text = element.getText().trim();
        logger.info("Text from element '{}': '{}'", elementName, text);
        return text;
    }

    @Step("Get attribute '{attributeName}' from element '{elementName}'")
    public String getAttribute(String attributeName) {
        logger.info("Getting attribute '{}' from element '{}'", attributeName, elementName);
        String attributeValue = element.getAttribute(attributeName);
        logger.info("Attribute '{}' from element '{}': '{}'", attributeName, elementName, attributeValue);
        return attributeValue;
    }

    @Step("Scroll to element '{elementName}'")
    public BaseElement scrollTo() {
        logger.info("Scrolling to element '{}'", elementName);
        element.scrollTo();
        logger.info("Scrolled to element '{}'", elementName);
        return this;
    }

    @Step("Highlight element '{elementName}'")
    public BaseElement highlight() {
        logger.info("Highlighting element '{}'", elementName);
        element.highlight();
        return this;
    }

    // Getters
    public SelenideElement getElement() {
        return element;
    }

    public String getElementName() {
        return elementName;
    }

    public String getLocator() {
        return locator;
    }
}