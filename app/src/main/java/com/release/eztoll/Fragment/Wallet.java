package com.release.eztoll.Fragment;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.release.eztoll.Adaptor.CustomAdapter;
import com.release.eztoll.Database.DBHelper;
import com.release.eztoll.R;
import com.release.eztoll.Retrofit.ConnectivityInterface;
import com.release.eztoll.Util.PrefferenceManager;
import com.release.eztoll.Util.ProgressHandler;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;
import static com.release.eztoll.Util.ApiConstants.ASSIGNED_COMPLAIN;
import static com.release.eztoll.Util.ApiConstants.ASSIGNED_COMPLAIN_BASE_URL;
import static com.release.eztoll.Util.Constants.ASSIGNED_COMPLAIN_REQUEST;

/**
 * Created by Bibhu on 03-Feb-18.
 */


public class Wallet extends Fragment implements ConnectivityInterface.ApiInterafce {

    public Wallet() {
        // Required empty public constructor
    }


    Toolbar mToolbar;
    PrefferenceManager prefferenceManager;
    ListView listView;

    ArrayList<String> Product = new ArrayList<>();
    ArrayList<String> Complain_Info = new ArrayList<>();;
    ArrayAdapter adapter;
    ProgressHandler progressHandler;
    ArrayList<com.release.eztoll.Model.Complain> complains_list = new ArrayList<>();
    TextView no_data;
    TextView total_amt;
    EditText money,money_250,money_500,money_1000;
    Button add_money;

    /**
     * This method is only responsible to initialize the view components.
     */
    private void _init(View view){

        prefferenceManager = PrefferenceManager.getFeaturePreference(getActivity());
        total_amt = (TextView) view.findViewById(R.id.total_amt);
        money = (EditText) view.findViewById(R.id.money);
        money_250 = (EditText) view.findViewById(R.id.money_250);
        money_500 = (EditText) view.findViewById(R.id.money_500);
        money_1000 = (EditText) view.findViewById(R.id.money_1000);
        add_money = (Button) view.findViewById(R.id.add_money);

        if(prefferenceManager!=null){
            int previous_amt = Integer.parseInt(prefferenceManager.getDataFromPref(PrefferenceManager.WALLET_AMOUNT,PrefferenceManager.DEFAULT_WALLET_AMOUNT));
            total_amt.setText("₹ "+previous_amt);
        }

        add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!money.getText().toString().trim().equals("")){
                    progressHandler = new ProgressHandler(getActivity());
                    progressHandler.show();
                    try{
                        showLoader(Integer.parseInt(money.getText().toString().trim()));
                    }catch (Exception e){
                        try{
                            progressHandler.hide();
                        }catch (Exception e1){}
                    }
                }else {
                    Toast.makeText(getActivity(),"Enter some amount.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        money.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if(!money.getText().toString().trim().equals("")){
                        progressHandler = new ProgressHandler(getActivity());
                        progressHandler.show();
                        try{
                            showLoader(Integer.parseInt(money.getText().toString().trim()));
                        }catch (Exception e){
                            try{
                                progressHandler.hide();
                            }catch (Exception e1){}
                        }
                    }else {
                        Toast.makeText(getActivity(),"Enter some amount.",Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });


        money_250.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    progressHandler = new ProgressHandler(getActivity());
                    progressHandler.show();
                    showLoader(250);
            }
        });

        money_500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressHandler = new ProgressHandler(getActivity());
                progressHandler.show();
                showLoader(500);
            }
        });

        money_1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressHandler = new ProgressHandler(getActivity());
                progressHandler.show();
                showLoader(1000);
            }
        });


    }

    public void showLoader(final int adding_amount) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();

                try{
                    progressHandler.hide();


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int previous_amt = Integer.parseInt(prefferenceManager.getDataFromPref(PrefferenceManager.WALLET_AMOUNT,PrefferenceManager.DEFAULT_WALLET_AMOUNT));
                            int current_balance = previous_amt + adding_amount;
                            prefferenceManager.setDataInPref(PrefferenceManager.WALLET_AMOUNT,""+current_balance);
                            total_amt.setText("₹ "+current_balance);
                            money.setText("");
                        }
                    });




                    Date date = Calendar.getInstance().getTime();




                    // Display a date in day, month, year format
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    formatter = new SimpleDateFormat("dd MMM yyyy");
                    String today = formatter.format(date);

                    SQLiteDatabase DB = getActivity().openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);

                        String Qry = "INSERT INTO " + DBHelper.TRANSACTION_TABLE + "(Id,Amount,TranStatus,Date) VALUES" +
                                "('" + 1 + "','" + adding_amount + "','" + 1 + "','"+ today + "')";

                        DB.execSQL(Qry);

                }catch (Exception e){
                    Log.v("BIBHU1","");
                }

            }
        },2000,1000);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wallet, container, false);
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

    /**
     * This API is responsible to get assigned Product against an agent .
     * @param token
     */
    public void getAssignedComplain(String token){
        HashMap<String ,String> inputHashMap = new HashMap<>();
        inputHashMap.put("token",token);

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(ASSIGNED_COMPLAIN,inputHashMap,ASSIGNED_COMPLAIN_REQUEST,this,ASSIGNED_COMPLAIN_BASE_URL);
        connectivityInterface.startApiProcessing();
    }

    /**
     * This method will handle preExecute processing.
     */
    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(getActivity());
        progressHandler.show();

        no_data.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }


    /**
     * This method will handle postExecute processing.
     */
    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if(progressHandler!=null && progressHandler.isShowing()){
            progressHandler.hide();
        }

        if(requestData == ASSIGNED_COMPLAIN_REQUEST){}

    }
}