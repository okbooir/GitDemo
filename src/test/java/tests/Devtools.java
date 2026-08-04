package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.HasAuthentication;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UsernameAndPassword;
import java.util.function.Predicate;
import org.openqa.selenium.Credentials;
import org.openqa.selenium.chromium.HasCdp;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v148.network.Network;
import org.openqa.selenium.devtools.v148.network.model.*;
import org.openqa.selenium.devtools.v150.emulation.Emulation;
import org.openqa.selenium.devtools.v150.network.model.ErrorReason;
import org.openqa.selenium.devtools.v150.performance.Performance;
import org.openqa.selenium.devtools.v150.performance.model.Metric;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LogEntry;
import org.testng.Assert;
import org.testng.annotations.Test;

import org.openqa.selenium.devtools.v150.fetch.Fetch;
import org.openqa.selenium.devtools.v150.fetch.model.HeaderEntry;
import org.openqa.selenium.devtools.v150.fetch.model.RequestPattern;
import org.openqa.selenium.devtools.v150.fetch.model.RequestStage;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import java.util.*;

import java.util.function.Supplier;

public class Devtools extends BaseTest {

    @Test (enabled = false)
    public void devTools_mobileView() throws InterruptedException {
        HasDevTools chromeDriver = (HasDevTools) getDriver();

        org.openqa.selenium.devtools.DevTools devTools = chromeDriver.getDevTools();
        devTools.createSession();
        devTools.send(Emulation.setDeviceMetricsOverride(600, 1000, 50, true, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));

        getDriver().get("https://rahulshettyacademy.com/angularAppdemo/");
        getDriver().findElement(By.cssSelector(".navbar-toggler")).click();
        Thread.sleep(2000);
        getDriver().findElement(By.linkText("Library")).click();


        // or (2nd way) through driver directrly  without selenium .send method

        HasCdp cdpDriver = (HasCdp) getDriver();

        Map deviceMetrics = new HashMap();
        deviceMetrics.put("width", 600);
        deviceMetrics.put("height", 1000);
        deviceMetrics.put("deviceScaleFactor", 50);
        deviceMetrics.put("mobile", true);


        cdpDriver.executeCdpCommand("Emulation.setDeviceMetricsOverride", deviceMetrics);

        getDriver().get("https://rahulshettyacademy.com/angularAppdemo/");
        getDriver().findElement(By.cssSelector(".navbar-toggler")).click();
        Thread.sleep(2000);
        getDriver().findElement(By.linkText("Library")).click();
    }

    @Test (enabled = false)
    public void devTools_localizationTesting() {
        HasDevTools chromeDriver = (HasDevTools) getDriver();

        org.openqa.selenium.devtools.DevTools devTools = chromeDriver.getDevTools();
        devTools.createSession();
        HasCdp cdpDriver = (HasCdp) getDriver();

        Map<String, Object> coordinates = new HashMap<String, Object>();
        coordinates.put("latitude", 40.4168);
        coordinates.put("longitude", -3.7038);
        coordinates.put("accuracy", 1);

        cdpDriver.executeCdpCommand("Emulation.setGeolocationOverride", coordinates);
        getDriver().get("http://www.google.com");
        getDriver().findElement(By.name("q")).sendKeys("netflix", Keys.ENTER);
    }

