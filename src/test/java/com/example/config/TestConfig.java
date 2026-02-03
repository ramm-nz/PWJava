package com.example.config;

public class TestConfig {
    
    // Browser Configuration
    public static final String BROWSER = System.getProperty("browser", "chromium"); // chromium, firefox, webkit
    public static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "false"));
    public static final int SLOW_MO = Integer.parseInt(System.getProperty("slowmo", "50"));
    
    // Parallel Execution Configuration
    public static final int WORKERS = Integer.parseInt(System.getProperty("workers", "4"));
    
    // Retry Configuration
    public static final int MAX_RETRIES = Integer.parseInt(System.getProperty("retries", "2"));
    
    // Screenshot Configuration
    public static final boolean SCREENSHOT_ON_FAILURE = Boolean.parseBoolean(System.getProperty("screenshot.failure", "true"));
    public static final boolean SCREENSHOT_ON_SUCCESS = Boolean.parseBoolean(System.getProperty("screenshot.success", "false"));
    public static final String SCREENSHOT_DIR = System.getProperty("screenshot.dir", "target/screenshots");
    
    // Video Configuration
    public static final boolean RECORD_VIDEO = Boolean.parseBoolean(System.getProperty("record.video", "false"));
    public static final String VIDEO_DIR = System.getProperty("video.dir", "target/videos");
    
    // Trace Configuration
    public static final boolean ENABLE_TRACE = Boolean.parseBoolean(System.getProperty("enable.trace", "false"));
    public static final String TRACE_DIR = System.getProperty("trace.dir", "target/traces");
    
    // Viewport Configuration
    public static final int VIEWPORT_WIDTH = Integer.parseInt(System.getProperty("viewport.width", "1920"));
    public static final int VIEWPORT_HEIGHT = Integer.parseInt(System.getProperty("viewport.height", "1080"));
    
    // Timeout Configuration
    public static final int DEFAULT_TIMEOUT = Integer.parseInt(System.getProperty("default.timeout", "30000"));
    public static final int NAVIGATION_TIMEOUT = Integer.parseInt(System.getProperty("navigation.timeout", "30000"));
    
    // Base URL
    public static final String BASE_URL = System.getProperty("base.url", "https://www.saucedemo.com/");
    
    // Test Credentials
    public static final String STANDARD_USER = "standard_user";
    public static final String LOCKED_OUT_USER = "locked_out_user";
    public static final String PROBLEM_USER = "problem_user";
    public static final String PERFORMANCE_GLITCH_USER = "performance_glitch_user";
    public static final String PASSWORD = "secret_sauce";
}
