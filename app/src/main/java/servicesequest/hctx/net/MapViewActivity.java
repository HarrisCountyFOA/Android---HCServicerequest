package servicesequest.hctx.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;

import servicesequest.hctx.net.Async.RequestListAsync;
import servicesequest.hctx.net.DAL.RequestInfoWindowAdapater;
import servicesequest.hctx.net.DAL.ServiceRequestDbContract;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.Request;
import servicesequest.hctx.net.Utility.AppPreferences;
import servicesequest.hctx.net.Utility.Utils;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ServiceRequestDbHelper dbHelper;
    Marker previousMarker;
    BottomNavigationView navigation;
    AppPreferences _appPrefs;
    HashMap<String, String> markerLocation;
    static final float COORDINATE_OFFSET = 0.00002f;
    int MAX_NUMBER_OF_MARKERS = 5;
    boolean loadData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        _appPrefs = new AppPreferences(this, "UserProfile");

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        markerLocation = new HashMap<String, String>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        FloatingActionButton btnList = findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ti = new Intent(getApplicationContext(), MainActivity.class);
                ti.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(ti);
            }
        });

        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ti = new Intent(getApplicationContext(), NewRequestActivity.class);
                startActivityForResult(ti, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            LoadData();
            loadData = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mapmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new RequestInfoWindowAdapater(MapViewActivity.this));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (previousMarker != null) {
                    previousMarker.setIcon(bitmapDescriptorFromVector(MapViewActivity.this, R.drawable.ic_request_map_marker));
                }
                marker.setIcon(bitmapDescriptorFromVector(MapViewActivity.this, R.drawable.ic_request_map_marker_active));
                previousMarker = marker;
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                Intent ai = new Intent(getApplicationContext(), RequestDetailsActivity.class);
                String id = marker.getSnippet();

                if (id != null && !id.equals("")) {
                    ai.putExtra(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_ID, id);
                }
                startActivity(ai);
            }
        });

        LoadData();
    }

    private void LoadData() {
        if (mMap != null && loadData) {
            RequestListAsync asyncTask = new RequestListAsync(this, new RequestListAsync.OnTaskCompleted() {
                @Override
                public void taskCompleted(List<Request> results) {
                    try {
                        if (results.size() != 0) {
                            final int size = results.size();
                            for (int i = 0; i < size; i++) {
                                Request R1 = results.get(i);

                                if (!markerLocation.containsKey(R1._id)) {
                                    String[] mark = coordinateForMarker(Double.parseDouble(R1.Latitude), Double.parseDouble(R1.Longitude));

                                    if (mark != null && mark.length > 0) {
                                        LatLng l = new LatLng(Double.parseDouble(mark[0]), Double.parseDouble(mark[1]));

                                        Marker mMarker = mMap.addMarker(new MarkerOptions().position(l).title(R1.Image_Name));
                                        mMarker.setIcon(bitmapDescriptorFromVector(MapViewActivity.this, R.drawable.ic_request_map_marker));
                                        mMarker.setSnippet(R1._id);
                                        markerLocation.put(R1._id, mark[0] + "," + mark[1]);
                                    }
                                }
                            }

                        } else if (results.size() == 0) {
                            Toast.makeText(getApplicationContext(), "No saved reports", Toast.LENGTH_LONG).show();
                        }

                        LatLng hc = new LatLng(29.8172035, -95.4148001);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hc, 9.0f));

                    } catch (Exception ex) {
                        String Info = "There was a problem getting your reports. Please try again later.<br><br>";
                        Utils.customPopMessge(getApplicationContext(), "Error", Info + ex.getMessage(), "error");
                    }
                }
            });
            asyncTask.execute();
        }
        loadData = true;
    }

    private String[] coordinateForMarker(double latitude, double longitude) {

        String[] location = new String[2];
        location[0] = latitude + "";
        location[1] = longitude + "";

        for (int i = 0; i <= MAX_NUMBER_OF_MARKERS; i++) {

            if (mapAlreadyHasMarkerForLocation((latitude + i
                    * COORDINATE_OFFSET)
                    + "," + (longitude + i * COORDINATE_OFFSET))) {

                // If i = 0 then below if condition is same as upper one. Hence, no need to execute below if condition.
                if (i == 0)
                    continue;

                if (mapAlreadyHasMarkerForLocation((latitude - i
                        * COORDINATE_OFFSET)
                        + "," + (longitude - i * COORDINATE_OFFSET))) {

                    continue;

                } else {
                    location[0] = latitude - (i * COORDINATE_OFFSET) + "";
                    location[1] = longitude - (i * COORDINATE_OFFSET) + "";
                    break;
                }

            } else {
                location[0] = latitude + (i * COORDINATE_OFFSET) + "";
                location[1] = longitude + (i * COORDINATE_OFFSET) + "";
                break;
            }
        }

        return location;
    }

    private boolean mapAlreadyHasMarkerForLocation(String location) {
        return (markerLocation.containsValue(location));
    }


    @Override
    protected void onResume() {
        super.onResume();
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        LoadData();
    }

    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent hi = new Intent(getApplicationContext(), MainActivity.class);
                    hi.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(hi);
                    return true;
                case R.id.navigation_dashboard:
                    Intent ti = new Intent(getApplicationContext(), TermsOfUseActivity.class);
                    ti.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(ti);
                    return true;
                case R.id.navigation_notifications:

                    Boolean profileSet = _appPrefs.getBoolean("ProfileSet");

                    if (profileSet) {
                        Intent pi = new Intent(getApplicationContext(), ProfileList.class);
                        pi.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(pi);
                    } else {
                        Intent pi = new Intent(getApplicationContext(), ProfileActivity.class);
                        pi.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(pi);
                    }

                    return true;
                case R.id.navigation_contacts:
                    Intent ai = new Intent(getApplicationContext(), ContactActivity.class);
                    ai.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(ai);
                    return true;
            }
            return false;
        }
    };

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
