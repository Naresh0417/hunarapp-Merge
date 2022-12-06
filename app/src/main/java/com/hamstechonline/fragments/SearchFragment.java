package com.hamstechonline.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

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
import com.hamstechonline.adapters.CustomAutoCompleteAdapter;
import com.hamstechonline.datamodel.CategoryDatamodel;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.HocLoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchFragment extends Fragment {

    ArrayList<CategoryDatamodel> materialDataModel = new ArrayList<>();
    HashMap<String, ArrayList<CategoryDatamodel>> autoTextViewData = new HashMap<>();
    ArrayList<String> stringNames= new ArrayList<String>();
    //String[] stringNames;
    AutoCompleteTextView autoSearch;
    HocLoadingDialog hocLoadingDialog;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_layout,
                container, false);
        autoSearch = view.findViewById(R.id.autoSearch);

        hocLoadingDialog = new HocLoadingDialog(getActivity());

        getSearchList(getActivity());

        return view;
    }

    private void getSearchList(final Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        try {
            params.put("appname","Hamstech");
            params.put("page","course");
            params.put("apikey",getResources().getString(R.string.lblApiKey));
            params.put("search","");
            metaData.put("metadata", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hocLoadingDialog.showLoadingDialog();
        final String mRequestBody = metaData.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConstants.getSearchList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            autoTextViewData.clear();
                            materialDataModel.clear();
                            stringNames.clear();
                            JSONArray jsonArray = jsonObject.getJSONArray("results");

                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                CategoryDatamodel datamodel = new CategoryDatamodel();
                                datamodel.setCategoryId(object.getString("courseId"));
                                datamodel.setCategoryname(object.getString("course_description"));
                                datamodel.setCategory_Title(object.getString("title"));
                                datamodel.setCat_image_url(object.getString("image_url"));
                                datamodel.setCatVideoUrl(object.getString("video_url"));

                                materialDataModel.add(datamodel);
                                autoTextViewData.put(object.getString("title"), materialDataModel);
                                stringNames.add(object.getString("title"));
                                //stringNames.add(object.getString("lesson_title"));
                            }
                            //ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.select_dialog_item, stringNames);
                            CustomAutoCompleteAdapter adapter = new CustomAutoCompleteAdapter(getActivity(),materialDataModel, stringNames);
                            autoSearch.setThreshold(2);
                            autoSearch.setAdapter(adapter);
                            hocLoadingDialog.hideDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            hocLoadingDialog.hideDialog();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                            hocLoadingDialog.hideDialog();
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
                    Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }

        };stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

}
