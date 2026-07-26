package pageobjects;

import abstractComponents.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class ProductCatalogue extends Utils {

    private final WebDriver driver;

    public ProductCatalogue(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    //PageFactory
    // WebElement email = driver.findElement(By.id());
     @FindBy(xpath="//h5/b") private List<WebElement> productsList;
    private final By products = By.xpath("//div[@class='card-body']/h5/b");
    private final By addToCartBtnOfProduct = By.xpath("../../button[2]");
    private final By toastMessage = By.id("toast-container");
     @FindBy(css = ".ng-animating" ) private WebElement spinner;
    @FindBy (xpath = "//div[@aria-label='Product Added To Cart']") private WebElement addedToCartMsg;

    public void addProductByName (String productName)  {
        waitNumberOfElements(products,2);
        productsList.stream().filter(s->s.getText().equalsIgnoreCase(productName)).findFirst().orElseThrow().findElement(addToCartBtnOfProduct).click();
        waitToVisible(addedToCartMsg);
        //waitToVisible(toastMessage);
        //waitToInvisible(spinner);
        //waitToVisible(addedToCartMsg);

    }

}
