package servicesequest.hctx.net.Async;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import servicesequest.hctx.net.DAL.ContactDataManager;
import servicesequest.hctx.net.DAL.ProfileDataManager;
import servicesequest.hctx.net.DAL.RequestDataManager;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.Profile;
import servicesequest.hctx.net.Model.Request;
import servicesequest.hctx.net.Model.contact;
import servicesequest.hctx.net.R;


public class ProfileListAsync extends AsyncTask<Void, Void, List<Profile>> {
    private OnTaskCompleted listener;
    private ProgressDialog pd;
    private Context _Context;
    ServiceRequestDbHelper dbHelper;
    private List<Profile> _requestlist;

    public interface OnTaskCompleted {
        void taskCompleted(List<Profile> results);
    }

    public ProfileListAsync(Context context, OnTaskCompleted listener) {
        this.listener = listener;
        _Context = context;
        pd = new ProgressDialog(_Context);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(_Context, R.style.MyDialogTheme);
        pd.setTitle("Loading Profile");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected List<Profile> doInBackground(Void... arg0) {

        dbHelper = new ServiceRequestDbHelper(_Context);
        ProfileDataManager datamanager = new ProfileDataManager();
        _requestlist = datamanager.getAllProfiles(dbHelper);

        if (_requestlist.size() > 0) {
            String precinct = _requestlist.get(0).precinct;

            if (!precinct.equals("0")) {
                ContactDataManager conManager = new ContactDataManager();
                contact comContact = conManager.getContactByPrecinct(dbHelper, "Precinct " + precinct);
                _requestlist.get(0).commContact = comContact;
            }
        }

        return _requestlist;
    }

    @Override
    protected void onPostExecute(List<Profile> results) {
        super.onPostExecute(results);
        pd.dismiss();
        listener.taskCompleted(results);
    }
}
