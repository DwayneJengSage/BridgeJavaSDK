package org.sagebionetworks.bridge.sdk;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;

abstract class BaseApiCaller {

    private static String host = HostName.getLocal();
    private static String api = "api/";
    private static String version = "v1/";
    private static String baseUrl = host + api + version;

    private static final HttpClient client = HttpClientBuilder.create()
            .setRedirectStrategy(new LaxRedirectStrategy())
            .setRetryHandler(new DefaultHttpRequestRetryHandler(5, true))
            .build();
    private static final Executor exec = Executor.newInstance(client);

    protected static final void setVersion(Version v) {
        switch (v) {
            case V1:
                version = "v1/";
                break;
        }
    }

    protected final void setHost(String hostname) {
        assert HostName.isValidHostName(hostname);
        host = hostname;
    }

    protected final Response get(String url) {
        return get(url, null);
    }

    protected final Response get(String url, Header header) {
        Response response = null;
        try {
            Request request = Request.Get(baseUrl + url);
            if (header != null) {
                request = request.addHeader(header);
            }
            response = exec.execute(request);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    protected final Response post(String url, String json) {
        Response response = null;
        try {
            Request request = Request.Post(baseUrl + url).bodyString(json, ContentType.APPLICATION_JSON);
            response = exec.execute(request);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    protected final String getSessionToken(Response response) {
        if (response == null) {
            throw new IllegalArgumentException("HttpResponse object is null.");
        }
        HttpResponse hr = null;
        try {
            hr = response.returnResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (hr.containsHeader("Bridge-Session")) {
            return hr.getFirstHeader("Bridge-Session").getValue();
        } else {
            throw new AssertionError("Session Token does not exist in this response.");
        }

    }
}
