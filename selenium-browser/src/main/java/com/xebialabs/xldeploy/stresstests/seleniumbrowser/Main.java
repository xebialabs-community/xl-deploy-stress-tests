package com.xebialabs.xldeploy.stresstests.seleniumbrowser;

import com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations.ImportApplication;
import com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations.Login;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private static final String WEBDRIVER_CHROME_DRIVER_KEY = "webdriver.chrome.driver";
    private static final String WEBDRIVER_CHROME_DRIVER = "chromedriver";
    private static final String TEMP_DIR = "java.io.tmpdir";
    private static final String OS_NAME = "os.name";
    private static final String WIN_OS = "win";
    private static final String MAC_OS = "mac";
    private static final String SOLARIS_OS = "sunos";
    private static final String IBM_AIX_OS = "aix";
    /** RedHat, CENTOS, Mandrake, Debian, Ubuntu OS name . **/
    private static final String LINUX_OS = "linux";
    /** https://nixos.org/ */
    private static final String NIX_OS = "nix";
    private static final String OS_MAC_DRIVER_KEY = "-mac";
    private static final String OS_LINUX_DRIVER_KEY = "-linux";
    private static final String OS_WIN_DRIVER_KEY = ".exe";
    private static final String OS_NOT_SUPPORTED_ERR_MSG = "OS type unsupported";
    private static final String CHROMEDRIVER_ERR_MSG = "Error while setting up Chromedriver";

    /** Main entry point in the application that spins up all available browser automation scenario's. */
    public static void main(String[] args) {
        String os = getOSDriverKey();
        System.setProperty(WEBDRIVER_CHROME_DRIVER_KEY, copyDriver(WEBDRIVER_CHROME_DRIVER, os));
        ChromeOptions options = new ChromeOptions();
        if (OS_LINUX_DRIVER_KEY.equals(os) || OS_MAC_DRIVER_KEY.equals(os)) {
            options.addArguments("--kiosk");
        } else {
            options.addArguments("--start-maximized");
        }
        ChromeDriver driver = new ChromeDriver(options);

        // login is the pre-requisite of each simulation scenario
        Login login = new Login();
        login.simulate(driver);

        ImportApplication packageUpload = new ImportApplication();
        packageUpload.simulate(driver);

        driver.quit();
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

    /** Gets the OS chrome driver resource indicator based on OS name. **/
    private static String getOSDriverKey() {
        String os = System.getProperty(OS_NAME).toLowerCase();

        if (os.contains(WIN_OS)) {
            return OS_WIN_DRIVER_KEY;
        } else if (os.contains(MAC_OS)) {
            return OS_MAC_DRIVER_KEY;
        } else if (os.contains(LINUX_OS) || os.contains(NIX_OS) || os.contains(IBM_AIX_OS) || os.contains(SOLARIS_OS)) {
            return OS_LINUX_DRIVER_KEY;
        } else {
            throw new IllegalArgumentException(OS_NOT_SUPPORTED_ERR_MSG);
        }
    }

}
