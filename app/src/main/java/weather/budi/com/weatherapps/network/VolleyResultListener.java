package weather.budi.com.weatherapps.network;

/**
 * Created by IT11 on 4/2/2015.
 */
public interface VolleyResultListener {
    public void onSuccess(int id, String result);

    public void onFinish(int id);

    public void onStart(int id);

    public boolean onError(int id, String msg);
}
