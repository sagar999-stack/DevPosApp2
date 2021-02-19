package com.example.devposapp2.ui.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devposapp2.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private Context context ;

    List<OrdersViewModel> orders;

    public OrdersAdapter(Context mContext, List<OrdersViewModel> orders) {
        this.context = mContext;
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.firstName.setText(orders.get(position).getFirstName());
        holder.date.setText(orders.get(position).getOrderDate());
        holder.grandTotal.setText("£"+orders.get(position).getGrandTotal());
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder{
        TextView firstName,date,grandTotal;
        CardView view_container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.first_name);
            date = itemView.findViewById(R.id.date);
            grandTotal = itemView.findViewById(R.id.grand_total);
            view_container = itemView.findViewById(R.id.container);
        }
    }


}
