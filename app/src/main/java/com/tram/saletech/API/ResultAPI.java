package com.tram.saletech.API;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultAPI {
    private APIRequest apiRequest;
    private List<Product> kq;
    private String name ="";

    public void setKq(List<Product> kq) {
        this.kq = kq;
    }

    public List<Product> getKq() {
        return kq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public APIRequest getApiRequest() {
        return apiRequest;
    }

    public void init(){
        //get API
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://maitram.net/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.apiRequest = retrofit.create(APIRequest.class);
    }
    public Call<List<User>> resultUserAPI(Integer idUser){
        init();

        //Lấy dữ liệu của user
        Call<List<User>> callbackUser = this.apiRequest.creatPost(idUser);

        return callbackUser;

    }
    public Call<String> resultUserAPI_checkLogin(String user, String pass){
        init();

        //Lấy dữ liệu của user
        Call<String> callbackUser = this.apiRequest.creatPost_checkLogin(user, pass);

        return callbackUser;

    }
    public Call<List<Product>> resultProductAPI() {
        init();
//        this.kq = kq;
        //Lấy dữ liệu của product
        Call<List<Product>> callbackProduct = this.apiRequest.fetchProduct();

        return callbackProduct;

    }
    public Call<List<Product>> resultCartAPI(String arrIdProduct) {
        init();
//        this.kq = kq;
        //Lấy dữ liệu của product
        Call<List<Product>> callbackProduct = this.apiRequest.creatPost_getProductFromCart(arrIdProduct);

        return callbackProduct;

    }
    public Call<String> insertListCartToUser(String arrIdProduct) {
        init();
//        this.kq = kq;
        //Lấy dữ liệu của product
        Call<String> callbackProduct = this.apiRequest.inserListCartToUserI(arrIdProduct);

        return callbackProduct;

    }
}
