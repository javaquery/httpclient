package com.javaquery.http;

import com.javaquery.http.exception.HttpException;
import com.javaquery.util.Objects;
import com.javaquery.util.collection.Collections;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Apache http request builder.
 *
 * @author javaquery
 * @since 1.0.0
 */
class ApacheHttpRequestBuilder {
    private HttpUriRequest apacheHttpRequest;
    private final HttpRequest httpRequest;

    /**
     * Instantiates a new Apache http request builder.
     *
     * @param httpRequest the http request
     */
    public ApacheHttpRequestBuilder(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    /**
     * Build http uri request.
     *
     * @return the http uri request
     */
    public HttpUriRequest build(){
        if(httpRequest.getHttpMethod() == HttpMethod.GET){
            apacheHttpRequest = new HttpGet(buildHttpRequestURI());
        }else if(httpRequest.getHttpMethod() == HttpMethod.POST){
            HttpPost httpPost = new HttpPost(buildHttpRequestURI());
            httpPost.setEntity(buildStringEntity());
            apacheHttpRequest = httpPost;
        }else if(httpRequest.getHttpMethod() == HttpMethod.PUT){
            HttpPut httpPut = new HttpPut(buildHttpRequestURI());
            httpPut.setEntity(buildStringEntity());
            apacheHttpRequest = httpPut;
        }else if(httpRequest.getHttpMethod() == HttpMethod.DELETE){
            apacheHttpRequest = new HttpDelete(buildHttpRequestURI());
        }

        if(Collections.nonNullNonEmpty(httpRequest.getHeaders())){
            httpRequest.getHeaders().forEach((key, value) -> apacheHttpRequest.setHeader(key, value));
        }
        return apacheHttpRequest;
    }

    private URI buildHttpRequestURI(){
        try {
            URIBuilder uriBuilder = new URIBuilder(httpRequest.getHost());
            uriBuilder.setPath(httpRequest.getEndPoint());
            if(Collections.nonNullNonEmpty(httpRequest.getQueryParameters())){
                httpRequest.getQueryParameters().forEach(uriBuilder::addParameter);
            }
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new HttpException(e);
        }
    }

    private StringEntity buildStringEntity(){
        if(Objects.nonNull(httpRequest.getHttpPayload())){
            if(Objects.nonNull(httpRequest.getHttpPayload().getPayload())){
                StringEntity stringEntity = new StringEntity(httpRequest.getHttpPayload().getPayload(), httpRequest.getHttpPayload().getCharset());
                httpRequest.withHeader(StringPool.CONTENT_TYPE, httpRequest.getHttpPayload().getContentType());
                return stringEntity;
            }else if(Collections.nonNullNonEmpty(httpRequest.getHttpPayload().getForm())){
                List<NameValuePair> form = new ArrayList<>();
                httpRequest.getHttpPayload().getForm().forEach((key, value) -> form.add(new BasicNameValuePair(key, value)));

                try {
                    return new UrlEncodedFormEntity(form, StringPool.UTF8);
                } catch (UnsupportedEncodingException e) {
                    throw new HttpException(e);
                }
            }
        }
        return null;
    }
}
