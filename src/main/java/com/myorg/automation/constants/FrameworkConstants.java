package com.myorg.automation.constants;

/**
 * Constants class containing all hardcoded strings used throughout the framework
 */
public final class FrameworkConstants {
    
    // Private constructor to prevent instantiation
    private FrameworkConstants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
    
    // Locator Prefixes
    public static final String XPATH_PREFIX = "xpath=";
    public static final String CSS_PREFIX = "css=";
    public static final String ID_PREFIX = "id=";
    public static final String CLASS_PREFIX = "class=";
    public static final String NAME_PREFIX = "name=";
    
    // CSS Selectors
    public static final String ID_SELECTOR_PREFIX = "#";
    public static final String CLASS_SELECTOR_PREFIX = ".";
    public static final String NAME_ATTRIBUTE_SELECTOR = "[name='%s']";
    
    // Element Types
    public static final String BUTTON_TYPE = "Button";
    public static final String TEXTBOX_TYPE = "Textbox";
    public static final String COMBOBOX_TYPE = "Combobox";
    public static final String CHECKBOX_TYPE = "Checkbox";
    public static final String LABEL_TYPE = "Label";
    public static final String DYNAMIC_LABEL_TYPE = "DynamicLabel";
    public static final String DYNAMIC_BUTTON_TYPE = "DynamicButton";
    public static final String DYNAMIC_LINK_TYPE = "DynamicLink";
    public static final String COLLECTION_TYPE = "Collection";
    public static final String LIST_ELEMENT_TYPE = "ListElement";
    
    // Default Locator Settings
    public static final String DEFAULT_LOCATOR_TYPE = "css";
    public static final String DEFAULT_WAIT_TYPE = "visible";
    public static final long DEFAULT_EXPLICIT_WAIT = 10000; // 10 seconds in milliseconds
    
    // Wait Types
    public static final String WAIT_TYPE_VISIBLE = "visible";
    public static final String WAIT_TYPE_CLICKABLE = "clickable";
    public static final String WAIT_TYPE_PRESENT = "present";
    public static final String WAIT_TYPE_INVISIBLE = "invisible";
    
    // Locator Types
    public static final String LOCATOR_TYPE_ID = "id";
    public static final String LOCATOR_TYPE_CSS = "css";
    public static final String LOCATOR_TYPE_XPATH = "xpath";
    public static final String LOCATOR_TYPE_CLASS = "class";
    public static final String LOCATOR_TYPE_NAME = "name";
    public static final String LOCATOR_TYPE_TAG = "tag";
    public static final String LOCATOR_TYPE_TEXT = "text";
    
    // Date Time Patterns
    public static final String TODAY_TOKEN = "<TODAY>";
    public static final String NOW_TOKEN = "<NOW>";
    public static final String YESTERDAY_TOKEN = "<YESTERDAY>";
    public static final String TOMORROW_TOKEN = "<TOMORROW>";
    public static final String NEXT_PATTERN = "<NEXT_%s>";
    public static final String PLUS_DAYS_PATTERN = "<PLUS_%d_DAYS?>";
    public static final String PLUS_WEEKS_PATTERN = "<PLUS_%d_WEEKS?>";
    public static final String PLUS_MONTHS_PATTERN = "<PLUS_%d_MONTHS?>";
    public static final String PLUS_YEARS_PATTERN = "<PLUS_%d_YEARS?>";
    public static final String MINUS_DAYS_PATTERN = "<MINUS_%d_DAYS?>";
    public static final String MINUS_WEEKS_PATTERN = "<MINUS_%d_WEEKS?>";
    public static final String MINUS_MONTHS_PATTERN = "<MINUS_%d_MONTHS?>";
    
    // Date Formats
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String US_DATE_FORMAT = "MM/dd/yyyy";
    public static final String EU_DATE_FORMAT = "dd/MM/yyyy";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    
    // Resource Paths
    public static final String PAGES_RESOURCE_PATH = "/pages/";
    public static final String TESTDATA_RESOURCE_PATH = "/testdata/";
    
    // Common Attributes
    public static final String HREF_ATTRIBUTE = "href";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String PLACEHOLDER_ATTRIBUTE = "placeholder";
    public static final String READONLY_ATTRIBUTE = "readonly";
    public static final String MULTIPLE_ATTRIBUTE = "multiple";
    public static final String INDETERMINATE_ATTRIBUTE = "indeterminate";
    
    // Browser Configuration
    public static final String CHROME_BROWSER = "chrome";
    public static final String FIREFOX_BROWSER = "firefox";
    public static final String EDGE_BROWSER = "edge";
    public static final String SAFARI_BROWSER = "safari";
    
    // Default Configurations
    public static final String DEFAULT_BROWSER = CHROME_BROWSER;
    public static final boolean DEFAULT_HEADLESS = false;
    public static final String DEFAULT_BROWSER_SIZE = "1920x1080";
    public static final long DEFAULT_TIMEOUT = 10000L;
    public static final long DEFAULT_PAGE_LOAD_TIMEOUT = 30000L;
    public static final boolean DEFAULT_SCREENSHOTS = true;
    public static final boolean DEFAULT_SAVE_PAGE_SOURCE = false;
    public static final String DEFAULT_REPORTS_FOLDER = "target/selenide-screenshots";
    
