package com.example.base;

import com.example.config.TestConfig;
import com.example.factory.PageFactory;
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
    
    // Option 1: PageManager for simple lazy initialization
    protected PageManager pageManager;
    
    // Option 2: PageFactory for advanced factory pattern
    protected PageFactory pageFactory;
    
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
            String videoFileName = sanitizeFileName(testInfo.getDisplayName()) + "_" + getTimestamp();
            Path videoPath = Paths.get(TestConfig.VIDEO_DIR, videoFileName);
            contextOptions.setRecordVideoDir(videoPath)
                    .setRecordVideoSize(TestConfig.VIEWPORT_WIDTH, TestConfig.VIEWPORT_HEIGHT);
        }
        
        // Create new context for this test
        synchronized (browser) {
            context = browser.newContext(contextOptions);
        }
        
        // Set timeouts
        context.setDefaultTimeout(TestConfig.DEFAULT_TIMEOUT);
        context.setDefaultNavigationTimeout(TestConfig.NAVIGATION_TIMEOUT);
        
        page = context.newPage();
        
        // Initialize page management utilities
        pageManager = new PageManager(page);
        pageFactory = new PageFactory(page);
        
        // Start tracing if enabled (only for sequential execution)
        if (TestConfig.ENABLE_TRACE && !TestConfig.PARALLEL_EXECUTION) {
            try {
                context.tracing().start(new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true));
            } catch (Exception e) {
                System.err.println("Failed to start tracing: " + e.getMessage());
            }
        }
    }

    @AfterEach
    void closeContext(TestInfo testInfo) {
        try {
            // Take screenshot on success if configured
            // Note: Screenshot on failure is handled by the ScreenshotExtension
            if (TestConfig.SCREENSHOT_ON_SUCCESS && page != null && !page.isClosed()) {
                takeScreenshot(testInfo.getDisplayName() + "_COMPLETED");
            }
            
            // Stop tracing and save if enabled
            if (TestConfig.ENABLE_TRACE && !TestConfig.PARALLEL_EXECUTION && context != null) {
                try {
                    String tracePath = TestConfig.TRACE_DIR + "/" + sanitizeFileName(testInfo.getDisplayName()) + "_" + getTimestamp() + ".zip";
                    context.tracing().stop(new Tracing.StopOptions()
                            .setPath(Paths.get(tracePath)));
                } catch (Exception e) {
                    System.err.println("Failed to stop tracing: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error in cleanup: " + e.getMessage());
        } finally {
            // Close context
            if (context != null) {
                try {
                    context.close();
                } catch (Exception e) {
                    System.err.println("Failed to close context: " + e.getMessage());
                }
            }
        }
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
