package servicesequest.hctx.net;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.List;

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
import servicesequest.hctx.net.Utility.Utils;


public class NewRequestActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        getSupportActionBar().setTitle("New Request");

        Places.initialize(getApplicationContext(), "AIzaSyAU4Qza-d8-PJ6rTI8r4k9tcpw1L-7nHAs");

        placesClient = Places.createClient(this);
        queue = Volley.newRequestQueue(this);
        txtView = ((TextInputEditText) findViewById(R.id.place_search));
        txtDescription = ((TextInputEditText) findViewById(R.id.txtDescription));
        txtView.addTextChangedListener(filterTextWatcher);
        txtAddPhoto = (TextView) findViewById(R.id.txtAddPhoto);
        btnPicture = (ImageView) findViewById(R.id.btnPicture);
        spinnerRequest = (Spinner) findViewById(R.id.spinnerRequest);

        RequestTypeSet.requestTypeSet.clear();

        spinnerRequest = (Spinner) findViewById(R.id.spinnerRequest);
        spinnerArrayAdapterRequest = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item);
        spinnerArrayAdapterRequest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerArrayAdapterRequest.add(new RequestTypeSelectSet("", ""));
        spinnerRequest.setAdapter(spinnerArrayAdapterRequest);
        spinnerRequest.setSelection(0, false);
        viewAnimator = findViewById(R.id.view_animator);
        sessionToken = AutocompleteSessionToken.newInstance();

        cmView = findViewById(R.id.cmView);
        cmView.setVisibility(View.GONE);
        btnPicture.setVisibility(View.GONE);

        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        text6 = findViewById(R.id.text6);
        text7 = findViewById(R.id.text7);
        text8 = findViewById(R.id.text8);

        initRecyclerView();
        txtAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtAddPhoto.getText().toString().equals("Add Photo")) {
                    selectImage();
                } else {
                    imageSet = false;
                    btnPicture.setVisibility(View.GONE);
                    txtAddPhoto.setText("Add Photo");
                }
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(NewRequestActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

            if (imageSet) {
                Bitmap bitmap = ((BitmapDrawable) btnPicture.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                newReq.Image = stream.toByteArray();
            }
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

                        Toast.makeText(getApplicationContext(), "Request Saved Successfully!!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to save request. Please try again!!", Toast.LENGTH_LONG).show();
                    }
                }
            });
            ayncTask.execute();
        }
    }

    public boolean Validation() {

        boolean results = false;

        if (txtView.getText().toString().trim().length() == 0) {
            txtView.setError("Address is required!");
            results = true;
        } else if (myLocation == null) {
            txtView.setError("Invalid Address!");
            results = true;
        }

        if (spinnerRequest.getSelectedItemPosition() <= 0 || RequestType == null || RequestTypeValue == null) {
            ((TextView) spinnerRequest.getSelectedView()).setError("Request Type is Required.");
        }

        if (txtDescription.getText().toString().trim().length() == 0) {
            txtDescription.setError("Description is required!");
            results = true;
        }

        return results;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    btnPicture.setImageBitmap(photo);
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    btnPicture.setImageURI(selectedImage);
                }
                break;
        }

        btnPicture.setVisibility(View.VISIBLE);
        txtAddPhoto.setText("Remove Photo");
        imageSet = true;
    }


    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(final Editable s) {

            handler.removeCallbacksAndMessages(null);
            if (runSearch) {
                // Start a new place prediction request in 300 ms
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myLocation = null;
                        if (!s.toString().equals("")) {
                            getPlacePredictions(s.toString());
                            if (viewAnimator.getVisibility() == View.GONE) {
                                viewAnimator.setVisibility(View.VISIBLE);
                            }
                        } else {
                            spinnerArrayAdapterRequest.clear();
                            spinnerArrayAdapterRequest.add(new RequestTypeSelectSet("", ""));
                            spinnerRequest.setAdapter(spinnerArrayAdapterRequest);
                            spinnerRequest.setSelection(0, false);

                            if (viewAnimator.getVisibility() == View.VISIBLE) {
                                viewAnimator.setVisibility(View.GONE);
                            }
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
        }
    };

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.places_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView
                .addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        adapter.setPlaceClickListener(new PlacePredictionAdapter.OnPlaceClickListener() {
            @Override
            public void onPlaceClicked(AutocompletePrediction place) {
                myLocation = null;
                runSearch = false;
                txtView.setText(place.getFullText(null));
                geocodePlaceAndDisplay(place);
                viewAnimator.setVisibility(View.GONE);
            }
        });
    }

    private void geocodePlaceAndDisplay(final AutocompletePrediction placePrediction) {
        // Construct the request URL
        final String apiKey = "AIzaSyAU4Qza-d8-PJ6rTI8r4k9tcpw1L-7nHAs";
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
                    GeocodingResult result = gson.fromJson(
                            results.getString(0), GeocodingResult.class);

                    myLocation = result.geometry.location;
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

                spinnerArrayAdapterRequest.add(new RequestTypeSelectSet("", ""));

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

                    Toast.makeText(getApplicationContext(), "You need a Harris County address to submit a services request. ", Toast.LENGTH_LONG).show();
                }
                //  txtAddress.setText(FullAddress);


//                if (_appPrefs.getString(ProfileSet.PROFILE_EMAIL).equals("")) {
//                    showSimplePopUp();
//                }
            }
        });
        asyncTask.execute();
    }

}
