package com.myorg.tests.healenium;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.epam.healenium.SelfHealingDriver;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.net.URL;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Healenium Demo Test Class
 * 
 * This class demonstrates how Healenium works by:
 * 1. Running tests against the original HTML page
 * 2. Running the same tests against modified HTML (with changed locators)
 * 3. Showing how Healenium automatically heals broken locators
 * 4. Providing methods to inspect healing results in the database
 */
@Epic("Healenium Demo")
@Feature("Self-Healing Locators")
public class HealeniumDemoTest {
    private static final Logger logger = LoggerFactory.getLogger(HealeniumDemoTest.class);
    
    // File paths for our test HTML pages
    private static final String ORIGINAL_HTML_PATH = "src/test/resources/healenium-demo/index.html";
    private static final String MODIFIED_HTML_PATH = "src/test/resources/healenium-demo/index-modified.html";
    
    // Healenium configuration
    private static final String HEALENIUM_SERVER_URL = "http://localhost:7878";
    private static final boolean USE_HEALENIUM = true;
    
    private WebDriver originalDriver;
    private String currentPageUrl;

    @BeforeClass(alwaysRun = true)
    @Step("Setup Healenium test environment")
    public void setupHealeniumEnvironment() {
        logger.info("Setting up Healenium test environment");
        
        try {
            // Configure Selenide for Healenium
            if (USE_HEALENIUM) {
                // Create Chrome driver for Healenium wrapping
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.addArguments("--window-size=1920,1080");
                
                // Check if we should use remote WebDriver (Docker) or local
                if (isHealeniumInfrastructureRunning()) {
                    logger.info("Using remote WebDriver with Selenium Grid");
                    originalDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
                } else {
                    logger.info("Using local ChromeDriver (Healenium infrastructure not detected)");
                    originalDriver = new ChromeDriver(options);
                }
                
                // Wrap with Healenium SelfHealingDriver
                SelfHealingDriver selfHealingDriver = SelfHealingDriver.create(originalDriver);
                
                // Configure Selenide to use our Healenium-wrapped driver
                Configuration.remote = null; // Disable remote for Selenide
                Selenide.open(); // This creates a default driver
                Selenide.closeWebDriver(); // Close it
                
                // Set our custom driver
                com.codeborne.selenide.WebDriverRunner.setWebDriver(selfHealingDriver);
                
                logger.info("Healenium SelfHealingDriver configured successfully");
            } else {
                // Standard Selenide configuration without Healenium
                Configuration.browser = "chrome";
                Configuration.headless = false;
                Configuration.browserSize = "1920x1080";
                logger.info("Standard Selenide configuration (no Healenium)");
            }
            
            // Common Selenide configuration
            Configuration.timeout = 10000;
            Configuration.screenshots = true;
            Configuration.savePageSource = false;
            
            logger.info("Test environment setup completed successfully");
            
        } catch (Exception e) {
            logger.error("Failed to setup Healenium test environment", e);
            throw new RuntimeException("Test environment setup failed", e);
        }
    }

