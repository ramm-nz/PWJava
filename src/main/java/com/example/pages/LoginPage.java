package com.example.pages;

import com.microsoft.playwright.Page;

public class LoginPage {
    private final Page page;
    
    // Locators
    private final String usernameInput = "#user-name";
    private final String passwordInput = "#password";
    private final String loginButton = "#login-button";
    private final String errorMessage = "[data-test='error']";
    
    public LoginPage(Page page) {
        this.page = page;
    }
    
    public void navigate() {
        page.navigate("https://www.saucedemo.com/");
    }
    
    public void login(String username, String password) {
        page.fill(usernameInput, username);
        page.fill(passwordInput, password);
        page.click(loginButton);
    }
    
    public void enterUsername(String username) {
        page.fill(usernameInput, username);
    }
    
    public void enterPassword(String password) {
        page.fill(passwordInput, password);
    }
    
    public void clickLogin() {
        page.click(loginButton);
    }
    
    public boolean isErrorMessageDisplayed() {
        return page.isVisible(errorMessage);
    }
    
    public String getErrorMessage() {
        return page.textContent(errorMessage);
    }
    
    public boolean isLoginButtonVisible() {
        return page.isVisible(loginButton);
    }
}
