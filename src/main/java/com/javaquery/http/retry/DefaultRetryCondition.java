package com.javaquery.http.retry;

import com.javaquery.http.HttpRequestResponse;
import com.javaquery.util.Objects;

/**
 * @author javaquery
 * @since 1.0.0
 */
public class DefaultRetryCondition implements RetryPolicy.RetryCondition {

    @Override
    public boolean shouldRetry(HttpRequestResponse httpRequestResponse, int retriesAttempted) {
        return isServiceOutage(httpRequestResponse);
    }

    private boolean isServiceOutage(HttpRequestResponse httpRequestResponse){
        if(Objects.isNull(httpRequestResponse) || Objects.isNull(httpRequestResponse.getHttpResponse())){
            return false;
        }
        return httpRequestResponse.getHttpResponse().getStatusCode() == 500
                || httpRequestResponse.getHttpResponse().getStatusCode() == 503;
    }
}
