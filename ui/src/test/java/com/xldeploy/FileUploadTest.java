package com.xldeploy;

import com.xebialabs.pages.LandingPage;
import com.xebialabs.pages.LoginPage;
import com.xebialabs.specs.BaseTest;
import org.testng.annotations.*;

public class FileUploadTest extends BaseTest{

    @BeforeMethod
    public void testStartUp(){
        LoginPage.login("admin","admin");
    }

    @Test
    public void uploadFile() throws InterruptedException{
        LandingPage
                .openLeftMenuItemDropDown("Applications")
                .isMenuOpened()
                .openItem("Import","From computer")
                .uploadFIle();
    }

}
