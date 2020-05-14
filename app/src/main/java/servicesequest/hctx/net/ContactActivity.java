package servicesequest.hctx.net;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import servicesequest.hctx.net.Async.ContactListAsync;
import servicesequest.hctx.net.DAL.ContactAdapter;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.contact;
import servicesequest.hctx.net.Utility.AppPreferences;
import servicesequest.hctx.net.Utility.Utils;

public class ContactActivity extends AppCompatActivity {

    ServiceRequestDbHelper dbHelper;
    Toolbar toolbar;
    BottomNavigationView navigation;
    AppPreferences _appPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        _appPrefs = new AppPreferences(this, "UserProfile");

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().findItem(R.id.navigation_contacts).setChecked(true);

        ContactListAsync asyncTask = new ContactListAsync(this, new ContactListAsync.OnTaskCompleted() {
            @Override
            public void taskCompleted(List<contact> results) {
                try {
                    if (results != null && results.size() != 0) {
                        ContactAdapter ad = new ContactAdapter(results, getApplicationContext());
                        RecyclerView vi = findViewById(R.id.recyclerview);
                        vi.setAdapter(ad);

                        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                        manager.setOrientation(RecyclerView.VERTICAL);

                        vi.setLayoutManager(manager);
                    } else {
                        Toast.makeText(getApplicationContext(), "No saved contacts", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    String Info = "There was a problem getting contacts. Please try again later.<br><br>";
                    Utils.customPopMessge(getApplicationContext(), "Error", Info + ex.getMessage(), "error");
                }
            }
        });
        asyncTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.getMenu().findItem(R.id.navigation_contacts).setChecked(true);
    }

    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent hi = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(hi);
                    return true;
                case R.id.navigation_dashboard:
                    Intent ti = new Intent(getApplicationContext(), TermsOfUseActivity.class);
                    startActivity(ti);
                    return true;
                case R.id.navigation_notifications:
                    Boolean profileSet = _appPrefs.getBoolean("ProfileSet");

                    if(profileSet)
                    {
                        Intent pi = new Intent(getApplicationContext(), ProfileList.class);
                        startActivity(pi);
                    }
                    else
                    {
                        Intent pi = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(pi);
                    }
                    return true;
                case R.id.navigation_contacts:
                    Intent ai = new Intent(getApplicationContext(), ContactActivity.class);
                    startActivity(ai);
                    return true;
            }
            return false;
        }
    };

}
