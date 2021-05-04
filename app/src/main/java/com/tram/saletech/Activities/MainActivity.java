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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tram.saletech.API.GetCart;
import com.tram.saletech.BroadcastInternet;
import com.tram.saletech.Fragment.HomeFragment;
import com.tram.saletech.Fragment.ProductFragment;
import com.tram.saletech.Navigation.ViewPagerAdapter;
import com.tram.saletech.R;
import com.tram.saletech.Navigation.Navigation;
import com.tram.saletech.Service.GetDataService;

import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity {
    private GoogleMap gm;
    private BroadcastInternet mMyBroadCast;
    ViewPager mViewPager;
    BottomNavigationView bottomNavigationView;
    Navigation mNavigation = new Navigation();
    public EditText mInputSearch;
    ImageView mIconSearch;
    public ProductFragment mProductFragment = new ProductFragment();
    public HomeFragment mHomeFragment = new HomeFragment();
    private String send;
    private String get;
    String SearchData_from_searchForm;
    public void setGet(String get) {
        this.get = get;
    }

    public String getSend() {
        return send;
    }

    public String getGet() {
        return get;
    }
    public static boolean active = false;
    //    SendData sendData;
    String mData;
    int mPressBackCount = 0;
    ImageView mClearSearch;

//    @Override
//    public void getSearchInput(String searchString) {
////        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT );
////        HomeFragment homeFragment = (HomeFragment) viewPagerAdapter.getItem(0);
////        homeFragment.getSearchInput(searchString);
//        Log.d("BBB",searchString);
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        if (!connected) {
            active = false;
            Intent intent = new Intent(MainActivity.this, NoInternetActivity.class);
            startActivity(intent);
            super.onStop();

        }


        //broadcast
        mMyBroadCast = new BroadcastInternet();
        IntentFilter filter = new IntentFilter("android.example.sendBroadcast");
        filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        registerReceiver(mMyBroadCast, filter);
        //check internet
        setContentView(R.layout.activity_main);
        active = true;

//        public void sendDataToActivity()
//    {
////            iSendData = mainActivity;
//            String string = mInputSearch.getText().toString().trim();
////            iSendData.send_from_HomeFragment(string);
//        }
        //start service
        Intent intent = new Intent(MainActivity.this, GetDataService.class);
        intent.putExtra("chuoi", "123");
        startService(intent);
//        ContextCompat.startForegroundService(MainActivity.this, intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //bottom navigation
        mViewPager = findViewById(R.id.home_viewPager);
        bottomNavigationView = findViewById(R.id.bottomNav_view);
        //setupViewPager();
        mNavigation.setupViewPager(mViewPager, bottomNavigationView, this);
//        mbtn1 = findViewById(R.id.btn1);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT );
        HomeFragment homeFragment = (HomeFragment) viewPagerAdapter.getItem(0);
        mInputSearch = findViewById(R.id.mysearch);
        mIconSearch = findViewById(R.id.iconSearch);
        mClearSearch = findViewById(R.id.clearSearch);
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

//        mHomeFragment = sendDatatoFragment();
//        mProductFragment = sendDatatoFragment();
        mIconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = mInputSearch.getText().toString().trim();
                send_to_ProductFragment(string);

            }
        });
        //xoa du lieu search
        mClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = "";
                send_to_ProductFragment(string);
                mInputSearch.setText("");
            }
        });
    }

    public void onBackPressed() {
        mPressBackCount++;
        if(mPressBackCount<2){
            mNavigation.pressBackNavigation();
            Toast.makeText(this, "Nhấn Back lần nữa để thoát app", Toast.LENGTH_SHORT).show();
            mPressBackCount = 0;
        } else {
            super.onBackPressed();
        }
    }

    // Hàm trong interface để lấy dữ liệu từ Homefragment
//    @Override
    public void send_to_ProductFragment(String data) {
//        this.SearchData_from_searchForm = data;
//        sendDatatoFragment();
//        Log.d("BBB",SearchData_from_searchForm);
        ProductFragment productFragment = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Send_fragment", data);
        productFragment.setArguments(bundle);
        mProductFragment = productFragment;
        //chuyển tab
        if(data != null && isEmpty(data) == false)
            if (mNavigation.getmViewPager().getCurrentItem() != 1) {
                mNavigation.getmViewPager().setCurrentItem(1);
            } else {
                mNavigation.getmViewPager().setCurrentItem(0);
                mNavigation.getmViewPager().setCurrentItem(1);
            }

    }

    public ProductFragment sendDatatoFragment(){

        ProductFragment productFragment = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Send_fragment", "this.SearchData_from_searchForm");
        productFragment.setArguments(bundle);
        return productFragment;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


//    @Override
//    public void sendString(String string) {
//        Intent intent = new Intent(MainActivity.this, ProductActivity.class);
//        intent.putExtra("chuoi",string);
//        startActivity(intent);
//
//    }


}