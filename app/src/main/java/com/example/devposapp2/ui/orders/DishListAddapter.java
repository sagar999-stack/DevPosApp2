package com.example.devposapp2.ui.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.devposapp2.R;

import java.util.ArrayList;
import java.util.List;

public class DishListAddapter extends ArrayAdapter<DishDetailsModel> {
    /**
     * Constructor. This constructor will result in the underlying data collection being
     * immutable, so methods such as {@link #clear()} will throw an exception.
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    private  Context context;
    private int mResource;
    public DishListAddapter(@NonNull Context context, int resource, @NonNull List<DishDetailsModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.mResource= resource;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView =inflater.inflate(mResource,parent,false);

        TextView dishName = convertView.findViewById(R.id.textViewx);
        TextView price = convertView.findViewById(R.id.priceView);
        dishName.setText(getItem(position).getDishName());
        price.setText(getItem(position).getPrice());

        return convertView;
    }
}
