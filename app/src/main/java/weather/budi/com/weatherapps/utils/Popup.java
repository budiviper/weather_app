package weather.budi.com.weatherapps.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class Popup {

    public static ProgressDialog showProgress(String message, Context ctx) {
        ProgressDialog progress = new ProgressDialog(ctx);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage(message);
        progress.show();
        return progress;
    }
}
