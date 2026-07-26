package base;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {

    int count=0;
    int maxTry = 1;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if(count<maxTry) {
            count++;
            System.out.println("retried");
            return true;
        }
        return false;
    }
}
