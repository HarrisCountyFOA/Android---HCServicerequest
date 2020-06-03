package servicesequest.hctx.net;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import servicesequest.hctx.net.Async.NewRequestAsync;
import servicesequest.hctx.net.DAL.ContactDataManager;
import servicesequest.hctx.net.DAL.LatLngAdapter;
import servicesequest.hctx.net.DAL.PlacePredictionAdapter;
import servicesequest.hctx.net.DAL.RequestDataManager;
import servicesequest.hctx.net.DAL.RequestTypeAsync;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.GeocodingResult;
import servicesequest.hctx.net.Model.RequestTypeSelectSet;
import servicesequest.hctx.net.Model.RequestTypeSet;
import servicesequest.hctx.net.Model.contact;
import servicesequest.hctx.net.Utility.AppPermission;
import servicesequest.hctx.net.Utility.FusedLocationProvider;
import servicesequest.hctx.net.Utility.GoogleMapWithScrollFix;
import servicesequest.hctx.net.Utility.ImagePicker;
import servicesequest.hctx.net.Utility.Utils;


public class NewRequestActivity extends AppCompatActivity implements LocationListener {

    private static final String TAG = NewRequestActivity.class.getSimpleName();
    private Location mGetedLocation;
    private double currentLat, currentLng;
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 101;
    TextInputEditText txtView;
    private PlacesClient placesClient;
    private PlacePredictionAdapter adapter = new PlacePredictionAdapter();
    private AutocompleteSessionToken sessionToken;
    private Gson gson = new GsonBuilder().registerTypeAdapter(LatLng.class, new LatLngAdapter())
            .create();
    private RequestQueue queue;
    RecyclerView recyclerView;
    TextView txtAddPhoto;
    ImageView btnPicture;
    TextInputEditText txtDescription;
    private int PICK_IMAGE_REQUEST = 1;
    Spinner spinnerRequest;
    ArrayAdapter<RequestTypeSelectSet> spinnerArrayAdapterRequest;
    public String Precinct = null;
    private Handler handler = new Handler();

    public String RequestType = null;
    public String RequestTypeValue = null;
    public int RequestTypeValuePosition = 0;
    LatLng myLocation;
    ServiceRequestDbHelper dbHelper;
    RequestDataManager manager;
    boolean imageSet = false;
    private ViewAnimator viewAnimator;
    boolean runSearch = true;

    TextView text2;
    TextView text3;
    TextView text4;
    TextView text5;
    TextView text6;
    TextView text7;
    TextView text8;
    CardView cmView;

    private GoogleMap mMap;
    boolean mapsearch = false;
    FusedLocationProvider fusedLocationProvider;
    InputMethodManager inputManager;

