package com.tram.saletech.RecyclerView;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tram.saletech.API.GetCart;
import com.tram.saletech.API.LoadImage;
import com.tram.saletech.API.Product;
import com.tram.saletech.Interface.OnListenerItem;
import com.tram.saletech.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    List<Product> mArr;
    OnListenerItem mOnListenerItem;
    GetCart mGetCart = GetCart.getInstance();
    public CartAdapter(List<Product> mArr) {
        this.mArr = mArr;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View  view = layoutInflater.inflate(R.layout.product_item_in_cart,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = mArr.get(position);
        final String imgURL  = "http://maitram.net/" + product.getImage();
        new LoadImage(holder.mTvimageProduct).execute(imgURL);
        holder.mTvNameProduct.setText(product.getName());
        int sale = Integer.parseInt(product.getSale());
        holder.mTvsale.setText(String.format("%,d",sale) + "đ");
        int quantity = Integer.parseInt(String.valueOf(holder.mTvEdtquantity.getEditableText()));
        holder.mTvprice.setText(String.valueOf(String.format("%,d",sale * quantity) + "đ"));
        holder.mTvEdtquantity.setText(mGetCart.listAllCart.get(position)[1]);
    }

    @Override
    public int getItemCount() {
        if(mArr == null){
            return 0;
        } else {
            return mArr.size();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mTvimageProduct;
        TextView mTvNameProduct;
        TextView mTvsale;
        EditText mTvEdtquantity;
        TextView mTvprice;
        Button mTvBtndel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvimageProduct = itemView.findViewById(R.id.imageProduct);
            mTvNameProduct = itemView.findViewById(R.id.nameProduct);
            mTvsale = itemView.findViewById(R.id.sale);
            mTvEdtquantity = itemView.findViewById(R.id.quantity);
            mTvprice = itemView.findViewById(R.id.price);
            mTvBtndel = itemView.findViewById(R.id.delBtn);


            mTvBtndel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnListenerItem != null) {
                        mOnListenerItem.onClick(getAdapterPosition());
                    }
                }
            });


        }

    }
    public void setOnItemClickListener(OnListenerItem mOnClickListenerItem){
        this.mOnListenerItem = mOnClickListenerItem;

    }
}
