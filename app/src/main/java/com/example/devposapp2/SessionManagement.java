package com.example.devposapp2;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";
 public String status;
    public SessionManagement(Context context, String status){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
          editor.putString("status",status);
        editor = sharedPreferences.edit();


    }



    public String getSession(){
        //return user id whose session is saved
        return sharedPreferences.getString(SESSION_KEY, "fff");
    }

    public void removeSession(){
        editor.putString(SESSION_KEY,"fff").commit();
    }
}
