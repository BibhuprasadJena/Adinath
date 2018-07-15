package com.release.eztoll.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.release.eztoll.Activity.UserDashBoard;
import com.release.eztoll.Adaptor.TransactionAdapter;
import com.release.eztoll.Database.DBHelper;
import com.release.eztoll.Model.TransactionModel;
import com.release.eztoll.Netwok.NetworkStatus;
import com.release.eztoll.R;
import com.release.eztoll.Retrofit.ConnectivityInterface;
import com.release.eztoll.Util.CommonUtility;
import com.release.eztoll.Util.PrefferenceManager;
import com.release.eztoll.Util.ProgressHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;
import static com.release.eztoll.Util.ApiConstants.CHANGE_PASSWORD;
import static com.release.eztoll.Util.ApiConstants.CHANGE_PASSWORD_BASE_URL;
import static com.release.eztoll.Util.Constants.CHANGE_PASSWORD_REQUEST;

/**
 * Created by Bibhu on 03-Feb-18.
 */


public class Transactions extends Fragment {

    ProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;
    SQLiteDatabase DB;
    ListView listview;
    TextView no_data;
    ArrayList<TransactionModel> transactionModels = new ArrayList<>();

    public Transactions() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This method is only responsible to initialize the view components.
     */
    private void _init(View view) {
        listview = (ListView) view.findViewById(R.id.listview);
        no_data = (TextView) view.findViewById(R.id.no_data);

        prefferenceManager = PrefferenceManager.getFeaturePreference(getActivity());
        DB = getActivity().openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);


    }

    public void getTransactionDetails(){

        Cursor cursor = DB.rawQuery("SELECT * FROM " + DBHelper.TRANSACTION_TABLE, null);
        int count = cursor.getCount();

        if (count > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TransactionModel transactionModel = new TransactionModel();
                    transactionModel.setPrice(cursor.getString(1).trim());
                    transactionModel.setStatus(cursor.getString(2).trim());
                    transactionModel.setDate(cursor.getString(3).trim());
                    transactionModels.add(transactionModel);

                } while (cursor.moveToNext());

                Collections.reverse(transactionModels);
                TransactionAdapter transactionAdapter = new TransactionAdapter(getActivity(),transactionModels);
                listview.setAdapter(transactionAdapter);

            }
        }else {
            no_data.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transaction, container, false);
        _init(rootView);
        getTransactionDetails();
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }




}
