package weather.budi.com.weatherapps.vo;

import java.util.List;

/**
 * Created by Budi on 1/14/2017.
 */

public class ForecastModel {

    private ForecastCityVO city;
    private List<ForecastListVO> list;

    public ForecastCityVO getCity() {
        return city;
    }

    public void setCity(ForecastCityVO city) {
        this.city = city;
    }

    public List<ForecastListVO> getList() {
        return list;
    }

    public void setList(List<ForecastListVO> list) {
        this.list = list;
    }
}
