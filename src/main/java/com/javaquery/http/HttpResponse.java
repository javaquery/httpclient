package com.javaquery.http;

import com.javaquery.util.Objects;
import org.apache.http.Header;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The Http response object.
 *
 * @author javaquery
 * @since 1.0.0
 */
public class HttpResponse {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpResponse.class);
    private int statusCode;
    private Map<String, String> headers;
    private String body;
    private org.apache.http.HttpResponse apacheHttpResponse;

    /**
     * Instantiates a new Http response.
     *
     * @param httpResponse the http response
     */
    public HttpResponse(org.apache.http.HttpResponse httpResponse) {
        apacheHttpResponse = httpResponse;
        if(Objects.nonNull(httpResponse.getAllHeaders())
            && httpResponse.getAllHeaders().length > 0){
            headers = new HashMap<>();
            for (Header header: httpResponse.getAllHeaders()) {
                headers.put(header.getName(), header.getValue());
            }
        }
    }

    /**
     * Update http response.
     *
     * @param apacheHttpResponse the apache http response
     */
    public void updateHttpResponse(org.apache.http.HttpResponse apacheHttpResponse) {
        this.apacheHttpResponse = apacheHttpResponse;
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
     * Gets body.
     *
     * @return the body
     */
    public String getBody() {
        if(Objects.nonNull(apacheHttpResponse)){
            try {
                body = EntityUtils.toString(apacheHttpResponse.getEntity());
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return body;
    }

    /**
     * Gets json object body.
     *
     * @return the json object body
     */
    public JSONObject getJSONObjectBody() {
        return new JSONObject(getBody());
    }

    /**
     * Get json array body json array.
     *
     * @return the json array
     */
    public JSONArray getJSONArrayBody(){
        return new JSONArray(getBody());
    }

    /**
     * Gets status code.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return Objects.nonNull(apacheHttpResponse) ? apacheHttpResponse.getStatusLine().getStatusCode() : -1;
    }
}
