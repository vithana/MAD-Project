package com.example.foodhut;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.foodhut.database.Common;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

public class AdminPanel extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.admin_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CustomerFragment()).commit();
            navigationView.setCheckedItem(R.id.admin_customer);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_panel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Are you sure want to logout?");

            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Common.loggedUser = null;
                    Toast.makeText(AdminPanel.this, "Successfully Logged out", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AdminPanel.this, MainActivity.class);
                    startActivity(intent);
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

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.admin_customer) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CustomerFragment()).commit();
        } else if (id == R.id.admin_product) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductFragment()).commit();
        } else if (id == R.id.admin_order) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderFragment()).commit();
        } else if (id == R.id.admin_offer) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OfferFragment()).commit();
        } else if (id == R.id.admin_add_offer) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdminAddNewOffer()).commit();
        } else if (id == R.id.admin_add_product) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdminAddNewProduct()).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
