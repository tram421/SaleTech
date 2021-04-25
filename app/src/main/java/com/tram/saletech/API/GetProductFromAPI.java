package com.tram.saletech.API;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetProductFromAPI{
    public synchronized List<Product> startReadAPI(){

        //Tạo 1 thread riêng đọc API, Quá trình đọc khá chậm không muốn ảnh hưởng đến main thread
        List<Product> list = new ArrayList<>();

                ResultAPI resultAPI = new ResultAPI();
                resultAPI.init();
                resultAPI.resultProductAPI().enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        for(int i = 0; i <response.body().size(); i++){
                            list.add(response.body().get(i));

                        }
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Log.d("BBB", "Lỗi 111 (đọc API): " + t.getMessage());
                    }
                });

        return list;

    }


}
