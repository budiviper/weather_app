package weather.budi.com.weatherapps.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import weather.budi.com.weatherapps.R;
import weather.budi.com.weatherapps.adapter.CardAdapter;
import weather.budi.com.weatherapps.network.VolleyResultListener;
import weather.budi.com.weatherapps.utils.Constants;
import weather.budi.com.weatherapps.utils.Popup;
import weather.budi.com.weatherapps.vo.CardVO;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by Budi on 1/13/2017.
 */

public class ListCityFragment extends Fragment implements VolleyResultListener{

    Activity a;
    ProgressDialog progress;

    @Bind(R.id.cardList)RecyclerView rv;
    @Bind(R.id.fabAdd)FloatingActionButton fabAdd;

    private GoogleApiClient mGoogleApiClient;
    private List<CardVO> lvo;
    private CardAdapter cardAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_list_city_layout,
                container, false);
        a=getActivity();
        ButterKnife.bind(this,contentView);

        mGoogleApiClient = new GoogleApiClient
                .Builder(a)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this, this)
                .build();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPlace();
            }
        });

//        progress = Popup.showProgress(getResources().getString(R.string.text_loading), a);
//        progress.setCancelable(true);

        lvo = new ArrayList<CardVO>();
        cardAdapter = new CardAdapter(a,lvo);

        rv.setHasFixedSize(true);
        rv.setAdapter(cardAdapter);
        rvLayoutManager = new LinearLayoutManager(a);
        rv.setLayoutManager(rvLayoutManager);

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

                // SAVE DATA HERE & SHOW CARD LIST

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
