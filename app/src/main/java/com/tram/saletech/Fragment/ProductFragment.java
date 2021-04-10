package com.tram.saletech.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tram.saletech.API.APIRequest;
import com.tram.saletech.API.Product;
import com.tram.saletech.API.ResultAPI;
import com.tram.saletech.Activities.MainActivity;
import com.tram.saletech.R;
import com.tram.saletech.RecyclerView.ProductAdapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {
    RecyclerView mRecyclerView;
    List<Product> mArr;
    ProductAdapter mAdapter;
    ImageView imageView;
    
    ImageView bmImage;
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

        
        mArr = new ArrayList<>();
        ResultAPI resultAPI = new ResultAPI();
        resultAPI.init();

        resultAPI.resultProductAPI().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
//                products.add(response.body().get(0));
                for (int i = 0; i < response.body().size(); i++) {
                    mArr.add(response.body().get(i));
                }
//                mArr.add(response.body().get(0));
//                mArr.add(response.body().get(1));
//                Log.d("BBB", "Trong response: " + products.toString());
                Log.d("BBB", "Trong response: " + mArr + "");
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d("BBB",  "Lỗi: "+ t.getMessage());
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter = new ProductAdapter(mArr);
                mRecyclerView.setAdapter(mAdapter);
                Log.d("BBB", "ngoai response: " + mArr + "");
            }
        }, 1000);









//        Toast.makeText(getContext(), resultAPI.getKq()+"", Toast.LENGTH_SHORT).show();
//        Log.d("AAA", String.valueOf(resultAPI.getKq().get(0).getId()));
//        final String imgURL  = resultAPI.getKq().get('image');
//        new ProductFragment.LoadImage(bmImage).execute(imgURL);
//        imageView.setImageBitmap();
//        resultAPI.getKq();


//        mArr =
        // Inflate the layout for this fragment

//        bmImage = v.findViewById(R.id.imageView1);
//        final String imgURL  = "http://maitram.net/api/image/tv01.jpg";
//        new ProductFragment.LoadImage(bmImage).execute(imgURL);

        return v;
    }

    //Lấy ảnh từ internet về
//    public static class LoadImage extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public LoadImage(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urlOfImage = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urlOfImage).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//    }
}