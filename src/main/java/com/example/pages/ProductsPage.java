package com.example.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class ProductsPage {
    private final Page page;
    
    // Locators
    private final String inventoryList = ".inventory_list";
    private final String cartBadge = ".shopping_cart_badge";
    private final String cartLink = ".shopping_cart_link";
    private final String pageTitle = ".title";
    private final String sortContainer = ".product_sort_container";
    private final String menuButton = "#react-burger-menu-btn";
    private final String logoutLink = "#logout_sidebar_link";
    private final String inventoryItemPrice = ".inventory_item_price";
    
    public ProductsPage(Page page) {
        this.page = page;
    }
    
    public boolean isDisplayed() {
        return page.isVisible(inventoryList);
    }
    
    public String getPageTitle() {
        return page.textContent(pageTitle);
    }
    
    public void addProductToCart(String productName) {
        String productId = productName.toLowerCase().replace(" ", "-");
        page.click("#add-to-cart-" + productId);
    }
    
    public void addProductByIndex(int index) {
        page.locator(".btn_inventory").nth(index).click();
    }
    
    public String getCartItemCount() {
        if (page.isVisible(cartBadge)) {
            return page.textContent(cartBadge);
        }
        return "0";
    }
    
    public void goToCart() {
        page.click(cartLink);
    }
    
    public void sortProducts(String sortOption) {
        page.selectOption(sortContainer, sortOption);
    }
    
    public void openMenu() {
        page.click(menuButton);
    }
    
    public void logout() {
        openMenu();
        page.waitForSelector(logoutLink, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE));
        page.click(logoutLink);
    }
    
    public String getFirstProductPrice() {
        return page.textContent(inventoryItemPrice);
    }
    
    public int getProductCount() {
        return page.locator(".inventory_item").count();
    }
}
