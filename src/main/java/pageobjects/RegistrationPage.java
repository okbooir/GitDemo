package pageobjects;

import abstractComponents.Utils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class RegistrationPage extends Utils {

    private final WebDriver driver;

    public RegistrationPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Page fields (mapped from provided outer HTML)
    @FindBy(id = "firstName") private WebElement firstNameField;
    @FindBy(id = "lastName") private WebElement lastNameField;
    @FindBy(id = "userEmail") private WebElement userEmailField;
    @FindBy(id = "userMobile") private WebElement userMobileField;
    @FindBy(css = "select[formcontrolname='occupation']") private WebElement occupationSelect;
    @FindBy(css = "input[formcontrolname='gender']") private List<WebElement> genderRadioButtons;
    @FindBy(id = "userPassword") private WebElement userPasswordField;
    @FindBy(id = "confirmPassword") private WebElement confirmPasswordField;
    @FindBy(css = "input[formcontrolname='required']") private WebElement ageCheckbox;
    // the register button in this form uses id="login" (value="Register")
    @FindBy(id = "login") private WebElement registerBtn;

    // generic error/toast locator used across app
    @FindBy(css = "[class*='flyInOut']") private WebElement errorMessage;

    // ----- Low-level actions -----
    public void enterFirstName(String firstName) {
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        lastNameField.clear();
        lastNameField.sendKeys(lastName);
    }

    public void enterEmail(String email) {
        userEmailField.clear();
        userEmailField.sendKeys(email);
    }

    public void enterMobile(String mobile) {
        userMobileField.clear();
        userMobileField.sendKeys(mobile);
    }

    public void selectOccupationByVisibleText(String visibleText) {
        Select sel = new Select(occupationSelect);
        sel.selectByVisibleText(visibleText);
    }

    public void chooseGender(String gender) {
        for (WebElement radio : genderRadioButtons) {
            String val = radio.getAttribute("value");
            if (val != null && val.equalsIgnoreCase(gender)) {
                if (!radio.isSelected()) {
                    radio.click();
                }
                break;
            }
        }
    }

    public void enterPassword(String password) {
        userPasswordField.clear();
        userPasswordField.sendKeys(password);
    }

    public void enterConfirmPassword(String password) {
        confirmPasswordField.clear();
        confirmPasswordField.sendKeys(password);
    }

    public void setAgreeAge(boolean agree) {
        if (ageCheckbox.isSelected() != agree) {
            ageCheckbox.click();
        }
    }

    // ----- High-level action -----
    /**
     * Fill all registration fields and submit the form.
     * Returns ProductCatalogue if the flow leads to the product page (matches app pattern).
     */
    public ProductCatalogue registerFull(String firstName, String lastName, String email, String mobile,
                                        String occupationVisibleText, String gender, String password, boolean agreeAge) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterMobile(mobile);
        if (occupationVisibleText != null && !occupationVisibleText.isEmpty()) {
            selectOccupationByVisibleText(occupationVisibleText);
        }
        if (gender != null && !gender.isEmpty()) {
            chooseGender(gender);
        }
        enterPassword(password);
        enterConfirmPassword(password);
        setAgreeAge(agreeAge);

        waitToBeClickable(registerBtn);
        registerBtn.click();

        // The application may show a toast or navigate to product catalogue; tests expecting ProductCatalogue
        // should adapt if registration shows a different page. We return ProductCatalogue for consistency.
        return new ProductCatalogue(driver);
    }

    public String getErrorMessage() {
        waitToVisible(errorMessage);
        return errorMessage.getText();
    }


}


