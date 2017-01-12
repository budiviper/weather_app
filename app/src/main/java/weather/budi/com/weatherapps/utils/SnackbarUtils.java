package weather.budi.com.weatherapps.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.widget.TextView;

import weather.budi.com.weatherapps.R;

/**
 * Created by Budi on 01/12/2017.
 */

public class SnackbarUtils extends Activity {

    public static void snackbarMessage(android.view.View view, Context context, String msg, String caption){

        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                .setAction(caption, new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {
                        // PUT ACTION HERE
                    }
                });

        android.view.View sbView = snackbar.getView();
        TextView sbText = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        sbText.setTextColor(Color.WHITE);
        sbText.setTextSize(16);

        TextView sbAction = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_action);
        //sbAction.setTypeface(null,Typeface.BOLD);
        sbAction.setTextColor(context.getResources().getColor( R.color.yellow_solid));
        sbAction.setTextSize(16);


        snackbar.show();

    }
}
