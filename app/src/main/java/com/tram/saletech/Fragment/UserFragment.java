package com.tram.saletech.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tram.saletech.API.APIRequest;
import com.tram.saletech.API.LoadImage;
import com.tram.saletech.API.Order;
import com.tram.saletech.API.OrderInfo;
import com.tram.saletech.API.ResultAPI;
import com.tram.saletech.API.User;
import com.tram.saletech.API.Voucher;
import com.tram.saletech.API.VoucherInfo;
import com.tram.saletech.Activities.MainActivity;
import com.tram.saletech.R;
import com.tram.saletech.RecyclerView.OrderAdapter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {
    TextView mSignUp;
    EditText mEdtUser, mEdtPass;
    Button mBtnLogin;
    ConstraintLayout mLayoutForm;
    RelativeLayout mLayoutLoginSuccess;
    TextView mBtnLogout;
    //Info user
    TextView mUserInfo;
    public static final String LOGIN_STATE = "LoginState";
    public static final String STATE = "logout";
    public static final String ID_USER = "idUser";
    String state;
    public static Integer mUserId;
    String mFullNameUser;
    String mAdressUser;
    String mPhoneUser;
    String mIdproduct;
    ImageView mImgVoucher;
    int mIdVoucher, mIdOrder;
    static CartFragment mCartFragment;
    VoucherInfo mVoucherInfo;
    OrderInfo mOrderInfo;
    RecyclerView mRecyclerViewOrder;
    private OrderAdapter mAdapter;
    private List<Order> mArr;
    LinearLayout mVoucherLayout;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void sendDatatoCartFragment(){

        CartFragment cartFragment = new CartFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("idUser", mUserId);
        cartFragment.setArguments(bundle);
        mCartFragment = cartFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        sendDatatoCartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        mapping(view);
        //Format
//        String signup = mSignUp.getText().toString();
//        mSignUp.setText((Html.fromHtml("<u>"+ signup + "</u>" ))); //tạo text underline
        mArr = new ArrayList<>();
        mAdapter = new OrderAdapter(mArr);
        loadPreferences();

        loadInfoUser(mUserId);

        mVoucherInfo = VoucherInfo.getInstance();
        mImgVoucher = view.findViewById(R.id.imgVoucher);
        mOrderInfo = OrderInfo.getInstance();
        mVoucherLayout = view.findViewById(R.id.voucherlayout);
        getAPIVoucher();
        loadStateAndUpdateView();



            mAdapter = new OrderAdapter(mArr);
            mRecyclerViewOrder.setAdapter(mAdapter);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ResultAPI resultAPI = new ResultAPI();
        resultAPI.init();

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = mEdtUser.getText().toString();
                String pass = mEdtPass.getText().toString();
                resultAPI.resultUserAPI_checkLogin(user, pass).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String respontCuted = (String) response.body().subSequence(0,3); //vi ket qua tra ve là: xxx|id_user
                        if (response.body().length() > 3)
                            mUserId = Integer.valueOf(response.body().substring(4)); //lấy id user
                        if (Integer.parseInt(respontCuted) == 100) {
                            //ĐĂNG NHẬP THÀNH CÔNG
                            Toast.makeText(getContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            savePreferences("login", mUserId);
                            loadInfoUser(mUserId);
                            getAPIVoucher();
                            loadStateAndUpdateView();


                        } else if (Integer.parseInt(respontCuted) == 200) {
                            Toast.makeText(getContext(), "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Không tồn tại user", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("BBB",t.getMessage() + " : Trong userFragment");

                    }
                });
            }
        });
        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences("logout", mUserId);
                loadStateAndUpdateView();


            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        loadPreferences();

        loadInfoUser(mUserId);
        getAPIVoucher();
        loadStateAndUpdateView();
    }

    private void getAPIVoucher()
    {
        ResultAPI callAPIvoucher = new ResultAPI();
        callAPIvoucher.getVoucherAPI(mUserId).enqueue(new Callback<List<Voucher>>() {
            @Override
            public void onResponse(Call<List<Voucher>> call, Response<List<Voucher>> response) { //kết quả chỉ có 1 voucher
                final String imgURL  = "http://maitram.net/" + response.body().get(0).getImage();
                new LoadImage(mImgVoucher).execute(imgURL);
                mVoucherInfo.valueVoucher = response.body().get(0).getValue();
                mVoucherInfo.nameVoucher = response.body().get(0).getName();
                mVoucherInfo.imgVoucher = imgURL;
                mVoucherInfo.idVoucher = response.body().get(0).getId();

            }

            @Override
            public void onFailure(Call<List<Voucher>> call, Throwable t) {
                Log.d("BBB","Lỗi trong UserFragment: " + t.getMessage());
            }
        });
    }



    private void savePreferences(String state, Integer userId)
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LOGIN_STATE, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(STATE, state);
        editor.putInt(ID_USER, userId);
        this.state = state;
        this.mUserId = userId;
        editor.apply();
    }
    private void loadPreferences()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LOGIN_STATE, getActivity().MODE_PRIVATE);
        state = sharedPreferences.getString(STATE, "logout");
        mUserId = sharedPreferences.getInt(ID_USER, 0);
    }
    private void loadStateAndUpdateView()
    {
                if (state.equals("login")) {
                    mLayoutForm.setVisibility(View.GONE);
                    mLayoutLoginSuccess.setVisibility(View.VISIBLE);
                    mVoucherLayout.setVisibility(View.VISIBLE);
                    mRecyclerViewOrder.setVisibility(View.VISIBLE);
                    mUserInfo.setText("Họ tên: " + mFullNameUser + "\n" + "Điện thoại: " + mPhoneUser + "\n"
                            + "Địa chỉ: " + mAdressUser);
                    if(mVoucherInfo.imgVoucher != null) {
                        new LoadImage(mImgVoucher).execute(mVoucherInfo.imgVoucher);
                    }
                }
                if (state.equals("logout")) {
                    mRecyclerViewOrder.setVisibility(View.GONE);
                    mLayoutForm.setVisibility(View.VISIBLE);
                    mVoucherLayout.setVisibility(View.GONE);
                    mLayoutLoginSuccess.setVisibility(View.GONE);
                }
                mEdtPass.setText("");




    }
    private void mapping(View view)
    {
//        mSignUp = view.findViewById(R.id.signup);
        mEdtUser = view.findViewById(R.id.user);
        mEdtPass = view.findViewById(R.id.password);
        mBtnLogin = view.findViewById(R.id.login);
        mLayoutForm = view.findViewById(R.id.layoutLoginForm);
        mLayoutLoginSuccess = view.findViewById(R.id.layoutSignInSuccess);
        mBtnLogout = view.findViewById(R.id.logOut);
        mUserInfo = view.findViewById(R.id.userInfo);
        mRecyclerViewOrder = view.findViewById(R.id.recyclerViewOrder);
    }
    private void loadInfoUser(Integer idUser)
    {
        mArr = new ArrayList<>();
        Thread threadAPI = new Thread(new Runnable() {
            @Override
            public void run() {
                ResultAPI resultAPI = new ResultAPI();
                resultAPI.init();

                resultAPI.resultUserAPI(idUser).enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.body().size() >0 ) {
                            mFullNameUser = response.body().get(0).getName();
                            mAdressUser = response.body().get(0).getAddress();
                            mPhoneUser = response.body().get(0).getPhone();
                            mIdproduct = response.body().get(0).getIdproduct();
//                mIdVoucher = Integer.parseInt(String.valueOf(response.body().get(0).getIdVoucher()));
//                mIdOrder = Integer.parseInt(String.valueOf(response.body().get(0).getIdOrder()));
                            sendDatatoCartFragment();
                        }

                    }
                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Log.d("BBB", "Lỗi trong UserFragment" + t.getMessage());
                    }
                });

                resultAPI.getOrderOfUser(mUserId).enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        if(response.body().size() > 0) {
                            mOrderInfo.descriptionOrder = new String[response.body().size()];
                            mOrderInfo.idOrder = new int[response.body().size()];
                            mOrderInfo.statusOrder = new int[response.body().size()];
                            for (int i = 0; i < response.body().size(); i++) {
                                mOrderInfo.descriptionOrder[i] = response.body().get(i).getDescription();
                                mOrderInfo.idOrder[i] = response.body().get(i).getId();
                                mOrderInfo.statusOrder[i] = response.body().get(i).getStatus();
                            }
                            for (int i = 0; i < mOrderInfo.idOrder.length; i++) {
                                if(mOrderInfo.statusOrder[i] != 3)
                                     mArr.add(new Order(mOrderInfo.idOrder[i], mOrderInfo.statusOrder[i], mOrderInfo.descriptionOrder[i])  );
                            }
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mAdapter = new OrderAdapter(mArr);
                            mAdapter.notifyDataSetChanged();
                            mRecyclerViewOrder.setAdapter(mAdapter);
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {
                        mArr = new ArrayList<>(); //nếu lỗi thì set bằng 0 vì ko có dữ liệu trả về
                        mAdapter.notifyDataSetChanged();
                        mRecyclerViewOrder.setAdapter(mAdapter);
                        Log.d("BBB", "(444) Lỗi trong UserFragment" + t.getMessage());

                    }
                });
            }



        });
        threadAPI.start();



    }


}