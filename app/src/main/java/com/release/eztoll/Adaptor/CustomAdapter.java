package com.release.eztoll.Adaptor;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.release.eztoll.Model.Complain;
import com.release.eztoll.Model.TollModel;
import com.release.eztoll.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<TollModel> {

    private final Activity context;
    TextView toll_name,toll_add,toll_price;
    ArrayList<TollModel> tollModels = new ArrayList<>();

    public CustomAdapter(Activity context, ArrayList<TollModel> tollModels) {
        super(context, R.layout.toll_list, tollModels);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.tollModels = tollModels;

    }

    @Override
    public int getCount() {
        return tollModels.size();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.toll_list, null, true);

        toll_name = (TextView) rowView.findViewById(R.id.toll_name);
        toll_add = (TextView) rowView.findViewById(R.id.toll_add);
        toll_price = (TextView) rowView.findViewById(R.id.toll_price);

        toll_name.setText(tollModels.get(position).getTollname().trim());
        toll_add.setText(tollModels.get(position).getAddress().trim());
        toll_price.setText("₹ "+tollModels.get(position).getPrice().trim());


        return rowView;

    };
}