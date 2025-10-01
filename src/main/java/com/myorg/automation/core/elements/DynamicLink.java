package com.myorg.automation.core.elements;

import com.codeborne.selenide.SelenideElement;
import com.myorg.automation.constants.FrameworkConstants;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dynamic Link element that accepts parameters for locator formatting
 */
public class DynamicLink extends BaseElement {
    private static final Logger logger = LoggerFactory.getLogger(DynamicLink.class);

    /**
     * Constructor for DynamicLink
     * @param locator Element locator template with %s placeholders
     * @param elementName Element name for logging
     */
    public DynamicLink(String locator, String elementName) {
        super(locator, elementName);
        logger.debug(FrameworkConstants.ELEMENT_CREATED_LOG, FrameworkConstants.ELEMENT_TYPE_DYNAMIC_LINK, locator);
    }

    /**
     * Click dynamic link with parameter
     * @param parameter Parameter to format the locator
     * @return this DynamicLink instance for method chaining
     */
    @Step("Click dynamic link with parameter '{parameter}': {this.elementName}")
    public DynamicLink click(String parameter) {
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
     * Get href attribute of dynamic link
     * @param parameter Parameter to format the locator
     * @return href attribute value
     */
    @Step("Get href from dynamic link with parameter '{parameter}': {this.elementName}")
    public String getHref(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException(FrameworkConstants.DYNAMIC_ELEMENT_PARAMETER_ERROR);
        }
        
        try {
            String dynamicLocator = String.format(locator, parameter);
            SelenideElement element = createElement(dynamicLocator);
            String href = element.getAttribute(FrameworkConstants.HREF_ATTRIBUTE);
            logger.info("Retrieved href '{}' from dynamic link '{}' with parameter '{}'", href, locator, parameter);
            return href;
        } catch (Exception e) {
            logger.error("Failed to get href from dynamic link '{}' with parameter '{}': {}", 
                locator, parameter, e.getMessage());
            throw new RuntimeException(String.format("Failed to get href from dynamic link '%s' with parameter '%s': %s", 
                locator, parameter, e.getMessage()), e);
        }
    }

    /**
     * Get text from dynamic link
     * @param parameter Parameter to format the locator
     * @return Link text
     */
    @Step("Get text from dynamic link with parameter '{parameter}': {this.elementName}")
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
     * Open link in new tab
     * @param parameter Parameter to format the locator
     * @return this DynamicLink instance for method chaining
     */
    @Step("Open dynamic link in new tab with parameter '{parameter}': {this.elementName}")
    public DynamicLink openInNewTab(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException(FrameworkConstants.DYNAMIC_ELEMENT_PARAMETER_ERROR);
        }
        
        try {
            String href = getHref(parameter);
            if (href != null && !href.isEmpty()) {
                com.codeborne.selenide.Selenide.executeJavaScript(FrameworkConstants.WINDOW_OPEN_JS, href);
                logger.info("Opened dynamic link '{}' with parameter '{}' in new tab", locator, parameter);
            }
        } catch (Exception e) {
            logger.error("Failed to open dynamic link '{}' with parameter '{}' in new tab: {}", 
                locator, parameter, e.getMessage());
            throw new RuntimeException(String.format("Failed to open dynamic link '%s' with parameter '%s' in new tab: %s", 
                locator, parameter, e.getMessage()), e);
        }
        return this;
    }

    /**
     * Check if dynamic link is displayed
     * @param parameter Parameter to format the locator
     * @return true if displayed, false otherwise
     */
    @Step("Check if dynamic link is displayed with parameter '{parameter}': {this.elementName}")
    public boolean isDisplayed(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException(FrameworkConstants.DYNAMIC_ELEMENT_PARAMETER_ERROR);
        }
        
        try {
            String dynamicLocator = String.format(locator, parameter);
            SelenideElement element = createElement(dynamicLocator);
            boolean displayed = element.isDisplayed();
            logger.debug("Dynamic link '{}' with parameter '{}' is displayed: {}", locator, parameter, displayed);
            return displayed;
        } catch (Exception e) {
            logger.warn("Error checking if dynamic link '{}' with parameter '{}' is displayed: {}", 
                locator, parameter, e.getMessage());
            return false;
        }
    }

    /**
     * Wait for dynamic link to be visible
     * @param parameter Parameter to format the locator
     * @return this DynamicLink instance for method chaining
     */
    @Step("Wait for dynamic link to be visible with parameter '{parameter}': {this.elementName}")
    public DynamicLink waitToBeVisible(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException(FrameworkConstants.DYNAMIC_ELEMENT_PARAMETER_ERROR);
        }
        
        try {
            String dynamicLocator = String.format(locator, parameter);
            SelenideElement element = createElement(dynamicLocator);
            element.should(com.codeborne.selenide.Condition.visible);
            logger.debug("Dynamic link '{}' with parameter '{}' is now visible", locator, parameter);
        } catch (Exception e) {
            logger.error("Failed to wait for dynamic link '{}' with parameter '{}' to be visible: {}", 
                locator, parameter, e.getMessage());
            throw new RuntimeException(String.format("Failed to wait for dynamic link '%s' with parameter '%s' to be visible: %s", 
                locator, parameter, e.getMessage()), e);
        }
        return this;
    }
}