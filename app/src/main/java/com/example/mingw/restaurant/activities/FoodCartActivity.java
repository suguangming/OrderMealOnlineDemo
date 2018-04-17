package com.example.mingw.restaurant.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.mingw.restaurant.FoodCart;
import com.example.mingw.restaurant.FoodCartAdapter;
import com.example.mingw.restaurant.R;
import com.example.mingw.restaurant.utils.DatabaseUtil;
import com.example.mingw.restaurant.utils.HttpUtil;
import java.util.ArrayList;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static com.example.mingw.restaurant.utils.DatabaseUtil.clearEmptyData;

public class FoodCartActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private List<FoodCart> foodCartList;
    private SharedPreferences pref;
    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_cart_toolbar);
        setSupportActionBar(toolbar);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        // clearEmptyData();
        FloatingActionButton submitCart = (FloatingActionButton) findViewById(R.id.fab_cart_submit_order);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_cart_food_cart_list);
        LinearLayoutManager manager;
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        // DatabaseUtil.addDefaultData();
        foodCartList = new ArrayList<>();
        adapter = new FoodCartAdapter(foodCartList);
        recyclerView.setAdapter(adapter);
        getFoodCart();
        submitCart.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                new Thread(new SubmitOrderThread()).start();
            }
        });
    }

    private void getFoodCart() {
        List<FoodCart> mFoodCartList = DatabaseUtil.getAllFoodCartData();
        if (mFoodCartList != null && !mFoodCartList.isEmpty()) {
            foodCartList.addAll(mFoodCartList);
            adapter.notifyDataSetChanged();
        }
    }

    private class SubmitOrderThread implements Runnable {
        String responseData;
        String url = "http://192.168.199.194:8080/food/order";
        String username = pref.getString("current_username", "");
        @Override public void run() {
            FormBody formBody;
            for (FoodCart f : foodCartList) {
                formBody = new FormBody.Builder()
                    .add("type", "new")
                    .add("username", username)
                    .add("foodName", f.getName())
                    .add("foodNumber", f.getNumber()+"")
                    .add("foodPrice", f.getPrice()+"")
                    .build();
                responseData = HttpUtil.postFormByOkHttp(url, formBody);
                Log.d("\ncart", "respData" + responseData + "\n");
            }
            handler.post(new Runnable() {
                @Override public void run() {
                    if (responseData!=null&&responseData.equals("submit success")) {
                        Toast.makeText(FoodCartActivity.this, "new order", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
