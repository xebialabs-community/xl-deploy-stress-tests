package com.xebialabs.xldeploy.stresstests.seleniumbrowser;

import com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations.ImportApplication;
import com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations.Login;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {

    public static void main(String[] args) {
        ChromeDriver driver = new ChromeDriver();

        // login is the pre-requisite of each simulation scenario
        Login login = new Login();
        login.simulate(driver);

        // other scenario's are given as command-line parameter
        ImportApplication packageUpload = new ImportApplication();
        packageUpload.simulate(driver);
    }
}
