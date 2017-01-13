package weather.budi.com.weatherapps.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import io.realm.Realm;
import weather.budi.com.weatherapps.R;
import weather.budi.com.weatherapps.vo.CityVO;

/**
 * Created by Budi on 1/13/2017.
 */

public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<CityVO> listVO;
    Activity activity;

    final int TYPE_CONTENT=1;
    private Realm realm;

    public CityAdapter(Activity a, List<CityVO> lvo){
        activity=a;
        listVO=lvo;
    }

    public void setData(List<CityVO> lvo) {
        listVO=lvo;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        try {
          if(vh instanceof CityHolder) {
              CityHolder holder = (CityHolder) vh;

              DecimalFormat format = new DecimalFormat("#");

              holder.tvTemp.setText(""+ format.format( getItem(position).getTemp()) + (char) 0x00B0);
              holder.tvCity.setText(getItem(position).getCityName());
          }
        }catch (Exception e){

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_city, parent, false));
    }

    class CityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvCity;
        TextView tvTime;
        TextView tvTemp;

        public CityHolder(View v) {
            super(v);
            // DEFINE FIND VIEW BY ID HERE

            tvCity = (TextView)v.findViewById(R.id.tvCityName);
            tvTime = (TextView)v.findViewById(R.id.tvTime);
            tvTemp = (TextView)v.findViewById(R.id.tvTemp);


            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
//            int mid= listVO.get(getAdapterPosition()).getMatch_id();
//            ((MainActivity) activity).changePage(App.PAGE_FOOTBALL_RESULT_LIVE, "" +mid);
        }
    }

    public CityVO getItem(int position) {
        return listVO.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listVO.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return listVO.size();
    }
}
