package servicesequest.hctx.net.Async;

import android.content.Context;
import android.os.AsyncTask;

import servicesequest.hctx.net.Utility.GeocodingLocation;


public class RequestNewTypeAsync extends AsyncTask<Void, Void, Boolean> {
	private OnTaskCompleted listener;
	private String _rcParam;
	private String _rcOrder;

	public interface OnTaskCompleted{
		void taskCompleted(Boolean results);
	}

	public RequestNewTypeAsync(String rcParam, String rcOrder, OnTaskCompleted listener){
		this.listener=listener;
		_rcParam = rcParam;
		_rcOrder = rcOrder;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
			return GeocodingLocation.getRequestTypeFromCategory(_rcParam, _rcOrder);

	}

	@Override
	protected void onPostExecute(Boolean results) {
		super.onPostExecute(results);
		listener.taskCompleted(results);
	}
}
