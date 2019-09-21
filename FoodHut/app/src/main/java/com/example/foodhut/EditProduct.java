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

public class EditProduct extends AppCompatActivity {

    EditText fName, fPrice, fCategory, fPortion;
    Button choose, upload;
    ImageView image;
    StorageReference storageReference;
    DatabaseReference db;

    private StorageTask storageTask;
    private ProgressDialog dialog = null;
    private Product product;
    public Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        dialog = new ProgressDialog(EditProduct.this);
        dialog.setMessage("Please Wait");
        dialog.show();

        String productId = getIntent().getStringExtra("product_id");

        choose = findViewById(R.id.chooseFoodEdit);
        upload = findViewById(R.id.foodEdit);
        image = findViewById(R.id.editFoodImage);
        fName = findViewById(R.id.editFoodName);
        fPrice = findViewById(R.id.editFoodPrice);
        fCategory = findViewById(R.id.editFoodCategory);
        fPortion = findViewById(R.id.editFoodPortion);

        db = FirebaseDatabase.getInstance().getReference("products").child(productId);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                product = dataSnapshot.getValue(Product.class);

                fName.setText(product.getFoodName());
                String price = String.valueOf(product.getPrice());
                fPrice.setText(price);
                fCategory.setText(product.getCategory());
                fPortion.setText(product.getPortion());

                storageReference = FirebaseStorage.getInstance().getReference("products");
                storageReference.child(product.getImageId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        Glide.with(EditProduct.this).load(uri.toString()).centerCrop().into(image);
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
                    Toast.makeText(EditProduct.this, "Upload in progress", Toast.LENGTH_SHORT).show();
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
            imageId = product.getImageId();

        Product p = new Product();

        p.setFoodName(fName.getText().toString().trim());
        double price = Double.parseDouble(fPrice.getText().toString().trim());
        p.setPrice(price);
        p.setCategory(fCategory.getText().toString().trim());
        p.setPortion(fPortion.getText().toString().trim());
        p.setImageId(imageId);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("products");
        ref.child(p.getFoodName()).setValue(p);

        if (!p.getFoodName().equalsIgnoreCase(product.getFoodName())) {
            DatabaseReference temp = FirebaseDatabase.getInstance().getReference("products").child(product.getFoodName());
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
                            Toast.makeText(EditProduct.this, "Successfully Updated", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(EditProduct.this, AdminPanel.class);
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
            Toast.makeText(EditProduct.this, "Successfully Updated", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(EditProduct.this, AdminPanel.class);
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
