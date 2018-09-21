package com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Browser automation for: XL Deploy -> Start a deployment "POST /deployit/deployment"
 */
public class DeployApplication extends SimulationBase {

    private static final String TEST_APP_PATH = String.format("Applications/%s/%s", TEST_APP, TEST_APP_VERSION);
    private static final String TEST_INFRA = "vm";
    private static final String TEST_INFRA_PATH = String.format("Infrastructure/%s", TEST_INFRA);
    private static final String TEST_ENV = "perfmon";
    public static final String TEST_ENV_PATH = String.format("Environments/%s", TEST_ENV);

    private static final Logger LOGGER = LoggerFactory.getLogger(DeployApplication.class);


    @Override
    public void simulate(WebDriver driver) {
        LOGGER.info("Create infrastructure");
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@data-id='Infrastructure']/div/span")));
        driver.findElement(By.xpath("//*[@data-id='Infrastructure']/div/span")).click();
        driver.findElement(By.xpath("//*[@data-id='Infrastructure']/div/i[@data-id='Infrastructure']")).click();
        driver.findElement(By.xpath("//li[@data-path='new']")).click();
        driver.findElement(By.xpath("//li[@data-path='new->overthere']")).click();
        driver.findElement(By.xpath("//a[@class='localhost']")).click();

        LOGGER.info("Set HTML form required fields");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//form//input[@name='name']")));
        driver.findElements(By.name("name")).get(0).sendKeys(TEST_INFRA);
        String os = getOSDriverKey();
        String osOption = (SimulationBase.OS_LINUX_DRIVER_KEY.equals(os) || SimulationBase.OS_MAC_DRIVER_KEY.equals(os))
                ? "UNIX" : "WINDOWS";
        driver.findElement(By.xpath("(//div[@class='xl-autocomplete-container'])[1]//input")).sendKeys(osOption);

        LOGGER.info("Save infrastructure and close tab");
        driver.findElement(By.xpath("(//div[@class='dip-view-body'])[1]/div/button[1]")).click();

        LOGGER.info("Create environment, open new tab");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@data-id='Environments']/div/span")));
        driver.findElement(By.xpath("//*[@data-id='Environments']/div/span")).click();
        driver.findElement(By.xpath("//*[@data-id='Environments']/div/i[@data-id='Environments']")).click();
        driver.findElement(By.xpath("//li[@data-path='new']")).click();
        driver.findElement(By.xpath("//span[contains(@class, 'menu-item-label') and text()='Environment']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//form)[1]//input[@name='name']")));
        driver.findElements(By.name("name")).get(0).sendKeys(TEST_ENV);

        LOGGER.info("Link infrastructure to environment");
        driver.findElement(By.xpath("(//div[@class='xl-autocomplete-container'])[1]//input"))
                .sendKeys(TEST_INFRA_PATH);
        driver.findElement(By.xpath(
                String.format("//div[@class='yt-option' and @title='%s']/span", TEST_INFRA_PATH))).click();

        LOGGER.info("Save environment and close tab");
        driver.findElement(By.xpath("(//div[@class='dip-view-body'])[1]/div/button[1]")).click();

        LOGGER.info("Search package to deploy, pre-condition ImportApplication has been completed");
        WebElement searchApp =  driver.findElement(By.xpath("//div[contains(@class, 'entity-panel-search')]//input[@type='search']"));
        searchApp.sendKeys(TEST_APP_PATH);
        searchApp.sendKeys(Keys.RETURN);
        driver.findElement(By.xpath(String.format("//span[text()='%s']", TEST_APP_PATH))).click();
        driver.findElement(By.xpath(String.format("//i[@data-id='%s']", TEST_APP_PATH))).click();
        driver.findElement(By.xpath("//li[@data-path='deploy']/a")).click();

        LOGGER.info("Search environment to deploy to package");
        WebElement searchEnv = driver.findElement(By.xpath("//div[contains(@class, 'filtered-multi-select')]//input[@type='search']"));
        searchEnv.sendKeys(TEST_ENV);
        searchEnv.sendKeys(Keys.RETURN);
        driver.findElement(By.xpath(String.format("//li[@data-id='%s']//input[@value='%s']", TEST_ENV_PATH, TEST_ENV_PATH))).click();

        LOGGER.info("Environment(s) selected, click Continue button");
        driver.findElement(By.xpath("//button[contains(@class, 'continue-btn')]")).click();

        LOGGER.info("No special configuration to be done, initiate deployment");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.xl-button.xl-primary.btn.btn-default")));
        driver.findElement(By.cssSelector("button.xl-button.xl-primary.btn.btn-default")).click();

        LOGGER.info("Assertions on deploy status and finish deployment");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'finish-btn')]")));
        performAssertion(driver);
        driver.findElement(By.xpath("//button[contains(@class, 'finish-btn')]")).click();
    }

    /** Perform assertions on application package import. */
    @Override
    protected void performAssertion(WebDriver driver) {
        WebElement deploymentSummary = driver.findElement(By.xpath(
                String.format("//span[contains(@class, 'node-name') and text()='Deploy %s %s on %s']", TEST_APP, TEST_APP_VERSION, TEST_ENV)));
        Assert.assertNotNull(deploymentSummary);
        Assert.assertNotNull(driver.findElement(By.cssSelector("span.status-tracker.done")));
    }

}
