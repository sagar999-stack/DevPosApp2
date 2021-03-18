package com.example.devposapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {
EditText email , password;
Button submit;
    public RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email= (EditText) findViewById(R.id.editTextTextEmailAddress);
        password= (EditText) findViewById(R.id.editTextTextPassword);
        submit= (Button) findViewById(R.id.loginButton);
        RequestQueue queue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("loginInfo",LoginActivity.this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(sharedPreferences.contains("status")){
            sendToMain(null);
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString();
                String passwordStr = password.getText().toString();
                JSONObject js = new JSONObject();
                try {
                    js.put("email",emailStr);
                    js.put("password",passwordStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String url = "https://devoretapi.co.uk/api/v1/systemUserLogin";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                        Request.Method.POST,url, js,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                int count =0;
                                try {
                                    String status = response.getString("status");
                                    String firstName = response.getJSONObject("data").getString("first_name");
                                    String lastName = response.getJSONObject("data").getString("last_name");
                                    String email = response.getJSONObject("data").getString("email");
                                    String mobileNum = response.getJSONObject("data").getString("mobile_no");
                                    String resId = response.getJSONObject("data").getString("restaurant_id");
                                    String userRole= response.getJSONObject("data").getString("user_role");
                                    String token = response.getString("token");



                                    editor.putString("status",status);
                                    editor.putString("firstName",firstName);
                                    editor.putString("lastName",lastName);
                                    editor.putString("email",email);
                                    editor.putString("mobileNum",mobileNum);
                                    editor.putString("resId",resId);
                                    editor.putString("userRole",userRole);
                                    editor.putString("token",token);
                                    editor.apply();

                                    LoginStatus loginStatus = new LoginStatus();
                                    loginStatus.setLogIn();



                                    if(sharedPreferences.contains("status")){
                                        sendToMain(null);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,  error.toString(),Toast.LENGTH_LONG).show();

                    }
                });
                queue.add(jsonObjReq);
            }
        });





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