package weather.budi.com.weatherapps.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import weather.budi.com.weatherapps.R;

public class ToastUtils extends Activity {

	public static void message(Context context, String msg) {
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_toast, null);
		TextView tvMsg = (TextView)view.findViewById(R.id.tvMessage);
		tvMsg.setText(msg);
		toast.setView(view);
		toast.setGravity(Gravity.BOTTOM, 0, 50);

		toast.show();
	}
}
