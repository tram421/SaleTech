package com.tram.saletech.API;

import java.util.ArrayList;
import java.util.List;

public class AllProduct {
    private static AllProduct instance = null;
    public List<Product> listAllProduct = new ArrayList<>();
    private AllProduct(){

    }
    public static AllProduct getInstance () {
        if (instance == null){
            instance = new AllProduct();
        }
        return instance;
    }

}
