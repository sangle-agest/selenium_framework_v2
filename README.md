# 🚀 Selenium Automation Framework

A comprehensive, JSON-driven automation framework built with Java 17, Selenide, TestNG, Allure, and Healenium for robust and maintainable web application testing.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Writing Tests](#writing-tests)
- [Running Tests](#running-tests)
- [Reporting](#reporting)
- [Best Practices](#best-practices)
- [Troubleshooting](#troubleshooting)

## ✨ Features

- **JSON-driven Page Object Model** - Define page elements in JSON files for easy maintenance
- **Element Wrappers** - Robust element types with built-in waiting and error handling
- **Dynamic Locators** - Support for parameterized locators using `String.format`
- **Date Token Resolution** - Smart resolution of date tokens like `<NEXT_FRIDAY>`, `<PLUS_3_DAYS>`
- **Allure Reporting** - Rich test reports with steps, screenshots, and attachments
- **Healenium Integration** - Self-healing locators to handle UI changes
- **TestNG Integration** - Powerful test execution with groups, parallel execution, and data providers
- **Selenide Framework** - Auto-waiting, stable element interactions
- **Comprehensive Logging** - SLF4J + Logback for detailed execution logs

## 🛠 Tech Stack

- **Java 17** - Programming language
- **Maven** - Build tool and dependency management
- **Selenide** - Web testing framework with auto-waiting
- **TestNG** - Testing framework
- **Allure** - Test reporting
- **Healenium** - Self-healing locators
- **Jackson** - JSON processing
- **SLF4J + Logback** - Logging

## 📁 Project Structure

```
selenium_framework_v2/
├── documents/
│   └── FRAMEWORK.md              # Detailed framework documentation
├── pom.xml                       # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/com/myorg/automation/
│   │   │   ├── core/            # Element wrappers and page factory
│   │   │   │   ├── BaseElement.java
│   │   │   │   ├── Button.java
│   │   │   │   ├── Textbox.java
│   │   │   │   ├── Combobox.java
│   │   │   │   ├── Checkbox.java
│   │   │   │   ├── Label.java
│   │   │   │   ├── Collection.java
│   │   │   │   ├── DynamicLabel.java
│   │   │   │   ├── DynamicButton.java
│   │   │   │   ├── DynamicLink.java
│   │   │   │   ├── ListElement.java
│   │   │   │   ├── DynamicPage.java
│   │   │   │   └── PageObjectFactory.java
│   │   │   ├── data/            # Data models for JSON mapping
│   │   │   │   ├── PageDefinition.java
│   │   │   │   └── ElementDefinition.java
│   │   │   └── utils/           # Utility classes
│   │   │       ├── DateTimeUtils.java
│   │   │       └── TestDataResolver.java
│   │   └── resources/
│   │       ├── pages/           # JSON page object definitions
│   │       │   ├── AgodaHomePage.json
│   │       │   └── AgodaSearchResultsPage.json
│   │       └── testdata/        # JSON test data files
│   │           ├── agoda_search_data.json
│   │           └── common_test_data.json
│   └── test/
│       ├── java/com/myorg/tests/
│       │   ├── BaseTest.java    # Base test class
│       │   └── AgodaSearchTest.java
│       └── resources/
│           ├── testng.xml       # TestNG configuration
│           ├── allure.properties
│           ├── allure-categories.json
│           ├── hlm.properties   # Healenium configuration
│           └── logback-test.xml # Logging configuration
└── README.md
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Chrome browser (or configure for other browsers)

### Installation

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd selenium_framework_v2
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Verify setup**
   ```bash
   mvn test -Dtest=AgodaSearchTest#testBasicHotelSearch
   ```

## ⚙️ Configuration

### Browser Configuration

Configure browser settings in `BaseTest.java` or pass as TestNG parameters:

```xml
<parameter name="browser" value="chrome"/>
<parameter name="headless" value="false"/>
<parameter name="timeout" value="10000"/>
```

### Selenide Configuration

Key configurations in `BaseTest.setupTestEnvironment()`:

```java
Configuration.browser = "chrome";
Configuration.headless = false;
Configuration.browserSize = "1920x1080";
Configuration.timeout = 10000;
Configuration.pageLoadTimeout = 30000;
```

### Healenium Setup

Configure Healenium in `src/test/resources/hlm.properties`:

```properties
hlm.server.url=http://localhost:7878
hlm.recovery.enabled=true
hlm.heal.enabled=true
```

## 📝 Writing Tests

### 1. Create Page Object JSON

Define your page elements in JSON format:

```json
{
  "pageName": "LoginPage",
  "url": "https://example.com/login",
  "elements": [
    {
      "name": "usernameField",
      "locator": "xpath=//input[@id='username']",
      "type": "Textbox"
    },
    {
      "name": "loginButton",
      "locator": "xpath=//button[@type='submit']",
      "type": "Button"
    }
  ]
}
```

### 2. Create Test Data JSON

Define test data with date token support:

```json
{
  "validUser": {
    "username": "testuser",
    "password": "password123",
    "loginDate": "<TODAY>",
    "sessionExpiry": "<PLUS_1_DAYS>"
  }
}
```

### 3. Write Test Class

```java
@Epic("User Authentication")
@Feature("Login Functionality")
public class LoginTest extends BaseTest {
    
    @Test(groups = {"smoke"})
    @Story("Valid User Login")
    public void testValidUserLogin() {
        // Load page and test data
        DynamicPage loginPage = PageObjectFactory.loadPage("/pages/LoginPage.json");
        Map<String, Object> testData = TestDataResolver.getTestCaseData("/testdata/users.json", "validUser");
        
        // Navigate to login page
        open(loginPage.getUrl());
        
        // Perform login
        loginPage.el("usernameField", Textbox.class).type(testData.get("username").toString());
        loginPage.el("passwordField", Textbox.class).type(testData.get("password").toString());
        loginPage.el("loginButton", Button.class).click();
        
        // Verify login success
        // Add assertions here
    }
}
```

### 4. Using Dynamic Elements

For elements with dynamic locators:

```json
{
  "name": "userMenuItem",
  "locator": "xpath=//div[@class='user-menu']//span[text()='%s']",
  "type": "DynamicButton"
}
```

```java
// Use with parameter
page.el("userMenuItem", DynamicButton.class).click("Profile");
```

### 5. Working with Collections

```json
{
  "name": "searchResults",
  "locator": "xpath=//div[@class='result-item']",
  "type": "Collection"
}
```

```java
Collection results = page.el("searchResults", Collection.class);
int count = results.size();
results.clickAt(0); // Click first item
```

## 🏃‍♂️ Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Groups
```bash
mvn test -Dgroups=smoke
mvn test -Dgroups=regression
```

### Run with Different Browser
```bash
mvn test -Dbrowser=firefox -Dheadless=true
```

### Run Specific Test Class
```bash
mvn test -Dtest=AgodaSearchTest
```

### Run with Custom TestNG Suite
```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Parallel Execution
Configure in `testng.xml`:
```xml
<suite name="ParallelSuite" parallel="methods" thread-count="3">
```

## 📊 Reporting

### Allure Reports

1. **Generate Allure report:**
   ```bash
   mvn allure:serve
   ```

2. **View report:** Opens automatically in browser

3. **Generate static report:**
   ```bash
   mvn allure:report
   ```

### Report Features

- ✅ Test execution timeline
- ✅ Test steps with screenshots
- ✅ Failed test details with stack traces
- ✅ Test categorization
- ✅ Environment information
- ✅ Trends and history

## 📋 Best Practices

### Page Object Design

1. **Keep JSON files focused** - One page per JSON file
2. **Use descriptive element names** - `loginButton` not `btn1`
3. **Add descriptions** - Help other team members understand elements
4. **Group related elements** - Use consistent naming patterns

### Test Data Management

1. **Use meaningful test case IDs** - `tc01_valid_login` not `tc01`
2. **Leverage date tokens** - `<NEXT_FRIDAY>` instead of hardcoded dates
3. **Separate test data by feature** - Don't mix unrelated data
4. **Use environment-specific data** - Different data for dev/test/prod

### Test Organization

1. **Extend BaseTest** - Inherit common functionality
2. **Use appropriate groups** - `@Test(groups = {"smoke", "regression"})`
3. **Add Allure annotations** - `@Epic`, `@Feature`, `@Story`, `@Step`
4. **Handle test data in @BeforeMethod** - Fresh data for each test

### Element Interactions

1. **Use appropriate element types** - `Button` for buttons, `Textbox` for inputs
2. **Let Selenide handle waits** - No explicit waits needed
3. **Use dynamic elements wisely** - When you have parameterized locators
4. **Verify before interacting** - Check element state when needed

## 🔧 Troubleshooting

### Common Issues

1. **Element not found**
   - Check locator in browser dev tools
   - Verify JSON syntax
   - Ensure element name matches usage

2. **Test data not resolved**
   - Verify JSON file path (starts with `/`)
   - Check test case ID exists in JSON
   - Validate JSON syntax

3. **Date tokens not working**
   - Ensure tokens are properly formatted: `<NEXT_FRIDAY>`
   - Check DateTimeUtils for supported tokens
   - Verify token case sensitivity

4. **Healenium not working**
   - Check Healenium server is running
   - Verify hlm.properties configuration
   - Ensure database connection

### Debug Tips

1. **Enable debug logging** - Set log level to DEBUG in logback-test.xml
2. **Use pause() method** - Add breakpoints for debugging
3. **Check Selenide screenshots** - Auto-captured on failures in `target/selenide-screenshots`
4. **Review Allure reports** - Detailed step-by-step execution

### Performance Tips

1. **Use page caching** - PageObjectFactory caches loaded pages
2. **Clear caches when needed** - In @AfterMethod or @AfterClass
3. **Optimize locators** - Prefer ID/CSS over complex XPath
4. **Use parallel execution** - Configure thread-count in testng.xml

## 📚 Additional Resources

- [Selenide Documentation](https://selenide.org/)
- [TestNG Documentation](https://testng.org/doc/)
- [Allure Documentation](https://docs.qameta.io/allure/)
- [Healenium Documentation](https://healenium.io/)
- [Framework Design Document](documents/FRAMEWORK.md)

---

**Happy Testing! 🎉**

For questions or contributions, please refer to the team documentation or create an issue.