package servicesequest.hctx.net.Async;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.Random;

import servicesequest.hctx.net.DAL.RequestDataManager;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.Request;
import servicesequest.hctx.net.R;


public class RequestAsync extends AsyncTask<Void, Void, Request> {
    private OnTaskCompleted listener;
    private ProgressDialog pd;
    private Context _Context;
    ServiceRequestDbHelper dbHelper;
    private Request _request;
    private String id;

    public interface OnTaskCompleted {
        void taskCompleted(Request results);
    }

    public RequestAsync(Context context, String id, OnTaskCompleted listener) {
        this.listener = listener;
        _Context = context;
        this.id = id;
        pd = new ProgressDialog(_Context);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(_Context, R.style.MyDialogTheme);
        pd.setTitle("Loading Reports");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected Request doInBackground(Void... arg0) {

        dbHelper = new ServiceRequestDbHelper(_Context);
        RequestDataManager datamanager = new RequestDataManager();
        _request = datamanager.getRequestByID(dbHelper, this.id);
        return _request;
    }

    @Override
    protected void onPostExecute(Request results) {
        super.onPostExecute(results);
        pd.dismiss();
        listener.taskCompleted(results);
    }

}
