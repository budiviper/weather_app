package weather.budi.com.weatherapps.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import weather.budi.com.weatherapps.R;
import weather.budi.com.weatherapps.adapter.ForecastAdapter;
import weather.budi.com.weatherapps.network.VolleyResultListener;
import weather.budi.com.weatherapps.network.VolleySingleton;
import weather.budi.com.weatherapps.utils.Constants;
import weather.budi.com.weatherapps.utils.Popup;
import weather.budi.com.weatherapps.utils.StringUtils;
import weather.budi.com.weatherapps.utils.UrlComposer;
import weather.budi.com.weatherapps.vo.ForecastListVO;
import weather.budi.com.weatherapps.vo.ForecastModel;
import weather.budi.com.weatherapps.vo.WeatherModel;


public class DetailFragment extends Fragment implements VolleyResultListener{

    private final static int HEADER_WEATHER = 1;
    private final static int FORECAST_WEATHER = 2;

    Activity a;
    ProgressDialog progress;
    WeatherModel weatherModel;
    ForecastModel forecastModel;

    private String cName;
    private ForecastAdapter forecastAdapter;
    private List<ForecastListVO> lvo;
    private RecyclerView.LayoutManager rvLayoutManager;

    @Bind(R.id.rvDetail)RecyclerView rvDetail;
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

        lvo = new ArrayList<ForecastListVO>();
        forecastAdapter = new ForecastAdapter(a,lvo);

        rvDetail.setHasFixedSize(true);
        rvDetail.setAdapter(forecastAdapter);
        rvLayoutManager = new LinearLayoutManager(a);
        rvDetail.setLayoutManager(rvLayoutManager);

        try {
            cName = getArguments().getString("city_name");
            getCityWeatherHeader(cName,Constants.UNIT_CELCIUS);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentView;
    }

    private void getCityWeatherHeader(String cityname, String unit) {

        String url="";
        url = UrlComposer.composeCurrentWeatherByCityName(cityname,unit);

        VolleySingleton.getInstance(a).
                addRequestQue(HEADER_WEATHER, url, a, DetailFragment.class, this);
    }

    private void getForecastDaily(String cityname, String unit, int totalDay){
        String url="";
        url = UrlComposer.composeForecastDaily(cityname,totalDay,Constants.UNIT_CELCIUS);

        VolleySingleton.getInstance(a).
                addRequestQue(FORECAST_WEATHER, url, a, DetailFragment.class, this);
    }

    private void headerResult(String result){

        if(Constants.MODE_DEV)
            System.out.println("JSON result: " + result);

        try{
            Gson gson = new Gson();
            weatherModel=new WeatherModel();
            weatherModel = gson.fromJson(result, WeatherModel.class);

            if(weatherModel.getId()>0)
                setDisplayHeaderInfo(weatherModel);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
//            if(progress.isShowing())
//                progress.dismiss();
            getForecastDaily(cName,Constants.UNIT_CELCIUS,7);
        }
    }

    private void forecastResult(String result){

        if(Constants.MODE_DEV)
            System.out.println("JSON result: " + result);

        try{
            Gson gson = new Gson();
            forecastModel=new ForecastModel();
            forecastModel = gson.fromJson(result, ForecastModel.class);

            if(forecastModel.getCity().getId()>0)
                setForecastDisplay(forecastModel);
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }

    private void setDisplayHeaderInfo(WeatherModel weatherModel){
        tvCity.setText(weatherModel.getName());
        tvWeatherInfo.setText(StringUtils.toTitleCase(weatherModel.getWeather().get(0).getDescription()));

        DecimalFormat format = new DecimalFormat("#");

        tvTemp.setText("" + format.format(weatherModel.getMain().getTemp()) + (char) 0x00B0);

        Glide.with(a)
                .load(UrlComposer.composeWeatherIcon(weatherModel.getWeather().get(0).getIcon()))
                .into(ivIcon);
    }

    private void setForecastDisplay(ForecastModel weatherModel){
        try{
            lvo = new ArrayList<ForecastListVO>();

            for(ForecastListVO vo:weatherModel.getList()){
                lvo.add(vo);
            }

            forecastAdapter.setData(lvo);
        }catch(Exception e){

        }finally {
            if(progress.isShowing())
                progress.dismiss();
        }
    }

    @Override
    public void onSuccess(int id, String result) {
        switch (id) {
            case HEADER_WEATHER:
                headerResult(result);
                break;
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
