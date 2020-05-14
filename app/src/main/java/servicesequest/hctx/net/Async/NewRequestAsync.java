package servicesequest.hctx.net.Async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import servicesequest.hctx.net.DAL.ProfileDataManager;
import servicesequest.hctx.net.DAL.RequestDataManager;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.Profile;
import servicesequest.hctx.net.Model.Request;
import servicesequest.hctx.net.R;
import servicesequest.hctx.net.Utility.GeocodingLocation;


public class NewRequestAsync extends AsyncTask<Void, Void, Boolean> {
    private OnTaskCompleted listener;
    private ProgressDialog pd;
    private Context _Context;
    private Request _R;

    public interface OnTaskCompleted {
        void taskCompleted(Boolean results);
    }

    public NewRequestAsync(Context context, Request R, OnTaskCompleted listener) {
        this.listener = listener;
        _Context = context;
        _R = R;
        pd = new ProgressDialog(_Context);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(_Context, R.style.MyDialogTheme);
        pd.setTitle("Processing Request");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected Boolean doInBackground(Void... arg0) {

        try {
            ServiceRequestDbHelper dbHelper = new ServiceRequestDbHelper(_Context);
            RequestDataManager manager = new RequestDataManager();

            manager.insertRequest(dbHelper, _R);
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
