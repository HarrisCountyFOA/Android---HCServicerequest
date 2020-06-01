package servicesequest.hctx.net.Utility;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class FusedLocationProvider extends LocationCallback implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Activity mActivity;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationListener mLocationListener;

    private static final long LOCATION_UPDATE_INTERVAL = 5000;
    private GoogleApiClient mGoogleApiClient;

    public FusedLocationProvider(Activity activity, LocationListener locationListener) {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationListener = locationListener;
        mActivity = activity;
    }

    /**
     * getLocationAvailability
     */
    private void getLocationAvailability() {
        try {
            mFusedLocationClient.getLocationAvailability().addOnCompleteListener(
                    new OnCompleteListener<LocationAvailability>() {
                        @Override
                        public void onComplete(@NonNull Task<LocationAvailability> task) {
                            FusedLocationProvider.this.onLocationAvailability(task.getResult());
                        }
                    });
        }
        catch (IllegalStateException ex) {
            //Log.w(LOG_TAG, ex.toString());
        }
        catch (SecurityException ex) {
            //Log.w(LOG_TAG, ex.toString());
        }
    }

    @Override
    public void onLocationAvailability(LocationAvailability availability) {
        if (availability.isLocationAvailable()) {
            requestLocationUpdates();
        }
    }

    @Override
    public void onLocationResult(LocationResult result) {
        if (mLocationListener != null) {
            Location location = result.getLastLocation();
            mLocationListener.onLocationChanged(location);
        }
    }

    /**
     * requestLocationUpdates
     */
    private void requestLocationUpdates() throws SecurityException {
        LocationRequest req = LocationRequest.create();
        req.setInterval(LOCATION_UPDATE_INTERVAL);
        mFusedLocationClient.requestLocationUpdates(req, this, Looper.myLooper());
    }

    public void connect() {
        mGoogleApiClient.connect();
    }

    public void disconnect() {
        if (mGoogleApiClient != null && (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected())) {
            if (mFusedLocationClient != null) {
                mFusedLocationClient.removeLocationUpdates(this);
            }

            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);
            getLocationAvailability();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

