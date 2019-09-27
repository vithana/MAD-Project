package com.example.foodhut;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class EmptyCart extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_empty_cart, container, false);

        Toast.makeText(getActivity().getApplicationContext(), "Cart is empty!", Toast.LENGTH_SHORT).show();

        return view;
    }
}
