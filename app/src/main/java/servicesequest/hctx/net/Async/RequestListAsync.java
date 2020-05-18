package servicesequest.hctx.net.Async;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import servicesequest.hctx.net.DAL.ContactDataManager;
import servicesequest.hctx.net.DAL.RequestDataManager;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.Request;
import servicesequest.hctx.net.Model.contact;
import servicesequest.hctx.net.R;

import static servicesequest.hctx.net.DAL.ContactDataManager.insertUpdateContacts;
import static servicesequest.hctx.net.Utility.GeocodingLocation.getContacts;


public class RequestListAsync extends AsyncTask<Void, Void, List<Request>> {
    private OnTaskCompleted listener;
    private ProgressDialog pd;
    private Context _Context;
    ServiceRequestDbHelper dbHelper;
    private List<Request> _requestlist;

    public interface OnTaskCompleted {
        void taskCompleted(List<Request> results);
    }

    public RequestListAsync(Context context, OnTaskCompleted listener) {
        this.listener = listener;
        _Context = context;
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
    protected List<Request> doInBackground(Void... arg0) {


        dbHelper = new ServiceRequestDbHelper(_Context);
        insertUpdateContacts(dbHelper, _Context, false);

        RequestDataManager datamanager = new RequestDataManager();
        _requestlist = datamanager.getAllRequests(dbHelper);
        return _requestlist;
    }

    @Override
    protected void onPostExecute(List<Request> results) {
        super.onPostExecute(results);
        pd.dismiss();
        listener.taskCompleted(results);
    }
}
