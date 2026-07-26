package resources;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportNG {

    public static ExtentReports getReportObject() {
        // with help of ExtentReports & ExtentSparkReporter Classes

        ExtentSparkReporter reporter = new ExtentSparkReporter(System.getProperty("user.dir")+"\\reports\\index.html");
        reporter.config().setReportName("Web Automation Results");
        reporter.config().setDocumentTitle("Test Results");

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Tester","Ahmed");
        return extent;
    }
}
