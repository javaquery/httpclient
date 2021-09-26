package com.javaquery.http;

import com.javaquery.http.retry.DefaultBackoffStrategy;
import com.javaquery.http.retry.DefaultRetryCondition;
import com.javaquery.http.retry.RetryPolicy;

/**
 * @author javaquery
 * @since 1.0.0
 */
public class DefaultRetryPolicy {

    public static RetryPolicy get(){
        return new RetryPolicy(new DefaultRetryCondition(), new DefaultBackoffStrategy(), 5);
    }
}
