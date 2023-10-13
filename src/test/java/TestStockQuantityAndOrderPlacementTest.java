import com.inflectra.spiratest.addons.junitextension.SpiraTestCase;
import com.inflectra.spiratest.addons.junitextension.SpiraTestConfiguration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


// SpiraPlan configuration
@SpiraTestConfiguration(
        url = "https://rmit.spiraservice.net/",
        login = "s3873827",
        rssToken = "{57FEA00B-7FEA-42A5-984B-95BE7332C0EB}",
        projectId = 192
)


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestStockQuantityAndOrderPlacementTest {

    private static ChromeDriver driver;
    private static int stockQTY = 0;
    // Initialize an array of Selenium WebElements with 3 elements
    private static String animalBoughtHrefLink = null;


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

        //3.1. Locate the username/email field and send the data
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

        // Find the elements and fill the form
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
//    @SpiraTestCase(testCaseId = 7307)
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
//    @SpiraTestCase(testCaseId = 7308)
    public void testLoginIfNoAccountThenSignupAndLogin()
    {
        driver.findElement(By.xpath("//*[@id=\"MenuContent\"]/a[2]")).click();
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
                driver.findElement(By.xpath("//*[@id=\"MenuContent\"]/a[2]")).click();
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

    // Find an animal that is in stock to prepare for the test
    // Check if animal stock quantity in-stock is greater than 0
    @Test
    @Order(3)
//    @SpiraTestCase(testCaseId = 7318)
    public void testFindAnimalWithQuantityInStock()
    {
        // Used to break loop in time
        boolean inStock = false;

        // Loop through the list and visit each link
        quickLinks: for (int i=1; i <= driver.findElement(By.id("QuickLinks")).findElements(By.tagName("a")).size(); ++i) {
            WebElement a = driver.findElement(By.xpath("//div[@id='QuickLinks']//a["+i+"]"));
            String href = a.getAttribute("href");
            driver.navigate().to(href);
            WebElement table = driver.findElement(By.tagName("table"));
            List<WebElement> animalTypeLinkElements = table.findElements(By.tagName("a"));

            for (WebElement animalTypeLink : animalTypeLinkElements) {
                String animalTypeHref = animalTypeLink.getAttribute("href");

                driver.navigate().to(animalTypeHref);
                List<WebElement> animalLinkElements = driver.findElements(By.xpath("//table/tbody/tr/td[1]/a[not(@class='Button')]"));

                for (WebElement animalLink : animalLinkElements) {
                    String animalHref = animalLink.getAttribute("href");
                    System.out.println("Searching item: " + animalHref);

                    driver.navigate().to(animalHref);
                    animalBoughtHrefLink = animalHref;
                    WebElement stockInfoElement = driver.findElement(By.xpath("//table/tbody/tr[5]/td"));
                    String stockInfo = stockInfoElement.getText();

                    if (stockInfo.contains("in stock")) {
                        System.out.println("Item is in stock.");
                        stockQTY = Integer.parseInt(stockInfo.replace(" in stock.", "").trim());
                        inStock = true;
                    } else if (stockInfo.contains("Back ordered")) {
                        System.out.println("Item is not in stock (Back ordered).");
                    } else {
                        System.out.println("Stock status is unknown.");
                    }

                    if (inStock) break quickLinks;
                    driver.navigate().back();
                }
                driver.navigate().back();
            }
            driver.navigate().back();
        }

        assertTrue(stockQTY > 0);
    }


    // Place order and check if order was placed successfully
    @Test
    @Order(4)
//    @SpiraTestCase(testCaseId = 7319)
    public void testOrderPlacedSuccessfully()
    {
        // Find the <td> element that contains stock information
        driver.findElement(By.xpath("//a[normalize-space()='Add to Cart']")).click();
        driver.findElement(By.xpath("//a[normalize-space()='Proceed to Checkout']")).click();
        driver.findElement(By.name("newOrder")).click();
        driver.findElement(By.xpath("//a[normalize-space()='Confirm']")).click();

        WebElement elem = null;
        try {
            elem = driver.findElement(By.xpath("//*[@id=\"Content\"]/ul/li"));
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        if (elem != null) {
            assertEquals(elem.getText(),"Thank you, your order has been submitted.");
        }
    }


    // Check if the test was a success
    @Test
    @Order(5)
//    @SpiraTestCase(testCaseId = 7320)
    public void testStockQuantityUpdatesSuccessfully()
    {
        driver.navigate().to(animalBoughtHrefLink);
        WebElement stockInfoElement = driver.findElement(By.xpath("//table/tbody/tr[5]/td"));
        String stockInfo = stockInfoElement.getText();
        int updatedStock = 0;

        if (stockInfo.contains("in stock.")) {
            updatedStock = Integer.parseInt(stockInfo.replace(" in stock.", "").trim());
        }

        System.out.println("Previous stock quantity: " + stockQTY);
        System.out.println("Updated stock quantity: " + updatedStock);

        assertTrue(updatedStock < stockQTY);
    }



    @AfterAll
    // Closing the browser
    public static void CloseBrowser()
    {
        driver.close();
    }
}
