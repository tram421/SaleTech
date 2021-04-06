/*
* @ App bán các mặt hàng công nghệ như: tủ lạnh, máy giặt, tivi, đèn mặt trời...
* @ Dữ liệu lấy về từ database qua API
* @ Chức năng: Trang chủ giới thiệu, sản phẩm, người dùng, giỏ hàng
* @ Mục tiêu: app chạy mô hình MVVM, tốc độ thực thi nhanh, mượt, qua lại giữa các màn hình nhanh,
* dữ liệu xử lý nhanh chóng, code dễ đọc, dễ sửa chữa và cải tiến sau này
*
*/

package com.tram.saletech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ViewPager mViewPager;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bottom navigation
        mViewPager = findViewById(R.id.home_viewPager);
        bottomNavigationView = findViewById(R.id.bottomNav_view);
        setupViewPager();
        //Nhấn chọn item của menu thì view pager thông qua adapter điều hướng qua fragment tương ứng
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.navigation_user:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.navigation_cart:
                        mViewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });


    }
    //Quản lý ViewPager
    private void setupViewPager(){
        //khởi tạo adapter
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerAdapter);
        //event khi người dùng lướt chọn fragment
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_user).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_cart).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}