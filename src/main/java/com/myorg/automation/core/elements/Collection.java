package com.myorg.automation.core.elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.myorg.automation.constants.FrameworkConstants;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Collection element wrapper for handling multiple elements
 */
public class Collection extends BaseElement {
    private static final Logger logger = LoggerFactory.getLogger(Collection.class);

    /**
     * Constructor for Collection
     * @param locator Element locator
     * @param elementName Element name for logging
     */
    public Collection(String locator, String elementName) {
        super(locator, elementName);
        logger.debug(FrameworkConstants.ELEMENT_CREATED_LOG, FrameworkConstants.ELEMENT_TYPE_COLLECTION, locator);
    }

    /**
     * Get the collection of elements
     * @return ElementsCollection
     */
    private ElementsCollection getCollection() {
        return createElement(locator).$$("");
    }

    /**
     * Get size of the collection
     * @return Number of elements in collection
     */
    @Step("Get size of collection: {this.elementName}")
    public int size() {
        try {
            ElementsCollection collection = getCollection();
            int size = collection.size();
            logger.info(FrameworkConstants.COLLECTION_SIZE_LOG, locator, size);
            return size;
        } catch (Exception e) {
            logger.error(FrameworkConstants.COLLECTION_SIZE_ERROR_LOG, locator, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.COLLECTION_SIZE_ERROR_LOG, locator, e.getMessage()), e);
        }
    }

    /**
     * Click element at specific index
     * @param index Index of element to click (0-based)
     * @return this Collection instance for method chaining
     */
    @Step("Click element at index {index} in collection: {this.elementName}")
    public Collection clickElementAt(int index) {
        try {
            ElementsCollection collection = getCollection();
            if (index < collection.size()) {
                collection.get(index).click();
                logger.info(FrameworkConstants.COLLECTION_ELEMENT_CLICKED_LOG, index, locator);
            } else {
                logger.warn(FrameworkConstants.COLLECTION_ELEMENT_NOT_FOUND_LOG, index, locator);
                throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for collection '%s' with size %d", 
                    index, locator, collection.size()));
            }
        } catch (Exception e) {
            logger.error(FrameworkConstants.COLLECTION_CLICK_ERROR_LOG, index, locator, e.getMessage());
            throw new RuntimeException(String.format(FrameworkConstants.COLLECTION_CLICK_ERROR_LOG, index, locator, e.getMessage()), e);
        }
        return this;
    }

    /**
     * Get element at specific index
     * @param index Index of element to get (0-based)
     * @return SelenideElement at the specified index
     */
    @Step("Get element at index {index} from collection: {this.elementName}")
    public SelenideElement getElementAt(int index) {
        try {
            ElementsCollection collection = getCollection();
            if (index < collection.size()) {
                return collection.get(index);
            } else {
                logger.warn(FrameworkConstants.COLLECTION_ELEMENT_NOT_FOUND_LOG, index, locator);
                throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for collection '%s' with size %d", 
                    index, locator, collection.size()));
            }
        } catch (Exception e) {
            logger.error("Failed to get element at index {} from collection '{}': {}", index, locator, e.getMessage());
            throw new RuntimeException(String.format("Failed to get element at index %d from collection '%s': %s", 
                index, locator, e.getMessage()), e);
        }
    }

    /**
     * Get text from all elements in collection
     * @return List of texts from all elements
     */
    @Step("Get texts from all elements in collection: {this.elementName}")
    public List<String> getAllTexts() {
        try {
            ElementsCollection collection = getCollection();
            List<String> texts = collection.texts();
            logger.debug("Texts from collection '{}': {}", locator, texts);
            return texts;
        } catch (Exception e) {
            logger.error("Failed to get texts from collection '{}': {}", locator, e.getMessage());
            throw new RuntimeException(String.format("Failed to get texts from collection '%s': %s", 
                locator, e.getMessage()), e);
        }
    }

    /**
     * Check if collection is empty
     * @return true if collection is empty, false otherwise
     */
    @Step("Check if collection is empty: {this.elementName}")
    public boolean isEmpty() {
        try {
            boolean empty = size() == 0;
            if (empty) {
                logger.info(FrameworkConstants.COLLECTION_EMPTY_LOG, locator);
            } else {
                logger.info(FrameworkConstants.COLLECTION_NOT_EMPTY_LOG, locator, size());
            }
            return empty;
        } catch (Exception e) {
            logger.error("Failed to check if collection '{}' is empty: {}", locator, e.getMessage());
            throw new RuntimeException(String.format("Failed to check if collection '%s' is empty: %s", 
                locator, e.getMessage()), e);
        }
    }

    /**
     * Get first element in collection
     * @return First SelenideElement
     */
    @Step("Get first element from collection: {this.elementName}")
    public SelenideElement getFirst() {
        return getElementAt(FrameworkConstants.FIRST_INDEX);
    }

    /**
     * Get last element in collection
     * @return Last SelenideElement
     */
    @Step("Get last element from collection: {this.elementName}")
    public SelenideElement getLast() {
        int lastIndex = size() - 1;
        return getElementAt(lastIndex);
    }

    /**
     * Click first element in collection
     * @return this Collection instance for method chaining
     */
    @Step("Click first element in collection: {this.elementName}")
    public Collection clickFirst() {
        return clickElementAt(FrameworkConstants.FIRST_INDEX);
    }

    /**
     * Click last element in collection
     * @return this Collection instance for method chaining
     */
    @Step("Click last element in collection: {this.elementName}")
    public Collection clickLast() {
        int lastIndex = size() - 1;
        return clickElementAt(lastIndex);
    }
}