package weather.budi.com.weatherapps.vo;

import java.util.List;

/**
 * Created by Budi on 1/11/2017.
 */

public class WeatherModel {

    private CoordinateVO coord;
    private List<WeatherVO> weather;
    private TemperatureVO main;
    private String name;
    private int id;

    public CoordinateVO getCoord() {
        return coord;
    }

    public void setCoord(CoordinateVO coord) {
        this.coord = coord;
    }

    public List<WeatherVO> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherVO> weather) {
        this.weather = weather;
    }

    public TemperatureVO getMain() {
        return main;
    }

    public void setMain(TemperatureVO main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }






}