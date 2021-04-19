package com.tram.saletech.Navigation;

import com.tram.saletech.Fragment.CartFragment;
import com.tram.saletech.Fragment.HomeFragment;
import com.tram.saletech.Fragment.ProductFragment;
import com.tram.saletech.Fragment.UserFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

//Dùng FragmentStatePagerAdapter thay cho FragmentPageAdapter dự phòng trường hợp phát triển app sau này
public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new ProductFragment();
            case 2:
                return new UserFragment();
            case 3:
                return new CartFragment();
            default:
                return new HomeFragment();
        }
    }
//navigation_product
    @Override
    public int getCount() {
        return 4;
    }


}
