package servicesequest.hctx.net.Async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.Random;

import servicesequest.hctx.net.DAL.ProfileDataManager;
import servicesequest.hctx.net.DAL.PushDataKsoap;
import servicesequest.hctx.net.DAL.RequestDataManager;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.Profile;
import servicesequest.hctx.net.Model.Request;
import servicesequest.hctx.net.R;
import servicesequest.hctx.net.Utility.GeocodingLocation;


public class NewRequestAsync extends AsyncTask<Void, Void, Boolean> {
    private OnTaskCompleted listener;
    private ProgressDialog pd;
    private WeakReference<Activity> weakActivity;
    private Request _R;
    private Request _P;

    public interface OnTaskCompleted {
        void taskCompleted(Boolean results);
    }

    public NewRequestAsync(Activity myActivity, Request R, OnTaskCompleted listener) {
        this.listener = listener;
        this.weakActivity = new WeakReference<>(myActivity);
        _R = R;
        pd = new ProgressDialog(weakActivity.get());

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(weakActivity.get(), R.style.MyDialogTheme);
        pd.setTitle("Processing Request");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected Boolean doInBackground(Void... arg0) {

        try {

            ServiceRequestDbHelper dbHelper = new ServiceRequestDbHelper(weakActivity.get());
            ProfileDataManager datamanager = new ProfileDataManager();
            Profile _P = datamanager.getProfileById(dbHelper, "1");


           String _UniqueId = UniqueID();
           Boolean results = new PushDataKsoap().passing(_P, _R, _UniqueId);

           if (results) {
               _R._UniqueId = _UniqueId;
               RequestDataManager manager = new RequestDataManager();
               manager.insertRequest(dbHelper, _R);
               dbHelper.close();
               return true;
           }
           else
           {
               dbHelper.close();
               return false;
           }

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


    private String UniqueID()
    {
        Random generator = new Random();

        int START = 100000000;
        int END = 199999999;

        long range = (long)START - (long)END + 1;
        long fraction = (long)(range * generator.nextDouble());
        int randomNumber =  (int)(fraction + START);

        return Integer.toString(randomNumber);
    }
}
