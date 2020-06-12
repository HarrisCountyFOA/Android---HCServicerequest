package servicesequest.hctx.net;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.$Gson$Preconditions;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.BubbleLayout;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;


import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import servicesequest.hctx.net.DAL.RequestDataManager;
import servicesequest.hctx.net.DAL.RequestListAdapter;
import servicesequest.hctx.net.DAL.RequestListInfoWindowAdapter;
import servicesequest.hctx.net.DAL.ServiceRequestDbContract;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.Request;
import servicesequest.hctx.net.Utility.AppPreferences;

import static com.mapbox.mapboxsdk.style.expressions.Expression.eq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ANCHOR_BOTTOM;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class map extends AppCompatActivity implements
        OnMapReadyCallback, MapboxMap.OnMapClickListener {

    BottomNavigationView navigation;
    AppPreferences _appPrefs;
    private MarkerViewManager markerViewManager;
    private MarkerView markerView;
    private SymbolManager symbolManager;
    private Symbol symbol;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private static final String GEOJSON_SOURCE_ID = "GEOJSON_SOURCE_ID";
    private static final String MARKER_IMAGE_ID = "MARKER_IMAGE_ID";
    private static final String MARKER_LAYER_ID = "MARKER_LAYER_ID";
    private static final String CALLOUT_LAYER_ID = "CALLOUT_LAYER_ID";
    private static final String PROPERTY_SELECTED = "selected";
    private static final String PROPERTY_NAME = "_id";
    private static final String PROPERTY_CAPITAL = "Image_Name";
    private GeoJsonSource source;
    private FeatureCollection featureCollection;
    private RecyclerView recyclerView;
    private AnimatorSet animatorSet;
    private static final long CAMERA_ANIMATION_TIME = 1950;
    private ValueAnimator markerAnimator;
    private boolean markerSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        recyclerView = findViewById(R.id.rv_on_top_of_map);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        _appPrefs = new AppPreferences(this, "UserProfile");

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);

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
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                mapboxMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        new LoadGeoJsonDataTask(map.this).execute();
                        mapboxMap.addOnMapClickListener(map.this);

                    }
                });
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                mapboxMap.setStyle(Style.DARK, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        new LoadGeoJsonDataTask(map.this).execute();
                        mapboxMap.addOnMapClickListener(map.this);

                    }
                });
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        new LoadGeoJsonDataTask(map.this).execute();
                        mapboxMap.addOnMapClickListener(map.this);

                    }
                });
                break;
        }


    }


    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));
    }

    /**
     * Sets up all of the sources and layers needed for this example
     *
     * @param collection the FeatureCollection to set equal to the globally-declared FeatureCollection
     */
    public void setUpData(final FeatureCollection collection) {
        featureCollection = collection;
        if (mapboxMap != null) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    setupSource(style);
                    setUpImage(style);
                    setUpMarkerLayer(style);
                    // setUpInfoWindowLayer(style);
                }
            });
        }
    }

    /**
     * Adds the GeoJSON source to the map
     */
    private void setupSource(@NonNull Style loadedStyle) {
        source = new GeoJsonSource(GEOJSON_SOURCE_ID, featureCollection);
        loadedStyle.addSource(source);
    }

    /**
     * Adds the marker image to the map for use as a SymbolLayer icon
     */
    private void setUpImage(@NonNull Style loadedStyle) {
        loadedStyle.addImage(MARKER_IMAGE_ID, bitmapDescriptorFromVector(map.this, R.drawable.ic_request_map_marker));
    }

    private Bitmap bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return bitmap;
    }

    /**
     * Updates the display of data on the map after the FeatureCollection has been modified
     */
    private void refreshSource() {
        if (source != null && featureCollection != null) {
            source.setGeoJson(featureCollection);
        }
    }

    /**
     * Setup a layer with maki icons, eg. west coast city.
     */
    private void setUpMarkerLayer(@NonNull Style loadedStyle) {
        loadedStyle.addLayer(new SymbolLayer(MARKER_LAYER_ID, GEOJSON_SOURCE_ID)
                .withProperties(
                        iconImage(MARKER_IMAGE_ID),
                        iconAllowOverlap(true),
                        iconOffset(new Float[]{0f, -8f})
                ));

        loadedStyle.addSource(new GeoJsonSource("selected-marker"));

        loadedStyle.addLayer(new SymbolLayer("selected-marker-layer", "selected-marker")
                .withProperties(PropertyFactory.iconImage(MARKER_IMAGE_ID),
                        iconAllowOverlap(true),
                        iconOffset(new Float[]{0f, -9f})));
    }

    private void setupRecyclerView(List<Request> results) {

        RequestListInfoWindowAdapter ad = new RequestListInfoWindowAdapter(results, getApplicationContext(), new RequestListInfoWindowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Request item) {

                Intent ai = new Intent(getApplicationContext(), RequestDetailsActivity.class);
                if (item != null) {
                    ai.putExtra(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_ID, item._id);
                }
                startActivity(ai);
            }
        });
        RecyclerView vi = recyclerView;
        vi.setAdapter(ad);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(RecyclerView.VERTICAL);

        vi.setLayoutManager(manager);
        recyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Setup a layer with Android SDK call-outs
     * <p>
     * name of the feature is used as key for the iconImage
     * </p>
     */
    private void setUpInfoWindowLayer(@NonNull Style loadedStyle) {
        loadedStyle.addLayer(new SymbolLayer(CALLOUT_LAYER_ID, GEOJSON_SOURCE_ID)
                .withProperties(
                        /* show image with id title based on the value of the name feature property */
                        iconImage("{_id}"),

                        /* set anchor of icon to bottom-left */
                        iconAnchor(ICON_ANCHOR_BOTTOM),

                        /* all info window and marker image to appear at the same time*/
                        iconAllowOverlap(true),

                        /* offset the info window to be above the marker */
                        iconOffset(new Float[]{-2f, -28f})
                )
/* add a filter to show only when selected feature property is true */
                .withFilter(eq((get(PROPERTY_SELECTED)), literal(true))));
    }

    /**
     * This method handles click events for SymbolLayer symbols.
     * <p>
     * When a SymbolLayer icon is clicked, we moved that feature to the selected state.
     * </p>
     *
     * @param screenPoint the point on screen clicked
     */
    private boolean handleClickIcon(PointF screenPoint) {

        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, MARKER_LAYER_ID);

        Style style = mapboxMap.getStyle();
        if (style != null) {
            final SymbolLayer selectedMarkerSymbolLayer =
                    (SymbolLayer) style.getLayer("selected-marker-layer");

            GeoJsonSource source = style.getSourceAs("selected-marker");


            if (!features.isEmpty()) {
                String name = features.get(0).getStringProperty(PROPERTY_NAME);
                List<Feature> featureList = featureCollection.features();
                if (featureList != null) {
                    for (int i = 0; i < featureList.size(); i++) {
                        if (featureList.get(i).getStringProperty(PROPERTY_NAME).equals(name)) {
                            if (featureSelectStatus(i)) {
                                setFeatureSelectState(featureList.get(i), false);
                                if (source != null) {
                                    source.setGeoJson(FeatureCollection.fromFeatures(
                                            new Feature[]{Feature.fromGeometry(featureCollection.features().get(i).geometry())}));
                                    deselectMarker(selectedMarkerSymbolLayer);
                                }
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                setSelected(i);
                                if (source != null) {
                                    source.setGeoJson(FeatureCollection.fromFeatures(
                                            new Feature[]{Feature.fromGeometry(featureCollection.features().get(i).geometry())}));
                                    selectMarker(selectedMarkerSymbolLayer);
                                }
                            }
                        }
                    }
                }
                return true;
            } else {
                if(featureCollection != null)
                {
                    for (Feature singleFeature : featureCollection.features()) {
                        singleFeature.addBooleanProperty(PROPERTY_SELECTED, false);
                    }
                }
                recyclerView.setVisibility(View.GONE);
                deselectMarker(selectedMarkerSymbolLayer);
                return false;
            }
        }

        return false;
    }

    /**
     * Set a feature selected state.
     *
     * @param index the index of selected feature
     */
    private void setSelected(int index) {
        if (featureCollection.features() != null) {
            Feature feature = featureCollection.features().get(index);
            setFeatureSelectState(feature, true);

            String id = feature.getStringProperty("_id");

            String name = feature.getStringProperty("Image_Name");

            String adds = feature.getStringProperty("FullAddress");

            String image = feature.getStringProperty("Image");

            Request R = new Request();
            R._id = id;
            R.Image_Name = name;
            R.FullAddress = adds;
            if (!image.equals("")) {
                String[] split = image.substring(1, image.length() - 1).split(", ");
                byte[] array = new byte[split.length];
                for (int i = 0; i < split.length; i++) {
                    array[i] = Byte.parseByte(split[i]);
                }

                R.Image = array;

            } else {
                R.Image = null;
            }

            String date = feature.getStringProperty("DateTime");
            R.DateTime = date;
            List<Request> r = new ArrayList<>();
            r.add(R);

            animateCameraToSelection(feature);
            setupRecyclerView(r);

            refreshSource();
        }
    }

    private void selectMarker(final SymbolLayer iconLayer) {
        markerAnimator = new ValueAnimator();
        markerAnimator.setObjectValues(1f, 2f);
        markerAnimator.setDuration(300);
        markerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                iconLayer.setProperties(
                        PropertyFactory.iconSize((float) animator.getAnimatedValue())
                );
            }
        });
        markerAnimator.start();
        markerSelected = true;
    }

    private void deselectMarker(final SymbolLayer iconLayer) {
        if(markerAnimator != null) {
            markerAnimator.setObjectValues(2f, 1f);
            markerAnimator.setDuration(300);
            markerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    iconLayer.setProperties(
                            PropertyFactory.iconSize((float) animator.getAnimatedValue())
                    );
                }
            });
            markerAnimator.start();
            markerSelected = false;
        }
    }


    /**
     * Animate camera to a feature.
     *
     * @param feature the feature to animate to
     */
    private void animateCameraToSelection(Feature feature, double newZoom) {
        CameraPosition cameraPosition = mapboxMap.getCameraPosition();

        if (animatorSet != null) {
            animatorSet.cancel();
        }

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                createLatLngAnimator(cameraPosition.target, convertToLatLng(feature)),
                createZoomAnimator(cameraPosition.zoom, newZoom)
        );
        animatorSet.start();
    }

    private void animateCameraToSelection(Feature feature) {
        double zoom = 17;
        animateCameraToSelection(feature, zoom);
    }

    private LatLng convertToLatLng(Feature feature) {
        Point symbolPoint = (Point) feature.geometry();
        return new LatLng(symbolPoint.latitude(), symbolPoint.longitude());
    }

    private Animator createLatLngAnimator(LatLng currentPosition, LatLng targetPosition) {
        ValueAnimator latLngAnimator = ValueAnimator.ofObject(new LatLngEvaluator(), currentPosition, targetPosition);
        latLngAnimator.setDuration(CAMERA_ANIMATION_TIME);
        latLngAnimator.setInterpolator(new FastOutSlowInInterpolator());
        latLngAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLng((LatLng) animation.getAnimatedValue()));
            }
        });
        return latLngAnimator;
    }

    private Animator createZoomAnimator(double currentZoom, double targetZoom) {
        ValueAnimator zoomAnimator = ValueAnimator.ofFloat((float) currentZoom, (float) targetZoom);
        zoomAnimator.setDuration(CAMERA_ANIMATION_TIME);
        zoomAnimator.setInterpolator(new FastOutSlowInInterpolator());
        zoomAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mapboxMap.moveCamera(CameraUpdateFactory.zoomTo((Float) animation.getAnimatedValue()));
            }
        });
        return zoomAnimator;
    }

    /**
     * Helper class to evaluate LatLng objects with a ValueAnimator
     */
    private static class LatLngEvaluator implements TypeEvaluator<LatLng> {

        private final LatLng latLng = new LatLng();

        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    }


    /**
     * Selects the state of a feature
     *
     * @param feature the feature to be selected.
     */
    private void setFeatureSelectState(Feature feature, boolean selectedState) {
        if (feature.properties() != null) {
            feature.properties().addProperty(PROPERTY_SELECTED, selectedState);
            refreshSource();
        }
    }

    /**
     * Checks whether a Feature's boolean "selected" property is true or false
     *
     * @param index the specific Feature's index position in the FeatureCollection's list of Features.
     * @return true if "selected" is true. False if the boolean property is false.
     */
    private boolean featureSelectStatus(int index) {
        if (featureCollection == null) {
            return false;
        }
        return featureCollection.features().get(index).getBooleanProperty(PROPERTY_SELECTED);
    }

    /**
     * Invoked when the bitmaps have been generated from a view.
     */
    public void setImageGenResults(final HashMap<String, Bitmap> imageMap) {
        if (mapboxMap != null) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    style.addImages(imageMap);
                }
            });
        }
    }

    /**
     * AsyncTask to load data from the assets folder.
     */
    private static class LoadGeoJsonDataTask extends AsyncTask<Void, Void, FeatureCollection> {

        private final WeakReference<map> activityRef;

        LoadGeoJsonDataTask(map activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        protected FeatureCollection doInBackground(Void... params) {
            map activity = activityRef.get();

            if (activity == null) {
                return null;
            }

            ServiceRequestDbHelper dbHelper = new ServiceRequestDbHelper(activity);
            RequestDataManager datamanager = new RequestDataManager();
            List<Request> _requestlist = datamanager.getAllRequests(dbHelper);

            List<Feature> symbolLayerIconFeatureList = new ArrayList<>();

            final int size = _requestlist.size();
            for (int i = 0; i < size; i++) {
                Request R1 = _requestlist.get(i);
                Gson gson = new Gson();
                JsonObject obj = new JsonObject();
                obj.addProperty("_id", R1._id);
                obj.addProperty("Image_Name", R1.Image_Name);
                obj.addProperty("FullAddress", R1.FullAddress);
                if (R1.Image != null) {
                    obj.addProperty("Image", Arrays.toString(R1.Image));
                } else {
                    obj.addProperty("Image", "");
                }

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    java.util.Date date = dateFormat.parse(R1.DateTime);
                    obj.addProperty("Date", new SimpleDateFormat("MMM dd yyyy").format(date));
                    obj.addProperty("Time", new SimpleDateFormat("hh.mm aa").format(date));
                    obj.addProperty("DateTime", R1.DateTime);
                } catch (ParseException e) {
                    obj.addProperty("Date", "");
                    obj.addProperty("Time", "");
                }


                symbolLayerIconFeatureList.add(Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(R1.Longitude), Double.parseDouble(R1.Latitude)), obj, R1._id));
            }


            return FeatureCollection.fromFeatures(symbolLayerIconFeatureList);
        }

        @Override
        protected void onPostExecute(FeatureCollection featureCollection) {
            super.onPostExecute(featureCollection);
            map activity = activityRef.get();
            if (featureCollection == null || activity == null) {
                return;
            }

// This example runs on the premise that each GeoJSON Feature has a "selected" property,
// with a boolean value. If your data's Features don't have this boolean property,
// add it to the FeatureCollection 's features with the following code:
            for (Feature singleFeature : featureCollection.features()) {
                singleFeature.addBooleanProperty(PROPERTY_SELECTED, false);
            }

            activity.setUpData(featureCollection);
            // new GenerateViewIconTask(activity).execute(featureCollection);
        }
    }

