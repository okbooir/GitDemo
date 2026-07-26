package pageobjects;

import abstractComponents.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CartPage extends Utils {

    private final WebDriver driver;

    public CartPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath = "//div[@class='cartSection']/h3") private List<WebElement> productsInCart;
    @FindBy(xpath = "//button[contains(text(),'Checkout')]") private WebElement checkoutBtn;
    private final By checkoutButton = By.xpath("//button[contains(text(),'Checkout')]");

    public boolean checkCartProducts (String productName) {
        boolean flag = productsInCart.stream().anyMatch(s->s.getText().equalsIgnoreCase(productName));
        return flag;
    }

    public CheckoutPage clickCheckoutBtn () {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'center'});", checkoutBtn);
        waitToBeClickable(checkoutBtn);
        checkoutBtn.click();
        return new CheckoutPage(driver);
    }

}
