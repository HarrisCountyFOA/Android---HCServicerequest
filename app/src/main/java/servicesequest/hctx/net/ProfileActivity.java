package servicesequest.hctx.net;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import servicesequest.hctx.net.Async.EditProfileAsync;
import servicesequest.hctx.net.Async.ProfileAsync;
import servicesequest.hctx.net.DAL.ProfileDataManager;
import servicesequest.hctx.net.DAL.ServiceRequestDbContract;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.Profile;
import servicesequest.hctx.net.Utility.AppPreferences;
import servicesequest.hctx.net.Utility.Utils;
import servicesequest.hctx.net.Utility.Valid;

public class ProfileActivity extends AppCompatActivity {

    EditText tv_FirstName;
    EditText tv_LastName;
    EditText tv_Address;
    EditText tv_City;
    EditText tv_State;
    EditText tv_ZipCode;
    EditText tv_Email;
    EditText tv_PrimaryPhone;
    EditText tv_SecondaryPhone;
    ServiceRequestDbHelper dbHelper;
    String profileId;
    Profile insUpd;
    ProfileDataManager manager;
    AppPreferences _appPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new ServiceRequestDbHelper(this);

        tv_FirstName = findViewById(R.id.FirstName);
        tv_LastName = findViewById(R.id.lastName);
        tv_Address = findViewById(R.id.Address);
        tv_Email = findViewById(R.id.email);
        tv_PrimaryPhone = findViewById(R.id.primaryphone);
        tv_SecondaryPhone = findViewById(R.id.secondaryphone);
        tv_City = findViewById(R.id.city);
        tv_State = findViewById(R.id.state);
        tv_ZipCode = findViewById(R.id.zipcode);
        _appPrefs = new AppPreferences(this, "UserProfile");

        tv_PrimaryPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        tv_SecondaryPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        tv_FirstName.setFocusableInTouchMode(true);
        tv_FirstName.requestFocus();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            profileId = extras.getString(ServiceRequestDbContract.ProfileEntry.COLUMN_ID);

            if (profileId != null) {
                initScreen(profileId);
            }
        }
    }


