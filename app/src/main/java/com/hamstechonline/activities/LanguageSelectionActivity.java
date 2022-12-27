package com.hamstechonline.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hamstechonline.R;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.CategoryDatamodel;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.GridSpacingItemDecoration;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.LogEventsActivity;
import com.moengage.core.DataCenter;
import com.moengage.core.LogLevel;
import com.moengage.core.MoEngage;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.NotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

public class LanguageSelectionActivity extends AppCompatActivity {

    RecyclerView listLanguages;
    Button btnNext;
    LanguageSelectionAdapter languageSelectionAdapter;
    ArrayList<CategoryDatamodel> languagesList;
    HocLoadingDialog hocLoadingDialog;
    String langPref = "Language",languageSelected = "";
    SharedPreferences prefs;
    int c;
    private Locale myLocale;
    UserDataBase dh;
    LogEventsActivity logEventsActivity;
    String gcm_id;
    AppEventsLogger logger;
    Button chbEnglish,chbHindi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.language_selection_activity);
        listLanguages = findViewById(R.id.listLanguages);
        btnNext = findViewById(R.id.btnNext);
        chbEnglish = findViewById(R.id.chbEnglish);
        chbHindi = findViewById(R.id.chbHindi);

        languagesList = new ArrayList<>();

        hocLoadingDialog = new HocLoadingDialog(this);
        logEventsActivity = new LogEventsActivity();
        logger = AppEventsLogger.newLogger(this);
        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");
        dh = new UserDataBase(this);

        MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);

        getData(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LanguageSelectionActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                gcm_id = newToken;
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logLanguageEvent(languageSelected);
                changeLang(langPref);
                if (getIntent().getStringExtra("pageName")!= null){
                    Intent intent = new Intent(LanguageSelectionActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                    LanguageSelectionActivity.this.finish();
                } else {
                    //finish();
                    Intent intent = new Intent(LanguageSelectionActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    LanguageSelectionActivity.this.finish();
                }
            }
        });

        chbEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chbEnglish.setBackground(getResources().getDrawable(R.drawable.english_check));
                chbEnglish.setTextColor(getResources().getColor(R.color.dark_pink));
                chbHindi.setTextColor(getResources().getColor(R.color.muted_blue));
                chbHindi.setBackground(getResources().getDrawable(R.drawable.hindi_txt_check));
                btnNext.setText(getResources().getString(R.string.lblNext));
                langPref = "en";
                languageSelected = "English";
            }
        });
        chbHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chbEnglish.setBackground(getResources().getDrawable(R.drawable.english_uncheck));
                chbEnglish.setTextColor(getResources().getColor(R.color.muted_blue));
                chbHindi.setTextColor(getResources().getColor(R.color.dark_pink));
                chbHindi.setBackground(getResources().getDrawable(R.drawable.hindi_txt_uncheck));
                btnNext.setText("आगे बढ़ें");
                langPref = "hi";
                languageSelected = "Hindi";
            }
        });

    }
    public void logLanguageEvent (String selectedLanguage) {
        Bundle params = new Bundle();
        params.putString("selectedLanguage", selectedLanguage);
        logger.logEvent("language", params);
    }
    @Override
    protected void onResume() {
        super.onResume();
        c = dh.getCount();
        /*if (c!=0){
            startActivity(new Intent(LanguageSelectionActivity.this,HomePageActivity.class));
        }*/
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
            View v = inflater.inflate(R.layout.language_selection_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            try {

                holder.txtLanguageName.setText(datamodels.get(position).getLangName());
                holder.txtChar.setText(datamodels.get(position).getLangChar());

                if (datamodels.get(position).getLangValue().equals(langPref)){
                    mSelectedVariation = position;
                    holder.relativeMain.setBackground(getResources().getDrawable(R.drawable.button_shape_orange));
                } else {
                    holder.relativeMain.setBackground(getResources().getDrawable(R.drawable.button_shape_gray));
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
                            holder.relativeMain.setBackground(getResources().getDrawable(R.drawable.button_shape_orange));
                        }
                        else {
                            holder.relativeMain.setBackground(getResources().getDrawable(R.drawable.button_shape_gray));
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
            TextView txtLanguageName,txtChar;
            RelativeLayout relativeMain;
            public ViewHolder(@NonNull View view) {
                super(view);
                txtLanguageName = view.findViewById(R.id.txtLanguageName);
                txtChar = view.findViewById(R.id.txtChar);
                relativeMain = view.findViewById(R.id.relativeMain);
            }
        }
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

                            languageSelectionAdapter = new LanguageSelectionAdapter(LanguageSelectionActivity.this,languagesList);
                            listLanguages.setLayoutManager(new GridLayoutManager(LanguageSelectionActivity.this, 2));
                            listLanguages.addItemDecoration(new GridSpacingItemDecoration(2,10,false));
                            listLanguages.setAdapter(languageSelectionAdapter);
                        }

                    } else {
                        Toast.makeText(LanguageSelectionActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                    }
                    hocLoadingDialog.hideDialog();

                } catch(JSONException e) {
                    finish();
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
                    Toast.makeText(LanguageSelectionActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        queue.add(sr);
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences(langPref, Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
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
            data.put("appname","Dashboard");
            data.put("mobile", "");
            data.put("fullname","");
            data.put("email","");
            data.put("category","");
            data.put("course","");
            data.put("lesson",gcm_id);
            data.put("activity","Before Registration/Login");
            data.put("pagename",langPref);
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
