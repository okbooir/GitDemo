package pageobjects;

import abstractComponents.Utils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class OrdersPage extends Utils {

    private final WebDriver driver;

    public OrdersPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath = "//table/tbody/tr/th") private WebElement orderId;
    @FindBy(css = "tr td:nth-child(3)") private List<WebElement> ordersNames;

    public String getOrderId () {
        return orderId.getText();
    }

    public boolean orderNameMatch (String productName) {
        return ordersNames.stream().anyMatch(name->name.getText().equals(productName));
    }


}
