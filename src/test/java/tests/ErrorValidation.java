package tests;

import base.BaseTest;
import base.Retry;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chromium.HasCdp;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v148.network.Network;
import org.openqa.selenium.devtools.v148.network.model.Request;
import org.openqa.selenium.devtools.v148.network.model.Response;
import org.openqa.selenium.devtools.v150.emulation.Emulation;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageobjects.*;

import org.openqa.selenium.devtools.v150.fetch.Fetch;
import org.openqa.selenium.devtools.v150.fetch.model.HeaderEntry;
import org.openqa.selenium.devtools.v150.fetch.model.RequestPattern;
import org.openqa.selenium.devtools.v150.fetch.model.RequestStage;
import java.nio.charset.StandardCharsets;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

import org.apache.poi.ss.usermodel.DataFormatter;

import java.util.logging.Formatter;
import java.nio.file.Files;
import java.nio.file.Path;

public class ErrorValidation extends BaseTest {

    @Test(retryAnalyzer = Retry.class , enabled = false)
    public void loginErrorValidation() throws IOException {
        LandingPage currentLandingPage = new LandingPage(getDriver());
        currentLandingPage.login("oir@gmail.com", "Ilovest1!");
        String errorMessage = currentLandingPage.getErrorMessage();
        Assert.assertEquals(errorMessage, "Incorrrect email or password.");

    }

    // data from hashmap
    @Test(groups = "errorHandling", dataProvider = "getHash")
    public void productErrorValidation(HashMap<String, String> map) throws IOException {
        LandingPage currentLandingPage = new LandingPage(getDriver());
        ProductCatalogue productCatalogue = currentLandingPage.login(map.get("email"), map.get("password"));

        productCatalogue.addProductByName(map.get("productName"));
        CartPage cartPage = productCatalogue.clickonCart();

        boolean result = cartPage.checkCartProducts(map.get("productName"));
        Assert.assertTrue(result);
    }

