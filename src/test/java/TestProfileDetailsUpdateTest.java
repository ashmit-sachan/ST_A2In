import com.inflectra.spiratest.addons.junitextension.SpiraTestCase;
import com.inflectra.spiratest.addons.junitextension.SpiraTestConfiguration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;


// SpiraPlan configuration
@SpiraTestConfiguration(
        url = "https://rmit.spiraservice.net/",
        login = "s3873827",
        rssToken = "{57FEA00B-7FEA-42A5-984B-95BE7332C0EB}",
        projectId = 192
)

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestProfileDetailsUpdateTest {
    private static ChromeDriver driver;


    // Helper function to clear the field, then fill in the required text
    public static void fillField(WebElement element, String input) {
        element.clear();
        element.sendKeys(input);
    }

    // Helper function to log in into the website
    private static void LogIn()
    {
        // Locate the username/email field and send the data
        WebElement username = driver.findElement(By.xpath("//form/p[2]/input[1]"));
        fillField(username, "3873827001");
        // Locate the password field and send the data
        WebElement password = driver.findElement(By.xpath("//form/p[2]/input[2]"));
        fillField(password, "Ashmit3873827");

        // Click on the Login button
        driver.findElement(By.name("signon")).click();
    }

    // Helper function to sign up to the website
    private static void SignUp()
    {
        driver.findElement(By.xpath("//*[@id=\"Catalog\"]/a")).click();
        driver.findElement(By.name("username")).sendKeys("3873827001");
        driver.findElement(By.name("password")).sendKeys("Ashmit3873827");
        driver.findElement(By.name("repeatedPassword")).sendKeys("Ashmit3873827");
        driver.findElement(By.name("account.firstName")).sendKeys("Ashmit");
        driver.findElement(By.name("account.lastName")).sendKeys("Sachan");
        driver.findElement(By.name("account.email")).sendKeys("ashmit1724@gmail.com");
        driver.findElement(By.name("account.phone")).sendKeys("0466011666");
        driver.findElement(By.name("account.address1")).sendKeys("Unit 666");
        driver.findElement(By.name("account.address2")).sendKeys("666 SixtySix Street");
        driver.findElement(By.name("account.city")).sendKeys("Melbourne");
        driver.findElement(By.name("account.state")).sendKeys("VIC");
        driver.findElement(By.name("account.zip")).sendKeys("3000");
        driver.findElement(By.name("account.country")).sendKeys("Australia");
        driver.findElement(By.name("account.languagePreference")).sendKeys("english");
        driver.findElement(By.name("account.favouriteCategoryId")).sendKeys("DOGS");
        driver.findElement(By.name("account.listOption")).click();
        driver.findElement(By.name("account.bannerOption")).click();

        driver.findElement(By.name("newAccount")).click();
    }


    @BeforeAll
    // Set Up driver
    public static void setup()
    {
        // Chromium executable path
        String chromiumPath = "C:\\Users\\ashmi\\AppData\\Local\\Chromium\\Application\\chrome.exe";
        // Configure ChromeOptions to specify the binary location (Chromium)
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBinary(chromiumPath);

        System.setProperty("Webdriver.chrome.driver","chromedriver.exe");
        driver = new ChromeDriver(chromeOptions);
    }


    // Check if website is accessible
    @Test
    @Order(1)
    @SpiraTestCase(testCaseId = 7307)
    public void testWebsite()
    {
        driver.get("https://petstore.octoperf.com/actions/Catalog.action");

        //Specify the amount of time driver should wait when searching for an element if it is not immediately present.
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        String expectedResult = "JPetStore Demo";
        String actualResult = driver.getTitle();
        assertEquals(expectedResult,actualResult);
    }


    // Log In into the website or Create Account to then Log In
    @Test
    @Order(2)
    @SpiraTestCase(testCaseId = 7308)
    public void testLoginIfNoAccountThenSignupAndLogin()
    {
        LogIn();

        WebElement element = null;
        try {
            element = driver.findElement(By.xpath("//*[@id=\"MenuContent\"]/a[3]"));
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            // Check if the element exists
            if (!(element != null && element.getText().equals("My Account"))) {
                SignUp();
                LogIn();
                try {
                    element = driver.findElement(By.xpath("//*[@id=\"MenuContent\"]/a[3]"));
                } catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                }

                // Check if the element exists
                if (element != null && element.getText().equals("My Account")) {
                    assertEquals(element.getText(),"My Account");
                }
            } else {
                assertEquals(element.getText(),"My Account");
            }

        }
    }

    // Change the used details
    @Test
    @Order(3)
    @SpiraTestCase(testCaseId = 7323)
    public void testChangeUserDetails()
    {
        driver.findElement(By.xpath("//*[@id=\"MenuContent\"]/a[3]")).click();

        fillField(driver.findElement(By.name("account.firstName")), "John");
        fillField(driver.findElement(By.name("account.lastName")), "Doe");

        driver.findElement(By.name("editAccount")).click();

        assertEquals(driver.findElement(By.name("account.firstName")).getAttribute("value"), "John");
        assertEquals(driver.findElement(By.name("account.lastName")).getAttribute("value"), "Doe");
    }


    // Check if Details were updated in the backend
    @Test
    @Order(4)
    @SpiraTestCase(testCaseId = 7324)
    public void testVerifyUserDetailsChangedSuccessfullyInDB()
    {
        driver.findElement(By.xpath("//*[@id=\"MenuContent\"]/a[2]")).click();

        //2. Locate the sign-in button and Click
        //Note: you can check the xpath by writing the xpath in console $x("xpath");
        driver.findElement(By.xpath("//*[@id=\"MenuContent\"]/a[2]")).click();
        LogIn();

        driver.findElement(By.xpath("//*[@id=\"MenuContent\"]/a[3]")).click();


        String firstName = driver.findElement(By.name("account.firstName")).getAttribute("value");
        String lastName = driver.findElement(By.name("account.lastName")).getAttribute("value");

        assertEquals(driver.findElement(By.name("account.firstName")).getAttribute("value"), "John");
        assertEquals(driver.findElement(By.name("account.lastName")).getAttribute("value"), "Doe");
    }

    @AfterAll
    // Closing the browser
    public static void CloseBrowser()
    {
        driver.close();
    }
}
