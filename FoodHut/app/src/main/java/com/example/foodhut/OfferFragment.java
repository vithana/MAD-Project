package com.example.foodhut;

import android.content.Context;
import android.content.Intent;
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

public class OfferFragment extends Fragment {

    private ListView listView;
    String mTitle[] = {"Offer 1", "Offer 2", "Offer 3", "Offer 4", "Offer 5", "Offer 6", "Offer 7", "Offer 8"};
    String mDescription[] = {"Price: Rs.1500", "Price: Rs.2500", "Price: Rs.1300", "Price: Rs.500", "Price: Rs.1700", "Price: Rs.2500", "Price: Rs.2800", "Price: Rs.3500"};
    int images[] = {R.drawable.burger, R.drawable.sandwitch, R.drawable.chicken, R.drawable.stake, R.drawable.cupcake, R.drawable.pizza, R.drawable.spawn, R.drawable.sphagetti};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
<<<<<<< HEAD

        View view = inflater.inflate(R.layout.fragment_offer, container, false);

        listView = (ListView)view.findViewById(R.id.admin_list_offer);

        OfferFragment.MyAdapter adapter = new OfferFragment.MyAdapter(getActivity(), mTitle, mDescription, images);
        listView.setAdapter(adapter);

        // now set item click on list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position ==  0) {
                    Intent i = new Intent(getActivity(), AdminAddNewOffer.class);
                    startActivity(i);
                }
            }
        });

        return view;
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String rTitle[];
        String rDescription[];
        int rImgs[];

        MyAdapter (Context c, String title[], String description[], int imgs[]) {
            super(c, R.layout.offer_row, R.id.offerTextView1, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rImgs = imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.offer_row, parent, false);
            ImageView images = row.findViewById(R.id.offerImage);
            TextView myTitle = row.findViewById(R.id.offerTextView1);
            TextView myDescription = row.findViewById(R.id.offerTextView2);

            // now set our resources on views
            images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle[position]);
            myDescription.setText(rDescription[position]);

            return row;
        }
=======
        return inflater.inflate(R.layout.fragment_offer, container, false);

>>>>>>> dev
    }
}
