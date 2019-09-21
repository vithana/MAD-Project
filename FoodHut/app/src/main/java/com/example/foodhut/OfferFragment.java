package com.example.foodhut;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.foodhut.database.Offer;
import com.example.foodhut.database.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class OfferFragment extends Fragment {

    private Offer offer;
    private ProgressDialog dialog = null;
    DatabaseReference db;
    ArrayList<Offer> list = new ArrayList<>();
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offer, container, false);
        db = FirebaseDatabase.getInstance().getReference("offers");

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait");

        listView = (ListView)view.findViewById(R.id.admin_list_offer);

        final OfferFragment.MyAdapter adapter = new OfferFragment.MyAdapter(getActivity(), list);

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                offer = dataSnapshot.getValue(Offer.class);
                list.add(offer);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dialog.show();
        listView.setAdapter(adapter);

        // now set item click on list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textview1 = (TextView) view.findViewById(R.id.offerTextView1);
                showPopup(view, textview1.getText().toString().trim());
            }
        });

        return view;
    }

    class MyAdapter extends ArrayAdapter<Offer> {

        MyAdapter (Context c, ArrayList<Offer> of) {
            super(c, R.layout.offer_row, of);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.offer_row, parent, false);
            final ImageView images = row.findViewById(R.id.offerImage1);
            final TextView myTitle = row.findViewById(R.id.offerTextView1);
            final TextView myDescription = row.findViewById(R.id.offerTextView2);

            final Offer f = getItem(position);

            StorageReference ref = FirebaseStorage.getInstance().getReference("offers");

            ref.child(f.getImageId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Glide.with(OfferFragment.this).load(uri.toString()).centerCrop().into(images);
                    myTitle.setText(f.getOfferName());
                    String m = String.valueOf(f.getPrice());
                    myDescription.setText("Price Rs. " + m);

                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

            return row;
        }
    }

    public void showPopup(View v, final String itemId) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.edit) {
                    Intent intent = new Intent(getActivity(), EditOffer.class);
                    intent.putExtra("offer_id", itemId);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.delete) {
                    DatabaseReference temp = FirebaseDatabase.getInstance().getReference("offers").child(itemId);
                    temp.removeValue();
                    Toast.makeText(getActivity().getApplicationContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OfferFragment()).commit();
                } else {
                    return false;
                }
                return true;
            }
        });

        inflater.inflate(R.menu.list_menu, popup.getMenu());
        popup.setGravity(Gravity.END);
        popup.show();
    }
}
