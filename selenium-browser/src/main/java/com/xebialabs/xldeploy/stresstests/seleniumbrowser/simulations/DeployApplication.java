package com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.logging.Logger;

/**
 * Browser automation for: XL Deploy -> Start a deployment "POST /deployit/deployment"
 */
public class DeployApplication extends SimulationBase {

    private static final Logger LOGGER = Logger.getLogger(DeployApplication.class.getName());

    @Override
    public void simulate(ChromeDriver driver) {
        LOGGER.info("Create infrastructure");
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@data-id='Infrastructure']/div/span")));
        driver.findElement(By.xpath("//*[@data-id='Infrastructure']/div/span")).click();
        driver.findElement(By.xpath("//*[@data-id='Infrastructure']/div/i[@data-id='Infrastructure']")).click();
        driver.findElement(By.xpath("//li[@data-path='new']")).click();
        driver.findElement(By.xpath("//li[@data-path='new->overthere']")).click();
        driver.findElementByXPath("//a[@class='localhost']").click();

        LOGGER.info("Set HTML form required fields");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//form//input[@name='name']")));
        driver.findElementsByName("name").get(0).sendKeys("Selenium");
        String os = getOSDriverKey();
        String osOption = (SimulationBase.OS_LINUX_DRIVER_KEY.equals(os) || SimulationBase.OS_MAC_DRIVER_KEY.equals(os))
                ? "UNIX" : "WINDOWS";
        driver.executeScript("document.getElementsByClassName('xl-autocomplete-container')[0].innerText = '" + osOption + "';");

        LOGGER.info("Save infrastructure");
        driver.findElementByXPath("//button[@class='xl-button xl-primary']").click();

        LOGGER.info("Create environment");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@data-id='Environments']/div/span")));
        driver.findElementByXPath("//*[@data-id='Environments']/div/span").click();
        driver.findElementByXPath("//*[@data-id='Environments']/div/i[@data-id='Environments']").click();
        driver.findElementByXPath("//li[@data-path='new']").click();
        driver.findElementByXPath("//span[contains(@class, 'menu-item-label') and text()='Environment']").click();
        driver.findElementByXPath("//input[@name='name']").sendKeys("SeleniumEnv");
        LOGGER.info("Link infrastructure to environment");
        driver.findElementByXPath("//div[contains(@class, 'xl-components-input') and text()='The infrastructure components of this Environment (Property: members)']/input")
                .sendKeys("Infrastructure/Selenium");
        driver.findElementByXPath("//div[@title='Infrastructure/Selenium']").click();
        LOGGER.info("Save environment");
        driver.findElementByXPath("//button[@class='xl-button xl-primary']").click();

        LOGGER.info("Search package to deploy, pre-condition ImportApplication has been completed");
        WebElement searchApp =  driver.findElementByXPath("//div[contains(@class, 'entity-panel-search')]/span/input[@type='search']");
        searchApp.sendKeys("Applications/test-dar/1.0");
        searchApp.sendKeys(Keys.RETURN);
        driver.findElementByXPath("//span[text()='Applications/test-dar/1.0']").click();
        driver.findElementByXPath("//i[@data-id='Applications/test-dar/1.0']").click();
        driver.findElementByXPath("//li[@data-path='deploy']/span").click();

        LOGGER.info("Search environment to deploy to package");
        WebElement searchEnv = driver.findElementByXPath("//div[contains(@class, 'main-content')]/span/input[@type='search']");
        searchEnv.sendKeys("SeleniumEnv");
        searchApp.sendKeys(Keys.RETURN);
        driver.findElementByXPath("//li[@data-id='Environments/SeleniumEnv']/div/input[@value='Environments/SeleniumEnv']").click();

        LOGGER.info("Click Continue button");
        driver.findElementByXPath("//button[@class='xl-button xl-primary continue-btn']").click();


        LOGGER.info("Initiate deployment");
        driver.findElementByXPath("//div[contains(@class, 'xl-category-content') and text()='Operating system the host runs (Property: os)']").click();

    }

    /** Perform assertions on application package import. */
    @Override
    protected void performAssertion(ChromeDriver driver) {

    }

}
