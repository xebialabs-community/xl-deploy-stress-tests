package com.xebialabs.xldeploy.stresstests.seleniumbrowser;

import com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations.ImportApplication;
import com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations.Login;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final String WEBDRIVER_CHROME_DRIVER_KEY = "webdriver.chrome.driver";
    private static final String WEBDRIVER_CHROME_DRIVER = "chromedriver";

    public static void main(String[] args) {
        ChromeDriver driver = new ChromeDriver();
        System.setProperty(WEBDRIVER_CHROME_DRIVER_KEY, copyDriver(WEBDRIVER_CHROME_DRIVER));

        // login is the pre-requisite of each simulation scenario
        Login login = new Login();
        login.simulate(driver);

        // other scenario's are given as command-line parameter
        ImportApplication packageUpload = new ImportApplication();
        packageUpload.simulate(driver);
    }

    private static String copyDriver(String driverName) {
        String OS = getOSName();
        String tempDir = System.getProperty("java.io.tmpdir");

        try (
            InputStream chromeDriver = Main.class.getClassLoader().getResourceAsStream(driverName.concat(OS)))
        {
            Path resolve = Paths.get(tempDir).resolve(driverName + OS);
            Files.copy(chromeDriver, resolve);
            File file = resolve.toFile();
            file.setExecutable(true);
            file.deleteOnExit();
            return resolve.toString();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String getOSName(){
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("win") >= 0) {
            OS = ".exe";
        } else if (OS.indexOf("mac") >= 0) {
            OS = "-mac";
        } else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0
                || OS.indexOf("sunos") >= 0) {
            OS = "-linux";
        } else {
            throw new RuntimeException("OS type unsupported");
        }
        return OS;
    }


}
