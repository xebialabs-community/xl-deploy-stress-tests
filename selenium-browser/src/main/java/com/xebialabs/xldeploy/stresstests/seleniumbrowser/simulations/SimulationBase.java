package com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations;

import org.openqa.selenium.WebDriver;

public abstract class SimulationBase {
    public static final String OS_NAME = "os.name";
    public static final String WIN_OS = "win";
    public static final String MAC_OS = "mac";
    public static final String SOLARIS_OS = "sunos";
    public static final String IBM_AIX_OS = "aix";
    /** RedHat, CENTOS, Mandrake, Debian, Ubuntu OS name . **/
    public static final String LINUX_OS = "linux";
    /** https://nixos.org/ */
    public static final String NIX_OS = "nix";
    public static final String OS_MAC_DRIVER_KEY = "-mac";
    public static final String OS_LINUX_DRIVER_KEY = "-linux";
    public static final String OS_WIN_DRIVER_KEY = ".exe";
    public static final String OS_NOT_SUPPORTED_ERR_MSG = "OS type unsupported";
    protected static final String TEST_APP = "collectd";
    protected static final String TEST_APP_VERSION = "1.0";

    public abstract void simulate(WebDriver driver);

    protected abstract void performAssertion(WebDriver driver);

    /** Gets the OS chrome driver resource indicator based on OS name. **/
    public static String getOSDriverKey() {
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
