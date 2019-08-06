package com.example.foodhut;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void navigate(View v) {
        Intent i = new Intent(this, SignUp.class);
        startActivity(i);
    }

    public void navHome(View v) {
        textView = (TextView)findViewById(R.id.username);
        String check = textView.getText().toString();

        if (check.equalsIgnoreCase("user")) {
            Intent i = new Intent(this, Main2Activity.class);
            startActivity(i);
        } else if (check.equalsIgnoreCase("admin")) {
            Intent i = new Intent(this, AdminPanel.class);
            startActivity(i);
        }
    }
}
