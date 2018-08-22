package com.xebialabs.specs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {
    protected static final String WEB_SERVER = System.getProperty("WEB_SERVER", "http://xl-deploy-uat.xebialabs.com:4516/");
    protected static final String BROWSER = System.getProperty("BROWSER", "chrome");
    protected static final boolean REMOTE_DRIVER = Boolean.valueOf(System.getProperty("REMOTE_DRIVER", "false"));
    protected static final String SELENIUM_HOST = System.getProperty("SELENIUM_HOST", "localhost");
    protected static final int SELENIUM_PORT = Integer.valueOf(System.getProperty("SELENIUM_PORT", "4444"));
    protected static final boolean CHROME_HEADLESS_MODE = Boolean.valueOf(System.getProperty("CHROME_HEADLESS_MODE", "false"));
    protected static final boolean FIREFOX_HEADLESS_MODE = Boolean.valueOf(System.getProperty("FIREFOX_HEADLESS_MODE", "true"));
    public static RemoteWebDriver driver;

    @BeforeClass
    public void setupWebDriver() throws MalformedURLException {
        if (REMOTE_DRIVER) {
            setupRemoteDriver();
            driver.setFileDetector(new LocalFileDetector());
        } else {
            setupLocalDriver();
        }
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        driver.manage().window().maximize();
    }

    private void setupLocalDriver() {
        if (BROWSER.equals("firefox")) {
            System.setProperty("webdriver.gecko.driver", copyDriver("geckodriver"));
            FirefoxBinary firefoxBinary = new FirefoxBinary();
            if(FIREFOX_HEADLESS_MODE){
                firefoxBinary.addCommandLineOptions("--headless");
            }
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setBinary(firefoxBinary);
            driver = new FirefoxDriver(firefoxOptions);
            driver.get(WEB_SERVER);
        } else if (BROWSER.equals("chrome")) {
            System.setProperty("webdriver.chrome.driver", copyDriver("chromedriver"));
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(CHROME_HEADLESS_MODE);
            options.addArguments("--window-size=1280,800"); // minimum resolution of XL Release
            driver = new ChromeDriver(options);
            driver.get(WEB_SERVER);
        } else {
            throw new RuntimeException("Browser type unsupported");
        }
    }

    private String copyDriver(String driverName) {
        String OS = getOSName();
        String tempDir = System.getProperty("java.io.tmpdir");
        try (
                InputStream stream = getClass().getResourceAsStream("/drivers/" + driverName + OS);
        ) {
            Path resolve = Paths.get(tempDir).resolve(driverName + OS);
            Files.copy(stream, resolve);
            File file = resolve.toFile();
            file.setExecutable(true);
            file.deleteOnExit();
            return resolve.toString();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String getOSName(){
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

    private void setupRemoteDriver() throws MalformedURLException {
        DesiredCapabilities capabilities;
        if (BROWSER.equals("firefox")) {
            capabilities = DesiredCapabilities.firefox();
        } else if (BROWSER.equals("internetExplorer")) {
            capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        } else if (BROWSER.equals("chrome")) {
            capabilities = DesiredCapabilities.chrome();
        } else {
            throw new RuntimeException("Browser type unsupported");
        }
        driver = new RemoteWebDriver(
                new URL("http://" + SELENIUM_HOST + ":" + SELENIUM_PORT + "/wd/hub"),
                capabilities);
        driver.get(WEB_SERVER);
    }

    @AfterClass
    public void suiteTearDown() {
        driver.manage().deleteAllCookies();
        driver.quit();
    }

}
