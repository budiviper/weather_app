package weather.budi.com.weatherapps.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import weather.budi.com.weatherapps.R;
import weather.budi.com.weatherapps.gps.GPSTracker;
import weather.budi.com.weatherapps.network.VolleyResultListener;
import weather.budi.com.weatherapps.network.VolleySingleton;
import weather.budi.com.weatherapps.utils.Constants;
import weather.budi.com.weatherapps.utils.Popup;
import weather.budi.com.weatherapps.utils.StringUtils;
import weather.budi.com.weatherapps.utils.UrlComposer;
import weather.budi.com.weatherapps.vo.ForecastModel;
import weather.budi.com.weatherapps.vo.WeatherModel;

import static android.app.Activity.RESULT_CANCELED;


public class DetailFragment extends Fragment implements VolleyResultListener{

    private final static int FORECAST_WEATHER = 1;

    Activity a;
    ProgressDialog progress;
    ForecastModel forecastModel;

    private String cName;

    @Bind(R.id.tvCity) TextView tvCity;
    @Bind(R.id.tvWeatherInfo) TextView tvWeatherInfo;
    @Bind(R.id.tvTemp) TextView tvTemp;
    @Bind(R.id.ivIcon) ImageView ivIcon;

    public DetailFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_detail_layout,
                container, false);
        a = getActivity();
        //a.setTitle(Constants.TITLE_HOME);
        ButterKnife.bind(this, contentView);

        progress = Popup.showProgress(getResources().getString(R.string.text_loading), a);
        progress.setCancelable(true);

        try {
            cName = getArguments().getString("city_name");
            getCurrentCityWeatherForecast(cName,Constants.UNIT_CELCIUS);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentView;
    }

    private void getCurrentCityWeatherForecast(String cityname, String unit) {

        String url="";
        url = UrlComposer.composeForecastByCityName(cityname,unit);


        if(true==Constants.MODE_DEV)
            System.out.println("URL:" + url);

        VolleySingleton.getInstance(a).
                addRequestQue(FORECAST_WEATHER, url, a, DetailFragment.class, this);
    }

    private void forecastResult(String result){

        if(true==Constants.MODE_DEV)
            System.out.println("JSON result: " + result);

        try{
            Gson gson = new Gson();
            forecastModel=new ForecastModel();
            forecastModel = gson.fromJson(result, ForecastModel.class);

            if(forecastModel.getCity().getId()>0)
                setDisplay(forecastModel);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(progress.isShowing())
                progress.dismiss();
        }
    }

    private void setDisplay(ForecastModel forecastModel){
        tvCity.setText(forecastModel.getCity().getName());
        tvWeatherInfo.setText(StringUtils.toTitleCase(forecastModel.getList().get(0).getWeather().get(0).getDescription()));

        DecimalFormat format = new DecimalFormat("#");

        tvTemp.setText("" + format.format(forecastModel.getList().get(0).getMain().getTemp()) + (char) 0x00B0);

        Glide.with(a)
                .load(UrlComposer.composeWeatherIcon(forecastModel.getList().get(0).getWeather().get(0).getIcon()))
                .into(ivIcon);
    }

    @Override
    public void onSuccess(int id, String result) {
        switch (id) {
            case FORECAST_WEATHER:
                forecastResult(result);
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
