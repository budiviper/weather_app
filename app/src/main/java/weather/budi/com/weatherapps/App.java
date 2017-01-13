package weather.budi.com.weatherapps;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Budi on 1/13/2017.
 */

public class App extends Application {

    private GoogleApiClient mGoogleApiClient;
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance=this;

        mGoogleApiClient = new GoogleApiClient
                .Builder(getApplicationContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }

    public static App getInstance() {
        return instance;
    }
}