    // download and upload
    @Test
    public void upload_and_download() throws IOException {
        getDriver().get(getProperty("uploadDownload"));
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

        //download
        getDriver().findElement(By.cssSelector("#downloadButton")).click();

        new WebDriverWait(getDriver(), Duration.ofSeconds(30))
                .until(d -> Files.exists(Paths.get(System.getProperty("user.dir"), "downloads", "download.xlsx")));

        //edit excel
        editCell2(Path.of(System.getProperty("user.dir"), "downloads", "download.xlsx").toString(), "price", "apple", "380");
        //editCell("C:\\Users\\ahmed\\Downloads\\download.xlsx", 2, 3, "350");

        //upload
        getDriver().findElement(By.cssSelector("input[type='file']")).sendKeys(Path.of(System.getProperty("user.dir"), "downloads", "download.xlsx").toString());

        // wait for success message to show then disappear

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".Toastify__toast-body div:nth-child(2)")));
        Assert.assertEquals(getDriver().findElement(By.cssSelector(".Toastify__toast-body div:nth-child(2)")).getText(), "Updated Excel Data Successfully.");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".Toastify__toast-body div:nth-child(2)")));


        // verify updated data in the web table

        String fruitColNum = getDriver().findElement(By.xpath("//div[text()='Fruit Name']")).getDomAttribute("data-column-id");
        String priceColNum = getDriver().findElement(By.xpath("//div[text()='Price']")).getDomAttribute("data-column-id");
        List<WebElement> fruits = getDriver().findElements(By.xpath("//div[@data-column-id='" + fruitColNum + "']"));
        fruits.stream().filter(s -> s.getText().toLowerCase().contains("apple")).forEach(s -> Assert.assertTrue(s.findElement(By.xpath("../div[@id='cell-" + priceColNum + "-undefined']")).getText().equals("380")));

        List<WebElement> fruitz = getDriver().findElements(By.xpath("//div[@role='row']"));
        fruitz.stream().filter(s -> s.getText().toLowerCase().contains("apple")).forEach(s -> Assert.assertTrue(s.findElement(By.xpath("div[@id='cell-" + priceColNum + "-undefined']")).getText().equals("380")));
    }

    // Data from json file
    @Test(groups = "errorHandling", dataProvider = "jsonConverted", retryAnalyzer = Retry.class)
    public void submitOrder(HashMap<String, String> testData) throws IOException {
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

    // Data from multi dimensional array
    @Test(groups = {"purchase", "authenticated"}, dataProvider = "getData")
    public void orderIdValidation(String email, String pass, String productName, String country) throws IOException {
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

    @Test
    public void testForExcel() throws IOException {
        ArrayList<String> data = excelData("purchase");
        for (String d : data) {
            System.out.println(d);
        }
        ;
    }

    @Test(dataProvider = "getExcel")
    public void excelDataProvide(String a1, String a2, String a3, String a4) {
        System.out.println(a1 + " " + a2 + " " + a3 + " " + a4);
    }

    @Test(dataProvider = "excelAllRows")
    public void excelDataProvider(String a1, String a2, String a3, String a4) {
        System.out.println(a1 + " " + a2 + " " + a3 + " " + a4);
    }

    @Test
    public void handleWindowsAuthPopup () throws InterruptedException {
        getDriver().get("https://admin:admin@the-internet.herokuapp.com/");
        // getDriver().get("https://the-internet.herokuapp.com/");
        getDriver().findElement(By.linkText("Basic Auth")).click();
    }

    @Test
    public void fileUploadWithAutoIt () throws InterruptedException, IOException {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(30));
        getDriver().get("https://www.ilovepdf.com/pdf_to_jpg");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("pickfiles")));
        getDriver().findElement(By.id("pickfiles")).click();
        String[] path = {System.getProperty("user.dir") + "\\src\\test\\resources\\executables\\fileupload.exe"};
        Runtime.getRuntime().exec(path);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("processTaskTextBtn")));
        getDriver().findElement(By.id("processTaskTextBtn")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("pickfiles")));
        getDriver().findElement(By.id("pickfiles")).click();
        new WebDriverWait(getDriver(), Duration.ofSeconds(30))
                .until(d -> Files.exists(Paths.get(System.getProperty("user.dir"), "downloads", "pdf-sample_0_page-0001.jpg")));
        Assert.assertTrue(Files.exists(Paths.get(System.getProperty("user.dir"), "downloads", "pdf-sample_0_page-0001.jpg")));
        Files.deleteIfExists(Paths.get(System.getProperty("user.dir"), "downloads", "pdf-sample_0_page-0001.jpg"));
    }


    @DataProvider(name = "getExcel")
    private Object[][] getExcel() throws IOException {

        String[] inputs = {"login", "purchase", "add profile", "delete profile"};
        Object[][] data = new Object[inputs.length][];
        for (int i = 0; i < inputs.length; i++) {
            data[i] = excelData(inputs[i]).toArray();
        }
        return data;
        // ----- USING MULTI-DIMENSION ARRAY ((Accept any data type)) -------
        //return new Object[][]{{excelData("delete profile")}};
    }


    @DataProvider(name = "getData")
    private Object[][] getData() {
        // ----- USING MULTI-DIMENSION ARRAY ((Accept any data type)) -------
        return new Object[][]{{"ookbooir@gmail.com", "Ilovetest1!", "ZARA COAT 3", "Egypt"}, {"okbooir@gmail.com", "Ilovetest2!", "ADIDAS ORIGINAL", "Egypt"}};
    }

    @DataProvider(name = "getHash", parallel = true)
    //(preffer to come fro json so no test data shown here in the test file
    private Object[][] getHash() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", "okbooir@gmail.com");
        map.put("password", "Ilovetest2!");
        map.put("productName", "ZARA COAT 3");

        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("email", "ookbooir@gmail.com");
        map1.put("password", "Ilovetest1!");
        map1.put("productName", "ADIDAS ORIGINAL");

        return new Object[][]{{map}, {map1}};

    }

    @DataProvider(name = "jsonConverted", parallel = true)
    private Object[][] jsonConverted() throws IOException {
        List<HashMap<String, String>> testData = getJsonDataToHashMap(System.getProperty("user.dir") + "\\src\\main\\java\\data\\testData.json");
        return new Object[][]{{testData.get(0)}, {testData.get(1)}};
    }


    public ArrayList<String> excelData(String testCaseName) throws IOException {
        FileInputStream fis = new FileInputStream(Path.of(System.getProperty("user.dir"), "downloads", "Book1.xlsx").toFile());
        // creating object of XSSFWorkbook class that takes the object of the fielinputstream object that access the excel file
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        int colIndex = 0;
        ArrayList<String> data = new ArrayList<String>();
        // store number of excel sheets in a field so you can loop on
        int sheets = workbook.getNumberOfSheets();
        // loop on excel sheets to choose the sheet i want, and store it in a sheet field
        for (int i = 0; i < sheets; i++) {
            if (workbook.getSheetName(i).equalsIgnoreCase("testdata")) {
                XSSFSheet sheet = workbook.getSheetAt(i); // sheet is collection of rows of our desired sheet
                // identify  testcases column by scanning the entire 1st row (header)
                Iterator<Row> rows = sheet.iterator(); // iterator for excel rows of our desired sheet
                Row firstRow = rows.next(); // now you are in 1st row (header) and store it in a Row field
                Iterator<Cell> ce = firstRow.cellIterator(); // iterator for cells of the 1st row
                while (ce.hasNext()) {   //loop on the cells iterator of 1st row to reach to desired cell that have "testCases"
                    Cell value = ce.next();
                    if (value.getStringCellValue().equalsIgnoreCase("testcases")) {
                        colIndex = value.getColumnIndex(); // save the desired cell column index in a field
                        break;

                    }
                }
                for (Row r : sheet) {  // loop on the rows of the sheet, and in each row search about "purchase" in only the index of the cell you saved before (colIndex)
                    if (r.getCell(colIndex).getStringCellValue().equalsIgnoreCase(testCaseName)) {
                        Iterator<Cell> ce2 = r.cellIterator();
                        while (ce2.hasNext()) {   //loop on the cells iterator of the row to add each cell string into the array list to feed to the test
                            Cell c = ce2.next();
                            if (c.getCellType() == CellType.STRING) {
                                data.add(c.getStringCellValue());
                            } else {
                                data.add(NumberToTextConverter.toText(c.getNumericCellValue()));
                            }
                        }
                        break;
                    }
                }
                break;


            }

        }
        return data;
    }


    @DataProvider(name = "excelAllRows")
    public Object[][] loopExcelRows() throws IOException {
        FileInputStream fis = new FileInputStream(Path.of(System.getProperty("user.dir"), "downloads", "Book1.xlsx").toFile());
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getPhysicalNumberOfRows(); // no. of rows in the sheet
        XSSFRow row1 = sheet.getRow(1);  //its row 1
        short columnscount = row1.getLastCellNum(); // number of columns in the excel
        Object[][] mData = new Object[rowCount - 1][columnscount];

        for (int row = 0; row < rowCount - 1; row++) {
            for (int col = 0; col < columnscount; col++) {
                XSSFCell ce = sheet.getRow(row + 1).getCell(col);
                DataFormatter formatter = new DataFormatter();
                mData[row][col] = formatter.formatCellValue(ce);
            }
        }
        return mData;
    }

    public void editCell(String filePath, int rowIndex, int columnIndex, String newValue) throws IOException {

        FileInputStream input = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(input);
        input.close();

        XSSFSheet sheet = workbook.getSheetAt(0);

        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            cell = row.createCell(columnIndex);
        }

        cell.setCellValue(newValue);

        FileOutputStream output = new FileOutputStream(filePath);
        workbook.write(output);

        output.close();
        workbook.close();
    }



    public void editCell2(String filepath, String colName, String fruitName, String newvalue) throws IOException {

        DataFormatter formatter = new DataFormatter();
        FileInputStream input = new FileInputStream(filepath);
        XSSFWorkbook workbook = new XSSFWorkbook(input);
        input.close();

        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowNum =0;
        int colNum=0;

        Iterator<Cell> c = sheet.getRow(0).cellIterator();
        while (c.hasNext()) {
           Cell ce =  c.next();
           if(ce.getCellType() == CellType.STRING && ce.getStringCellValue().equalsIgnoreCase(colName)) {
               colNum = ce.getColumnIndex();
               break;
           }
        }


        Iterator<Row> rows = sheet.iterator();
        while (rows.hasNext()) {
            Row r = rows.next();
            Iterator<Cell> cc = r.cellIterator();
            while (cc.hasNext()) {
                Cell k = cc.next();
                if (k.getCellType() == CellType.STRING && k.getStringCellValue().equalsIgnoreCase(fruitName)) {
                    rowNum = k.getRowIndex();
                    break;
                }
            }
        }

        Row row = sheet.getRow(rowNum);

        Cell cell = row.getCell(colNum);

        cell.setCellValue(newvalue);

        FileOutputStream output = new FileOutputStream(filepath);
        workbook.write(output);

        output.close();
        workbook.close();
    }
}