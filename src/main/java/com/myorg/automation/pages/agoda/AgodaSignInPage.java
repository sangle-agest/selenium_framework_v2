package com.myorg.automation.pages.agoda;

import com.myorg.automation.pages.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AgodaSignInPage - Page object for Agoda's sign in page
 * 
 * Handles:
 * - User authentication
 * - Login form interactions
 * - Social login options
 * - Password recovery
 */
public class AgodaSignInPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(AgodaSignInPage.class);
    private static final String PAGE_NAME = "agoda_signin";
    
    public AgodaSignInPage() {
        super(PAGE_NAME);
    }
    
    /**
     * Enter email address
     * @param email The email address to enter
     * @return AgodaSignInPage instance for method chaining
     */
    public AgodaSignInPage enterEmail(String email) {
        logger.info("Entering email: {}", email);
        typeText("email_input", email);
        return this;
    }
    
    /**
     * Enter password
     * @param password The password to enter
     * @return AgodaSignInPage instance for method chaining
     */
    public AgodaSignInPage enterPassword(String password) {
        logger.info("Entering password");
        typeText("password_input", password);
        return this;
    }
    
    /**
     * Click sign in button
     * @return AgodaHomePage instance (assuming successful login)
     */
    public AgodaHomePage clickSignIn() {
        logger.info("Clicking sign in button");
        clickElement("signin_button");
        return new AgodaHomePage();
    }
    
    /**
     * Sign in with email and password
     * @param email The email address
     * @param password The password
     * @return AgodaHomePage instance (assuming successful login)
     */
    public AgodaHomePage signIn(String email, String password) {
        logger.info("Signing in with email: {}", email);
        return enterEmail(email)
                .enterPassword(password)
                .clickSignIn();
    }
    
    /**
     * Click forgot password link
     * @return AgodaSignInPage instance for method chaining
     */
    public AgodaSignInPage clickForgotPassword() {
        logger.info("Clicking forgot password link");
        clickElement("forgot_password_link");
        return this;
    }
    
    /**
     * Sign in with Google
     * @return AgodaHomePage instance (assuming successful login)
     */
    public AgodaHomePage signInWithGoogle() {
        logger.info("Signing in with Google");
        clickElement("google_signin_button");
        return new AgodaHomePage();
    }
    
    /**
     * Sign in with Facebook
     * @return AgodaHomePage instance (assuming successful login)
     */
    public AgodaHomePage signInWithFacebook() {
        logger.info("Signing in with Facebook");
        clickElement("facebook_signin_button");
        return new AgodaHomePage();
    }
    
    /**
     * Navigate to sign up page
     * @return AgodaSignUpPage instance
     */
    public AgodaSignUpPage goToSignUp() {
        logger.info("Navigating to sign up page");
        clickElement("signup_link");
        return new AgodaSignUpPage();
    }
    
    /**
     * Get current email value
     * @return The email value
     */
    public String getEmailValue() {
        String email = getElementAttribute("email_input", "value");
        logger.debug("Current email value: {}", email);
        return email;
    }
    
    /**
     * Check if sign in button is enabled
     * @return true if sign in button is enabled, false otherwise
     */
    public boolean isSignInButtonEnabled() {
        boolean isEnabled = isElementEnabled("signin_button");
        logger.debug("Sign in button enabled: {}", isEnabled);
        return isEnabled;
    }
    
    /**
     * Check if error message is displayed
     * @return true if error message is visible, false otherwise
     */
    public boolean isErrorMessageDisplayed() {
        boolean isDisplayed = isElementVisible("error_message");
        logger.debug("Error message displayed: {}", isDisplayed);
        return isDisplayed;
    }
    
    /**
     * Get error message text
     * @return The error message text
     */
    public String getErrorMessage() {
        String errorMessage = getElementText("error_message");
        logger.debug("Error message: {}", errorMessage);
        return errorMessage;
    }
}