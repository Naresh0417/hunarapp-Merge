package com.hamstechonline.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hamstechonline.R;
import com.hamstechonline.activities.MiniLessonsEnrolNowActivity;
import com.hamstechonline.adapters.MiniLessonsExpandableListAdapter;
import com.hamstechonline.adapters.NSDCExpandableListAdapter;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.CategoryDatamodel;
import com.hamstechonline.datamodel.CourseDetailsResponse;
import com.hamstechonline.datamodel.CourseOverview;
import com.hamstechonline.datamodel.DetailInfo;
import com.hamstechonline.datamodel.HeaderInfo;
import com.hamstechonline.datamodel.LessonsDataModel;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.FacebookEventsHelper;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.NonScrollExpandableListView;
import com.hamstechonline.utils.Params;
import com.hamstechonline.utils.SharedPrefsUtils;
import com.hamstechonline.utils.UserDataConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class AssignmentFragment extends Fragment {

    NonScrollExpandableListView listExpandable;
    private int lastExpandedPosition = -1;
    MiniLessonsExpandableListAdapter expandableListAdapter;
    NSDCExpandableListAdapter NSDCexpandableListAdapter;
    List<String> groupNames;
    String searchName,overviewContent,NSDCCOntent,course_syllabus,course_starterkit,course_starter_kit_img,course_certified_by,
            course_certified_by_img,course_live_webinars,course_councelling,course_workshops,course_career_options,
            questions, answers,faclutylist;
    UserDataBase userDataBase;
    String language;
    TextView txtTitle,txtDescription;
    LinearLayout btnEnrolNow;
    TextView txtTotalFee;
    ArrayList<LessonsDataModel> faqsArrayList = new ArrayList<>();
    ArrayList<LessonsDataModel> ansArrayList = new ArrayList<>();
    ArrayList<LessonsDataModel> facultyArrayList = new ArrayList<>();
    ArrayList<CategoryDatamodel> categoryDatamodel = new ArrayList<>();
    private LinkedHashMap<String, HeaderInfo> myDepartments = new LinkedHashMap<String, HeaderInfo>();
    private ArrayList<HeaderInfo> deptList = new ArrayList<HeaderInfo>();
    ArrayList <String> childData = new ArrayList<>();
    LogEventsActivity logEventsActivity;
    String CategoryName,CourseLog,LessonLog,ActivityLog,PagenameLog;
    HocLoadingDialog hocLoadingDialog;
    String langPref = "Language";
    SharedPreferences prefs;
    Bundle params;
    AppEventsLogger logger;
    FirebaseAnalytics firebaseAnalytics;
    SharedPrefsUtils sharedPrefsUtils;
    RecyclerView overViewList;
    ApiInterface apiService;
    OverViewdapter overViewdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.minicourses_details_fragment, container, false);
        listExpandable = v.findViewById(R.id.listExpandable);
        txtTitle = v.findViewById(R.id.txtTitle);
        txtDescription = v.findViewById(R.id.txtDescription);
        btnEnrolNow = v.findViewById(R.id.btnEnrolNow);
        txtTotalFee = v.findViewById(R.id.txtTotalFee);
        overViewList = v.findViewById(R.id.overViewList);


        listExpandable.setOnGroupClickListener(myListGroupClicked);
        userDataBase = new UserDataBase(getActivity());
        groupNames = new ArrayList<>();
        categoryDatamodel.clear();

        listExpandable.setFocusable(false);

        hocLoadingDialog = new HocLoadingDialog(getActivity());
        params = new Bundle();
        logger = AppEventsLogger.newLogger(getActivity());
        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        prefs = getActivity().getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "");
        listExpandable.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        logEventsActivity = new LogEventsActivity();

        sharedPrefsUtils = new SharedPrefsUtils(getActivity(), getString(R.string.app_name));
        groupNames.clear();
        groupNames = Arrays.asList(getActivity().getResources().getStringArray(R.array.OtherExpandableList));

        if(getActivity().getIntent().getStringExtra("CategoryId") != null) {
            searchName = getActivity().getIntent().getStringExtra("CategoryId");
            language = getActivity().getIntent().getStringExtra("language");
            txtTotalFee.setText(getResources().getString(R.string.enrolNowMiniCourse)+" "+
                    getActivity().getIntent().getStringExtra("amount")+"/-");
            getLessons(getActivity());
            courseDetails();
        }

        btnEnrolNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryName = getActivity().getIntent().getStringExtra("CategoryName");
                CourseLog = getActivity().getIntent().getStringExtra("description");LessonLog = "";
                ActivityLog = "Overview";PagenameLog = "Accordion";
                logAccordionEvent(CourseLog,ActivityLog);
                getLogEvent(getActivity());
                new AppsFlyerEventsHelper(getActivity()).EventEnroll();
                Intent intent = new Intent(getActivity(), MiniLessonsEnrolNowActivity.class);
                sharedPrefsUtils.setSharedPrefBoolean(ApiConstants.isFromCourse, true);
                intent.putExtra("getCourseId",Integer.parseInt(getActivity().getIntent().getStringExtra("CategoryId")));
                startActivity(intent);
            }
        });

        return v;
    }

    public static AssignmentFragment newInstance(String text) {

        AssignmentFragment f = new AssignmentFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    private void setListViewHeight(ExpandableListView listView,
                                        int group) {
        MiniLessonsExpandableListAdapter listAdapter = (MiniLessonsExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }


        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10) {
            height = 200;
        }
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void setNSDCListViewHeight(ExpandableListView listView,
                                   int group) {
        NSDCExpandableListAdapter listAdapter = (NSDCExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }


        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10) {
            height = 200;
        }
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void courseDetails() {
        CourseDetailsResponse courseDetailsResponse = new CourseDetailsResponse("Hamstech","course",
                getActivity().getResources().getString(R.string.lblApiKey),searchName,language,langPref,userDataBase.getUserMobileNumber(1),"no");
        Call<CourseDetailsResponse> call = apiService.getCourseDetails(courseDetailsResponse);
        call.enqueue(new Callback<CourseDetailsResponse>() {
            @Override
            public void onResponse(Call<CourseDetailsResponse> call, retrofit2.Response<CourseDetailsResponse> response) {
                overViewdapter = new OverViewdapter(getActivity(),response.body().getCourseOverview());
                overViewList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                overViewList.setAdapter(overViewdapter);
                txtDescription.setText(response.body().getCourseDetails().getCourseDescription());
            }

            @Override
            public void onFailure(Call<CourseDetailsResponse> call, Throwable t) {

            }
        });
    }

    public void getLessons(Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        try {
            params.put("appname","Hamstech");
            params.put("page","course");
            params.put("apikey",getResources().getString(R.string.lblApiKey));
            params.put("courseId",searchName);
            params.put("language",language);
            params.put("lang",langPref);
            metaData.put("metadata", params);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }

        final String mRequestBody = metaData.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.getListLessons, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    faqsArrayList.clear();
                    ansArrayList.clear();
                    facultyArrayList.clear();
                    if (jo.getString("status").equals("ok")) {

                        overviewContent = jo.getString("overview");
                        NSDCCOntent = jo.getJSONObject("detail").getString("nsdc_desc");
                        course_syllabus = jo.getString("course_syllabus");
                        course_starterkit = jo.getString("course_starterkit");
                        course_starter_kit_img = jo.getString("course_starter_kit_img");
                        course_certified_by = jo.getString("course_certified_by");
                        course_certified_by_img = jo.getString("course_certified_by_img");
                        course_live_webinars = jo.getString("course_live_webinars");
                        course_workshops = jo.getString("course_workshops");
                        course_councelling = jo.getString("course_councelling");
                        course_career_options = jo.getString("course_career_options");
                        questions = jo.getString("questions");
                        answers = jo.getString("answers");
                        faclutylist = jo.getString("faclutylist");
                        String nsdc_amount = jo.getJSONObject("detail").getString("nsdc_amount_6_months");
                        //childData.add(overviewContent);
                        txtTitle.setText(getActivity().getResources().getString(R.string.Overview));
                        txtDescription.setText(overviewContent);

                        if (getActivity().getIntent().getStringExtra("statusNSDC") != null){
                            if (getActivity().getIntent().getStringExtra("statusNSDC").equals("1")){
                                childData.add(NSDCCOntent);
                                childData.add(course_syllabus);
                                childData.add(course_starterkit+"~"+course_starter_kit_img);
                                childData.add(faclutylist);
                                childData.add(course_certified_by+"~"+course_certified_by_img);
                                childData.add(course_workshops);
                                childData.add(course_live_webinars);
                                childData.add(course_councelling);
                                childData.add(course_career_options);
                                childData.add(questions+"~"+answers);

                                LoadData();

                                NSDCexpandableListAdapter = new NSDCExpandableListAdapter(getActivity(),deptList,
                                        Integer.parseInt(getActivity().getIntent().getStringExtra("CategoryId")),
                                        getActivity().getIntent().getStringExtra("CategoryName"),
                                        getActivity().getIntent().getStringExtra("CourseName"));
                                listExpandable.setAdapter(NSDCexpandableListAdapter);

                                setNSDCListViewHeight(listExpandable,9);
                                listExpandable.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                                    @Override
                                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                                        setNSDCListViewHeight(parent, groupPosition);
                                        return false;
                                    }
                                });
                            } else {
                                childData.add(course_syllabus);
                                childData.add(course_starterkit+"~"+course_starter_kit_img);
                                childData.add(faclutylist);
                                childData.add(course_certified_by+"~"+course_certified_by_img);
                                childData.add(course_workshops);
                                childData.add(course_live_webinars);
                                childData.add(course_councelling);
                                childData.add(course_career_options);
                                childData.add(questions+"~"+answers);

                                LoadData();

                                expandableListAdapter = new MiniLessonsExpandableListAdapter(getActivity(),deptList,
                                        Integer.parseInt(getActivity().getIntent().getStringExtra("CategoryId")),
                                        getActivity().getIntent().getStringExtra("CategoryName"),
                                        getActivity().getIntent().getStringExtra("CourseName"));
                                listExpandable.setAdapter(expandableListAdapter);

                                setListViewHeight(listExpandable,8);
                                listExpandable.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                                    @Override
                                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                                        setListViewHeight(parent, groupPosition);
                                        return false;
                                    }
                                });
                            }
                        } else {
                            childData.add(course_syllabus);
                            childData.add(course_starterkit+"~"+course_starter_kit_img);
                            childData.add(faclutylist);
                            childData.add(course_certified_by+"~"+course_certified_by_img);
                            childData.add(course_workshops);
                            childData.add(course_live_webinars);
                            childData.add(course_councelling);
                            childData.add(course_career_options);
                            childData.add(questions+"~"+answers);

                            LoadData();

                            expandableListAdapter = new MiniLessonsExpandableListAdapter(getActivity(),deptList,
                                    Integer.parseInt(getActivity().getIntent().getStringExtra("CategoryId")),
                                    getActivity().getIntent().getStringExtra("CategoryName"),
                                    getActivity().getIntent().getStringExtra("CourseName"));
                            listExpandable.setAdapter(expandableListAdapter);

                            setListViewHeight(listExpandable,8);
                            listExpandable.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                                setListViewHeight(parent, groupPosition);
                                return false;
                            }
                        });
                        }

                        listExpandable.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                            @Override
                            public void onGroupExpand(int groupPosition) {
                                if (lastExpandedPosition != -1
                                        && groupPosition != lastExpandedPosition) {
                                    listExpandable.collapseGroup(lastExpandedPosition);
                                }
                                lastExpandedPosition = groupPosition;
                            }
                        });

                        listExpandable.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                                CategoryName = getActivity().getIntent().getStringExtra("CategoryName");
                                CourseLog = getActivity().getIntent().getStringExtra("CourseName");
                                LessonLog = "";
                                ActivityLog = groupNames.get(groupPosition);PagenameLog = "Accordion";
                                logAccordionEvent(CourseLog,ActivityLog);
                                getLogEvent(getActivity());
                                return false;
                            }
                        });

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
                    Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        sr.setRetryPolicy(new DefaultRetryPolicy(7000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    private void LoadData (){

        for (int m = 0; m < groupNames.size(); m++) {

            addProduct(groupNames.get(m), childData.get(m));
        }
    }

    public class OverViewdapter extends RecyclerView.Adapter<OverViewdapter.ViewHolder> {

        Context context;
        List<CourseOverview> courseOverviews;

        public OverViewdapter(Context context, List<CourseOverview> courseOverviews) {
            this.context = context;
            this.courseOverviews = courseOverviews;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.overview_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            try {
                holder.txtLabel.setText(courseOverviews.get(position).getContent());
                Glide.with(context)
                        .load(courseOverviews.get(position).getIcon())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgIcon);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return courseOverviews.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgIcon;
            TextView txtLabel;

            public ViewHolder(@NonNull View view) {
                super(view);
                imgIcon = view.findViewById(R.id.imgIcon);
                txtLabel = view.findViewById(R.id.txtLabel);
            }

        }
    }

    public void logAccordionEvent (String course, String accordion) {
        //params.putString("course", course);
        params.putString(AppEventsConstants.EVENT_PARAM_LEVEL, accordion);
        logger.logEvent(AppEventsConstants.EVENT_NAME_ACHIEVED_LEVEL, params);
        params.putString(Params.ACCORDION_NAME, accordion);
        logger.logEvent("accordion", params);
        new FacebookEventsHelper(getActivity()).logSpendCreditsEvent(accordion);
    }

    public void getLogEvent(Context context){
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", UserDataConstants.userMobile);
            data.put("fullname",UserDataConstants.userName);
            data.put("email",UserDataConstants.userMail);
            data.put("category",CategoryName);
            data.put("course",CourseLog);
            data.put("lesson",LessonLog);
            data.put("activity",ActivityLog);
            data.put("pagename",PagenameLog);
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int addProduct(String department, String product){

        int groupPosition = 0;

        HeaderInfo headerInfo = myDepartments.get(department);
        if (headerInfo == null) {
            headerInfo = new HeaderInfo();
            headerInfo.setName(department);
            myDepartments.put(department, headerInfo);
            deptList.add(headerInfo);
        }

        ArrayList<DetailInfo> productList = headerInfo.getProductList();
        int listSize = productList.size();
        listSize++;


        DetailInfo detailInfo = new DetailInfo();
        detailInfo.setSequence(String.valueOf(listSize));
        detailInfo.setName(product);
        productList.add(detailInfo);
        headerInfo.setProductList(productList);

        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }

    private ExpandableListView.OnGroupClickListener myListGroupClicked = new ExpandableListView.OnGroupClickListener() {

        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {
            HeaderInfo headerInfo = deptList.get(groupPosition);
            return false;
        }

    };
}
