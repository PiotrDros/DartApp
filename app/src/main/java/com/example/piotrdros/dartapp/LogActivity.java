package com.example.piotrdros.dartapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class LogActivity extends AppCompatActivity {

    public static final String CONTNET = "content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);


        Bundle b = getIntent().getExtras();
        String content= b.getString(CONTNET, "Empty Contetnt");

//        savedInstanceState

        TextView textView = (TextView) findViewById(R.id.log_text_view);
        textView.setText(content);

    }
}
