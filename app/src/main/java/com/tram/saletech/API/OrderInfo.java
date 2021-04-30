package com.tram.saletech.API;

public class OrderInfo {
    private static OrderInfo instance;
    public int[] idOrder;
    public String[] descriptionOrder;
    public int[] statusOrder;
    private OrderInfo(){}
    public static OrderInfo getInstance()
    {
        if (instance == null) {
            instance = new OrderInfo();
        }
        return instance;
    }
}
