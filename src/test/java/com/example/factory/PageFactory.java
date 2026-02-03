package com.example.factory;

import com.example.pages.*;
import com.microsoft.playwright.Page;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Page Factory Pattern - Advanced approach for managing Page Objects
 * Uses generics and reflection for flexible page object creation
 */
public class PageFactory {
    
    private final Page page;
    private final Map<Class<?>, Object> pageCache = new HashMap<>();
    
    public PageFactory(Page page) {
        this.page = page;
    }
    
    /**
     * Generic method to get or create page objects
     * @param pageClass The class of the page object to create
     * @return Instance of the requested page object
     */
    @SuppressWarnings("unchecked")
    public <T> T getPage(Class<T> pageClass) {
        // Check if page object already exists in cache
        if (pageCache.containsKey(pageClass)) {
            return (T) pageCache.get(pageClass);
        }
        
        // Create new instance and cache it
        try {
            Constructor<T> constructor = pageClass.getConstructor(Page.class);
            T pageObject = constructor.newInstance(page);
            pageCache.put(pageClass, pageObject);
            return pageObject;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create page object: " + pageClass.getName(), e);
        }
    }
    
    /**
     * Clear the page object cache
     */
    public void clearCache() {
        pageCache.clear();
    }
    
    // Convenience methods for type-safe access
    public LoginPage loginPage() {
        return getPage(LoginPage.class);
    }
    
    public ProductsPage productsPage() {
        return getPage(ProductsPage.class);
    }
    
    public CartPage cartPage() {
        return getPage(CartPage.class);
    }
    
    public CheckoutPage checkoutPage() {
        return getPage(CheckoutPage.class);
    }
}
