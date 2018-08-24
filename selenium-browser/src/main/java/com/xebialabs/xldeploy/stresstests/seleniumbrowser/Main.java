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
    private static final String OS_MAC_DRIVER_KEY = "-mac";
    private static final String OS_LINUX_DRIVER_KEY = "-linux";
    private static final String OS_WIN_DRIVER_KEY = ".exe";


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
    }

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
            throw new IllegalStateException(e);
        }
    }

    private static String getOSDriverKey(){
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("win") >= 0) {
            return OS_WIN_DRIVER_KEY;
        } else if (os.indexOf("mac") >= 0) {
            return OS_MAC_DRIVER_KEY;
        } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0
                || os.indexOf("sunos") >= 0) {
            return OS_LINUX_DRIVER_KEY;
        } else {
            throw new RuntimeException("OS type unsupported");
        }
    }

}
