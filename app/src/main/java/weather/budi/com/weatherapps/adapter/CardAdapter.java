package weather.budi.com.weatherapps.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import weather.budi.com.weatherapps.R;
import weather.budi.com.weatherapps.vo.CardVO;

/**
 * Created by Budi on 1/13/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<CardVO> listVO;
    Activity activity;

    final int TYPE_CONTENT=1;

    public CardAdapter(Activity a, List<CardVO> lvo){
        activity=a;
        listVO=lvo;
    }

    public void setData(List<CardVO> lvo) {
        listVO=lvo;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        try {
          if(vh instanceof CardHolder) {
              CardHolder holder = (CardHolder) vh;
//              holder.tvTime.setText(getItem(position).getHome_team());
          }
        }catch (Exception e){

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_city, parent, false));
    }

    class CardHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvCity;
        TextView tvTime;
        TextView tvDegree;

        public CardHolder(View v) {
            super(v);
            // DEFINE FIND VIEW BY ID HERE

            tvCity = (TextView)v.findViewById(R.id.tvCity);
            tvTime = (TextView)v.findViewById(R.id.tvTime);
            tvDegree = (TextView)v.findViewById(R.id.tvDegree);


            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
//            int mid= listVO.get(getAdapterPosition()).getMatch_id();
//            ((MainActivity) activity).changePage(App.PAGE_FOOTBALL_RESULT_LIVE, "" +mid);
        }
    }

    public CardVO getItem(int position) {
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
