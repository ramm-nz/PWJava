package com.example.base;

import com.example.pages.*;
import com.microsoft.playwright.Page;

/**
 * Page Manager for lazy initialization of Page Objects
 * Creates page objects only when they are accessed
 */
public class PageManager {
    
    private final Page page;
    
    // Page object instances (lazy initialized)
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    
    public PageManager(Page page) {
        this.page = page;
    }
    
    // Lazy getters - creates page objects only when needed
    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(page);
        }
        return loginPage;
    }
    
    public ProductsPage getProductsPage() {
        if (productsPage == null) {
            productsPage = new ProductsPage(page);
        }
        return productsPage;
    }
    
    public CartPage getCartPage() {
        if (cartPage == null) {
            cartPage = new CartPage(page);
        }
        return cartPage;
    }
    
    public CheckoutPage getCheckoutPage() {
        if (checkoutPage == null) {
            checkoutPage = new CheckoutPage(page);
        }
        return checkoutPage;
    }
    
    // Method to reset all page objects (useful for test cleanup)
    public void resetPages() {
        loginPage = null;
        productsPage = null;
        cartPage = null;
        checkoutPage = null;
    }
}
