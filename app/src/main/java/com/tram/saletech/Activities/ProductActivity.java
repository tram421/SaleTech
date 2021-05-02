package com.tram.saletech.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tram.saletech.API.GetCart;
import com.tram.saletech.API.LoadImage;
import com.tram.saletech.API.Product;
import com.tram.saletech.AppConstants;
import com.tram.saletech.Fragment.ProductFragment;
import com.tram.saletech.R;

public class ProductActivity extends AppCompatActivity {
    WebView mWcontent;
    TextView mTxtName;
    TextView mTxtPrice;
    TextView mTxtSale;
    Button mBtnAddCart;
    GetCart mGetCart;
    ImageView mImg;
    @SuppressLint({"ClickableViewAccessibility", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
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
                        Toast.makeText(ProductActivity.this,
                                "Đã thêm vào giỏ hàng",
                                Toast.LENGTH_SHORT).show();
                }
            });

        }

    }


}