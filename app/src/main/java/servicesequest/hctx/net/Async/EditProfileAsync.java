package servicesequest.hctx.net.Async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import servicesequest.hctx.net.DAL.ProfileDataManager;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.Profile;
import servicesequest.hctx.net.R;
import servicesequest.hctx.net.Utility.GeocodingLocation;


public class EditProfileAsync extends AsyncTask<Void, Void, Boolean> {
    private OnTaskCompleted listener;
    private ProgressDialog pd;
    private Context _Context;
    private Profile _p;
    private String _profileId;
    String _address;

    public interface OnTaskCompleted {
        void taskCompleted(Boolean results);
    }

    public EditProfileAsync(Context context, String address, Profile p, String profileId, OnTaskCompleted listener) {
        this.listener = listener;
        _Context = context;
        _address = address;
        _p = p;
        _profileId = profileId;
        pd = new ProgressDialog(_Context);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(_Context, R.style.MyDialogTheme);
        pd.setTitle("Verifying Address");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected Boolean doInBackground(Void... arg0) {

        try {
            ServiceRequestDbHelper dbHelper = new ServiceRequestDbHelper(_Context);
            ProfileDataManager manager = new ProfileDataManager();

            String precinct = (_address == null) ? "0" : GeocodingLocation.geoCode(_address, _Context);

            if (precinct != null) {
                this._p.precinct = precinct;
            } else {
                this._p.precinct = "0";
            }

            if (_profileId != null && !_profileId.equals("")) {
                this._p._id = _profileId;
                manager.updateProfile(dbHelper, this._p);
            } else {
                manager.insertProfile(dbHelper, this._p);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    @Override
    protected void onPostExecute(Boolean results) {
        super.onPostExecute(results);
        pd.dismiss();
        listener.taskCompleted(results);
    }
}
