package com.tram.saletech.API;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import java.io.InputStream;

//Lấy ảnh từ internet về
public class LoadImage extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    ImageSwitcher bmImageSwitcher;

    public LoadImage(ImageView bmImage) {
        this.bmImage = bmImage;
    }
    public LoadImage(ImageSwitcher bmImage) {
        this.bmImageSwitcher = bmImage;
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
