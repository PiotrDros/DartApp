package com.example.piotrdros.dartapp;

import android.app.Application;

import org.apache.http.client.CookieStore;

import java.net.CookieManager;


public class DartApplication extends Application {

  private CookieStore cookieStore;
    private String cookies;
    private CookieManager cookieManager;

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }


    public void setCookieManager(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }


    public static final String TAG = "DartApplication";
}
