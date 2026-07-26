package pageobjects;

import abstractComponents.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OrderConfirmationPage extends Utils {

    private final WebDriver driver;

    public OrderConfirmationPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

     @FindBy(xpath = "//td/h1[@class='hero-primary']") private WebElement orderConfirmation;
    private final By orderConfirmMsg = By.xpath("//td/h1[@class='hero-primary']");
     @FindBy(css = "label.ng-star-inserted") private WebElement orderId;


    public String confirmationMessage () {
        waitToVisible(orderConfirmation);
        return orderConfirmation.getText();

    }

    public String getOrderId () {
        return orderId.getText().split("\\|")[1].trim();

    }

}
