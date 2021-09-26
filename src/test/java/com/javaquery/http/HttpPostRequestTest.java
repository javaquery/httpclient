package com.javaquery.http;

import com.javaquery.http.handler.HttpRequestHandler;
import com.javaquery.util.io.Console;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author javaquery
 * @since 1.0.0
 */

public class HttpPostRequestTest {

    private static final String SAMPLE_POST_PAYLOAD = "{\"javaquery-key\":\"javaquery-value\"}";

    @Test
    public void performJSONPostRequest(){
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder("PostRequest", HttpMethod.POST)
                .withHost("https://httpbin.org")
                .withEndPoint("/post")
                .withQueryParameter("utm_source", "javaquery")
                .withHttpPayload(new HttpRequest.HttpPayload(StringPool.UTF8, "application/json", SAMPLE_POST_PAYLOAD))
                .build();

        HttpExecutionContext httpExecutionContext = new HttpExecutionContext();
        httpExecutionContext.addHttpRequestHandler(headerHttpRequestHandler());

        HttpClient httpClient = new HttpClient();
        httpClient.execute(httpExecutionContext, httpRequest, httpResponse -> {
            Assertions.assertEquals(200, httpResponse.getStatusCode());

            JSONObject jsonObject = httpResponse.getJSONObjectBody();
            Assertions.assertNotNull(jsonObject);

            JSONObject headers = jsonObject.optJSONObject("headers");
            Assertions.assertEquals("faca52e4-1e15-11ec-9621-0242ac130002", headers.optString("Request-Id"));

            JSONObject args = jsonObject.optJSONObject("args");
            Assertions.assertEquals("javaquery", args.optString("utm_source"));

            String body = jsonObject.optString("data");
            Assertions.assertEquals(SAMPLE_POST_PAYLOAD, body);
            return null;
        });
    }

    @Test
    public void performFormPostRequest(){
        Map<String, String> form = new HashMap<>();
        form.put("form-key", "form-value");

        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder("PostRequest", HttpMethod.POST)
                .withHost("https://httpbin.org")
                .withEndPoint("/post")
                .withQueryParameter("utm_source", "javaquery")
                .withHttpPayload(new HttpRequest.HttpPayload(StringPool.UTF8, "application/x-www-form-urlencoded", form))
                .build();

        HttpExecutionContext httpExecutionContext = new HttpExecutionContext();
        httpExecutionContext.addHttpRequestHandler(headerHttpRequestHandler());

        HttpClient httpClient = new HttpClient();
        httpClient.execute(httpExecutionContext, httpRequest, httpResponse -> {
            Assertions.assertEquals(200, httpResponse.getStatusCode());

            JSONObject jsonObject = httpResponse.getJSONObjectBody();
            Assertions.assertNotNull(jsonObject);

            JSONObject headers = jsonObject.optJSONObject("headers");
            Assertions.assertEquals("faca52e4-1e15-11ec-9621-0242ac130002", headers.optString("Request-Id"));

            JSONObject args = jsonObject.optJSONObject("args");
            Assertions.assertEquals("javaquery", args.optString("utm_source"));

            JSONObject body = jsonObject.optJSONObject("form");
            Assertions.assertEquals("form-value", body.optString("form-key"));
            return null;
        });
    }

    @Test
    public void performPostRequestWithoutPayload(){
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder("PostRequest", HttpMethod.POST)
                .withHost("https://httpbin.org")
                .withEndPoint("/post")
                .withQueryParameter("utm_source", "javaquery")
                .build();

        HttpExecutionContext httpExecutionContext = new HttpExecutionContext();
        httpExecutionContext.addHttpRequestHandler(headerHttpRequestHandler());

        HttpClient httpClient = new HttpClient();
        httpClient.execute(httpExecutionContext, httpRequest, httpResponse -> {
            Assertions.assertEquals(200, httpResponse.getStatusCode());

            JSONObject jsonObject = httpResponse.getJSONObjectBody();
            Assertions.assertNotNull(jsonObject);

            JSONObject headers = jsonObject.optJSONObject("headers");
            Assertions.assertEquals("faca52e4-1e15-11ec-9621-0242ac130002", headers.optString("Request-Id"));

            JSONObject args = jsonObject.optJSONObject("args");
            Assertions.assertEquals("javaquery", args.optString("utm_source"));
            return null;
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
            }

            @Override
            public void onError(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest, Exception exception) {

            }
        };
    }
}
