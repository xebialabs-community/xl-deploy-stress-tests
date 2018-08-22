package com.xebialabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.LocalFileDetector;

import com.xebialabs.specs.BaseTest;

public class ItemPage extends AbstractPageObject{

    public ItemPage(){
        super(BaseTest.driver);
    }

    @Override
    protected By getUniqueElement() {
        return By.cssSelector(".working-panel-section");
    }

    public static ItemPage uploadFIle() throws InterruptedException {
//        BaseTest.driver.findElementByXPath("//button[contains(text(),'Browse')]").click();
//        BaseTest.driver.findElementByXPath("//div[@class='file-upload-browse-area']//input[@type='text']")
//                .sendKeys("/Users/chitrang/Documents/cypresslogs");
        WebElement input =  BaseTest.driver.findElementByXPath("//input[@name='ajax_upload_file_input']");
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", input);
        input.sendKeys("/Users/chitrang/Documents/cypresslogs");
        Thread.sleep(3000); // just for demo purpose on how to do file upload please remove this sleep and put assertions where necessary
        BaseTest.driver.findElementByXPath("//button[@class='xl-button xl-primary']").click();
        Thread.sleep(3000);
        return new ItemPage();
    }
}
