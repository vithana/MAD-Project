package com.example.foodhut;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.foodhut.database.Common;
import com.example.foodhut.database.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeliveryForm extends AppCompatActivity {

    EditText fName, phn, street, city;
    Button btn;
    DatabaseReference db;

    private Order order;
    private ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_form);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait");

        fName = findViewById(R.id.fullname);
        phn = findViewById(R.id.phnNo);
        street = findViewById(R.id.streetAdd);
        city = findViewById(R.id.city);
        btn = findViewById(R.id.delivery_btn);

        final String total = getIntent().getStringExtra("total");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(fName.getText().toString().trim())) {
                    fName.setError("Please Enter Full Name");
                } else if (TextUtils.isEmpty(phn.getText().toString().trim())) {
                    phn.setError("Please Enter Phone Number");
                } else if (TextUtils.isEmpty(street.getText().toString().trim())) {
                    street.setError("Please Enter Street Address");
                } else if (TextUtils.isEmpty(city.getText().toString().trim())) {
                    city.setError("Please Enter City");
                } else {

                    dialog.show();
                    db = FirebaseDatabase.getInstance().getReference("orders");

                    order = new Order();
                    order.setCusName(Common.loggedUser.getUserName());
                    order.setFullName(fName.getText().toString().trim());
                    order.setPhoneNo(phn.getText().toString().trim());
                    order.setStreet(street.getText().toString().trim());
                    order.setCity(city.getText().toString().trim());
                    order.setTotal(Double.parseDouble(total));

                    DatabaseReference ref = db.push();
                    order.setOrderId(ref.getKey());

                    db.child(order.getOrderId()).setValue(order);

                    dialog.dismiss();
                    Toast.makeText(DeliveryForm.this, "Successfully Saved", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(DeliveryForm.this, ConfirmDeatils.class);
                    i.putExtra("orderId", order.getOrderId());
                    i.putExtra("street", order.getStreet());
                    i.putExtra("city", order.getCity());
                    i.putExtra("phn", order.getPhoneNo());
                    i.putExtra("total", String.valueOf(order.getTotal()));
                    startActivity(i);
                }
            }
        });
    }
}
