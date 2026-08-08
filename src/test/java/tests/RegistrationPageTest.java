package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageobjects.ProductCatalogue;
import pageobjects.RegistrationPage;

public class RegistrationPageTest extends BaseTest {

    /**
     * Test successful registration with all required fields filled
     */
    @Test(description = "Verify user can successfully register with valid credentials")
    public void testSuccessfulRegistrationWithAllFields() {
        RegistrationPage registrationPage = new RegistrationPage(getDriver());

        // Navigate to registration page (adjust URL based on your app)
        // getDriver().get(getProperty("baseUrl") + "/register");
        getDriver().get("https://rahulshettyacademy.com/client/#/auth/register");

        // Fill all registration fields
        registrationPage.enterFirstName("Ahmed");
        registrationPage.enterLastName("Ali");
        registrationPage.enterEmail("ahmed.ali." + System.currentTimeMillis() + "@example.com");
        registrationPage.enterMobile("01012345678");
        registrationPage.selectOccupationByVisibleText("Engineer");
        registrationPage.chooseGender("Male");
        registrationPage.enterPassword("TestPassword123!");
        registrationPage.enterConfirmPassword("TestPassword123!");
        registrationPage.setAgreeAge(true);

        // Submit the form
        ProductCatalogue catalogue = registrationPage.registerFull(
                "Ahmed", "Ali",
                "ahmed.ali." + System.currentTimeMillis() + "@example.com",
                "01012345678",
                "Engineer",
                "Male",
                "TestPassword123!",
                true
        );

        // Verify navigation or success (adapt based on actual app behavior)
        Assert.assertNotNull(catalogue, "ProductCatalogue should be returned after successful registration");
    }

    /**
     * Test registration with all occupations
     */
    @Test(description = "Verify registration works with different occupations", dataProvider = "occupationData")
    public void testRegistrationWithDifferentOccupations(String occupation) {
        RegistrationPage registrationPage = new RegistrationPage(getDriver());

        getDriver().get("https://rahulshettyacademy.com/client/#/auth/register");

        // Fill all fields with occupation variation
        String uniqueEmail = "user." + System.currentTimeMillis() + "@example.com";
        registrationPage.enterFirstName("Test");
        registrationPage.enterLastName("User");
        registrationPage.enterEmail(uniqueEmail);
        registrationPage.enterMobile("01098765432");
        registrationPage.selectOccupationByVisibleText(occupation);
        registrationPage.chooseGender("Female");
        registrationPage.enterPassword("Pass@1234");
        registrationPage.enterConfirmPassword("Pass@1234");
        registrationPage.setAgreeAge(true);

        // Submit form
        ProductCatalogue catalogue = registrationPage.registerFull(
                "Test", "User", uniqueEmail, "01098765432",
                occupation, "Female", "Pass@1234", true
        );

        Assert.assertNotNull(catalogue, "Should complete registration with " + occupation + " occupation");
    }

    /**
     * Test registration with different genders
     */
    @Test(description = "Verify registration works with different genders", dataProvider = "genderData")
    public void testRegistrationWithDifferentGenders(String gender) {
        RegistrationPage registrationPage = new RegistrationPage(getDriver());

        getDriver().get("https://rahulshettyacademy.com/client/#/auth/register");

        String uniqueEmail = "gender." + System.currentTimeMillis() + "@example.com";
        registrationPage.enterFirstName("GenderTest");
        registrationPage.enterLastName("User");
        registrationPage.enterEmail(uniqueEmail);
        registrationPage.enterMobile("01055555555");
        registrationPage.selectOccupationByVisibleText("Doctor");
        registrationPage.chooseGender(gender);
        registrationPage.enterPassword("SecurePass123!");
        registrationPage.enterConfirmPassword("SecurePass123!");
        registrationPage.setAgreeAge(true);

        ProductCatalogue catalogue = registrationPage.registerFull(
                "GenderTest", "User", uniqueEmail, "01055555555",
                "Doctor", gender, "SecurePass123!", true
        );

        Assert.assertNotNull(catalogue, "Should complete registration with " + gender + " gender");
    }

    /**
     * Test first name field entry
     */
    @Test(description = "Verify first name can be entered correctly")
    public void testEnterFirstName() {
        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        getDriver().get("https://rahulshettyacademy.com/client/#/auth/register");
        String firstName = "FirstNameTest";

        registrationPage.enterFirstName(firstName);
        // Note: Add assertion to verify the value entered (if getter exists or via JavaScript)
        // For now, this test verifies no exception is thrown
    }

    /**
     * Test last name field entry
     */
    @Test(description = "Verify last name can be entered correctly")
    public void testEnterLastName() {
        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        getDriver().get("https://rahulshettyacademy.com/client/#/auth/register");
        String lastName = "LastNameTest";

        registrationPage.enterLastName(lastName);
        // Verify no exception thrown
    }

    /**
     * Test email field entry
     */
    @Test(description = "Verify email can be entered correctly")
    public void testEnterEmail() {
        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        getDriver().get("https://rahulshettyacademy.com/client/#/auth/register");
        String email = "test." + System.currentTimeMillis() + "@example.com";

        registrationPage.enterEmail(email);
        // Verify no exception thrown
    }

