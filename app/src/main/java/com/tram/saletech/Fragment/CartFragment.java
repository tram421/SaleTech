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
import com.tram.saletech.API.OrderInfo;
import com.tram.saletech.API.Product;
import com.tram.saletech.API.ResultAPI;
import com.tram.saletech.API.User;
import com.tram.saletech.API.VoucherInfo;
import com.tram.saletech.Interface.OnListenerItem;
import com.tram.saletech.R;
import com.tram.saletech.RecyclerView.CartAdapter;
import com.tram.saletech.RecyclerView.ProductAdapter;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

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
    //bill
    TextView mTxtBill;
    VoucherInfo mVoucherInfor;
    TextView mTxtVoucher;
    Button mBtnOrder;
    OrderInfo mOrderInfo;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        callAPI();
        mcount = 0;
        callIdUserFromUserFragment();
        mTxtEmptyCart = view.findViewById(R.id.emtyCart);
        mGetCart = GetCart.getInstance();
        mRecyclerView = view.findViewById(R.id.recyclerViewInCart);
        mTxtBill = view.findViewById(R.id.txtBill);
        mTxtVoucher = view.findViewById(R.id.txtVoucher);
        mBtnOrder = view.findViewById(R.id.btnOrder);
        mVoucherInfor = VoucherInfo.getInstance();
        mOrderInfo = OrderInfo.getInstance();
        setRecyclerView(view);
        loadInfoUser(mIdUser);
        mBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Product> list = new ArrayList<>();
                list = mGetCart.listProductInCart(mAllProduct.listAllProduct, mGetCart.listAllCart);
                String sendDescription = "" ;
                for (int i = 0; i < list.size(); i++) {
                    sendDescription += list.get(i).getName();
                    sendDescription += "---S??? l?????ng: "+list.get(i).getQuantity() + ",\n";
                }
                sendDescription += "...END...";
                sendOrder(mIdUser, mGetCart.arrayToStringInsertAPI(mGetCart.listAllCart), mVoucherInfor.idVoucher, getBill(),sendDescription);

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mArr = mGetCart.listProductInCart(mAllProduct.listAllProduct, mGetCart.listAllCart);
                //c???p nh???t cart m??i khi chuy???n sang th??? cart
                if (mGetCart.listAllCart != null) {
                    mArr.clear();
                    for (int i = 0; i < mGetCart.listAllCart.size(); i++) {
                        for (int j = 0; j < mAllProduct.listAllProduct.size(); j++) {
                            String id = mAllProduct.listAllProduct.get(j).getId();
                            if (mGetCart.listAllCart.get(i)[0].equals(id)) {
                                mArr.add(mAllProduct.listAllProduct.get(j));//
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
                getBill();
            }
        },500);
    }

    @Override
    public void onStart() {
        //ch??? load xong
        super.onStart();
        getBill();
    }
    //  L??u d??? li???u l???i l??n database khi t???t app
    @Override
    public void onStop() {
        super.onStop();
        saveCartToServer(mGetCart, mIdUser);
    }


    /*
    listProduct c?? d???ng: 1of2,5of3....
    */
    private void sendOrder(int userID, String listProduct, int idVoucher, int totalBill, String description)
    {
        ResultAPI insertToOrderTable = new ResultAPI();
        insertToOrderTable.insertToOrder(userID, listProduct, idVoucher, totalBill, description).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                mOrderInfo.idOrder[] = Integer.parseInt(response.body());
                if(Integer.parseInt(response.body()) > 0) {
                    Toast.makeText(getActivity(), "?????t h??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("BBB","Insert d??? li???u t??? Cart Fragment th???t b???i: " + t.getMessage());
            }
        });
        Toast.makeText(getActivity(), "Nh???n ?????t h??ng" + totalBill, Toast.LENGTH_SHORT).show();
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
                    mAdapter.notifyItemRemoved(Integer.parseInt(position + "")); //x??a 1 item
                    mGetCart.listAllCart = mGetCart.remove(position,mGetCart.listAllCart);
                    getBill();
                }
            });
        }
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
                    mAdapter.notifyItemRemoved(Integer.parseInt(position + "")); //x??a 1 item
                    mGetCart.listAllCart = mGetCart.remove(position,mGetCart.listAllCart);
                    getBill();
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
    public static void saveCartToServer(GetCart mGetCart, int mIdUser)
    {
        ResultAPI resultAPI = new ResultAPI();
//        Log.d("BBB", String.valueOf(mIdUser));
        if(mGetCart.listAllCart != null) {
            if(mGetCart.listAllCart.size() > 0 ) {
                resultAPI.insertListCartToUser(mGetCart.arrayToStringInsertAPI(mGetCart.listAllCart), mIdUser).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.body().equals("SUCCESSFUL")) {
                            Log.d("BBB","Trong Cart Fragment: L??u cart th??nh c??ng");
                        } else {
                            Log.d("BBB","L???i: trong CartFragment: Kh??ng g???i ???????c d??? li???u l??n server");
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        } else {
            Log.d("BBB","Kh??ng c?? g?? ????? l??u");
        }
    }
    //h??m t??nh ti???n
    private int getBill()
    {
        String content = "";
        int allTotal = 0;

        int[] total = new int[mArr.size()];
        if (mGetCart.listAllCart != null) { //ki???m tra c?? t???n t???i bi???n kh???i t???o cart ch??a
            if (mGetCart.listAllCart.size() > 0) { //ki???m tra s??? l?????ng s???n ph???m trong gi??? h??ng ch??a
                if (mArr.size() > 0) {//ki???m tra m???ng thu???c recyclerView c?? ???????c g??n d??? li???u ch??a
                    for (int i = 0; i < mArr.size(); i++) {
                        total[i] = Integer.parseInt(mArr.get(i).getSale()) * mArr.get(i).getQuantity() ;

                    }
                    content += "T???ng ti???n h??ng: ";
                    for (int j = 0; j < total.length; j++) {
                        allTotal += total[j];
                    }
                    content += String.format("%,d", allTotal) + " ??";
                }
            }
        }
        mTxtVoucher.setText("Voucher:     " + mVoucherInfor.nameVoucher);
        content += "\n-" + String.format("%,d", mVoucherInfor.valueVoucher) + " ??";
        content += "\n T???ng thanh to??n:       " + String.format("%,d", (allTotal - mVoucherInfor.valueVoucher)) + " ??";
        mTxtBill.setText(content);
        return allTotal;
    }
    private void callAPI()
    {
        flagWaitCallAPI = false;
        Thread threadCallAPI = new Thread(new Runnable() {
            @Override
            public void run() {
                if(mIdUser == 0) {
                    try {
                        Thread.sleep(700); //ch??? 1 ch??t ????? g???i l???i, Ch??? get d??? li???u v???
                    } catch (InterruptedException e) {
                        Log.d("BBB","l???i CartFragment: " + e.getMessage());
                    }
                    callAPI();
                } else {
                    ResultAPI resultAPI = new ResultAPI();
                    resultAPI.init();
                    resultAPI.resultUserAPI(mIdUser).enqueue(new Callback<List<User>>() {
                        @Override
                        public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                            //gi??? s??? d??? li???u l???y v??? c?? d???ng: 1|5,5|3,8|1 ho???c null
                            if (mGetCart.listAllCart != null) {
                                mListProductInCart = response.body().get(0).getIdproduct() ; //1of5,5of3,8of1
                                mGetCart.listAllCart = mGetCart.StringToArray(mListProductInCart);
                                //g???i l??n api ????? l???y d??? li???u s???n ph???m
                                ResultAPI resultAPI_GetProductInCart = new ResultAPI();
                                resultAPI_GetProductInCart.resultCartAPI(mGetCart.listToStringSendAPI(mListProductInCart))
                                        .enqueue(new Callback<List<Product>>() {
                                            @Override
                                            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                                                mArr = new ArrayList<>();
                                                Product tempProduct;
                                                int quantity;
                                                for (int j = 0; j < response.body().size(); j++) {
                                                    for (int i = 0; i < mGetCart.listAllCart.size(); i++) {
                                                        if(response.body().get(j).getId().equals(mGetCart.listAllCart.get(i)[0])){
                                                            quantity = Integer.parseInt(mGetCart.listAllCart.get(i)[1]);
                                                            tempProduct = response.body().get(j);
                                                            tempProduct.setQuantity(quantity);
                                                            mArr.add(response.body().get(j));
                                                        }
                                                    }
                                                }
                                                flagWaitCallAPI = true;
                                            }
                                            @Override
                                            public void onFailure(Call<List<Product>> call, Throwable t) {
                                                Log.d("BBB", "L???i t??? cart fragment: " + t.getMessage());
                                            }
                                        });
                            } else {
                                Log.d("BBB","Trong CartFragment: Ch??a c?? d??? li???u trong gi??? - begin");
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
                    if (mIdUser == 0) { //ch??a l???y ???????c user id
                        callIdUserFromUserFragment();
                    } else { //l???y ???????c th?? tr??? count v??? 0
                        mcount = 0;
                    }
                } else { //user ch??a th???c hi???n h??m set argument
                    new Handler().postDelayed(new Runnable() { //delay ch??? user th???c hi???n
                        @Override
                        public void run() {
                            callIdUserFromUserFragment();
                        }
                    },100);
                }

            } else { //g???i ????? quy qu?? nhi???u l???n v???n ch??a get ???????c d??? li???u
                Log.d("BBB"," Trong cart fragment: Kh??ng l???y ???????c d??? li???u t??? userfragment tr??? qua");
            }
    }
    private void loadInfoUser(Integer idUser)
    {
       if(idUser > 0) {
           ResultAPI resultAPI = new ResultAPI();

           resultAPI.resultUserAPI(idUser).enqueue(new Callback<List<User>>() {
               @Override
               public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                   mGetCart.listAllCart = mGetCart.StringToArray(response.body().get(0).getIdproduct());
               }
               @Override
               public void onFailure(Call<List<User>> call, Throwable t) {
                   Log.d("BBB", "L???i " + t.getMessage());
               }
           });
       } else {
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   loadInfoUser(mIdUser);
               }
           },1000);
       }

    }
}