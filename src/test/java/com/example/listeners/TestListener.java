package com.example.listeners;

import org.junit.jupiter.api.extension.*;

public class TestListener implements TestWatcher, AfterAllCallback {
    
    private int totalTests = 0;
    private int passedTests = 0;
    private int failedTests = 0;
    private int skippedTests = 0;
    
    @Override
    public void testSuccessful(ExtensionContext context) {
        totalTests++;
        passedTests++;
        System.out.println("✓ PASSED: " + context.getDisplayName());
    }
    
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        totalTests++;
        failedTests++;
        System.err.println("✗ FAILED: " + context.getDisplayName());
        System.err.println("  Reason: " + cause.getMessage());
    }
    
    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        totalTests++;
        skippedTests++;
        System.out.println("⊘ ABORTED: " + context.getDisplayName());
    }
    
    @Override
    public void testDisabled(ExtensionContext context, java.util.Optional<String> reason) {
        totalTests++;
        skippedTests++;
        System.out.println("⊗ DISABLED: " + context.getDisplayName() + 
            (reason.isPresent() ? " - " + reason.get() : ""));
    }
    
    @Override
    public void afterAll(ExtensionContext context) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST EXECUTION SUMMARY");
        System.out.println("=".repeat(60));
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Passed: " + passedTests + " (" + getPercentage(passedTests, totalTests) + "%)");
        System.out.println("Failed: " + failedTests + " (" + getPercentage(failedTests, totalTests) + "%)");
        System.out.println("Skipped: " + skippedTests + " (" + getPercentage(skippedTests, totalTests) + "%)");
        System.out.println("=".repeat(60) + "\n");
    }
    
    private String getPercentage(int value, int total) {
        if (total == 0) return "0.00";
        return String.format("%.2f", (value * 100.0) / total);
    }
}
