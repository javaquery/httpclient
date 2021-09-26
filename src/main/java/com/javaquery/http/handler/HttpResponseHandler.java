package com.javaquery.http.handler;

import com.javaquery.http.HttpResponse;

/**
 * The interface Http response handler.
 *
 * @param <R> the type parameter
 * @author javaquery
 * @since 1.0.0
 */
public interface HttpResponseHandler<R> {
    /**
     * On response r.
     *
     * @param httpResponse the http response
     * @return the r
     */
    R onResponse(HttpResponse httpResponse);
}
