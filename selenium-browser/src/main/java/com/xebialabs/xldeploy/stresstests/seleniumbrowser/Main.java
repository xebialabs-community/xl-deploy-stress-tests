package com.xebialabs.xldeploy.stresstests.seleniumbrowser;

import com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations.DeployApplication;
import com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations.ImportApplication;
import com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations.Login;
import com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations.SimulationBase;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final String WEBDRIVER_CHROME_DRIVER_KEY = "webdriver.chrome.driver";
    private static final String WEBDRIVER_CHROME_DRIVER = "chromedriver";
    private static final String TEMP_DIR = "java.io.tmpdir";
    private static final String CHROMEDRIVER_ERR_MSG = "Error while setting up Chromedriver";

    /** Main entry point in the application that spins up all available browser automation scenario's. */
    public static void main(String[] args) {
        String os = SimulationBase.getOSDriverKey();
        System.setProperty(WEBDRIVER_CHROME_DRIVER_KEY, copyDriver(WEBDRIVER_CHROME_DRIVER, os));
        ChromeOptions options = new ChromeOptions();
        if (SimulationBase.OS_LINUX_DRIVER_KEY.equals(os) || SimulationBase.OS_MAC_DRIVER_KEY.equals(os)) {
            options.addArguments("--kiosk");
        } else {
            options.addArguments("--start-maximized");
        }
        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        // login is the pre-requisite of each simulation scenario
        Login login = new Login();
        login.simulate(driver);

        ImportApplication packageUpload = new ImportApplication();
        packageUpload.simulate(driver);

        DeployApplication deployPackage = new DeployApplication();
        deployPackage.simulate(driver);
    }

    /** Copies Chrome driver resource to the right directory needed by Selenium to spin up. */
    private static String copyDriver(String driverName, String os) {
        String tempDir = System.getProperty(TEMP_DIR);
        Path resolve = Paths.get(tempDir).resolve(driverName.concat(os));
        if (resolve.toFile().exists()) {
            return resolve.toString();
        }

        try (
            InputStream chromeDriver = Main.class.getClassLoader().getResourceAsStream(driverName.concat(os)))
        {
            Files.copy(chromeDriver, resolve);
            File file = resolve.toFile();
            file.setExecutable(true);
            file.deleteOnExit();
            return resolve.toString();
        } catch (IOException e) {
            throw new IllegalStateException(CHROMEDRIVER_ERR_MSG, e);
        }
    }

}
