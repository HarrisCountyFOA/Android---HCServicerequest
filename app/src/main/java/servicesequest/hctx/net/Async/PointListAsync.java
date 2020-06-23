package servicesequest.hctx.net.Async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import servicesequest.hctx.net.DAL.PointDataManager;
import servicesequest.hctx.net.DAL.ServiceRequestDbHelper;
import servicesequest.hctx.net.Model.GeoPoint;
import servicesequest.hctx.net.R;


public class PointListAsync extends AsyncTask<Void, Void, List<GeoPoint>> {
    private OnTaskCompleted listener;
    private ProgressDialog pd;
    private Context _Context;
    ServiceRequestDbHelper dbHelper;
    private List<GeoPoint> _pointlist;

    public interface OnTaskCompleted {
        void taskCompleted(List<GeoPoint> results);
    }

    public PointListAsync(Context context, OnTaskCompleted listener) {
        this.listener = listener;
        _Context = context;
        pd = new ProgressDialog(_Context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(_Context, R.style.MyDialogTheme);
        pd.setTitle("Loading Map");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected List<GeoPoint> doInBackground(Void... arg0) {

        dbHelper = new ServiceRequestDbHelper(_Context);
        final PointDataManager datamanager = new PointDataManager();
        _pointlist = datamanager.getAllPoints(dbHelper);

        return _pointlist;
    }

    @Override
    protected void onPostExecute(List<GeoPoint> results) {
        super.onPostExecute(results);
        pd.dismiss();
        listener.taskCompleted(results);
    }
}
