package com.javaquery.http;

/**
 * @author javaquery
 * @since 1.0.0
 */
public class HttpRequestResponse {
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;
    private int retriesAttempted = 0;

    public HttpRequestResponse(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public int getRetriesAttempted() {
        return retriesAttempted;
    }

    public void setRetriesAttempted(int retriesAttempted) {
        this.retriesAttempted = retriesAttempted;
    }
}
