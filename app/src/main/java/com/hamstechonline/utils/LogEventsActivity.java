package com.hamstechonline.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamstechonline.R;
import com.hamstechonline.database.UserDataBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LogEventsActivity {

    Context context;
    JSONObject jsonObject;
    String langPref = "Language",mobile,fullname,email = "";
    SharedPreferences prefs;
    UserDataBase userDataBase;
    int version;

    @SuppressLint("NotConstructor")
    public boolean LogEventsActivity(Context context, JSONObject jsonObject){
        this.context = context;
        this.jsonObject = jsonObject;
        prefs = context.getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");
        userDataBase = new UserDataBase(context);
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (userDataBase.getCount() != 0) {
            try {
                mobile = userDataBase.getUserMobileNumber(1);
                fullname = userDataBase.getUserName(1);
                if (UserDataConstants.userMail != null) email = "";
            } catch (NullPointerException ex){
                ex.printStackTrace();
            }
        } else {
            try {
                mobile = jsonObject.getString("mobile");
                fullname = jsonObject.getString("fullname");
                email = jsonObject.getString("email");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            params.put("apikey",context.getResources().getString(R.string.lblApiKey));
            params.put("appname","Hamstech");
            data.put("mobile",mobile);
            data.put("fullname",fullname);
            data.put("email",email);
            data.put("lang",langPref);
            data.put("category",jsonObject.getString("category"));
            data.put("course",jsonObject.getString("course"));
            data.put("lesson",jsonObject.getString("lesson"));
            data.put("activity",jsonObject.getString("activity"));
            data.put("pagename",jsonObject.getString("pagename"));
            data.put("version",version);
            metaData.put("metadata", params);
            metaData.put("data", data);
        } catch (JSONException e) {
            Log.e("log","77   "+e.toString());
            e.printStackTrace();
        }
        final String mRequestBody = metaData.toString();
        Log.e("log","80   "+mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConstants.getLogevent,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("log","86   "+response.toString());
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return mRequestBody.getBytes();
                }
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);

        return true;
    }
}
