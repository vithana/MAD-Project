package com.example.foodhut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.foodhut.database.Offer;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserOfferDetails extends AppCompatActivity {


    TextView offer_name,offer_price,offer_description;
    ImageView offer_img;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String offerId="";

    FirebaseDatabase database;
    DatabaseReference offers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_offer_details);

        database = FirebaseDatabase.getInstance();
        offers = FirebaseDatabase.getInstance().getReference("offers");

        numberButton = findViewById(R.id.number_button);
        btnCart = findViewById(R.id.crtBtn);

        offer_description = findViewById(R.id.offer_description);
        offer_name = findViewById(R.id.offer_name);
        offer_price = findViewById(R.id.offer_price);
        offer_img = findViewById(R.id.img_offer);


        collapsingToolbarLayout = findViewById(R.id.collapsing);


        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);


        offerId = getIntent().getStringExtra("offer_name");
        getDetailsOffer(offerId);




    }

    private void getDetailsOffer(String offerId) {

//        offers.child(offerId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Offer offer = dataSnapshot.getValue(Offer.class);
//
//
//                Picasso.get().load(offer.getImageId()).into(offer_img);
//
//                collapsingToolbarLayout.setTitle(offer.getOfferName());
//                offer_price.setText((int) offer.getPrice());
//                offer_name.setText(offer.getOfferName());
//                offer_description.setText(offer.getDescription());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        offers.child(offerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Offer offer = dataSnapshot.getValue(Offer.class);

                collapsingToolbarLayout.setTitle(offer.getOfferName());
                String temp = String.valueOf(offer.getPrice());
                offer_price.setText(temp);
                offer_name.setText(offer.getOfferName());
                offer_description.setText(offer.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
