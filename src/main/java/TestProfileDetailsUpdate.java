import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Objects;


public class TestProfileDetailsUpdate {

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
        boolean signup = false;
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

            LogIn();
        }

        // 4. Navigate to My Account page
        driver.findElement(By.xpath("//*[@id=\"MenuContent\"]/a[3]")).click();


        // 5. Change the used details

        // 5.1. Change FirstName and LastName fields
        fillField(driver.findElement(By.name("account.firstName")), "John");
        fillField(driver.findElement(By.name("account.lastName")), "Doe");

        // 5.2. Click on "Save Account Information" button
        driver.findElement(By.name("editAccount")).click();

        // 6. Logout to unset cookies 
        driver.findElement(By.xpath("//*[@id=\"MenuContent\"]/a[2]")).click();

        // 7. Log back in
        driver.findElement(By.xpath("//*[@id=\"MenuContent\"]/a[2]")).click();
        LogIn();

        // 8. Navigate to My Account page
        driver.findElement(By.xpath("//*[@id=\"MenuContent\"]/a[3]")).click();
        
        // 9. Get the values stored in the columns that were updated
        String firstName = driver.findElement(By.name("account.firstName")).getAttribute("value");
        String lastName = driver.findElement(By.name("account.lastName")).getAttribute("value");

        // 10. Print to show results
        if (Objects.equals(firstName, "John")) {
            System.out.println("Successfully matching updated First Name");
        } else {
            System.out.println("Un-matching First Name - " + firstName + "(Correct - John)");
        }

        if (Objects.equals(lastName, "Doe")) {
            System.out.println("Successfully matching updated Last Name");
        } else {
            System.out.println("Un-matching Last Name - " + lastName + "(Correct - Doe)");
        }

        // 11. Close the browser window
        driver.close();
    }
}
