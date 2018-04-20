package com.example.mingw.restaurant.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import com.example.mingw.restaurant.FoodCart;
import com.example.mingw.restaurant.R;
import com.example.mingw.restaurant.adapter.FoodCartAdapter;
import com.example.mingw.restaurant.utils.DatabaseUtil;
import com.example.mingw.restaurant.utils.HttpUtil;
import java.util.ArrayList;
import java.util.List;
import okhttp3.FormBody;

import static com.example.mingw.restaurant.utils.DatabaseUtil.clearEmptyData;
import static com.example.mingw.restaurant.utils.DatabaseUtil.setStatus;

/**
 * FoodCartActivity class
 * @author guangming
 * @date 2018/04/20
 */
public class FoodCartActivity extends AppCompatActivity {

    private static Handler handler = new Handler();
    private String server;
    private SharedPreferences pref;
    private RecyclerView.Adapter adapter;
    private List<FoodCart> foodCartList;
    private SwipeRefreshLayout swipeRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_cart);
        Toolbar toolbar = findViewById(R.id.tb_cart_toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = findViewById(R.id.rv_cart_food_cart_list);
        LinearLayoutManager manager;
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        FloatingActionButton fabToSubmitOrder = findViewById(
            R.id.fab_cart_submit_order);
        // 获取PreferenceManager
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String serverIP = pref.getString("server_url", "192.168.199.194");
        server = "http://" + serverIP + ":8080/food/order";
        clearEmptyData();
        foodCartList = new ArrayList<>();
        adapter = new FoodCartAdapter(foodCartList);
        recyclerView.setAdapter(adapter);
        getFoodCart();
        fabToSubmitOrder.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                new Thread(new SubmitOrderThread()).start();
            }
        });
        //下拉刷新
        swipeRefresh = findViewById(R.id.sr_cart_swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                refreshFoodCart();
            }
        });
    }


    /**
     * 从数据库中获取购物车数据
     */
    private void getFoodCart() {
        List<FoodCart> mFoodCartList = DatabaseUtil.getAllFoodCartData();
        if (mFoodCartList != null && !mFoodCartList.isEmpty()) {
            foodCartList.clear();
            foodCartList.addAll(mFoodCartList);
            adapter.notifyDataSetChanged();
        }
    }


    /**
     * 刷新购物车数据
     */
    private void refreshFoodCart() {
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        getFoodCart();
                        Toast.makeText(FoodCartActivity.this, "数据已刷新", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }


    /**
     * 提交订单线程
     */
    private class SubmitOrderThread implements Runnable {
        String submitSuccessCode = "submit success";
        String responseData;
        String username = pref.getString("current_username", "");


        @Override public void run() {
            FormBody formBody;
            for (FoodCart f : foodCartList) {
                if (f.getStatus().equals("未提交")) {
                    formBody = new FormBody.Builder()
                        .add("type", "new")
                        .add("username", username)
                        .add("foodName", f.getFoodname())
                        .add("foodNumber", f.getFoodnumber() + "")
                        .add("foodPrice", f.getFoodprice() + "")
                        .build();
                    responseData = HttpUtil.postFormByOkHttp(server, formBody);
                    setStatus(f, "已提交");
                } else {
                    continue;
                }
            }
            handler.post(new Runnable() {
                @Override public void run() {
                    if (responseData != null && submitSuccessCode.equals(responseData)) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(FoodCartActivity.this);
                        dialog.setTitle("订单提交");
                        dialog.setMessage("订单提交成功");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
