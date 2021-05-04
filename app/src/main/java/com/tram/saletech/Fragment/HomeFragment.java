package com.tram.saletech.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tram.saletech.API.AllProduct;
import com.tram.saletech.API.GetProductFromAPI;
import com.tram.saletech.API.Product;
import com.tram.saletech.Activities.MainActivity;
import com.tram.saletech.Activities.NoInternetActivity;
import com.tram.saletech.Activities.ProductActivity;
import com.tram.saletech.AppConstants;
import com.tram.saletech.R;
import com.tram.saletech.RecyclerView.ProductAdapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;


public class HomeFragment extends Fragment  implements OnMapReadyCallback {
    private GoogleMap gm;
    private SupportMapFragment fragment;
    RecyclerView mListHot, mListNew, mListSale;
    List<Product> mListProductAPI;
    LinearLayout mCatTivi, mCatMayGiat, mCatTulanh, mCatGiadung;

    ImageSwitcher mBanner;
    String[] mArrImages = {
            "http://maitram.net/api/image/banner1.jpg",
            "http://maitram.net/api/image/banner2.jpg",
            "http://maitram.net/api/image/banner3.jpg"
    };
    AllProduct mAllProduct;
    boolean mIsStarted = false;
    int mCount = 0;
    MainActivity mainActivity;
//    private  SendData iSendData;

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment getInstance(String string) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("home", string);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListProductAPI = new GetProductFromAPI().startReadAPI();



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);




        mainActivity = (MainActivity) getActivity();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initRecyclerViews(view, mListProductAPI);

            }
        },1000);

        mBanner = view.findViewById(R.id.imageSwitcher);

        mBanner.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }
        });

        Animation out = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);
        Animation in = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);

        mBanner.setOutAnimation(out);
        mBanner.setInAnimation(in);

        mBanner.setImageResource(R.drawable.banner_sample);
        CountDownTimer countDownTimer = new CountDownTimer(2200 , 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mIsStarted = true;
                if (millisUntilFinished - 2000 > 0){
                    if (mCount >= mArrImages.length){
                        mCount = 0;
                    }
                    new LoadImage1(mBanner).execute(mArrImages[mCount++]);
                }
            }

            @Override
            public void onFinish() {
                this.start();
            }
        };
        countDownTimer.start();

        WebView view1 = new WebView(getContext());
        view1 = view.findViewById(R.id.footerContent);
        view.setVerticalScrollBarEnabled(false);

        view1.loadData(getString(R.string.hello), "text/html; charset=utf-8", "utf-8");
//        ((LinearLayout)view.findViewById(R.id.footerContent)).addView(view);
//
//        view.loadData(getString(R.string.hello), "text/html; charset=utf-8", "utf-8");
        mAllProduct = AllProduct.getInstance();
        mCatTivi = view.findViewById(R.id.catTivi);
        mCatMayGiat = view.findViewById(R.id.catMayGiat);
        mCatTulanh = view.findViewById(R.id.catTulanh);
        mCatGiadung = view.findViewById(R.id.catGiadung);
        createMap();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCatTivi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCat(1);
            }
        });
        mCatMayGiat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCat(3);
            }
        });
        mCatTulanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCat(2);
            }
        });
        mCatGiadung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCat(4);
            }
        });




    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }

    @Override
    public void onStart() {
//        getSearchInput();
//        Log.d("BBB",mainActivity.getSend());
//        mainActivity.setGet("Tr get get");
//        sendDataToActivity();
//        String receive_fragment = mainActivity.mHomeFragment.getArguments().getString("Send_fragment");
//        Log.d("BBB","Tu home fragment nhan du lieu tu Main: " + receive_fragment);


        super.onStart();



    }

    @Override
    public void onResume() {
        super.onResume();
        createMap();



    }

    private void getCat(int idCat)
    {
        List<Product> list =  new ArrayList<>();
        String[] key;

        if (mAllProduct.listAllProduct != null) {
            for (int i = 0; i < mAllProduct.listAllProduct.size(); i++) {

                if (mAllProduct.listAllProduct.get(i).getIdCategory().equals(String.valueOf(idCat)))    {
                    list.add(mAllProduct.listAllProduct.get(i));

                }
            }
            Intent intent = new Intent(getActivity(), ProductActivity.class);
            for (int i = 0; i < list.size(); i++) {
                intent.putExtra(AppConstants.KEY_INTENT_SEND_PRODUCT_SIZE, list.size());
                key = new String[list.size()];
                key[i] = AppConstants.KEY_INTENT_SEND_PRODUCT_KEYOF_ARR + i;
                intent.putExtra(key[i], list.get(i));
            }
            startActivity(intent);

        }
    }
    private void initRecyclerViews(View view, List<Product> listAPI){

                ProductAdapter listHotAdapter;
                ProductAdapter listNewAdapter;
                ProductAdapter listSaleAdapter;
                List<Product> listHotArr = new ArrayList<>();
                List<Product> listNewArr = new ArrayList<>();
                List<Product> listSaleArr = new ArrayList<>();

                mListHot = view.findViewById(R.id.listHot);
                mListNew = view.findViewById(R.id.listNew);
                mListSale = view.findViewById(R.id.listSale);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), HORIZONTAL, false);
                LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), HORIZONTAL, false);
                LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), HORIZONTAL, false);

