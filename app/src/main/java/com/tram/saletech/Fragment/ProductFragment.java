package com.tram.saletech.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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

import com.tram.saletech.API.AllProduct;
import com.tram.saletech.API.GetCart;
import com.tram.saletech.API.MyFlag;
import com.tram.saletech.API.Product;
import com.tram.saletech.API.ResultAPI;
import com.tram.saletech.Activities.MainActivity;
import com.tram.saletech.Activities.ProductDetailActivity;
import com.tram.saletech.AppConstants;
import com.tram.saletech.Interface.OnListenerToAddCart;
import com.tram.saletech.Interface.OnlistenerClickToView;
import com.tram.saletech.R;
import com.tram.saletech.RecyclerView.PaginationScrollListener;
import com.tram.saletech.RecyclerView.ProductAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

public class ProductFragment extends Fragment {
    RecyclerView mRecyclerView;
    List<Product> mArr = new ArrayList<>();
    ProductAdapter mAdapter;
    public static List<Product> mListAPI;
    TextView mSearchContent;
    MainActivity mainActivity;
    public String mSearch;
    Boolean flagRunFinish = false;
    AllProduct mAllProduct;

    private boolean isLoading = false;
    private boolean isLastpage = false;
    private int currentPage = 1;
    private int itemEachPage = 6;
    private int totalPage = 0;
    private int totalItem = 0;
    private boolean mFlag = false;
    private boolean flagLoadmore = true;

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
        //V???a Attach thread xong l?? get api v???
         mListAPI = startReadAPI();

        mAllProduct = AllProduct.getInstance();

