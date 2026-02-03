package com.example.tests;

import com.example.base.BaseTest;
import com.example.config.TestConfig;
import com.example.listeners.ScreenshotExtension;
import com.example.listeners.TestListener;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Sauce Demo Test Suite using PageFactory Pattern
 * Page objects are lazily initialized and cached through the factory
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith({TestListener.class, ScreenshotExtension.class})
public class SauceDemoTest extends BaseTest {
    
    // No need to declare page objects - using pageFactory from BaseTest
    // pageFactory provides: loginPage(), productsPage(), cartPage(), checkoutPage()

    @Test
    @Order(1)
    @DisplayName("Test successful login with standard user")
    void testSuccessfulLogin() {
        pageFactory.loginPage().navigate();
        assertTrue(pageFactory.loginPage().isLoginButtonVisible(), "Login button should be visible");
        
        pageFactory.loginPage().login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        assertTrue(pageFactory.productsPage().isDisplayed(), "Products list should be visible after login");
        assertEquals("Products", pageFactory.productsPage().getPageTitle(), "Page title should be 'Products'");
        assertEquals(TestConfig.BASE_URL + "inventory.html", page.url(), "URL should be inventory page");
    }

    @Test
    @Order(2)
    @DisplayName("Test login with invalid credentials")
    void testInvalidLogin() {
        pageFactory.loginPage().navigate();
        pageFactory.loginPage().login("invalid_user", "wrong_password");
        
        assertTrue(pageFactory.loginPage().isErrorMessageDisplayed(), "Error message should be displayed");
        String errorText = pageFactory.loginPage().getErrorMessage();
        assertThat(errorText).contains("Username and password do not match");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test login with locked out user")
    void testLockedOutUser() {
        pageFactory.loginPage().navigate();
        pageFactory.loginPage().login(TestConfig.LOCKED_OUT_USER, TestConfig.PASSWORD);
        
        assertTrue(pageFactory.loginPage().isErrorMessageDisplayed(), "Error message should be displayed");
        String errorText = pageFactory.loginPage().getErrorMessage();
        assertThat(errorText).contains("Sorry, this user has been locked out");
    }

    @Test
    @Order(4)
    @DisplayName("Test adding product to cart")
    void testAddProductToCart() {
        pageFactory.loginPage().navigate();
        pageFactory.loginPage().login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        pageFactory.productsPage().addProductToCart("sauce-labs-backpack");
        
        String cartBadge = pageFactory.productsPage().getCartItemCount();
        assertEquals("1", cartBadge, "Cart should show 1 item");
        
        pageFactory.productsPage().goToCart();
        assertTrue(pageFactory.cartPage().isCartItemDisplayed(), "Cart should contain an item");
        assertEquals(1, pageFactory.cartPage().getCartItemCount(), "Cart should have exactly 1 item");
    }

    @Test
    @Order(5)
    @DisplayName("Test adding multiple products to cart")
    void testAddMultipleProductsToCart() {
        pageFactory.loginPage().navigate();
        pageFactory.loginPage().login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        pageFactory.productsPage().addProductToCart("sauce-labs-backpack");
        pageFactory.productsPage().addProductToCart("sauce-labs-bike-light");
        pageFactory.productsPage().addProductToCart("sauce-labs-bolt-t-shirt");
        
        String cartBadge = pageFactory.productsPage().getCartItemCount();
        assertEquals("3", cartBadge, "Cart should show 3 items");
        
        pageFactory.productsPage().goToCart();
        assertEquals(3, pageFactory.cartPage().getCartItemCount(), "Cart should have exactly 3 items");
    }

    @Test
    @Order(6)
    @DisplayName("Test complete checkout flow")
    void testCompleteCheckoutFlow() {
        pageFactory.loginPage().navigate();
        pageFactory.loginPage().login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        pageFactory.productsPage().addProductToCart("sauce-labs-backpack");
        pageFactory.productsPage().goToCart();
        
        assertTrue(pageFactory.cartPage().isCartItemDisplayed(), "Cart should have items");
        pageFactory.cartPage().proceedToCheckout();
        
        pageFactory.checkoutPage().fillCheckoutInformation("John", "Doe", "12345");
        pageFactory.checkoutPage().clickContinue();
        
        assertTrue(pageFactory.checkoutPage().isSummaryInfoDisplayed(), "Summary info should be visible");
        pageFactory.checkoutPage().clickFinish();
        
        assertTrue(pageFactory.checkoutPage().isOrderComplete(), "Order should be complete");
        assertEquals("Thank you for your order!", pageFactory.checkoutPage().getCompleteHeader());
        assertThat(pageFactory.checkoutPage().getCompleteText()).contains("Your order has been dispatched");
    }

    @Test
    @Order(7)
    @DisplayName("Test removing product from cart")
    void testRemoveProductFromCart() {
        pageFactory.loginPage().navigate();
        pageFactory.loginPage().login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        pageFactory.productsPage().addProductToCart("sauce-labs-backpack");
        pageFactory.productsPage().goToCart();
        
        assertEquals(1, pageFactory.cartPage().getCartItemCount(), "Cart should have 1 item");
        
        pageFactory.cartPage().removeItem(0);
        
        assertFalse(pageFactory.cartPage().isCartItemDisplayed(), "Cart should be empty after removal");
        assertEquals("0", pageFactory.productsPage().getCartItemCount(), "Cart badge should not be visible");
    }

    @Test
    @Order(8)
    @DisplayName("Test sorting products by price (low to high)")
    void testProductSortingLowToHigh() {
        pageFactory.loginPage().navigate();
        pageFactory.loginPage().login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        pageFactory.productsPage().sortProducts("lohi");
        
        String firstPrice = pageFactory.productsPage().getFirstProductPrice();
        assertNotNull(firstPrice, "First product price should be visible");
        assertTrue(firstPrice.startsWith("$"), "Price should start with $");
    }
    
    @Test
    @Order(9)
    @DisplayName("Test sorting products by name (A to Z)")
    void testProductSortingNameAtoZ() {
        pageFactory.loginPage().navigate();
        pageFactory.loginPage().login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        pageFactory.productsPage().sortProducts("az");
        
        int productCount = pageFactory.productsPage().getProductCount();
        assertTrue(productCount > 0, "Should have products on the page");
    }

    @Test
    @Order(10)
    @DisplayName("Test logout functionality")
    void testLogout() {
        pageFactory.loginPage().navigate();
        pageFactory.loginPage().login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        assertTrue(pageFactory.productsPage().isDisplayed(), "Should be on products page");
        
        pageFactory.productsPage().logout();
        
        assertTrue(pageFactory.loginPage().isLoginButtonVisible(), "Should be redirected to login page");
        assertEquals(TestConfig.BASE_URL, page.url(), "URL should be login page");
    }
    
    @Test
    @Order(11)
    @DisplayName("Test continue shopping from cart")
    void testContinueShoppingFromCart() {
        pageFactory.loginPage().navigate();
        pageFactory.loginPage().login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        pageFactory.productsPage().goToCart();
        pageFactory.cartPage().continueShopping();
        
        assertTrue(pageFactory.productsPage().isDisplayed(), "Should return to products page");
        assertEquals("Products", pageFactory.productsPage().getPageTitle());
    }
    
    @Test
    @Order(12)
    @DisplayName("Test checkout with missing information")
    void testCheckoutWithMissingInformation() {
        pageFactory.loginPage().navigate();
        pageFactory.loginPage().login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        pageFactory.productsPage().addProductToCart("sauce-labs-backpack");
        pageFactory.productsPage().goToCart();
        pageFactory.cartPage().proceedToCheckout();
        
        // Try to continue without filling information
        pageFactory.checkoutPage().clickContinue();
        
        // Should show error (page should still be on checkout step one)
        assertTrue(page.isVisible("[data-test='error']"), "Error message should be displayed");
    }
}
