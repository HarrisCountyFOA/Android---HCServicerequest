package servicesequest.hctx.net;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import servicesequest.hctx.net.Async.RequestListAsync;
import servicesequest.hctx.net.DAL.RequestListAdapter;
import servicesequest.hctx.net.DAL.ServiceRequestDbContract;
import servicesequest.hctx.net.Model.Request;
import servicesequest.hctx.net.Utility.AppPermission;
import servicesequest.hctx.net.Utility.AppPreferences;
import servicesequest.hctx.net.Utility.Utils;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView navigation;
    AppPreferences _appPrefs;
    FrameLayout frameLayout;
    ConstraintLayout consLayout;
    FloatingActionButton btnMap;
    FloatingActionButton btnAdd;
    Button btnEditProfile;
    Button btnNewRequest;
    TextView textView;
    TextView txtNoReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        _appPrefs = new AppPreferences(this, "UserProfile");
        frameLayout = findViewById(R.id.frameLayout);
        consLayout = findViewById(R.id.consLayout);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnNewRequest = findViewById(R.id.btnNewRequest);
        textView = findViewById(R.id.textView);
        txtNoReports = findViewById(R.id.txtNoReports);

        LoadData();

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);

        btnMap = findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ti = new Intent(getApplicationContext(), MapViewActivity.class);
                ti.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ti);
            }
        });

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent ti = new Intent(getApplicationContext(), NewRequestActivity.class);
                    startActivityForResult(ti, 1);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String profileId = _appPrefs.getString("ProfileId");

                Intent pi = new Intent(getApplicationContext(), ProfileActivity.class);
                if (!profileId.equals("")) {
                    pi.putExtra(ServiceRequestDbContract.ProfileEntry.COLUMN_ID, profileId);
                }
                startActivity(pi);
            }
        });

        btnNewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent pi = new Intent(getApplicationContext(), NewRequestActivity.class);
                    startActivity(pi);
            }
        });
    }

    private void LoadData() {
        RequestListAsync asyncTask = new RequestListAsync(this, new RequestListAsync.OnTaskCompleted() {
            @Override
            public void taskCompleted(List<Request> results) {
                try {
                    if (results.size() != 0) {
                        RequestListAdapter ad = new RequestListAdapter(results, getApplicationContext(), new RequestListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Request item) {

                                Intent ai = new Intent(getApplicationContext(), RequestDetailsActivity.class);
                                if (item != null) {
                                    ai.putExtra(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_ID, item._id);
                                }
                                startActivity(ai);
                            }
                        });
                        RecyclerView vi = findViewById(R.id.recyclerview);
                        vi.setAdapter(ad);

                        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                        manager.setOrientation(RecyclerView.VERTICAL);

                        vi.setLayoutManager(manager);
                    }

                    Boolean profileSet = _appPrefs.getBoolean("ProfileSet");
                    if (!profileSet || results.size() == 0) {
                        consLayout.setVisibility(View.VISIBLE);
                        frameLayout.setVisibility(View.GONE);
                        btnMap.setVisibility(View.GONE);
                        btnAdd.setVisibility(View.GONE);
                        getSupportActionBar().hide();

                        if (results.size() == 0 && profileSet) {
                            btnNewRequest.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.GONE);
                            txtNoReports.setText(getString(R.string.no_request_have_been_submitted));
                            btnEditProfile.setText(getString(R.string.edit_profile));
                            btnEditProfile.setVisibility(View.GONE);
                        } else {
                            btnNewRequest.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                            txtNoReports.setText("");
                            btnEditProfile.setText(getString(R.string.create_profile));
                            btnEditProfile.setVisibility(View.VISIBLE);
                        }

                    } else {
                        consLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);
                        btnMap.setVisibility(View.VISIBLE);
                        btnAdd.setVisibility(View.VISIBLE);
                        getSupportActionBar().show();
                    }

                } catch (Exception ex) {
                    String Info = "There was a problem getting your reports. Please try again later.<br><br>";
                    Utils.customPopMessge(getApplicationContext(), "Error", Info + ex.getMessage(), "error");
                }
            }
        });
        asyncTask.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            LoadData();
        }
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

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        for(String permission: permissions){
//            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)){
//                Toast.makeText(MainActivity.this, R.string.requestPermissions, Toast.LENGTH_LONG).show();
//                Intent ti = new Intent(getApplicationContext(), NewRequestActivity.class);
//                startActivity(ti);
//                //Log.e("denied", permission);  //denied
//            }else{
//                if(ActivityCompat.checkSelfPermission((MainActivity.this), permission) == PackageManager.PERMISSION_GRANTED){
//                    Intent ti = new Intent(getApplicationContext(), NewRequestActivity.class);
//                    startActivity(ti);
//                    //Log.e("allowed", permission); //allowed
//                } else{
//                    //Log.e("set to never ask again", permission); //set to never ask again
//                    Intent ti = new Intent(getApplicationContext(), NewRequestActivity.class);
//                    startActivity(ti);
//                    //Toast.makeText(MainActivity.this, R.string.requestPermissions, Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }
}
