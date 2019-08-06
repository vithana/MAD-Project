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

public class CustomerFragment extends Fragment {

    private ListView listView;
    String mTitle[] = {"John Derlando", "Tony Stark", "Peter Parker", "Steve Rodgers", "Bruce Wayne", "Robert", "Tom Holland", "Bruce Banner"};
    String mDescription[] = {"john@gmail.com", "tony@gmail.com", "peter@gmail.com", "steve@gmail.com", "bruce@gmail.com", "robert@gmail.com", "tom@gmail.com", "banner@gmail.com"};
    int images[] = {R.drawable.cus1, R.drawable.cus2, R.drawable.cus3, R.drawable.cus4, R.drawable.cus5, R.drawable.cus6, R.drawable.cus7, R.drawable.cus8};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_customer, container, false);

        listView = (ListView)view.findViewById(R.id.admin_list_customer);

        CustomerFragment.MyAdapter adapter = new CustomerFragment.MyAdapter(getActivity(), mTitle, mDescription, images);
        listView.setAdapter(adapter);

        // now set item click on list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position ==  0) {
                    Toast.makeText( getActivity(), "Facebook Description", Toast.LENGTH_SHORT).show();
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
            super(c, R.layout.customer_row, R.id.customerTextView1, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rImgs = imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.customer_row, parent, false);
            ImageView images = row.findViewById(R.id.customerImage);
            TextView myTitle = row.findViewById(R.id.customerTextView1);
            TextView myDescription = row.findViewById(R.id.customerTextView2);

            // now set our resources on views
            images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle[position]);
            myDescription.setText(rDescription[position]);

            return row;
        }
    }
}
