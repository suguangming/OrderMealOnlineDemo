package com.example.mingw.restaurant.utils;

import java.util.Calendar;

public class SerialNoUtil {

    /**
     * 设置订单编号
     * @param u 用户名
     * @return 生成的订单编号
     */
    public static String getSerialNo(String u) {
        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH)+1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取系统时间
        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);

        return year + month + day + hour + minute + u;
    }
}
