package servicesequest.hctx.net;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import servicesequest.hctx.net.Async.RequestAsync;
import servicesequest.hctx.net.DAL.RequestDataManager;
import servicesequest.hctx.net.DAL.ServiceRequestDbContract;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.Request;
import servicesequest.hctx.net.Utility.AppPreferences;
import servicesequest.hctx.net.Utility.Utils;

public class RequestDetailsActivity extends AppCompatActivity {

    String requestId;
    ServiceRequestDbHelper dbHelper;
    RequestDataManager manager;
    ImageView avatar;
    TextView txtName;
    TextView txvDate;
    TextView txvTime;
    TextView txvaddress;
    TextView txvComments;
    BottomNavigationView navigation;
    AppPreferences _appPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        avatar = findViewById(R.id.avatar);
        txtName = findViewById(R.id.txvName);
        txvDate = findViewById(R.id.txvDate);
        txvTime = findViewById(R.id.txvTime);
        txvaddress = findViewById(R.id.txvaddress);
        txvComments = findViewById(R.id.txvComments);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        _appPrefs = new AppPreferences(this, "UserProfile");

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            requestId = extras.getString(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_ID);

            if (requestId != null) {
                initScreen(requestId);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
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

    public static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private void initScreen(String id) {

        dbHelper = new ServiceRequestDbHelper(this);
        manager = new RequestDataManager();
        Request p = manager.getRequestByID(dbHelper, id);

        RequestAsync asyncTask = new RequestAsync(this, id, new RequestAsync.OnTaskCompleted() {
            @Override
            public void taskCompleted(Request p) {
                try {
                    if (p != null && p._id != null && !p._id.equals("")) {
                        if (p.Image != null && p.Image.length > 0) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(p.Image, 0, p.Image.length);
                            int height = bmp.getHeight();

                            float i = ((float)avatar.getWidth())/((float)bmp.getWidth());
                            float imageHeight = i * (height);

                            ConstraintLayout.LayoutParams parms = new ConstraintLayout.LayoutParams(avatar.getWidth(), (int) imageHeight);
                            avatar.setLayoutParams(parms);
                            avatar.setScaleType(ImageView.ScaleType.FIT_START);
                            avatar.setImageBitmap(bmp);

                            avatar.setVisibility(View.VISIBLE);
                        } else {
                            avatar.setVisibility(View.GONE);
                        }

                        txtName.setText(p.Image_Name);

                        try {

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            java.util.Date date = dateFormat.parse(p.DateTime);

                            txvDate.setText((new SimpleDateFormat("MMM dd, yyyy")).format(date));

                            txvTime.setText((new SimpleDateFormat("hh.mm aa")).format(date));
                        } catch (ParseException e) {
                            Toast.makeText(getApplicationContext(), "Failed to parse date", Toast.LENGTH_LONG).show();
                        }

                        txvaddress.setText(p.FullAddress);
                        txvComments.setText(p.Description);


                    } else {
                        Toast.makeText(getApplicationContext(), "No report found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    String Info = "There was a problem getting your report. Please try again later.<br><br>";
                    Utils.customPopMessge(getApplicationContext(), "Error", Info + ex.getMessage(), "error");
                }
            }
        });
        asyncTask.execute();
    }
}

