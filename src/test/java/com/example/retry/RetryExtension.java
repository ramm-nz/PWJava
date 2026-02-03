package com.example.retry;

import com.example.config.TestConfig;
import org.junit.jupiter.api.extension.*;

public class RetryExtension implements TestExecutionExceptionHandler, AfterTestExecutionCallback {
    
    private static final String RETRY_COUNT_KEY = "retryCount";
    
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        int retryCount = getRetryCount(context);
        
        if (retryCount < TestConfig.MAX_RETRIES) {
            System.out.println(String.format(
                "Test '%s' failed. Retry attempt %d of %d",
                context.getDisplayName(),
                retryCount + 1,
                TestConfig.MAX_RETRIES
            ));
            
            setRetryCount(context, retryCount + 1);
            
            // Re-run the test
            context.getStore(ExtensionContext.Namespace.GLOBAL).put(RETRY_COUNT_KEY, retryCount + 1);
            
            // Note: This is a simplified retry mechanism
            // For production, consider using a more robust solution
            throw throwable;
        } else {
            System.out.println(String.format(
                "Test '%s' failed after %d retries",
                context.getDisplayName(),
                TestConfig.MAX_RETRIES
            ));
            throw throwable;
        }
    }
    
    @Override
    public void afterTestExecution(ExtensionContext context) {
        // Reset retry count after successful execution
        if (!context.getExecutionException().isPresent()) {
            setRetryCount(context, 0);
        }
    }
    
    private int getRetryCount(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.GLOBAL)
            .getOrDefault(RETRY_COUNT_KEY, Integer.class, 0);
    }
    
    private void setRetryCount(ExtensionContext context, int count) {
        context.getStore(ExtensionContext.Namespace.GLOBAL)
            .put(RETRY_COUNT_KEY, count);
    }
}
