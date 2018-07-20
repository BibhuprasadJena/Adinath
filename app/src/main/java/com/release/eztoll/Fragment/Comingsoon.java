package com.release.eztoll.Fragment;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.release.eztoll.Adaptor.TransactionAdapter;
import com.release.eztoll.Database.DBHelper;
import com.release.eztoll.Model.TransactionModel;
import com.release.eztoll.R;
import com.release.eztoll.Util.PrefferenceManager;
import com.release.eztoll.Util.ProgressHandler;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Bibhu on 03-Feb-18.
 */


public class Comingsoon extends Fragment {

    ProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;
    SQLiteDatabase DB;
    ListView listview;
    TextView no_data;
    ArrayList<TransactionModel> transactionModels = new ArrayList<>();

    public Comingsoon() {
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

        prefferenceManager = PrefferenceManager.getFeaturePreference(getActivity());
        DB = getActivity().openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.comingsoon, container, false);
        _init(rootView);
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
