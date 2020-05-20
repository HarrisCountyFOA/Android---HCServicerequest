package servicesequest.hctx.net;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import servicesequest.hctx.net.Async.ProfileListAsync;
import servicesequest.hctx.net.DAL.ServiceRequestDbContract;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.Profile;
import servicesequest.hctx.net.Model.contact;
import servicesequest.hctx.net.Utility.AppPreferences;
import servicesequest.hctx.net.Utility.Utils;

public class ProfileList extends AppCompatActivity {

    TextView firstName;
    TextView lastName;
    TextView email;
    TextView address;
    TextView city;
    TextView primaryphone;
    TextView secondaryphone;
    TextView text2;
    TextView text3;
    TextView text4;
    TextView text5;
    TextView text6;
    TextView text7;
    //TextView text8;
    String profileId;
    ServiceRequestDbHelper dbHelper;
    CardView cmView;
    BottomNavigationView navigation;
    AppPreferences _appPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        _appPrefs = new AppPreferences(this, "UserProfile");

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().findItem(R.id.navigation_notifications).setChecked(true);

        firstName = findViewById(R.id.firstName);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        primaryphone = findViewById(R.id.primaryphone);
        secondaryphone = findViewById(R.id.secondaryphone);
        cmView = findViewById(R.id.cmView);

        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        text6 = findViewById(R.id.text6);
        text7 = findViewById(R.id.text7);
       // text8 = findViewById(R.id.text8);


        initScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.getMenu().findItem(R.id.navigation_notifications).setChecked(true);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profilelist_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                openProfileActivity(null, true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openProfileActivity(View view, Boolean isEdit) {
        Intent ai = new Intent(this, ProfileActivity.class);
        if (profileId != null) {
            ai.putExtra(ServiceRequestDbContract.ProfileEntry.COLUMN_ID, profileId);
        }

        if(isEdit)
        {
            startActivityForResult(ai, 1);
        }
        else {
            startActivity(ai);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            initScreen();
        }
    }

    private void initScreen() {

        ProfileListAsync asyncTask = new ProfileListAsync(this, new ProfileListAsync.OnTaskCompleted() {
            @Override
            public void taskCompleted(List<Profile> profiles) {
                try {
                    if (profiles.size() != 0) {
                        Profile p = profiles.get(0);
                        firstName.setText(p.firstname + " " + p.lastname);
                        email.setText(p.email);

                        if (p.streetaddress != null && !p.streetaddress.trim().equals("")) {
                            address.setText(p.streetaddress);
                            address.setVisibility(View.VISIBLE);
                        } else {
                            address.setVisibility(View.GONE);
                        }

                        city.setVisibility(View.GONE);

                        if (p.city != null && !p.city.trim().equals("")) {
                            city.setText(p.city);
                            city.setVisibility(View.VISIBLE);
                        }

                        if ((p.state != null && !p.state.trim().equals("")) || (p.zipcode != null && !p.zipcode.trim().equals(""))) {
                            if (p.city != null && !p.city.trim().equals("")) {
                                city.setText(p.city + ", " + p.state + " " + p.zipcode);
                                city.setVisibility(View.VISIBLE);
                            } else {
                                city.setText(p.state + " " + p.zipcode);
                                city.setVisibility(View.VISIBLE);
                            }
                        }

                        if (p.primaryphone != null && !p.primaryphone.trim().equals("")) {
                            primaryphone.setText(p.primaryphone);
                            primaryphone.setVisibility(View.VISIBLE);
                        } else {
                            primaryphone.setVisibility(View.GONE);
                        }

                        if (p.secondaryphone != null && !p.secondaryphone.trim().equals("")) {
                            secondaryphone.setText(p.secondaryphone);
                            secondaryphone.setVisibility(View.VISIBLE);
                        } else {
                            secondaryphone.setVisibility(View.GONE);
                        }

                        profileId = p._id;
                        if (p.commContact != null && p.commContact._id != null && !p.commContact._id.trim().equals("")) {
                            contact comContact = p.commContact;
                            text2.setText(comContact.firstname);
                            text3.setText(comContact.title);
                            text4.setText(comContact.email);
                            text5.setText(comContact.phone);
                            text6.setText(comContact.website);
                            text7.setText(comContact.streetaddress + "\n" + comContact.city);
                            cmView.setVisibility(View.VISIBLE);
                        } else {
                            cmView.setVisibility(View.GONE);
                        }

                    } else {
                        openProfileActivity(null, false);
                    }

                } catch (Exception ex) {
                    String Info = "There was a problem getting your profile. Please try again later.<br><br>";
                    Utils.customPopMessge(ProfileList.this, "Error", Info + ex.getMessage(), "error");
                }
            }
        });
        asyncTask.execute();
    }
}
