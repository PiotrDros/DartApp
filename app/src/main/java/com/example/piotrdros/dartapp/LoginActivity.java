package com.example.piotrdros.dartapp;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.CookieManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    static class Pair {
        String cookie;
        String token;
        String ASPXAUTH;

        @Override
        public String toString() {
            return String.format(".ASPXAUTH=%s, __RequestVerificationToken=%s", token, ASPXAUTH);
        }
    }

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView userNameTextView;
    private EditText passwordEditText;
    private View mProgressView;
    private View mLoginFormView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        userNameTextView = (AutoCompleteTextView) findViewById(R.id.user_name);


        passwordEditText = (EditText) findViewById(R.id.password);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button loginButton = (Button) findViewById(R.id.log_in_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        userNameTextView.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the login attempt.
        String userName = userNameTextView.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        // Check for a valid userName.
        if (TextUtils.isEmpty(userName)) {
            userNameTextView.setError(getString(R.string.error_field_required));
            focusView = userNameTextView;
            cancel = true;
        } else if (!isUserNameValid(userName)) {
            userNameTextView.setError(getString(R.string.error_invalid_email));
            focusView = userNameTextView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(userName, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUserNameValid(String email) {
        //TODO: Replace this with your own logic
        return email != null && !email.isEmpty();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.piotrdros.dartapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.piotrdros.dartapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserName;
        private final String mPassword;

        UserLoginTask(String userName, String password) {
            mUserName = userName;
            mPassword = password;
        }


        private Pair getRequestVerificationToken(HttpClient client) throws IOException {
            Pair pair = new Pair();
            String value = "";

            HttpGet get = new HttpGet("http://78.9.79.62/Account/Login");
            HttpResponse response = client.execute(get);


            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            response.getEntity().getContent()));

            StringBuilder sb = new StringBuilder("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);

                if (line.contains("__RequestVerificationToken")) {
                    Pattern pattern = Pattern.compile(".*value=\"(.*)\".*");
                    Matcher matcher = pattern.matcher(line);
                    matcher.matches();

                    value = matcher.group(1);
                    break;
                }

            }
            in.close();


          pair.cookie =  getCookie( response, "__RequestVerificationToken");

            pair.token = value;

            String cookie = CookieManager.getInstance().getCookie("http://78.9.79.62/Account/Login");
            if (cookie == null) {
                cookie = "null";
            }


            return  pair;
        }

        private String getCookie(HttpResponse response, String cookieName) {
            Header[] cookieHeader = response.getHeaders("Set-Cookie");
            if (cookieHeader.length > 0) {
                String cookies = cookieHeader[0].getValue();
                String[] split = cookies.split(";");
                for (String s : split) {
                    Log.v("Ciasteczko", s);
                    if (s.startsWith(cookieName)) {
                        String[] split2 =  s.split("=");
                      return   split2[1];
//                        Log.v("Cookies", split2[1]);
                    }
                }



            }
            return null;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            //     HttpPost httpPost = new HttpPost("http://www.example.com");
            String value = "";
            BufferedReader in = null;
            try {

                DefaultHttpClient client = new DefaultHttpClient();


                Pair pair = getRequestVerificationToken(client);
                login(pair, client);




                HttpPost httppost = new HttpPost("http://78.9.79.62/Game/JsonPoke");




                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");
                InputStream inputStream = null;
                String result = null;


                CookieStore cs = new BasicCookieStore();
                Cookie cookie1 = new BasicClientCookie( "__RequestVerificationToken", pair.cookie);
//                cs.addCookie(cookie1);
                Cookie cookie2 = new BasicClientCookie( ".ASPXAUTH", "B53DC4430560E38B3DCA55980D73D9DFD222A0AEE5210EEF0D5FF68EFA8EED0F79288243433A81226CAC97FB0994B60772AF6C18056583406E70906760B4F9155D04FC5A169771D84B2B18A1F7C2D66629EA1437E98AD4B234BADB1320A6279FC4A2DCBB4E27DE78CF2A3CCB10C0B023C6DCD4860F007E6B8C2E8F16EE2A8E2DAD083CF69873BBF43CF12AFCAD324039");
                cs.addCookie(cookie2);
//                client.setCookieStore(cs);
                CookieStore cookieStore = client.getCookieStore();
                Log.v("CookieStore", cookieStore.toString());
//                Log.v("CookieStore", pair.toString());
                HttpResponse response = client.execute(httppost);
                                client = new DefaultHttpClient();
                client.setCookieStore(cookieStore);

                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder  sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();

                showLogActivity(result);




            } catch (Exception e) {
                e.printStackTrace();
            }



            return true;
        }


        @NonNull
        private void login(Pair pair, HttpClient client) throws IOException {
            if (client == null) {
                client = new DefaultHttpClient();
            }

            HttpResponse response;
            BufferedReader in;
            StringBuilder sb;
            String line;
            String NL;
            String result;HttpPost request = new HttpPost(
                    "http://78.9.79.62/Account/Login");
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
//                postParameters.add(new BasicNameValuePair("UserName", "Piotr+Dro%C5%9B"));


            postParameters.add(new BasicNameValuePair("UserName", mUserName));
            postParameters.add(new BasicNameValuePair("Password", mPassword));
            postParameters.add(new BasicNameValuePair("RemeberMe", "false"));
            postParameters.add(new BasicNameValuePair("__RequestVerificationToken", pair.token));


            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                    postParameters, "UTF-8");


            Log.v("formatEntity", "formatEntity:" + mUserName);

            request.setEntity(formEntity);
            response = client.execute(request);

            pair.ASPXAUTH =  getCookie( response, ".ASPXAUTH");

            in = new BufferedReader(
                    new InputStreamReader(
                            response.getEntity().getContent()));

            sb = new StringBuilder("");
            line = "";
            NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            result = sb.toString();
            Log.v("LoginDupa", sb.toString());



        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                passwordEditText.setError(getString(R.string.error_incorrect_password));
                passwordEditText.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void showLogActivity(String s) {
        Intent intent = new Intent(getApplicationContext(), LogActivity.class);
        Bundle b = new Bundle();
        b.putString(LogActivity.CONTNET, s); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
    }
}

