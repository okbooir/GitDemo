package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageobjects.LandingPage;

import java.time.Duration;
import java.util.List;

public class StandAloneTest {

    @Test
    public void addOrder() {


        String productName = "ZARA COAT 3";
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
        Actions actions = new Actions(driver);

        LandingPage landingPage = new LandingPage(driver);

        //landing page
        driver.get("https://rahulshettyacademy.com/client");

        //write login credentials and press login button
        driver.findElement(By.id("userEmail")).sendKeys("ookbooir@gmail.com");
        driver.findElement(By.id("userPassword")).sendKeys("Ilovetest1!");
        driver.findElement(By.id("login")).click();

        // add the specific product you want to cart
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//h5/b")));
        List<WebElement> productNames = driver.findElements(By.xpath("//h5/b"));

        productNames.stream().filter(s->s.getText().equals(productName)).forEach(s->s.findElement(By.xpath("../..//button[contains(text(),'Add To Cart')]")).click());

       // for (int i=0;i<=productNames.size();i++) {
     //       if (productNames.get(i).getText().equals("ZARA COAT 3")) {
      //          driver.findElements(By.xpath("//button[contains(text(),'Add To Cart')]")).get(i).click();
      //      }
      //  }

        // wait the loading to be visible then invisible then click to cart
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast-container")));
        wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector(".ng-animating"))));

        driver.findElement(By.cssSelector("button[routerlink='/dashboard/cart']")).click();


        // make sure that the product in cart is the one you added to cart then click checkout
        List <WebElement> productsInCart = driver.findElements(By.xpath("//div[@class='cartSection']//h3"));

        boolean flag = productsInCart.stream().anyMatch(s->s.getText().equals(productName));
        Assert.assertTrue(flag);

        driver.findElement(By.xpath("//button[contains(text(),'Checkout')]")).click();

        // input your country then click place order
        driver.findElement(By.xpath("//*[@placeholder='Select Country']")).sendKeys("Egypt");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()=' Egypt']")));
        driver.findElement(By.xpath("//span[text()=' Egypt']")).click();
        //wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='actions']/a")));
       // driver.findElement(By.xpath("//a[text()='Place Order ']")).click();


        actions.moveToElement(driver.findElement(By.xpath("//a[text()='Place Order ']")))
                .click()
                .perform();

        // make sure that thank you message text displayed is equals to the message you want
        Assert.assertTrue(driver.findElement(By.cssSelector(".hero-primary")).getText().equalsIgnoreCase("Thankyou for the order."));
        // get the order id text then go to the orders page
        String orderId = driver.findElement(By.cssSelector("label.ng-star-inserted")).getText().split("\\|")[1].trim();
        // make sure that order id in the orders page is equal to the order id you captured
        driver.findElement(By.xpath("//button[@routerlink='/dashboard/myorders']")).click();
        Assert.assertEquals(driver.findElement(By.xpath("//table/tbody/tr/th")).getText(),orderId);
        driver.quit();
    }


}
