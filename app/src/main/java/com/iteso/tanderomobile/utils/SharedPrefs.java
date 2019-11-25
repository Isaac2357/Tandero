package com.iteso.tanderomobile.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.lang.ref.WeakReference;

/** SharedPrefs util class.*/
public class SharedPrefs {
    /** Context reference to be able of
     * get the editor.
     * */
    private WeakReference<Context> contextWeakReference;
    /**
     * Public constructor.
     * @param context Context reference.
     */
    public SharedPrefs(final Context context) {
        this.contextWeakReference = new WeakReference<Context>(context);
    }

    /**
     * Methos that saves and object into SharedPreferences.
     * @param key key of the value to be saved.
     * @param value value to be saved in SharedPreferences.
     */
    @SuppressLint("ApplySharedPref")
    public void saveToPrefs(final String key, final Object value) {
        if (contextWeakReference.get() != null) {
            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get());
            final SharedPreferences.Editor editor = prefs.edit();
            if (value instanceof Integer) {
                editor.putInt(key, ((Integer) value));
            } else if (value instanceof String) {
                editor.putString(key, value.toString());
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, ((Boolean) value));
            } else if (value instanceof Long) {
                editor.putLong(key, ((Long) value));
            } else if (value instanceof Float) {
                editor.putFloat(key, ((Float) value));
            } else if (value instanceof Double) {
                editor.putLong(key, Double.doubleToRawLongBits((double) value));
            }
            editor.commit(); // Save it synchrounously
        }
    }

    /**
     * Method to get info (values) from the SharedPreferences.
     * @param key Key of the value to be fetched.
     * @param defaultValue Default value.
     * @return value from SharedPreferences.
     */
    public Object getFromPrefs(final String key, final Object defaultValue) {
        if (contextWeakReference.get() != null) {
            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get());
            try {
                if (defaultValue instanceof String) {
                    return prefs.getString(key, defaultValue.toString());
                } else if (defaultValue instanceof Integer) {
                    return prefs.getInt(key, ((Integer) defaultValue));
                } else if (defaultValue instanceof Boolean) {
                    return prefs.getBoolean(key, ((Boolean) defaultValue));
                } else if (defaultValue instanceof Long) {
                    return prefs.getLong(key, ((Long) defaultValue));
                } else if (defaultValue instanceof Float) {
                    return prefs.getFloat(key, ((Float) defaultValue));
                } else if (defaultValue instanceof Double) {
                    return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits((double) defaultValue)));
                }
            } catch (Exception e) {
                if (e.getMessage() != null) {
                    Log.e("Execption", e.getMessage());
                }
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
