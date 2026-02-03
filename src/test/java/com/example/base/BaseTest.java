package com.example.base;

import com.example.config.TestConfig;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseTest {
    
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;
    private static final ThreadLocal<BrowserContext> threadLocalContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> threadLocalPage = new ThreadLocal<>();
    
    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        
        // Select browser based on configuration
        BrowserType browserType;
        switch (TestConfig.BROWSER.toLowerCase()) {
            case "firefox":
                browserType = playwright.firefox();
                break;
            case "webkit":
                browserType = playwright.webkit();
                break;
            case "chromium":
            default:
                browserType = playwright.chromium();
                break;
        }
        
        browser = browserType.launch(new BrowserType.LaunchOptions()
                .setHeadless(TestConfig.HEADLESS)
                .setSlowMo(TestConfig.SLOW_MO));
        
        // Create screenshot directory
        createDirectory(TestConfig.SCREENSHOT_DIR);
        
        // Create video directory if recording is enabled
        if (TestConfig.RECORD_VIDEO) {
            createDirectory(TestConfig.VIDEO_DIR);
        }
        
        // Create trace directory if tracing is enabled
        if (TestConfig.ENABLE_TRACE) {
            createDirectory(TestConfig.TRACE_DIR);
        }
    }

    @AfterAll
    static void closeBrowser() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @BeforeEach
    void createContextAndPage(TestInfo testInfo) {
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(TestConfig.VIEWPORT_WIDTH, TestConfig.VIEWPORT_HEIGHT);
        
        // Enable video recording if configured
        if (TestConfig.RECORD_VIDEO) {
            contextOptions.setRecordVideoDir(Paths.get(TestConfig.VIDEO_DIR))
                    .setRecordVideoSize(TestConfig.VIEWPORT_WIDTH, TestConfig.VIEWPORT_HEIGHT);
        }
        
        context = browser.newContext(contextOptions);
        
        // Set timeouts
        context.setDefaultTimeout(TestConfig.DEFAULT_TIMEOUT);
        context.setDefaultNavigationTimeout(TestConfig.NAVIGATION_TIMEOUT);
        
        page = context.newPage();
        
        // Start tracing if enabled
        if (TestConfig.ENABLE_TRACE) {
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));
        }
        
        // Store in ThreadLocal for parallel execution
        threadLocalContext.set(context);
        threadLocalPage.set(page);
    }

    @AfterEach
    void closeContext(TestInfo testInfo) {
        // Take screenshot on success if configured
        // Note: Screenshot on failure is handled by the ScreenshotExtension
        if (TestConfig.SCREENSHOT_ON_SUCCESS) {
            takeScreenshot(testInfo.getDisplayName() + "_COMPLETED");
        }
        
        // Stop tracing and save if enabled
        if (TestConfig.ENABLE_TRACE && context != null) {
            String tracePath = TestConfig.TRACE_DIR + "/" + sanitizeFileName(testInfo.getDisplayName()) + "_" + getTimestamp() + ".zip";
            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(Paths.get(tracePath)));
        }
        
        // Close context
        if (context != null) {
            context.close();
        }
        
        // Clean up ThreadLocal
        threadLocalContext.remove();
        threadLocalPage.remove();
    }
    
    // Helper method to navigate to base URL
    protected void navigateToBaseUrl() {
        page.navigate(TestConfig.BASE_URL);
    }
    
    // Helper method to take screenshot
    protected void takeScreenshot(String fileName) {
        try {
            String sanitizedFileName = sanitizeFileName(fileName);
            String timestamp = getTimestamp();
            Path screenshotPath = Paths.get(TestConfig.SCREENSHOT_DIR, 
                    sanitizedFileName + "_" + timestamp + ".png");
            
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(screenshotPath)
                    .setFullPage(true));
            
            System.out.println("Screenshot saved: " + screenshotPath);
        } catch (Exception e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
        }
    }
    
    // Helper method to take screenshot with custom path
    protected void takeScreenshot(String fileName, boolean fullPage) {
        try {
            String sanitizedFileName = sanitizeFileName(fileName);
            String timestamp = getTimestamp();
            Path screenshotPath = Paths.get(TestConfig.SCREENSHOT_DIR, 
                    sanitizedFileName + "_" + timestamp + ".png");
            
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(screenshotPath)
                    .setFullPage(fullPage));
            
            System.out.println("Screenshot saved: " + screenshotPath);
        } catch (Exception e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
        }
    }
    
    // Helper method to get current page for parallel execution
    protected Page getCurrentPage() {
        return threadLocalPage.get() != null ? threadLocalPage.get() : page;
    }
    
    // Helper method to get current context for parallel execution
    protected BrowserContext getCurrentContext() {
        return threadLocalContext.get() != null ? threadLocalContext.get() : context;
    }
    
    // Utility method to create directory
    private static void createDirectory(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + dirPath + " - " + e.getMessage());
        }
    }
    
    // Utility method to sanitize file names
    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
    
    // Utility method to get timestamp
    private String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return dateFormat.format(new Date());
    }
}
