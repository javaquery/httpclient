package com.javaquery.http.logging;

import com.javaquery.http.HttpMethod;
import com.javaquery.http.HttpRequest;

import java.util.Map;

/**
 * The type Loggable http request.
 *
 * @author javaquery
 * @since 1.0.0
 */
public class LoggableHttpRequest {

    private final String httpRequestName;
    private final HttpMethod httpMethod;
    private final String host;
    private final String endPoint;
    private final Map<String, String> headers;
    private final Map<String, String> queryParameters;
    private final HttpRequest.HttpPayload httpPayload;

    /**
     * Instantiates a new Loggable http request.
     *
     * @param httpRequest the http request
     */
    public LoggableHttpRequest(HttpRequest httpRequest){
        this.httpRequestName = httpRequest.getHttpRequestName();
        this.httpMethod = httpRequest.getHttpMethod();
        this.host = httpRequest.getHost().getHost();
        this.endPoint = httpRequest.getEndPoint();
        this.headers = httpRequest.getHeaders();
        this.queryParameters = httpRequest.getQueryParameters();
        this.httpPayload = httpRequest.getHttpPayload();
    }

    /**
     * Gets http request name.
     *
     * @return the http request name
     */
    public String getHttpRequestName() {
        return httpRequestName;
    }

    /**
     * Gets http method.
     *
     * @return the http method
     */
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets end point.
     *
     * @return the end point
     */
    public String getEndPoint() {
        return endPoint;
    }

    /**
     * Gets headers.
     *
     * @return the headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Gets query parameters.
     *
     * @return the query parameters
     */
    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }

    /**
     * Gets http payload.
     *
     * @return the http payload
     */
    public HttpRequest.HttpPayload getHttpPayload() {
        return httpPayload;
    }
}