    /**
     * Test mobile field entry
     */
    @Test(description = "Verify mobile number can be entered correctly")
    public void testEnterMobile() {
        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        getDriver().get("https://rahulshettyacademy.com/client/#/auth/register");
        String mobile = "01012345678";

        registrationPage.enterMobile(mobile);
        // Verify no exception thrown
    }

    /**
     * Test password field entry
     */
    @Test(description = "Verify password can be entered correctly")
    public void testEnterPassword() {
        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        getDriver().get("https://rahulshettyacademy.com/client/#/auth/register");
        String password = "SecurePass@123";

        registrationPage.enterPassword(password);
        // Verify no exception thrown
    }

    /**
     * Test confirm password field entry
     */
    @Test(description = "Verify confirm password can be entered correctly")
    public void testEnterConfirmPassword() {
        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        getDriver().get("https://rahulshettyacademy.com/client/#/auth/register");
        String password = "SecurePass@123";

        registrationPage.enterConfirmPassword(password);
        // Verify no exception thrown
    }

    /**
     * Test age checkbox toggle
     */
    @Test(description = "Verify age checkbox can be checked and unchecked")
    public void testAgeCheckboxToggle() {
        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        getDriver().get("https://rahulshettyacademy.com/client/#/auth/register");

        // Set checkbox to checked
        registrationPage.setAgreeAge(true);
        // Verify (would need getter or JavaScript evaluation in real scenario)

        // Set checkbox to unchecked
        registrationPage.setAgreeAge(false);
        // Verify (would need getter or JavaScript evaluation in real scenario)
    }

    /**
     * Test that male gender option can be selected
     */
    @Test(description = "Verify male gender radio button can be selected")
    public void testSelectMaleGender() {
        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        getDriver().get("https://rahulshettyacademy.com/client/#/auth/register");
        registrationPage.chooseGender("Male");
        // Verify (would need getter or JavaScript evaluation in real scenario)
    }

    /**
     * Test that female gender option can be selected
     */
    @Test(description = "Verify female gender radio button can be selected")
    public void testSelectFemaleGender() {
        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        getDriver().get("https://rahulshettyacademy.com/client/#/auth/register");


        registrationPage.chooseGender("Female");
        // Verify (would need getter or JavaScript evaluation in real scenario)
    }

    /**
     * Test occupation dropdown selection
     */
    @Test(description = "Verify occupation can be selected from dropdown")
    public void testSelectOccupation() {
        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        getDriver().get("https://rahulshettyacademy.com/client/#/auth/register");
        registrationPage.selectOccupationByVisibleText("Student");
        // Verify selection (would need getter or assertion in real scenario)
    }

    /**
     * Test registration with minimum required fields (if applicable to your app)
     */
    @Test(description = "Verify registration with minimal required fields")
    public void testRegistrationWithMinimalFields() {
        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        getDriver().get("https://rahulshettyacademy.com/client/#/auth/register");

        String uniqueEmail = "minimal." + System.currentTimeMillis() + "@example.com";

        // Fill only required fields
        registrationPage.enterFirstName("Min");
        registrationPage.enterLastName("User");
        registrationPage.enterEmail(uniqueEmail);
        registrationPage.enterPassword("Pass1234!");
        registrationPage.enterConfirmPassword("Pass1234!");
        registrationPage.setAgreeAge(true);

        // Attempt registration with minimal data
        ProductCatalogue catalogue = registrationPage.registerFull(
                "Min", "User", uniqueEmail, null,
                null, null, "Pass1234!", true
        );

        Assert.assertNotNull(catalogue, "Should handle registration with minimal fields");
    }

    /**
     * Test registering multiple users in sequence
     */
    @Test(description = "Verify multiple users can be registered sequentially")
    public void testRegistrationMultipleUsers() {
        getDriver().get("https://rahulshettyacademy.com/client/#/auth/register");
        for (int i = 0; i < 3; i++) {
            RegistrationPage registrationPage = new RegistrationPage(getDriver());
            String uniqueEmail = "multi.user" + i + "." + System.currentTimeMillis() + "@example.com";

            registrationPage.enterFirstName("User" + i);
            registrationPage.enterLastName("Last" + i);
            registrationPage.enterEmail(uniqueEmail);
            registrationPage.enterMobile("010" + i + "2345678");
            registrationPage.selectOccupationByVisibleText("Scientist");
            registrationPage.chooseGender(i % 2 == 0 ? "Male" : "Female");
            registrationPage.enterPassword("Pass@" + i + "234");
            registrationPage.enterConfirmPassword("Pass@" + i + "234");
            registrationPage.setAgreeAge(true);

            ProductCatalogue catalogue = registrationPage.registerFull(
                    "User" + i, "Last" + i, uniqueEmail, "010" + i + "2345678",
                    "Scientist", i % 2 == 0 ? "Male" : "Female",
                    "Pass@" + i + "234", true
            );

            Assert.assertNotNull(catalogue, "User " + i + " registration should succeed");

            getDriver().get("https://rahulshettyacademy.com/client/#/auth/register");
        }
    }

    /**
     * Data provider for occupation values
     */
    @DataProvider(name = "occupationData")
    public Object[][] getOccupationData() {
        return new Object[][] {
                { "Doctor" },
                { "Student" },
                { "Engineer" },
                { "Scientist" }
        };
    }

    /**
     * Data provider for gender values
     */
    @DataProvider(name = "genderData")
    public Object[][] getGenderData() {
        return new Object[][] {
                { "Male" },
                { "Female" }
        };
    }

}

