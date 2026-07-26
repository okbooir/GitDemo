package pageobjects;

import abstractComponents.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckoutPage extends Utils {
    private final WebDriver driver;

    public CheckoutPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath = "//*[@placeholder='Select Country']") private WebElement countryField;
    @FindBy(xpath = "//span[text()=' Egypt']") private WebElement countrySelect;
    private final By countrySelectLocator = By.xpath("//span[text()=' Egypt']");
    @FindBy(xpath = "//a[text()='Place Order ']") private WebElement placeOrderBtn;
    private final By placOrderButton = By.xpath("//a[text()='Place Order ']");

    public void chooseCountry (String country){
        countryField.sendKeys(country);
        waitToBeClickable(countrySelect);
        countrySelect.click();
    }

    public OrderConfirmationPage clickPlaceOrder () {
        waitToBeClickable(placeOrderBtn);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", placeOrderBtn);
        js.executeScript("arguments[0].click();", placeOrderBtn);
        //if (placeOrderBtn.isDisplayed()) {
       //     Actions actions = new Actions(driver);
       //     actions.moveToElement(placeOrderBtn)
       //             .click()
      //              .build().perform();
       // }
        return new OrderConfirmationPage(driver);
    }
}
