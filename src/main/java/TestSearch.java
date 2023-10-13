import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;


public class TestSearch {
    private static ChromeDriver driver;

    public static void main(String[] args) {
        // Chromium executable path
        String chromiumPath = "C:\\Users\\ashmi\\AppData\\Local\\Chromium\\Application\\chrome.exe";

        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        // Configure ChromeOptions to specify the binary location (Chromium)
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBinary(chromiumPath);

        driver = new ChromeDriver(chromeOptions);

        // 1. Browse the JPetStore Demo page
        driver.get("https://petstore.octoperf.com/actions/Catalog.action");

        // 2. Fetch all the searchable parameters

        // 2.1. Create a list to store the names
        List<String> names = new ArrayList<>();

        // 2.2. Loop through the list and visit each link
        for (int i=1; i <= driver.findElement(By.id("QuickLinks")).findElements(By.tagName("a")).size(); ++i) {
            WebElement a = driver.findElement(By.xpath("//div[@id='QuickLinks']//a["+i+"]"));
            String href = a.getAttribute("href");
            driver.navigate().to(href);

            List<WebElement> nameElements = driver.findElements(By.xpath("//table/tbody/tr/td[2]"));

            // Loop through the list of name elements and extract the text
            for (WebElement nameElement : nameElements) {
                String name = nameElement.getText();
                names.add(name);
            }

            // Navigate back to the original page
            driver.navigate().back();
        }

        // 3. Define list with search parameters
        List<String> searchStrings = new ArrayList<>();
        searchStrings.add("gold");
        searchStrings.add("fish");
        searchStrings.add("sh");
        searchStrings.add("ig");
        searchStrings.add("an");

        // 4. Perform search operations to check search functionality

        // 4.1. Loop through search strings to test search multiple times
        for (String substring : searchStrings) {
            List<String> expected = new ArrayList<>();
            List<String> results = new ArrayList<>();

            // 4.2. Create a list of expected search results
            for (String name : names) {
                if (name.contains(substring)) {
                    expected.add(name);
                }
            }

            // 4.3. Perform Search operation on page
            WebElement searchInput = driver.findElement(By.name("keyword"));
            WebElement searchButton = driver.findElement(By.name("searchProducts"));
            searchInput.clear();
            searchInput.sendKeys(substring);

            // 4.4. Submit the search form
            searchButton.click();

            // 4.5. Locate and extract the search results
            List<WebElement> searchResults = driver.findElements(By.xpath("//table/tbody/tr/td[3]"));

            // 4.6. Check if the expected item name appears in the search results
            for (WebElement result : searchResults) {
                String name = result.getText();
                expected.remove(name);
                results.add(name);
            }

            System.out.println("Search Results: " + results);
        }

        driver.close();
    }
    }
