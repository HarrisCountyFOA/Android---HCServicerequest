package servicesequest.hctx.net;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import servicesequest.hctx.net.Async.ProfileListAsync;
import servicesequest.hctx.net.Async.RequestListAsync;
import servicesequest.hctx.net.DAL.ProfileDataManager;
import servicesequest.hctx.net.DAL.RequestListAdapter;
import servicesequest.hctx.net.DAL.ServiceRequestDbContract;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.Profile;
import servicesequest.hctx.net.Model.Request;
import servicesequest.hctx.net.Utility.AppPreferences;
import servicesequest.hctx.net.Utility.Utils;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView navigation;
    AppPreferences _appPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        _appPrefs = new AppPreferences(this, "UserProfile");

        LoadData();

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);

        FloatingActionButton btnMap = findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ti = new Intent(getApplicationContext(), MapViewActivity.class);
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
                    } else if (results.size() == 0) {
                        Toast.makeText(getApplicationContext(), "No saved reports", Toast.LENGTH_LONG).show();
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

                    if(profileSet)
                    {
                        Intent pi = new Intent(getApplicationContext(), ProfileList.class);
                        pi.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(pi);
                    }
                    else
                    {
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

}
