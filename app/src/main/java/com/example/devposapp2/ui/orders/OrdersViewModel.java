package com.example.devposapp2.ui.orders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OrdersViewModel  {
  public String firstName;
  public  String orderDate;
    public String grandTotal;


    public OrdersViewModel() {

    }
    public OrdersViewModel(String firstName, String orderDate) {
        this.firstName = firstName;
        this.orderDate = orderDate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getOrderDate() {
        return orderDate;
    }
    public String getGrandTotal() {
        return grandTotal;
    }
}