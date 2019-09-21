package com.example.foodhut;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.foodhut.database.Common;
import com.example.foodhut.database.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileFragment extends Fragment {

    TextView uName;
    EditText fullName, email, address, tel;
    Button btn;
    DatabaseReference db;

    private ProgressDialog dialog = null;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        uName = view.findViewById(R.id.name);
        fullName = view.findViewById(R.id.customerName);
        email = view.findViewById(R.id.email);
        address = view.findViewById(R.id.address);
        tel = view.findViewById(R.id.telephone);
        btn = view.findViewById(R.id.update_btn);

        uName.setText(Common.loggedUser.getUserName());
        fullName.setText(Common.loggedUser.getFullName());
        email.setText(Common.loggedUser.getEmail());
        address.setText(Common.loggedUser.getAddress());
        tel.setText(Common.loggedUser.getTelephone());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Please Wait");

                db = FirebaseDatabase.getInstance().getReference("users").child(Common.loggedUser.getUserName());

                user = new User();

                user.setUserName(Common.loggedUser.getUserName());
                user.setEmail(email.getText().toString().trim());
                user.setFullName(fullName.getText().toString().trim());
                user.setTelephone(tel.getText().toString().trim());
                user.setAddress(address.getText().toString().trim());
                user.setPassword(Common.loggedUser.getPassword());

                db.setValue(user);
                Common.loggedUser = user;

                dialog.dismiss();
                Toast.makeText(getActivity().getApplicationContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1, new UserProfileFragment()).commit();
            }
        });
        
        return view;
    }
}
