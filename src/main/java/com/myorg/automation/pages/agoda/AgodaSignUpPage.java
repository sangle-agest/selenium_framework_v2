package com.myorg.automation.pages.agoda;

import com.myorg.automation.pages.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AgodaSignUpPage - Page object for Agoda's sign up page
 * 
 * Handles:
 * - User registration
 * - Account creation
 * - Email verification
 */
public class AgodaSignUpPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(AgodaSignUpPage.class);
    private static final String PAGE_NAME = "agoda_signup";
    
    public AgodaSignUpPage() {
        super(PAGE_NAME);
    }
    
    /**
     * Enter email for registration
     * @param email The email to enter
     * @return AgodaSignUpPage instance for method chaining
     */
    public AgodaSignUpPage enterEmail(String email) {
        logger.info("Entering email for registration: {}", email);
        typeText("email_input", email);
        return this;
    }
    
    /**
     * Enter password for registration
     * @param password The password to enter
     * @return AgodaSignUpPage instance for method chaining
     */
    public AgodaSignUpPage enterPassword(String password) {
        logger.info("Entering password for registration");
        typeText("password_input", password);
        return this;
    }
    
    /**
     * Confirm password
     * @param password The password to confirm
     * @return AgodaSignUpPage instance for method chaining
     */
    public AgodaSignUpPage confirmPassword(String password) {
        logger.info("Confirming password");
        typeText("confirm_password_input", password);
        return this;
    }
    
    /**
     * Enter first name
     * @param firstName The first name to enter
     * @return AgodaSignUpPage instance for method chaining
     */
    public AgodaSignUpPage enterFirstName(String firstName) {
        logger.info("Entering first name: {}", firstName);
        typeText("first_name_input", firstName);
        return this;
    }
    
    /**
     * Enter last name
     * @param lastName The last name to enter
     * @return AgodaSignUpPage instance for method chaining
     */
    public AgodaSignUpPage enterLastName(String lastName) {
        logger.info("Entering last name: {}", lastName);
        typeText("last_name_input", lastName);
        return this;
    }
    
    /**
     * Click sign up button
     * @return AgodaHomePage instance (assuming successful registration)
     */
    public AgodaHomePage clickSignUp() {
        logger.info("Clicking sign up button");
        clickElement("signup_button");
        return new AgodaHomePage();
    }
    
    /**
     * Complete registration process
     * @param email The email
     * @param password The password
     * @param firstName The first name
     * @param lastName The last name
     * @return AgodaHomePage instance (assuming successful registration)
     */
    public AgodaHomePage signUp(String email, String password, String firstName, String lastName) {
        logger.info("Completing registration for: {} {}", firstName, lastName);
        return enterEmail(email)
                .enterPassword(password)
                .confirmPassword(password)
                .enterFirstName(firstName)
                .enterLastName(lastName)
                .clickSignUp();
    }
    
    /**
     * Check if sign up button is enabled
     * @return true if sign up button is enabled, false otherwise
     */
    public boolean isSignUpButtonEnabled() {
        boolean isEnabled = isElementEnabled("signup_button");
        logger.debug("Sign up button enabled: {}", isEnabled);
        return isEnabled;
    }
}