//        FEATURE SẢN PHẨM HOT
                for (int i = 0; i < listAPI.size(); i++) {
                    if (listAPI.get(i).getIsFeature() == 1) { //lấy những sản phẩm có feature = 1 trong database
                        listHotArr.add(listAPI.get(i));
                    }
                }
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AppConstants.WIDTH_OF_EACH_ITEM_PRODUCT * listHotArr.size(),
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mListHot.setLayoutParams(layoutParams);
                mListHot.setLayoutManager(layoutManager);
                listHotAdapter = new ProductAdapter(listHotArr);
                mListHot.setAdapter(listHotAdapter);
                listHotAdapter.removeFooterLoading();
                //SẢN PHẨM MỚI
                if (listAPI.size() > 0) {
                    for (int i = listAPI.size()-1; i > (listAPI.size() - 11); i--) {

                        listNewArr.add(listAPI.get(i));
                    }
                    LinearLayout.LayoutParams layoutParams_new = new LinearLayout.LayoutParams(AppConstants.WIDTH_OF_EACH_ITEM_PRODUCT * listNewArr.size(),
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    mListNew.setLayoutParams(layoutParams_new);
                    mListNew.setLayoutManager(layoutManager1);
                    listNewAdapter = new ProductAdapter(listNewArr);
                    mListNew.setAdapter(listNewAdapter);
                    listNewAdapter.removeFooterLoading();



                //SẢN PHẨM KHUYẾN MÃI
                listSaleArr = listAPI;
                List<Product> list = new ArrayList<>();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    listSaleArr.sort(new SaleSorter());
                }
                for (int i = listSaleArr.size()-1; i > listSaleArr.size()-11; i--) {
                    list.add(listSaleArr.get(i));
                }
                listSaleAdapter = new ProductAdapter(list);
                mListSale.setLayoutParams(layoutParams_new);
                mListSale.setLayoutManager(layoutManager2);
                mListSale.setAdapter(listSaleAdapter);
                listSaleAdapter.removeFooterLoading();
                ProductFragment.getProductItemToCart(true, listHotAdapter, listHotArr, getActivity());
                ProductFragment.getProductItemToCart(true, listSaleAdapter, list, getActivity());
                ProductFragment.getProductItemToCart(true, listNewAdapter, listNewArr, getActivity());
                ProductFragment.clickViewProduct(getActivity(), listHotAdapter,listHotArr);
                ProductFragment.clickViewProduct(getActivity(), listSaleAdapter,list);
                ProductFragment.clickViewProduct(getActivity(), listNewAdapter,listNewArr);
                }
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

    class LoadImage1 extends AsyncTask<String, Void, Bitmap> {
        ImageSwitcher bmImageSwitcher;

        public LoadImage1(ImageSwitcher bmImage) {
            this.bmImageSwitcher = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlOfImage).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            Drawable drawable =new BitmapDrawable(result);
            bmImageSwitcher.setImageDrawable(drawable);

        }


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
    }
    private void createMap() {
//        SupportMapFragment smf = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        SupportMapFragment smf = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        smf.getMapAsync(this);
        smf.getMapAsync(this::onMapReady);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng mekolink = new LatLng(9.957524370109264, 105.73425114151195); //9.957524370109264, 105.73425114151195
        gm = googleMap;
        gm.addMarker(new MarkerOptions().position(mekolink).title("Bách hóa Mekolink"));
        CameraPosition cp = new CameraPosition.Builder().target(mekolink).zoom(12).build();
        gm.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
    }

}

//class DataMock{
//    public List<Product> getMock(){
//        List<Product> listMock = new ArrayList<>();
//        listMock.add(new Product("1","Tên sản phẩm","api/image/tv02.jpg","2000","2000","Tram","Tram","Tram","Tram",1,0));
//        listMock.add(new Product("2","Tên sản phẩm", "api/image/tv03.jpg","20000","2000","Tram","Tram","Tram","Tram",0,0));
//        listMock.add(new Product("3","Tên sản phẩm", "api/image/tv04.jpg","20000","2000","Tram","Tram","Tram","Tram",0,0));
//        listMock.add(new Product("4","Tên sản phẩm","api/image/tv01.jpg","2000","2000","Tram","Tram","Tram","Tram",1,0));
//        listMock.add(new Product("5","Tên sản phẩm", "api/image/tv02.jpg","20000","2000","Tram","Tram","Tram","Tram",1,0));
//        listMock.add(new Product("6","Tram5", "api/image/tv01.jpg","20000","2000","Tram","Tram","Tram","Tram",0,0));
//
//        return listMock;
//    }
//}


