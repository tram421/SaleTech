package com.tram.saletech.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.tram.saletech.API.Product;
import com.tram.saletech.AppConstants;
import com.tram.saletech.Fragment.ProductFragment;
import com.tram.saletech.Interface.OnListenerToAddCart;
import com.tram.saletech.Navigation.Navigation;
import com.tram.saletech.R;
import com.tram.saletech.RecyclerView.ProductAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    ProductAdapter mAdapter;
    List<Product> mArr;
    RecyclerView mRecycler;
    Navigation mNavigation = new Navigation();
    int mPressBackCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        mRecycler = findViewById(R.id.recyclerProduct);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecycler.setLayoutManager(layoutManager);
        Intent getIntent = getIntent();
        mArr = new ArrayList<>();
        int size = getIntent.getIntExtra(AppConstants.KEY_INTENT_SEND_PRODUCT_SIZE,0);
        for (int i = 0; i < size; i++) {
            mArr.add(getIntent.getParcelableExtra(AppConstants.KEY_INTENT_SEND_PRODUCT_KEYOF_ARR + i));
        }

        mAdapter = new ProductAdapter(mArr);
        mAdapter.removeFooterLoading();
        mRecycler.setAdapter(mAdapter);
        ProductFragment.clickViewProduct(ProductActivity.this, mAdapter,mArr);
        ProductFragment.getProductItemToCart(true, mAdapter, mArr, ProductActivity.this);

    }


    static class SaleSorter implements Comparator<Product>
    {
        //sắp xếp theo giá giảm

        @Override
        public int compare(Product o1, Product o2) {
//        return String.valueOf(o1.getId()).compareTo(String.valueOf(o2.getId()));
            int price1 = Math.round(100 - ((Float.parseFloat(o1.getSale()) / Float.parseFloat(o1.getPrice()))*100));
            int price2 = Math.round(100 - ((Float.parseFloat(o2.getSale()) / Float.parseFloat(o2.getPrice()))*100));

            return price1 - price2;
        }

    }
    public void onBackPressed() {
        Intent intent1 = new Intent(ProductActivity.this, MainActivity.class);
        startActivity(intent1);
        super.onBackPressed();
    }
}