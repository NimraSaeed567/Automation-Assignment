package Base;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class BaseTests {
    protected WebDriver driver;
    private static List<Credentials.User> users; // Declare the users variable here
    private static HtmlReport report;

    @BeforeAll
    public static void loadCredentials() throws IOException {
        // Load the credentials from the JSON file
        ObjectMapper objectMapper = new ObjectMapper();
        Credentials credentials = objectMapper.readValue(new File("src/test/java/Base/credentials.json"), Credentials.class);
        users = credentials.getUsers();

        // Initialize HtmlReport
        report = new HtmlReport("testReport.html");
    }

    @BeforeEach
    public void setUp() {
        try {
            URL gridUrl = new URL("http://172.16.2.42:4444"); // Replace with your Selenium Grid Hub URL

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setBrowserName("firefox");

            driver = new RemoteWebDriver(gridUrl, capabilities);

            // Set implicit wait
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            // Maximize the window
            driver.manage().window().maximize();

            // Open the target website with a timeout
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            driver.get("https://www.saucedemo.com/");
            wait.until(ExpectedConditions.titleIs("Swag Labs"));
            report.log("Setup", "PASS", "Setup completed successfully");
            System.out.println(driver.getTitle());
        } catch (Exception e) {
            try {
                report.log("Setup", "FAIL", "Setup failed: " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        try {
            report.log("Teardown", "PASS", "Teardown completed successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void endReport() throws IOException {
        report.close();
    }

    @Test
    @Tag("login")
  //  1. Successful Sign In - Automate the login process using valid credentials.
    public void testLogin() {
        if (users == null || users.isEmpty()) { // Check if users is null or empty
            System.out.println("No users loaded from the credentials file.");
            try {
                report.log("Test Login", "FAIL", "No users loaded from the credentials file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
            WebElement passwordField = driver.findElement(By.id("password"));
            WebElement loginButton = driver.findElement(By.id("login-button"));
            // Read all usernames and passwords
            usernameField.sendKeys(users.get(0).getUsername());
            passwordField.sendKeys(users.get(0).getPassword());
            loginButton.click();
            report.log("Test Login", "PASS", "Login successful");
        } catch (TimeoutException e) {
            try {
                report.log("Test Login", "FAIL", "Element not found within the wait time: " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        } catch (Exception e) {
            try {
                report.log("Test Login", "FAIL", e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }

        // Add some verification or logout if needed
        driver.get("https://www.saucedemo.com/"); // reload the login page
    }

    @Test
    @Tag("login")
    @Tag("Productpage")
    // 2. Add Items to Cart and Remove Them from the Products Page - Add items to the cart from the products page and then remove them. Verify the cart icon and that the “Add to Cart” option returns for the product tile

    public void testcart() {
        if (users == null || users.isEmpty()) { // Check if users is null or empty
            System.out.println("No users loaded from the credentials file.");
            try {
                report.log("Test Cart", "FAIL", "No users loaded from the credentials file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
            WebElement passwordField = driver.findElement(By.id("password"));
            WebElement loginButton = driver.findElement(By.id("login-button"));
            // Read 0 username and password
            usernameField.sendKeys(users.get(0).getUsername());
            passwordField.sendKeys(users.get(0).getPassword());
            loginButton.click();
            // Add items to cart
            WebElement Product1 = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack")));
            Product1.click();
            WebElement Product2 = driver.findElement(By.id("add-to-cart-sauce-labs-bike-light"));
            Product2.click();
            // Navigate to cart page
            WebElement cartIcon = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/div[1]/div[3]/a"));
            cartIcon.click();
            System.out.println("cart page1");
            // Remove from cart page
            WebElement removeProduct1 = driver.findElement(By.id("remove-sauce-labs-backpack"));
            WebElement removeProduct2 = driver.findElement(By.id("remove-sauce-labs-bike-light"));

            removeProduct1.click();
            removeProduct2.click();
            System.out.println("Items Removed");
            // Verify all items are removed
            boolean isCartBadgePresent = driver.findElements(By.className("shopping_cart_badge")).size() == 0;
            // Assert that badge is not present means cart is empty
            assertEquals(true, isCartBadgePresent, "Cart badge should not be present indicating the cart is empty");
            System.out.println("Cart badge is empty");
            report.log("Test Cart", "PASS", "Cart test passed");
        } catch (TimeoutException e) {
            try {
                report.log("Test Cart", "FAIL", "Element not found within the wait time: " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        } catch (Exception e) {
            try {
                report.log("Test Cart", "FAIL", e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @Test
    @Tag("login")
    @Tag("checkoutpage")
       //3. Add Items to Cart and Remove Them from the Checkout Page:- Add items to the cart, proceed to the checkout page, and remove them from the cart on the checkout page. Verify that it is removed from the cart successfully
    public void testcart2() {
        if (users == null || users.isEmpty()) { // Check if users is null or empty
            System.out.println("No users loaded from the credentials file.");
            try {
                report.log("Test Cart 2", "FAIL", "No users loaded from the credentials file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"user-name\"]")));
            WebElement passwordField = driver.findElement(By.xpath("//*[@id=\"password\"]"));
            WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"login-button\"]"));
            // Read from json file
            usernameField.sendKeys(users.get(0).getUsername());
            passwordField.sendKeys(users.get(0).getPassword());
            loginButton.click();
            System.out.println("Read Successfully");
            report.log("Test Cart 2", "PASS", "Login successful in test cart 2");
            // Add items to cart
            WebElement Product1 = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack")));
            Product1.click();
            WebElement Product2 = driver.findElement(By.id("add-to-cart-sauce-labs-bike-light"));
            Product2.click();
            System.out.println("Items added in testcart2");
            report.log("Test Cart 2", "PASS", "Items added to cart in test cart 2");
            // Navigate to cart page
            WebElement cartIcon = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/div[1]/div[3]/a"));
            cartIcon.click();
            System.out.println("cart page2");
            // remove items from checkout page
            WebElement removeProduct1 = driver.findElement(By.id("remove-sauce-labs-backpack"));
            WebElement removeProduct2 = driver.findElement(By.id("remove-sauce-labs-bike-light"));

            removeProduct1.click();
            removeProduct2.click();
            System.out.println("Items removed from cart2");
            report.log("Test Cart 2", "PASS", "Items removed from cart in test cart 2");
            // verify items are removed
            boolean isCartBadgePresent = driver.findElements(By.className("shopping_cart_badge")).isEmpty();
            assertEquals(true, isCartBadgePresent, "Cart badge should not be present indicating the cart is empty");
            System.out.println("verification");
            report.log("Test Cart 2", "PASS", "Cart test 2 passed");
        } catch (TimeoutException e) {
            try {
                report.log("Test Cart 2", "FAIL", "Element not found within the wait time: " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        } catch (Exception e) {
            try {
                report.log("Test Cart 2", "FAIL", e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @Test
    @Tag("login")
    @Tag("productdetailpage")

    // 4. Add Items to Cart and Remove Them from the Product Details Page:- Add items to the cart from the product details page ( By clicking on the product)  and then remove them. Verify that the cart icon and that the “Add to Cart” option returns
    public void testcart3() {
        if (users == null || users.isEmpty()) { // Check if users is null or empty
            System.out.println("No users loaded from the credentials file.");
            try {
                report.log("Test Cart 3", "FAIL", "No users loaded from the credentials file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            // Step 1: Log in to the application
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
            WebElement passwordField = driver.findElement(By.id("password"));
            WebElement loginButton = driver.findElement(By.id("login-button"));
            // Read from json file
            usernameField.sendKeys(users.get(0).getUsername());
            passwordField.sendKeys(users.get(0).getPassword());
            loginButton.click();

            // Step 2: Add items to the cart from the product details page
            // Navigate to the product details page for 'Sauce Labs Backpack'
            WebElement product1Link = driver.findElement(By.xpath("//div[text()='Sauce Labs Backpack']")); // Adjust this selector as needed
            product1Link.click();
            System.out.println("productdetailspage");

            // Add 'Sauce Labs Backpack' to the cart
            WebElement addToCartButton1 = driver.findElement(By.id("add-to-cart"));
            addToCartButton1.click();
            System.out.println("Backpackadded");
            // Go back to the product listing page
            WebElement backButton = driver.findElement(By.className("inventory_details_back_button"));
            backButton.click();
            System.out.println("Productlistingpage");
            // Navigate to the product details page for 'Sauce Labs Bike Light'
            WebElement product2Link = driver.findElement(By.xpath("//div[text()='Sauce Labs Bike Light']")); // Adjust this selector as needed
            product2Link.click();
            System.out.println("productdetailspage");
            // Add 'Sauce Labs Bike Light' to the cart
            WebElement addToCartButton2 = driver.findElement(By.id("add-to-cart"));
            addToCartButton2.click();
            System.out.println("Bike light added");
            // Go back to the product listing page
            backButton = driver.findElement(By.className("inventory_details_back_button"));
            backButton.click();
            System.out.println("productlistingpage");
            // Step 3: Navigate to the cart page
            WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
            cartIcon.click();
            System.out.println("cart page");
            // Step 4: Remove items from the cart
            WebElement removeProduct1 = driver.findElement(By.id("remove-sauce-labs-backpack"));
            WebElement removeProduct2 = driver.findElement(By.id("remove-sauce-labs-bike-light"));

            removeProduct1.click();
            removeProduct2.click();
            System.out.println("itemsremoved");
            // Step 5: Verify items have been removed
            boolean isCartBadgePresent = driver.findElements(By.className("shopping_cart_badge")).isEmpty();
            if (!isCartBadgePresent) {
                WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
                assertEquals("", cartBadge.getText(), "Cart badge should be empty indicating the cart is empty.");
            } else {
                assertTrue(isCartBadgePresent, "Cart badge should not be present indicating the cart is empty.");
            }
            System.out.println("Verification");
            report.log("Test Cart 3", "PASS", "Cart test 3 passed");
        } catch (TimeoutException e) {
            try {
                report.log("Test Cart 3", "FAIL", "Element not found within the wait time: " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        } catch (Exception e) {
            try {
                report.log("Test Cart 3", "FAIL", e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @Test
    @Tag("login")
    @Tag("Purchase")
     // 5. Buy Items:- Add items to the cart, proceed to checkout, and complete the purchase.
    public void testcart4() {
        if (users == null || users.isEmpty()) { // Check if users is null or empty
            System.out.println("No users loaded from the credentials file.");
            try {
                report.log("Test Cart 4", "FAIL", "No users loaded from the credentials file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            // Step 1: Log in to the application
            WebElement usernameField = driver.findElement(By.id("user-name"));
            WebElement passwordField = driver.findElement(By.id("password"));
            WebElement loginButton = driver.findElement(By.id("login-button"));

            //read from json file
            usernameField.sendKeys(users.get(0).getUsername());
            passwordField.sendKeys(users.get(0).getPassword());
            loginButton.click();

            // Step 2: Add items to the cart
            WebElement Product1 = driver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
            Product1.click();
            WebElement Product2 = driver.findElement(By.id("add-to-cart-sauce-labs-bike-light"));
            Product2.click();
            // Step 3: Navigate to the cart page
            WebElement cartIcon = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/div[1]/div[3]/a"));
            cartIcon.click();
            System.out.println("cart clicked");
            // Step 4: Proceed to checkout
            WebElement checkoutButton = driver.findElement(By.xpath("//*[@id=\"checkout\"]"));
            checkoutButton.click();
            System.out.println("cart clicked2");
            // Step 5: Fill in checkout information
            WebElement firstNameField = driver.findElement(By.id("first-name"));
            WebElement lastNameField = driver.findElement(By.id("last-name"));
            WebElement postalCodeField = driver.findElement(By.id("postal-code"));

            firstNameField.sendKeys("Nimra");
            lastNameField.sendKeys("Saeed");
            postalCodeField.sendKeys("3000");

            WebElement continueButton = driver.findElement(By.id("continue"));
            continueButton.click();
            System.out.println("Correct Information");
            // Step 6: Finish the purchase
            WebElement finishButton = driver.findElement(By.id("finish"));
            finishButton.click();
            System.out.println("Purchase Finish");
            // Step 7: Verify purchase completion
            WebElement completeHeader = driver.findElement(By.className("complete-header"));
            assertEquals("Thank you for your order!", completeHeader.getText(), "Purchase was not completed successfully.");
            report.log("Test Cart 4", "PASS", "Purchase completed successfully.");
        } catch (TimeoutException e) {
            try {
                report.log("Test Cart 4", "FAIL", "Element not found within the wait time: " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        } catch (Exception e) {
            try {
                report.log("Test Cart 4", "FAIL", e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @Test
    @Tag("login")
    @Tag("logout")
      // 6. Add Items to Cart, Logout, and Login Again to Verify Cart Persistence:- Add items to the cart, log out, log back in, and verify that the cart retains the items.


    public void testcart5() {
        if (users == null || users.isEmpty()) { // Check if users is null or empty
            System.out.println("No users loaded from the credentials file.");
            try {
                report.log("Test Cart 5", "FAIL", "No users loaded from the credentials file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            // Step 1: Log in to the application
            WebElement usernameField = driver.findElement(By.id("user-name"));
            WebElement passwordField = driver.findElement(By.id("password"));
            WebElement loginButton = driver.findElement(By.id("login-button"));

            //read from json file
            usernameField.sendKeys(users.get(0).getUsername());
            passwordField.sendKeys(users.get(0).getPassword());
            loginButton.click();

            // Step 2: Add items to the cart
            WebElement Product1 = driver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
            Product1.click();
            WebElement Product2 = driver.findElement(By.id("add-to-cart-sauce-labs-bike-light"));
            Product2.click();
            // Step 3: Log out
            WebElement menuButton = driver.findElement(By.id("react-burger-menu-btn"));
            menuButton.click();
            WebElement logoutLink = driver.findElement(By.id("logout_sidebar_link"));
            logoutLink.click();
            System.out.println("logged out");
            // Step 4: Log back in
            usernameField = driver.findElement(By.id("user-name"));
            passwordField = driver.findElement(By.id("password"));
            loginButton = driver.findElement(By.id("login-button"));

            usernameField.sendKeys("standard_user");
            passwordField.sendKeys("secret_sauce");
            loginButton.click();
            System.out.println("logged in");
            // Step 5: Verify items are still in the cart
            WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
            cartIcon.click();

            WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
            assertEquals("2", cartBadge.getText(), "Cart badge should show 2 items indicating the cart retains the items after logging out and back in.");

            WebElement cartItem1 = driver.findElement(By.xpath("//div[text()='Sauce Labs Backpack']"));
            WebElement cartItem2 = driver.findElement(By.xpath("//div[text()='Sauce Labs Bike Light']"));

            assertTrue(cartItem1.isDisplayed(), "Sauce Labs Backpack should still be in the cart after logging back in.");
            assertTrue(cartItem2.isDisplayed(), "Sauce Labs Bike Light should still be in the cart after logging back in.");

            System.out.println("Items persisted in the cart after logging out and back in.");
            report.log("Test Cart 5", "PASS", "Cart test 5 passed");
        } catch (TimeoutException e) {
            try {
                report.log("Test Cart 5", "FAIL", "Element not found within the wait time: " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        } catch (Exception e) {
            try {
                report.log("Test Cart 5", "FAIL", e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @Test
    @Tag("login")
    @Tag("Sorting")
     // 7. Verify All Sorting Options on Products Page:- Verify the functionality of all sorting options (e.g., price, name) on the products page.
    public void testcart6() {
        if (users == null || users.isEmpty()) { // Check if users is null or empty
            System.out.println("No users loaded from the credentials file.");
            try {
                report.log("Test Cart 6", "FAIL", "No users loaded from the credentials file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            // Step 1: Log in to the application
            WebElement usernameField = driver.findElement(By.id("user-name"));
            WebElement passwordField = driver.findElement(By.id("password"));
            WebElement loginButton = driver.findElement(By.id("login-button"));

            //read from json file
            usernameField.sendKeys(users.get(0).getUsername());
            passwordField.sendKeys(users.get(0).getPassword());
            loginButton.click();

            // Step 2: Verify sorting by Name (A to Z)
            Select sortDropdown = new Select(driver.findElement(By.className("product_sort_container")));
            sortDropdown.selectByVisibleText("Name (A to Z)");
            List<WebElement> productNames = driver.findElements(By.className("inventory_item_name"));
            List<String> actualProductNames = new ArrayList<>();
            for (WebElement productName : productNames) {
                actualProductNames.add(productName.getText());
            }
            List<String> expectedProductNames = new ArrayList<>(actualProductNames);
            Collections.sort(expectedProductNames);
            assertEquals(expectedProductNames, actualProductNames, "Products are not sorted by Name (A to Z)");
            System.out.println("A-Z arrangement");

            // Step 3: Verify sorting by Name (Z to A)
            sortDropdown.selectByVisibleText("Name (Z to A)");
            productNames = driver.findElements(By.className("inventory_item_name"));
            actualProductNames = new ArrayList<>();
            for (WebElement productName : productNames) {
                actualProductNames.add(productName.getText());
            }
            expectedProductNames = new ArrayList<>(actualProductNames);
            expectedProductNames.sort(Collections.reverseOrder());
            assertEquals(expectedProductNames, actualProductNames, "Products are not sorted by Name (Z to A)");
            System.out.println("Z-A arrangement");

            // Step 4: Verify sorting by Price (low to high)
            sortDropdown.selectByVisibleText("Price (low to high)");
            List<WebElement> productPrices = driver.findElements(By.className("inventory_item_price"));
            List<Double> actualProductPrices = new ArrayList<>();
            for (WebElement productPrice : productPrices) {
                actualProductPrices.add(Double.parseDouble(productPrice.getText().replace("$", "")));
            }
            List<Double> expectedProductPrices = new ArrayList<>(actualProductPrices);
            Collections.sort(expectedProductPrices);
            assertEquals(expectedProductPrices, actualProductPrices, "Products are not sorted by Price (low to high)");
            System.out.println("Price low to high arrangement");

            // Step 5: Verify sorting by Price (high to low)
            sortDropdown.selectByVisibleText("Price (high to low)");
            productPrices = driver.findElements(By.className("inventory_item_price"));
            actualProductPrices = new ArrayList<>();
            for (WebElement productPrice : productPrices) {
                actualProductPrices.add(Double.parseDouble(productPrice.getText().replace("$", "")));
            }
            List<Double> expectedProductPricesHighToLow = new ArrayList<>(actualProductPrices);
            expectedProductPricesHighToLow.sort(Collections.reverseOrder());
            assertEquals(expectedProductPricesHighToLow, actualProductPrices, "Products are not sorted by Price (high to low)");
            System.out.println("Price high to low arrangement");

            report.log("Test Cart 6", "PASS", "Cart test 6 passed");
        } catch (TimeoutException e) {
            try {
                report.log("Test Cart 6", "FAIL", "Element not found within the wait time: " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        } catch (Exception e) {
            try {
                report.log("Test Cart 6", "FAIL", e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
