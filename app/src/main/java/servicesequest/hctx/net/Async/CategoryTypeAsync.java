package servicesequest.hctx.net.Async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import servicesequest.hctx.net.R;
import servicesequest.hctx.net.Utility.GeocodingLocation;


public class CategoryTypeAsync extends AsyncTask<Void, Void, Boolean> {
	private OnTaskCompleted listener;
	//private ProgressDialog pd;
	private Context _Context;


	public interface OnTaskCompleted{
		void taskCompleted(Boolean results);
	}

	public CategoryTypeAsync(Context context, OnTaskCompleted listener){
		this.listener=listener;
		_Context = context;
		//pd = new ProgressDialog(_Context);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		//pd = new ProgressDialog(_Context, R.style.MyDialogTheme);
		//pd.setTitle("Verifying Address");
		//pd.setMessage("Please wait...");
		//pd.setCancelable(false);
		//pd.show();
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
			return GeocodingLocation.getRequestCategory();

	}

	@Override
	protected void onPostExecute(Boolean results) {
		super.onPostExecute(results);
		//pd.dismiss();
		listener.taskCompleted(results);
	}
}
