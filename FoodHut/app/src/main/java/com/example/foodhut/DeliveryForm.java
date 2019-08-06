package com.example.foodhut;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DeliveryForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_form);
    }

    public void navCon(View v) {
        Intent i = new Intent(this, ConfirmDeatils.class);
        startActivity(i);
    }
}
