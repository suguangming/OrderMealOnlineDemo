package com.example.mingw.restaurant.Activities;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.example.mingw.restaurant.Food;
import com.example.mingw.restaurant.FoodAdapter;
import com.example.mingw.restaurant.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

import static com.example.mingw.restaurant.utils.HttpUtil.getStringByOkhttp;

public class MainActivity extends AppCompatActivity {

    Gson gson = new Gson();
    private SwipeRefreshLayout swipeRefresh;
    private FoodAdapter adapter;
    private List<Food> foodList;
    private String url = "http://192.168.199.194:8080/food/index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_main_toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView;
        LinearLayoutManager manager;
        recyclerView = findViewById(R.id.recyclerView_mainList);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        foodList = new ArrayList<>();
        adapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(adapter);
        getData();

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                refreshFoods();
            }
        });
    }

    private void getData() {
        new AsyncTask<Void, Integer, String>() {
            @Override protected String doInBackground(Void... params) {
                String s = getStringByOkhttp(url);
                return s;
            }
            @Override protected void onPostExecute(String s) {
                if (s != null && !s.isEmpty()) {
                    List<Food> foodData = parseData(s);
                    foodList.addAll(foodData);
                    adapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }

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
                        Toast.makeText(MainActivity.this, "refresh", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private List<Food> parseData(String responseData) {
        return gson.fromJson(responseData, new TypeToken<List<Food>>(){}.getType());
    }
}
