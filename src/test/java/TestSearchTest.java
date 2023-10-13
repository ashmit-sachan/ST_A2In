import com.inflectra.spiratest.addons.junitextension.SpiraTestCase;
import com.inflectra.spiratest.addons.junitextension.SpiraTestConfiguration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
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
public class TestSearchTest {
    private static ChromeDriver driver;

    // Create a list to store the names
    private static List<String> names = new ArrayList<>();

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


    // Check if search parameteres were extracted successfully
    @Test
    @Order(2)
//    @SpiraTestCase(testCaseId = 7321)
    public void testCollectAllNamesToVerifySearchResults()
    {
        for (int i=1; i <= driver.findElement(By.id("QuickLinks")).findElements(By.tagName("a")).size(); ++i) {
            WebElement a = driver.findElement(By.xpath("//div[@id='QuickLinks']//a["+i+"]"));
            String href = a.getAttribute("href");
            driver.navigate().to(href);

            List<WebElement> nameElements = driver.findElements(By.xpath("//table/tbody/tr/td[2]"));

            for (WebElement nameElement : nameElements) {
                String name = nameElement.getText();
                names.add(name);
            }
            // Navigate back to the original page
            driver.navigate().back();
        }
        assertEquals(16, names.size());
    }


    // Verify the expected search results with actual search results
    @Test
    @Order(3)
//    @SpiraTestCase(testCaseId = 7322)
    public void testVerifyAllSearchResults()
    {
        List<String> searchStrings = new ArrayList<>();
        searchStrings.add("gold");
        searchStrings.add("fish");
        searchStrings.add("sh");
        searchStrings.add("ig");
        searchStrings.add("an");

        for (String substring : searchStrings) {
            List<String> expected = new ArrayList<>();
            List<String> results = new ArrayList<>();

            for (String name : names) {
                if (name.contains(substring)) {
                    expected.add(name);
                }
            }

            WebElement searchInput = driver.findElement(By.name("keyword"));

            WebElement searchButton = driver.findElement(By.name("searchProducts"));
            searchInput.clear();
            searchInput.sendKeys(substring);

            searchButton.click();

            List<WebElement> searchResults = driver.findElements(By.xpath("//table/tbody/tr/td[3]"));

            System.out.println("Expected Results: " + expected);

            for (WebElement result : searchResults) {
                String name = result.getText();
                expected.remove(name);
                results.add(name);
            }

            System.out.println("Search Results: " + results);
            assertTrue(expected.isEmpty());
        }
    }


    @AfterAll
    // Closing the browser
    public static void CloseBrowser()
    {
        driver.close();
    }
}

