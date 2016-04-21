package com.example.piotrdros.dartapp;

import android.app.Application;

import org.apache.http.client.CookieStore;


public class DartApplication extends Application {

  private CookieStore cookieStore;

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }
}
