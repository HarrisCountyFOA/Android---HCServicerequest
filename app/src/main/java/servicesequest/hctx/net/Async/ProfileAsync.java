package servicesequest.hctx.net.Async;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import servicesequest.hctx.net.DAL.ProfileDataManager;
import servicesequest.hctx.net.DAL.RequestDataManager;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.Profile;
import servicesequest.hctx.net.Model.Request;
import servicesequest.hctx.net.R;


public class ProfileAsync extends AsyncTask<Void, Void, Profile> {
    private OnTaskCompleted listener;
    private ProgressDialog pd;
    private Context _Context;
    ServiceRequestDbHelper dbHelper;
    private Profile _profile;
    private String id;

    public interface OnTaskCompleted {
        void taskCompleted(Profile results);
    }

    public ProfileAsync(Context context, String id, OnTaskCompleted listener) {
        this.listener = listener;
        _Context = context;
        this.id = id;
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
    protected Profile doInBackground(Void... arg0) {

        dbHelper = new ServiceRequestDbHelper(_Context);
        ProfileDataManager datamanager = new ProfileDataManager();
        _profile = datamanager.getProfileById(dbHelper, this.id);
        return _profile;
    }

    @Override
    protected void onPostExecute(Profile result) {
        super.onPostExecute(result);
        pd.dismiss();
        listener.taskCompleted(result);
    }
}
