package com.example.foodhut;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.foodhut.database.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomerFragment extends Fragment {

    private User user;
    private ProgressDialog dialog = null;
    DatabaseReference db;
    ArrayList<User> list = new ArrayList<>();
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        db = FirebaseDatabase.getInstance().getReference("users");

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait");

        listView = (ListView)view.findViewById(R.id.admin_list_customer);
        final CustomerFragment.MyAdapter adapter = new CustomerFragment.MyAdapter(getActivity(), list);

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                user = dataSnapshot.getValue(User.class);
                list.add(user);
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
                TextView textview1 = (TextView) view.findViewById(R.id.customerTextView1);
                showPopup(view, textview1.getText().toString().trim());
            }
        });

        return view;
    }

    class MyAdapter extends ArrayAdapter<User> {

        MyAdapter (Context c, ArrayList<User> f) {
            super(c, R.layout.customer_row, f);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.customer_row, parent, false);
            TextView myTitle = row.findViewById(R.id.customerTextView1);
            TextView myDescription = row.findViewById(R.id.customerTextView2);

            User u = getItem(position);

            // now set our resources on views
            myTitle.setText(u.getUserName());
            myDescription.setText(u.getEmail());

            dialog.dismiss();
            return row;
        }
    }

    public void showPopup(View v, final String itemId) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                DatabaseReference temp = FirebaseDatabase.getInstance().getReference("users").child(itemId);
                temp.removeValue();
                Toast.makeText(getActivity().getApplicationContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CustomerFragment()).commit();

                return true;
            }
        });

        inflater.inflate(R.menu.list_menu_delete, popup.getMenu());
        popup.setGravity(Gravity.END);
        popup.show();
    }
}
