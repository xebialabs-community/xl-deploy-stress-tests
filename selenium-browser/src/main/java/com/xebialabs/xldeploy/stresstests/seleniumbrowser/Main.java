package com.xebialabs.xldeploy.stresstests.seleniumbrowser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {

    // Optional parameter key names
    private static final String LOGIN_URL_KEY = "baseUrl";
    private static final String WEBDRIVER_CHROME_DRIVER_KEY = "webdriver.chrome.driver";
    private static final String USERNAME_KEY = "username";
    private static final String PASS_KEY = "password";

    // Default values
    private static final String XL_DEPLOY_LOCAL_LOGIN = "http://localhost:4516/#/login";
    private static final String WEBDRIVER_CHROME_DRIVER_MACOS = "/usr/local/bin/chromedriver";
    private static final String DEFAULT_USERNAME = "user1";
    private static final String DEFAULT_PASS = "password";

    public static void main(String[] args) {
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

        WebDriver driver = new ChromeDriver();

        // launch ChromeDriver and direct it to the XL Deploy Login URL
        driver.get(loginUrl);

        //login to XL-Deploy
        driver.findElement(By.xpath("//form//input[@name='username']")).sendKeys(username);
        driver.findElement(By.xpath("//form//input[@name='password']")).sendKeys(pass);
        driver.findElement(By.xpath("//form//button[@type='submit']")).click();

        // Applications ->  Import -> From computer: "POST /deployit/package/upload/test.txt"
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@data-id='Applications']/div/span")));
        driver.findElement(By.xpath("//*[@data-id='Applications']/div/span")).click();
        driver.findElement(By.xpath("//*[@data-id='Applications']/div/i[@data-id='Applications']")).click();
        driver.findElement(By.xpath("//li[@data-path='import']/a")).click();
        driver.findElement(By.xpath("//li[@data-path='import->fromComputer']/a")).click();
    }

}
