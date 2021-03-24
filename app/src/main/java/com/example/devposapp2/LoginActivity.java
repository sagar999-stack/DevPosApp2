package com.example.devposapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
EditText email , password;
Button submit;
String resId;
    String emailStr;
    String passwordStr;
    public RequestQueue mQueue;
    private ProgressBar spinner;
    Connection connection = new Connection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email= (EditText) findViewById(R.id.editTextTextEmailAddress);
        password= (EditText) findViewById(R.id.editTextTextPassword);
        submit= (Button) findViewById(R.id.loginButton);
        emailStr=null;
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";       passwordStr=null;

        SharedPreferences loginInfo = LoginActivity.this.getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = loginInfo.edit();
        resId = loginInfo.getString("resId", "data not found");
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        RequestQueue queue = Volley.newRequestQueue(this);
        if(resId =="data not found"){


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            spinner.setVisibility(View.VISIBLE);
                            emailStr = email.getText().toString();
                            Matcher matcher= Pattern.compile(regex).matcher(emailStr);
                            if (matcher.matches()){

                                emailStr = email.getText().toString();
                                passwordStr = password.getText().toString();
                                if(connection.checkInternetConnection(LoginActivity.this))
                                {
                                JSONObject js = new JSONObject();
                                try {
                                    js.put("email", emailStr);
                                    js.put("password", passwordStr);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String url = "https://devoretapi.co.uk/api/v1/systemUserLogin";
                                JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                                        Request.Method.POST, url, js,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                int count = 0;
                                                try {
                                                    String status = response.getString("status");

                                                    if(response.getString("status").matches("success")){
                                                        String firstName = response.getJSONObject("data").getString("first_name");
                                                        String lastName = response.getJSONObject("data").getString("last_name");
                                                        String email = response.getJSONObject("data").getString("email");
                                                        String mobileNum = response.getJSONObject("data").getString("mobile_no");
                                                        String resId = response.getJSONObject("data").getString("restaurant_id");
                                                        String userRole = response.getJSONObject("data").getString("user_role");
                                                        String token = response.getString("token");

                                                        getResInfo(resId);
                                                        editor.putString("status", status);
                                                        editor.putString("firstName", firstName);
                                                        editor.putString("lastName", lastName);
                                                        editor.putString("email", email);
                                                        editor.putString("mobileNum", mobileNum);
                                                        editor.putString("resId", resId);
                                                        editor.putString("userRole", userRole);
                                                        editor.putString("token", token);
                                                        editor.apply();
                                                        Toast.makeText(getApplicationContext(),"Successfully logged in.",Toast.LENGTH_LONG).show();
                                                        sendToMain(null);
                                                        LoginStatus loginStatus = new LoginStatus();
                                                        loginStatus.setLogIn();
                                                    }
                                                    else{
                                                        Toast.makeText(getApplicationContext(),response.getString("status")+". "+response.getString("msg"),Toast.LENGTH_LONG).show();
                                                    }


//                                            if (loginInfo.contains("status")) {
//
//                                                Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();
////                                                sendToMain(null);
//                                            }
//                                            if(status=="error"){
//                                                Toast.makeText(getApplicationContext(),response.getString("msg"),Toast.LENGTH_LONG).show();
//                                            }
//                                            else{
//
//                                            }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                                    }
                                });
                                queue.add(jsonObjReq);



                            }
                            else{
                                    Toast.makeText(getApplicationContext(),"No Internet. PLease check your internet connection.",Toast.LENGTH_LONG).show();
                            }



                        }
                        else{
                                Toast.makeText(getApplicationContext(),"Invalid email address.",Toast.LENGTH_LONG).show();
                        }
                        }



                });



        }else{
            sendToMain(null);
        }
        if(loginInfo.contains("status")){
            sendToMain(null);
        }

    }

    private void getResInfo(String resId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        SharedPreferences resInfo = LoginActivity.this.getSharedPreferences("resInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = resInfo.edit();
        String url = "https://devoretapi.co.uk/api/v1/printer/getRestInfo/"+ resId;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int count = 0;
                        try {
                            String restaurantName = response.getString("restaurant_name");
                            String postcode = response.getString("postcode");
                            String streetAddress = response.getString("street_address");
                            String reservationTel = response.getString("reservation_tel");
                            String businessTel = response.getString("business_tel");
                            String email = response.getString("email");
                            String domain = response.getString("domain");
                            String localArea = response.getString("local_area");
                            String cityOrTown = response.getString("city_or_town");



                                editor.putString("restaurantName", restaurantName);
                                editor.putString("postcode", postcode);
                                editor.putString("streetAddress", streetAddress);
                                editor.putString("reservationTel", reservationTel);
                                editor.putString("businessTel", businessTel);
                                editor.putString("email", email);
                                editor.putString("domain", domain);
                                editor.putString("localArea", localArea);
                                editor.putString("cityOrTown", cityOrTown);

                                editor.apply();





//                                            if (loginInfo.contains("status")) {
//
//                                                Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();
////                                                sendToMain(null);
//                                            }
//                                            if(status=="error"){
//                                                Toast.makeText(getApplicationContext(),response.getString("msg"),Toast.LENGTH_LONG).show();
//                                            }
//                                            else{
//
//                                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        queue.add(jsonObjReq);
    }


    public void sendToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }
//    private View.OnClickListener mClickListener = new View.OnClickListener() {
//        public void onClick(View v) {
//        String emailStr = email.getText().toString();
//        String passwordStr = password.getText().toString();
//            JSONObject js = new JSONObject();
//            try {
//                js.put("email",emailStr);
//                js.put("password",passwordStr);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            String url = "https://devoretapi.co.uk/api/v1/systemUserLogin";
//            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
//                    Request.Method.POST,url, js,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            int count =0;
//                            Log.d("JSON", String.valueOf(response));
//
//                        }
//                    }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.d("JSON", String.valueOf(error));
//
//                }
//            });
//            mQueue.add(jsonObjReq);
//        }
//    };
}