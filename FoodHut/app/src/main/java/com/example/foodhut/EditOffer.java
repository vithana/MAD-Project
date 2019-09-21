package com.example.foodhut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodhut.database.Offer;
import com.example.foodhut.database.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class EditOffer extends AppCompatActivity {

    EditText ofName, ofDes, ofPrice;
    Button choose, upload;
    ImageView image;
    StorageReference storageReference;
    DatabaseReference db;

    private StorageTask storageTask;
    private ProgressDialog dialog = null;
    private Offer offer;
    public Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_offer);

        dialog = new ProgressDialog(EditOffer.this);
        dialog.setMessage("Please Wait");
        dialog.show();

        String offerId = getIntent().getStringExtra("offer_id");

        choose = findViewById(R.id.chooseOfferEdit);
        upload = findViewById(R.id.offerEdit);
        image = findViewById(R.id.editOfferImage);
        ofName = findViewById(R.id.offerNameEdit);
        ofDes = findViewById(R.id.offerDescriptionEdit);
        ofPrice = findViewById(R.id.offerPriceEdit);

        db = FirebaseDatabase.getInstance().getReference("offers").child(offerId);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                offer = dataSnapshot.getValue(Offer.class);

                ofName.setText(offer.getOfferName());
                ofDes.setText(offer.getDescription());
                String price = String.valueOf(offer.getPrice());
                ofPrice.setText(price);

                storageReference = FirebaseStorage.getInstance().getReference("offers");
                storageReference.child(offer.getImageId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        Glide.with(EditOffer.this).load(uri.toString()).centerCrop().into(image);
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fileChooser();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (storageTask != null && storageTask.isInProgress()) {
                    Toast.makeText(EditOffer.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                    fileUploader();
                }
            }
        });
    }

    private String getExtention(Uri uri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void fileUploader() {

        String imageId;

        if (uri != null)
            imageId = System.currentTimeMillis() + "." + getExtention(uri);
        else
            imageId = offer.getImageId();

        Offer f = new Offer();

        f.setOfferName(ofName.getText().toString().trim());
        f.setDescription(ofDes.getText().toString().trim());
        double price = Double.parseDouble(ofPrice.getText().toString().trim());
        f.setPrice(price);
        f.setImageId(imageId);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("offers");
        ref.child(f.getOfferName()).setValue(f);

        if (!f.getOfferName().equalsIgnoreCase(offer.getOfferName())) {
            DatabaseReference temp = FirebaseDatabase.getInstance().getReference("offers").child(offer.getOfferName());
            temp.removeValue();
        }

        if (uri != null) {
            StorageReference ref1 = storageReference.child(imageId);

            storageTask = ref1.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            // downloadUrl = taskSnapshot.getDownloadUrl();
                            dialog.dismiss();
                            Toast.makeText(EditOffer.this, "Successfully Updated", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(EditOffer.this, AdminPanel.class);
                            i.putExtra("change", "edit");
                            startActivity(i);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
        } else {
            dialog.dismiss();
            Toast.makeText(EditOffer.this, "Successfully Updated", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(EditOffer.this, AdminPanel.class);
            i.putExtra("change", "edit");
            startActivity(i);
        }
    }

    private void fileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == -1 && data != null && data.getData() != null) {
            uri = data.getData();
            image.setImageURI(uri);
        }
    }
}
