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
    TextView price,date,details,vowner,vno;
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
        vowner = (TextView) rowView.findViewById(R.id.vowner);
        vno = (TextView) rowView.findViewById(R.id.vno);


        details.setText(transactionModels.get(position).getToll_name());
        date.setText(transactionModels.get(position).getDate());
        vowner.setText(transactionModels.get(position).getVowner());
        vno.setText("VL no: "+transactionModels.get(position).getVno());


        if(transactionModels.get(position).getStatus().trim().equals("1")){
            price.setText("+₹ "+transactionModels.get(position).getPrice());
            price.setTextColor(Color.parseColor("#0F8A17"));
            vowner.setText("");
            vno.setText("");
            vowner.setVisibility(View.GONE);
            vno.setVisibility(View.GONE);

            details.setText("Money Added To Wallet");
            details.setTextColor(Color.parseColor("#0F8A17"));
        }else{
            price.setText("-₹ "+transactionModels.get(position).getPrice());
            price.setTextColor(Color.RED);
        }


        return rowView;

    };
}