package weather.budi.com.weatherapps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import weather.budi.com.weatherapps.network.VolleyResultListener;
import weather.budi.com.weatherapps.network.VolleySingleton;
import weather.budi.com.weatherapps.utils.Constants;
import weather.budi.com.weatherapps.utils.Popup;
import weather.budi.com.weatherapps.utils.UrlComposer;
import weather.budi.com.weatherapps.vo.WeatherModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements VolleyResultListener {

    private final static int HOME = 1;

    Activity a;
    ProgressDialog progress;
    WeatherModel weatherModel;

    @Bind(R.id.tvCity)TextView tvCity;
    @Bind(R.id.tvWeatherInfo)TextView tvWeatherInfo;
    @Bind(R.id.tvDegree)TextView tvDegree;
    @Bind(R.id.ivIcon)ImageView ivIcon;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_main,
                container, false);
        a = getActivity();
        a.setTitle(Constants.TITLE_HOME);
        ButterKnife.bind(this,contentView);

        progress = Popup.showProgress(getResources().getString(R.string.text_loading), a);
        progress.setCancelable(true);

        try {
            getCurrentLocationWeather();
        }catch (Exception e){
            e.printStackTrace();
        }
        return contentView;
    }

    private void getCurrentLocationWeather(){
        String url = UrlComposer.composeTodayWeather(1642907,Constants.UNIT_CELCIUS);
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
        tvWeatherInfo.setText(weatherModel.getWeather().get(0).getDescription());

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
