package a.com.androidassigment.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {



    public static String getJsonString(JSONObject obj, String key) {

        try {
            if (obj.has(key) && (obj.getString(key) != null)
                    && (!obj.getString(key).equals("null"))) {
                return obj.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            final NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
                return true;
            }

        }
        return false;

    }


}
