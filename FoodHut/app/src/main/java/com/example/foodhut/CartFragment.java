package com.example.foodhut;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.foodhut.database.Cart;
import com.example.foodhut.database.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    private Cart cart = null;
    private ProgressDialog dialog = null;
    private double total = 0;
    DatabaseReference db;
    ArrayList<Cart> list = new ArrayList<>();
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        db = FirebaseDatabase.getInstance().getReference("cart").child(Common.loggedUser.getUserName());
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait");

        listView = (ListView)view.findViewById(R.id.user_cart);
        final CartFragment.MyAdapter adapter = new CartFragment.MyAdapter(getActivity(), list);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    db.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            cart = dataSnapshot.getValue(Cart.class);
                            total += cart.getTotal();
                            list.add(cart);
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
                } else {
                    dialog.dismiss();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1, new EmptyCart()).commit();
                }
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
                TextView textview1 = (TextView) view.findViewById(R.id.cartTextView4);
                String s[] = textview1.getText().toString().trim().split(":");
                showPopup(view, s[1]);
            }
        });

        return view;
    }

    class MyAdapter extends ArrayAdapter<Cart> {

        MyAdapter (Context c, ArrayList<Cart> crt) {
            super(c, R.layout.cart_row, crt);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.cart_row, parent, false);
            final ImageView images = row.findViewById(R.id.cartImage);
            final TextView myTitle = row.findViewById(R.id.cartTextView1);
            final TextView total = row.findViewById(R.id.cartTextView2);
            final TextView qty = row.findViewById(R.id.cartTextView3);
            final TextView order = row.findViewById(R.id.cartTextView4);

            final Cart cart1 = getItem(position);

            StorageReference ref;

            if (cart1.getType().equalsIgnoreCase("product")) {
                ref = FirebaseStorage.getInstance().getReference("products");
            } else {
                ref = FirebaseStorage.getInstance().getReference("offers");
            }
            
            ref.child(cart1.getImageId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Glide.with(CartFragment.this).load(uri.toString()).centerCrop().into(images);
                    myTitle.setText(cart1.getFoodName());
                    String v = String.valueOf(cart1.getTotal());
                    total.setText("Total: " + v);
                    String v1 = String.valueOf(cart1.getQuantity());
                    qty.setText("Qty: " + v1);
                    order.setText("ID:" + cart1.getOrderId());

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

    public void showPopup(View view, final String id) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage("Are you sure want to delete this?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference temp = FirebaseDatabase.getInstance().getReference("cart").child(Common.loggedUser.getUserName());
                temp.child(id).removeValue();
                Toast.makeText(getActivity().getApplicationContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1, new CartFragment()).commit();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //getActivity().finish();
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }
}
