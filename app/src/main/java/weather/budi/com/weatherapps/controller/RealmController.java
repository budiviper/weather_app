package weather.budi.com.weatherapps.controller;

/**
 * Created by Budi on 1/13/2017.
 */
import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import io.realm.Realm;
import io.realm.RealmResults;
import weather.budi.com.weatherapps.vo.CityVO;


public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    //Refresh the realm istance
    public void refresh() {
        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        realm.clear(CityVO.class);
        realm.commitTransaction();
    }

    // ********* QUERY ALL *********
    public RealmResults<CityVO> getAllCity() {
        return realm.where(CityVO.class).findAll();
    }

    public CityVO getCityById(int id) {
        return realm.where(CityVO.class).equalTo("id", id).findFirst();
    }

    //check if CityVO.class is empty
    public boolean hasCity() {
        return !realm.allObjects(CityVO.class).isEmpty();
    }

    // ********* QUERY BY *********
    public RealmResults<CityVO> queryCity(String cityName) {
        return realm.where(CityVO.class)
                .contains("cityName", cityName)
                .findAll();

    }
}
