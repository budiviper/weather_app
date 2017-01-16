package weather.budi.com.weatherapps.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import io.realm.Realm;
import weather.budi.com.weatherapps.MainActivity;
import weather.budi.com.weatherapps.R;
import weather.budi.com.weatherapps.utils.Constants;
import weather.budi.com.weatherapps.utils.UrlComposer;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_city, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        try {
          if(vh instanceof CityHolder) {
              CityHolder holder = (CityHolder) vh;

              DecimalFormat format = new DecimalFormat("#");

              holder.tvTime.setText(""+getItem(position).getTime());
              holder.tvTemp.setText(""+ format.format( getItem(position).getTemp()) + (char) 0x00B0);
              holder.tvCity.setText(getItem(position).getCityName());


              if (getItem(position).getWeather_id()==800)
                  holder.card_view.setBackgroundResource(R.drawable.bg_clear);
              else if (getItem(position).getWeather_id()==801)
                  holder.card_view.setBackgroundResource(R.drawable.bg_few_cloud);
              else if (getItem(position).getWeather_id()==802)
                  holder.card_view.setBackgroundResource(R.drawable.bg_scattered_cloud);
              else if (getItem(position).getWeather_id()==803)
                  holder.card_view.setBackgroundResource(R.drawable.bg_broken_cloud);
              else if (getItem(position).getWeather_id()==804)
                  holder.card_view.setBackgroundResource(R.drawable.bg_overcast_cloud);
              else if (getItem(position).getWeather_id()>=600 && getItem(position).getWeather_id()<=622)
                  holder.card_view.setBackgroundResource(R.drawable.bg_snow);
              else if(getItem(position).getWeather_id() >=500 && getItem(position).getWeather_id()<=531 )
                  holder.card_view.setBackgroundResource(R.drawable.bg_rain);
              else if(getItem(position).getWeather_id()>=300 && getItem(position).getWeather_id() <=321)
                  holder.card_view.setBackgroundResource(R.drawable.bg_drizzle);
              else if (getItem(position).getWeather_id()>=200 && getItem(position).getWeather_id()<=232)
                  holder.card_view.setBackgroundResource(R.drawable.bg_thunderstorm);
              else if(getItem(position).getWeather_id()==701)
                  holder.card_view.setBackgroundResource(R.drawable.bg_mist);
              else if(getItem(position).getWeather_id()==711)
                  holder.card_view.setBackgroundResource(R.drawable.bg_smoke);
              else if(getItem(position).getWeather_id()==721)
                  holder.card_view.setBackgroundResource(R.drawable.bg_haze);
              else if(getItem(position).getWeather_id()==731 || getItem(position).getWeather_id()==761)
                  holder.card_view.setBackgroundResource(R.drawable.bg_dust_whirl);
              else if(getItem(position).getWeather_id()==762)
                  holder.card_view.setBackgroundResource(R.drawable.bg_volcanic_ash);
              else if(getItem(position).getWeather_id()==771)
                  holder.card_view.setBackgroundResource(R.drawable.bg_squall);
              else if(getItem(position).getWeather_id()==781)
                  holder.card_view.setBackgroundResource(R.drawable.bg_tornado);
          }
        }catch (Exception e){

        }
    }

    class CityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvCity;
        TextView tvTime;
        TextView tvTemp;
        CardView card_view;

        public CityHolder(View v) {
            super(v);
            // DEFINE FIND VIEW BY ID HERE

            tvCity = (TextView)v.findViewById(R.id.tvCityName);
            tvTime = (TextView)v.findViewById(R.id.tvTime);
            tvTemp = (TextView)v.findViewById(R.id.tvTemp);
            card_view = (CardView) v.findViewById(R.id.card_view);


            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            String cityName= listVO.get(getAdapterPosition()).getCityName();
            ((MainActivity) activity).changePage(Constants.PAGE_DETAIL, "" +cityName);
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
