package com.tram.saletech.API;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GetCart {
    private static GetCart instance = null;
    public String listItemInCart;
    public List<String[]> listAllCart;
    private GetCart()
    {

    }
    public static GetCart getInstance()
    {
        if (instance == null) {
            instance = new GetCart();
        }
        return instance;
    }
    public List<String[]> StringToArray(String str)
    {
        List<String[]> arr2;
        String[] arr = str.split(",");
        arr2 = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {
            arr2.add(arr[i].split("of"));
        }
        return arr2;
    }
    public String listToStringSendAPI(String str)
    {
        String stringForSendAPI = "";
        if (str != "") {
            List<String[]> arrList = StringToArray(str);
            for (int i = 0; i < arrList.size(); i++) {
                stringForSendAPI += (i == 0) ? arrList.get(i)[0] : "," + arrList.get(i)[0];
            }
        }
        return stringForSendAPI;
    }
    public List<String[]> remove(int position, List<String[]> arr)
    {
        List<String[]> result = arr;
        result.remove(position);
//        for (int i = 0; i < result.size(); i++) {
//            Log.d("BBB",result.get(i)[0]);
//            Log.d("BBB",result.get(i)[1]);
//            Log.d("BBB","-----------------------");
//        }
        return result;
    }

    public String arrayToStringInsertAPI(List<String[]> arr)
    {
        String str = "";
        if ( arr.size() > 0 ) {
            for (int i = 0; i < arr.size(); i++) {
                str += (i == 0) ? arr.get(i)[0] + "of" + arr.get(i)[1] : "," + arr.get(i)[0] + "of" + arr.get(i)[1];
            }
        }
        Log.d("BBB",str);
        return str;
    }



}
