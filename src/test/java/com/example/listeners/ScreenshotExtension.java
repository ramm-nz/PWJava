package com.example.listeners;

import com.example.config.TestConfig;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class ScreenshotExtension implements TestWatcher {
    
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        if (TestConfig.SCREENSHOT_ON_FAILURE) {
            captureScreenshot(context, "FAILED");
        }
    }
    
    @Override
    public void testSuccessful(ExtensionContext context) {
        if (TestConfig.SCREENSHOT_ON_SUCCESS) {
            captureScreenshot(context, "PASSED");
        }
    }
    
    private void captureScreenshot(ExtensionContext context, String status) {
        try {
            // Try to get the Page object from the test instance
            Optional<Object> testInstance = context.getTestInstance();
            if (testInstance.isPresent()) {
                Object instance = testInstance.get();
                
                // Use reflection to get the page field
                java.lang.reflect.Field pageField = findPageField(instance.getClass());
                if (pageField != null) {
                    pageField.setAccessible(true);
                    Page page = (Page) pageField.get(instance);
                    
                    if (page != null && !page.isClosed()) {
                        String testName = context.getDisplayName();
                        String sanitizedName = sanitizeFileName(testName);
                        String timestamp = getTimestamp();
                        String fileName = sanitizedName + "_" + status + "_" + timestamp + ".png";
                        
                        Path screenshotPath = Paths.get(TestConfig.SCREENSHOT_DIR, fileName);
                        page.screenshot(new Page.ScreenshotOptions()
                                .setPath(screenshotPath)
                                .setFullPage(true));
                        
                        System.out.println("Screenshot saved: " + screenshotPath);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
    
    private java.lang.reflect.Field findPageField(Class<?> clazz) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField("page");
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
    
    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
    
    private String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return dateFormat.format(new Date());
    }
}
