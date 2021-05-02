package com.tram.saletech.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.tram.saletech.API.Product;
import com.tram.saletech.AppConstants;
import com.tram.saletech.R;
import com.tram.saletech.RecyclerView.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    ProductAdapter mAdapter;
    List<Product> mArr;
    RecyclerView mRecycler;
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
        Log.d("BBB","kích thước: "+size);
        for (int i = 0; i < size; i++) {
            mArr.add(getIntent.getParcelableExtra(AppConstants.KEY_INTENT_SEND_PRODUCT_KEYOF_ARR + i));
        }

        mAdapter = new ProductAdapter(mArr);
        mAdapter.removeFooterLoading();
        mRecycler.setAdapter(mAdapter);




    }
}