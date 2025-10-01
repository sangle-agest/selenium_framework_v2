package com.myorg.automation.core.elements;

import com.codeborne.selenide.SelenideElement;
import com.myorg.automation.constants.FrameworkConstants;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checkbox element wrapper providing checkbox-specific functionality
 */
public class Checkbox extends BaseElement {
    private static final Logger logger = LoggerFactory.getLogger(Checkbox.class);

    /**
     * Constructor for Checkbox
     * @param locator Element locator
     * @param elementName Element name for logging
     */
    public Checkbox(String locator, String elementName) {
        super(locator, elementName);
        logger.debug(FrameworkConstants.ELEMENT_CREATED_LOG, FrameworkConstants.ELEMENT_TYPE_CHECKBOX, locator);
    }

    /**
     * Check the checkbox if not already checked
     * @return this Checkbox instance for method chaining
     */
    @Step("Check checkbox: {this.locator}")
    public Checkbox check() {
        try {
            if (!isChecked()) {
                SelenideElement element = getElement();
                element.click();
                logger.info(FrameworkConstants.CHECKBOX_CHECKED_LOG, locator);
            } else {
                logger.debug(FrameworkConstants.CHECKBOX_ALREADY_CHECKED_LOG, locator);
            }
        } catch (Exception e) {
            logger.error(FrameworkConstants.CHECKBOX_CHECK_ERROR_LOG, locator, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.CHECKBOX_CHECK_ERROR_LOG, locator, e.getMessage()), e);
        }
        return this;
    }

    /**
     * Uncheck the checkbox if currently checked
     * @return this Checkbox instance for method chaining
     */
    @Step("Uncheck checkbox: {this.locator}")
    public Checkbox uncheck() {
        try {
            if (isChecked()) {
                SelenideElement element = getElement();
                element.click();
                logger.info(FrameworkConstants.CHECKBOX_UNCHECKED_LOG, locator);
            } else {
                logger.debug(FrameworkConstants.CHECKBOX_ALREADY_UNCHECKED_LOG, locator);
            }
        } catch (Exception e) {
            logger.error(FrameworkConstants.CHECKBOX_UNCHECK_ERROR_LOG, locator, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.CHECKBOX_UNCHECK_ERROR_LOG, locator, e.getMessage()), e);
        }
        return this;
    }

    /**
     * Set checkbox state (check or uncheck)
     * @param checked true to check, false to uncheck
     * @return this Checkbox instance for method chaining
     */
    @Step("Set checkbox state to {checked}: {this.locator}")
    public Checkbox setChecked(boolean checked) {
        if (checked) {
            check();
        } else {
            uncheck();
        }
        return this;
    }

    /**
     * Check if the checkbox is currently checked
     * @return true if checked, false otherwise
     */
    @Step("Check if checkbox is checked: {this.locator}")
    public boolean isChecked() {
        try {
            SelenideElement element = getElement();
            boolean checked = element.isSelected() || element.getAttribute(FrameworkConstants.ATTRIBUTE_CHECKED) != null;
            logger.debug(FrameworkConstants.CHECKBOX_STATE_LOG, locator, checked);
            return checked;
        } catch (Exception e) {
            logger.error(FrameworkConstants.CHECKBOX_STATE_CHECK_ERROR_LOG, locator, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.CHECKBOX_STATE_CHECK_ERROR_LOG, locator, e.getMessage()), e);
        }
    }

    /**
     * Toggle checkbox state (check if unchecked, uncheck if checked)
     * @return this Checkbox instance for method chaining
     */
    @Step("Toggle checkbox state: {this.locator}")
    public Checkbox toggle() {
        try {
            boolean currentState = isChecked();
            SelenideElement element = getElement();
            element.click();
            logger.info(FrameworkConstants.CHECKBOX_TOGGLED_LOG, locator, currentState, !currentState);
        } catch (Exception e) {
            logger.error(FrameworkConstants.CHECKBOX_TOGGLE_ERROR_LOG, locator, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.CHECKBOX_TOGGLE_ERROR_LOG, locator, e.getMessage()), e);
        }
        return this;
    }

    /**
     * Verify checkbox is in expected state
     * @param expectedState Expected state (true for checked, false for unchecked)
     * @return this Checkbox instance for method chaining
     * @throws AssertionError if checkbox is not in expected state
     */
    @Step("Verify checkbox state is {expectedState}: {this.locator}")
    public Checkbox verifyState(boolean expectedState) {
        boolean actualState = isChecked();
        if (actualState != expectedState) {
            String message = String.format(FrameworkConstants.CHECKBOX_STATE_VERIFICATION_ERROR, 
                locator, expectedState, actualState);
            logger.error(message);
            throw new AssertionError(message);
        }
        logger.info(FrameworkConstants.CHECKBOX_STATE_VERIFIED_LOG, locator, expectedState);
        return this;
    }

    /**
     * Check if checkbox is enabled for interaction
     * @return true if enabled, false otherwise
     */
    @Step("Check if checkbox is enabled: {this.locator}")
    public boolean isEnabled() {
        try {
            SelenideElement element = getElement();
            boolean enabled = element.isEnabled();
            logger.debug(FrameworkConstants.CHECKBOX_ENABLED_LOG, locator, enabled);
            return enabled;
        } catch (Exception e) {
            logger.error(FrameworkConstants.CHECKBOX_ENABLED_CHECK_ERROR_LOG, locator, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.CHECKBOX_ENABLED_CHECK_ERROR_LOG, locator, e.getMessage()), e);
        }
    }
}