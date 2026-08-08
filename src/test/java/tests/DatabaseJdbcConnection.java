package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.sql.*;

public class DatabaseJdbcConnection {

    public static void main (String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/qadbt", "root", "8311266");
        Statement statement = connection.createStatement();
        ResultSet re = statement.executeQuery("select * from credentials where scenario='zerobalancecard';");
        String username;
        String password;
        while (re.next()) {
            username = re.getString("username");
            password = re.getString("password");
            System.out.println("username: "+username+" password: "+password);
            break;
        }

        WebDriver driver = new ChromeDriver();
        driver.get("https://login.salesforce.com/");
        driver.findElement(By.id("username")).sendKeys(re.getString("username"));
        driver.findElement(By.id("password")).sendKeys(re.getString("password"));

        re = statement.executeQuery("select * from credentials where scenario='rewardscard';");
        re.next();
        driver.get("https://login.salesforce.com/");
        driver.findElement(By.id("username")).sendKeys(re.getString("username"));
        driver.findElement(By.id("password")).sendKeys(re.getString("password"));




    }

}
