package com.example.mingw.restaurant.Activities;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.example.mingw.restaurant.R;

public class DetailActivity extends AppCompatActivity {

    public static final String FOOD_NAME = "food_name";
    public static  final String FOOD_CONTENT = "food_content";
    public static final String FOOD_IMAGE_URL = "food_image_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_detail_toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String baseUrl = "http://192.168.199.194:8080/food/img/";
        String foodName = intent.getStringExtra(FOOD_NAME);
        String foodContent = intent.getStringExtra(FOOD_CONTENT);
        String foodImageUrl = baseUrl + intent.getStringExtra(FOOD_IMAGE_URL);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctb_detail_collapsing_toolbar);
        ImageView foodImageView = (ImageView) findViewById(R.id.iv_detail_food_image);
        TextView foodContentText = (TextView) findViewById(R.id.tv_detail_food_content_text);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(foodName);
        Glide.with(this).load(foodImageUrl).into(foodImageView);
        foodContentText.setText(foodContent);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
