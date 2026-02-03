package com.example.tests;

import com.example.base.BaseTest;
import com.example.base.PageManager;
import com.example.config.TestConfig;
import com.example.listeners.ScreenshotExtension;
import com.example.listeners.TestListener;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Example test class using PageManager for lazy initialization
 * This approach creates page objects only when needed
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith({TestListener.class, ScreenshotExtension.class})
public class SauceDemoTestWithPageManager extends BaseTest {
    
    private PageManager pageManager;
    
    @BeforeEach
    void initializePageManager() {
        pageManager = new PageManager(page);
    }

    @Test
    @Order(1)
    @DisplayName("Test successful login with PageManager")
    void testSuccessfulLogin() {
        // Access pages through pageManager - created only when called
        pageManager.getLoginPage().navigate();
        assertTrue(pageManager.getLoginPage().isLoginButtonVisible(), "Login button should be visible");
        
        pageManager.getLoginPage().login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        assertTrue(pageManager.getProductsPage().isDisplayed(), "Products list should be visible after login");
        assertEquals("Products", pageManager.getProductsPage().getPageTitle(), "Page title should be 'Products'");
    }

    @Test
    @Order(2)
    @DisplayName("Test complete checkout with PageManager")
    void testCompleteCheckout() {
        pageManager.getLoginPage().navigate();
        pageManager.getLoginPage().login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        pageManager.getProductsPage().addProductToCart("sauce-labs-backpack");
        pageManager.getProductsPage().goToCart();
        
        assertTrue(pageManager.getCartPage().isCartItemDisplayed(), "Cart should have items");
        pageManager.getCartPage().proceedToCheckout();
        
        pageManager.getCheckoutPage().fillCheckoutInformation("John", "Doe", "12345");
        pageManager.getCheckoutPage().clickContinue();
        pageManager.getCheckoutPage().clickFinish();
        
        assertTrue(pageManager.getCheckoutPage().isOrderComplete(), "Order should be complete");
    }
}
