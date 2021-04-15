package com.tram.saletech.Fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tram.saletech.API.MyFlag;
import com.tram.saletech.API.Product;
import com.tram.saletech.API.ResultAPI;
import com.tram.saletech.R;
import com.tram.saletech.RecyclerView.PaginationScrollListener;
import com.tram.saletech.RecyclerView.ProductAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {
    RecyclerView mRecyclerView;
    List<Product> mArr = new ArrayList<>();
    List<Product> mListAPI;

    ProductAdapter mAdapter;
    private boolean isLoading;
    private boolean isLastpage = false;
    private int currentPage = 1;
    private int itemEachPage = 6;
    private int totalPage = 0;
    private int totalItem = 0;
    private boolean mFlag = false;


    MyFlag myFlagAPI = new MyFlag(0);

//    CallAPIService mCallAPIProduct;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {

        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_product, container, false);

        mRecyclerView = v.findViewById(R.id.recyclerViewProduct);


//        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(layoutManager);


        //Tạo 1 thread riêng đọc API, Quá trình đọc khá chậm không muốn ảnh hưởng đến main thread
        mAdapter = new ProductAdapter(mArr);
        mListAPI = startReadAPI();

        //        mArr.add(mListAPI.get(0));
//        Log.d("BBB",mArr.toString() );

//        mAdapter.setData(startReadAPI());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setFirstData();

            }
        },2000);



        mRecyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager, mListAPI, currentPage) {
            @Override
            public void loadMoreItem() {
                isLoading = true;
                currentPage += 1;
                loadNextPage();

            }

            @Override
            public boolean isLoading() {
//                loadNextPage();
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                Log.d("BBB","cuoi trang");
                if(currentPage >= totalPage && mFlag == false){
                    mAdapter.removeFooterLoading();
                    mFlag = true;
                }

                return isLastpage;
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        return v;
    }


    private void setFirstData(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mArr = getListProduct(currentPage);
                mAdapter.setData(mArr);
                if(currentPage <= totalPage){
                    mAdapter.addFooterLoading();
                } else {
                    isLastpage = true;
                }
            }
        },500);

    }
    private  List<Product> getListProduct(int currentPage){


        List<Product> list =  new ArrayList<>();
        int startItem = (currentPage - 1) * itemEachPage;
        int endItem = startItem + itemEachPage;
        if(endItem > totalItem) {
            endItem = totalItem;

        }
        Log.d("BBB",startItem + ": startItem");
        Log.d("BBB",endItem + ": endItem");

        Log.d("BBB","-----------------------------------------------------------");
        if (mListAPI.size() > 0 && startItem < totalItem) {
            for(int i = startItem; i < endItem; i++){
                list.add(mListAPI.get(i));

            }
        }
        return list;
    }
    private void loadNextPage(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Product> list = getListProduct(currentPage);
                mAdapter.removeFooterLoading();
                mArr.addAll(list);
                mAdapter.notifyDataSetChanged();

                isLoading = false;
                if(currentPage <= totalPage){
                    mAdapter.addFooterLoading();
                } else {
                    isLastpage = true;
                }
            }
        },1000);


    }
    private  List<Product> startReadAPI(){
        List<Product> list = new ArrayList<>();

        Thread threadStartRead = new Thread(new Runnable() {
            @Override
            public void run() {
                ResultAPI resultAPI = new ResultAPI();
                resultAPI.init();
                resultAPI.resultProductAPI().enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        for(int i = 0; i <response.body().size(); i++){
                                list.add(response.body().get(i));
                        }
                        //Sau khi get du lieu ve tu API thi lấy size chia ra so trang
                        totalPage = (int)Math.ceil((double)list.size()/(double)itemEachPage);
                        totalItem = list.size();
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Log.d("BBB", "Lỗi 111 (đọc API): " + t.getMessage());
                        Toast.makeText(getActivity(), "Lỗi 111", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        threadStartRead.start();
        return list;
    }




}