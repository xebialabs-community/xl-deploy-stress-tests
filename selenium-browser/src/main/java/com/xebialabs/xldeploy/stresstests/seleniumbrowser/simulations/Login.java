package com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** XL Deploy Login. */
public class Login extends SimulationBase {

    // Optional parameter key names
    private static final String LOGIN_URL_KEY = "baseUrl";
    private static final String WEBDRIVER_CHROME_DRIVER_KEY = "webdriver.chrome.driver";
    private static final String USERNAME_KEY = "username";
    private static final String PASS_KEY = "password";

    // Default values
    private static final String XL_DEPLOY_LOCAL_LOGIN = "http://localhost:4516/#/login";
    private static final String WEBDRIVER_CHROME_DRIVER_MACOS = "/usr/local/bin/chromedriver";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASS = "admin";

    @Override
    public void simulate(WebDriver driver) {
        String loginUrl = (System.getProperty(LOGIN_URL_KEY) == null || System.getProperty(LOGIN_URL_KEY).isEmpty())
                ? XL_DEPLOY_LOCAL_LOGIN : System.getProperty(LOGIN_URL_KEY);
        if (System.getProperty(WEBDRIVER_CHROME_DRIVER_KEY) == null
                || System.getProperty(WEBDRIVER_CHROME_DRIVER_KEY).isEmpty()) {
            System.setProperty(WEBDRIVER_CHROME_DRIVER_KEY, WEBDRIVER_CHROME_DRIVER_MACOS);
        }
        String username = (System.getProperty(USERNAME_KEY) == null || System.getProperty(USERNAME_KEY).isEmpty())
                ? DEFAULT_USERNAME : System.getProperty(USERNAME_KEY);
        String pass = (System.getProperty(PASS_KEY) == null || System.getProperty(PASS_KEY).isEmpty())
                ? DEFAULT_PASS : System.getProperty(PASS_KEY);

        // launch ChromeDriver and direct it to the XL Deploy Login URL
        driver.get(loginUrl);

        //login to XL-Deploy
        driver.findElement(By.xpath("//form//input[@name='username']")).sendKeys(username);
        driver.findElement(By.xpath("//form//input[@name='password']")).sendKeys(pass);
        driver.findElement(By.xpath("//form//button[@type='submit']")).click();
    }
}
