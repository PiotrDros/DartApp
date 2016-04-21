package com.example.piotrdros.dartapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LogActivity extends AppCompatActivity {

    public static final String CONTENT = "content";

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        String content = "Empty Contetnt";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            content = bundle.getString(CONTENT, "Empty Contetnt");
        }

        textView = (TextView) findViewById(R.id.log_text_view);
        textView.setText(content);


        GetJSonTask getJSonTask = new GetJSonTask();
        getJSonTask.execute();
    }

    private class GetJSonTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {

                String appUrl = Util.getAppUrl(getApplicationContext());


                DartApplication dartApplication = (DartApplication) LogActivity.this.getApplication();

                HttpPost httppost = new HttpPost("http://" + appUrl + "/Game/JsonPoke");
                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");
                InputStream inputStream = null;


                DefaultHttpClient client = new DefaultHttpClient();
                client.setCookieStore(dartApplication.getCookieStore());

                HttpResponse response = null;
                response = client.execute(httppost);

                HttpEntity entity = response.getEntity();

                inputStream = entity.getContent();
                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                return sb.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            LogActivity.this.textView.setText(s);
        }
    }

}