    @Test (enabled = false)
    public void devTools_logNetworkActivity() { // ex. to check responses status codes and make sure they are success
        HasDevTools chromeDriver = (HasDevTools) getDriver();
        org.openqa.selenium.devtools.DevTools devTools = chromeDriver.getDevTools();
        devTools.createSession();

        //enable network activity track
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));

        // listen to a network event of: the request will be sent, and get request url, headers, and print them.
        devTools.addListener(Network.requestWillBeSent(), requestWillBeSent -> {
            Request req = requestWillBeSent.getRequest();
            //System.out.println(req.getUrl());
            //System.out.println(req.getHeaders());
            //System.out.println("--------------------------");
        });
        // listen to a network event of: http response received from api, and get response url, status, headers, and print them - OR REPORT IF STATUS CODE IS NOT SUCCESS
        devTools.addListener(Network.responseReceived(), responseReceived -> {
            Response res = responseReceived.getResponse();
            //System.out.println(res.getStatus());
            //System.out.println("--------------------------");
            if (res.getStatus().toString().startsWith("4") || res.getStatus().toString().startsWith("5")) {
                System.out.println(res.getUrl() + " is failing with status code: " + res.getStatus());

            }
        });
        getDriver().get("https://rahulshettyacademy.com/angularAppdemo");
        getDriver().findElement(By.cssSelector(".btn-primary")).click();


    }

    @Test (enabled = false)
    public void devTools_mockResponsOfNetworkRequest() throws InterruptedException {  // will mock an api response of a specific request to see that data on page updated according to the mocked response
        HasDevTools chrome = (HasDevTools) getDriver();

        DevTools devTools = chrome.getDevTools();
        devTools.createSession();

        RequestPattern pattern = new RequestPattern(
                Optional.of("*Library/GetBook.php?AuthorName=shetty*"),
                Optional.empty(),
                Optional.of(RequestStage.REQUEST)
        );

        devTools.send(Fetch.enable(Optional.of(List.of(pattern)), Optional.of(false)));

        devTools.addListener(Fetch.requestPaused(), request -> {

            String mockResponse = """
                        [
                             {
                                 "book_name": "RobotFramework",
                                 "isbn": "RS843",
                                 "aisle": "25298957"
                             }
                        ]
                    """;

            String encodedBody = Base64.getEncoder()
                    .encodeToString(
                            mockResponse.getBytes(StandardCharsets.UTF_8)
                    );

            List<HeaderEntry> headers = List.of(
                    new HeaderEntry(
                            "Content-Type",
                            "application/json"
                    )

            );

            devTools.send(Fetch.fulfillRequest(request.getRequestId(), 200, Optional.of(headers), Optional.empty(), Optional.of(encodedBody), Optional.empty()));
        });

        getDriver().get("https://rahulshettyacademy.com/angularAppdemo");
        getDriver().findElement(By.cssSelector(".btn-primary")).click();
        Assert.assertEquals(getDriver().findElement(By.cssSelector("p")).getText() , "Oops only 1 Book available");
    }

    @Test (enabled = false)
    public void devTools_mockRequestUrlOfNetworkRequest() throws InterruptedException {  // will mock an api request URL of a specific request
        HasDevTools chrome = (HasDevTools) getDriver();

        DevTools devTools = chrome.getDevTools();
        devTools.createSession();

        devTools.send(Fetch.enable(Optional.empty(),Optional.empty()));

        devTools.addListener(Fetch.requestPaused() , request -> {
            if (request.getRequest().getUrl().contains("shetty")) {
                String mockedUrl = request.getRequest().getUrl().replace("=shetty","=BadGuy");
                devTools.send(Fetch.continueRequest(request.getRequestId(),Optional.of(mockedUrl),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty()));
            }
            else {
                devTools.send(Fetch.continueRequest(request.getRequestId(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty()));
        }});

        getDriver().get("https://rahulshettyacademy.com/angularAppdemo");
        getDriver().findElement(By.cssSelector(".btn-primary")).click();

    }

    @Test (enabled = false)
    public void devTools_FailaRequest() throws InterruptedException {  // we will fail a network request to check a specific fail message that will shown on page
        HasDevTools chrome = (HasDevTools) getDriver();

        DevTools devTools = chrome.getDevTools();
        devTools.createSession();

        RequestPattern pattern = new RequestPattern(
                Optional.of("*Library/GetBook.php?AuthorName=shetty*"),
                Optional.empty(),
                Optional.of(RequestStage.REQUEST)
        );

        devTools.send(Fetch.enable(Optional.of(List.of(pattern)),Optional.empty()));

        devTools.addListener(Fetch.requestPaused() , requestPaused -> {
            devTools.send(Fetch.failRequest(requestPaused.getRequestId(), ErrorReason.ABORTED));
        });

        getDriver().get("https://rahulshettyacademy.com/angularAppdemo");
        getDriver().findElement(By.cssSelector(".btn-primary")).click();
        Thread.sleep(20000);


    }

    @Test (enabled = false)
    public void devTools_BlockaRequest_BlockUrl() throws InterruptedException { // make the request not made from beginning (ex. to stop loading of css/images/components on the page)
        HasDevTools chrome = (HasDevTools) getDriver();

        DevTools devTools = chrome.getDevTools();
        devTools.createSession();

        List<BlockPattern> patterns = List.of(
                new BlockPattern("*://*:*/*.jpg", true),
                new BlockPattern("*://*:*/*.jpeg", true),
                new BlockPattern("*://*:*/*.png", true),
                new BlockPattern("*://*:*/*.css", true)
        );
        devTools.send(Network.enable(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty()));
        devTools.send(Network.setBlockedURLs(Optional.of(patterns), Optional.empty()));

        getDriver().get("https://rahulshettyacademy.com/");
        //getDriver().findElement(By.cssSelector(".btn-lg.btn-success")).click();


    }

    @Test (enabled = false)
    public void devTools_mockNetworkSpeed() throws InterruptedException {   // mock speed ( make slow speed network by applying LATENCY(delay), downloadBytes, uploadBytes ) to see website behavior to determine the waits secs you should use in your tests and catch http fails
        HasDevTools chrome = (HasDevTools) getDriver();

        DevTools devTools = chrome.getDevTools();
        devTools.createSession();

        NetworkConditions conditions = new NetworkConditions("",3000,20000,10000, Optional.of(ConnectionType.ETHERNET),Optional.empty(),Optional.empty(),Optional.empty());

        devTools.send(Network.enable(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty()));
        devTools.send(Network.emulateNetworkConditionsByRule(false,List.of(conditions)));

        // capture error and time when http request call fail
        devTools.addListener(Network.loadingFailed() , loadingFailed -> {
            System.out.println(loadingFailed.getErrorText());
            System.out.println(loadingFailed.getTimestamp());
        });

        getDriver().get("https://rahulshettyacademy.com/angularAppdemo/");
        getDriver().findElement(By.cssSelector(".btn-primary")).click();

    }

    @Test (enabled = false)
    public void devTools_handleBasicAuth() throws InterruptedException {   // handle basic auth window to login to a link the require user & pass (( Best for CI because AUTO IT can't handle it in headless in jenkins ))
// predicate (intro
        Predicate<URI> uriPredicate = uri -> uri.getHost().contains("httpbin.org");

        HasAuthentication ha = (HasAuthentication) getDriver();

        ha.register(uriPredicate,UsernameAndPassword.of("foo","bar"));
        getDriver().get("https://httpbin.org/basic-auth/foo/bar");

    }

    @Test
    public void devTools_logJavaScriptErrors() throws InterruptedException { // log javascript errors than always shown in the console of dev tools (ex. error in calculation of products qty prices)
        // log when any failures only ( capture errors of javascript from console)
        //the correct behavior is to write this code in onTestFailure() in listeners class
        getDriver().get("https://rahulshettyacademy.com/angularAppdemo");
        getDriver().findElement(By.cssSelector(".btn-lg.btn-success")).click();
        getDriver().findElement(By.partialLinkText("Selenium")).click();
        getDriver().findElement(By.cssSelector(".add-to-cart")).click();
        getDriver().findElement(By.partialLinkText("Cart")).click();
        getDriver().findElement(By.id("exampleInputEmail1")).clear();
        getDriver().findElement(By.id("exampleInputEmail1")).sendKeys("2");

        LogEntries browserLogs = getDriver().manage().logs().get(LogType.BROWSER);  // create object of LogEntries class to get browser logs
        List<LogEntry> allLogs = browserLogs.getAll(); // store all logs in a list
        for (LogEntry e : allLogs) {   //iterate to the list to print each log
            System.out.println(e.getMessage());
        }


    }


}
