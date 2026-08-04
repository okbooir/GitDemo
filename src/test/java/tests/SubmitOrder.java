package tests;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import pageobjects.*;

import java.io.IOException;
import java.util.logging.Level;

public class SubmitOrder extends BaseTest {

    @Test
    public void submitOrder() throws IOException {
        LandingPage currentLandingPage = new LandingPage(getDriver());
        currentLandingPage.goTo(getProperty("baseUrl"));
        ProductCatalogue productCatalogue = currentLandingPage.login("ookbooir@gmail.com", "Ilovetest1!");

        productCatalogue.addProductByName("ZARA COAT 3");
        CartPage cartPage = productCatalogue.clickonCart();

        boolean flag = cartPage.checkCartProducts("ZARA COAT 3");
        Assert.assertTrue(flag);
        CheckoutPage checkoutPage = cartPage.clickCheckoutBtn();

        checkoutPage.chooseCountry("Egypt");
        OrderConfirmationPage orderConfirmationPage = checkoutPage.clickPlaceOrder();

        String confirmationMessage = orderConfirmationPage.confirmationMessage();
        Assert.assertTrue(confirmationMessage.equalsIgnoreCase("Thankyou for the order."));
        String confirmationPageOrderId = orderConfirmationPage.getOrderId();
        OrdersPage ordersPage = orderConfirmationPage.clickOnOrders();
        String ordersPageOrderId = ordersPage.getOrderId();
        Assert.assertEquals(confirmationPageOrderId, ordersPageOrderId);
        System.out.println("mohamed");


    }

}
