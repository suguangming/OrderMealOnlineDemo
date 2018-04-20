package com.example.mingw.restaurant.utils;

import com.example.mingw.restaurant.FoodCart;
import java.util.List;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

public class DatabaseUtil {

    /**
     * 添加到购物车数据库
     * @param foodCart
     */
    public static void addToDatabase(FoodCart foodCart) {
        List<FoodCart> foodCartListTemp = DataSupport.where("foodname = ?", foodCart.getFoodname()).find(FoodCart.class);
        if (foodCartListTemp.isEmpty()) {
            foodCart.save();
        } else {
            updateData("plus", foodCart.getFoodname());
        }
    }


    /**
     * 获取数据库中所有订单
     * @return 所有订单的 List
     */
    public static List<FoodCart> getAllFoodCartData(){
        return DataSupport.findAll(FoodCart.class);
    }

    public static void createDataBase() {
        Connector.getDatabase();
    }


    /**
     * 更新单个订单的数量
     * @param operation
     * @param foodCartName
     */
    public static void updateData(String operation, String foodCartName) {
        switch (operation){
            case "plus":
                FoodCart foodCartTemp1;
                List<FoodCart> foodCartListTemp1 = DataSupport.where("foodname = ?", foodCartName).find(FoodCart.class);
                foodCartTemp1 = foodCartListTemp1.get(0);
                foodCartTemp1.setFoodnumber(foodCartTemp1.getFoodnumber()+1);
                foodCartTemp1.setStatus("未提交");
                foodCartTemp1.updateAll("foodname = ?", foodCartTemp1.getFoodname());
                break;
            case "minus":
                FoodCart foodCartTemp2;
                List<FoodCart> foodCartListTemp2 = DataSupport.where("foodname = ?", foodCartName).find(FoodCart.class);
                foodCartTemp2 = foodCartListTemp2.get(0);
                foodCartTemp2.setStatus("未提交");
                foodCartTemp2.setFoodnumber(foodCartTemp2.getFoodnumber()-1);
                foodCartTemp2.updateAll("foodname = ?", foodCartTemp2.getFoodname());
                break;
            default:
                break;
        }
    }


    /**
     * 更改单个订单状态
     * @param foodCart
     * @param newStatus
     */
    public static void setStatus(FoodCart foodCart, String newStatus) {
        List<FoodCart> foodCartListTemp = DataSupport.where("foodname = ?", foodCart.getFoodname()).find(FoodCart.class);
        FoodCart foodCartTemp = foodCartListTemp.get(0);
        foodCartTemp.setStatus(newStatus);
        foodCartTemp.updateAll("foodname = ?", foodCartTemp.getFoodname());

    }


    /**
     * 清除数据库中无效数据
     */
    public static void clearEmptyData() {
        DataSupport.deleteAll(FoodCart.class, "foodnumber < ?", "1");
    }

    public static void deleteOrder(String foodCartName) {
        DataSupport.deleteAll(FoodCart.class, "foodname = ?", foodCartName);
    }

}
