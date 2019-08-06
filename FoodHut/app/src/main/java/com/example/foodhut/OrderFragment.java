package com.example.foodhut;

import android.content.Context;
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

public class OrderFragment extends Fragment {

    private ListView listView;
    String mTitle[] = {"John Derlando", "Tony Stark", "Peter Parker", "Steve Rodgers", "Bruce Wayne", "Robert", "Tom Holland", "Bruce Banner"};
    String mDescription[] = {"Total: Rs.1500", "Total: Rs.2500", "Total: Rs.1300", "Total: Rs.500", "Total: Rs.1700", "Total: Rs.2500", "Total: Rs.2800", "Total: Rs.3500"};
    int images[] = {R.drawable.burger, R.drawable.sandwitch, R.drawable.chicken, R.drawable.stake, R.drawable.cupcake, R.drawable.pizza, R.drawable.spawn, R.drawable.sphagetti};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);

        listView = (ListView)view.findViewById(R.id.admin_list_order);

        MyAdapter adapter = new MyAdapter(getActivity(), mTitle, mDescription, images);
        listView.setAdapter(adapter);

        // now set item click on list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position ==  0) {
                    Toast.makeText( getActivity(), "Facebook Description", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(getActivity(), "Whatsapp Description", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(getActivity(), "Twitter Description", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(getActivity(), "Instagram Description", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(getActivity(), "Youtube Description", Toast.LENGTH_SHORT).show();
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
            super(c, R.layout.order_row, R.id.orderTextView1, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rImgs = imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.order_row, parent, false);
            ImageView images = row.findViewById(R.id.orderImage);
            TextView myTitle = row.findViewById(R.id.orderTextView1);
            TextView myDescription = row.findViewById(R.id.orderTextView2);

            // now set our resources on views
            images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle[position]);
            myDescription.setText(rDescription[position]);

            return row;
        }
    }
}
