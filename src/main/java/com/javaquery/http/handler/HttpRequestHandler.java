package com.javaquery.http.handler;

import com.javaquery.http.HttpExecutionContext;
import com.javaquery.http.HttpRequest;
import com.javaquery.http.HttpResponse;

/**
 * The interface Http request handler.
 *
 * @author javaquery
 * @since 1.0.0
 */
public interface HttpRequestHandler {

    /**
     * Before http request.
     *
     * @param httpExecutionContext the http execution context
     * @param httpRequest          the http request
     */
    void beforeRequest(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest);

    /**
     * After http response.
     *
     * @param httpExecutionContext the http execution context
     * @param httpRequest          the http request
     * @param httpResponse         the http response
     */
    void afterResponse(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest, HttpResponse httpResponse);

    /**
     * On error.
     *
     * @param httpExecutionContext the http execution context
     * @param httpRequest          the http request
     * @param exception            the exception
     */
    void onError(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest, Exception exception);
}
