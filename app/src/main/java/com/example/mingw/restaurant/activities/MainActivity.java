package com.example.mingw.restaurant.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import com.example.mingw.restaurant.Food;
import com.example.mingw.restaurant.R;
import com.example.mingw.restaurant.adapter.FoodAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

import static com.example.mingw.restaurant.utils.HttpUtil.getStringByOkHttp;

/**
 * MainActivity class
 * @author guangming
 * @date 2018/04/20
 */
public class MainActivity extends AppCompatActivity {

    Gson gson = new Gson();
    private String server;
    private FoodAdapter adapter;
    private List<Food> foodList;
    private SwipeRefreshLayout swipeRefresh;
    private long firstTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.tb_main_toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String serverIP = pref.getString("server_url", "192.168.199.194");
        server = "http://" + serverIP + ":8080/food/index";
        FloatingActionButton fabToCart = findViewById(R.id.fab_main_go_to_foodCart);
        RecyclerView recyclerView = findViewById(R.id.rv_main_food_list);
        LinearLayoutManager manager;
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        foodList = new ArrayList<>();
        adapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(adapter);

        getData();
        fabToCart.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FoodCartActivity.class));
            }
        });
        swipeRefresh = findViewById(R.id.sr_main_swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                refreshFoods();
            }
        });
    }


    /**
     * 在线获取商品信息
     */
    private void getData() {
        new AsyncTask<Void, Integer, String>() {
            @Override protected String doInBackground(Void... params) {
                String s = getStringByOkHttp(server);
                return s;
            }


            @Override protected void onPostExecute(String s) {
                if (s != null && !s.isEmpty()) {
                    List<Food> foodData = parseData(s);
                    foodList.clear();
                    foodList.addAll(foodData);
                    adapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }


    /**
     * 刷新数据
     */
    private void refreshFoods() {
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        getData();
                        Toast.makeText(MainActivity.this, "数据已刷新", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }


    /**
     * 使用GSON解析获取到的数据
     */
    private List<Food> parseData(String responseData) {
        return gson.fromJson(responseData, new TypeToken<List<Food>>() {}.getType());
    }


    /**
     * 双击退出
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int minLastTime = 2000;
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > minLastTime) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
