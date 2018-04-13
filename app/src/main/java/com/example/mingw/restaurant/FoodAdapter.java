package com.example.mingw.restaurant;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private Context mContext;
    private List<Food> mFoodList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View foodView;
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;

        public ViewHolder (View view) {
            super(view);
            foodView = view;
            foodImage = (ImageView) view.findViewById(R.id.food_image);
            foodName = (TextView) view.findViewById(R.id.food_name);
            foodPrice = (TextView) view.findViewById(R.id.food_price);
        }
    }

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
        holder.foodView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Food food = mFoodList.get(position);
                Toast.makeText(v.getContext(), "u clicked view " + food.getName() + food.getId(),
                    Toast.LENGTH_SHORT).show();
            }
        });
        holder.foodImage.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Food food = mFoodList.get(position);
                Toast.makeText(v.getContext(), "u clicked view " + food.getName() + food.getId(),
                    Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Food food = mFoodList.get(position);
        String imgUrl = "http://192.168.199.194:8080/food/img/" + food.getImgUrl();
        Glide.with(mContext).load(imgUrl).into(holder.foodImage);
        // holder.foodImage.setImageResource(R.drawable.food_chips);
        holder.foodName.setText(food.getName());
        holder.foodPrice.setText("￥" + food.getPrice());
    }


    @Override public int getItemCount() {
        return mFoodList.size();
    }
}
