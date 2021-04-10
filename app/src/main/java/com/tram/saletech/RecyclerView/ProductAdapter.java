package com.tram.saletech.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tram.saletech.API.Product;
import com.tram.saletech.Fragment.ProductFragment;
import com.tram.saletech.R;

import java.io.InputStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    List<Product> mArrProduct;

    public ProductAdapter(List<Product> mArrProduct) {
        this.mArrProduct = mArrProduct;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View  view = layoutInflater.inflate(R.layout.product_item,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mArrProduct.get(position);
        final String imgURL  = "http://maitram.net/api/image/tv01.jpg";
//        final String imgURL  = product.getImage();
        new ProductAdapter.LoadImage(holder.mTvimageProduct).execute(imgURL);
        holder.mTvNameProduct.setText(product.getName());
        holder.mTvsale.setText(product.getSale());
        holder.mTvprice.setText(product.getPrice());

        holder.mTvpercentCostSale.setText("Giảm");
    }

    @Override
    public int getItemCount() {
        if(mArrProduct == null){
            return 0;
        } else {
            return mArrProduct.size();
        }
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{
        ImageView mTvimageProduct;
        TextView mTvNameProduct, mTvsale, mTvprice, mTvpercentCostSale;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvimageProduct = itemView.findViewById(R.id.imageProduct);
            mTvNameProduct = itemView.findViewById(R.id.nameProduct);
            mTvsale = itemView.findViewById(R.id.sale);
            mTvprice = itemView.findViewById(R.id.price);
            mTvpercentCostSale = itemView.findViewById(R.id.percentCostSale);

        }
    }
    //Lấy ảnh từ internet về
    public class LoadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlOfImage).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}


