package com.example.mingw.restaurant.activities;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.mingw.restaurant.FoodCart;
import com.example.mingw.restaurant.R;
import com.example.mingw.restaurant.utils.DatabaseUtil;

public class DetailActivity extends AppCompatActivity {

    public static final String FOOD_IMAGE_URL = "food_image_id";
    public static final String FOOD_NAME = "food_name";
    public static final String FOOD_PRICE = "food_price";
    public static  final String FOOD_CONTENT = "food_content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_detail_toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String baseUrl = "http://192.168.199.194:8080/food/img/";
        final String foodName = intent.getStringExtra(FOOD_NAME);
        String foodContent = intent.getStringExtra(FOOD_CONTENT);
        final String foodPrice = intent.getStringExtra(FOOD_PRICE);
        final String foodImageUrl = baseUrl + intent.getStringExtra(FOOD_IMAGE_URL);
        FloatingActionButton addToCart = (FloatingActionButton) findViewById(R.id.fab_detail_add_to_cart);
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
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                FoodCart fc = new FoodCart();
                fc.setName(foodName);
                fc.setNumber(1);
                fc.setImgUrl(foodImageUrl);
                fc.setPrice(Double.parseDouble(foodPrice));
                DatabaseUtil.addToDatabase(fc);
                Toast.makeText(DetailActivity.this, "added to cart", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
