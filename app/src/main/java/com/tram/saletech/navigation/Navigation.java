package com.tram.saletech.navigation;

import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tram.saletech.Activities.MainActivity;
import com.tram.saletech.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

//Quản lý Navigation
public class Navigation {
    private  ViewPager mViewPager;
    private BottomNavigationView bottomNavigationView;
    private MainActivity mainActivity;

    public ViewPager getmViewPager() {
        return mViewPager;
    }

    //ViewPager for bottom Navigation
    public void setupViewPager(ViewPager mViewPager, BottomNavigationView bottomNavigationView,MainActivity mainActivity){
        this.mViewPager = mViewPager;
        this.bottomNavigationView = bottomNavigationView;
        this.mainActivity = mainActivity;
        //khởi tạo adapter
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mainActivity.getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerAdapter);
        //giữ trạng thái của fragment với số lượng lưu trữ là 2 fragment
        mViewPager.setOffscreenPageLimit(3);
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
                        bottomNavigationView.getMenu().findItem(R.id.navigation_product_activity).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_user).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_cart).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //Nhấn chọn item của menu thì view pager thông qua adapter điều hướng qua fragment tương ứng
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.navigation_product_activity:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.navigation_user:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.navigation_cart:
                        mViewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }

        });


    }

    public void pressBackNavigation(){

        this.mViewPager.setCurrentItem(0);

    }


}
