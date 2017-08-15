package com.atm.client;

import com.google.api.client.http.*;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;

import javax.inject.Inject;
import java.util.Map;

public class RequestBuilder {
    private HttpRequest lastRequest;
    private HttpResponse lastResponse;
    private HttpRequestFactory httpRequestFactory;

    @Inject
    public RequestBuilder(HttpRequestFactory httpRequestFactory) {
        this.httpRequestFactory = httpRequestFactory;
    }

    public HttpRequestFactory getHttpRequestFactory() {
        return httpRequestFactory;
    }

    public HttpRequest getLastRequest() {
        return lastRequest;
    }

    public HttpResponse getLastResponse() {
        return lastResponse;
    }

    protected HttpResponse execute(String method, String url, Map<String,Object> headers, Object payload) throws Exception {
        lastRequest = httpRequestFactory.buildRequest(method, new GenericUrl(url),
                payload != null ? new JsonHttpContent(new JacksonFactory(), payload) : new EmptyContent());
        lastRequest.setHeaders(httpHeadersFrom(headers));
        lastResponse = lastRequest.execute();

        return lastResponse;
    }

    private static HttpHeaders httpHeadersFrom(Map<String,Object> headersMap) {
        HttpHeaders headers = new HttpHeaders();
        headers.setUnknownKeys(headersMap);

        return headers;
    }

}
