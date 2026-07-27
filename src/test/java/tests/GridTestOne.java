package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class GridTestOne {

    @Test
    public void homepageCheck() throws URISyntaxException, MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName("chrome");
        caps.setPlatform(Platform.WIN11);
        caps.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        // caps.setCapability(CapabilityType.BROWSER_NAME,"chrome");
        WebDriver driver = new RemoteWebDriver(new URI("http://192.168.1.103:4444").toURL(), caps);
        driver.get("https://www.google.com");
        System.out.println(driver.getTitle());
        driver.findElement(By.name("q")).sendKeys("ahmed");
        driver.quit();
    }
}
