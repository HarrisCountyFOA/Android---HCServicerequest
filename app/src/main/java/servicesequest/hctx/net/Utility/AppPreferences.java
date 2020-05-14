package servicesequest.hctx.net.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
	private SharedPreferences _sharedPrefs;
	private Editor _prefsEditor;


	public AppPreferences(Context context, String name) {
		_sharedPrefs = context.getSharedPreferences(name, Activity.MODE_PRIVATE);
		_prefsEditor = _sharedPrefs.edit();
	}

	public void putInt(String key, int value) {
		_prefsEditor.putInt(key, value);
		_prefsEditor.apply();
	}

	public void putLong(String key, long value) {
		_prefsEditor.putLong(key, value);
		_prefsEditor.apply();
	}

	public void putDouble(String key, double value) {
		putString(key, String.valueOf(value));
	}

	public void putString(String key, String value) {
		_prefsEditor.putString(key, value);
		_prefsEditor.apply();
	}

	public void putBoolean(String key, boolean value) {
		_prefsEditor.putBoolean(key, value);
		_prefsEditor.apply();
	}

	public int getInt(String key) {
		return _sharedPrefs.getInt(key, 0);
	}

	public long getLong(String key) {
		return _sharedPrefs.getLong(key, 0l);
	}

	public String getString(String key) {
		return _sharedPrefs.getString(key, "");
	}

	public double getDouble(String key) {
		String number = getString(key);
		try {
			double value = Double.parseDouble(number);
			return value;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public boolean getBoolean(String key) {
		return _sharedPrefs.getBoolean(key, false);
	}

	public void remove(String key) {
		_prefsEditor.remove(key);
		_prefsEditor.apply();
	}

	public void clear() {
		_prefsEditor.clear();
		_prefsEditor.apply();
	}

}
