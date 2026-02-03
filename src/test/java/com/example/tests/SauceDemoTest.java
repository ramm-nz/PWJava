package com.example.tests;

import com.example.base.BaseTest;
import com.example.config.TestConfig;
import com.example.listeners.TestListener;
import com.example.pages.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Execution(ExecutionMode.CONCURRENT) // Enable parallel execution
@ExtendWith(TestListener.class)
public class SauceDemoTest extends BaseTest {
    
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    
    @BeforeEach
    void initializePages() {
        loginPage = new LoginPage(page);
        productsPage = new ProductsPage(page);
        cartPage = new CartPage(page);
        checkoutPage = new CheckoutPage(page);
    }

    @Test
    @Order(1)
    @DisplayName("Test successful login with standard user")
    void testSuccessfulLogin() {
        loginPage.navigate();
        assertTrue(loginPage.isLoginButtonVisible(), "Login button should be visible");
        
        loginPage.login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        assertTrue(productsPage.isDisplayed(), "Products list should be visible after login");
        assertEquals("Products", productsPage.getPageTitle(), "Page title should be 'Products'");
        assertEquals(TestConfig.BASE_URL + "inventory.html", page.url(), "URL should be inventory page");
    }

    @Test
    @Order(2)
    @DisplayName("Test login with invalid credentials")
    void testInvalidLogin() {
        loginPage.navigate();
        loginPage.login("invalid_user", "wrong_password");
        
        assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        String errorText = loginPage.getErrorMessage();
        assertThat(errorText).contains("Username and password do not match");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test login with locked out user")
    void testLockedOutUser() {
        loginPage.navigate();
        loginPage.login(TestConfig.LOCKED_OUT_USER, TestConfig.PASSWORD);
        
        assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        String errorText = loginPage.getErrorMessage();
        assertThat(errorText).contains("Sorry, this user has been locked out");
    }

    @Test
    @Order(4)
    @DisplayName("Test adding product to cart")
    void testAddProductToCart() {
        loginPage.navigate();
        loginPage.login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        productsPage.addProductToCart("sauce-labs-backpack");
        
        String cartBadge = productsPage.getCartItemCount();
        assertEquals("1", cartBadge, "Cart should show 1 item");
        
        productsPage.goToCart();
        assertTrue(cartPage.isCartItemDisplayed(), "Cart should contain an item");
        assertEquals(1, cartPage.getCartItemCount(), "Cart should have exactly 1 item");
    }

    @Test
    @Order(5)
    @DisplayName("Test adding multiple products to cart")
    void testAddMultipleProductsToCart() {
        loginPage.navigate();
        loginPage.login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        productsPage.addProductToCart("sauce-labs-backpack");
        productsPage.addProductToCart("sauce-labs-bike-light");
        productsPage.addProductToCart("sauce-labs-bolt-t-shirt");
        
        String cartBadge = productsPage.getCartItemCount();
        assertEquals("3", cartBadge, "Cart should show 3 items");
        
        productsPage.goToCart();
        assertEquals(3, cartPage.getCartItemCount(), "Cart should have exactly 3 items");
    }

    @Test
    @Order(6)
    @DisplayName("Test complete checkout flow")
    void testCompleteCheckoutFlow() {
        loginPage.navigate();
        loginPage.login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        productsPage.addProductToCart("sauce-labs-backpack");
        productsPage.goToCart();
        
        assertTrue(cartPage.isCartItemDisplayed(), "Cart should have items");
        cartPage.proceedToCheckout();
        
        checkoutPage.fillCheckoutInformation("John", "Doe", "12345");
        checkoutPage.clickContinue();
        
        assertTrue(checkoutPage.isSummaryInfoDisplayed(), "Summary info should be visible");
        checkoutPage.clickFinish();
        
        assertTrue(checkoutPage.isOrderComplete(), "Order should be complete");
        assertEquals("Thank you for your order!", checkoutPage.getCompleteHeader());
        assertThat(checkoutPage.getCompleteText()).contains("Your order has been dispatched");
    }

    @Test
    @Order(7)
    @DisplayName("Test removing product from cart")
    void testRemoveProductFromCart() {
        loginPage.navigate();
        loginPage.login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        productsPage.addProductToCart("sauce-labs-backpack");
        productsPage.goToCart();
        
        assertEquals(1, cartPage.getCartItemCount(), "Cart should have 1 item");
        
        cartPage.removeItem(0);
        
        assertFalse(cartPage.isCartItemDisplayed(), "Cart should be empty after removal");
        assertEquals("0", productsPage.getCartItemCount(), "Cart badge should not be visible");
    }

    @Test
    @Order(8)
    @DisplayName("Test sorting products by price (low to high)")
    void testProductSortingLowToHigh() {
        loginPage.navigate();
        loginPage.login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        productsPage.sortProducts("lohi");
        
        String firstPrice = productsPage.getFirstProductPrice();
        assertNotNull(firstPrice, "First product price should be visible");
        assertTrue(firstPrice.startsWith("$"), "Price should start with $");
    }
    
    @Test
    @Order(9)
    @DisplayName("Test sorting products by name (A to Z)")
    void testProductSortingNameAtoZ() {
        loginPage.navigate();
        loginPage.login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        productsPage.sortProducts("az");
        
        int productCount = productsPage.getProductCount();
        assertTrue(productCount > 0, "Should have products on the page");
    }

    @Test
    @Order(10)
    @DisplayName("Test logout functionality")
    void testLogout() {
        loginPage.navigate();
        loginPage.login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        assertTrue(productsPage.isDisplayed(), "Should be on products page");
        
        productsPage.logout();
        
        assertTrue(loginPage.isLoginButtonVisible(), "Should be redirected to login page");
        assertEquals(TestConfig.BASE_URL, page.url(), "URL should be login page");
    }
    
    @Test
    @Order(11)
    @DisplayName("Test continue shopping from cart")
    void testContinueShoppingFromCart() {
        loginPage.navigate();
        loginPage.login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        productsPage.goToCart();
        cartPage.continueShopping();
        
        assertTrue(productsPage.isDisplayed(), "Should return to products page");
        assertEquals("Products", productsPage.getPageTitle());
    }
    
    @Test
    @Order(12)
    @DisplayName("Test checkout with missing information")
    void testCheckoutWithMissingInformation() {
        loginPage.navigate();
        loginPage.login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        productsPage.addProductToCart("sauce-labs-backpack");
        productsPage.goToCart();
        cartPage.proceedToCheckout();
        
        // Try to continue without filling information
        checkoutPage.clickContinue();
        
        // Should show error (page should still be on checkout step one)
        assertTrue(page.isVisible("[data-test='error']"), "Error message should be displayed");
    }
}
