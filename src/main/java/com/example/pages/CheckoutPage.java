package com.example.pages;

import com.microsoft.playwright.Page;

public class CheckoutPage {
    private final Page page;
    
    // Locators - Step One
    private final String firstNameInput = "#first-name";
    private final String lastNameInput = "#last-name";
    private final String postalCodeInput = "#postal-code";
    private final String continueButton = "#continue";
    private final String cancelButton = "#cancel";
    
    // Locators - Step Two (Overview)
    private final String summaryInfo = ".summary_info";
    private final String finishButton = "#finish";
    
    // Locators - Complete
    private final String completeHeader = ".complete-header";
    private final String completeText = ".complete-text";
    private final String backHomeButton = "#back-to-products";
    
    public CheckoutPage(Page page) {
        this.page = page;
    }
    
    // Step One methods
    public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        page.fill(firstNameInput, firstName);
        page.fill(lastNameInput, lastName);
        page.fill(postalCodeInput, postalCode);
    }
    
    public void clickContinue() {
        page.click(continueButton);
    }
    
    public void clickCancel() {
        page.click(cancelButton);
    }
    
    // Step Two methods
    public boolean isSummaryInfoDisplayed() {
        return page.isVisible(summaryInfo);
    }
    
    public void clickFinish() {
        page.click(finishButton);
    }
    
    public String getPaymentInformation() {
        return page.textContent(".summary_value_label:has-text('SauceCard')");
    }
    
    public String getShippingInformation() {
        return page.textContent(".summary_value_label:has-text('Pony Express')");
    }
    
    public String getSubtotal() {
        return page.textContent(".summary_subtotal_label");
    }
    
    public String getTax() {
        return page.textContent(".summary_tax_label");
    }
    
    public String getTotal() {
        return page.textContent(".summary_total_label");
    }
    
    // Complete methods
    public boolean isOrderComplete() {
        return page.isVisible(completeHeader);
    }
    
    public String getCompleteHeader() {
        return page.textContent(completeHeader);
    }
    
    public String getCompleteText() {
        return page.textContent(completeText);
    }
    
    public void backToHome() {
        page.click(backHomeButton);
    }
}
