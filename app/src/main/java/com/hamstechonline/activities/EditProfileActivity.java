package com.hamstechonline.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hamstechonline.R;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.editprofile.datamodel.MentorResponse;
import com.hamstechonline.editprofile.datamodel.ProfileData;
import com.hamstechonline.fragments.NavigationFragment;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;
import com.moengage.core.DataCenter;
import com.moengage.core.LogLevel;
import com.moengage.core.MoEngage;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.NotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class EditProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    Spinner spnWhyHamstech,spnAge,spnWork,spnLanguage;
    private static final int FILE_SELECT_CODE = 0;
    Bitmap mBitmap, resized;
    String type_image, picturePath, imgString,selectedCity="";
    CircleImageView profile_image;
    CheckBox isStudent;
    TextView txtUserMobile;
    AutoCompleteTextView txtSelectCity;
    EditText txtEmail,txtUserName;
    DrawerLayout drawer;
    BottomNavigationView navigation;
    NavigationFragment navigationFragment;
    NavigationView navSideMenu;
    Button btnSave;
    UserDataBase userDataBase;
    List<String> categories = new ArrayList<String>();
    List<String> occupationList = new ArrayList<String>();
    List<String> whyList = new ArrayList<String>();
    List<String> ageList = new ArrayList<String>();
    List<String> languageList = new ArrayList<String>();
    String imagePathData = "";
    int mMenuId;
    HocLoadingDialog hocLoadingDialog;
    LogEventsActivity logEventsActivity;
    String langPref = "Language",selectedLanguage,PagenameLog = "", is_student = "no";
    SharedPreferences prefs;
    private Locale myLocale;
    AppEventsLogger logger;
    Bundle params;
    ApiInterface apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.edit_profile);

        spnWork = findViewById(R.id.spnWork);
        spnWhyHamstech = findViewById(R.id.spnWhyHamstech);
        spnAge = findViewById(R.id.spnAge);
        spnLanguage = findViewById(R.id.spnLanguage);
        profile_image = findViewById(R.id.profile_image);
        drawer = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.navigation);
        navSideMenu = findViewById(R.id.navSideMenu);
        btnSave = findViewById(R.id.btnSave);
        txtUserMobile = findViewById(R.id.txtUserMobile);
        txtUserName = findViewById(R.id.txtUserName);
        txtEmail = findViewById(R.id.txtEmail);
        txtSelectCity = findViewById(R.id.txtSelectCity);
        isStudent = findViewById(R.id.isStudent);

        hocLoadingDialog = new HocLoadingDialog(this);
        logEventsActivity = new LogEventsActivity();
        logger = AppEventsLogger.newLogger(this);
        params = new Bundle();
        userDataBase = new UserDataBase(this);

        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");
        changeLang(langPref);

        if (langPref.equals("hi")){
            ageList.add("चुनिए");
            ageList.add("18-25");
            ageList.add("26-30");
            ageList.add("31-35");
            ageList.add("36-40");
        } else {
            ageList.add("Choose");
            ageList.add("18-25");
            ageList.add("26-30");
            ageList.add("31-35");
            ageList.add("36-40");
        }
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        //getMaterial(this);
        getCityNames();
        navigationFragment = NavigationFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.navSideMenu, navigationFragment, "")
                .commit();

        navigation.setOnNavigationItemSelectedListener(this);
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);
        txtSelectCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = categories.get(position);
            }
        });

        isStudent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) is_student = "yes";
                else is_student = "no";
            }
        });

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        spnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLanguage = spnLanguage.getSelectedItem().toString();
                if (spnLanguage.getSelectedItemPosition() == 0){
                    langPref = "en";
                } else if (spnLanguage.getSelectedItemPosition() == 1){
                    langPref = "hi";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ValidateInputs()) {
                    return;
                } else {
                    String email = txtEmail.getText().toString().trim();
                    String emailPattern = "[a-zA-Z0-9.]+@[a-z]+\\.+[a-z]+";
                    if (email.matches(emailPattern)){
                        logLanguageEvent(selectedLanguage);
                        changeLang(langPref);
                        uploadProfile(EditProfileActivity.this);
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void logLanguageEvent (String selectedLanguage) {
        params.putString("selectedLanguage", selectedLanguage);
        logger.logEvent("language", params);
    }
    @Override
    protected void onStart() {
        drawer.closeDrawers();
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        super.onStart();
    }
    private boolean ValidateInputs() {

        boolean result = IsValid(txtUserName, "Enter Name") &&
                IsValid(txtEmail, "Enter Email");
        return result;
    }

    private boolean IsValid(EditText txtText, String validationMessage) {
        if (txtText.getText().toString().trim().equals("")) {
            txtText.setError(validationMessage);
            return false;
        }
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mMenuId = item.getItemId();
        for (int i = 0; i < navigation.getMenu().size(); i++) {
            MenuItem menuItem = navigation.getMenu().getItem(i);
            boolean isChecked = menuItem.getItemId() == item.getItemId();
            menuItem.setChecked(isChecked);
        }

        switch (item.getItemId()) {
            case R.id.navigation_home:
                PagenameLog = "Home Page";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                getLogEvent(EditProfileActivity.this);
                Intent intentCourses = new Intent(EditProfileActivity.this, HomePageActivity.class);
                startActivity(intentCourses);
                return true;
            case R.id.navigation_chat:
                PagenameLog = "Chat support";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "chat with whatsapp");
                logger.logEvent(AppEventsConstants.EVENT_NAME_CONTACT,params);
                getLogEvent(EditProfileActivity.this);

                PackageManager packageManager = getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);

                try {
                    String url = "https://api.whatsapp.com/send?phone="+ "919010100240" +"&text=" +
                            URLEncoder.encode(getResources().getString(R.string.whatsAppmsg), "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.navigation_enrol:
                PagenameLog = "Success Story";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                getLogEvent(EditProfileActivity.this);
                Intent enrol = new Intent(EditProfileActivity.this, SuccessStoryActivity.class);
                startActivity(enrol);
                return true;
            case R.id.navigation_today:
                PagenameLog = "Hunar Club";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                getLogEvent(EditProfileActivity.this);
                Intent hamstech = new Intent(EditProfileActivity.this, BuzzActivity.class);
                startActivity(hamstech);
                return true;
            case R.id.navigation_aboutus:
                PagenameLog = "Contact Page";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                new AppsFlyerEventsHelper(this).EventContactus();
                getLogEvent(EditProfileActivity.this);
                Intent about = new Intent(EditProfileActivity.this, ContactActivity.class);
                startActivity(about);
                return true;
        }
        return false;
    }

    public void sideMenu(View view){
        drawer.openDrawer(Gravity.LEFT);
    }

    public void SelectImage(View view){
        if (isStoragePermissionGranted()) {
            showFileChooser();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
            builder.setMessage("You can't use this app without granting this permission")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();

                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        }
                    });
            builder.create();
        }
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 26) {
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }

    }
    private void showFileChooser() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(i, FILE_SELECT_CODE);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE) {

            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null)
                {
                    Uri selectedImage = data.getData();
                    try {
                        mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    resized = Bitmap.createScaledBitmap(mBitmap,(int)(mBitmap.getWidth()*0.3), (int)(mBitmap.getHeight()*0.3), true);
                    profile_image.setImageBitmap(resized);
                    if (resized != null) {
                        imagePathData = getEncoded64ImageStringFromBitmap(resized);
                        System.out.println(""+imagePathData);
                        uploadImage(EditProfileActivity.this);
                    }
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);

                    type_image = getContentResolver().getType(selectedImage);

                    if (type_image.equals(null)) type_image = "images/jpeg";

                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    try{
                        File file = new File(picturePath);
                        long length = file.length();
                        length = length/1024;
                    } catch(Exception e) {
                        System.out.println("File not found : " + e.getMessage() + e);
                    }

                    if (picturePath.isEmpty() || picturePath == null) {
                        Toast.makeText(this, "Unable to capture the image..Please try again", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(this,
                            "Image Loading Failed", Toast.LENGTH_LONG)
                            .show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

                Toast.makeText(this,
                        "User cancelled file upload", Toast.LENGTH_LONG)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(this,
                        "Sorry! Failed to load file", Toast.LENGTH_LONG)
                        .show();
            }
        }

    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    public void uploadProfile(Context context){

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        hocLoadingDialog.showLoadingDialog();

        if (is_student.equalsIgnoreCase("yes")) {
            new AppsFlyerEventsHelper(this).EventIsStudent();
        }

        try {
            params.put("phone",userDataBase.getUserMobileNumber(1));
            params.put("appname","Hamstech");
            params.put("page","profile");
            params.put("apikey",getResources().getString(R.string.lblApiKey));
            params.put("email",txtEmail.getText().toString().trim());
            params.put("prospectname",txtUserName.getText().toString().trim());
            params.put("city",txtSelectCity.getText().toString().trim());
            params.put("occupation",spnWork.getSelectedItem());
            params.put("ageband",spnAge.getSelectedItem());
            params.put("is_student",is_student);
            params.put("whyhamstech",spnWhyHamstech.getSelectedItem());
            params.put("lang",langPref);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = params.toString();
        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.uploadProfile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    JSONObject object = jo.getJSONObject("status");
                    if (object.getInt("status")==200){
                        hocLoadingDialog.hideDialog();
                        startActivity(getIntent());
                    } else {
                        Toast.makeText(EditProfileActivity.this, ""+jo.getString("messsage"), Toast.LENGTH_SHORT).show();
                    }
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
                    Toast.makeText(EditProfileActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }
        };
        queue.add(sr);
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

    public void getCityNames(){
        MentorResponse mentorRequest = new MentorResponse(langPref);
        Call<MentorResponse> call = apiService.cityNames(mentorRequest);
        hocLoadingDialog.showLoadingDialog();
        call.enqueue(new Callback<MentorResponse>() {
            @Override
            public void onResponse(Call<MentorResponse> call, retrofit2.Response<MentorResponse> response) {
                hocLoadingDialog.hideDialog();
                for (int j = 0; j<response.body().getCities().size(); j++){
                    categories.add(response.body().getCities().get(j).getCityname());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditProfileActivity.this, R.layout.select_dialog_item, categories);
                txtSelectCity.setThreshold(2);
                txtSelectCity.setAdapter(dataAdapter);
                if (langPref.equals("hi")) {
                    occupationList.add("चुनिए");
                    whyList.add("चुनिए");
                } else {
                    occupationList.add("Choose");
                    whyList.add("Choose");
                }
                for (int j = 0; j<response.body().getOccupations().size(); j++){
                    occupationList.add(response.body().getOccupations().get(j).getOccupation());
                }
                ArrayAdapter<String> occupationAdapter = new ArrayAdapter<String>(EditProfileActivity.this, R.layout.select_dialog_item, occupationList);

                dataAdapter.setDropDownViewResource(R.layout.spinner_text);
                spnWork.setAdapter(occupationAdapter);

                for (int j = 0; j<response.body().getWhyhamstech().size(); j++){
                    whyList.add(response.body().getWhyhamstech().get(j).getWhyhamstech());
                }
                ArrayAdapter<String> whyAdapter = new ArrayAdapter<String>(EditProfileActivity.this, R.layout.select_dialog_item, whyList);
                dataAdapter.setDropDownViewResource(R.layout.spinner_text);
                spnWhyHamstech.setAdapter(whyAdapter);
                if (langPref.equals("en")){
                    languageList.add("English");
                    languageList.add("Hindi");
                } else {
                    languageList.add("अंग्रेज़ी");
                    languageList.add("हिंदी");
                }
                ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(EditProfileActivity.this, R.layout.select_dialog_item, languageList);
                dataAdapter.setDropDownViewResource(R.layout.spinner_text);
                spnLanguage.setAdapter(languageAdapter);

                ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(EditProfileActivity.this, R.layout.select_dialog_item, ageList);
                dataAdapter.setDropDownViewResource(R.layout.spinner_text);
                spnAge.setAdapter(ageAdapter);
                getUserProfile();

            }

            @Override
            public void onFailure(Call<MentorResponse> call, Throwable t) {

            }
        });
    }

    public void getUserProfile(){
        ProfileData userData = new ProfileData(userDataBase.getUserMobileNumber(1),"profile",getResources().getString(R.string.lblApiKey));
        Call<ProfileData> call = apiService.userProfile(userData);
        hocLoadingDialog.showLoadingDialog();
        call.enqueue(new Callback<ProfileData>() {
            @Override
            public void onResponse(Call<ProfileData> call, retrofit2.Response<ProfileData> response) {
                hocLoadingDialog.hideDialog();
                txtUserMobile.setText(response.body().getData().getPhone());
                txtUserName.setText(response.body().getData().getProspectname());
                UserDataConstants.userMail = response.body().getData().getEmail();
                UserDataConstants.userName = response.body().getData().getProspectname();
                UserDataConstants.userMobile = response.body().getData().getPhone();
                UserDataConstants.userAddress = response.body().getData().getAddress();
                UserDataConstants.userCity = response.body().getData().getCity();
                UserDataConstants.userPincode = response.body().getData().getPincode();
                UserDataConstants.userState = response.body().getData().getState();
                UserDataConstants.userCountryName = response.body().getData().getCountry();
                if (response.body().getData().getProspectname() == null || response.body().getData().getProspectname().equals("null")) {

                } else {
                    if (!response.body().getData().getEmail().equalsIgnoreCase("no-reply@hunarcourses.com"))
                    txtEmail.setText(response.body().getData().getEmail());
                }

                if (response.body().getData().getCity() != null) {
                    txtSelectCity.setText(response.body().getData().getCity());
                }

                if (response.body().getData().getIs_student().equalsIgnoreCase("yes")) {
                    isStudent.setChecked(true);
                } else {

                }

                Glide.with(EditProfileActivity.this)

                        .load(response.body().getData().getProfilepic())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(profile_image);
                txtSelectCity.setText(response.body().getData().getCity());
                setSpinnerCity(categories.size(),response.body().getData().getCity());
                setSpinnerWork(occupationList.size(),response.body().getData().getOccupation());
                setSpinnerAge(ageList.size(),response.body().getData().getAgeband());
                setSpinnerWhy(whyList.size(),response.body().getData().getWhyhamstech());
                setLanguage(response.body().getData().getLang());
            }

            @Override
            public void onFailure(Call<ProfileData> call, Throwable t) {

            }
        });
    }

    public void uploadImage(Context context){

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();

        try {
            params.put("page","profile");
            params.put("phone",userDataBase.getUserMobileNumber(1));
            params.put("imagedata",imagePathData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = params.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.uploadImage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    JSONObject object = jo.getJSONObject("status");
                    if (object.getInt("status")==200){
                        Toast.makeText(EditProfileActivity.this, ""+object.getString("messsage"), Toast.LENGTH_SHORT).show();
                        //getProfile(EditProfileActivity.this);
                        getUserProfile();
                    } else {
                        Toast.makeText(EditProfileActivity.this, ""+jo.getString("messsage"), Toast.LENGTH_SHORT).show();
                    }
                } catch(JSONException e) {
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
                    Toast.makeText(EditProfileActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }
        };
        queue.add(sr);
    }
    public void setLanguage(String lang){
        if(lang.equals("en")){
            spnLanguage.setSelection(0);
        } else if (lang.equals("hi")){
            spnLanguage.setSelection(1);
        } else if (lang.equals("te")){
            spnLanguage.setSelection(2);
        }
    }
    public void setSpinnerCity(int spinnerSize,String name){
        for (int i = 0; i<spinnerSize; i++){
            if (categories.get(i).equals(name)){
                selectedCity = categories.get(i);
            }
        }

    }
    public void setSpinnerWork(int spinnerSize,String name){
        for (int i = 0; i<spinnerSize; i++){
            if (occupationList.get(i).equals(name)){
                spnWork.setSelection(i);
            }
        }

    }
    public void setSpinnerAge(int spinnerSize,String name){
        for (int i = 0; i<spinnerSize; i++){
            if (ageList.get(i).equals(name)){
                spnAge.setSelection(i);
            }
        }

    }
    public void setSpinnerWhy(int spinnerSize,String name){
        for (int i = 0; i<spinnerSize; i++){
            if (whyList.get(i).equals(name)){
                spnWhyHamstech.setSelection(i);
            }
        }

    }

    public void getLogEvent(Context context){
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", UserDataConstants.userMobile);
            data.put("fullname",UserDataConstants.userName);
            data.put("email",UserDataConstants.userMail);
            data.put("category","");
            data.put("course","");
            data.put("lesson","");
            data.put("activity","");
            data.put("pagename",PagenameLog);
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentCourses = new Intent(EditProfileActivity.this, HomePageActivity.class);
        startActivity(intentCourses);
        finish();
    }

}
