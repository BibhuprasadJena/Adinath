package com.release.eztoll.Fragment;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.release.eztoll.Database.DBHelper;
import com.release.eztoll.Model.TransactionModel;
import com.release.eztoll.R;
import com.release.eztoll.Util.PrefferenceManager;
import com.release.eztoll.Util.ProgressHandler;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Bibhu on 03-Feb-18.
 */


public class Config extends Fragment {

    ProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;
    SQLiteDatabase DB;
    ListView listview;
    TextView no_data;
    Switch config_value;
    ArrayList<TransactionModel> transactionModels = new ArrayList<>();

    public Config() {
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
        config_value = (Switch)view.findViewById(R.id.config_value);

        if(prefferenceManager.getDataFromPref(PrefferenceManager.CONFIG,PrefferenceManager.DEFAULT_CONFIG).trim().equals("1")){
            config_value.setChecked(true);
        }else{
            config_value.setChecked(false);
        }

        config_value.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                    if(isChecked){
                        prefferenceManager.setDataInPref(PrefferenceManager.CONFIG,"1");
                    }else {
                        prefferenceManager.setDataInPref(PrefferenceManager.CONFIG,"0");
                    }
            }
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.config, container, false);
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
