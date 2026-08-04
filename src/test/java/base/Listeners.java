package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LogEntry;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import resources.ExtentReportNG;

import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// ITestListener interface which implements testNg Listeners
public class Listeners extends BaseTest implements ITestListener {      // we implement ITestListener Interface
    private final ExtentReports extent = ExtentReportNG.getReportObject();
    private final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();
    private static final Logger logger = LogManager.getLogger(Listeners.class);

    @Override
    public void onTestStart(ITestResult result) {
        ITestListener.super.onTestStart(result);
        // String browserName = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        try {
            ExtentTest test = extent.createTest(result.getName() + " [" + System.getProperty("browser") + "]");
            extentTest.set(test);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        //ITestListener.super.onTestSuccess(result);
        extentTest.get().log(Status.PASS , "Test Passed");
        extentTest.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest currentExtentTest = extentTest.get();

        // Add the failure exception to ExtentReports
        if (currentExtentTest != null) {
            currentExtentTest.fail(result.getThrowable());
        }

        //if (getDriver() != null) {

            LogEntries browserLogs = getDriver().manage().logs().get(LogType.BROWSER);

            for (LogEntry log : browserLogs) {

                if (log.getLevel().intValue() >= Level.SEVERE.intValue()) {

                    System.out.println("JS ERROR: " + log.getMessage());
                    logger.error("Browser JS Error: {}", log.getMessage());
                    if (currentExtentTest != null) {
                        currentExtentTest.fail("Browser JS Error: " + log.getMessage());
                    }
                }
            }
        //}

        // Object testInstance = result.getInstance();

       /* if (!(testInstance instanceof BaseTest)) {

            if (currentExtentTest != null) {
                currentExtentTest.warning(
                        "Screenshot was not captured because the test class "
                                + "does not extend BaseTest."
                );
            }

            return;
        }

        BaseTest baseTest = (BaseTest) testInstance; */

        try {
            /*
             * This retrieves the WebDriver associated with
             * the current TestNG thread.
             */
            //WebDriver currentDriver = baseTest.getDriver();

            /*
             * A unique name prevents parallel tests from
             * overwriting each other's screenshots.
             */
            String screenshotName =
                    result.getMethod().getMethodName()
                            + "_thread_"
                            + Thread.currentThread().getId()
                            + "_"
                            + System.currentTimeMillis();

            String screenshotPath = getScreenshot(
                    screenshotName);

            System.out.println(
                    "FAILURE SCREENSHOT | Thread: "
                            + Thread.currentThread().getId()
                            + " | Driver: "
                            + System.identityHashCode(getDriver())
            );

            if (currentExtentTest != null) {
                currentExtentTest.addScreenCaptureFromPath(
                        screenshotPath,
                        "Failure Screenshot"
                );
            }

        } catch (IllegalStateException exception) {

            /*
             * This can happen when @BeforeMethod failed before
             * the driver was initialized.
             */
            if (currentExtentTest != null) {
                currentExtentTest.warning(
                        "Screenshot was not captured because the driver "
                                + "was unavailable: "
                                + exception.getMessage()
                );
            }

        } catch (Exception exception) {

            if (currentExtentTest != null) {
                currentExtentTest.warning(
                        "Screenshot capture failed: "
                                + exception.getMessage()
                );
            }
        }
        extentTest.remove();
        // ITestListener.super.onTestFailure(result);
        /*extentTest.get().fail(result.getThrowable());

        WebDriver driver = null;
        String path = null;

        try {

            driver = (WebDriver) result.getTestClass()
                    .getRealClass()
                    .getField("driver")
                    .get(result.getInstance());

            path = getScreenshot(result.getName(), driver);

        } catch (Exception e) {
            e.printStackTrace();
        }

        extentTest.get().addScreenCaptureFromPath(path,result.getName()); */

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP , "Test Skipped");
        extentTest.remove();
        // ITestListener.super.onTestSkipped(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
    }

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        ITestListener.super.onFinish(context);
        extent.flush();
    }
}
