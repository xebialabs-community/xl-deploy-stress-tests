package com.xebialabs.specs;

public class GlobalFunctions {

    /**
     * clears content of the text field by id
     * @param id id of the element
     */
    public static void clearTextById(String id){
        BaseTest.driver.findElementById(id).click();
        BaseTest.driver.findElementById(id).clear();
    }

    /**
     * clears content of the text field by xpath
     * @param xpath xpath of the element
     */
    public static void clearTextByXpath(String xpath){
        BaseTest.driver.findElementByXPath(xpath).click();
        BaseTest.driver.findElementByXPath(xpath).clear();
    }

    /**
     * types string into text field by id
     * @param id id of the element
     * @param text text to write into text field
     */
    public static void typeTextById(String id, String text){
        BaseTest.driver.findElementById(id).click();
        BaseTest.driver.findElementById(id).sendKeys(text);
    }

    /**
     * types string into text field by xpath
     * @param xpath xpath of the element
     * @param text text to write into text field
     */
    public static void typeTextByXPath(String xpath, String text){
        BaseTest.driver.findElementByXPath(xpath).click();
        BaseTest.driver.findElementByXPath(xpath).sendKeys(text);
    }

}
