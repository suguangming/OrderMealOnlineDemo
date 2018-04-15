package com.example.mingw.restaurant.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.mingw.restaurant.FoodCart;
import com.example.mingw.restaurant.FoodCartAdapter;
import com.example.mingw.restaurant.R;
import com.example.mingw.restaurant.utils.FoodCartDatabaseUtil;
import java.util.ArrayList;
import java.util.List;

public class ShopCartActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private List<FoodCart> foodCartList;
    private ImageButton foodCartNumberMinus;
    private ImageButton foodCartNumberPlus;
    private TextView foodCartNumber;
    private int foodNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_cart_toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_cart_food_cart_list);
        LinearLayoutManager manager;
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        //FoodCartDatabaseUtil.addDefaultData();
        foodCartList = new ArrayList<>();
        adapter = new FoodCartAdapter(foodCartList);
        recyclerView.setAdapter(adapter);
        getFoodCart();
    }

    private void getFoodCart() {
        List<FoodCart> mFoodCartList = FoodCartDatabaseUtil.getAllFoodCartData();
        if (mFoodCartList != null && !mFoodCartList.isEmpty()) {
            foodCartList.addAll(mFoodCartList);
            adapter.notifyDataSetChanged();
        }
    }

}
