package com.release.eztoll.Fragment;

/**
 * Created by MUVI on 03-Feb-18.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonArray;
import com.release.eztoll.Adaptor.CustomAdapter;
import com.release.eztoll.Model.TollModel;
import com.release.eztoll.R;
import com.release.eztoll.Retrofit.ConnectivityInterface;
import com.release.eztoll.Util.Constants;
import com.release.eztoll.Util.GeofenceTrasitionService;
import com.release.eztoll.Util.JeoFence;
import com.release.eztoll.Util.PrefferenceManager;
import com.release.eztoll.Util.ProgressHandler;
import com.release.eztoll.Util.ProxymityAlert;
import com.release.eztoll.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.LOCATION_SERVICE;
import static com.release.eztoll.Util.ApiConstants.ASSIGNED_COMPLAIN;
import static com.release.eztoll.Util.ApiConstants.ASSIGNED_COMPLAIN_BASE_URL;
import static com.release.eztoll.Util.Constants.ASSIGNED_COMPLAIN_REQUEST;
import static com.release.eztoll.Util.JeoFence.createGeofenceRequest;
import com.google.android.gms.location.GeofencingApi;


public class DashBoard extends Fragment implements ConnectivityInterface.ApiInterafce, OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    ProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;
    FloatingActionButton add_customer;
    ListView listview;
    TextView no_data;
    ImageView search;
    LocationManager locationManager;
    public static final int REQUEST_LOCATION = 001;
    PlaceAutocompleteFragment placeAutocompleteFragment;

    GoogleApiClient googleApiClient;
    double LATITUDE;
    double LONGITUDE;

    LocationRequest locationRequest;
    LocationSettingsRequest.Builder locationSettingsRequest;
    PendingResult<LocationSettingsResult> pendingResult;
    EditText source, dest;
    Button list, map;
    LinearLayout map_layout;

    ArrayList<TollModel> tollModels = new ArrayList<>();
    CustomAdapter customAdapter;
    int count = 0;
    private GeofencingClient geofencingClient;


    public DashBoard() {
        // Required empty public constructor
    }

    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dashboard, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        geofencingClient = LocationServices.getGeofencingClient(getActivity());

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();



        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        } else {
            mEnableGps();
        }

        source = (EditText) rootView.findViewById(R.id.source);
        dest = (EditText) rootView.findViewById(R.id.dest);
        map = (Button) rootView.findViewById(R.id.map);
        list = (Button) rootView.findViewById(R.id.list);
        search = (ImageView) rootView.findViewById(R.id.search);

        listview = (ListView) rootView.findViewById(R.id.listview);
        map_layout = (LinearLayout) rootView.findViewById(R.id.map_layout);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview.setVisibility(View.GONE);
                map_layout.setVisibility(View.VISIBLE);
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tollModels.size() < 1) {
                    Toast.makeText(getActivity(), "There is nothing to see in toll listing.", Toast.LENGTH_SHORT).show();
                    return;
                }

                listview.setVisibility(View.VISIBLE);
                map_layout.setVisibility(View.GONE);
            }
        });


        dest.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    validateSourceDestination();
                    return true;
                }
                return false;
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateSourceDestination();
            }
        });

     /*   placeAutocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
        placeAutocompleteFragment.setFilter(autocompleteFilter);

        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Toast.makeText(getActivity(),place.getName().toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getActivity(),status.toString(),Toast.LENGTH_SHORT).show();

            }
        });*/


        // Inflate the layout for this fragment
        return rootView;
    }


    public void validateSourceDestination() {
        if (source.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Enter source loaction.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dest.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Enter your destination.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressHandler = new ProgressHandler(getActivity());
        progressHandler.show();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                timer.purge();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getTollDetails();
                        progressHandler.hide();
                    }
                });


            }
        }, 2000, 30000);
    }

    public void getTollDetails() {

        tollModels.clear();

        customAdapter = new CustomAdapter(getActivity(), tollModels);
        listview.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();


        try {
            JSONObject jsonObject = new JSONObject(Util.tollList);
            JSONArray jsonArray = jsonObject.getJSONArray("TollNameList");

            for (int i = 0; i < jsonArray.length(); i++) {

                TollModel tollModel = new TollModel();

                tollModel.setTollname(jsonArray.getJSONObject(i).optString("TollName"));
                tollModel.setAddress(jsonArray.getJSONObject(i).optString("Location"));
                tollModel.setPrice(jsonArray.getJSONObject(i).optString("VechiclePrice"));
                tollModel.setLatitide(jsonArray.getJSONObject(i).optString("Latitude"));
                tollModel.setLongitude(jsonArray.getJSONObject(i).optString("Longitude"));

                tollModels.add(tollModel);

            }

            customAdapter = new CustomAdapter(getActivity(), tollModels);
            listview.setAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();

            DrawMarker();

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
    /*public void getAssignedComplain(String token){
        HashMap<String ,String> inputHashMap = new HashMap<>();
        inputHashMap.put("tag",token);

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(ASSIGNED_COMPLAIN,inputHashMap,ASSIGNED_COMPLAIN_REQUEST,this,ASSIGNED_COMPLAIN_BASE_URL);
        connectivityInterface.startApiProcessing();
    }*/

    /**
     * This method will handle preExecute processing.
     */
    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(getActivity());
        progressHandler.show();

        no_data.setVisibility(View.GONE);
        listview.setVisibility(View.VISIBLE);
    }


    /**
     * This method will handle postExecute processing.
     */
    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if (progressHandler != null && progressHandler.isShowing()) {
            progressHandler.hide();
        }

        if (requestData == ASSIGNED_COMPLAIN_REQUEST) {
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }


    private void mEnableGps() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        mLocationSetting();
    }

    public void mLocationSetting() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);

        locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        mResult();

    }

    public void mResult() {
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest.build());
        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();


                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {

                            status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.


                        break;
                }
            }

        });
    }

    //callback method
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made

                        getLocation();

                        break;
                    case Activity.RESULT_CANCELED:
                        getActivity().finish();
                        break;
                    default:
                        break;
                }
                break;

        }
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        try {

//            Toast.makeText(getActivity(),"lat="+location.getLatitude()+"+\n"+"lang="+location.getLongitude(),Toast.LENGTH_SHORT).show();

            LATITUDE = location.getLatitude();
            LONGITUDE = location.getLongitude();




//            Geocoder geocoder;
//            List<Address> addresses;
//            geocoder = new Geocoder(getActivity(), Locale.getDefault());

//            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            if (count == 0) {
                /**
                 * Start Geofencing.
                 */

               /* LatLng demo = new LatLng(19.82192576 ,85.84128353);
                Geofence geofence = JeoFence.createGeofence( demo, 5.0f ,"first_one");
                GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
                addGeofence(geofenceRequest);*/


                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                addLocationAlert(location.getLatitude(),location.getLongitude());


                // Add a marker in Sydney and move the camera
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
                count = 1;
            } else {
                DrawMarker();
            }
        } catch (Exception e) {

            Log.v("BIBHU123","Exception ="+e.toString());
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.v("BIBHU11","api client connected");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);

    }

    public void DrawMarker() {
        mMap.clear();

        LatLng demo = new LatLng(19.82192576 ,85.84128353);
        drawGeofence(demo);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            LatLng latLng_own_location = new LatLng(LATITUDE, LONGITUDE);
//            addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            mMap.addMarker(new MarkerOptions().position(latLng_own_location).title("")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker)));

        } catch (Exception e) {
            e.printStackTrace();
        }


        for (int i = 0; i < tollModels.size(); i++) {
            try {
                double latitude = Double.parseDouble(tollModels.get(i).getLatitide());
                double longitude = Double.parseDouble(tollModels.get(i).getLongitude());

//                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                LatLng latLng = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(latLng).title(tollModels.get(i).getAddress()).icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker)));

                if (count == 0) {
                    count = 1;
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 6.0f));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }




    }




    // Draw Geofence circle on GoogleMap
    private Circle geoFenceLimits;
    private void drawGeofence(LatLng latLng) {



        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .strokeColor(Color.argb(50, 70,70,70))
                .fillColor( Color.argb(100, 150,150,150) )
                .radius(5.0f);
                 mMap.addCircle( circleOptions );
    }

    @Override
    public void onResult(@NonNull Status status) {

    }

    /*******************************************************************************************/


    @SuppressLint("MissingPermission")
    private void addLocationAlert(double lat, double lng){

            String key = ""+lat+"-"+lng;
            Geofence geofence = getGeofence(lat, lng, key);
            geofencingClient.addGeofences(getGeofencingRequest(geofence),
                    getGeofencePendingIntent())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(),
                                        "Location alter has been added",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(),
                                        "Location alter could not be added",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    }


    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(getActivity(), GeofenceTrasitionService.class);
        return PendingIntent.getService(getActivity(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private Geofence getGeofence(double lat, double lang, String key) {
        return new Geofence.Builder()
                .setRequestId(key)
                .setCircularRegion(lat, lang, 5)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(10000)
                .build();
    }










}