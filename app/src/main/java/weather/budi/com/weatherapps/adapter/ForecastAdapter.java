package weather.budi.com.weatherapps.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import weather.budi.com.weatherapps.R;
import weather.budi.com.weatherapps.utils.StringUtils;
import weather.budi.com.weatherapps.utils.UrlComposer;
import weather.budi.com.weatherapps.vo.ForecastListVO;

/**
 * Created by Budi on 1/15/2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<ForecastListVO> listVO;
    Activity activity;
    final int TYPE_CONTENT=1;

    public ForecastAdapter(Activity a, List<ForecastListVO> lvo){
        activity=a;
        listVO=lvo;
    }

    public void setData(List<ForecastListVO> lvo) {
        listVO=lvo;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ForecastHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_forecast, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        try {
            if(vh instanceof ForecastHolder) {
                ForecastHolder holder = (ForecastHolder) vh;

                DecimalFormat format = new DecimalFormat("#");

                if(position==0){
                    holder.forecastRow.setBackgroundColor(activity.getResources().getColor(R.color.yellow_solid));
                }else{
                    holder.forecastRow.setBackgroundColor(activity.getResources().getColor(R.color.no_color));
                }

                holder.tvDate.setText(""+ StringUtils.convertEpoch(getItem(position).getDt(),"EEEE"));
                holder.tvTempMax.setText(""+ format.format(getItem(position).getTemp().getMax()) + (char) 0x00B0);
                holder.tvTempMin.setText(""+ format.format(getItem(position).getTemp().getMin()) + (char) 0x00B0);

                Glide.with(activity)
                        .load(UrlComposer.composeWeatherIcon(getItem(position).getWeather().get(0).getIcon()))
                        .into(holder.ivIcon);
            }
        }catch (Exception e){

        }
    }

    class ForecastHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        LinearLayout forecastRow;
        TextView tvDate;
        TextView tvTempMax;
        TextView tvTempMin;
        ImageView ivIcon;

        public ForecastHolder(View v) {
            super(v);
            // DEFINE FIND VIEW BY ID HERE

            forecastRow = (LinearLayout) v.findViewById(R.id.forecastRow);
            tvDate = (TextView)v.findViewById(R.id.tvDate);
            tvTempMax = (TextView)v.findViewById(R.id.tvTempMax);
            tvTempMin = (TextView)v.findViewById(R.id.tvTempMin);
            ivIcon = (ImageView) v.findViewById(R.id.ivIcon);


            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){

        }
    }

    public ForecastListVO getItem(int position) {
        return listVO.get(position);
    }

    @Override
    public long getItemId(int position) {
//        return listVO.get(position).getId();
        return 0;
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
