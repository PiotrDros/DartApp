package com.example.piotrdros.dartapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
            DartApplication dartApplication = (DartApplication) LogActivity.this.getApplication();

            try {
                URL url = new URL("http://78.9.79.62/Game/JsonPoke");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                urlConnection.setRequestProperty("Cookie", dartApplication.getCookies());

                urlConnection.connect();

                Log.v("MyConn2",dartApplication.getCookies()) ;


                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                try {
                    StringBuilder sb = new StringBuilder("");
                    String line = "";
                    String NL = System.getProperty("line.separator");
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + NL);

                    }
                    return sb.toString();
                } finally {
                    reader.close();
                }
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
