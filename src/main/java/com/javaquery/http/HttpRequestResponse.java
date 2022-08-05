package com.javaquery.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private long executionStartTime;
    private long executionEndTime;

    /**
     * Instantiates a new Http request response.
     *
     * @param httpRequest the http request
     */
    public HttpRequestResponse(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
        this.executionStartTime = System.currentTimeMillis();
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

    /**
     * Set execution end time and executionTime (milliseconds) will be added in attributes.
     */
    public void setExecutionEndTime(){
        executionEndTime = System.currentTimeMillis();
    }

    /**
     * Gets attributes.
     *
     * @return the attributes
     */
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(StringPool.LOG_ACTION, StringPool.LOG_HTTP_REQUEST);
        if(executionStartTime != 0 && executionEndTime != 0){
            attributes.put(StringPool.EXECUTION_TIME_MS, (executionEndTime - executionStartTime));
        }
        attributes.put(StringPool.LOG_HTTP_REQUEST, httpRequest);
        if(Objects.nonNull(httpResponse)){
            attributes.put(StringPool.LOG_HTTP_RESPONSE, httpResponse);
        }
        attributes.put(StringPool.RETRIES_ATTEMPTED, retriesAttempted);
        return attributes;
    }
}
