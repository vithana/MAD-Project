package com.example.foodhut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodhut.database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUp extends AppCompatActivity {

    EditText email, username, pwd, cPwd;
    Button btn;
    DatabaseReference db;
    User user;

    private ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        pwd = findViewById(R.id.pwd);
        cPwd = findViewById(R.id.cPwd);
        btn = findViewById(R.id.signup_btn);

        user = new User();

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (TextUtils.isEmpty(email.getText().toString().trim())) {
                    email.setError("Please Enter Email!");
                } else if (TextUtils.isEmpty(username.getText().toString().trim())) {
                    username.setError("Please Enter Username");
                } else if (TextUtils.isEmpty(pwd.getText().toString())) {
                    pwd.setError("Please Enter Password");
                } else if (TextUtils.isEmpty(cPwd.getText().toString().trim())) {
                    cPwd.setError("Please Enter Confirm Password");
                } else {
                    if (pwd.getText().toString().equalsIgnoreCase(cPwd.getText().toString())) {

                        db = FirebaseDatabase.getInstance().getReference("users");

                        dialog = new ProgressDialog(SignUp.this);
                        dialog.setMessage("Please Wait");

                        try {
                            user.setEmail(email.getText().toString().trim());
                            user.setUserName(username.getText().toString().trim());

                            String hashPwd = toHexString(getSHA(pwd.getText().toString()));

                            user.setPassword(hashPwd);

                            dialog.show();

                            submit(user.getUserName());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Passwords not matched", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void navigate(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    private void submit(final String id) {
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(id).exists()){
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Username is already exists", Toast.LENGTH_SHORT).show();
                } else {
                    db.child(user.getUserName()).setValue(user);
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
