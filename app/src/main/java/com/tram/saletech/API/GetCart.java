package com.tram.saletech.API;

import android.util.Log;

import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

public class GetCart {
    private static GetCart instance = null;
    public List<Product> listProductInCartArr;
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
    //đầu vào là string dạng 1of2,5of8
    //đầu ra là dạng [{1,2},{5,8}]
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
        if(result.size() > position)
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
        if ( arr.size() > 0) {
            for (int i = 0; i < arr.size(); i++) {
                if(arr.size() > i)
                    try {
                        str += (i == 0) ? arr.get(i)[0] + "of" + arr.get(i)[1] : "," + arr.get(i)[0] + "of" + arr.get(i)[1];
                    } catch (Exception e) {
                        Log.d("BBB","Lỗi trong GetCart" + e.getMessage());
                        str = "0of0";

                    }

            }
        }
        return str;
    }

//mGetCart.listProductInCartArr.add(mArr.get(postiton));
    public List<Product> listProductInCart(List<Product> listAllProduct, List<String[]> listInCart)
    {
        List<Product> arr = new ArrayList<>();
        if(listAllProduct != null && listInCart != null) {
            if (listAllProduct.size() > 0 && listInCart.size() > 0) {
                for (int i = 0; i < listInCart.size(); i++) {
                    for (int j = 0; j < listAllProduct.size(); j++) {
                        String idInput = listInCart.get(i)[0];
                        String idList = listAllProduct.get(j).getId();
                        Product tempProduct;
                        if (idInput.equals(idList)) {
                            tempProduct = listAllProduct.get(j);
                            tempProduct.setQuantity(Integer.parseInt(listInCart.get(i)[1]));
                            arr.add(tempProduct);
                        }

                    }
                }
            }
        }





        return arr;
    }

}
