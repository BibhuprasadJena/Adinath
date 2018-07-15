package com.release.eztoll.Adaptor;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.release.eztoll.Model.TollModel;
import com.release.eztoll.Model.TransactionModel;
import com.release.eztoll.R;

import java.util.ArrayList;

public class TransactionAdapter extends ArrayAdapter<TransactionModel> {

    private final Activity context;
    TextView price,date,details;
    ArrayList<TransactionModel> transactionModels = new ArrayList<>();

    public TransactionAdapter(Activity context, ArrayList<TransactionModel> transactionModels) {
        super(context, R.layout.transaction_list, transactionModels);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.transactionModels = transactionModels;

    }

    @Override
    public int getCount() {
        return transactionModels.size();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.transaction_list, null, true);

        details = (TextView) rowView.findViewById(R.id.details);
        date = (TextView) rowView.findViewById(R.id.date);
        price = (TextView) rowView.findViewById(R.id.price);

        if(transactionModels.get(position).getStatus().trim().equals("1")){
            details.setText("Money Added To Wallet");
            price.setText("+₹ "+transactionModels.get(position).getPrice());
            price.setTextColor(Color.parseColor("#0F8A17"));
        }else{
            details.setText("Money Paid");
            price.setText("-₹ "+transactionModels.get(position).getPrice());
            price.setTextColor(Color.RED);
        }

        date.setText(transactionModels.get(position).getDate());
        return rowView;

    };
}