    // Error Messages
    public static final String ELEMENT_NOT_FOUND_ERROR = "Element '%s' not found in page definition for %s";
    public static final String JSON_FILE_NOT_FOUND_ERROR = "JSON file not found: %s";
    public static final String TEST_DATA_NOT_FOUND_ERROR = "Test data JSON file not found: %s";
    public static final String TEST_CASE_NOT_FOUND_ERROR = "Test case '%s' not found in %s";
    public static final String ELEMENT_CREATION_ERROR = "Failed to create element: %s";
    public static final String PAGE_LOAD_ERROR = "Failed to load page from JSON file: %s";
    public static final String TEST_DATA_LOAD_ERROR = "Failed to load test data from JSON file: %s";
    public static final String INVALID_INTEGER_ERROR = "Invalid integer value for key '%s': %s";
    public static final String INVALID_PAGE_DEFINITION_ERROR = "Invalid page definition: %s";
    public static final String INVALID_TEST_DATA_ERROR = "Invalid test data: %s";
    
    // Logging Messages
    public static final String ELEMENT_CLICKED_LOG = "Successfully clicked element '{}'";
    public static final String ELEMENT_TYPED_LOG = "Successfully typed '{}' in element '{}'";
    public static final String ELEMENT_CLEARED_LOG = "Successfully cleared element '{}'";
    public static final String ELEMENT_VISIBLE_LOG = "Element '{}' is now visible";
    public static final String ELEMENT_CLICKABLE_LOG = "Element '{}' is now clickable";
    public static final String PAGE_LOADED_LOG = "Successfully loaded and cached page: {} from {}";
    public static final String TEST_DATA_LOADED_LOG = "Successfully loaded and cached test data from: {}";
    public static final String ELEMENT_CACHE_CLEARED_LOG = "Clearing element cache for page: {}";
    
    // System Properties
    public static final String BASE_URL_PROPERTY = "base.url";
    public static final String BROWSER_PROPERTY = "browser";
    public static final String HEADLESS_PROPERTY = "headless";
    public static final String TIMEOUT_PROPERTY = "timeout";
    
    // Configuration File Names
    public static final String FRAMEWORK_PROPERTIES = "framework.properties";
    public static final String HEALENIUM_PROPERTIES = "hlm.properties";
    public static final String ALLURE_PROPERTIES = "allure.properties";
    public static final String TESTNG_XML = "testng.xml";
    public static final String LOGBACK_CONFIG = "logback-test.xml";
    
    // Test Groups
    public static final String SMOKE_GROUP = "smoke";
    public static final String REGRESSION_GROUP = "regression";
    public static final String INTEGRATION_GROUP = "integration";
    public static final String API_GROUP = "api";
    public static final String UI_GROUP = "ui";
    
    // Common Wait Times (in milliseconds)
    public static final long SHORT_WAIT = 2000L;
    public static final long MEDIUM_WAIT = 5000L;
    public static final long LONG_WAIT = 10000L;
    public static final long EXTRA_LONG_WAIT = 30000L;
    
    // Retry Configuration
    public static final int DEFAULT_RETRY_COUNT = 3;
    public static final long DEFAULT_RETRY_DELAY = 2000L;
    
    // Collection Operations
    public static final int FIRST_INDEX = 0;
    public static final int SECOND_INDEX = 1;
    public static final String FIRST_POSITION_XPATH = "[1]";
    public static final String LAST_POSITION_XPATH = "[last()]";
    
    // JavaScript Constants
    public static final String WINDOW_OPEN_JS = "window.open(arguments[0], '_blank');";
    public static final String SCROLL_TO_ELEMENT_JS = "arguments[0].scrollIntoView(true);";
    public static final String HIGHLIGHT_ELEMENT_JS = "arguments[0].style.border='3px solid red'";
    
    // ===================================
    // Element Creation Messages
    // ===================================
    public static final String ELEMENT_CREATED_LOG = "Created {} element with locator: {}";
    
    // ===================================
    // Element Type Constants 
    // ===================================
    public static final String ELEMENT_TYPE_BUTTON = "button";
    public static final String ELEMENT_TYPE_TEXTBOX = "textbox";
    public static final String ELEMENT_TYPE_CHECKBOX = "checkbox";
    public static final String ELEMENT_TYPE_COMBOBOX = "combobox";
    public static final String ELEMENT_TYPE_LABEL = "label";
    public static final String ELEMENT_TYPE_LINK = "link";
    public static final String ELEMENT_TYPE_COLLECTION = "collection";
    public static final String ELEMENT_TYPE_DYNAMIC_BUTTON = "dynamic_button";
    public static final String ELEMENT_TYPE_DYNAMIC_LABEL = "dynamic_label";
    public static final String ELEMENT_TYPE_DYNAMIC_LINK = "dynamic_link";
    public static final String ELEMENT_TYPE_LIST = "list";
    
