# Healenium Self-Healing Demo Guide

This guide will walk you through demonstrating Healenium's self-healing locator capabilities using the provided test framework and HTML demo pages.

## Overview

Healenium is a self-healing test automation library that automatically fixes broken locators by analyzing the page structure and finding alternative selectors when the original ones fail. This demo shows:

1. **Baseline Testing**: Running tests on original HTML page with working locators
2. **Locator Changes**: Switching to modified HTML page with changed element IDs/classes
3. **Self-Healing**: Watching Healenium automatically fix broken locators
4. **Database Inspection**: Viewing healing activity in the database

## Prerequisites

1. **Java 17+** installed
2. **Maven** installed
3. **Docker and Docker Compose** installed (for infrastructure)
4. **Chrome browser** installed

## Demo Components

### HTML Demo Pages
- `src/test/resources/healenium-demo/index.html` - Original page with initial locators
- `src/test/resources/healenium-demo/index-modified.html` - Modified page with changed locators

### Infrastructure
- **PostgreSQL**: Stores healing data and statistics
- **Healenium Backend**: Processes healing requests
- **Healenium Frontend**: Web UI for viewing healing activity
- **Selenium Grid**: Provides browser automation infrastructure

### Test Suite
- `HealeniumDemoTest.java` - Comprehensive test class demonstrating healing
- `healenium-demo-suite.xml` - TestNG suite configuration

## Step-by-Step Demo Instructions

### Step 1: Start Healenium Infrastructure

```bash
# Make the management script executable (if not already done)
chmod +x manage-healenium.sh

# Start all Healenium services
./manage-healenium.sh start

# Wait for services to be healthy (this may take 2-3 minutes)
./manage-healenium.sh status
```

**Expected Output:**
```
✓ PostgreSQL is healthy
✓ Healenium Backend is healthy
✓ Healenium Frontend is healthy
✓ Selenium Hub is healthy
✓ Chrome Node is healthy
✓ Firefox Node is healthy
All services are running successfully!
```

### Step 2: Verify Infrastructure Access

Open these URLs in your browser to verify services:

- **Selenium Grid Console**: http://localhost:4444/grid/console
- **Healenium Frontend**: http://localhost:8080
- **Healenium Backend Health**: http://localhost:7878/health

### Step 3: Run Baseline Tests (Original Page)

```bash
# Compile the project
mvn clean compile test-compile

# Run baseline tests on original HTML page
mvn test -Dtest=HealeniumDemoTest#testOriginalPageFormSubmission,testOriginalPageNavigation,testOriginalPageDynamicItems
```

**What happens:**
- Tests run against `index.html` with original locators (e.g., `id="username"`, `id="submit-btn"`)
- All tests should pass, establishing baseline behavior
- Healenium learns the page structure and element relationships

### Step 4: Run Healing Tests (Modified Page)

```bash
# Run healing tests on modified HTML page
mvn test -Dtest=HealeniumDemoTest#testModifiedPageFormSubmissionWithHealing,testModifiedPageNavigationWithHealing,testModifiedPageDynamicItemsWithHealing
```

**What happens:**
- Tests run against `index-modified.html` where locators changed (e.g., `id="user-name-input"`, `id="form-submit-button"`)
- Tests initially try original locators (which will fail)
- Healenium automatically finds new locators based on element properties and page structure
- Tests continue with healed locators

### Step 5: View Healing Results

#### Option A: Check Test Logs
Look for healing indicators in test output:
```
✓ Healenium successfully healed form locators
✓ Healenium successfully healed submit button locator
✓ Healenium successfully healed success message locator
```

#### Option B: Healenium Frontend Dashboard
1. Open http://localhost:8080
2. View healing statistics and recent activity
3. Explore healed selector mappings

#### Option C: Database Inspection
```bash
# Access the database directly
./manage-healenium.sh db

# Once connected, run these SQL queries:

-- View all healing activity
SELECT * FROM selector_healing ORDER BY healing_timestamp DESC;

-- View healing statistics
SELECT * FROM healing_statistics;

-- View recent healing activity (last 24 hours)
SELECT * FROM recent_healing_activity;

-- Count successful healings
SELECT 
    original_selector,
    healed_selector,
    COUNT(*) as healing_count
FROM selector_healing 
WHERE success = true 
GROUP BY original_selector, healed_selector;
```

