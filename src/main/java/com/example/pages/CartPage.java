package com.example.pages;

import com.microsoft.playwright.Page;

public class CartPage {
    private final Page page;
    
    // Locators
    private final String cartItem = ".cart_item";
    private final String checkoutButton = "#checkout";
    private final String continueShoppingButton = "#continue-shopping";
    private final String removeButton = ".cart_button";
    private final String itemName = ".inventory_item_name";
    private final String itemPrice = ".inventory_item_price";
    
    public CartPage(Page page) {
        this.page = page;
    }
    
    public boolean isCartItemDisplayed() {
        return page.isVisible(cartItem);
    }
    
    public int getCartItemCount() {
        return page.locator(cartItem).count();
    }
    
    public void proceedToCheckout() {
        page.click(checkoutButton);
    }
    
    public void continueShopping() {
        page.click(continueShoppingButton);
    }
    
    public void removeItem(int index) {
        page.locator(removeButton).nth(index).click();
    }
    
    public String getItemName(int index) {
        return page.locator(itemName).nth(index).textContent();
    }
    
    public String getItemPrice(int index) {
        return page.locator(itemPrice).nth(index).textContent();
    }
    
    public boolean isCheckoutButtonVisible() {
        return page.isVisible(checkoutButton);
    }
}
