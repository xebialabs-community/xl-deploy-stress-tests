package com.xebialabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.sun.org.apache.bcel.internal.generic.LAND;

import com.xebialabs.specs.BaseTest;

public class LandingPage extends AbstractPageObject{

    public LandingPage(){
        super(BaseTest.driver);
    }

    @Override
    protected By getUniqueElement() {
        return By.cssSelector(".left-panel-section");
    }

    public static LandingPage openLeftMenuItemDropDown(String item){
//        Actions action = new Actions(BaseTest.driver);
        WebElement el = BaseTest.driver.findElementByXPath("//span[contains(text(),'" + item + "')]");
//        action.moveToElement(el);
        el.click();
        BaseTest.driver.findElementByXPath("//div[@class='infinite-tree-item infinite-tree-selected']//i[@class='infinite-tree-closed context-menu-button pull-right']").click();
        return new LandingPage();
    }

    public static LandingPage isMenuOpened(){
        BaseTest.driver.findElementByClassName("popover-content").isDisplayed();
        return new LandingPage();
    }

    public static ItemPage openItem(String item, String subItem){
        BaseTest.driver.findElementByXPath("//a[@title='"+ item +"']").click();
        BaseTest.driver.findElementByXPath("//a[@title='"+ subItem +"']").click();
        return new ItemPage();
    }
}
