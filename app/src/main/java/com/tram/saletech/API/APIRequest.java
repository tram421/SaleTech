package com.tram.saletech.API;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIRequest  {
    @GET("http://maitram.net/api/user.php")
    Call<List<User>> fetchUser();
    @GET("http://maitram.net/api/product.php")
    Call<List<Product>> fetchProduct();

}
