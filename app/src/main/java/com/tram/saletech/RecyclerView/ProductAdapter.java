package com.tram.saletech.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tram.saletech.API.LoadImage;
import com.tram.saletech.API.Product;
import com.tram.saletech.Fragment.ProductFragment;
import com.tram.saletech.Interface.OnListenerItem;
import com.tram.saletech.Interface.OnListenerToAddCart;
import com.tram.saletech.R;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    List<Product> mListProduct;
    private Boolean isLoadingAdd = true;
    OnListenerToAddCart mOnListenerToAddCart;
    @Override
    public int getItemViewType(int position) {
        if (mListProduct != null && position == (mListProduct.size()-1) && isLoadingAdd == true) {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    public void setData(List<Product> list){
        this.mListProduct = list;
        notifyDataSetChanged();
    }



    public ProductAdapter(List<Product> mArrProduct) {
        this.mListProduct = mArrProduct;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(TYPE_ITEM == viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View  view = layoutInflater.inflate(R.layout.product_item,parent,false);
            return new ProductViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_item_loading, parent, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == TYPE_ITEM){
            Product product = mListProduct.get(position);
            ProductViewHolder productViewHolder = (ProductViewHolder) holder;
            if (product.getName() !=null) {
                int price = Integer.parseInt(product.getPrice());
                int sale = Integer.parseInt(product.getSale());
                float  percent = 100 - ((float)((float)sale/(float)price)*100);
                final String imgURL  = "http://maitram.net/" + product.getImage();
                new LoadImage(productViewHolder.mTvimageProduct).execute(imgURL);
                productViewHolder.mTvNameProduct.setText(product.getName());
                productViewHolder.mTvsale.setText(String.format("%,d",sale) + "đ");
                productViewHolder.mTvprice.setText(Html.fromHtml(
                        "<del>"
                                + String.format("%,d", price) + "đ"
                                + "</del>" ));
                if(percent > 0){
                    productViewHolder.mTvpercentCostSale.setText( "-" + Math.round(percent) + "%" );
                } else {
                    productViewHolder.mTvpercentCostSale.setText("");
                }
            }


        }

    }


    @Override
    public int getItemCount() {
        if(mListProduct == null){
            return 0;
        } else {
            return mListProduct.size();
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        ImageView mTvimageProduct;
        TextView mTvNameProduct, mTvsale, mTvprice, mTvpercentCostSale;
        Button mTvBtnAddToCart;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvimageProduct = itemView.findViewById(R.id.imageProduct);
            mTvNameProduct = itemView.findViewById(R.id.nameProduct);
            mTvsale = itemView.findViewById(R.id.sale);
            mTvprice = itemView.findViewById(R.id.price);
            mTvpercentCostSale = itemView.findViewById(R.id.percentCostSale);
            mTvBtnAddToCart = itemView.findViewById(R.id.btnAddtoCart);

            mTvBtnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnListenerToAddCart != null) {
                        mOnListenerToAddCart.onClick(getAdapterPosition());
                    } else {
                        Log.d("BBB","Trong Product Adapter : Chưa gọi được hàm click");
                    }
                }
            });

        }
    }
    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }

    public void addFooterLoading(){
        isLoadingAdd = true;
        mListProduct.add(new Product());
    }
    public void removeFooterLoading(){

        isLoadingAdd = false;
        int position = mListProduct.size() - 1;
//        Log.d("BBB","xóa 1 phần tử: " + mListProduct.get(position).getId());
        try{
            if(mListProduct.get(position).getId() == null){
                Product product = mListProduct.get(position);
                mListProduct.remove(position);
                notifyItemRemoved(position);
            }
        }catch (Exception e){
            Log.d("BBB",e.getMessage() + " |-------|  trong: ProductAdapter: removeFooterLoading(): Có thể not internet ko get đc API");
        }


    }

    public void setOnItemAddToCart(OnListenerToAddCart onListenerToAddCart){
        this.mOnListenerToAddCart = onListenerToAddCart;

    }
    public void removeFooterLoadingWithParam(List<Product> list, int position){
//            Product product = mListProduct.get(position);
            list.remove(position);
            notifyItemRemoved(position);
    }
}


