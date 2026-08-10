package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import pageobjects.LandingPage;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.openqa.selenium.support.ThreadGuard;
import java.util.logging.Level;

public class BaseTest {


    //public LandingPage landingPage;
    //public WebDriver driver;

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    //private final ThreadLocal<LandingPage> LANDING_PAGE = new ThreadLocal<>();




    @BeforeMethod (alwaysRun = true)
    //@Parameters("browser")
    protected void launchApp() throws IOException {
        initializeDriver();
        getDriver().get(getProperty("baseUrl"));
        //LandingPage currentLandingPage = new LandingPage(getDriver());
        //LANDING_PAGE.set(currentLandingPage);
        //currentLandingPage.goTo(getProperty("baseUrl"));
    }

    @BeforeMethod (onlyForGroups = "authenticated" , dependsOnMethods = "launchApp")
    public void authintication() {
        LandingPage landingPage = new LandingPage(getDriver());
        landingPage.login("okbooeg@gmail.com","Ilovetest1!");
    }


    @AfterMethod(alwaysRun = true)
    protected void teardown() {

        /*
         * Do not call getDriver() here because, if setup failed,
         * getDriver() would throw another exception.
         */
        WebDriver currentDriver = DRIVER.get();

        try {

            if (currentDriver != null) {
                currentDriver.quit();
            }

        } finally {

            /*
             * Remove both values from the current TestNG worker thread.
             */
            //LANDING_PAGE.remove();
            DRIVER.remove();

            System.out.println(
                    "Browser closed on thread: "
                            + Thread.currentThread().getId()
            );
        }
    }


    protected String getProperty(String property) throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream("config.properties"));
        //FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\java\\resources\\GlobalData.properties"); // convert the properties file to stream object
        //prop.load(fis);
        return prop.getProperty(property);
    }


    private WebDriver initializeDriver() throws IOException {

        String browserName = System.getProperty("browser")!=null ? System.getProperty("browser") : getProperty("browser");
        // String browserName = getProperty("browser");
        String downloadPath = Paths.get(System.getProperty("user.dir"), "downloads").toString();


        if (browserName.toLowerCase().contains("chrome")) {
            ChromeOptions options = new ChromeOptions();
            Map<String, Object> prefs = new HashMap<>();
            // prefs.put("profile.default_content_settings.popups",0);  // if you want block browser popups
            prefs.put("profile.default_content_setting_values.notifications", 2); // don't allow notifications from websites
            prefs.put("download.default_directory", downloadPath);
            prefs.put("download.prompt_for_download", false); // download file automatic without asking for download path
            options.setExperimentalOption("prefs", prefs);

            LoggingPreferences logs = new LoggingPreferences();
            logs.enable(LogType.BROWSER, Level.ALL);
            options.setCapability("goog:loggingPrefs", logs);

            if (browserName.toLowerCase().contains("headless")) {
                options.addArguments("--headless=new", "--window-size=1920,1080");            }
            WebDriverManager.chromedriver().setup();
            DRIVER.set(ThreadGuard.protect(new ChromeDriver(options)));

        }

        else if (browserName.toLowerCase().contains("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            WebDriverManager.firefoxdriver().setup();
            if (browserName.toLowerCase().contains("headless")) {
                options.addArguments("--headless=new", "--window-size=1920,1080");            }
            DRIVER.set(ThreadGuard.protect(new FirefoxDriver(options)));
        }

        else if (browserName.toLowerCase().contains("edge")) {
            EdgeOptions options = new EdgeOptions();
            WebDriverManager.edgedriver().setup();
            if (browserName.toLowerCase().contains("headless")) {
                options.addArguments("--headless=new", "--window-size=1920,1080");
            }
            DRIVER.set(ThreadGuard.protect(new EdgeDriver(options)));
        }
        else {
            throw new IllegalArgumentException(
                    "Unsupported browser: " + browserName
            );
        }

        //getDriver().manage().window().setSize(new Dimension(1440,900)); // run in full screen
        if (!browserName.toLowerCase().contains("headless")) {
            getDriver().manage().window().maximize();
        }

        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return getDriver();

        }

    protected final WebDriver getDriver() {

        WebDriver currentDriver = DRIVER.get();

        if (currentDriver == null) {
            throw new IllegalStateException(
                    "No WebDriver exists for thread: "
                            + Thread.currentThread().getId()
                            + ". Check that @BeforeMethod executed successfully."
            );
        }

        return DRIVER.get();
    }

    /*protected final LandingPage getLandingPage() {

        LandingPage currentLandingPage =
                LANDING_PAGE.get();

        if (currentLandingPage == null) {
            throw new IllegalStateException(
                    "No LandingPage exists for thread: "
                            + Thread.currentThread().getId()
                            + ". Check whether @BeforeMethod completed successfully."
            );
        }

        return currentLandingPage;
    } */

    protected List<HashMap<String,String>> getJsonDataToHashMap(String filePath) throws IOException {
        //convert json file to string and put it in a string variable
        String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
        // convert the json string to multiple hashmaps because it have different data sets (through jackson databind dependency) and store them in a list
        ObjectMapper mapper = new ObjectMapper();
        List<HashMap<String,String>> data = mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {
        });
        return data;

    }

    protected String getScreenshot(String testCaseName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) getDriver();
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destination = Paths.get(System.getProperty("user.dir"), "reports", testCaseName + ".png").toString();

        FileUtils.copyFile(source, new File(destination));

        System.out.println(
                "SETUP | Thread: "
                        + Thread.currentThread().getId()
                        + " | Driver: "
                        + System.identityHashCode(getDriver())
        );

        return testCaseName + ".png";

    }


}
