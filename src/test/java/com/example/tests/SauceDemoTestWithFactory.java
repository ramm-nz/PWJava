package com.example.tests;

import com.example.base.BaseTest;
import com.example.config.TestConfig;
import com.example.factory.PageFactory;
import com.example.listeners.ScreenshotExtension;
import com.example.listeners.TestListener;
import com.example.pages.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Example test class using PageFactory pattern
 * This approach provides flexible, generic page object creation
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith({TestListener.class, ScreenshotExtension.class})
public class SauceDemoTestWithFactory extends BaseTest {
    
    private PageFactory pageFactory;
    
    @BeforeEach
    void initializePageFactory() {
        pageFactory = new PageFactory(page);
    }

    @Test
    @Order(1)
    @DisplayName("Test login with Factory - Generic Approach")
    void testLoginWithGenericFactory() {
        // Generic approach - flexible but requires class reference
        LoginPage loginPage = pageFactory.getPage(LoginPage.class);
        ProductsPage productsPage = pageFactory.getPage(ProductsPage.class);
        
        loginPage.navigate();
        loginPage.login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        assertTrue(productsPage.isDisplayed());
    }

    @Test
    @Order(2)
    @DisplayName("Test checkout with Factory - Convenience Methods")
    void testCheckoutWithConvenienceMethods() {
        // Using convenience methods - cleaner syntax
        pageFactory.loginPage().navigate();
        pageFactory.loginPage().login(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        
        pageFactory.productsPage().addProductToCart("sauce-labs-backpack");
        pageFactory.productsPage().goToCart();
        
        pageFactory.cartPage().proceedToCheckout();
        
        pageFactory.checkoutPage().fillCheckoutInformation("John", "Doe", "12345");
        pageFactory.checkoutPage().clickContinue();
        pageFactory.checkoutPage().clickFinish();
        
        assertTrue(pageFactory.checkoutPage().isOrderComplete());
    }
}
