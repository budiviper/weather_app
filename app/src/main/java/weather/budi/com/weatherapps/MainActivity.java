package weather.budi.com.weatherapps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import io.realm.Realm;
import io.realm.RealmResults;
import weather.budi.com.weatherapps.fragment.DetailFragment;
import weather.budi.com.weatherapps.fragment.ListCityFragment;
import weather.budi.com.weatherapps.utils.Constants;
import weather.budi.com.weatherapps.vo.CityVO;

public class MainActivity extends AppCompatActivity {

    int currentPage;
    private SearchView mSearchView;
    private MenuItem deleteMenuItem;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        changePage(Constants.PAGE_LIST,"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        deleteMenuItem = menu.findItem(R.id.action_delete);
        return true;
    }

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // newText is text entered by user to SearchView
            //Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_LONG).show();
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            deleteAllSavedLocation();
            return true;
        }

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllSavedLocation(){

        mRealm = Realm.getInstance(App.getInstance());

        RealmResults<CityVO> results = mRealm.where(CityVO.class).findAll();

        // All changes to data must happen in a transaction
        mRealm.beginTransaction();

        // Delete all matches
        results.clear();

        mRealm.commitTransaction();

        changePage(Constants.PAGE_LIST,"");
    }

    public void changePage(int pageId, String... param){
        try{
            if(pageId == Constants.PAGE_LIST) {
                ListCityFragment listCityFragment = new ListCityFragment();
                setFragment(listCityFragment);
            }else if (pageId == Constants.PAGE_DETAIL) {
                DetailFragment detailFragment = new DetailFragment();

                Bundle args = new Bundle();
                args.putString("city_name", param[0].toString());
                detailFragment.setArguments(args);
                setFragment(detailFragment);
            }

            currentPage = pageId;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setFragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.container, fragment);

        try {
            fragmentTransaction.commit();
        }catch (Exception e){
            e.printStackTrace();
            fragmentTransaction.commitNowAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {

        if(currentPage == Constants.PAGE_LIST) {
            super.onBackPressed();
        }else if (currentPage == Constants.PAGE_DETAIL) {
            changePage(Constants.PAGE_LIST);
        }
    }
}
