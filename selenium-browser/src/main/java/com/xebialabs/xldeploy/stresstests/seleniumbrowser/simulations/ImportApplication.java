package com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** XL Deploy -> Applications ->  Import -> From computer: "POST /deployit/package/upload/..." */
public class ImportApplication extends SimulationBase {

    @Override
    public void simulate(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@data-id='Applications']/div/span")));
        driver.findElement(By.xpath("//*[@data-id='Applications']/div/span")).click();
        driver.findElement(By.xpath("//*[@data-id='Applications']/div/i[@data-id='Applications']")).click();
        driver.findElement(By.xpath("//li[@data-path='import']/a")).click();
        driver.findElement(By.xpath("//li[@data-path='import->fromComputer']/a")).click();
    }

}
