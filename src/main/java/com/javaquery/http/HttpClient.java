package com.javaquery.http;

import com.javaquery.http.handler.HttpResponseHandler;
import com.javaquery.http.logging.LoggableHttpRequest;
import com.javaquery.util.collection.Collections;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Http client.
 *
 * @author javaquery
 * @since 1.0.0
 */
public class HttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

    /**
     * Execute.
     *
     * @param httpExecutionContext the http execution context
     * @param httpRequest          the http request
     * @param httpResponseHandler  the http response handler
     */
    public void execute(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest, HttpResponseHandler httpResponseHandler){
        doExecute(httpExecutionContext, httpRequest, httpResponseHandler);
    }

    private void doExecute(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest, HttpResponseHandler httpResponseHandler){
        LoggableHttpRequest loggableHttpRequest = null;
        try {
            beforeRequest(httpExecutionContext, httpRequest);

            HttpUriRequest apacheHttpRequest = new ApacheHttpRequestBuilder(httpRequest).build();
            loggableHttpRequest = new LoggableHttpRequest(httpRequest);
            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            org.apache.http.HttpResponse closeableHttpResponse = closeableHttpClient.execute(apacheHttpRequest);

            HttpResponse httpResponse = new HttpResponse(closeableHttpResponse);
            afterResponse(httpExecutionContext, httpRequest, httpResponse);
            httpResponseHandler.onResponse(httpResponse);
        }catch (Exception exception){
            //exception.printStackTrace();
            LOGGER.error(exception.getMessage(), exception);
            onError(httpExecutionContext, httpRequest, exception);
        }finally {
            LOGGER.info(StringPool.LOG_ACTION, StringPool.LOG_HTTP_REQUEST);
            LOGGER.info(StringPool.LOG_HTTP_REQUEST, loggableHttpRequest);
        }
    }

    private void beforeRequest(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest){
        if(Collections.nonNullNonEmpty(httpExecutionContext.getHttpRequestHandlers())){
            httpExecutionContext.getHttpRequestHandlers().forEach(httpRequestHandler -> httpRequestHandler.beforeRequest(httpExecutionContext, httpRequest));
        }
    }

    private void afterResponse(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest, HttpResponse httpResponse){
        if(Collections.nonNullNonEmpty(httpExecutionContext.getHttpRequestHandlers())){
            httpExecutionContext.getHttpRequestHandlers().forEach(httpRequestHandler -> httpRequestHandler.afterResponse(httpExecutionContext, httpRequest, httpResponse));
        }
    }

    private void onError(HttpExecutionContext httpExecutionContext, HttpRequest httpRequest, Exception exception){
        if(Collections.nonNullNonEmpty(httpExecutionContext.getHttpRequestHandlers())){
            httpExecutionContext.getHttpRequestHandlers().forEach(httpRequestHandler -> httpRequestHandler.onError(httpExecutionContext, httpRequest, exception));
        }
    }
}
