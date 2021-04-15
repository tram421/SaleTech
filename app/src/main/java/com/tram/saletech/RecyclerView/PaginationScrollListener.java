package com.tram.saletech.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.tram.saletech.API.Product;
import com.tram.saletech.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
    private GridLayoutManager gridLayoutManager;
    List<Product> list;
    int currentPage;

    public PaginationScrollListener(GridLayoutManager gridLayoutManager, List<Product> list, int currentPage) {
        this.gridLayoutManager = gridLayoutManager;
        this.list = list;
        this.currentPage = currentPage;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);


//        int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
        int visibleItemCount = recyclerView.getAdapter().getItemCount();
//        int visibleItemCount = listVisible.size();
        int totalItemCount = list.size();
        int firstVisible = ((GridLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        if(isLoading() || isLastPage()){
            Log.d("BBB","Không tải trang");

            return;
        }
        //điều kiện load thêm sản phẩm: vị trí nhìn thấy không phải trên cùng and số item nhìn thấy nhỏ hơn tổng số
        // and số item đã tải trừ cho vị trí item đang xem có khoảng cách là dưới 5 item
        if ((firstVisible >= 0) && visibleItemCount < totalItemCount && (visibleItemCount - firstVisible) <= 5) {
            loadMoreItem();
        }

    }

    public abstract void loadMoreItem();
    public abstract boolean isLoading();
    public abstract boolean isLastPage();


}
