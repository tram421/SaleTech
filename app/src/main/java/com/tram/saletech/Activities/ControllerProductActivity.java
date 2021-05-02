package com.tram.saletech.Activities;

import android.widget.Toast;

import com.tram.saletech.API.GetCart;
import com.tram.saletech.API.Product;

import java.util.List;

public class ControllerProductActivity {

    public static String addToCart(List<String[]> listInCart, Product product)
    {
        GetCart mGetCartInside = GetCart.getInstance();
        String result = "";
        //kiểm tra trùng sp có sẵn ko
        int id = Integer.parseInt(product.getId());
        boolean flagEmter =false;
        for (int i = 0; i < listInCart.size(); i++) {
            if (id == Integer.parseInt(listInCart.get(i)[0])) {

                int quantity = Integer.parseInt(listInCart.get(i)[1]) + 1;
                String[] st = {
                        String.valueOf(id),
                        String.valueOf(quantity)
                };
                flagEmter = true;
                listInCart.add(st);
                listInCart = mGetCartInside.remove(i, listInCart);
                break; //tim thay thi ko loop nua
            }
        }
        if (!flagEmter) {
            String[] st = {
                    String.valueOf(id),
                    String.valueOf(1)
            };
            listInCart.add(st);
        }
        result = "SUCCESSFUL";
        return result;
    }


}
