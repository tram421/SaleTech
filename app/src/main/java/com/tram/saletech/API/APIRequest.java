package com.tram.saletech.API;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIRequest  {
    @POST("http://maitram.net/api/user.php")
    Call<List<User>> fetchUser();
    @GET("http://maitram.net/api/product.php")
    Call<List<Product>> fetchProduct();

    @FormUrlEncoded
    @POST("http://maitram.net/api/user.php")
    Call<List<User>> creatPost(
      @Field("userid") Integer userid
    );

    @FormUrlEncoded
    @POST("http://maitram.net/api/user.php")
    Call<String> creatPost_checkLogin(
            @Field("user") String name,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("http://maitram.net/api/cart.php")
    Call<List<Product>> creatPost_getProductFromCart(
            @Field("cart") String idProduct
    );

    @FormUrlEncoded
    @POST("http://maitram.net/api/cart.php")
    Call<String> inserListCartToUserI(
            @Field("list") String listIdAndQuantity
    );
}
