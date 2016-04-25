package com.example.piotrdros.dartapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

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

            String  appUrl = Util.getAppUrl(getApplicationContext());
           return Util.getStringFromHttpGet("http://" + appUrl + "/Game/JsonPoke", dartApplication);

    }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONArray array = new JSONArray(s);

                String one = array.getString(0);
                String two = array.getString(1);
                String three =array.getString(2);

                LogActivity.this.textView.setText(String.format("Values: %s, %s, %s", one, two, three));
            } catch (JSONException e) {
                Log.e("LogActivity", e.toString());

                LogActivity.this.textView.setText(e.toString());
            }


        }
    }

}
