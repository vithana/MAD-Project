package com.example.foodhut;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodhut.database.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUp extends AppCompatActivity {

    EditText email, username, pwd, cPwd;
    Button btn;
    DatabaseReference db;
    User user;

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


                if (pwd.getText().toString().equalsIgnoreCase(cPwd.getText().toString())) {

                    db = FirebaseDatabase.getInstance().getReference().child("User");

                    try {
                        user.setEmail(email.getText().toString().trim());
                        user.setUserName(username.getText().toString().trim());

                        String hashPwd = toHexString(getSHA(pwd.getText().toString()));

                        user.setPassword(hashPwd);

                        db.child("userkey").setValue(user);

                        Toast.makeText(getApplicationContext(), "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Passwords not matched", Toast.LENGTH_SHORT).show();
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
}
