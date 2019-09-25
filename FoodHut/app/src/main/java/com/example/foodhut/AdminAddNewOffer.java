package com.example.foodhut;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.foodhut.database.Offer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class AdminAddNewOffer extends Fragment {

    EditText ofName, ofDes, ofPrice;
    Button choose, upload;
    ImageView img;
    StorageReference storageReference;
    DatabaseReference db;

    private StorageTask storageTask;
    private ProgressDialog dialog = null;
    private Offer offer;
    public Uri uri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_add_new_offer, container, false);
        storageReference = FirebaseStorage.getInstance().getReference("offers");
        db = FirebaseDatabase.getInstance().getReference("offers");

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait");

        offer = new Offer();

        choose = view.findViewById(R.id.chooseOffer);
        upload = view.findViewById(R.id.offerAdd);
        img = view.findViewById(R.id.offerImage);
        ofName = view.findViewById(R.id.offerName);
        ofDes = view.findViewById(R.id.offerDescription);
        ofPrice = view.findViewById(R.id.offerPrice);

        choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fileChooser();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (storageTask != null && storageTask.isInProgress()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                    fileUploader();
                }
            }
        });

        return view;
    }

    private String getExtention(Uri uri) {

        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void fileUploader() {

        String imageId = System.currentTimeMillis() + "." + getExtention(uri);
        offer.setOfferName(ofName.getText().toString().trim());

        double price = Double.parseDouble(ofPrice.getText().toString().trim());

        offer.setPrice(price);
        offer.setDescription(ofDes.getText().toString().trim());
        offer.setImageId(imageId);

        db.child(offer.getOfferName()).setValue(offer);

        StorageReference ref = storageReference.child(imageId);

        storageTask = ref.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        // downloadUrl = taskSnapshot.getDownloadUrl();
                        dialog.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
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
            img.setImageURI(uri);
        }
    }
}
