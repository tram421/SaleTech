package com.tram.saletech.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tram.saletech.API.Order;
import com.tram.saletech.API.Product;
import com.tram.saletech.R;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    List<Order> mArr;

    public OrderAdapter(List<Order> mArr) {
        this.mArr = mArr;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View  view = layoutInflater.inflate(R.layout.layout_order_item,parent,false);
        return new OrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = mArr.get(position);


        holder.mTvidOrder.setText("Mã đơn hàng: MKL000"  + String.valueOf(order.getId()));
        String description = order.getDescription().replaceAll(",", "\n");
        holder.mTVdescriptionOrder.setText(description);
        String status = "";
        switch (order.getStatus()) {
            case 0:
                status = "Chưa xác nhận";
                break;
            case 1:
                status = "Đã xác nhận";
                break;
            case 2:
                status = "Đang giao hàng";
                break;
            default:
                status = "Chưa xác nhận";
                break;
        }
        holder.mTvStatus.setText(status);
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
        TextView mTvidOrder;
        TextView mTVdescriptionOrder;
        TextView mTvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvidOrder = itemView.findViewById(R.id.idOrder);
            mTVdescriptionOrder = itemView.findViewById(R.id.descriptionOrder);
            mTvStatus = itemView.findViewById(R.id.statusOrder);
        }

    }
}
