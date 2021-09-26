package com.javaquery.http;

import com.javaquery.http.handler.HttpResponseHandler;
import com.javaquery.http.logging.LoggableHttpRequest;
import com.javaquery.http.retry.RetryPolicy;
import com.javaquery.util.Objects;
import com.javaquery.util.collection.Collections;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The type Http client.
 *
 * @author javaquery
 * @since 1.0.0
 */
public class HttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

    private static final int MAX_BACKOFF_IN_MILLISECONDS = 5 * 60 * 1000;

    /**
     * Execute.
     *
     * @param httpExecutionContext the http execution context
     * @param httpRequest          the http request
     * @param httpResponseHandler  the http response handler
     */
    public <R> R execute(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest, HttpResponseHandler<R> httpResponseHandler) {
        final Map<String, String> originalHeaders = new HashMap<>(httpRequest.getHeaders());
        final Map<String, String> originalQueryParameters = new LinkedHashMap<>(httpRequest.getQueryParameters());
        HttpRequestResponse httpRequestResponse = new HttpRequestResponse(httpRequest);

        R responseHandlerResult = doExecute(httpExecutionContext, httpRequestResponse, httpResponseHandler);

        if (Objects.nonNull(httpRequest.getRetryPolicy())) {
            while (shouldRetry(httpRequest.getRetryPolicy(), httpRequestResponse)) {
                sleepFor(httpRequest.getRetryPolicy(), httpRequestResponse, httpRequestResponse.getRetriesAttempted());
                int retriesAttempted = httpRequestResponse.getRetriesAttempted();
                httpRequestResponse = new HttpRequestResponse(httpRequest);
                httpRequestResponse.setRetriesAttempted(retriesAttempted + 1);
                httpRequest.withHeaders(originalHeaders);
                httpRequest.withQueryParameter(originalQueryParameters);
                responseHandlerResult = doExecute(httpExecutionContext, httpRequestResponse, httpResponseHandler);
            }
            if (httpRequestResponse.getRetriesAttempted() == httpRequest.getRetryPolicy().getMaxErrorRetry()) {
                httpResponseHandler.onMaxRetryAttempted(httpRequestResponse.getHttpResponse());
            }
        }

        return responseHandlerResult;
    }

    private <R> R doExecute(HttpExecutionContext httpExecutionContext, HttpRequestResponse httpRequestResponse, HttpResponseHandler<R> httpResponseHandler) {
        LoggableHttpRequest loggableHttpRequest = null;
        HttpRequest httpRequest = httpRequestResponse.getHttpRequest();
        try {
            beforeRequest(httpExecutionContext, httpRequest);

            ApacheHttpRequestBuilder apacheHttpRequestBuilder = new ApacheHttpRequestBuilder(httpRequest);
            HttpUriRequest apacheHttpRequest = apacheHttpRequestBuilder.build();

            loggableHttpRequest = new LoggableHttpRequest(httpRequest);

            CloseableHttpClient closeableHttpClient = HttpClients.custom()
                    .setDefaultCredentialsProvider(apacheHttpRequestBuilder.credentialsProvider())
                    .build();
            org.apache.http.HttpResponse closeableHttpResponse = closeableHttpClient.execute(apacheHttpRequest);

            HttpResponse httpResponse = new HttpResponse(closeableHttpResponse);
            afterResponse(httpExecutionContext, httpRequest, httpResponse);
            httpRequestResponse.setHttpResponse(httpResponse);
        } catch (Exception exception) {
            exception.printStackTrace();
            LOGGER.error(exception.getMessage(), exception);
            onError(httpExecutionContext, httpRequest, exception);
        } finally {
            LOGGER.info(StringPool.LOG_ACTION, StringPool.LOG_HTTP_REQUEST);
            LOGGER.info(StringPool.LOG_HTTP_REQUEST, loggableHttpRequest);
        }

        if (Objects.nonNull(httpResponseHandler) && shouldExecuteHttpResponseHandler(httpRequestResponse)) {
            return httpResponseHandler.onResponse(httpRequestResponse.getHttpResponse());
        }
        return null;
    }

    private void beforeRequest(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest) {
        if (Collections.nonNullNonEmpty(httpExecutionContext.getHttpRequestHandlers())) {
            httpExecutionContext.getHttpRequestHandlers().forEach(httpRequestHandler -> httpRequestHandler.beforeRequest(httpExecutionContext, httpRequest));
        }
    }

    private void afterResponse(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest, HttpResponse httpResponse) {
        if (Collections.nonNullNonEmpty(httpExecutionContext.getHttpRequestHandlers())) {
            httpExecutionContext.getHttpRequestHandlers().forEach(httpRequestHandler -> httpRequestHandler.afterResponse(httpExecutionContext, httpRequest, httpResponse));
        }
    }

    private void onError(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest, Exception exception) {
        if (Collections.nonNullNonEmpty(httpExecutionContext.getHttpRequestHandlers())) {
            httpExecutionContext.getHttpRequestHandlers().forEach(httpRequestHandler -> httpRequestHandler.onError(httpExecutionContext, httpRequest, exception));
        }
    }

    private boolean shouldExecuteHttpResponseHandler(HttpRequestResponse httpRequestResponse) {
        return Objects.isNull(httpRequestResponse.getHttpRequest().getRetryPolicy())
                || !shouldRetry(httpRequestResponse.getHttpRequest().getRetryPolicy(), httpRequestResponse);
    }

    private boolean shouldRetry(RetryPolicy retryPolicy, HttpRequestResponse httpRequestResponse) {
        boolean shouldRetryAttempted = retryPolicy.retryTillSucceed() || retryPolicy.getMaxErrorRetry() > httpRequestResponse.getRetriesAttempted();
        boolean shouldRetryCustomCondition = retryPolicy.getRetryCondition().shouldRetry(httpRequestResponse, httpRequestResponse.getRetriesAttempted());
        return shouldRetryAttempted && shouldRetryCustomCondition;
    }

    private void sleepFor(RetryPolicy retryPolicy, HttpRequestResponse httpRequestResponse, int retriesAttempted) {
        long delay = retryPolicy.getBackOffStrategy().delayBeforeNextRetry(httpRequestResponse, retriesAttempted);
        delay = Math.min(delay, MAX_BACKOFF_IN_MILLISECONDS);
        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
