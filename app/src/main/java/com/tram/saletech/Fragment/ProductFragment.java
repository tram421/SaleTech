package com.tram.saletech.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tram.saletech.API.MyFlag;
import com.tram.saletech.API.Product;
import com.tram.saletech.API.ResultAPI;
import com.tram.saletech.Activities.MainActivity;
import com.tram.saletech.R;
import com.tram.saletech.RecyclerView.PaginationScrollListener;
import com.tram.saletech.RecyclerView.ProductAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {
    RecyclerView mRecyclerView;
    List<Product> mArr = new ArrayList<>();
    List<Product> mListAPI;
    TextView mSearchContent;
    MainActivity mainActivity;
    public String mSearch;
//    public interface ReceiveData{
//        public void data(String data);
//    }

    //    ReceiveData receiveData;
    ProductAdapter mAdapter;
    private boolean isLoading;
    private boolean isLastpage = false;
    private int currentPage = 1;
    private int itemEachPage = 6;
    private int totalPage = 0;
    private int totalItem = 0;
    private boolean mFlag = false;
    private boolean flagLoadmore = true;



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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //Vừa Attach thread xong là get api về
        mListAPI = startReadAPI();

        CountDownTimer countDownTimer = new CountDownTimer(1000,10) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(mListAPI.size() >0){
                    Toast.makeText(context, "Tải dữ liệu xong", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFinish() {
            }
        };
        countDownTimer.start();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_product, container, false);
        mainActivity = (MainActivity) getActivity();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchContent = view.findViewById(R.id.searchContent);
        mRecyclerView = view.findViewById(R.id.recyclerViewProduct);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ProductAdapter(mArr);
        mRecyclerView.setAdapter(mAdapter);
        setFirstData();

        Log.d("BBB","Vao loadMoreItonViewCreatedem: " + this.flagLoadmore);
//        Log.d("BBB","Vao loadMoreItonViewCreatedem: " + this.isLastpage);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    //gọi mỗi khi mở sang tab product
    @Override
    public void onResume() {
        super.onResume();
        //gán lại cờ cho resume khi trang được quay lại mà ko có từ khóa search...cho phép loadmore
//        mSearch = null;
        mSearch = "";
        if(isEmpty(mainActivity.mInputSearch.getText())){
            currentPage = 1;
            flagLoadmore = true;
//            mSearch = null;
            this.isLoading = false;
            this.isLastpage = false;
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
            mRecyclerView.setLayoutManager(layoutManager);
            mAdapter = new ProductAdapter(mArr);
            mRecyclerView.setAdapter(mAdapter);
            setFirstData();
            //cuộn trang để load more
            mRecyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager, mListAPI, currentPage, flagLoadmore) {
                @Override
                public void loadMoreItem() {
                    isLoading = true;
                    currentPage += 1;
                    //Độ trễ để đủ thời gian get dữ liệu về từ API
                    loadNextPage();
                }
                @Override
                public boolean isLoading() {
                    return isLoading;
                }
                @Override
                public boolean isLastPage() {
                    if(currentPage >= totalPage){
                        mAdapter.removeFooterLoading();
                    }
                    return isLastpage;
                }
            });


        } else {
            if(mainActivity.mProductFragment.getArguments() != null){
                mSearch = mainActivity.mProductFragment.getArguments().getString("Send_fragment");
                if(mSearch != null){
                    mSearchContent.setText("Kết quả tìm kiểm của: " + mSearch);
                    flagLoadmore = false;
                    loadData(mSearch);

                }
            }
        }

        


//        Log.d("BBB",mainActivity.mInputSearch.getText().toString() + " : mInputSearch");
    }
    private void loadData(String search){
        List<Product> list =  new ArrayList<>();
            for(int i = 0; i < mListAPI.size(); i++){
                Pattern pattern = Pattern.compile(search, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(mListAPI.get(i).getName());
                boolean matchFound = matcher.find();
                if (matchFound) {
                    list.add(mListAPI.get(i));
                }else {
//                    Log.d("BBB",mListAPI.get(i).getName() + "");
                }
            }
        mArr = list;
        mAdapter.setData(mArr);
    }
    private void setFirstData(){

//        this.mSearch = search;
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
        },700);
    }
    private  List<Product> getListProduct(int currentPage){


        List<Product> list =  new ArrayList<>();
        int startItem = (currentPage - 1) * itemEachPage;
        int endItem = startItem + itemEachPage;
        if(endItem > totalItem) {
            endItem = totalItem;

        }

        if (mListAPI.size() > 0 && startItem < totalItem) {
            for(int i = startItem; i < endItem; i++){
                list.add(mListAPI.get(i));
            }
        }
        return list;

    }

    private void loadNextPage() {
        if (currentPage <= totalPage) {
            isLoading = false;
            List<Product> list = getListProduct(currentPage);
            mAdapter.removeFooterLoading();
            mArr.addAll(list);
            mAdapter.notifyDataSetChanged();

            mAdapter.addFooterLoading();

        } else {
            Log.d("BBB", "Quá số trang");
            isLastpage = true;
        }
    }


    private  List<Product> startReadAPI(){
        //Tạo 1 thread riêng đọc API, Quá trình đọc khá chậm không muốn ảnh hưởng đến main thread
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