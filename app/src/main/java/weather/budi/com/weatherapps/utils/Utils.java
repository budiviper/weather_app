package weather.budi.com.weatherapps.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Utils {
    public static void Log(String msg, String value) {
        if (Constants.MODE_DEV) {
            Log.e(Constants.TAG, msg + " " + value);
        }
    }

    public static void toast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(20);
        toast.show();
    }

    public static long getCurentTime() {
        return new Date().getTime();
    }

    public static boolean isData(Context context) {
       return isConnectedMobile(context) && !isConnectedWifi(context);
    }
    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static boolean isConnectedWifi(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isConnectedMobile(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }


    public static int isJSONValid(String test) {
        int result = -1;
        if (StringUtils.isEmpty(test)) {
            return -1;
        }else {
            try {
                new JSONObject(test);
                result = 0;
            } catch (JSONException ex) {
                try {
                    new JSONArray(test);
                    result = 1;
                } catch (JSONException ex1) {
                    return -1;
                }
            }
            return result;
        }
    }
}