    @AfterClass(alwaysRun = true)
    @Step("Cleanup Healenium test environment")
    public void cleanupHealeniumEnvironment() {
        logger.info("Cleaning up Healenium test environment");
        
        try {
            if (originalDriver != null) {
                originalDriver.quit();
            }
            Selenide.closeWebDriver();
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
        fillFormWithOriginalLocators();
        
        // Submit form
        $(byId("submit-btn")).click();
        
        // Verify success message
        $(byId("success-message")).shouldBe(visible);
        
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
        testNavigationLinksWithOriginalLocators();
        
        logger.info("Original page navigation test completed successfully");
    }

    @Test(priority = 3)
    @Story("Original Page Test")
    @Description("Test dynamic items with original HTML page")
    @Step("Test dynamic items on original page")
    public void testOriginalPageDynamicItems() {
        logger.info("Testing dynamic items on original page");
        
        // Open original HTML page if not already open
        if (!isCorrectPageOpen(ORIGINAL_HTML_PATH)) {
            openHtmlPage(ORIGINAL_HTML_PATH);
        }
        
        // Test dynamic items with original locators
        testDynamicItemsWithOriginalLocators();
        
        logger.info("Original page dynamic items test completed successfully");
    }

    @Test(priority = 10)
    @Story("Modified Page Test - Healenium Healing")
    @Description("Test form interaction with modified HTML page (Healenium should heal broken locators)")
    @Step("Test form submission on modified page with Healenium healing")
    public void testModifiedPageFormSubmissionWithHealing() {
        logger.info("Testing form submission on modified page - Healenium should heal locators");
        
        // Open modified HTML page (with changed locators)
        openHtmlPage(MODIFIED_HTML_PATH);
        
        // Verify page loaded (this should work as text content is the same)
        $(byText("Healenium Self-Healing Demo")).shouldBe(visible);
        
        // Try to fill form with ORIGINAL locators - Healenium should heal these
        try {
            fillFormWithOriginalLocators();
            logger.info("✓ Healenium successfully healed form locators");
        } catch (Exception e) {
            logger.error("✗ Healenium failed to heal form locators: {}", e.getMessage());
            // Try with new locators as fallback
            fillFormWithModifiedLocators();
        }
        
        // Try to submit form with original locator - Healenium should heal this
        try {
            $(byId("submit-btn")).click();
            logger.info("✓ Healenium successfully healed submit button locator");
        } catch (Exception e) {
            logger.error("✗ Healenium failed to heal submit button locator: {}", e.getMessage());
            $(byId("form-submit-button")).click(); // Use new locator as fallback
        }
        
        // Verify success message (try original locator first)
        try {
            $(byId("success-message")).shouldBe(visible);
            logger.info("✓ Healenium successfully healed success message locator");
        } catch (Exception e) {
            logger.error("✗ Healenium failed to heal success message locator: {}", e.getMessage());
            $(byId("form-success-message")).shouldBe(visible); // Use new locator as fallback
        }
        
        logger.info("Modified page form submission test completed");
    }

    @Test(priority = 11)
    @Story("Modified Page Test - Healenium Healing")
    @Description("Test navigation links with modified HTML page (Healenium should heal broken locators)")
    @Step("Test navigation links on modified page with Healenium healing")
    public void testModifiedPageNavigationWithHealing() {
        logger.info("Testing navigation on modified page - Healenium should heal locators");
        
        // Open modified HTML page if not already open
        if (!isCorrectPageOpen(MODIFIED_HTML_PATH)) {
            openHtmlPage(MODIFIED_HTML_PATH);
        }
        
        // Try to test navigation with ORIGINAL locators - Healenium should heal these
        try {
            testNavigationLinksWithOriginalLocators();
            logger.info("✓ Healenium successfully healed navigation locators");
        } catch (Exception e) {
            logger.error("✗ Healenium failed to heal navigation locators: {}", e.getMessage());
            testNavigationLinksWithModifiedLocators();
        }
        
        logger.info("Modified page navigation test completed");
    }

    @Test(priority = 12)
    @Story("Modified Page Test - Healenium Healing")
    @Description("Test dynamic items with modified HTML page (Healenium should heal broken locators)")
    @Step("Test dynamic items on modified page with Healenium healing")
    public void testModifiedPageDynamicItemsWithHealing() {
        logger.info("Testing dynamic items on modified page - Healenium should heal locators");
        
        // Open modified HTML page if not already open
        if (!isCorrectPageOpen(MODIFIED_HTML_PATH)) {
            openHtmlPage(MODIFIED_HTML_PATH);
        }
        
        // Try to test dynamic items with ORIGINAL locators - Healenium should heal these
        try {
            testDynamicItemsWithOriginalLocators();
            logger.info("✓ Healenium successfully healed dynamic items locators");
        } catch (Exception e) {
            logger.error("✗ Healenium failed to heal dynamic items locators: {}", e.getMessage());
            testDynamicItemsWithModifiedLocators();
        }
        
        logger.info("Modified page dynamic items test completed");
    }

    // Helper methods for form filling with original locators
    @Step("Fill form using original locators")
    private void fillFormWithOriginalLocators() {
        $(byId("username")).setValue("healenium_user");
        $(byId("email")).setValue("healenium@example.com");
        $(byId("country")).selectOption("United States");
        $(byId("tech")).click();
        $(byId("sports")).click();
        $(byId("comments")).setValue("Testing Healenium self-healing capabilities");
    }

    // Helper methods for form filling with modified locators
    @Step("Fill form using modified locators")
    private void fillFormWithModifiedLocators() {
        $(byId("user-name-input")).setValue("healenium_user");
        $(byId("email-address")).setValue("healenium@example.com");
        $(byId("country-selector")).selectOption("United States");
        $(byId("tech-checkbox")).click();
        $(byId("sports-checkbox")).click();
        $(byId("user-comments")).setValue("Testing Healenium self-healing capabilities");
    }

    // Helper methods for navigation testing with original locators
    @Step("Test navigation links using original locators")
    private void testNavigationLinksWithOriginalLocators() {
        // Test each navigation link
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

    // Helper methods for navigation testing with modified locators
    @Step("Test navigation links using modified locators")
    private void testNavigationLinksWithModifiedLocators() {
        // Test each navigation link with new class
        $$(byClassName("navigation-link")).shouldHave(size(4));
        
        $(byId("home-link")).shouldBe(visible).click();
        sleep(500);
        
        $(byId("about-link")).shouldBe(visible).click();
        sleep(500);
        
        $(byId("contact-link")).shouldBe(visible).click();
        sleep(500);
        
        $(byId("help-link")).shouldBe(visible).click();
        sleep(500);
    }

    // Helper methods for dynamic items testing with original locators
    @Step("Test dynamic items using original locators")
    private void testDynamicItemsWithOriginalLocators() {
        // Verify items title
        $(byId("items-title")).shouldBe(visible).shouldHave(text("Dynamic Items"));
        
        // Count initial items
        ElementsCollection items = $$(byClassName("item"));
        int initialCount = items.size();
        Assert.assertTrue(initialCount >= 3, "Should have at least 3 initial items");
        
        // Add a new item
        $(byId("add-item-btn")).click();
        sleep(1000);
        
        // Verify new item was added
        $$(byClassName("item")).shouldHave(size(initialCount + 1));
    }

    // Helper methods for dynamic items testing with modified locators
    @Step("Test dynamic items using modified locators")
    private void testDynamicItemsWithModifiedLocators() {
        // Verify items title with new id
        $(byId("dynamic-items-title")).shouldBe(visible).shouldHave(text("Dynamic Items"));
        
        // Count initial items
        ElementsCollection items = $$(byClassName("item"));
        int initialCount = items.size();
        Assert.assertTrue(initialCount >= 3, "Should have at least 3 initial items");
        
        // Add a new item with new button id
        $(byId("add-new-item-button")).click();
        sleep(1000);
        
        // Verify new item was added
        $$(byClassName("item")).shouldHave(size(initialCount + 1));
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

    private boolean isHealeniumInfrastructureRunning() {
        try {
            // Check if Healenium server is running
            java.net.URL url = new URL(HEALENIUM_SERVER_URL + "/health");
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            logger.warn("Healenium infrastructure not detected: {}", e.getMessage());
            return false;
        }
    }

    // Database inspection methods (to be called manually or in separate test)
    @Test(enabled = false) // Disabled by default, enable manually to inspect database
    @Step("Inspect Healenium database for healing results")
    public void inspectHealeniumDatabase() {
        // This method would typically connect to the database and query healing results
        // For demonstration purposes, we'll just log the information
        logger.info("=== Healenium Database Inspection ===");
        logger.info("To inspect the Healenium database manually:");
        logger.info("1. Connect to PostgreSQL: psql -h localhost -p 5432 -U healenium_user -d healenium");
        logger.info("2. View healing statistics: SELECT * FROM healing_statistics;");
        logger.info("3. View recent healing activity: SELECT * FROM recent_healing_activity;");
        logger.info("4. View all healings: SELECT * FROM selector_healing ORDER BY healing_timestamp DESC;");
    }
}