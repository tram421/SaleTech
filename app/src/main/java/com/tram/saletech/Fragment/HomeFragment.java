package com.tram.saletech.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
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
import android.widget.ViewSwitcher;

import com.tram.saletech.API.LoadImage;
import com.tram.saletech.API.Product;
import com.tram.saletech.Activities.MainActivity;
import com.tram.saletech.R;
import com.tram.saletech.RecyclerView.ProductAdapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;


public class HomeFragment extends Fragment{
    ProductAdapter mAdapter;
    RecyclerView mListHot, mListNew, mListSale;
    List<Product> mArr = new ArrayList<>();
    int eachItemWidth = 430;
    ImageSwitcher mBanner;
    String[] mArrImages = {
            "http://maitram.net/api/image/banner1.jpg",
            "http://maitram.net/api/image/banner2.jpg",
            "http://maitram.net/api/image/banner3.jpg"
    };
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
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mainActivity = (MainActivity) getActivity();
        initRecyclerViews(view);
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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

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

    private void initRecyclerViews(View view){

        mListHot = view.findViewById(R.id.listHot);
        mListNew = view.findViewById(R.id.listNew);
        mListSale = view.findViewById(R.id.listSale);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mArr = new DataMock().getMock();

//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 500);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(eachItemWidth * mArr.size(), LinearLayout.LayoutParams.WRAP_CONTENT);

        mListHot.setLayoutParams(layoutParams);
        mListHot.setLayoutManager(layoutManager);

        mListNew.setLayoutParams(layoutParams);
        mListSale.setLayoutParams(layoutParams);
        mListNew.setLayoutManager(layoutManager1);
        mListSale.setLayoutManager(layoutManager2);

        mAdapter = new ProductAdapter(mArr);
        mListHot.setAdapter(mAdapter);

        mListNew.setAdapter(mAdapter);
        mListSale.setAdapter(mAdapter);
    }





}

class DataMock{
    public List<Product> getMock(){
        List<Product> listMock = new ArrayList<>();
        listMock.add(new Product("1","Tên sản phẩm","api/image/tv02.jpg","2000","2000","Tram","Tram","Tram","Tram","Tram"));
        listMock.add(new Product("2","Tên sản phẩm", "api/image/tv03.jpg","20000","2000","Tram","Tram","Tram","Tram","Tram"));
        listMock.add(new Product("3","Tên sản phẩm", "api/image/tv04.jpg","20000","2000","Tram","Tram","Tram","Tram","Tram"));
        listMock.add(new Product("4","Tên sản phẩm","api/image/tv01.jpg","2000","2000","Tram","Tram","Tram","Tram","Tram"));
        listMock.add(new Product("5","Tên sản phẩm", "api/image/tv02.jpg","20000","2000","Tram","Tram","Tram","Tram","Tram"));
        listMock.add(new Product("6","Tram5", "api/image/tv01.jpg","20000","2000","Tram","Tram","Tram","Tram","Tram"));

        return listMock;
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
