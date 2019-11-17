package com.example.cmovlab1;

import com.loopj.android.http.*;

public class ServerRestClient {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String base_url, String endpoint, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setMaxRetriesAndTimeout(0, 2000);
        client.get(getAbsoluteUrl(base_url, endpoint), params, responseHandler);
    }

    public static void post(String base_url, String endpoint, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(base_url, endpoint), params, responseHandler);
    }

    private static String getAbsoluteUrl(String base_url, String endpoint) {
        return "http://" + base_url + ":5000" + endpoint;
    }

}