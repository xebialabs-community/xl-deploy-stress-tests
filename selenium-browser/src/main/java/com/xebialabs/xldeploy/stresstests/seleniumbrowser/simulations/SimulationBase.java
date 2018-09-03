package com.xebialabs.xldeploy.stresstests.seleniumbrowser.simulations;

import org.openqa.selenium.chrome.ChromeDriver;

public abstract class SimulationBase {

    public abstract void simulate(ChromeDriver driver);

    protected abstract void performAssertion(ChromeDriver driver);
}
