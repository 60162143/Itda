package com.example.itda.ui.home;

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

import java.util.ArrayList;

public class CategoryRvAdapter extends RecyclerView.Adapter<CategoryRvAdapter.CustomCategoryViewHolder> {

    private ArrayList<mainCategoryData> Categories = new ArrayList<>();

    @NonNull
    @Override
    public CategoryRvAdapter.CustomCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_category, parent, false);
        return new CustomCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRvAdapter.CustomCategoryViewHolder holder, int position) {
        mainCategoryData category = Categories.get(position);
        Glide.with(holder.itemView).load(Uri.parse(category.getImagePath())).error(R.drawable.kindcoffee).fallback(R.drawable.hongik).into(holder.Circle_Category);
        holder.Name_Category.setText(category.getCategoryNm());
    }

    @Override
    public int getItemCount() {
        return Categories.size();
    }

    public void setCategories(ArrayList<mainCategoryData> categories){
        this.Categories = categories;
    }

    public static class CustomCategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView Circle_Category;
        TextView Name_Category;

        public CustomCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            Circle_Category = itemView.findViewById(R.id.category_image);
            Name_Category = itemView.findViewById(R.id.category_name);
        }
    }
}