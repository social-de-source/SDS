package com.example.socialde_source;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onSend(android.view.View view) {
        EditText editText = findViewById(R.id.Message);
        String message = editText.getText().toString();
        Log.d("log",message);
    }
}
