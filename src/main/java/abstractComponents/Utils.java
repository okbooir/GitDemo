package abstractComponents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.CartPage;
import pageobjects.OrdersPage;

import java.time.Duration;

public class Utils {

    private final WebDriver driver;
    public Utils (WebDriver driver) {
        this.driver = driver;
    }

     @FindBy(css = "button[routerlink='/dashboard/cart']") private WebElement cart;
    private final By cartBtn = By.cssSelector("button[routerlink='/dashboard/cart']");
     @FindBy(css = "button[routerlink='/dashboard/myorders']") private WebElement myOrders;
    private final By myOrdersBtn = By.xpath("//button[@routerlink='/dashboard/myorders']");



    protected void waitToVisible (WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitToInvisible (WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    protected void waitToBeClickable (WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void waitStaleness (WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.stalenessOf(element));

    }

    public void waitUrlChange(String oldUrl) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> !driver.getCurrentUrl().equals(oldUrl));
    }

    public CartPage clickonCart () {
        waitToBeClickable(cart);
        cart.click();
        return new CartPage(driver);
    }

    public OrdersPage clickOnOrders () {
        waitToBeClickable(myOrders);
        myOrders.click();
        return new OrdersPage(driver);
    }

    public void goTo(String url) {
        driver.get(url);
    }

    protected void waitNumberOfElements (By locator, int number) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator,number));
    }
}
