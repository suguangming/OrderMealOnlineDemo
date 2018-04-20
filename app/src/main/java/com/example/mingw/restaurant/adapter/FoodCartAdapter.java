package com.example.mingw.restaurant.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.mingw.restaurant.FoodCart;
import com.example.mingw.restaurant.R;
import com.example.mingw.restaurant.utils.DatabaseUtil;
import java.util.List;


public class FoodCartAdapter extends RecyclerView.Adapter<FoodCartAdapter.ViewHolder> {

    private Context mContext;
    private List<FoodCart> mFoodCartList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewFoodCartItem;
        ImageView imageViewFoodCartItemImage;
        TextView textViewFoodCartItemName;
        TextView textViewFoodCartItemNumber;
        TextView textViewFoodCartItemPrice;
        TextView textViewFoodCartItemStatus;
        ImageButton imageButtonFoodCartItemNumberMinus;
        ImageButton imageButtonFoodCartItemNumberPlus;

        public ViewHolder (View view) {
            super(view);
            cardViewFoodCartItem = (CardView) view;
            imageViewFoodCartItemImage = view.findViewById(R.id.iv_cart_food_image);
            textViewFoodCartItemName = view.findViewById(R.id.tv_cart_food_name);
            textViewFoodCartItemStatus = view.findViewById(R.id.tv_cart_food_status);
            textViewFoodCartItemNumber = view.findViewById(R.id.tv_cart_food_number);
            textViewFoodCartItemPrice = view.findViewById(R.id.tv_cart_food_price);
            imageButtonFoodCartItemNumberMinus = view.findViewById(R.id.ibt_cart_food_number_minus);
            imageButtonFoodCartItemNumberPlus = view.findViewById(R.id.ibt_cart_food_number_plus);
        }
    }

    public FoodCartAdapter(List<FoodCart> foodCartList) {
        mFoodCartList = foodCartList;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.food_cart_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.imageButtonFoodCartItemNumberMinus.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(mContext, "minus", Toast.LENGTH_SHORT).show();
                String foodCartName2Minus = holder.textViewFoodCartItemName.getText().toString();
                DatabaseUtil.updateData("minus",foodCartName2Minus);
                int i = Integer.parseInt(holder.textViewFoodCartItemNumber.getText().toString());
                holder.textViewFoodCartItemNumber.setText(i-1+"");
            }
        });
        holder.imageButtonFoodCartItemNumberPlus.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(mContext, "plus", Toast.LENGTH_SHORT).show();
                String foodCartName2Plus = holder.textViewFoodCartItemName.getText().toString();
                DatabaseUtil.updateData("plus",foodCartName2Plus);
                int i = Integer.parseInt(holder.textViewFoodCartItemNumber.getText().toString());
                holder.textViewFoodCartItemNumber.setText(i+1+"");
            }
        });
        holder.cardViewFoodCartItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setMessage("确定删除?");
                dialog.setCancelable(true);
                dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        DatabaseUtil.deleteOrder(holder.textViewFoodCartItemName.getText().toString());
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FoodCart foodCart = mFoodCartList.get(position);
        if (foodCart.getFoodnumber() == 0) {
            holder.cardViewFoodCartItem.setVisibility(View.GONE);
        }else {
            holder.cardViewFoodCartItem.setVisibility(View.VISIBLE);
            String imgUrl = foodCart.getImgUrl();
            Glide.with(mContext).load(imgUrl).into(holder.imageViewFoodCartItemImage);
            holder.textViewFoodCartItemName.setText(foodCart.getFoodname());
            holder.textViewFoodCartItemStatus.setText(foodCart.getStatus());
            holder.textViewFoodCartItemNumber.setText(foodCart.getFoodnumber()+"");
            holder.textViewFoodCartItemPrice.setText("￥" + foodCart.getFoodprice() * foodCart.getFoodnumber());
        }
    }


    @Override public int getItemCount() {
        return mFoodCartList.size();
    }
}
