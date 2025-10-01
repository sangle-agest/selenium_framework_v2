package com.myorg.automation.core.elements;

import com.myorg.automation.constants.FrameworkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ListElement is an alias for Collection to provide alternative naming
 * for handling multiple elements with the same locator
 */
public class ListElement extends Collection {
    private static final Logger logger = LoggerFactory.getLogger(ListElement.class);
    
    /**
     * Constructor for ListElement
     * @param locator Element locator
     * @param elementName Element name for logging
     */
    public ListElement(String locator, String elementName) {
        super(locator, elementName);
        logger.debug(FrameworkConstants.ELEMENT_CREATED_LOG, FrameworkConstants.ELEMENT_TYPE_LIST, locator);
    }
}