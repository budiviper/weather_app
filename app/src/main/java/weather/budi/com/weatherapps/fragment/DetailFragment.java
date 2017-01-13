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
import weather.budi.com.weatherapps.vo.WeatherModel;

import static android.app.Activity.RESULT_CANCELED;


public class DetailFragment extends Fragment implements VolleyResultListener, GoogleApiClient.OnConnectionFailedListener {

    private final static int HOME = 1;

    private double lat=0;
    private double lon=0;

    private GoogleApiClient mGoogleApiClient;

    Activity a;
    ProgressDialog progress;
    WeatherModel weatherModel;

    @Bind(R.id.tvVersion) TextView tvVersion;
    @Bind(R.id.tvCity) TextView tvCity;
    @Bind(R.id.tvWeatherInfo) TextView tvWeatherInfo;
    @Bind(R.id.tvDegree) TextView tvDegree;
    @Bind(R.id.ivIcon) ImageView ivIcon;
    @Bind(R.id.fab) FloatingActionButton fab;

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

        mGoogleApiClient = new GoogleApiClient
                .Builder(a)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this, this)
                .build();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                findPlace();
            }
        });

        PackageInfo pInfo = null;
        try {
            pInfo = a.getPackageManager().getPackageInfo(a.getPackageName(), 0);
            String version = pInfo.versionName;
            tvVersion.setText("v"+version);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        progress = Popup.showProgress(getResources().getString(R.string.text_loading), a);
        progress.setCancelable(true);

        try {
            lat = getArguments().getDouble("lat");
            lon = getArguments().getDouble("lon");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            getCurrentLocationWeather();
        }
        return contentView;
    }

    public void findPlace() {
        try {
            Intent intent =
                    new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(a);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
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

        String url="";

        if(lat!=0 && lon!=0){
            url = UrlComposer.composeCurrentWeatherByPosition(lon,lat,Constants.UNIT_CELCIUS);
        }else{
            url = UrlComposer.composeCurrentWeatherByPosition(gps.getLongitude(),gps.getLatitude(),Constants.UNIT_CELCIUS);
        }

        if(true==Constants.MODE_DEV)
            System.out.println("URL:" + url);

        VolleySingleton.getInstance(a).
                addRequestQue(HOME, url, a, DetailFragment.class, this);
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

    // A place has been received; use requestCode to track the request.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(a, data);

                LatLng latlang = place.getLatLng();

                final double latFin = latlang.latitude;
                final double lonFin = latlang.longitude;

                if(Constants.MODE_DEV) {
                    System.out.println("Lat: " + latFin);
                    System.out.println("Lon: " + lonFin);
                }


                Log.e("Tag", "Place: " + place.getName() + "," + place.getAddress() + place.getPhoneNumber());

                // DO SOMETHING HERE
                lat = latlang.latitude;
                lon = latlang.longitude;

                getCurrentLocationWeather();



            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(a, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
