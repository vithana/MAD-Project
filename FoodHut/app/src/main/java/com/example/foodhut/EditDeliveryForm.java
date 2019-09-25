package com.example.foodhut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodhut.database.Common;
import com.example.foodhut.database.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditDeliveryForm extends AppCompatActivity {

    EditText fName, phn, street, city;
    Button btn;
    DatabaseReference db;

    private Order order;
    private ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delivery_form);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait");
        dialog.show();

        fName = findViewById(R.id.fullnameEdit);
        phn = findViewById(R.id.phnNoEdit);
        street = findViewById(R.id.streetAddEdit);
        city = findViewById(R.id.cityEdit);
        btn = findViewById(R.id.delivery_btn_edit);

        final String orderId = getIntent().getStringExtra("order_id");
        final String total = getIntent().getStringExtra("total");

        db = FirebaseDatabase.getInstance().getReference("orders");

        db.child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                order = dataSnapshot.getValue(Order.class);

                fName.setText(order.getFullName());
                phn.setText(order.getPhoneNo());
                street.setText(order.getStreet());
                city.setText(order.getCity());

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                order = new Order();
                order.setCusName(Common.loggedUser.getUserName());
                order.setFullName(fName.getText().toString().trim());
                order.setPhoneNo(phn.getText().toString().trim());
                order.setStreet(street.getText().toString().trim());
                order.setCity(city.getText().toString().trim());
                order.setTotal(Double.parseDouble(total));
                order.setOrderId(orderId);

                db.child(order.getOrderId()).setValue(order);

                dialog.dismiss();
                Toast.makeText(EditDeliveryForm.this, "Successfully Saved", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(EditDeliveryForm.this, ConfirmDeatils.class);
                i.putExtra("orderId", order.getOrderId());
                i.putExtra("street", order.getStreet());
                i.putExtra("city", order.getCity());
                i.putExtra("phn", order.getPhoneNo());
                i.putExtra("total", String.valueOf(order.getTotal()));
                startActivity(i);
            }
        });
    }
}