//    /**
//     * AsyncTask to generate Bitmap from Views to be used as iconImage in a SymbolLayer.
//     * <p>
//     * Call be optionally be called to update the underlying data source after execution.
//     * </p>
//     * <p>
//     * Generating Views on background thread since we are not going to be adding them to the view hierarchy.
//     * </p>
//     */
//    private static class GenerateViewIconTask extends AsyncTask<FeatureCollection, Void, HashMap<String, Bitmap>> {
//
//        private final HashMap<String, View> viewMap = new HashMap<>();
//        private final WeakReference<map> activityRef;
//        private final boolean refreshSource;
//
//        GenerateViewIconTask(map activity, boolean refreshSource) {
//            this.activityRef = new WeakReference<>(activity);
//            this.refreshSource = refreshSource;
//        }
//
//        GenerateViewIconTask(map activity) {
//            this(activity, false);
//        }
//
//        @SuppressWarnings("WrongThread")
//        @Override
//        protected HashMap<String, Bitmap> doInBackground(FeatureCollection... params) {
//            map activity = activityRef.get();
//            if (activity != null) {
//                HashMap<String, Bitmap> imagesMap = new HashMap<>();
//                LayoutInflater inflater = LayoutInflater.from(activity);
//
//                FeatureCollection featureCollection = params[0];
//
//                for (Feature feature : featureCollection.features()) {
//
//                    View bubbleLayout = (View) LayoutInflater.from(activity)
//                            .inflate(R.layout.activity_request_infolist, null);
//
//                    String id = feature.getStringProperty("_id");
//
//                    String name = feature.getStringProperty("Image_Name");
//                    TextView titleTextView = bubbleLayout.findViewById(R.id.txvName);
//                    titleTextView.setText(name);
//
//                    String adds = feature.getStringProperty("FullAddress");
//                    TextView descriptionTextView = bubbleLayout.findViewById(R.id.txvaddress);
//                    descriptionTextView.setText(adds);
//
//                    String image = feature.getStringProperty("Image");
//                    ImageView avatar = (ImageView) bubbleLayout.findViewById(R.id.avatar);
//
//                    if (image != "") {
//                        String[] split = image.substring(1, image.length() - 1).split(", ");
//                        byte[] array = new byte[split.length];
//                        for (int i = 0; i < split.length; i++) {
//                            array[i] = Byte.parseByte(split[i]);
//                        }
//
//                        Bitmap bmp = BitmapFactory.decodeByteArray(array, 0, array.length);
//                        avatar.setImageBitmap(bmp);
//                        avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                        avatar.setVisibility(View.VISIBLE);
//
//                    } else {
//                        avatar.setVisibility(View.GONE);
//                    }
//
//                    String date = feature.getStringProperty("Date");
//                    TextView txvDate = (TextView) bubbleLayout.findViewById(R.id.txvDate);
//                    txvDate.setText(date);
//
//                    String time = feature.getStringProperty("Time");
//                    TextView txvTime = (TextView) bubbleLayout.findViewById(R.id.txvTime);
//                    txvTime.setText(time);
//
////                    DisplayMetrics displayMetrics = new DisplayMetrics();
////                    (activity).getWindowManager()
////                            .getDefaultDisplay()
////                            .getMetrics(displayMetrics);
////
////                    int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
////                    bubbleLayout.measure(displayMetrics.widthPixels, 400);
////
////                    float measuredWidth = bubbleLayout.getMeasuredWidth();
////
////                    bubbleLayout.setArrowPosition(measuredWidth / 2 - 5);
//
//
//                    Bitmap bitmap = loadBitmapFromView(bubbleLayout);
//                    //SymbolGenerator.generate(bubbleLayout, displayMetrics.widthPixels);
//                    imagesMap.put(id, bitmap);
//                    // viewMap.put(id, bubbleLayout);
//                }
//
//                return imagesMap;
//            } else {
//                return null;
//            }
//        }
//
//        public static Bitmap loadBitmapFromView(View v) {
//            if (v.getMeasuredHeight() <= 0) {
//
//                int specWidth = View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED);
//                v.measure(specWidth, specWidth);
//                int questionWidth = v.getMeasuredWidth();
//                int measuredHeight = v.getMeasuredHeight();
//
//                v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
//                Canvas c = new Canvas(b);
//                v.layout(0, 0, questionWidth, measuredHeight);
//                v.draw(c);
//                return b;
//            } else {
//                Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
//                Canvas c = new Canvas(b);
//                v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
//                v.draw(c);
//                return b;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(HashMap<String, Bitmap> bitmapHashMap) {
//            super.onPostExecute(bitmapHashMap);
//            map activity = activityRef.get();
//            if (activity != null && bitmapHashMap != null) {
//                activity.setImageGenResults(bitmapHashMap);
//                if (refreshSource) {
//                    activity.refreshSource();
//                }
//            }
//            //Toast.makeText(activity, R.string.tap_on_marker_instruction, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * Utility class to generate Bitmaps for Symbol.
//     */
//    private static class SymbolGenerator {
//
//        /**
//         * Generate a Bitmap from an Android SDK View.
//         *
//         * @param view the View to be drawn to a Bitmap
//         * @return the generated bitmap
//         */
//        static Bitmap generate(@NonNull View view, int width) {
//            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//            view.measure(measureSpec, measureSpec);
//
//            int measuredWidth = (width - 100);
//            //view.getMeasuredWidth();
//            int measuredHeight = view.getMeasuredHeight();
//
//            view.layout(0, 0, measuredWidth, measuredHeight);
//            Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
//            bitmap.eraseColor(Color.TRANSPARENT);
//            Canvas canvas = new Canvas(bitmap);
//            view.draw(canvas);
//            return bitmap;
//        }
//    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mapmenu, menu);

        MenuItem item = menu.findItem(R.id.map_box);
        item.setVisible(false);

        MenuItem item1 = menu.findItem(R.id.google_map);
        item1.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        recyclerView.setVisibility(View.GONE);

        switch (item.getItemId()) {
            case R.id.normal_map:
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        new LoadGeoJsonDataTask(map.this).execute();
                        mapboxMap.addOnMapClickListener(map.this);

                    }
                });
                return true;
            case R.id.hybrid_map:
                mapboxMap.setStyle(Style.SATELLITE_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        new LoadGeoJsonDataTask(map.this).execute();
                        mapboxMap.addOnMapClickListener(map.this);

                    }
                });
                return true;
            case R.id.satellite_map:
                mapboxMap.setStyle(Style.SATELLITE, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        new LoadGeoJsonDataTask(map.this).execute();
                        mapboxMap.addOnMapClickListener(map.this);

                    }
                });
                return true;
            case R.id.terrain_map:
                mapboxMap.setStyle(Style.TRAFFIC_DAY, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        new LoadGeoJsonDataTask(map.this).execute();
                        mapboxMap.addOnMapClickListener(map.this);

                    }
                });
                return true;
            case R.id.google_map:
                Intent ti = new Intent(getApplicationContext(), MapViewActivity.class);
                ti.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ti);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
