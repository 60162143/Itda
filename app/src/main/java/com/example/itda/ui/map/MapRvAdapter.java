package com.example.itda.ui.map;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itda.R;

import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class MapRvAdapter extends RecyclerView.Adapter<MapRvAdapter.CustomStoreViewHolder> {

    private ArrayList<MapStoreData> Stores = new ArrayList<>();

    private MapView mapView;

    private Context mContext;

    // ==========[Click 이벤트 구현을 위해 추가된 코드] =================
    // OnItemClickListener 인터페이스 선언
    public interface OnItemClickListener {
        void onItemClicked(int position, String data);
    }

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        itemClickListener = listener;
    }
    // ================================================================

    public MapRvAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public MapRvAdapter.CustomStoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_store, parent, false);

/*        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapView.selectPOIItem(mapView.findPOIItemByTag());
            }
        });*/
        return new CustomStoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MapRvAdapter.CustomStoreViewHolder holder, int position) {
        MapStoreData store = Stores.get(position);
        Glide.with(holder.itemView).load(Uri.parse(store.getMapStoreIamagePath())).error(R.drawable.kindcoffee).fallback(R.drawable.hongik).into(holder.mapStoreImage);
        holder.mapStoreName.setText(store.getMapStoreName());
        if(store.getMapStoreDistance() <= 0.01){
            holder.mapStoreDistance.setText("10m 이내");
        }else{
            holder.mapStoreDistance.setText(String.format("%.2f", store.getMapStoreDistance()) + "m");
        }
        holder.mapStoreStarScore.setText(String.format("%.1f", store.getMapStoreScore()));
        holder.mapStoreInfo.setText(store.getMapStoreInfo());
        //holder.mapStoreHashTag.setText(store.getMapStoreHashTag());
    }

    @Override
    public int getItemCount() {
        return Stores.size();
    }

    public void setStores(ArrayList<MapStoreData> stores){
        this.Stores = stores;
    }

    public MapView getMapView() {
        return mapView;
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public static class CustomStoreViewHolder extends RecyclerView.ViewHolder {
        ImageView mapStoreImage;
        TextView mapStoreName;
        TextView mapStoreDistance;
        TextView mapStoreStarScore;
        TextView mapStoreInfo;
        TextView mapStoreHashTag;

        public CustomStoreViewHolder(@NonNull View itemView) {
            super(itemView);
            mapStoreImage = itemView.findViewById(R.id.map_store_image);
            mapStoreName = itemView.findViewById(R.id.map_store_name);
            mapStoreDistance = itemView.findViewById(R.id.map_store_distance);
            mapStoreStarScore = itemView.findViewById(R.id.map_star_score);
            mapStoreInfo = itemView.findViewById(R.id.map_store_info);
            mapStoreHashTag = itemView.findViewById(R.id.map_store_hashTag);
        }
    }
}