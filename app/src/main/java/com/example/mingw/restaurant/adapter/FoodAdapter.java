package com.example.mingw.restaurant.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.mingw.restaurant.Food;
import com.example.mingw.restaurant.R;
import com.example.mingw.restaurant.activities.DetailActivity;
import java.util.List;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private Context mContext;
    private List<Food> mFoodList;


    public FoodAdapter(List<Food> foodList) {
        mFoodList = foodList;
    }


    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.food_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.foodCardView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Food food = mFoodList.get(position);
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(DetailActivity.FOOD_NAME, food.getName());
                intent.putExtra(DetailActivity.FOOD_PRICE, food.getPrice() + "");
                intent.putExtra(DetailActivity.FOOD_CONTENT, food.getDescription());
                intent.putExtra(DetailActivity.FOOD_IMAGE_URL, food.getImgUrl());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String serverIP = pref.getString("server_url", "192.168.199.194");
        String imageServer = "http://" + serverIP + ":8080/food/img/";
        Food food = mFoodList.get(position);
        String imageUrl = imageServer + food.getImgUrl();
        Glide.with(mContext).load(imageUrl).into(holder.foodImage);
        holder.foodName.setText(food.getName());
        holder.foodPrice.setText("￥" + food.getPrice());
    }


    @Override public int getItemCount() {
        return mFoodList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView foodCardView;
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;


        public ViewHolder(View view) {
            super(view);
            foodCardView = (CardView) view;
            foodImage = view.findViewById(R.id.iv_food_food_image);
            foodName = view.findViewById(R.id.tv_food_food_name);
            foodPrice = view.findViewById(R.id.tv_food_food_price);
        }
    }
}
