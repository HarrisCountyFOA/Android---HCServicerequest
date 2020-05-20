package servicesequest.hctx.net;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import servicesequest.hctx.net.Utility.AppPreferences;

public class TermsOfUseActivity extends AppCompatActivity {

    BottomNavigationView navigation;
    AppPreferences _appPrefs;
    TextView txtTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        _appPrefs = new AppPreferences(this, "UserProfile");
        txtTerms = findViewById(R.id.txtTerms);

        String htmlAsString = getString(R.string.large_text);
        txtTerms.setText(Html.fromHtml(htmlAsString));

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
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

                    boolean profileSet = _appPrefs.getBoolean("ProfileSet");

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
