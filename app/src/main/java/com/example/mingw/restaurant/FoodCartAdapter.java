package com.example.mingw.restaurant;

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
import com.example.mingw.restaurant.utils.DatabaseUtil;
import java.util.List;
import org.w3c.dom.Text;

public class FoodCartAdapter extends RecyclerView.Adapter<FoodCartAdapter.ViewHolder> {

    private Context mContext;
    private List<FoodCart> mFoodCartList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cartFoodCardView;
        ImageView cartFoodImage;
        TextView cartFoodName;
        TextView cartFoodNumber;
        TextView cartFoodPrice;
        TextView cartStatus;
        ImageButton cartFoodNumberMinus;
        ImageButton cartFoodNumberPlus;

        public ViewHolder (View view) {
            super(view);
            cartFoodCardView = (CardView) view;
            cartFoodImage = (ImageView) view.findViewById(R.id.iv_cart_food_image);
            cartFoodName = (TextView) view.findViewById(R.id.tv_cart_food_name);
            cartStatus = (TextView) view.findViewById(R.id.tv_cart_food_status);
            cartFoodNumber = (TextView) view.findViewById(R.id.tv_cart_food_number);
            cartFoodPrice = (TextView) view.findViewById(R.id.tv_cart_food_price);
            cartFoodNumberMinus = (ImageButton) view.findViewById(R.id.ibt_cart_food_number_minus);
            cartFoodNumberPlus = (ImageButton) view.findViewById(R.id.ibt_cart_food_number_plus);
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
        holder.cartFoodNumberMinus.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(mContext, "minus", Toast.LENGTH_SHORT).show();
                String foodCartName2Minus = holder.cartFoodName.getText().toString();
                DatabaseUtil.updateData("minus",foodCartName2Minus);
                int i = Integer.parseInt(holder.cartFoodNumber.getText().toString());
                holder.cartFoodNumber.setText(i-1+"");
            }
        });
        holder.cartFoodNumberPlus.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(mContext, "plus", Toast.LENGTH_SHORT).show();
                String foodCartName2Plus = holder.cartFoodName.getText().toString();
                DatabaseUtil.updateData("plus",foodCartName2Plus);
                int i = Integer.parseInt(holder.cartFoodNumber.getText().toString());
                holder.cartFoodNumber.setText(i+1+"");
            }
        });
        holder.cartFoodCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setMessage("确定删除");
                dialog.setCancelable(true);
                dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        DatabaseUtil.deleteOrder(holder.cartFoodName.getText().toString());
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
            holder.cartFoodCardView.setVisibility(View.GONE);
        }else {
            holder.cartFoodCardView.setVisibility(View.VISIBLE);
            String imgUrl = foodCart.getImgUrl();
            Glide.with(mContext).load(imgUrl).into(holder.cartFoodImage);
            holder.cartFoodName.setText(foodCart.getFoodname());
            holder.cartStatus.setText(foodCart.getStatus());
            holder.cartFoodNumber.setText(foodCart.getFoodnumber()+"");
            holder.cartFoodPrice.setText("￥" + foodCart.getFoodprice() * foodCart.getFoodnumber());
        }
    }


    @Override public int getItemCount() {
        return mFoodCartList.size();
    }
}
