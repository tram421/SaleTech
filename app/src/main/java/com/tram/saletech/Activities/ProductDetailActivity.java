package com.tram.saletech.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tram.saletech.API.AllProduct;
import com.tram.saletech.API.GetCart;
import com.tram.saletech.API.LoadImage;
import com.tram.saletech.API.Product;
import com.tram.saletech.AppConstants;
import com.tram.saletech.Fragment.CartFragment;
import com.tram.saletech.Fragment.ProductFragment;
import com.tram.saletech.Fragment.UserFragment;
import com.tram.saletech.Interface.OnListenerToAddCart;
import com.tram.saletech.R;
import com.tram.saletech.RecyclerView.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;

public class ProductDetailActivity extends AppCompatActivity {

    WebView mWcontent;
    TextView mTxtName;
    TextView mTxtPrice;
    TextView mTxtSale;
    Button mBtnAddCart;
    GetCart mGetCart;
    ImageView mImg;
    RecyclerView mListSimilar;
    List<Product> mArr;
    ProductAdapter mAdapter;
    @SuppressLint({"ClickableViewAccessibility", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetail);
        mWcontent = findViewById(R.id.contentdetail);
        mTxtName = findViewById(R.id.namedetail);
        mTxtPrice = findViewById(R.id.pricedetail);
        mTxtSale = findViewById(R.id.saledetail);
        mBtnAddCart = findViewById(R.id.btnAddtoCartdetail);
        mGetCart = GetCart.getInstance();
        mImg = findViewById(R.id.imagedetail);
//        Intent intent = getIntent();
//        String chuoi = intent.getStringExtra("chuoi");
//        Toast.makeText(this, chuoi, Toast.LENGTH_SHORT).show();
//        Log.d("BBB",chuoi + "product activiti");


        mWcontent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        Intent receivedIntent = getIntent();

        if(receivedIntent!=null){
            Product product = (Product) receivedIntent.getParcelableExtra(AppConstants.KEY_INTENT_VIEW_PRODUCT);
            String des = product.getDescription();
            String html = product.getDescription().replaceAll("<a",
                    "<a style = 'color: black !important;text-decoration: none;!important'");
            mWcontent.loadData( html, "text/html; charset=utf-8", "utf-8" );
            new LoadImage(mImg).execute("http://maitram.net/" + product.getImage());
            Log.d("BBB",product.getImage());
            mTxtName.setText(product.getName());
            mTxtPrice.setText(Html.fromHtml(
                    "<del>"
                            + String.format("%,d", Integer.parseInt(product.getPrice())) + "đ"
                            + "</del>" ));
            mTxtSale.setText(String.format("%,d", Integer.parseInt(product.getSale())));
            mBtnAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String result = ControllerProductActivity.addToCart(mGetCart.listAllCart, product);
                    if(result.equals("SUCCESSFUL"))
                        Toast.makeText(ProductDetailActivity.this,
                                "Đã thêm vào giỏ hàng",
                                Toast.LENGTH_SHORT).show();
                }
            });
            String[] mStringSimilar = product.getSimilar().split(",");
            if(AllProduct.getInstance().listAllProduct != null) {
                int size = AllProduct.getInstance().listAllProduct.size();
                if ( size > 0) {
                    mArr = new ArrayList<>();
                    for (int i = 0; i < mStringSimilar.length; i++) {
                        for (int j = 0; j < size; j++) {
                            if (mStringSimilar[i].equals(AllProduct.getInstance().listAllProduct.get(j).getId())) {
                                mArr.add(AllProduct.getInstance().listAllProduct.get(j));
                            }
                        }
                    }
                }
            }

            mListSimilar = findViewById(R.id.listSimilar);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, HORIZONTAL, false);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AppConstants.WIDTH_OF_EACH_ITEM_PRODUCT * mArr.size(),
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mListSimilar.setLayoutManager(layoutManager);
            mListSimilar.setLayoutParams(layoutParams);
            mAdapter = new ProductAdapter(mArr);
            mAdapter.removeFooterLoading();
            mListSimilar.setAdapter(mAdapter);
            ProductFragment.clickViewProduct(ProductDetailActivity.this, mAdapter,mArr);

            ProductFragment.getProductItemToCart(true, mAdapter, mArr, ProductDetailActivity.this);


        }




    }
    //trước khi thoát activity thì lưu giỏ hàng lại ...vì cartfragment khi bị create sẻ get dũ liệu từ API
    @Override
    protected void onPause() {
        super.onPause();
        CartFragment.saveCartToServer(GetCart.getInstance(), UserFragment.mUserId); //lưu vào giỏ hàng online
    }
    //ko lưu được trong stop vì chương trình kết thúc trước khi thực hiện lệnh
    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onBackPressed() {
        Intent intent1 = new Intent(ProductDetailActivity.this, MainActivity.class);
        startActivity(intent1);
        super.onBackPressed();
    }
}