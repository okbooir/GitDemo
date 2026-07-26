package tests;

import base.BaseTest;
import base.Retry;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageobjects.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ErrorValidation extends BaseTest {

    @Test (retryAnalyzer = Retry.class)
    public void loginErrorValidation () throws IOException {
        LandingPage currentLandingPage = new LandingPage(getDriver());
        currentLandingPage.login("oir@gmail.com", "Ilovest1!");
        String errorMessage = currentLandingPage.getErrorMessage();
        Assert.assertEquals(errorMessage,"Incorrrect email or password.");

    }

    @Test (groups = "errorHandling" , dataProvider = "getHash")
    public void productErrorValidation (HashMap<String,String> map) throws IOException {
        LandingPage currentLandingPage = new LandingPage(getDriver());
        ProductCatalogue productCatalogue = currentLandingPage.login(map.get("email"), map.get("password"));

        productCatalogue.addProductByName(map.get("productName"));
        CartPage cartPage = productCatalogue.clickonCart();

        boolean result = cartPage.checkCartProducts(map.get("productName"));
        Assert.assertTrue(result);
    }

    @Test (groups = "errorHandling" , dataProvider = "jsonConverted", retryAnalyzer = Retry.class)
    public void submitOrder (HashMap<String,String> testData) throws IOException {
        LandingPage currentLandingPage = new LandingPage(getDriver());
        ProductCatalogue productCatalogue = currentLandingPage.login(testData.get("email"), testData.get("password"));

        productCatalogue.addProductByName(testData.get("productName"));
        CartPage cartPage = productCatalogue.clickonCart();
        CheckoutPage checkoutPage = cartPage.clickCheckoutBtn();
        checkoutPage.chooseCountry(testData.get("country"));
        OrderConfirmationPage orderConfirmationPage = checkoutPage.clickPlaceOrder();

        String confirmationMessage = orderConfirmationPage.confirmationMessage();
        Assert.assertTrue(confirmationMessage.equalsIgnoreCase("Thankyou for the order."));
    }

    @Test ( groups = {"purchase" , "authenticated"} , dataProvider = "getData")
    public void orderIdValidation (String email , String pass , String productName , String country) throws IOException {
        //LandingPage currentLandingPage = new LandingPage(getDriver());
        //ProductCatalogue productCatalogue = currentLandingPage.login(email, pass);
        ProductCatalogue productCatalogue = new ProductCatalogue(getDriver());
        productCatalogue.addProductByName(productName);
        CartPage cartPage = productCatalogue.clickonCart();
        CheckoutPage checkoutPage = cartPage.clickCheckoutBtn();
        checkoutPage.chooseCountry(country);
        String currentUrl = getDriver().getCurrentUrl();
        OrderConfirmationPage orderConfirmationPage = checkoutPage.clickPlaceOrder();
        orderConfirmationPage.waitUrlChange(currentUrl);
        OrdersPage ordersPage = orderConfirmationPage.clickOnOrders();
        boolean flag = ordersPage.orderNameMatch(productName);
        Assert.assertTrue(flag);
    }

    @DataProvider (name = "getData")
    private Object[][] getData() {
        // ----- USING MULTI-DIMENSION ARRAY ((Accept any data type)) -------
        return new Object[][] {{"ookbooir@gmail.com" , "Ilovetest1!" , "ZARA COAT 3" , "Egypt"},{"okbooir@gmail.com" , "Ilovetest2!" , "ADIDAS ORIGINAL" , "Egypt"}};
    }

    @DataProvider (name = "getHash", parallel = true)  //(preffer to come fro json so no test data shown here in the test file
    private Object[][] getHash() {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("email","okbooir@gmail.com");
        map.put("password","Ilovetest2!");
        map.put("productName","ZARA COAT 3");

        HashMap<String,String> map1 = new HashMap<String,String>();
        map1.put("email","ookbooir@gmail.com");
        map1.put("password","Ilovetest1!");
        map1.put("productName","ADIDAS ORIGINAL");

        return new Object[][] {{map},{map1}};

    }

    @DataProvider (name = "jsonConverted", parallel = true)
        private Object[][] jsonConverted() throws IOException {
            List<HashMap<String,String>> testData = getJsonDataToHashMap(System.getProperty("user.dir")+"\\src\\main\\java\\data\\testData.json");
            return new Object[][] {{testData.get(0)},{testData.get(1)}};
        }


}
