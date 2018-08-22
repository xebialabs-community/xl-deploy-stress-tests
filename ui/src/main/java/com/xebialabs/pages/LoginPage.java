package com.xebialabs.pages;

import org.openqa.selenium.By;

import com.xebialabs.specs.BaseTest;

public class LoginPage extends AbstractPageObject {

    public LoginPage(){
        super(BaseTest.driver);
    }

    @Override
    protected By getUniqueElement() {
        return By.cssSelector(".xl-button");
    }

    static By in_userId = By.name("username");
    static By in_password = By.name("password");
    static By btn_login = By.cssSelector(".xl-button");

    /**
     * login to XL Release
     * @param  uName XLR Login Username.
     * @param  pwd XLR Login user password.
     * @return MainMenu shows main menu of XL Release.
     */
    public static LandingPage login(String uName, String pwd){
        BaseTest.driver.findElement(in_userId).sendKeys(uName);
        BaseTest.driver.findElement(in_password).sendKeys(pwd);
        BaseTest.driver.findElement(btn_login).click();
        return new LandingPage();
    }
}
