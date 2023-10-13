import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;


public class TestStockQuantityAndOrderPlacement {
    private static ChromeDriver driver;

    // Helper function to clear the field, then fill in the required text
    public static void fillField(WebElement element, String input) {
        element.clear();
        element.sendKeys(input);
    }

    // Helper function to log in into the website
    public static void LogIn()
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


        // 2. Locate the sign-in link and Click
        driver.findElement(By.xpath("//*[@id=\"MenuContent\"]/a[2]")).click();


        // 3. Try logging in into the website with given credentials

        // 3.1. Use LogIn helper function to fill the fields and click on the button
        LogIn();

        // 3.2. Verify the LogIn
        WebElement element = null;
        try {
            element = driver.findElement(By.xpath("//*[@id=\"Content\"]/ul/li"));
        } catch (Exception e) {
            // Handle the case when the element is not found
        }

        // 3.3. If log in failed then register new user with credentials and then try log in again
        if (element != null && element.getText().equals("Invalid username or password. Signon failed.")) {
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

            // Find the elements and fill the form
            driver.findElement(By.name("account.languagePreference")).sendKeys("english");
            driver.findElement(By.name("account.favouriteCategoryId")).sendKeys("DOGS");
            driver.findElement(By.name("account.listOption")).click();
            driver.findElement(By.name("account.bannerOption")).click();

            driver.findElement(By.name("newAccount")).click();

            driver.findElement(By.xpath("//*[@id=\"MenuContent\"]/a[2]")).click();
            LogIn();
        }

        // 4. Search for an element that is in stock

        // 4.1. Define variables
        // Boolean to determine if any item is in stock
        boolean inStock = false;
        // Initial stock quantity before placing order
        int stockQTY = 0;
        // href link to the item that was ordered
        String animalBoughtHrefLink = null;

        // 4.2. Loop through all Animal Categories one by one to search if stock is available
        quickLinks: for (int i=1; i <= driver.findElement(By.id("QuickLinks")).findElements(By.tagName("a")).size(); ++i) {
            WebElement a = driver.findElement(By.xpath("//div[@id='QuickLinks']//a["+i+"]"));
            String href = a.getAttribute("href");
            // Navigate to high level animal link
            driver.navigate().to(href);

            // Extract links from the table
            WebElement table = driver.findElement(By.tagName("table"));
            List<WebElement> animalTypeLinkElements = table.findElements(By.tagName("a"));

            // Loop through links to animal type level
            for (WebElement animalTypeLink : animalTypeLinkElements) {
                String animalTypeHref = animalTypeLink.getAttribute("href");
                // Navigate to animal type link
                driver.navigate().to(animalTypeHref);

                // Extract links from the table
                List<WebElement> animalLinkElements = driver.findElements(By.xpath("//table/tbody/tr/td[1]/a[not(@class='Button')]"));

                // Loop through links to specific animal
                for (WebElement animalLink : animalLinkElements) {
                    String animalHref = animalLink.getAttribute("href");
                    // Navigate to specific animal
                    driver.navigate().to(animalHref);
                    System.out.println("Searching item: " + animalHref);

                    // Update animalBoughtHrefLink
                    animalBoughtHrefLink = animalHref;

                    // Collect stock information
                    WebElement stockInfoElement = driver.findElement(By.xpath("//table/tbody/tr[5]/td"));
                    String stockInfo = stockInfoElement.getText();

                    if (stockInfo.contains("in stock.")) {
                        System.out.println("Item is in stock.");
                        stockQTY = Integer.parseInt(stockInfo.replace(" in stock.", "").trim());
                        inStock = true;
                    } else if (stockInfo.contains("Back ordered")) {
                        System.out.println("Item is not in stock (Back ordered).");
                    } else {
                        System.out.println("Stock status is unknown.");
                    }

                    if (inStock) break quickLinks;
                    // Navigate back to the original page
                    driver.navigate().back();
                }
                // Navigate back to the original page
                driver.navigate().back();
            }
            // Navigate back to the original page
            driver.navigate().back();
        }


        // 5. Place order
        driver.findElement(By.xpath("//a[normalize-space()='Add to Cart']")).click();
        driver.findElement(By.xpath("//a[normalize-space()='Proceed to Checkout']")).click();
        driver.findElement(By.name("newOrder")).click();
        driver.findElement(By.xpath("//a[normalize-space()='Confirm']")).click();


        // 6. Check if order was placed successfully
        WebElement elem = null;
        try {
            elem = driver.findElement(By.xpath("//*[@id=\"Content\"]/ul/li"));
        } catch (Exception e) {
            // Handle the case when the element is not found
        }

        // 7. Check if the element exists, then confirm order status and print result
        if (elem != null && elem.getText().equals("Thank you, your order has been submitted.")) {
            System.out.println(
                    "==========================\n"+
                    "Order Placed successfully!\n"+
                    "=========================="
            );
        }

        // 8. Navigate back to the link of animal whose order was placed
        driver.navigate().to(animalBoughtHrefLink);
        WebElement stockInfoElement = driver.findElement(By.xpath("//table/tbody/tr[5]/td"));

        // 9. Fetch the updated stock quantity again
        String stockInfo = stockInfoElement.getText();
        int updatedStock = 0;

        // Check if the text indicates the item is in stock or not
        if (stockInfo.contains("in stock.")) {
            updatedStock = Integer.parseInt(stockInfo.replace(" in stock.", "").trim());
        }

        // 10. Print previos and current stock quantity
        System.out.println("Previous stock quantity: " + stockQTY);
        System.out.println("Updated stock quantity: " + updatedStock);

        // 11. Print results
        if (stockQTY-1 == updatedStock) {
            System.out.println("Stock Updated successfully!");
        } else {
            System.out.println("Stock Update failed!");
        }

        driver.close();
    }
}