    ImageView imgDescActive,imgTypeActive,imgLocationActive,imgPhotoActive;
    int selectedItem = -1;
    public byte[] Bitmap_Image;
    Button btnPictureDelete;
    RelativeLayout imagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        getSupportActionBar().setTitle("New Request");

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));

        placesClient = Places.createClient(this);
        queue = Volley.newRequestQueue(this);
        txtView = findViewById(R.id.place_search);
        txtDescription =  findViewById(R.id.txtDescription);
        txtView.addTextChangedListener(filterTextWatcher);
        txtAddPhoto = findViewById(R.id.txtAddPhoto);
        btnPicture = findViewById(R.id.btnPicture);
        spinnerRequest = findViewById(R.id.spinnerRequest);

        //Make the keyboard have done using textMultiLine
        txtDescription.setImeOptions(EditorInfo.IME_ACTION_DONE);
        txtDescription.setRawInputType(InputType.TYPE_CLASS_TEXT);

        RequestTypeSet.requestTypeSet.clear();

        spinnerRequest = findViewById(R.id.spinnerRequest);
        spinnerArrayAdapterRequest = new ArrayAdapter<RequestTypeSelectSet>(this, R.layout.style_spinner_layout){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                // If this is the selected item position
                if (position == selectedItem)
                    view.setBackgroundColor(Color.LTGRAY);
                else
                    view.setBackgroundColor(getResources().getColor(R.color.TextInputBackground));


                // Set the hint text color gray/ hide the item in the popup
                TextView tv = (TextView) view;
                if (position == 0) {
                    //tv.setVisibility(View.GONE);
                    tv.setTextColor(getResources().getColor(R.color.TextInput));
                    tv.setText(getResources().getString(R.string.TypePopupHint));
                    tv.setTypeface(null, Typeface.BOLD);
                    view.setBackgroundColor(Color.LTGRAY);
                } else {
                    //tv.setVisibility(View.VISIBLE);
                    tv.setTextColor(getResources().getColor(R.color.TextInput));
                    tv.setTypeface(null, Typeface.NORMAL);
                }
                return view;
            }
        };
        spinnerArrayAdapterRequest.setDropDownViewResource(R.layout.style_spinner_dropdown_layout);

        spinnerArrayAdapterRequest.add(new RequestTypeSelectSet("", ""));
        spinnerRequest.setAdapter(spinnerArrayAdapterRequest);
        spinnerRequest.setSelection(0, false);


        viewAnimator = findViewById(R.id.view_animator);
        sessionToken = AutocompleteSessionToken.newInstance();


        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        text6 = findViewById(R.id.text6);
        text7 = findViewById(R.id.text7);
        text8 = findViewById(R.id.text8);

        imgDescActive = findViewById(R.id.imgDescActive);
        imgTypeActive = findViewById(R.id.imgTypeActive);
        imgLocationActive = findViewById(R.id.imgLocationActive);
        imgPhotoActive = findViewById(R.id.imgPhotoActive);
        btnPictureDelete = findViewById(R.id.btnPictureDelete);
        imagePreview = findViewById(R.id.imagePreview);

        cmView = findViewById(R.id.cmView);
        cmView.setVisibility(View.GONE);
        imagePreview.setVisibility(View.GONE);

        initRecyclerView();
        txtAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppPermission.checkCameraPermission(NewRequestActivity.this)) {
                    if (!imageSet) {
                        ImagePicker.pickImage(NewRequestActivity.this, getResources().getString(R.string.TakePicture));
                    } else {
                        final CharSequence[] items = {getResources().getString(R.string.AddUpdate), getResources().getString(R.string.Remove),};
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewRequestActivity.this);
                        builder.setTitle(getResources().getString(R.string.PicturePopTitle));
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                switch (which) {
                                    case 0: //Add/Update
                                    {
                                        ImagePicker.pickImage(NewRequestActivity.this, getResources().getString(R.string.TakePicture));
                                    }
                                    case 1: //Remove
                                    {
                                        imageSet = false;
                                        Bitmap_Image = null;
                                        imagePreview.setVisibility(View.GONE);
                                        txtAddPhoto.setText(getResources().getString(R.string.PhotoTitleAdd));
                                        imgPhotoActive.setImageResource(R.drawable.ic_check_circle_silver_24dp);
                                    }
                                }
                            }
                        });
                        builder.setNegativeButton(getResources().getString(R.string.PicturePopCancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.show();
                    }
                }

            }
        });

        btnPictureDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSet = false;
                Bitmap_Image = null;
                imagePreview.setVisibility(View.GONE);
                txtAddPhoto.setText(getResources().getString(R.string.PhotoTitleAdd));
                imgPhotoActive.setImageResource(R.drawable.ic_check_circle_silver_24dp);
            }
        });


        spinnerRequest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                RequestTypeSelectSet rt = (RequestTypeSelectSet) spinnerRequest.getSelectedItem();
                RequestTypeValuePosition = spinnerRequest.getSelectedItemPosition();

                if (rt != null) {
                    RequestType = rt.key;
                    RequestTypeValue = String.valueOf(rt.value);

                }

                // First item is disable and it is used for hint
                if(position > 0) {
                    imgTypeActive.setImageResource(R.drawable.ic_check_circle_green_24dp);
                    ((TextView) parentView.getChildAt(0)).setTextColor(getResources().getColor(R.color.TextInput));
                    ((TextView) parentView.getChildAt(0)).setTextSize(18f);
                }
                else {
                    imgTypeActive.setImageResource(R.drawable.ic_check_circle_silver_24dp);
                }
                selectedItem = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        //Start a connect for location
        fusedLocationProvider = new FusedLocationProvider(NewRequestActivity.this, this);
        fusedLocationProvider.connect();

        // Obtain the Custom GoogleMapWithScrollFix and get notified when the map is ready to be used.
        //So map can work with scroll
        ((GoogleMapWithScrollFix) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);

                final ScrollView mScrollView1 = findViewById(R.id.ScrollView01);
                ((GoogleMapWithScrollFix) getSupportFragmentManager().findFragmentById(R.id.map)).setListener(new GoogleMapWithScrollFix.OnTouchListener() {
                    @Override
                    public void onTouch() {
                        //Disable scrolling of outside scroll view
                        mScrollView1.requestDisallowInterceptTouchEvent(true);
                    }
                });
            }
        });

        txtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0)//gray Icon
                    imgDescActive.setImageResource(R.drawable.ic_check_circle_silver_24dp);
                else if (s.length() > 0)//Green Icon
                    imgDescActive.setImageResource(R.drawable.ic_check_circle_green_24dp);

            }
        });
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, imageReturnedIntent);
        if (bitmap != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            imgPhotoActive.setImageResource(R.drawable.ic_check_circle_green_24dp);
            Bitmap_Image = bos.toByteArray();
            btnPicture.setImageBitmap(bitmap);
            imagePreview.setVisibility(View.VISIBLE);
            txtAddPhoto.setText(getResources().getString(R.string.PhotoTitleUpdate));
            imageSet = true;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newrequest_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveRequest();
                return true;
            case R.id.action_helpinfo:
                String Info = "1.  Enter street address and select from the auto suggest list.<br><br>"
                        + "2. Select request type. request type will be empty if location entered is outside of harris county <br><br>"
                        + "3. Enter description. <br><br>"
                        + "4. (Optional) Attach a photo.  <br><br>"
                        + "5. Tap Submit.  You will receive an email when your request has been submitted with the request ticket number for future follow-up.";

                Utils.customPopMessge(this, "Instructions", Info, "instructions");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveRequest() {

        if (!Validation()) {

            dbHelper = new ServiceRequestDbHelper(this);
            manager = new RequestDataManager();

            servicesequest.hctx.net.Model.Request newReq = new servicesequest.hctx.net.Model.Request();


            newReq.Image = Bitmap_Image;
            newReq.Image_Name = RequestType;
            newReq.Latitude = Double.toString(myLocation.latitude);
            newReq.Longitude = Double.toString(myLocation.longitude);
            newReq.FullAddress = txtView.getText().toString();
            newReq.Precinct = Precinct;
            newReq.RequestType = RequestType;
            newReq.RequestTypeValue = RequestTypeValue;
            newReq.Description = txtDescription.getText().toString();
            newReq.Sent = false;

            NewRequestAsync ayncTask = new NewRequestAsync(this, newReq, new NewRequestAsync.OnTaskCompleted() {
                @Override
                public void taskCompleted(Boolean results) {
                    if (results) {
                        setResult(RESULT_OK, new Intent());
                        finish();

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.RequestSuccess), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.RequestFailed), Toast.LENGTH_LONG).show();
                    }
                }
            });
            ayncTask.execute();
        }
    }

    public boolean Validation() {

        boolean results = false;


        if (txtView.getText().toString().trim().length() == 0) {
            txtView.setError(getResources().getString(R.string.ErrAddress));
            results = true;
        } else if (myLocation == null) {
            txtView.setError(getResources().getString(R.string.ErrAddressInvalid));
            results = true;
        }

        if (spinnerRequest.getSelectedItemPosition() <= 0 || RequestType == null || RequestTypeValue == null) {
            ((TextView) spinnerRequest.getSelectedView()).setError(getResources().getString(R.string.ErrType));
        }

        if (txtDescription.getText().toString().trim().length() == 0) {
            txtDescription.setError(getResources().getString(R.string.ErrDescription));
            results = true;
        }

        return results;
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(final Editable s) {

            handler.removeCallbacksAndMessages(null);
            if (runSearch) {
                // Start a new place prediction request in 300 ms
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //myLocation = null; //i will check back later on this - juan.
                        if (!s.toString().equals("")) {
                            if (!mapsearch) {
                                getPlacePredictions(s.toString());

                                if (viewAnimator.getVisibility() == View.GONE) {
                                    viewAnimator.setVisibility(View.VISIBLE);
                                }
                            }
                            else
                            {
                                if (viewAnimator.getVisibility() == View.VISIBLE) {
                                    viewAnimator.setVisibility(View.GONE);
                                }
                                mapsearch = false;
                            }
                        } else {
                            RequestTypeSet.requestTypeSet.clear();
                            spinnerArrayAdapterRequest.clear();
                            spinnerArrayAdapterRequest.add(new RequestTypeSelectSet(getResources().getString(R.string.TypeHint), ""));
                            spinnerRequest.setAdapter(spinnerArrayAdapterRequest);
                            spinnerRequest.setSelection(0, false);

                            if (viewAnimator.getVisibility() == View.VISIBLE) {
                                viewAnimator.setVisibility(View.GONE);
                            }

                            cmView.setVisibility(View.GONE);
                        }
                    }
                }, 300);
            } else {
                runSearch = true;
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() == 0)//gray Icon
                imgLocationActive.setImageResource(R.drawable.ic_check_circle_silver_24dp);
            else if (s.length() > 0)//Green Icon
                imgLocationActive.setImageResource(R.drawable.ic_check_circle_green_24dp);
        }
    };

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.places_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        adapter.setPlaceClickListener(new PlacePredictionAdapter.OnPlaceClickListener() {
            @Override
            public void onPlaceClicked(AutocompletePrediction place) {
                myLocation = null;
                runSearch = false;
                txtView.setText(place.getFullText(null));
                geocodePlaceAndDisplay(place);
                viewAnimator.setVisibility(View.GONE);
                closeKeyboard();
            }
        });
    }

    private void geocodePlaceAndDisplay(final AutocompletePrediction placePrediction) {

        // Construct the request URL
        final String apiKey = getString(R.string.google_maps_key);
        final String url = "https://maps.googleapis.com/maps/api/geocode/json?place_id=%s&key=%s";
        final String requestURL = String.format(url, placePrediction.getPlaceId(), apiKey);

        // Use the HTTP request URL for Geocoding API to get geographic coordinates for the place
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Inspect the value of "results" and make sure it's not empty
                    JSONArray results = response.getJSONArray("results");
                    if (results.length() == 0) {
                        Log.w(TAG, "No results from geocoding request.");
                        return;
                    }

                    // Use Gson to convert the response JSON object to a POJO
                    GeocodingResult result = gson.fromJson(results.getString(0), GeocodingResult.class);

                    if(result.formatted_address != null)
                    {
                        runSearch = false;
                        txtView.setText(result.formatted_address);
                    }

                    myLocation = result.geometry.location;
                    initMap();
                    AddressCheck();
                    // displayDialog(placePrediction, result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Request failed");
            }
        });

        // Add the request to the Request queue.
        queue.add(request);
    }

    private void displayDialog(AutocompletePrediction place, GeocodingResult result) {
        new AlertDialog.Builder(this)
                .setTitle(place.getPrimaryText(null))
                .setMessage("Geocoding result:\n" + result.geometry.location)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }


    private void getPlacePredictions(String query) {

        // The value of 'bias' biases prediction results to the rectangular region provided
        // (currently Kolkata). Modify these values to get results for another area. Make sure to
        // pass in the appropriate value/s for .setCountries() in the
        // FindAutocompletePredictionsRequest.Builder object as well.
        final LocationBias bias = RectangularBounds.newInstance(
                new LatLng(29.749907, -95.358421), // SW lat, lng
                new LatLng(29.749907, -95.358421) // NE lat, lng
        );

        // Create a new programmatic Place Autocomplete request in Places SDK for Android
        final FindAutocompletePredictionsRequest newRequest = FindAutocompletePredictionsRequest
                .builder()
                .setSessionToken(sessionToken)
                .setLocationBias(bias)
                .setTypeFilter(TypeFilter.ADDRESS)
                .setQuery(query)
                .setCountries("US")
                .build();

        // Perform autocomplete predictions request
        placesClient.findAutocompletePredictions(newRequest).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
            @Override
            public void onSuccess(FindAutocompletePredictionsResponse findAutocompletePredictionsResponse) {
                List<AutocompletePrediction> predictions = findAutocompletePredictionsResponse.getAutocompletePredictions();
                adapter.setPredictions(predictions);
                viewAnimator.setDisplayedChild(predictions.isEmpty() ? 0 : 1);
            }
        });
    }

    public static String getPrecinct(String Precinct) {
        if (Precinct.contains("1"))
            return "Precinct 1";
        else if (Precinct.contains("2"))
            return "Precinct 2";
        else if (Precinct.contains("3"))
            return "Precinct 3";
        else if (Precinct.contains("4"))
            return "Precinct 4";

        return null;
    }

    public void AddressCheck() {
        RequestTypeAsync asyncTask = new RequestTypeAsync(this, myLocation.latitude, myLocation.longitude, new RequestTypeAsync.OnTaskCompleted() {
            @Override
            public void taskCompleted(Boolean results) {
                spinnerArrayAdapterRequest.clear();


                RequestType = null;
                RequestTypeValue = null;

                spinnerArrayAdapterRequest.add(new RequestTypeSelectSet(getResources().getString(R.string.TypeHint), ""));

                if (results) {
                    for (RequestTypeSet type : RequestTypeSet.requestTypeSet) {
                        spinnerArrayAdapterRequest.add(new RequestTypeSelectSet(type.Category, type.Code));
                    }

                    if (!RequestTypeSet.requestTypeSet.get(1).Name.equals(Precinct))
                        RequestTypeValuePosition = 0;


                    spinnerRequest.setAdapter(spinnerArrayAdapterRequest);
                    spinnerRequest.setSelection(RequestTypeValuePosition, false);

                    Precinct = getPrecinct(RequestTypeSet.requestTypeSet.get(1).Name);

                    if (!Precinct.equals("0")) {
                        ContactDataManager conManager = new ContactDataManager();
                        dbHelper = new ServiceRequestDbHelper(getApplicationContext());
                        contact comContact = conManager.getContactByPrecinct(dbHelper, Precinct);

                        if (comContact != null) {
                            text2.setText(comContact.firstname);
                            text3.setText(comContact.title);
                            text4.setText(comContact.email);
                            text5.setText(comContact.phone);
                            text6.setText(comContact.website);
                            text7.setText(comContact.streetaddress);
                            text8.setText(comContact.city);
                        }

                        cmView.setVisibility(View.VISIBLE);
                    } else {
                        cmView.setVisibility(View.GONE);
                    }


                    // txtPct.setText(getPrecinct(Precinct));

                    // SubmitAction = true;
                } else {

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.ErrInvalidHarrisCountyAddress), Toast.LENGTH_LONG).show();
                }
                //  txtAddress.setText(FullAddress);


//                if (_appPrefs.getString(ProfileSet.PROFILE_EMAIL).equals("")) {
//                    showSimplePopUp();
//                }
            }
        });
        asyncTask.execute();
    }

    ///########Close Key Board
    private void closeKeyboard() {
        if (getCurrentFocus() != null)
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initMap()
    {
        LatLng hc = new LatLng(myLocation.latitude, myLocation.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hc, 15.0f));
        mMap.clear();
        Marker perth = mMap.addMarker(new MarkerOptions().position(hc).draggable(true));
        MarkerDrag();
    }

    public void MarkerDrag() {
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                //Log.i("System out", "onMarkerDragStart...");
            }

            @Override
            public void onMarkerDragEnd(Marker arg0) {
                myLocation = arg0.getPosition();
                getAddress();
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                //Log.i("System out", "onMarkerDrag...");
            }
        });
    }

    private void getAddress()
    {
        Geocoder gc = new Geocoder(NewRequestActivity.this);
        List<Address> list = null;
        try {
            list = gc.getFromLocation(myLocation.latitude, myLocation.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Address add = list.get(0);
        String Address = add.getAddressLine(0);

        txtView.setText(Address);
        RequestTypeSet.requestTypeSet.clear();
        AddressCheck();
        mapsearch = true;
    }


    @Override
    public void onLocationChanged(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        myLocation = new LatLng(currentLatitude, currentLongitude);

        fusedLocationProvider.disconnect();
        initMap();
        getAddress();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                Toast.makeText(NewRequestActivity.this, R.string.requestPermissions, Toast.LENGTH_LONG).show();
                //Log.e("denied", permission);  //denied
            } else {
                if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                    //Log.e("allowed", permission); //allowed
                } else {
                    //Log.e("set to never ask again", permission); //set to never ask again
                    //if (permission.equals("android.permission.CAMERA"))
                    Toast.makeText(NewRequestActivity.this, R.string.requestPermissions, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
