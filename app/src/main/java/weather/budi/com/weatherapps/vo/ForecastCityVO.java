package weather.budi.com.weatherapps.vo;

/**
 * Created by Budi on 1/14/2017.
 */

public class ForecastCityVO {
    private int id;
    private String name;
    private CoordinateVO coord;
    private String country;

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

    public CoordinateVO getCoord() {
        return coord;
    }

    public void setCoord(CoordinateVO coord) {
        this.coord = coord;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