### Step 6: Run Complete Demo Suite

```bash
# Run the complete demo suite
mvn test -DsuiteXmlFile=src/test/resources/suites/healenium-demo-suite.xml

# Generate Allure report
mvn allure:serve
```

## Understanding the Healing Process

### How Healenium Works

1. **Learning Phase**: During successful test runs, Healenium learns:
   - Element attributes (id, class, name, etc.)
   - Element position in DOM tree
   - Element visual characteristics
   - Parent-child relationships

2. **Healing Phase**: When locator fails, Healenium:
   - Analyzes current page structure
   - Compares with learned element characteristics
   - Calculates similarity scores for potential matches
   - Selects best matching element
   - Updates locator strategy

3. **Persistence**: Healing mappings are stored in database for future use

### Locator Changes in Demo

| Element | Original Locator | Modified Locator | Healing Strategy |
|---------|------------------|------------------|------------------|
| Username Field | `id="username"` | `id="user-name-input"` | Matches by input type, label, position |
| Email Field | `id="email"` | `id="email-address"` | Matches by input type, placeholder text |
| Submit Button | `id="submit-btn"` | `id="form-submit-button"` | Matches by button type, text content |
| Success Message | `id="success-message"` | `id="form-success-message"` | Matches by content, position after form |

## Troubleshooting

### Common Issues

1. **Services Not Starting**
   ```bash
   # Check Docker status
   docker ps
   
   # Check logs for specific service
   ./manage-healenium.sh logs healenium-backend
   ```

2. **Tests Failing Without Healing**
   - Verify Healenium infrastructure is running
   - Check if `USE_HEALENIUM = true` in test configuration
   - Ensure baseline tests ran successfully first

3. **Database Connection Issues**
   ```bash
   # Restart database
   docker-compose restart healenium-postgres
   
   # Check database logs
   ./manage-healenium.sh logs healenium-postgres
   ```

4. **Browser Issues**
   ```bash
   # Check Selenium Grid status
   curl http://localhost:4444/grid/console
   
   # Restart browser nodes
   docker-compose restart chrome firefox
   ```

## Cleanup

```bash
# Stop all services
./manage-healenium.sh stop

# Remove all containers and volumes (complete cleanup)
./manage-healenium.sh cleanup
```

## Advanced Scenarios

### Custom Healing Configuration

You can modify healing behavior by updating `ConfigManager.java`:

```java
// Healing sensitivity (0.5-1.0, higher = more strict matching)
public static double getHealeniumSimilarityThreshold() {
    return getDoubleProperty("healenium.similarity.threshold", 0.75);
}

// Maximum healing attempts
public static int getHealeniumMaxAttempts() {
    return getIntProperty("healenium.max.attempts", 3);
}
```

### Production Considerations

1. **Performance**: Healing adds small overhead (~100-500ms per failed locator)
2. **Accuracy**: Higher similarity thresholds reduce false positives
3. **Maintenance**: Regular database cleanup recommended for large test suites
4. **Monitoring**: Use Healenium dashboard to monitor healing success rates

## Demo Variations

### Scenario 1: Gradual Changes
Modify individual elements in HTML and run specific tests to see selective healing.

### Scenario 2: Structure Changes
Modify DOM structure (add/remove wrapper divs) to test structural healing.

### Scenario 3: Attribute Changes
Change multiple attributes (id, class, name) to test multi-attribute healing.

## Next Steps

1. **Integrate with CI/CD**: Add Healenium to your continuous integration pipeline
2. **Real Application Testing**: Apply to your actual application with real test scenarios
3. **Healing Analytics**: Set up monitoring and alerting for healing success rates
4. **Team Training**: Train team members on interpreting healing results and maintaining test stability

## Resources

- [Healenium Documentation](https://healenium.io/docs)
- [Healenium GitHub Repository](https://github.com/healenium/healenium-web)
- [Selenide Documentation](https://selenide.org/)
- [TestNG Documentation](https://testng.org/doc/documentation-main.html)