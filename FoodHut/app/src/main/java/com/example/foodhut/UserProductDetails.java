package com.example.foodhut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.foodhut.database.Product;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProductDetails extends AppCompatActivity {



    TextView product_name,product_price;
    ImageView product_img;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String productId="";

    FirebaseDatabase database;
    DatabaseReference products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_details);

        database = FirebaseDatabase.getInstance();
        products = FirebaseDatabase.getInstance().getReference("products");

        numberButton = findViewById(R.id.number_button);
        btnCart = findViewById(R.id.crtBtn);


        product_name = findViewById(R.id.product_name);
        product_price = findViewById(R.id.product_price);
        product_img = findViewById(R.id.img_product);


        collapsingToolbarLayout = findViewById(R.id.collapsing);


        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);


        productId = getIntent().getStringExtra("product_name");
        getDetailsProduct(productId);




    }

    private void getDetailsProduct(String productId) {


        products.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Product product = dataSnapshot.getValue(Product.class);

                collapsingToolbarLayout.setTitle(product.getFoodName());
                String temp = String.valueOf(product.getPrice());
                product_price.setText(temp);
                product_name.setText(product.getFoodName());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
