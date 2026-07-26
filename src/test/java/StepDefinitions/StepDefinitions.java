package StepDefinitions;

import base.BaseTest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pageobjects.*;

import java.io.IOException;

public class StepDefinitions extends BaseTest {

    public LandingPage landingPage;
    public ProductCatalogue productCatalogue;
    public CartPage cartPage;
    public CheckoutPage checkoutPage;
    public OrderConfirmationPage orderConfirmationPage;

    @Given("Landing on site url")
    public void landing_on_site_url() throws IOException {
        launchApp();
    }

    @Given("^Logged in with username (.+) and password (.+)$")
    public void logged_in_with_username_and_password(String username, String password) {
        landingPage = new LandingPage(getDriver());
        productCatalogue = landingPage.login(username, password);
    }

    @When("^Add product (.+) to cart$")
    public void add_product_to_cart(String productName) {
        productCatalogue.addProductByName(productName);
    }

    @And("^Checkout the same (.+) and click submit the order$")
    public void checkout_the_same_product_and_click_submit_the_order(String productName) throws IOException {
        cartPage = productCatalogue.clickonCart();
        boolean flag = cartPage.checkCartProducts(productName);
        Assert.assertTrue(flag);
        checkoutPage = cartPage.clickCheckoutBtn();
        checkoutPage.chooseCountry(getProperty("country"));
        orderConfirmationPage = checkoutPage.clickPlaceOrder();
    }

    @Then("Message {string} is displayed on confirmation page")
    public void message_is_displayed_on_confirmation_page (String string) {
        String confirmationMessage = orderConfirmationPage.confirmationMessage();
        Assert.assertTrue(confirmationMessage.equalsIgnoreCase(string));
        getDriver().quit();
    }

    @Then ("Error message {string} is displayed")
    public void error_message_is_displayed(String string) {
        String errorMessage = landingPage.getErrorMessage();
        Assert.assertEquals(errorMessage,string);
        getDriver().quit();

    }


}
