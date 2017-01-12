package weather.budi.com.weatherapps;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import weather.budi.com.weatherapps.gps.GPSTracker;
import weather.budi.com.weatherapps.network.VolleyResultListener;
import weather.budi.com.weatherapps.network.VolleySingleton;
import weather.budi.com.weatherapps.utils.Constants;
import weather.budi.com.weatherapps.utils.Popup;
import weather.budi.com.weatherapps.utils.SnackbarUtils;
import weather.budi.com.weatherapps.utils.StringUtils;
import weather.budi.com.weatherapps.utils.ToastUtils;
import weather.budi.com.weatherapps.utils.UrlComposer;
import weather.budi.com.weatherapps.vo.WeatherModel;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements VolleyResultListener {

    private final static int HOME = 1;

    Activity a;
    Context ctx;
    ProgressDialog progress;
    WeatherModel weatherModel;

    @Bind(R.id.tvCity) TextView tvCity;
    @Bind(R.id.tvWeatherInfo) TextView tvWeatherInfo;
    @Bind(R.id.tvDegree) TextView tvDegree;
    @Bind(R.id.ivIcon) ImageView ivIcon;


    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_main,
                container, false);
        a = getActivity();
        ctx = getActivity();
        a.setTitle(Constants.TITLE_HOME);
        ButterKnife.bind(this, contentView);

        progress = Popup.showProgress(getResources().getString(R.string.text_loading), a);
        progress.setCancelable(true);

        try {
            getCurrentLocationWeather();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentView;
    }

    private void getCurrentLocationWeather() {

        GPSTracker gps = new GPSTracker(a);
        if(gps.canGetLocation()==true){
            try {
                //ToastUtils.message(a,"Coordinate: " + gps.getLatitude() + " - " + gps.getLongitude());
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        String url = UrlComposer.composeCurrentWeatherByPosition(gps.getLongitude(),gps.getLatitude(),Constants.UNIT_CELCIUS);

        if(true==Constants.MODE_DEV)
            System.out.println("URL:" + url);

        VolleySingleton.getInstance(a).
                addRequestQue(HOME, url, a, MainFragment.class, this);
    }

    private void homeResult(String result){

        if(true==Constants.MODE_DEV)
            System.out.println("JSON result: " + result);

        try{
            Gson gson = new Gson();
            weatherModel=new WeatherModel();
            weatherModel = gson.fromJson(result, WeatherModel.class);

            if(weatherModel.getId()>0)
                setDisplay(weatherModel);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(progress.isShowing())
                progress.dismiss();
        }
    }

    private void setDisplay(WeatherModel weatherModel){
        tvCity.setText(weatherModel.getName());
        tvWeatherInfo.setText(StringUtils.toTitleCase(weatherModel.getWeather().get(0).getDescription()));

        DecimalFormat format = new DecimalFormat("#");

        tvDegree.setText("" + format.format(weatherModel.getMain().getTemp()) + (char) 0x00B0);

        Glide.with(a)
                .load(UrlComposer.composeWeatherIcon(weatherModel.getWeather().get(0).getIcon()))
                .into(ivIcon);
    }

    @Override
    public void onSuccess(int id, String result) {
        switch (id) {
            case HOME:
                homeResult(result);
                break;
        }
    }

    @Override
    public void onFinish(int id) {

    }

    @Override
    public void onStart(int id) {

    }

    @Override
    public boolean onError(int id, String msg) {
        return false;
    }
}
