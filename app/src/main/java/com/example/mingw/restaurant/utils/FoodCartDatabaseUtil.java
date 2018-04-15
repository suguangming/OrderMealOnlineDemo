package com.example.mingw.restaurant.utils;

import android.content.Context;
import android.widget.Toast;
import com.example.mingw.restaurant.FoodCart;
import java.util.List;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

public class FoodCartDatabaseUtil {

    public static void addDefaultData(){
        DataSupport.deleteAll(FoodCart.class);
        FoodCart foodCart1 = new FoodCart();
        foodCart1.setName("aaa");
        foodCart1.setNumber(1);
        foodCart1.setPrice(10);
        foodCart1.save();

        FoodCart foodCart2 = new FoodCart();
        foodCart2.setName("bbb");
        foodCart2.setNumber(2);
        foodCart2.setPrice(20);
        foodCart2.save();

        FoodCart foodCart3 = new FoodCart();
        foodCart3.setName("ccc");
        foodCart3.setNumber(3);
        foodCart3.setPrice(30);
        foodCart3.save();
    }
    public static List<FoodCart> getAllFoodCartData(){
        return DataSupport.findAll(FoodCart.class);
    }

    public static void createDataBase() {
        Connector.getDatabase();
    }

    public static void updateData(String operation, String foodCartName) {
        switch (operation){
            case "plus":
                FoodCart foodCartTemp1 = new FoodCart();
                List<FoodCart> foodCartListTemp1 = DataSupport.where("name = ?", foodCartName).find(FoodCart.class);
                foodCartTemp1 = foodCartListTemp1.get(0);
                foodCartTemp1.setNumber(foodCartTemp1.getNumber()+1);
                foodCartTemp1.updateAll("name = ?", foodCartTemp1.getName());
                break;
            case "minus":
                FoodCart foodCartTemp2;
                List<FoodCart> foodCartListTemp2 = DataSupport.where("name = ?", foodCartName).find(FoodCart.class);
                foodCartTemp2 = foodCartListTemp2.get(0);
                foodCartTemp2.setNumber(foodCartTemp2.getNumber()-1);
                foodCartTemp2.updateAll("name = ?", foodCartTemp2.getName());
                break;
            default:
                break;
        }
    }

    public void newData(Context context, FoodCart foodCart) {
        FoodCart foodCartTemp = foodCart;
        List<FoodCart> foodCartListTemp = DataSupport.select("name", foodCartTemp.getName()).find(FoodCart.class);
        if (foodCartListTemp.isEmpty()){
            foodCart.save();
        } else {
            Toast.makeText(context, "already exist", Toast.LENGTH_SHORT).show();
        }
    }

    public static void clearEmptyData() {
        List<FoodCart> foodCarts = getAllFoodCartData();
        int index = 0;
        for (FoodCart foodCart : foodCarts) {
            if (foodCart.getNumber()==0) {
                foodCarts.remove(index);
            }
            index++;
        }
        DataSupport.deleteAll(FoodCart.class);
        for (FoodCart foodCart : foodCarts) {
            foodCart.save();
        }
    }

}
