package com.example.terminal;

import com.loopj.android.http.*;


public class ServerRestClient {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static String serverPublicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAri9PrYxc08w7EclDNZ1CyQhVJhcI87V7ILeTDLVKDusfwRFCmAJx4i78efhkEmh+izJlhOquRH266TPPw/rO9NPPV4y4HtuFtQrsQmkX4wtEy9ETNfrbHxMk1j24+VKzaAnMW4Ky3XRdkAKENopqp3cgxaz/GAMV80ajHXCetTu0enCr3YEoaEOBsRyWalGfbdDeqfxOkAzfYiO8CWbDbzxSZO9vC0BO1gixeBZwMggq6V5hOG43GEwCvmduAE6+LGGC5SgUaLgTfjWNH2y2rshztRJcJC3vKPTgf3+RfzOklGDyeHxugJzYvN01GcDHm2z2QTtark8xyKhhykUoxQIDAQAB-----END PUBLIC KEY-----";

    public static void get(String base_url, String endpoint, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setMaxRetriesAndTimeout(0, 2000);
        client.get(getAbsoluteUrl(base_url, endpoint), params, responseHandler);
    }

    public static void post(String base_url, String endpoint, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(base_url, endpoint), params,responseHandler);
    }

    private static String getAbsoluteUrl(String base_url, String endpoint) {
        return "http://" + base_url + ":5000" + endpoint;
    }

}