        CountDownTimer countDownTimer = new CountDownTimer(3000,10) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(mListAPI.size() >0){
                    Toast.makeText(getActivity(), "T???i d??? li???u xong", Toast.LENGTH_SHORT).show();
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
        totalPage = (int)Math.ceil((double)mListAPI.size()/(double)itemEachPage);
        totalItem = mListAPI.size();
        mSearchContent = v.findViewById(R.id.searchContent);
        mRecyclerView = v.findViewById(R.id.recyclerViewProduct);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(layoutManager);
        setFirstData();
        mAdapter = new ProductAdapter(mArr);
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    //g???i m???i khi m??? sang tab product
    @Override
    public void onResume() {
        GetCart mGetCart = GetCart.getInstance();

        super.onResume();

        //g??n l???i c??? cho resume khi trang ???????c quay l???i m?? ko c?? t??? kh??a search...cho ph??p loadmore
        mSearch = "";
        if(isEmpty(mainActivity.mInputSearch.getText())){
            //tra ve tong so trang
            totalPage = (int)Math.ceil((double)mListAPI.size()/(double)itemEachPage);
            mSearchContent.setText("");
            currentPage = 1;
            flagLoadmore = true;
            this.isLoading = false;
            this.isLastpage = false;
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
            mRecyclerView.setLayoutManager(layoutManager);
            mAdapter = new ProductAdapter(mArr);
            mRecyclerView.setAdapter(mAdapter);
            setFirstData();
            flagRunFinish = true; //ch???y xong recycler ,  khi m??? app l???n ?????u cung ch???y xong flag n??y
            //cu???n trang ????? load more
            mRecyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager, mListAPI, currentPage, flagLoadmore) {
                @Override
                public void loadMoreItem() {
                    isLoading = true;
                    currentPage += 1;
                    loadNextPage();
                    clickViewProduct(getActivity(), mAdapter, mArr);
                    try {
                        getProductItemToCart(flagRunFinish, mAdapter, mArr, getActivity());

                    } catch (Exception e) {
                        Log.d("BBB","(111) L???i trong ProductFragment: " + e.getMessage());
                    }
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
        } else { //chuy???n tab m?? c?? d??? li???u trong input text
            if(mainActivity.mProductFragment.getArguments() != null){
                mSearch = mainActivity.mProductFragment.getArguments().getString("Send_fragment");
                if(mSearch != null){
                    mSearchContent.setText("K???t qu??? t??m ki???m c???a: " + mSearch);
                    flagLoadmore = false;
                    loadData(mSearch);
                }
            }
        }
        try {
            getProductItemToCart(flagRunFinish, mAdapter, mArr, getActivity());
            clickViewProduct(getActivity(),mAdapter, mArr);
        } catch (Exception e) {
            Log.d("BBB","(222) L???i trong ProductFragment: " + e.getMessage());
        }

    }

    public static void clickViewProduct(FragmentActivity fragmentActivity, ProductAdapter adapter, List<Product> arr)
    {
        adapter.setOnItemToView(new OnlistenerClickToView() {
            @Override
            public void onClickToView(int position) {
                Intent intent = new Intent(fragmentActivity, ProductDetailActivity.class);
                intent.putExtra(AppConstants.KEY_INTENT_VIEW_PRODUCT, arr.get(position));
                fragmentActivity.startActivity(intent);
            }
        });
    }
    public static String getProductItemToCart(Boolean flag, ProductAdapter adapter, List<Product> arr, Context context)
    {
        String result;
        if (flag) {
            GetCart mGetCart = GetCart.getInstance();
            adapter.setOnItemAddToCart(new OnListenerToAddCart() {
                    @Override
                    public void onClick(int postiton) {
                        if(mGetCart.listAllCart != null && mGetCart.listAllCart.size() > 0) {
                            boolean flagEnter = false;
                            if(arr != null && arr.size() > postiton) {

                                    int id = Integer.parseInt(arr.get(postiton).getId());

//                            if (mGetCart.listAllCart.size() > 0) {

                                for (int i = 0; i < mGetCart.listAllCart.size(); i++) {

                                    int temp = 0; //l???y d??? li??u tr??n database ????? v??o bi???n t???m
                                    int quantity = 0;
                                    if(mGetCart.listAllCart.get(i)[0].equals("")) { //n???u gi??? h??ng tr???ng tr??n database s??? tr??? v??? ""
                                        temp = 0;

                                    } else {
                                        temp = Integer.parseInt(mGetCart.listAllCart.get(i)[0]);

                                    }
                                    quantity = Integer.parseInt(mGetCart.listAllCart.get(i)[1]) + 1;
                                    if (id == temp) {
                                        String[] st = {
                                                String.valueOf(id),
                                                String.valueOf(quantity)
                                        };
                                        mGetCart.listAllCart.add(st);
                                        mGetCart.listAllCart = mGetCart.remove(i, mGetCart.listAllCart);
                                        flagEnter = true; //t??m th???y s???n ph???m
                                        Toast.makeText(context, "???? th??m v??o gi??? h??ng", Toast.LENGTH_SHORT).show();
                                        break; //tim thay thi ko loop nua
                                    }
                                }
                                try {
                                if (!flagEnter) { //n???u kh??ng c?? sp tr??ng
                                    String[] st = {
                                            String.valueOf(id),
                                            String.valueOf(1)
                                    };
                                    mGetCart.listAllCart.add(st);
                                    Toast.makeText(context, "???? th??m v??o gi??? h??ng", Toast.LENGTH_SHORT).show();
                                    for (int i = 0; i < mGetCart.listAllCart.size(); i++) {
                                        Log.d("BBB","S???n ph???m trong gi???:" + mGetCart.listAllCart.get(i)[0]);
                                    }
                                }
                                } catch (Exception e) {
                                    Log.d("BBB","(333)L???i trong ProductFragment: " + e.getMessage());
                                }
                            }
                        } else { //n???u ch??a c?? s???n ph???m n??o trong gi??? h??ng
                            String[] addToCart = {
                                    String.valueOf(arr.get(postiton).getId()),
                                    String.valueOf(1)
                            };
                                mGetCart.listAllCart = new ArrayList<>();
                                mGetCart.listAllCart.add(addToCart);
                        }

                    }

                });
            result = "???? th??m v??o gi??? h??ng";
        } else {
            result = "Ch??a th??? th??m v??o gi??? h??ng";
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        getProductItemToCart(flag, adapter, arr, context);
                    } catch (Exception e) {
                        Log.d("BBB","L???i trong ProductFragment: " + e.getMessage());
                    }
                }//flagRunFinish
            },2000);
        }
        return result;
    }

    private void loadData(String search){
        mAdapter.removeFooterLoading();
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
        totalPage = (int)Math.ceil((double)list.size()/(double)itemEachPage);
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

                if (currentPage <= totalPage) {
                    if(currentPage != totalPage) {
                        mAdapter.addFooterLoading();
                    }
                } else {
                    isLastpage = true;
                    mAdapter.removeFooterLoading();
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentPage <= totalPage) {
                    isLoading = false;
                    List<Product> list = getListProduct(currentPage);
                    mAdapter.removeFooterLoading();
                    mArr.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    if (currentPage != totalPage) mAdapter.addFooterLoading();
                } else {
                    //qu?? s??? trang
                    isLastpage = true;
                    mAdapter.removeFooterLoading();
                }
            }
        },1000);
    }
    private  List<Product> startReadAPI(){
        //T???o 1 thread ri??ng ?????c API, Qu?? tr??nh ?????c kh?? ch???m kh??ng mu???n ???nh h?????ng ?????n main thread
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
                        mAllProduct.listAllProduct = list;
                        //Sau khi get du lieu ve tu API thi l???y size chia ra so trang
                        totalPage = (int)Math.ceil((double)list.size()/(double)itemEachPage);
                        totalItem = list.size();

                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Log.d("BBB", "L???i 111 (?????c API): " + t.getMessage());
                        Toast.makeText(getActivity(), "L???i 111", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        threadStartRead.start();
        return list;
    }
}