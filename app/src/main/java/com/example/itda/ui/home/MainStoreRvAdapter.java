package com.example.itda.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itda.R;
import com.example.itda.ui.info.InfoActivity;

import java.util.ArrayList;

public class MainStoreRvAdapter extends RecyclerView.Adapter<MainStoreRvAdapter.CustomMainCategoryViewHolder>{

    private ArrayList<mainStoreData> Stores = new ArrayList<>();
    private Context mContext;
    private Intent intent;

    public MainStoreRvAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public MainStoreRvAdapter.CustomMainCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_store, parent, false);
        return new CustomMainCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainStoreRvAdapter.CustomMainCategoryViewHolder holder, int position) {
        mainStoreData store = Stores.get(position);
        Glide.with(holder.itemView).load(Uri.parse(store.getStoreThumbnailPath())).error(R.drawable.kindcoffee).fallback(R.drawable.hongik).into(holder.main_store_image);
        holder.main_store_text.setText(store.getStoreName());
        holder.main_store_image.setOnClickListener(new View.OnClickListener() {

            //메인 화면 이미지 클릭 시 버튼 리스너
            @Override
            public void onClick(View v) {
                intent = new Intent(mContext, InfoActivity.class);
                intent.putExtra("Store", (Parcelable) store);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Stores.size();
    }

    public void setStores(ArrayList<mainStoreData> stores){
        this.Stores = stores;
    }

    public static class CustomMainCategoryViewHolder extends RecyclerView.ViewHolder {
        ImageButton main_store_image;
        TextView main_store_text;

        public CustomMainCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            main_store_image = itemView.findViewById(R.id.store_image);
            main_store_text = itemView.findViewById(R.id.store_name);
        }
    }

}
