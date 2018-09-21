package com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.logging.Logger;

/** XL Deploy -> Applications ->  Import -> From computer: "POST /deployit/package/upload/PetClinic-1.0.dar" */
public class ImportApplication extends SimulationBase {

    static final String TEST_APP = "collectd";
    static final String TEST_APP_VERSION = "1.0";

    private static final Logger LOGGER = Logger.getLogger(ImportApplication.class.getName());

    @Override
    public void simulate(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 60);

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@data-id='Applications']/div/span")));
        driver.findElement(By.xpath("//*[@data-id='Applications']/div/span")).click();
        driver.findElement(By.xpath("//*[@data-id='Applications']/div/i[@data-id='Applications']")).click();
        driver.findElement(By.xpath("//li[@data-path='import']/a")).click();
        driver.findElement(By.xpath("//li[@data-path='import->fromComputer']/a")).click();

        File testPackage = new File(getClass().getClassLoader().getResource(
                String.format("%s-%s.dar", TEST_APP, TEST_APP_VERSION)).getFile());
        driver.findElement(By.xpath("//input[@name='ajax_upload_file_input']")).sendKeys(testPackage.getAbsolutePath());
        driver.findElement(By.xpath("//button[@class='xl-button xl-primary']")).click();

        performAssertion(driver);
        driver.findElement(By.xpath("//button[@class='xl-button xl-cancel']")).click();
    }

    /** Perform assertions on application package import. */
    @Override
    protected void performAssertion(WebDriver driver) {
        WebElement progressBarSuccess = driver.findElement(By.cssSelector("div.progress-status.success"));
        Assert.assertNotNull(progressBarSuccess);

        LOGGER.info(progressBarSuccess.getText());
    }

}
