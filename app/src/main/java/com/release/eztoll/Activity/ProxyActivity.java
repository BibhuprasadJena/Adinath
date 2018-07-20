package com.release.eztoll.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.release.eztoll.R;

public class ProxyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy);

        Toast.makeText(getApplicationContext(),"Proximity called",Toast.LENGTH_LONG).show();

    }
}
