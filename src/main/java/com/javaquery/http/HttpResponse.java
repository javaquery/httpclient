package com.javaquery.http;

import com.javaquery.util.Objects;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The type Http response.
 *
 * @author javaquery
 * @since 1.0.0
 */
public class HttpResponse {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpResponse.class);
    private int statusCode;
    private String body;
    private org.apache.http.HttpResponse apacheHttpResponse;

    /**
     * Instantiates a new Http response.
     *
     * @param httpResponse the http response
     */
    public HttpResponse(org.apache.http.HttpResponse httpResponse) {
        apacheHttpResponse = httpResponse;
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
