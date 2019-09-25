package com.example.foodhut;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodhut.database.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfirmDeatils extends AppCompatActivity {

    TextView title, add, pNo, tot;
    Button edit, confirm;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_deatils);

        final String order = getIntent().getStringExtra("orderId");
        String street = getIntent().getStringExtra("street");
        String city = getIntent().getStringExtra("city");
        String phn = getIntent().getStringExtra("phn");
        final String total = getIntent().getStringExtra("total");

        title = findViewById(R.id.confTitle);
        add = findViewById(R.id.textView16);
        pNo = findViewById(R.id.textView15);
        tot = findViewById(R.id.textView19);
        edit = findViewById(R.id.button2);
        confirm = findViewById(R.id.confirm);

        title.setText(Common.loggedUser.getUserName());
        add.setText(street);
        pNo.setText(phn);
        tot.setText(total + "LKR");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConfirmDeatils.this, EditDeliveryForm.class);
                i.putExtra("total", total);
                i.putExtra("order_id", order);
                startActivity(i);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance().getReference("cart");
                db.child(Common.loggedUser.getUserName()).removeValue();

                Toast.makeText(ConfirmDeatils.this, "Successfully Confirmed", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ConfirmDeatils.this, Main2Activity.class);
                startActivity(intent);
            }
        });
    }
}
