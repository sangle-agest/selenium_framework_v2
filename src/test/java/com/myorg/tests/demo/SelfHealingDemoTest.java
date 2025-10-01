package com.myorg.tests.demo;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.io.File;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Self-Healing Locator Demo Test Class
 * 
 * This class demonstrates the concept of self-healing locators by:
 * 1. Running tests against the original HTML page with working locators
 * 2. Running the same tests against modified HTML (with changed locators)
 * 3. Showing how tests can be made resilient to locator changes using multiple strategies
 * 
 * Note: This is a simplified demo that shows the concept without requiring full Healenium infrastructure
 */
@Epic("Self-Healing Demo")
@Feature("Resilient Locator Strategies")
public class SelfHealingDemoTest {
    private static final Logger logger = LoggerFactory.getLogger(SelfHealingDemoTest.class);
    
    // File paths for our test HTML pages
    private static final String ORIGINAL_HTML_PATH = "src/test/resources/healenium-demo/index.html";
    private static final String MODIFIED_HTML_PATH = "src/test/resources/healenium-demo/index-modified.html";
    
    private String currentPageUrl;

    @BeforeClass(alwaysRun = true)
    @Step("Setup test environment")
    public void setupTestEnvironment() {
        logger.info("Setting up test environment");
        
        try {
            // Configure Selenide
            Configuration.browser = "chrome";
            Configuration.headless = false;
            Configuration.browserSize = "1920x1080";
            Configuration.timeout = 10000;
            Configuration.screenshots = true;
            Configuration.savePageSource = false;
            
            // Check if Selenium Grid is running
            if (isSeleniumGridRunning()) {
                logger.info("Using remote WebDriver with Selenium Grid");
                Configuration.remote = "http://localhost:4444/wd/hub";
            } else {
                logger.info("Using local ChromeDriver (Selenium Grid not detected)");
                Configuration.remote = null;
            }
            
            logger.info("Test environment setup completed successfully");
            
        } catch (Exception e) {
            logger.error("Failed to setup test environment", e);
            throw new RuntimeException("Test environment setup failed", e);
        }
    }

    @AfterClass(alwaysRun = true)
    @Step("Cleanup test environment")
    public void cleanupTestEnvironment() {
        logger.info("Cleaning up test environment");
        
        try {
            closeWebDriver();
            logger.info("Test environment cleanup completed successfully");
        } catch (Exception e) {
            logger.error("Error during test environment cleanup", e);
        }
    }

    @Test(priority = 1)
    @Story("Original Page Test")
    @Description("Test form interaction with original HTML page (establishes baseline)")
    @Step("Test form submission on original page")
    public void testOriginalPageFormSubmission() {
        logger.info("Testing form submission on original page");
        
        // Open original HTML page
        openHtmlPage(ORIGINAL_HTML_PATH);
        
        // Verify page loaded
        $(byId("page-title")).shouldBe(visible).shouldHave(text("Healenium Self-Healing Demo"));
        
        // Fill form with original locators
        fillFormWithPrimaryLocators();
        
        // Submit form
        clickSubmitButtonWithPrimaryLocator();
        
        // Verify success message
        verifySuccessMessageWithPrimaryLocator();
        
        logger.info("Original page form submission test completed successfully");
    }

    @Test(priority = 2)
    @Story("Original Page Test")
    @Description("Test navigation links with original HTML page")
    @Step("Test navigation links on original page")
    public void testOriginalPageNavigation() {
        logger.info("Testing navigation on original page");
        
        // Open original HTML page if not already open
        if (!isCorrectPageOpen(ORIGINAL_HTML_PATH)) {
            openHtmlPage(ORIGINAL_HTML_PATH);
        }
        
        // Test navigation links with original locators
        testNavigationLinksWithPrimaryLocators();
        
        logger.info("Original page navigation test completed successfully");
    }

    @Test(priority = 10)
    @Story("Modified Page Test - Self-Healing Simulation")
    @Description("Test form interaction with modified HTML page using resilient locator strategies")
    @Step("Test form submission on modified page with self-healing strategies")
    public void testModifiedPageFormSubmissionWithSelfHealing() {
        logger.info("Testing form submission on modified page - demonstrating self-healing strategies");
        
        // Open modified HTML page (with changed locators)
        openHtmlPage(MODIFIED_HTML_PATH);
        
        // Verify page loaded (this should work as text content is the same)
        $(byText("Healenium Self-Healing Demo")).shouldBe(visible);
        
        // Fill form using resilient locator strategies
        fillFormWithResilientLocators();
        
        // Submit form using resilient locator strategy
        clickSubmitButtonWithResilientLocator();
        
        // Verify success message using resilient locator strategy
        verifySuccessMessageWithResilientLocator();
        
        logger.info("Modified page form submission test completed successfully");
    }

