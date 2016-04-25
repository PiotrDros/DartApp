package com.example.piotrdros.dartapp;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView userNameTextView;
    private EditText passwordEditText;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox remeberMeCheckBox;
    private DartApplication dartApplication;


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


        remeberMeCheckBox = (CheckBox) findViewById(R.id.rember_me);

        Button loginButton = (Button) findViewById(R.id.log_in_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        dartApplication = (DartApplication) LoginActivity.this.getApplication();
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
        Boolean remeberMe = remeberMeCheckBox.isChecked();

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
            mAuthTask = new UserLoginTask(userName, password, remeberMe);
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
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserName;
        private final String mPassword;
        private final Boolean mRemberMe;
        private final String appUrl;

        UserLoginTask(String userName, String password, Boolean remberMe) {
            mUserName = userName;
            mPassword = password;
            mRemberMe = remberMe;

            appUrl = Util.getAppUrl(getApplicationContext());
        }


        private String getRequestVerificationToken() throws IOException {
            CookieManager cm = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(cm);
            dartApplication.setCookieManager(cm);

            URL url = new URL("http://" + appUrl + "/Account/Login");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            dartApplication.setCookies(urlConnection.getHeaderField("Set-Cookie"));

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                StringBuilder sb = new StringBuilder("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = reader.readLine()) != null) {
                    sb.append(line + NL);

                    if (line.contains("__RequestVerificationToken")) {
                        Pattern pattern = Pattern.compile(".*value=\"(.*)\".*");
                        Matcher matcher = pattern.matcher(line);
                        matcher.matches();

                        return matcher.group(1);
                    }

                }
            } finally {
                reader.close();
                urlConnection.disconnect();
            }

            return null;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            String value = "";
            BufferedReader in = null;
            try {
                String requestVerificationToken =   getRequestVerificationToken();
                login(requestVerificationToken);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }


        @NonNull
        private void login(String requestVerificationToken) throws IOException {
            CookieManager cm  =  dartApplication.getCookieManager();


            URL url = new URL("http://"+ appUrl +"/Account/Login");
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
//            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
//            urlConnection.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            urlConnection.setRequestProperty("Cookie", dartApplication.getCookies());
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);



            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("UserName", mUserName)
                    .appendQueryParameter("Password", mPassword)
                    .appendQueryParameter("RemeberMe", mRemberMe.toString())
                    .appendQueryParameter("__RequestVerificationToken", requestVerificationToken);
            String query = builder.build().getEncodedQuery();
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();



            urlConnection.connect();
            // we have to touch urlConnection to get somehow cookies from cm... Still don't know why...
            urlConnection.getResponseCode();

            List<HttpCookie> cookies = cm.getCookieStore().getCookies();
            String appCookies = "";
            for (HttpCookie cookie : cookies) {
                appCookies = appCookies + cookie.toString() + ";";
            }
            dartApplication.setCookies( appCookies);

            Log.v("Headers", urlConnection.getHeaderFields().toString())  ;
            Log.v("Headers", cookies.toString())  ;
            Log.v("Headers", dartApplication.getCookies())  ;
            urlConnection.disconnect();
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
}

