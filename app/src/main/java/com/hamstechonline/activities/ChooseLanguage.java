package com.hamstechonline.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.appevents.AppEventsLogger;
import com.hamstechonline.R;
import com.hamstechonline.datamodel.CategoryDatamodel;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.AppsFlyerEventParameter;
import com.hamstechonline.utils.GridSpacingItemDecoration;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;
import com.moe.pushlibrary.MoEHelper;
import com.moengage.core.DataCenter;
import com.moengage.core.LogLevel;
import com.moengage.core.MoEngage;
import com.moengage.core.Properties;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.NotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

public class ChooseLanguage extends AppCompatActivity {

    RecyclerView listLanguages;
    Button btnNext;
    LanguageSelectionAdapter languageSelectionAdapter;
    ArrayList<CategoryDatamodel> languagesList;
    HocLoadingDialog hocLoadingDialog;
    String langPref = "Language",languageSelected;
    SharedPreferences prefs;
    private Locale myLocale;
    LogEventsActivity logEventsActivity;
    AppEventsLogger logger;
    Properties properties;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.language_list_activity);

        listLanguages = findViewById(R.id.listLanguages);
        btnNext = findViewById(R.id.btnNext);

        languagesList = new ArrayList<>();
        hocLoadingDialog = new HocLoadingDialog(this);
        logger = AppEventsLogger.newLogger(this);
        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");

        logEventsActivity = new LogEventsActivity();

        MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);

        getData(this);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logLanguageEvent(languageSelected);
                getLogEvent(ChooseLanguage.this);
                properties = new Properties();
                properties.addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref);
                MoEHelper.getInstance(ChooseLanguage.this).trackEvent(AppsFlyerEventParameter.EVENT_LANGUAGE,properties);
                if (getIntent().getStringExtra("pageName")!= null){
                    changeLang(langPref);
                    finish();
                    Intent intent = new Intent(ChooseLanguage.this, RegistrationActivity.class);
                    startActivity(intent);
                } else {
                    changeLang(langPref);
                    finish();
                    Intent intent = new Intent(ChooseLanguage.this, HomePageActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void logLanguageEvent (String selectedLanguage) {
        Bundle params = new Bundle();
        params.putString("selectedLanguage", selectedLanguage);
        logger.logEvent("language", params);
    }

    public void getData(Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        try {
            params.put("appname","Hamstech");
            params.put("page","language");
            params.put("apikey",getResources().getString(R.string.lblApiKey));
            metaData.put("metadata", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hocLoadingDialog.showLoadingDialog();

        final String mRequestBody = metaData.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.get_lang_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    languagesList.clear();

                    if (jo.getString("status").equals("ok")){
                        if (jo.isNull("Get_all_languages")){

                        } else {
                            JSONArray jsonArray = jo.getJSONArray("Get_all_languages");
                            for (int i = 0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                CategoryDatamodel datamodel = new CategoryDatamodel();
                                datamodel.setLangName(jsonObject.getString("lang_name"));
                                datamodel.setLangChar(jsonObject.getString("lang_character"));
                                datamodel.setLangStatus(jsonObject.getString("lang_status"));
                                datamodel.setLangValue(jsonObject.getString("data_lang"));

                                if (jsonObject.getString("lang_status").equals("1")) languagesList.add(datamodel);
                            }

                            languageSelectionAdapter = new LanguageSelectionAdapter(ChooseLanguage.this,languagesList);
                            listLanguages.setLayoutManager(new GridLayoutManager(ChooseLanguage.this, 1));
                            listLanguages.addItemDecoration(new GridSpacingItemDecoration(1,0,false));
                            listLanguages.setAdapter(languageSelectionAdapter);
                        }

                    } else {
                        Toast.makeText(ChooseLanguage.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                    }
                    hocLoadingDialog.hideDialog();

                } catch(Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

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
                    Toast.makeText(ChooseLanguage.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }

        };
        queue.add(sr);
    }

    public class LanguageSelectionAdapter extends RecyclerView.Adapter<LanguageSelectionAdapter.ViewHolder> {

        Context context;
        ArrayList<CategoryDatamodel> datamodels = new ArrayList<>();
        int mSelectedVariation;

        public LanguageSelectionAdapter(Context context,ArrayList<CategoryDatamodel> datamodels){
            this.context=context;
            this.datamodels = datamodels;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.language_list_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            try {
                holder.txtLanguageName.setText(datamodels.get(position).getLangName());

                if (datamodels.get(position).getLangValue().equals(langPref)){
                    mSelectedVariation = position;
                    holder.relativeMain.setBackground(getResources().getDrawable(R.drawable.language_shape_orange));
                } else {
                    holder.relativeMain.setBackground(getResources().getDrawable(R.drawable.language_shape_gray));
                }

                holder.relativeMain.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mSelectedVariation = position;
                        langPref = datamodels.get(position).getLangValue();
                        languageSelected = datamodels.get(position).getLangName();
                        if(position == mSelectedVariation){
                            holder.relativeMain.setBackground(getResources().getDrawable(R.drawable.language_shape_orange));
                        }
                        else {
                            holder.relativeMain.setBackground(getResources().getDrawable(R.drawable.language_shape_gray));
                        }
                        notifyDataSetChanged();
                    }
                });

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return datamodels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtLanguageName;
            RadioButton btnRadio;
            LinearLayout relativeMain;
            public ViewHolder(@NonNull View view) {
                super(view);
                txtLanguageName = view.findViewById(R.id.txtLanguageName);
                relativeMain = view.findViewById(R.id.relativeMain);
            }
        }
    }

    public void saveLocale(String lang){
        SharedPreferences prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Language", lang);
        editor.commit();
    }

    public void changeLang(String lang){
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    public void getLogEvent(Context context){
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Hamstech");
            data.put("mobile", UserDataConstants.userMobile);
            data.put("fullname",UserDataConstants.userName);
            data.put("email",UserDataConstants.userMail);
            data.put("category","");
            data.put("course","");
            data.put("lesson","Choose Language");
            data.put("activity","Selected Language");
            data.put("pagename",langPref);
            boolean logevent = logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
