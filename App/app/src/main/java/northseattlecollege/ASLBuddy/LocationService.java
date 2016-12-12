package northseattlecollege.ASLBuddy;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.TimeUnit;

/**
 * Created by nathanflint on 11/7/16.
 */
public class LocationService {
    private GoogleApiClient mGoogleApiClient;
    private LocationListener locationListener;
    private LocationRequest locationRequest;

    public LocationService(Context mainActivity,
                           LocationListener locationListener,
                           GoogleApiClient.ConnectionCallbacks connectionCallbacks,
                           GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        mGoogleApiClient = new GoogleApiClient.Builder(mainActivity)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .addApi(LocationServices.API)
                .build();
        this.locationListener = locationListener;
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setSmallestDisplacement(0) //displacement in meters
                .setInterval(TimeUnit.MINUTES.toMillis(5))        // 5 mins, in milliseconds
                .setFastestInterval(TimeUnit.MINUTES.toMillis(5)); // 5 mins, in milliseconds
        mGoogleApiClient.connect();
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, locationListener);
    }

    public void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, locationListener);
        } catch (SecurityException e){
            e.printStackTrace();
        }
    }

    public void destroy() {
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
    }
}
