package servicesequest.hctx.net.Async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import servicesequest.hctx.net.DAL.ContactDataManager;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.contact;
import servicesequest.hctx.net.R;

import static servicesequest.hctx.net.Utility.GeocodingLocation.getContacts;


public class ContactListAsync extends AsyncTask<Void, Void, List<contact>> {
    private OnTaskCompleted listener;
    private ProgressDialog pd;
    private Context _Context;
    ServiceRequestDbHelper dbHelper;
    private List<contact> _contactlist;

    public interface OnTaskCompleted {
        void taskCompleted(List<contact> results);
    }

    public ContactListAsync(Context context, OnTaskCompleted listener) {
        this.listener = listener;
        _Context = context;
        pd = new ProgressDialog(_Context);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(_Context, R.style.MyDialogTheme);
        pd.setTitle("Loading contacts");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected List<contact> doInBackground(Void... arg0) {

        List<contact> fCon = getContacts();
        dbHelper = new ServiceRequestDbHelper(_Context);
        ContactDataManager datamanager = new ContactDataManager();

        if (fCon != null && fCon.size() > 0) {
            for (int i = 0; i < fCon.size(); i++) {
                contact jCon = (contact) fCon.get(i);
                contact dbCon = datamanager.getContactByPrecinct(dbHelper, jCon.precinct);

                if (dbCon._id != null && !dbCon._id.equals("")) {
                    datamanager.updateContact(dbHelper, jCon);
                } else {
                    datamanager.insertContact(dbHelper, jCon);
                }
            }
            _contactlist = fCon;
        } else {
            _contactlist = datamanager.getAllContacts(dbHelper);
        }

        return _contactlist;
    }

    @Override
    protected void onPostExecute(List<contact> results) {
        super.onPostExecute(results);
        pd.dismiss();
        listener.taskCompleted(results);
    }
}