//    @Override
//    public void onBackPressed() {
//        if (profileId == null) {
//            Intent ai = new Intent(this, MainActivity.class);
//            startActivity(ai);
//        } else {
//            Intent ai = new Intent(this, ProfileList.class);
//            startActivity(ai);
//        }
//        finish();
//    }

    private void initScreen(String id) {

        ProfileAsync asyncTask = new ProfileAsync(this, id, new ProfileAsync.OnTaskCompleted() {
            @Override
            public void taskCompleted(Profile prof) {

                try {
                    if (prof != null && prof._id != null && !prof._id.equals("")) {
                        tv_FirstName.setText(prof.firstname);
                        tv_LastName.setText(prof.lastname);
                        tv_Email.setText(prof.email);
                        tv_Address.setText(prof.streetaddress);
                        tv_City.setText(prof.city);
                        tv_State.setText(prof.state);
                        tv_ZipCode.setText(prof.zipcode);
                        tv_PrimaryPhone.setText(prof.primaryphone);
                        tv_SecondaryPhone.setText(prof.secondaryphone);
                    } else {
                        Toast.makeText(getApplicationContext(), "No profile found", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception ex) {
                    String Info = "There was a problem getting your profile. Please try again later.<br><br>";
                    Utils.customPopMessge(getApplicationContext(), "Error", Info + ex.getMessage(), "error");
                }
            }
        });
        asyncTask.execute();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profileactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProfile(null);
                return true;
//            case android.R.id.home:
////                if (profileId == null) {
////                    startActivity(new Intent(this, MainActivity.class));
////                } else {
////                    startActivity(new Intent(this, ProfileList.class));
////                }
//              //  finish();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveProfile(View view) {
        if (!Validation()) {
            dbHelper = new ServiceRequestDbHelper(this);
            manager = new ProfileDataManager();

            insUpd = new Profile();
            insUpd.firstname = tv_FirstName.getText().toString();
            insUpd.lastname = tv_LastName.getText().toString();
            insUpd.streetaddress = tv_Address.getText().toString();
            insUpd.email = tv_Email.getText().toString();
            insUpd.primaryphone = tv_PrimaryPhone.getText().toString();
            insUpd.secondaryphone = tv_SecondaryPhone.getText().toString();
            insUpd.city = tv_City.getText().toString();
            insUpd.state = tv_State.getText().toString();
            insUpd.zipcode = tv_ZipCode.getText().toString();

            String addr = null;

            if (insUpd.streetaddress != null && !insUpd.streetaddress.trim().equals("")) {
                addr = insUpd.streetaddress;
                if (insUpd.city != null && !insUpd.city.trim().equals("")) {
                    addr = addr + ", " + insUpd.city;
                    if (insUpd.state != null && !insUpd.state.trim().equals("")) {
                        addr = addr + ", " + insUpd.state;
                        if (insUpd.zipcode != null && !insUpd.zipcode.trim().equals("")) {
                            addr = addr + " " + insUpd.zipcode;
                        }
                    }
                }
            }


            EditProfileAsync asyncTask = new EditProfileAsync(this, addr, insUpd, profileId, new EditProfileAsync.OnTaskCompleted() {
                @Override
                public void taskCompleted(Boolean results) {

                    if (results) {

                        Toast.makeText(getApplicationContext(), "Profile has been saved", Toast.LENGTH_LONG).show();

                        _appPrefs.putBoolean("ProfileSet", true);

                        if (profileId == null) {
                            Intent ai = new Intent(getApplicationContext(), ProfileList.class);
                            startActivity(ai);

                            finish();
                        } else {
                            setResult(RESULT_OK, new Intent());
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to save profile. Please try again!!", Toast.LENGTH_LONG).show();
                    }
                }
            });
            asyncTask.execute();
        }
    }

    public boolean Validation() {

        boolean results = false;

        if (tv_FirstName.getText().toString().trim().length() == 0) {
            tv_FirstName.setError("First name is required!");
            results = true;
        }

        if (tv_LastName.getText().toString().trim().length() == 0) {
            tv_LastName.setError("Last name is required!");
            results = true;
        }

        if (!Valid.isValidEmail(tv_Email.getText().toString()) || tv_Email.getText().toString().trim().length() == 0) {
            tv_Email.setError("Email Address Required or Invalid!");
            results = true;
        }

        if (tv_PrimaryPhone.getText().toString().trim().length() > 0 && digitCount(tv_PrimaryPhone.getText().toString().trim()) != 10) {
            tv_PrimaryPhone.setError("Primary Phone Is Invalid!");
            tv_PrimaryPhone.requestFocus();
            tv_PrimaryPhone.setFocusableInTouchMode(true);
            results = true;
        }

        if (tv_SecondaryPhone.getText().toString().trim().length() > 0 && digitCount(tv_SecondaryPhone.getText().toString()) != 10) {
            tv_SecondaryPhone.setError("Secondary Phone Is Invalid!");
            tv_SecondaryPhone.requestFocus();
            tv_SecondaryPhone.setFocusableInTouchMode(true);
            results = true;
        }

//
//        if(tv_Address.getText().toString().trim().length() == 0  && (tv_City.getText().toString().trim().length() > 0 || tv_State.getText().toString().trim().length() > 0 || tv_ZipCode.getText().toString().trim().length() > 0)) {
//            tv_Address.setError("Street Address Is Required!");
//            results = true;
//        }
//
//        if(tv_City.getText().toString().trim().length() == 0  && (tv_Address.getText().toString().trim().length() > 0 || tv_State.getText().toString().trim().length() > 0 || tv_ZipCode.getText().toString().trim().length() > 0)) {
//            tv_City.setError("City Is Required!");
//            results = true;
//        }
//
//        if(tv_State.getText().toString().trim().length() == 0  && (tv_City.getText().toString().trim().length() > 0 || tv_Address.getText().toString().trim().length() > 0 || tv_ZipCode.getText().toString().trim().length() > 0)) {
//            tv_State.setError("State Is Required!");
//            results = true;
//        }
//
//        if(tv_ZipCode.getText().toString().trim().length() == 0  && (tv_City.getText().toString().trim().length() > 0 || tv_State.getText().toString().trim().length() > 0 || tv_Address.getText().toString().trim().length() > 0)) {
//            tv_ZipCode.setError("Zipcode Is Required!");
//            results = true;
//        }

        return results;
    }

    private Integer digitCount(String s) {
        int count = 0;
        for (int i = 0, len = s.length(); i < len; i++) {
            if (Character.isDigit(s.charAt(i))) {
                count++;
            }
        }
        return count;
    }

}
