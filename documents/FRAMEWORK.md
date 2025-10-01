# 🚀 Automation Framework Design Document

## 1. Tech Stack

- **Language**: Java 17  
- **Build Tool**: Maven  
- **Test Framework**: TestNG  
- **Web Driver Layer**: Selenium + Selenide (auto-wait, stable actions)  
- **Reporting**: Allure  
- **Locator Healing**: Healenium (self-healing locators)  
- **BDD Layer (Optional)**: Gherkin (Cucumber or Serenity BDD if needed)  
- **Data Storage**: JSON (for PageObjects & test data)  
- **Logging**: SLF4J + Logback  
- **Utilities**: Custom `DateTimeUtils`, `TestDataResolver`  

---

## 2. Framework Goals

- ✅ JSON-driven **Page Object Model** (POM)  
- ✅ **Element Wrappers** for reusable actions (`Button`, `Textbox`, `Combobox`, etc.)  
- ✅ **Dynamic locators** using `%s` placeholders → `String.format`  
- ✅ **Collections / Lists** support for repeating UI structures  
- ✅ **Date tokens** like `<NEXT_FRIDAY>`, `<PLUS_3_DAYS>` resolved by `DateTimeUtils`  
- ✅ Centralized **logging** & **Allure step annotations**  
- ✅ **Healenium** to recover from locator changes  

---

## 3. Folder Structure

```
automation-framework/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/myorg/automation/
│   │   │   ├── core/                # Element wrappers, PageObjectFactory, DynamicPage
│   │   │   ├── data/                # PageDefinition, ElementDefinition
│   │   │   ├── utils/               # DateTimeUtils, TestDataResolver
│   │   └── resources/
│   │       └── pages/               # JSON page object definitions
│   │       └── testdata/            # JSON test data
│   └── test/
│       └── java/com/myorg/tests/
│           └── AgodaSearchTest.java
```

---

## 4. JSON Page Objects

### Example: `pages/AgodaHomePage.json`
```json
{
  "pageName": "AgodaHomePage",
  "url": "https://www.agoda.com/",
  "elements": [
    {
      "name": "searchBox",
      "locator": "xpath=//input[@id='search']",
      "type": "Textbox"
    },
    {
      "name": "searchButton",
      "locator": "xpath=//button[contains(@class,'search-btn')]",
      "type": "Button"
    },
    {
      "name": "hotelNameInResults",
      "locator": "xpath=//div[@class='hotel-card'][%s]//h3",
      "type": "DynamicLabel"
    },
    {
      "name": "firstFiveHotels",
      "locator": "xpath=//div[@class='hotel-card']",
      "type": "Collection"
    }
  ]
}
```

## 5. Test Data JSON

### Example: `testdata/agoda_search_data.json`
```json
{
  "tc01": {
    "place": "Da Nang",
    "checkInDate": "<NEXT_FRIDAY>",
    "checkOutDate": "<PLUS_3_DAYS>",
    "travellerType": "Family Travelers",
    "rooms": 2,
    "adults": 4
  }
}
```

## 6. Supported Element Types

- **Button**
- **Textbox**
- **Combobox**
- **Checkbox**
- **Label**
- **DynamicLabel**
- **DynamicButton**
- **DynamicLink**
- **Collection** (multiple elements)
- **ListElement** (alias for Collection)

Each extends `BaseElement` and supports:
- Auto-wait (Selenide)
- Robust logging (SLF4J)
- Allure @Step annotations

## 7. Dynamic Locators

Placeholders `%s` in locators are replaced at runtime with `String.format`.

**Example JSON:**
```json
{
  "name": "hotelNameInResults",
  "locator": "xpath=//div[@class='hotel-card'][%s]//h3",
  "type": "DynamicLabel"
}
```

**Usage:**
```java
String hotelName = page.el("hotelNameInResults", DynamicLabel.class).getText("1");
```

## 8. DateTimeUtils

Resolves tokens in test data:
- `<NEXT_FRIDAY>` → Next Friday's date
- `<PLUS_3_DAYS>` → Today + 3 days
- `<PLUS_2_WEEKS>` → Today + 2 weeks

**Usage:**
```java
String checkIn = DateTimeUtils.resolve("<NEXT_FRIDAY>", "yyyy-MM-dd");
```

## 9. Page Factory & DynamicPage

`PageObjectFactory` loads a JSON file and creates a `DynamicPage`

`DynamicPage` stores element instances and exposes helper methods:

```java
DynamicPage home = PageObjectFactory.loadPage("/pages/AgodaHomePage.json");
home.el("searchBox", Textbox.class).type("Da Nang");
home.el("searchButton", Button.class).click();
```

## 10. Example Test Case

**Test: Agoda Search and Sort Hotels**

```java
@Test
public void searchAndSortHotels() {
    DynamicPage home = PageObjectFactory.loadPage("/pages/AgodaHomePage.json");
    open(home.getUrl());

    // Step 1: Enter search
    home.el("searchBox", Textbox.class).type("Da Nang");
    home.el("searchButton", Button.class).click();

    switchToLatestTab();

    // Step 2: Verify hotels displayed
    Collection hotels = home.el("firstFiveHotels", Collection.class);
    Assert.assertTrue(hotels.size() >= 5);

    // Step 3: Verify hotel names
    for (int i = 1; i <= 5; i++) {
        String hotelName = home.el("hotelNameInResults", DynamicLabel.class).getText(String.valueOf(i));
        Assert.assertTrue(hotelName.contains("Da Nang"));
    }
}
```

## 11. Integration Points

- **Allure** → auto logs from @Step, screenshots on failure
- **Healenium** → replaces broken locators at runtime (configured via `hlm.properties`)
- **Maven** → manage dependencies (selenide, testng, allure-testng, healenium-web)
- **CI/CD** → GitHub Actions or Jenkins (Maven test + Allure report upload)

## 12. Next Steps

1. Generate Maven `pom.xml` with required dependencies.
2. Implement `BaseElement` and all wrappers.
3. Implement `PageObjectFactory` + `DynamicPage`.
4. Add `DateTimeUtils`.
5. Setup Allure & Healenium.
6. Write sample tests with Agoda page + JSON test data.
7. Add CI pipeline for auto-execution.

## 13. Run & Report

**Run tests:**
```bash
mvn clean test
```

**Serve Allure report:**
```bash
mvn allure:serve
```

## 14. Benefits

✅ Clean separation of UI locators (JSON) and code  
✅ Easy maintenance (update JSON, not Java)  
✅ Extendable with new element wrappers  
✅ Auto-wait with Selenide → stable tests  
✅ Allure + Healenium integration → powerful reporting + self-healing