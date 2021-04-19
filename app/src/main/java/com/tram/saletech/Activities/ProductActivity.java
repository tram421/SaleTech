package com.tram.saletech.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tram.saletech.R;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        String chuoi = intent.getStringExtra("chuoi");
        Toast.makeText(this, chuoi, Toast.LENGTH_SHORT).show();
        Log.d("BBB",chuoi + "product activiti");

    }


}