package com.example.foodhut;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ConfirmDeatils extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_deatils);
    }

    public void navConfDel(View v) {
        Intent i = new Intent(this, DeliveryForm.class);
        startActivity(i);
    }
}
