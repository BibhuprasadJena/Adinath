package com.release.eztoll.Util;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class JeoFence {

    private static final long GEO_DURATION = 60 * 60 * 1000;
    public static PendingIntent geoFencePendingIntent;

    /**
     *  Create a Geofence
     */
    public static Geofence createGeofence(LatLng latLng, float radius, String geo_fence_request_id) {
        return new Geofence.Builder()
                .setRequestId(geo_fence_request_id)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }


    /**
     *  Create a Geofence Request
     */
    public static GeofencingRequest createGeofenceRequest(Geofence geofence) {
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }


    /**
     *  Creating GeofencePendingIntent.
     */
    public static PendingIntent createGeofencePendingIntent(Context context, int GEOFENCE_REQ_CODE) {
        if (geoFencePendingIntent != null)
            return geoFencePendingIntent;

        Intent intent = new Intent(context, GeofenceTrasitionService.class);
        return PendingIntent.getService(context, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }



}
