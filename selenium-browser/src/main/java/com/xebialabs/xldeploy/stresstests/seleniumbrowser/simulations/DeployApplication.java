package com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.logging.Logger;
import org.junit.Assert;

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
        driver.findElementByXPath("(//div[@class='xl-autocomplete-container'])[1]//input").sendKeys(osOption);

        LOGGER.info("Save infrastructure");
        driver.findElementByXPath("(//div[@class='dip-view-body'])[1]/div/button[1]").click();


        LOGGER.info("Create environment, open new tab");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@data-id='Environments']/div/span")));
        driver.findElementByXPath("//*[@data-id='Environments']/div/span").click();
        driver.findElementByXPath("//*[@data-id='Environments']/div/i[@data-id='Environments']").click();
        driver.findElementByXPath("//li[@data-path='new']").click();
        driver.findElementByXPath("//span[contains(@class, 'menu-item-label') and text()='Environment']").click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//form)[2]//input[@name='name']")));
        driver.findElementsByName("name").get(1).sendKeys("SeleniumEnv");

        LOGGER.info("Link infrastructure to environment");
        driver.findElementByXPath("(//div[@class='xl-autocomplete-container'])[3]//input")
                .sendKeys("Infrastructure/Selenium");
        driver.findElementByXPath("//div[@class='yt-option' and @title='Infrastructure/Selenium']/span").click();

        LOGGER.info("Save environment");
        driver.findElementByXPath("(//div[@class='dip-view-body'])[2]/div/button[1]").click();

        LOGGER.info("Search package to deploy, pre-condition ImportApplication has been completed");
        WebElement searchApp =  driver.findElementByXPath("//div[contains(@class, 'entity-panel-search')]//input[@type='search']");
        searchApp.sendKeys("Applications/test-dar/1.0");
        searchApp.sendKeys(Keys.RETURN);
        driver.findElementByXPath("//span[text()='Applications/test-dar/1.0']").click();
        driver.findElementByXPath("//i[@data-id='Applications/test-dar/1.0']").click();
        driver.findElementByXPath("//li[@data-path='deploy']/a").click();

        LOGGER.info("Search environment to deploy to package");
        WebElement searchEnv = driver.findElementByXPath("//div[contains(@class, 'filtered-multi-select')]//input[@type='search']");
        searchEnv.sendKeys("SeleniumEnv");
        searchEnv.sendKeys(Keys.RETURN);
        driver.findElementByXPath("//li[@data-id='Environments/SeleniumEnv']//input[@value='Environments/SeleniumEnv']").click();

        LOGGER.info("Environment(s) selected, click Continue button");
        driver.findElementByXPath("//button[contains(@class, 'continue-btn')]").click();

        LOGGER.info("No special configuration to be done, initiate deployment");
        driver.findElementByCssSelector("button.xl-button.xl-primary.btn.btn-default").click();

        performAssertion(driver);

        LOGGER.info("Finish deployment");
        driver.findElementByXPath("//button[contains(@class, 'finish-btn')]").click();

    }

    /** Perform assertions on application package import. */
    @Override
    protected void performAssertion(ChromeDriver driver) {
        WebElement deploymentSummary = driver.findElementByXPath(
                "//span[contains(@class, 'node-name') and text()='Deploy test-dar 1.0 on SeleniumEnv']");
        Assert.assertNotNull(deploymentSummary);
        Assert.assertNotNull(driver.findElementByCssSelector("span.status-tracker.done"));
    }

}
