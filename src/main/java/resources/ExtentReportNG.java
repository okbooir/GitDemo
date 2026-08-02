package resources;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.file.Paths;

public class ExtentReportNG {


    public static ExtentReports getReportObject() {
        // with help of ExtentReports & ExtentSparkReporter Classes

        ExtentSparkReporter reporter = new ExtentSparkReporter(
                Paths.get(System.getProperty("user.dir"), "reports", "index.html").toString());
        reporter.config().setReportName("Web Automation Results");
        reporter.config().setDocumentTitle("Test Results");

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Tester","Ahmed");
        return extent;
    }
}
