# JavaQuery HttpClient

Welcome to the JavaQuery http client
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.javaquery/httpclient/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.javaquery/httpclient)

# Why http client?

The http client is created on top of Apache http client (org.apache.httpcomponents:httpclient:4.5) with the goal of providing extensive 
functionality on any http request.

# Features

The http client has the following features to make your http request experience rich. 

### Request handlers

Create common or request specific HttpRequestHandler (implement com.javaquery.http.handler.HttpRequestHandler) and you can add following interceptor in the request.
- `beforeRequest(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest);`
- `afterResponse(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest, HttpResponse httpResponse);`
- `onError(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest, Exception exception);`

### Response handlers

Same as request handler, create common or request specific HttpResponseHandler(com.javaquery.http.handler.HttpResponseHandler) and you can add following action on your response.
- `R onResponse(HttpResponse httpResponse);`
- `onMaxRetryAttempted(HttpResponse httpResponse);`

### Response

- You can make other request in after response of request handler and update final response to process.
- Get response as `JSONObject` or `JSONArray` directly.

### Retry policy

Define your custom RetryPolicy for response specific status code or based on server response. Implement `RetryPolicy.RetryCondition` and `RetryPolicy.BackoffStrategy`.

### Examples

I have created working http request examples in test cases. Explore all test cases to understand how this http client works.
