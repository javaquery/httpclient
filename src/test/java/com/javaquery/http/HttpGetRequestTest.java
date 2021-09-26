package com.javaquery.http;

import com.javaquery.http.handler.HttpRequestHandler;
import com.javaquery.http.handler.HttpResponseHandler;
import com.javaquery.util.io.Console;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author javaquery
 * @since 1.0.0
 */

public class HttpGetRequestTest {

    @Test
    public void performGetRequest(){
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder("InventoryUpdate", HttpMethod.GET)
                .withHost("https://www.google.com")
                .build();

        HttpExecutionContext httpExecutionContext = new HttpExecutionContext();

        HttpClient httpClient = new HttpClient();

        httpClient.execute(httpExecutionContext, httpRequest, new HttpResponseHandler<Object>() {
            @Override
            public Object onResponse(HttpResponse httpResponse) {
                Assertions.assertNotNull(httpResponse.getBody());
                return null;
            }

            @Override
            public void onMaxRetryAttempted(HttpResponse httpResponse) {

            }
        });
    }

    @Test
    public void performJSONGetRequest(){
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder("GetRequest", HttpMethod.GET)
                .withHost("https://httpbin.org")
                .withEndPoint("/get")
                .withQueryParameter("utm_source", "javaquery")
                .build();

        HttpExecutionContext httpExecutionContext = new HttpExecutionContext();
        httpExecutionContext.addMetaData("meta", "data");
        httpExecutionContext.addHttpRequestHandler(headerHttpRequestHandler());

        HttpClient httpClient = new HttpClient();
        httpClient.execute(httpExecutionContext, httpRequest, new HttpResponseHandler<Object>() {
            @Override
            public Object onResponse(HttpResponse httpResponse) {
                Assertions.assertEquals(200, httpResponse.getStatusCode());

                JSONObject jsonObject = httpResponse.getJSONObjectBody();
                Assertions.assertNotNull(jsonObject);

                JSONObject headers = jsonObject.optJSONObject("headers");
                Assertions.assertEquals("faca52e4-1e15-11ec-9621-0242ac130002", headers.optString("Request-Id"));

                JSONObject args = jsonObject.optJSONObject("args");
                Assertions.assertEquals("javaquery", args.optString("utm_source"));
                return null;
            }

            @Override
            public void onMaxRetryAttempted(HttpResponse httpResponse) {

            }
        });
    }

    @Test
    public void performRetryGetRequest(){
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder("GetRequest", HttpMethod.GET)
                .withHost("https://httpbin.org")
                .withEndPoint("/status/500")
                .withRetryPolicy(DefaultRetryPolicy.get())
                .build();

        HttpExecutionContext httpExecutionContext = new HttpExecutionContext();

        HttpClient httpClient = new HttpClient();
        httpClient.execute(httpExecutionContext, httpRequest, new HttpResponseHandler<Object>() {
            @Override
            public Object onResponse(HttpResponse httpResponse) {
                Assertions.assertEquals(500, httpResponse.getStatusCode());
                return null;
            }

            @Override
            public void onMaxRetryAttempted(HttpResponse httpResponse) {
                Assertions.assertEquals(500, httpResponse.getStatusCode());
            }
        });
    }

    private HttpRequestHandler headerHttpRequestHandler(){
        return new HttpRequestHandler() {
            @Override
            public void beforeRequest(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest) {
                httpRequest.withHeader("Request-Id", "faca52e4-1e15-11ec-9621-0242ac130002");
            }

            @Override
            public void afterResponse(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest, HttpResponse httpResponse) {
                Assertions.assertNotNull(httpResponse);
                Assertions.assertNotNull(httpExecutionContext.getMetaData());
                Assertions.assertEquals("data", httpExecutionContext.getMeta("meta", null));
                Assertions.assertEquals("defaultValue", httpExecutionContext.getMeta("no-key", "defaultValue"));
            }

            @Override
            public void onError(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest, Exception exception) {

            }
        };
    }
}
