package weather.budi.com.weatherapps.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import weather.budi.com.weatherapps.App;
import weather.budi.com.weatherapps.R;
import weather.budi.com.weatherapps.adapter.CityAdapter;
import weather.budi.com.weatherapps.controller.RealmController;
import weather.budi.com.weatherapps.network.VolleyResultListener;
import weather.budi.com.weatherapps.network.VolleySingleton;
import weather.budi.com.weatherapps.utils.Constants;
import weather.budi.com.weatherapps.utils.Popup;
import weather.budi.com.weatherapps.utils.StringUtils;
import weather.budi.com.weatherapps.utils.UrlComposer;
import weather.budi.com.weatherapps.vo.CityVO;
import weather.budi.com.weatherapps.vo.WeatherModel;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by Budi on 1/13/2017.
 */

public class ListCityFragment extends Fragment implements VolleyResultListener{

    Context ctx;
    Activity a;
    ProgressDialog progress;

    @Bind(R.id.cardList)RecyclerView rv;
    @Bind(R.id.fabAdd)FloatingActionButton fabAdd;

    WeatherModel weatherModel;
    private Realm mRealm;
    private List<CityVO> lvo;
    private CityAdapter cityAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;

    private final static int SEARCH_CITY = 1;
    private final static int SEARCH_CITY_TIME = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_list_city_layout,
                container, false);
        a=getActivity();
        ButterKnife.bind(this,contentView);

        progress = Popup.showProgress(getResources().getString(R.string.text_loading), a);
        progress.setCancelable(true);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPlace();
            }
        });

        lvo = new ArrayList<CityVO>();
        cityAdapter = new CityAdapter(a,lvo);

        rv.setHasFixedSize(true);
        rv.setAdapter(cityAdapter);
        rvLayoutManager = new LinearLayoutManager(a);
        rv.setLayoutManager(rvLayoutManager);

        setAllCityDisplay();

        return contentView;
    }

    private void setAllCityDisplay(){
        try{
            lvo=new ArrayList<CityVO>();

            RealmResults<CityVO> realmResults = RealmController.with(this).getAllCity();

            if(realmResults.size()>0)
                for(CityVO a:realmResults){
                    //lvo.add(a);
                    getCityTime(a.getCityName());
                }
            else
                if(progress.isShowing())
                    progress.dismiss();
        }catch(Exception e){

        }
    }

    private void getCityTime(String cityName){
        String url="";
        url = UrlComposer.composeCurrentWeatherByCityName(cityName,Constants.UNIT_CELCIUS);

        if(true==Constants.MODE_DEV)
            System.out.println("URL:" + url);

        VolleySingleton.getInstance(a).
                addRequestQue(SEARCH_CITY_TIME, url, a, ListCityFragment.class, this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                if(!progress.isShowing())
                    progress.show();

                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(a, data);

                LatLng latlang = place.getLatLng();

                if(Constants.MODE_DEV) {
                    System.out.println("Lat: " + latlang.latitude);
                    System.out.println("Lon: " + latlang.longitude);
                    System.out.println("Place: " + place.getName());
                }

                final StringBuilder sb = new StringBuilder(place.getName().length());
                sb.append(place.getName());

                getCityWeather(sb.toString());
                //getLocationWeather(round(latlang.latitude),round(latlang.longitude));

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(a, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        }catch (Exception e){

        }
    }

    private void getCityWeather(String cityName){
        String url="";
        url = UrlComposer.composeCurrentWeatherByCityName(cityName,Constants.UNIT_CELCIUS);

        VolleySingleton.getInstance(a).
                addRequestQue(SEARCH_CITY, url, a, ListCityFragment.class, this);
    }

    private void searchResult(String result){

        if(true==Constants.MODE_DEV)
            System.out.println("JSON result: " + result);

        try{
            Gson gson = new Gson();
            weatherModel=new WeatherModel();
            weatherModel = gson.fromJson(result, WeatherModel.class);

            CityVO dbVO = RealmController.with(this).getCityById(weatherModel.getId());

            if(dbVO==null){

                long time = weatherModel.getDt();

                if(true==Constants.MODE_DEV)
                    System.out.println("WAKTU: " + StringUtils.convertEpoch(time,"hh:mm a"));

                // ADD TO REALM DATABASE
                mRealm = Realm.getInstance(App.getInstance());
                mRealm.beginTransaction();
                CityVO cityVO = mRealm.createObject(CityVO.class);
                cityVO.setLat(weatherModel.getCoord().getLat());
                cityVO.setLon(weatherModel.getCoord().getLon());
                cityVO.setTime(StringUtils.convertEpoch(time,"hh:mm a"));
                cityVO.setId(weatherModel.getId());
                cityVO.setCityName(weatherModel.getName());
                cityVO.setTemp(weatherModel.getMain().getTemp());
                mRealm.commitTransaction();

                lvo.add(cityVO);

            }

            cityAdapter.setData(lvo);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(progress.isShowing())
                progress.dismiss();
        }
    }

    private void cityTimeResult(String result){
        try{
            Gson gson = new Gson();
            weatherModel=new WeatherModel();
            weatherModel = gson.fromJson(result, WeatherModel.class);

            CityVO dbVO = RealmController.with(this).getCityById(weatherModel.getId());

            long time = weatherModel.getDt();

            if(true==Constants.MODE_DEV)
                System.out.println("WAKTU: " + StringUtils.convertEpoch(time,"hh:mm a"));

            mRealm = Realm.getInstance(App.getInstance());
            mRealm.beginTransaction();

            if(dbVO==null){
                // ADD TO REALM DB

                CityVO cityVO = mRealm.createObject(CityVO.class);
                cityVO.setLat(weatherModel.getCoord().getLat());
                cityVO.setLon(weatherModel.getCoord().getLon());
                cityVO.setTime(StringUtils.convertEpoch(time,"hh:mm a"));
                cityVO.setId(weatherModel.getId());
                cityVO.setCityName(weatherModel.getName());
                cityVO.setTemp(weatherModel.getMain().getTemp());
                lvo.add(cityVO);
            }else{
                // UPDATE REALM DB

                dbVO.setTime(StringUtils.convertEpoch(time,"hh:mm a"));
                lvo.add(dbVO);
            }

            mRealm.commitTransaction();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cityAdapter.setData(lvo);

            if(progress.isShowing())
                progress.dismiss();
        }
    }

    @Override
    public void onSuccess(int id, String result) {
        switch (id) {
            case SEARCH_CITY:
                searchResult(result);
                break;
            case SEARCH_CITY_TIME:
                cityTimeResult(result);
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
