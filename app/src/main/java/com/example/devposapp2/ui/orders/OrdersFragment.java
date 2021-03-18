package com.example.devposapp2.ui.orders;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.devposapp2.Connection;
import com.example.devposapp2.LoginStatus;
import com.example.devposapp2.R;
import com.example.devposapp2.Socketmanager;
import com.example.devposapp2.SpaceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.SpannableString;
import android.widget.Toast;

public class OrdersFragment extends Fragment {
    public OrdersFragment() {
        // Required empty public constructor
    }

    OrdersAdapter myadapter;
    private RequestQueue mQueue;
    List<OrdersViewModel> orders = new ArrayList<>();
    RecyclerView recyclerView;
    public Socketmanager mSockManager;
    int port=9100;
    String printerIp;
    String resId;
    TextToSpeech t1;
    private Button buttonPf=null;
    private ProgressBar spinner;
    SwipeRefreshLayout swipeRefreshLayout;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_orders, container, false);
        spinner = (ProgressBar)root.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        recyclerView = root.findViewById(R.id.recycleView);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSockManager=new Socketmanager(getContext());
        SharedPreferences connectionFields = getContext().getSharedPreferences("connectionFields",getContext().MODE_PRIVATE);
        printerIp= connectionFields.getString("ipAddress","data not found");
        String  portStr= connectionFields.getString("port","data not found");
        port = Integer.parseInt(portStr);
        SharedPreferences loginInfo = getContext().getSharedPreferences("loginInfo", getContext().MODE_PRIVATE);
        resId = loginInfo.getString("resId", "data not found");
        t1=new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        allSettingsAndLoadDataAndPassToAdapter();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allSettingsAndLoadDataAndPassToAdapter();
                myadapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

//            Thread thread = new Thread(){
//                @Override
//                public  void  run(){
//                    while (!interrupted()){
//                        try {
//                            jsonParseListOfPrintedOrders();
//
//                            Thread.sleep(5000);
//
//
//
//
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            };
//
//
//            thread.start();
        return root;
    }

public void allSettingsAndLoadDataAndPassToAdapter(){
    spinner.setVisibility(View.VISIBLE);
    orders = new ArrayList<>();
    Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
// Set up the network to use HttpURLConnection as the HTTP client.
    Network network = new BasicNetwork(new HurlStack());
// Instantiate the RequestQueue with the cache and network.
    mQueue = new RequestQueue(cache, network);
    mQueue.start();
    jsonParseListOfPrintedOrders();
    myadapter = new OrdersAdapter(getContext(),orders) ;
    recyclerView.setAdapter(myadapter);
}
    public void data(
            String firstName,
            String phoneNumber,
            String customerAddress,
            String orderDate,
            String orderTime,
            String deliveryTime,
            JSONArray orderedItems,
            String subTotal,
            String discount,
            String serviceCharge,
            String deliveryCharge,
            String grandTotal,
            String paymentMethod,
            String orderPolicy,
            String resName,
            boolean printed)  {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        OrdersViewModel ordersViewModel = new OrdersViewModel();
        ordersViewModel.setResName(resName);
        ordersViewModel.setFirstName(firstName);
        ordersViewModel.setCustomerPhoneNum(phoneNumber);
        ordersViewModel.setCustomerAddress(customerAddress);
        ordersViewModel.setOrderDate(orderDate);
        ordersViewModel.setSubTotal(subTotal);
        ordersViewModel.setOrderTime(orderTime);
        ordersViewModel.setDeliveryTime(deliveryTime);
        ordersViewModel.setDiscount(discount);
        ordersViewModel.setServiceCharge(serviceCharge);
        ordersViewModel.setDeliveryCharge(deliveryCharge);
        ordersViewModel.setGrandTotal(grandTotal);
        ordersViewModel.setPaymentMethod(paymentMethod);
        ordersViewModel.setOrderPolicy(orderPolicy);
        ordersViewModel.setOrderedItems(orderedItems);
        ordersViewModel.setPrinted(printed);
        orders.add(ordersViewModel);


    }


    public void jsonParseListOfPrintedOrders() {
        String url = "https://devoretapi.co.uk/api/v1/admin/orders/"+resId+"/today";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        spinner.setVisibility(View.GONE);
                         int arrayLength=0;
                        int count = 0;
                        if(arrayLength<=response.length()){
                            while (count<response.length()){
                                if(count>=arrayLength){
                                    arrayLength++;
                                    try {
                                        JSONObject obj = response.getJSONObject(count);

                                        JSONObject customerInfo = obj.getJSONObject("customer_info");
                                        String printingStatus = obj.getString("printing_status");
                                        int printingStatusInt = Integer.parseInt(printingStatus);
                                        String _id = obj.getString("_id");
                                        String firstName = customerInfo.getString("first_name");
                                        String phoneNumber = customerInfo.getString("mobile_no");
                                        String address1 = customerInfo.getString("address1");
                                        String address2 = customerInfo.getString("address2");
                                        String postCode = customerInfo.getString("postcode");
                                        String city = customerInfo.getString("city");
                                        String customerAddress = address1+","+address2+","+postCode+","+city;
                                        String orderDate = obj.getString("order_date");
                                        String orderTime = obj.getString("order_time");
                                        String deliveryTime = obj.getString("delivery_time");
                                        JSONArray orderedItems = obj.getJSONArray("order_items");
                                        String subTotal = obj.getString("sub_total");
                                        String discount = obj.getString("discount_amount");
                                        String serviceCharge = obj.getString("service_charge");
                                        String deliveryCharge = obj.getString("delivery_charge");
                                        String grandTotal = obj.getString("grand_total");
                                        String order_policy = obj.getString("order_policy");
                                        String paymentMethod = obj.getString("payment_method");
                                        String resName = obj.getString("restaurant_name");
                                        boolean printed = false;

                                        data(firstName,phoneNumber,customerAddress,orderDate,orderTime,deliveryTime,orderedItems,subTotal,discount,serviceCharge,deliveryCharge,grandTotal,paymentMethod,order_policy,resName,printed);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                count++;

                            }
                        }
                        else{

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}