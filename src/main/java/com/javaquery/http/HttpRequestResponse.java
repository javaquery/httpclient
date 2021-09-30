package com.javaquery.http;

/**
 * Holds http request and response object.
 *
 * @author javaquery
 * @since 1.0.0
 */
public class HttpRequestResponse {
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;
    private int retriesAttempted = 0;

    /**
     * Instantiates a new Http request response.
     *
     * @param httpRequest the http request
     */
    public HttpRequestResponse(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    /**
     * Gets http request.
     *
     * @return the http request
     */
    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    /**
     * Sets http request.
     *
     * @param httpRequest the http request
     */
    public void setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    /**
     * Gets http response.
     *
     * @return the http response
     */
    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    /**
     * Sets http response.
     *
     * @param httpResponse the http response
     */
    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    /**
     * Gets retries attempted.
     *
     * @return the retries attempted
     */
    public int getRetriesAttempted() {
        return retriesAttempted;
    }

    /**
     * Sets retries attempted.
     *
     * @param retriesAttempted the retries attempted
     */
    public void setRetriesAttempted(int retriesAttempted) {
        this.retriesAttempted = retriesAttempted;
    }
}