    @Test(priority = 11)
    @Story("Modified Page Test - Self-Healing Simulation")
    @Description("Test navigation links with modified HTML page using resilient locator strategies")
    @Step("Test navigation links on modified page with self-healing strategies")
    public void testModifiedPageNavigationWithSelfHealing() {
        logger.info("Testing navigation on modified page - demonstrating self-healing strategies");
        
        // Open modified HTML page if not already open
        if (!isCorrectPageOpen(MODIFIED_HTML_PATH)) {
            openHtmlPage(MODIFIED_HTML_PATH);
        }
        
        // Test navigation using resilient locator strategies
        testNavigationLinksWithResilientLocators();
        
        logger.info("Modified page navigation test completed successfully");
    }

    // Primary locator methods (work with original page)
    @Step("Fill form using primary locators")
    private void fillFormWithPrimaryLocators() {
        $(byId("username")).setValue("demo_user");
        $(byId("email")).setValue("demo@example.com");
        $(byId("country")).selectOption("United States");
        $(byId("tech")).click();
        $(byId("sports")).click();
        $(byId("comments")).setValue("Testing self-healing locator strategies");
    }

    @Step("Click submit button using primary locator")
    private void clickSubmitButtonWithPrimaryLocator() {
        $(byId("submit-btn")).click();
    }

    @Step("Verify success message using primary locator")
    private void verifySuccessMessageWithPrimaryLocator() {
        $(byId("success-message")).shouldBe(visible);
    }

    @Step("Test navigation links using primary locators")
    private void testNavigationLinksWithPrimaryLocators() {
        $$(byClassName("nav-link")).shouldHave(size(4));
        
        $(byId("home-link")).shouldBe(visible).click();
        sleep(500);
        
        $(byId("about-link")).shouldBe(visible).click();
        sleep(500);
        
        $(byId("contact-link")).shouldBe(visible).click();
        sleep(500);
        
        $(byId("help-link")).shouldBe(visible).click();
        sleep(500);
    }

    // Resilient locator methods (work with both original and modified pages)
    @Step("Fill form using resilient locator strategies")
    private void fillFormWithResilientLocators() {
        // Username field - try multiple strategies
        fillUsernameFieldResilient("demo_user");
        
        // Email field - try multiple strategies
        fillEmailFieldResilient("demo@example.com");
        
        // Country dropdown - try multiple strategies
        selectCountryResilient("United States");
        
        // Checkboxes - try multiple strategies
        selectInterestsResilient();
        
        // Comments field - try multiple strategies
        fillCommentsFieldResilient("Testing self-healing locator strategies");
    }

    private void fillUsernameFieldResilient(String value) {
        try {
            // Strategy 1: Try original ID
            $(byId("username")).setValue(value);
            logger.info("✓ Username filled using original locator (id='username')");
        } catch (Exception e1) {
            try {
                // Strategy 2: Try new ID
                $(byId("user-name-input")).setValue(value);
                logger.info("✓ Username filled using fallback locator (id='user-name-input')");
            } catch (Exception e2) {
                try {
                    // Strategy 3: Try by label text
                    $(byXpath("//label[contains(text(), 'Username')]/following-sibling::input")).setValue(value);
                    logger.info("✓ Username filled using XPath with label text");
                } catch (Exception e3) {
                    // Strategy 4: Try by placeholder
                    $(byAttribute("placeholder", "Enter username")).setValue(value);
                    logger.info("✓ Username filled using placeholder attribute");
                }
            }
        }
    }

    private void fillEmailFieldResilient(String value) {
        try {
            $(byId("email")).setValue(value);
            logger.info("✓ Email filled using original locator");
        } catch (Exception e1) {
            try {
                $(byId("email-address")).setValue(value);
                logger.info("✓ Email filled using fallback locator");
            } catch (Exception e2) {
                $(byAttribute("type", "email")).setValue(value);
                logger.info("✓ Email filled using input type");
            }
        }
    }

    private void selectCountryResilient(String value) {
        try {
            $(byId("country")).selectOption(value);
            logger.info("✓ Country selected using original locator");
        } catch (Exception e1) {
            try {
                $(byId("country-selector")).selectOption(value);
                logger.info("✓ Country selected using fallback locator");
            } catch (Exception e2) {
                $(byXpath("//label[contains(text(), 'Country')]/following-sibling::select")).selectOption(value);
                logger.info("✓ Country selected using XPath with label");
            }
        }
    }

