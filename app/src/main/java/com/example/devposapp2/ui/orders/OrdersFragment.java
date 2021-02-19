package com.example.devposapp2.ui.orders;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.devposapp2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {
    public OrdersFragment() {
        // Required empty public constructor
    }

    OrdersAdapter myadapter;
    private RequestQueue mQueue;
    List<OrdersViewModel> orders = new ArrayList<>();
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        recyclerView = root.findViewById(R.id.recycleView);





        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());


// Instantiate the RequestQueue with the cache and network.
        mQueue = new RequestQueue(cache, network);



// Start the queue
        mQueue.start();

        String url = "https://devoretapi.co.uk/epos/getLastOrders/5c6974efc8bd250a10b572a0";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int count = 0;
                        while (count<response.length()){
                            try {
                                JSONObject obj = response.getJSONObject(count);



                                String  grandTotal = obj.getString("grand_total");

                                String orderDate = obj.getString("order_date");

//                                OrdersViewModel ordersViewModel = new OrdersViewModel();
//                                ordersViewModel.setFirstName(name.toString());
//                                ordersViewModel.setOrderDate(date.toString());
//                                orders.add(ordersViewModel);



//                                SpannableString ss = new SpannableString(ResName);
//                                ss.getSpanFlags(boldSpan);
//                                String orderDate = obj.getString("order_date").toString();
//                                String orderItems = obj.getString("order_items");
//                                JSONArray orderedItems = obj.getJSONArray("order_items");
//
//                                StringBuilder allDishes = new StringBuilder();
//                                for (int i = 0; i < orderedItems.length(); i++) {
//                                    JSONObject order_item = orderedItems.getJSONObject(i);
//                                    String dishName = order_item.getString("dish_name").toString();
//                                    allDishes.append(dishName + "\n");
//
//
//                                }
//
                                JSONObject customerInfo = obj.getJSONObject("customer_info");
                                String firstName = customerInfo.getString("first_name");

                                data(firstName,orderDate,grandTotal);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            count++;

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
        /////////////////
        setuprecyclerview(orders);

        return root;
    }
  public void data(String firstName, String orderDate,String grandTotal)  {

      recyclerView.setHasFixedSize(true);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        OrdersViewModel ordersViewModel = new OrdersViewModel();
        ordersViewModel.setFirstName(firstName);
        ordersViewModel.setOrderDate(orderDate);
        ordersViewModel.setGrandTotal(grandTotal);
        orders.add(ordersViewModel);


    }
//
//    public void jsonParse() {
//
//
//        String url = "https://devoretapi.co.uk/epos/getLastOrders/5c6974efc8bd250a10b572a0";
//        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,null,
//                new com.android.volley.Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        int count = 0;
//                        while (count<response.length()){
//
//                            try {
//                                JSONObject obj = response.getJSONObject(count);
//
//                                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
//
//                                String  ResName = obj.getString("restaurant_name").toString();
//
//                                SpannableString ss = new SpannableString(ResName);
//                                ss.getSpanFlags(boldSpan);
//                                String orderDate = obj.getString("order_date").toString();
//                                String orderItems = obj.getString("order_items");
//                                JSONArray orderedItems = obj.getJSONArray("order_items");
//
//                                StringBuilder allDishes = new StringBuilder();
//                                for (int i = 0; i < orderedItems.length(); i++) {
//                                    JSONObject order_item = orderedItems.getJSONObject(i);
//                                    String dishName = order_item.getString("dish_name").toString();
//                                    allDishes.append(dishName + "\n");
//
//
//                                }
//
//                                JSONArray customerInfo = obj.getJSONArray("customer_info");
//
//                                for (int i = 0; i < customerInfo.length(); i++) {
//                                    JSONObject customer_info = customerInfo.getJSONObject(i);
//                                  String   first_name = customer_info.getString("first_name").toString();
//
//
//                                }
//
//
//
//
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            count++;
//
//                        }
//
//
//
//
//
//                    }
//
//
//
//
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//
//        mQueue.add(request);
//
//    }
    private void setuprecyclerview(List<OrdersViewModel> orders) {



        myadapter = new OrdersAdapter(getContext(),orders) ;

        recyclerView.setAdapter(myadapter);


    }

}