package com.tram.saletech.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tram.saletech.API.AllProduct;
import com.tram.saletech.API.GetCart;
import com.tram.saletech.API.MyFlag;
import com.tram.saletech.API.Product;
import com.tram.saletech.API.ResultAPI;
import com.tram.saletech.API.User;
import com.tram.saletech.Interface.OnListenerItem;
import com.tram.saletech.R;
import com.tram.saletech.RecyclerView.CartAdapter;
import com.tram.saletech.RecyclerView.ProductAdapter;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    String mListProductInCart;
    int mIdUser;
    int mcount;
    Boolean flagWaitCallAPI = false;
    TextView mTxtEmptyCart;
//recycler
    RecyclerView mRecyclerView;
    List<Product> mArr = new ArrayList<>();
    CartAdapter mAdapter;
    GetCart mGetCart;
    AllProduct mAllProduct = AllProduct.getInstance();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mcount = 0;
//        mArr = Product.getMock();
        callIdUserFromUserFragment();
        callAPI();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

//        UserFragment userFragment = new UserFragment();
//        String m;
//        m = userFragment.getArguments().getString("mParam1");
   

//        Bundle bundle = this.getArguments();

//        if (bundle != null) {
//            String myInt = bundle.getString("mParam1", "");
//            Log.d("BBB",myInt + "Trong cartFragment");
//        }
        mTxtEmptyCart = view.findViewById(R.id.emtyCart);
        mGetCart = GetCart.getInstance();
        mRecyclerView = view.findViewById(R.id.recyclerViewInCart);
        setRecyclerView(view);
        return view;
    }
    private void setRecyclerView(View view)
    {
        if (mArr.size() > 0) {

            mAdapter = new CartAdapter(mArr);
            mRecyclerView.setAdapter(mAdapter);
            mTxtEmptyCart.setVisibility(View.GONE);

            mAdapter.setOnItemClickListener(new OnListenerItem() {
                @Override
                public void onClick(int position) {
//                Toast.makeText(MainActivity.this, mArrNowFoodVns.get(position).getName(), Toast.LENGTH_SHORT).show();
                    mArr.remove(position); //xoa trong mang
                    mAdapter.notifyItemRemoved(Integer.parseInt(position + "")); //xóa 1 item
                    mGetCart.listAllCart = mGetCart.remove(position,mGetCart.listAllCart);

                }
            });



        } else {
//            Chua get du lieu
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setRecyclerView(view);
                }
            },200);
        }
        
    }

    @Override
    public void onResume() {
        super.onResume();

        mArr = mGetCart.listProductInCart(mAllProduct.listAllProduct, mGetCart.listAllCart);
        //cập nhật cart môi khi chuyển sang thẻ cart
        if (mGetCart.listAllCart != null) {
            mArr.clear();
            Log.d("BBB", String.valueOf(mArr.size()));
            for (int i = 0; i < mGetCart.listAllCart.size(); i++) {
                for (int j = 0; j < mAllProduct.listAllProduct.size(); j++) {
                    String id = mAllProduct.listAllProduct.get(j).getId();
                    if (mGetCart.listAllCart.get(i)[0].equals(id)) {
                        mArr.add(mAllProduct.listAllProduct.get(j));
//
                    }
                }
            }
            if (mArr.size() > 0) {
                mTxtEmptyCart.setVisibility(View.GONE);
            }
            if (mArr.size() > 1) {
                updateRecyclerView();
            }
        }



    }


    private void updateRecyclerView()
    {

        mArr = mGetCart.listProductInCart(mAllProduct.listAllProduct, mGetCart.listAllCart);
        if (mArr.size() > 0) {
            mAdapter = new CartAdapter(mArr);
            mRecyclerView.setAdapter(mAdapter);
            mTxtEmptyCart.setVisibility(View.GONE);

            mAdapter.setOnItemClickListener(new OnListenerItem() {
                @Override
                public void onClick(int position) {
//                Toast.makeText(MainActivity.this, mArrNowFoodVns.get(position).getName(), Toast.LENGTH_SHORT).show();
                    mArr.remove(position); //xoa trong mang
                    mAdapter.notifyItemRemoved(Integer.parseInt(position + "")); //xóa 1 item
                    mGetCart.listAllCart = mGetCart.remove(position,mGetCart.listAllCart);

                }
            });
        }

    }
    //  Lưu dữ liệu lại lên database khi tắt app
    @Override
    public void onStop() {
        super.onStop();

        ResultAPI resultAPI = new ResultAPI();
//        Log.d("BBB", String.valueOf(mIdUser));
        if(mGetCart.listAllCart != null) {
            resultAPI.insertListCartToUser(mGetCart.arrayToStringInsertAPI(mGetCart.listAllCart), mIdUser).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().equals("SUCCESSFUL")) {
                        Log.d("BBB","Trong Cart Fragment: Lưu cart thành công");
                    } else {
                        Log.d("BBB","Lỗi: trong CartFragment: Không gửi được dữ liệu lên server" + mGetCart.arrayToStringInsertAPI(mGetCart.listAllCart));
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        } else {
            Log.d("BBB","Không có gì để lưu");
        }


    }



    @Override
    public void onStart() {

        //chờ load xong

        super.onStart();
        


        

    }

    private void callAPI()
    {
        flagWaitCallAPI = false;
        Thread threadCallAPI = new Thread(new Runnable() {
            @Override
            public void run() {
                if(mIdUser == 0) {
                    try {
                        Thread.sleep(700); //chờ 1 chút để gọi lại, Chờ get dữ liệu về
                    } catch (InterruptedException e) {
                        Log.d("BBB","lỗi CartFragment: " + e.getMessage());
                    }
                    callAPI();
                } else {
                    ResultAPI resultAPI = new ResultAPI();
                    resultAPI.init();
                    resultAPI.resultUserAPI(mIdUser).enqueue(new Callback<List<User>>() {
                        @Override
                        public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                            //giả sử dữ liệu lấy về có dạng: 1|5,5|3,8|1 hoặc null
                            if (mGetCart.listAllCart != null) {
                                mListProductInCart = response.body().get(0).getIdproduct() ; //1of5,5of3,8of1
                                mGetCart.listAllCart = mGetCart.StringToArray(mListProductInCart);
                                //gọi lên api để lấy dữ liệu sản phẩm
                                ResultAPI resultAPI_GetProductInCart = new ResultAPI();
                                resultAPI_GetProductInCart.init();
                                resultAPI_GetProductInCart.resultCartAPI(mGetCart.listToStringSendAPI(mListProductInCart))
                                        .enqueue(new Callback<List<Product>>() {
                                            @Override
                                            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                                                mArr = new ArrayList<>();
                                                for (int j = 0; j < response.body().size(); j++) {
                                                    mArr.add(response.body().get(j));
                                                }
                                                flagWaitCallAPI = true;

                                            }

                                            @Override
                                            public void onFailure(Call<List<Product>> call, Throwable t) {
                                                Log.d("BBB", "Lỗi từ cart fragment: " + t.getMessage());
                                            }
                                        });
                            }


                        }

                        @Override
                        public void onFailure(Call<List<User>> call, Throwable t) {
                            Log.d("BBB",t.getMessage() +  " : Trong CartFragment");
                        }
                    });
                }
            }
        });
        threadCallAPI.start();

    }
    private synchronized void callIdUserFromUserFragment()
    {
        mcount++;
            if(mcount < 1000)
            {
                if(UserFragment.mCartFragment != null){
                    mIdUser = UserFragment.mCartFragment.getArguments().getInt("idUser");
                    if (mIdUser == 0) { //chưa lấy được user id
                        callIdUserFromUserFragment();
                    } else { //lấy được thì trả count về 0
                        mcount = 0;
                    }
                } else { //user chưa thực hiện hàm set argument
                    new Handler().postDelayed(new Runnable() { //delay chờ user thực hiện
                        @Override
                        public void run() {
                            callIdUserFromUserFragment();
                        }
                    },100);
                }

            } else { //gọi đệ quy quá nhiều lần vẫn chưa get được dữ liệu
                Log.d("BBB"," Trong cart fragment: Không lấy được dữ liệu từ userfragment trả qua");
            }





    }
    private void loadInfoUser(Integer idUser)
    {
        ResultAPI resultAPI = new ResultAPI();
        resultAPI.init();

        resultAPI.resultUserAPI(idUser).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
//                mListProductInCart = response.body().get(0).getIdproduct();
//                mIdVoucher = Integer.parseInt(String.valueOf(response.body().get(0).getIdVoucher()));
//                mIdOrder = Integer.parseInt(String.valueOf(response.body().get(0).getIdOrder()));
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("BBB", "Lỗi " + t.getMessage());
            }
        });
    }
}