package pageobjects;

import abstractComponents.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LandingPage extends Utils {

    private final WebDriver driver;

    public LandingPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    //PageFactory
     @FindBy(id="userEmail") private WebElement useremail;
     @FindBy(id="userPassword") private WebElement userpassword;
     @FindBy(id="login") private WebElement loginBtn;
     @FindBy(css = "[class*='flyInOut']") private WebElement loginErrorMessage;
    private final By errorMessage = By.cssSelector("[class*='flyInOut']");

    public ProductCatalogue login (String email , String pass) {
        useremail.sendKeys(email);
        userpassword.sendKeys(pass);
        loginBtn.click();
        return new ProductCatalogue(driver);
    }

    public String getErrorMessage (){
        waitToVisible(loginErrorMessage);
        return loginErrorMessage.getText();
    }


}
