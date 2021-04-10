/*
* @ App bán các mặt hàng công nghệ như: tủ lạnh, máy giặt, tivi, đèn mặt trời...
* @ Dữ liệu lấy về từ database qua API
* @ Chức năng: Trang chủ giới thiệu, sản phẩm, người dùng, giỏ hàng
* @ Mục tiêu: app chạy mô hình MVVM, tốc độ thực thi nhanh, mượt, qua lại giữa các màn hình nhanh,
* dữ liệu xử lý nhanh chóng, code dễ đọc, dễ sửa chữa và cải tiến sau này
*
*/

package com.tram.saletech.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tram.saletech.API.ResultAPI;
import com.tram.saletech.R;
import com.tram.saletech.navigation.Navigation;

public class MainActivity extends AppCompatActivity {
    ViewPager mViewPager;
    BottomNavigationView bottomNavigationView;
    Navigation mnavigation = new Navigation();
    int mPressBackCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bottom navigation
        mViewPager = findViewById(R.id.home_viewPager);
        bottomNavigationView = findViewById(R.id.bottomNav_view);
//        setupViewPager();

        mnavigation.setupViewPager(mViewPager, bottomNavigationView, this);
        //API
        ResultAPI resultAPI = new ResultAPI();
//        resultAPI.resultUserAPI();
//        resultAPI.resultProductAPI();


    }

    @Override
    public void onBackPressed() {
        mPressBackCount++;
        if(mPressBackCount<2){
            mnavigation.pressBackNavigation();
            Toast.makeText(this, "Nhấn Back lần nữa để thoát app", Toast.LENGTH_SHORT).show();
            mPressBackCount = 0;
        } else {
            super.onBackPressed();
        }
    }
}