    private void selectInterestsResilient() {
        // Technology checkbox
        try {
            $(byId("tech")).click();
            logger.info("✓ Tech checkbox selected using original locator");
        } catch (Exception e1) {
            try {
                $(byId("tech-checkbox")).click();
                logger.info("✓ Tech checkbox selected using fallback locator");
            } catch (Exception e2) {
                $(byXpath("//input[@type='checkbox' and @value='Technology']")).click();
                logger.info("✓ Tech checkbox selected using value attribute");
            }
        }
        
        // Sports checkbox
        try {
            $(byId("sports")).click();
            logger.info("✓ Sports checkbox selected using original locator");
        } catch (Exception e1) {
            try {
                $(byId("sports-checkbox")).click();
                logger.info("✓ Sports checkbox selected using fallback locator");
            } catch (Exception e2) {
                $(byXpath("//input[@type='checkbox' and @value='Sports']")).click();
                logger.info("✓ Sports checkbox selected using value attribute");
            }
        }
    }

    private void fillCommentsFieldResilient(String value) {
        try {
            $(byId("comments")).setValue(value);
            logger.info("✓ Comments filled using original locator");
        } catch (Exception e1) {
            try {
                $(byId("user-comments")).setValue(value);
                logger.info("✓ Comments filled using fallback locator");
            } catch (Exception e2) {
                $(byTagName("textarea")).setValue(value);
                logger.info("✓ Comments filled using tag name");
            }
        }
    }

    @Step("Click submit button using resilient locator strategy")
    private void clickSubmitButtonWithResilientLocator() {
        try {
            $(byId("submit-btn")).click();
            logger.info("✓ Submit button clicked using original locator");
        } catch (Exception e1) {
            try {
                $(byId("form-submit-button")).click();
                logger.info("✓ Submit button clicked using fallback locator");
            } catch (Exception e2) {
                try {
                    $(byXpath("//button[@type='submit']")).click();
                    logger.info("✓ Submit button clicked using type attribute");
                } catch (Exception e3) {
                    $(byText("Submit")).click();
                    logger.info("✓ Submit button clicked using button text");
                }
            }
        }
    }

    @Step("Verify success message using resilient locator strategy")
    private void verifySuccessMessageWithResilientLocator() {
        try {
            $(byId("success-message")).shouldBe(visible);
            logger.info("✓ Success message verified using original locator");
        } catch (Exception e1) {
            try {
                $(byId("form-success-message")).shouldBe(visible);
                logger.info("✓ Success message verified using fallback locator");
            } catch (Exception e2) {
                try {
                    $(byClassName("success")).shouldBe(visible);
                    logger.info("✓ Success message verified using class name");
                } catch (Exception e3) {
                    $(byText("Form submitted successfully!")).shouldBe(visible);
                    logger.info("✓ Success message verified using text content");
                }
            }
        }
    }

    @Step("Test navigation links using resilient locator strategies")
    private void testNavigationLinksWithResilientLocators() {
        ElementsCollection navLinks;
        try {
            navLinks = $$(byClassName("nav-link"));
            logger.info("✓ Navigation links found using original class name");
        } catch (Exception e) {
            navLinks = $$(byClassName("navigation-link"));
            logger.info("✓ Navigation links found using fallback class name");
        }
        
        navLinks.shouldHave(size(4));
        
        // Test each navigation link with resilient strategies
        clickNavigationLinkResilient("home", "Home");
        clickNavigationLinkResilient("about", "About");
        clickNavigationLinkResilient("contact", "Contact");
        clickNavigationLinkResilient("help", "Help");
    }

    private void clickNavigationLinkResilient(String linkType, String linkText) {
        try {
            $(byId(linkType + "-link")).click();
            logger.info("✓ {} link clicked using original locator", linkText);
        } catch (Exception e1) {
            try {
                $(byXpath("//a[contains(@href, '" + linkType + "')]")).click();
                logger.info("✓ {} link clicked using href attribute", linkText);
            } catch (Exception e2) {
                $(byText(linkText)).click();
                logger.info("✓ {} link clicked using link text", linkText);
            }
        }
        sleep(500);
    }

    // Utility methods
    private void openHtmlPage(String htmlPath) {
        File htmlFile = new File(htmlPath);
        if (!htmlFile.exists()) {
            throw new RuntimeException("HTML file not found: " + htmlPath);
        }
        
        currentPageUrl = "file://" + htmlFile.getAbsolutePath();
        open(currentPageUrl);
        
        // Wait for page to load
        sleep(2000);
        logger.info("Opened HTML page: {}", htmlPath);
    }

    private boolean isCorrectPageOpen(String expectedHtmlPath) {
        File expectedFile = new File(expectedHtmlPath);
        String expectedUrl = "file://" + expectedFile.getAbsolutePath();
        String currentUrl = Selenide.webdriver().driver().url();
        return expectedUrl.equals(currentUrl);
    }

    private boolean isSeleniumGridRunning() {
        try {
            java.net.URL url = new java.net.URL("http://localhost:4444/status");
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            logger.warn("Selenium Grid not detected: {}", e.getMessage());
            return false;
        }
    }
}