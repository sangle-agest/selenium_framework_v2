package com.myorg.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.myorg.automation.core.PageObjectFactory;
import com.myorg.automation.utils.TestDataResolver;
import com.myorg.automation.config.ConfigManager;
import com.myorg.automation.constants.FrameworkConstants;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import static com.codeborne.selenide.Selenide.closeWebDriver;

/**
 * Base test class providing common setup and teardown functionality
 */
public abstract class BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser", "headless", "timeout", "baseUrl"})
    @Step("Setup test environment")
    public void setupTestEnvironment(@Optional String browser,
                                   @Optional String headless,
                                   @Optional String timeout,
                                   @Optional String baseUrl) {
        logger.info("Setting up test environment");
        
        // Use ConfigManager with parameter overrides
        String finalBrowser = browser != null ? browser : ConfigManager.getBrowser();
        boolean finalHeadless = headless != null ? Boolean.parseBoolean(headless) : ConfigManager.isHeadless();
        long finalTimeout = timeout != null ? Long.parseLong(timeout) : ConfigManager.getBrowserTimeout();
        String finalBaseUrl = baseUrl != null ? baseUrl : ConfigManager.getBaseUrl();
        
        logger.info("Browser: {}, Headless: {}, Timeout: {}, Base URL: {}", 
            finalBrowser, finalHeadless, finalTimeout, finalBaseUrl);

        // Configure Selenide using ConfigManager
        Configuration.browser = finalBrowser;
        Configuration.headless = finalHeadless;
        Configuration.browserSize = ConfigManager.getBrowserSize();
        Configuration.timeout = finalTimeout;
        Configuration.pageLoadTimeout = ConfigManager.getPageLoadTimeout();
        Configuration.screenshots = ConfigManager.shouldTakeScreenshots();
        Configuration.savePageSource = ConfigManager.shouldSavePageSource();
        Configuration.reportsFolder = ConfigManager.getReportsFolder();
        
        // Additional Selenide configurations from ConfigManager
        Configuration.fastSetValue = ConfigManager.shouldUseFastSetValue();
        Configuration.clickViaJs = ConfigManager.shouldClickViaJS();
        
        // Store base URL for tests
        System.setProperty(FrameworkConstants.BASE_URL_PROPERTY, finalBaseUrl);
        
        logger.info("Test environment setup completed successfully with ConfigManager");
    }

    @AfterClass(alwaysRun = true)
    @Step("Cleanup test environment")
    public void cleanupTestEnvironment() {
        logger.info("Cleaning up test environment");
        
        try {
            // Close browser
            closeWebDriver();
            
            // Clear framework caches
            PageObjectFactory.clearCache();
            TestDataResolver.clearCache();
            
            logger.info("Test environment cleanup completed successfully");
        } catch (Exception e) {
            logger.error("Error during test environment cleanup", e);
        }
    }

    /**
     * Gets the base URL from system properties
     */
    protected String getBaseUrl() {
        return System.getProperty("base.url", "https://www.agoda.com");
    }

    /**
     * Helper method to pause execution for debugging
     */
    protected void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted", e);
        }
    }

    /**
     * Helper method to switch to the latest opened tab/window
     */
    protected void switchToLatestTab() {
        logger.debug("Switching to latest tab");
        Selenide.switchTo().window(Selenide.webdriver().driver().getWebDriver().getWindowHandles().size() - 1);
    }

    /**
     * Helper method to switch to first tab/window
     */
    protected void switchToFirstTab() {
        logger.debug("Switching to first tab");
        Selenide.switchTo().window(0);
    }

    /**
     * Helper method to get current window handle
     */
    protected String getCurrentWindowHandle() {
        return Selenide.webdriver().driver().getWebDriver().getWindowHandle();
    }

    /**
     * Helper method to close current tab and switch to previous
     */
    protected void closeCurrentTabAndSwitchToPrevious() {
        logger.debug("Closing current tab and switching to previous");
        Selenide.closeWindow();
        switchToLatestTab();
    }
}