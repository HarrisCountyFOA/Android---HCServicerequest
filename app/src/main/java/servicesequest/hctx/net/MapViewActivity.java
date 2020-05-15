package servicesequest.hctx.net;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import servicesequest.hctx.net.DAL.RequestDataManager;
import servicesequest.hctx.net.DAL.RequestInfoWindowAdapater;
import servicesequest.hctx.net.DAL.ServiceRequestDbContract;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.Request;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ServiceRequestDbHelper dbHelper;
    Marker previousMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Haris Helps");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                if(previousMarker!=null){
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

        dbHelper = new ServiceRequestDbHelper(this);
        RequestDataManager datamanager = new RequestDataManager();
        ArrayList<Request> requests = datamanager.getAllRequests(dbHelper);

        final int size = requests.size();
        for (int i = 0; i < size; i++) {
            Request R1 = requests.get(i);

            LatLng l = new LatLng(Double.parseDouble(R1.Latitude), Double.parseDouble(R1.Longitude));
            Marker mMarker = mMap.addMarker(new MarkerOptions().position(l).title(R1.Image_Name));
            mMarker.setIcon(bitmapDescriptorFromVector(MapViewActivity.this, R.drawable.ic_request_map_marker));
            mMarker.setSnippet(R1._id);
        }

        // Add a marker in Sydney and move the camera
        LatLng hc = new LatLng(29.8172035, -95.4148001);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hc, 9.0f));
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