    // ===================================
    // Checkbox Constants
    // ===================================
    public static final String ATTRIBUTE_CHECKED = "checked";
    public static final String CHECKBOX_CHECKED_LOG = "Checked checkbox: {}";
    public static final String CHECKBOX_UNCHECKED_LOG = "Unchecked checkbox: {}";
    public static final String CHECKBOX_ALREADY_CHECKED_LOG = "Checkbox already checked: {}";
    public static final String CHECKBOX_ALREADY_UNCHECKED_LOG = "Checkbox already unchecked: {}";
    public static final String CHECKBOX_CHECK_ERROR_LOG = "Failed to check checkbox '{}': {}";
    public static final String CHECKBOX_UNCHECK_ERROR_LOG = "Failed to uncheck checkbox '{}': {}";
    public static final String CHECKBOX_STATE_LOG = "Checkbox '{}' state: {}";
    public static final String CHECKBOX_STATE_CHECK_ERROR_LOG = "Failed to check state of checkbox '{}': {}";
    public static final String CHECKBOX_TOGGLED_LOG = "Toggled checkbox '{}' from {} to {}";
    public static final String CHECKBOX_TOGGLE_ERROR_LOG = "Failed to toggle checkbox '{}': {}";
    public static final String CHECKBOX_STATE_VERIFICATION_ERROR = "Checkbox '%s' expected state: %s, actual state: %s";
    public static final String CHECKBOX_STATE_VERIFIED_LOG = "Verified checkbox '{}' state: {}";
    public static final String CHECKBOX_ENABLED_LOG = "Checkbox '{}' enabled: {}";
    public static final String CHECKBOX_ENABLED_CHECK_ERROR_LOG = "Failed to check enabled state of checkbox '{}': {}";
    
    // ===================================
    // Combobox Constants
    // ===================================
    public static final String COMBOBOX_OPTION_SELECTED_LOG = "Selected option '{}' in combobox: {}";
    public static final String COMBOBOX_OPTION_BY_INDEX_SELECTED_LOG = "Selected option at index {} in combobox: {}";
    public static final String COMBOBOX_CLEARED_LOG = "Cleared combobox selection: {}";
    public static final String COMBOBOX_SELECT_ERROR_LOG = "Failed to select option '{}' in combobox '{}': {}";
    public static final String COMBOBOX_SELECT_BY_INDEX_ERROR_LOG = "Failed to select option at index {} in combobox '{}': {}";
    public static final String COMBOBOX_GET_SELECTED_ERROR_LOG = "Failed to get selected option from combobox '{}': {}";
    public static final String COMBOBOX_GET_OPTIONS_ERROR_LOG = "Failed to get options from combobox '{}': {}";
    public static final String COMBOBOX_CLEAR_ERROR_LOG = "Failed to clear combobox '{}': {}";
    
    // ===================================
    // Label Constants
    // ===================================
    public static final String LABEL_TEXT_RETRIEVED_LOG = "Retrieved text '{}' from label: {}";
    public static final String LABEL_TEXT_ERROR_LOG = "Failed to get text from label '{}': {}";
    
    // ===================================
    // Dynamic Element Constants
    // ===================================
    public static final String DYNAMIC_LOCATOR_CREATED_LOG = "Created dynamic locator: {} with parameter: {}";
    public static final String DYNAMIC_ELEMENT_PARAMETER_ERROR = "Parameter cannot be null or empty for dynamic element";
    public static final String DYNAMIC_ELEMENT_CLICKED_LOG = "Clicked dynamic element '{}' with parameter '{}'";
    public static final String DYNAMIC_ELEMENT_TYPED_LOG = "Typed '{}' in dynamic element '{}' with parameter '{}'";
    public static final String DYNAMIC_ELEMENT_TEXT_RETRIEVED_LOG = "Retrieved text '{}' from dynamic element '{}' with parameter '{}'";
    public static final String DYNAMIC_ELEMENT_CLICK_ERROR_LOG = "Failed to click dynamic element '{}' with parameter '{}': {}";
    public static final String DYNAMIC_ELEMENT_TYPE_ERROR_LOG = "Failed to type in dynamic element '{}' with parameter '{}': {}";
    public static final String DYNAMIC_ELEMENT_TEXT_ERROR_LOG = "Failed to get text from dynamic element '{}' with parameter '{}': {}";
    
    // ===================================
    // Collection Constants
    // ===================================
    public static final String COLLECTION_SIZE_LOG = "Collection '{}' contains {} elements";
    public static final String COLLECTION_ELEMENT_CLICKED_LOG = "Clicked element at index {} in collection: {}";
    public static final String COLLECTION_ELEMENT_NOT_FOUND_LOG = "Element at index {} not found in collection: {}";
    public static final String COLLECTION_SIZE_ERROR_LOG = "Failed to get size of collection '{}': {}";
    public static final String COLLECTION_CLICK_ERROR_LOG = "Failed to click element at index {} in collection '{}': {}";
    public static final String COLLECTION_EMPTY_LOG = "Collection '{}' is empty";
    public static final String COLLECTION_NOT_EMPTY_LOG = "Collection '{}' is not empty (size: {})";
}