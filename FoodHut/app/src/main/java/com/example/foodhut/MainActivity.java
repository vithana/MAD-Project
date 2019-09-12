package com.example.foodhut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {

    EditText email, pwd;
    Button btn;
    DatabaseReference db;
    User user;

    private TextView textView;
    private ProgressDialog dialog = null;
    SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.login_username);
        pwd = findViewById(R.id.pass);
        btn = findViewById(R.id.login_btn);

        user = new User();

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db = FirebaseDatabase.getInstance().getReference("users");

                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("Please Wait");

                try {
                    String hashPwd = toHexString(getSHA(pwd.getText().toString()));

                    dialog.show();

                    logIn(email.getText().toString(), hashPwd);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
         }
        );
    }

    public void navigate(View v) {
        Intent i = new Intent(this, SignUp.class);
        startActivity(i);
    }

    public void navHome(View v) {
        textView = (TextView)findViewById(R.id.username);
        String check = textView.getText().toString();

        if (check.equalsIgnoreCase("admin")) {
            Intent i = new Intent(this, Main2Activity.class);
            startActivity(i);
        } else if (check.equalsIgnoreCase("admin")) {
            Intent i = new Intent(this, AdminPanel.class);
            startActivity(i);
        }
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

    private void logIn(final String id,final String password) {
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(id).exists()){
                    dialog.dismiss();
                    if (!id.isEmpty()){
                        User user1=dataSnapshot.child(id).getValue(User.class);
                        if (user1.getPassword().equals(password)){
                            Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();

                            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_name", user1.getUserName());
                            editor.commit();

                            if (user1.getUserName().equalsIgnoreCase("admin")) {
                                Intent intphto =new Intent(getApplicationContext(), AdminPanel.class);
                                startActivity(intphto);
                            } else  {
                                Intent intphto =new Intent(getApplicationContext(), Main2Activity.class);
                                startActivity(intphto);
                            }

                        }else {
                            Toast.makeText(getApplicationContext(),"Password Incorrect",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"User is not registered",Toast.LENGTH_LONG).show();
                    }

                }else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"User is not registered",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
