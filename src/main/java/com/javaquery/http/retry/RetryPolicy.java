package com.javaquery.http.retry;

import com.javaquery.http.HttpRequestResponse;
import com.javaquery.http.StringPool;

/**
 * @author javaquery
 * @since 1.0.0
 */
public final class RetryPolicy {

    private final RetryCondition retryCondition;

    private final BackoffStrategy backOffStrategy;

    private final int maxErrorRetry;

    private boolean retryTillSuccess = false;

    public RetryPolicy(RetryCondition retryCondition, BackoffStrategy backOffStrategy, int maxErrorRetry) {
        if (maxErrorRetry < 0) {
            throw new IllegalArgumentException(StringPool.ERROR_MAX_RETRY_VALUE);
        }
        this.retryCondition = retryCondition;
        this.backOffStrategy = backOffStrategy;
        this.maxErrorRetry = maxErrorRetry;
    }

    public RetryCondition getRetryCondition() {
        return retryCondition;
    }

    public BackoffStrategy getBackOffStrategy() {
        return backOffStrategy;
    }

    public int getMaxErrorRetry() {
        return maxErrorRetry;
    }

    public boolean retryTillSucceed() {
        return retryTillSuccess;
    }

    public void retryTillSuccess() {
        this.retryTillSuccess = true;
    }

    public interface RetryCondition {
        boolean shouldRetry(HttpRequestResponse httpRequestResponse, int retriesAttempted);
    }

    public interface BackoffStrategy {
        long delayBeforeNextRetry(HttpRequestResponse httpRequestResponse, int retriesAttempted);
    }
}
