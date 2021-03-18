package com.example.devposapp2.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.devposapp2.Connection;
import com.example.devposapp2.R;
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
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
import java.awt.font.TextAttribute;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.collection.ArraySet;

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
import com.android.volley.toolbox.Volley;
import com.example.devposapp2.Socketmanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class SettingsFragment extends Fragment {

    private SettingsViewModel notificationsViewModel;
    private WebView mWebView;
    private Button buttonCon=null;
    private Button buttonDisCon=null;
    private Button buttonPf=null;
    private Button buttonCash=null;
    private Button buttonCut=null;
    private EditText mTextIp=null;
    private EditText mTextPort=null;
    private EditText mprintfData=null;
    private EditText mprintfLog=null;
    private Socketmanager mSockManager;
    private String mydata = null;
Connection connection = new Connection();
    private Button click=null;
    private TextView data=null;
    private RequestQueue mQueue;
int port ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        buttonCon=root.findViewById(R.id.conTest);
        buttonDisCon=root.findViewById(R.id.disConTest);
//        buttonPf=root.findViewById(R.id.printf);
//        buttonCash=root.findViewById(R.id.buttonCash);
//        buttonCut=root.findViewById(R.id.buttonCut);
        mTextIp=root.findViewById(R.id.printerIp);
        mTextPort=root.findViewById(R.id.printerPort);
//        mprintfData=root.findViewById(R.id.printfData);
        mprintfLog=root.findViewById(R.id.printfLog);
        ButtonListener buttonListener=new ButtonListener();
        buttonCon.setOnClickListener(buttonListener);
        buttonDisCon.setOnClickListener(buttonListener);
//        buttonPf.setOnClickListener(buttonListener);
//        buttonCash.setOnClickListener(buttonListener);
//        buttonCut.setOnClickListener(buttonListener);
        mSockManager=new Socketmanager(getContext());

//        click = root. findViewById(R.id.buttonData);
//        data = root.findViewById(R.id.fetcheddata);

        return root;
    }


    class ButtonListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.conTest:

                     port = Integer.parseInt( mTextPort.getText().toString() );
                    if (connection.conTest(mTextIp.getText().toString(),port)) {
                        PrintfLog("connected...");
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("connectionFields",getContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("ipAddress",mTextIp.getText().toString());
                        editor.putString("port", mTextPort.getText().toString());
                        editor.commit();
                    }
                    else {
                        PrintfLog("Not connected...");
                    }
                    break;
                case R.id.disConTest:

                    if (mSockManager.close()) {
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("connectionFields",getContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("ipAddress","");

                        editor.commit();
                        PrintfLog("Disconnected...");

                    }else {
                        PrintfLog("connected...");
                    }

                    break;
         }

        }
    }


    public void PrintfLog(String logString) {
        mprintfLog.setText(logString);
    }




}