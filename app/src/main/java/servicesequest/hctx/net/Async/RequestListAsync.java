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

        ContactDataManager cdatamanager = new ContactDataManager();
        List<contact> _contactlist = cdatamanager.getAllContacts(dbHelper);

        if(_contactlist.size() == 0) {
            List<contact> fCon = getContacts();
            if (fCon != null && fCon.size() > 0) {
                for (int i = 0; i < fCon.size(); i++) {
                    contact jCon = (contact) fCon.get(i);
                    contact dbCon = cdatamanager.getContactByPrecinct(dbHelper, jCon.precinct);

                    if (dbCon._id != null && !dbCon._id.equals("")) {
                        cdatamanager.updateContact(dbHelper, jCon);
                    } else {
                        cdatamanager.insertContact(dbHelper, jCon);
                    }
                }
            }
        }

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
