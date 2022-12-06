package com.hamstechonline.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hamstechonline.R;
import com.hamstechonline.activities.EnrolNowActivity;
import com.hamstechonline.datamodel.DetailInfo;
import com.hamstechonline.datamodel.HeaderInfo;
import com.hamstechonline.datamodel.LessonsDataModel;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.GridSpacingItemDecoration;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.SharedPrefsUtils;
import com.hamstechonline.utils.UserDataConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NSDCExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<HeaderInfo> deptList;
    RecyclerView recyclerView;
    LearnFromExpertFacult learnFromExpertFacult;
    CoursesFaqsAdapter faqsAdapter;
    ArrayList<LessonsDataModel> faqsArrayList = new ArrayList<>();
    ArrayList<LessonsDataModel> ansArrayList = new ArrayList<>();
    ArrayList<LessonsDataModel> facultyArrayList = new ArrayList<>();
    int CatId;
    LogEventsActivity logEventsActivity;
    String CategoryName,CourseLog,LessonLog,ActivityLog,PagenameLog;
    Handler handler = new Handler();
    SharedPrefsUtils sharedPrefsUtils;

    public NSDCExpandableListAdapter(Context context, ArrayList<HeaderInfo> deptList, int CatId, String CategoryName, String CourseName) {
        this.context = context;
        this.deptList = deptList;
        this.CatId = CatId;
        logEventsActivity = new LogEventsActivity();
        this.CategoryName = CategoryName;
        this.CourseLog = CourseName;
        sharedPrefsUtils = new SharedPrefsUtils(context, context.getString(R.string.app_name));
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<DetailInfo> productList = deptList.get(groupPosition).getProductList();
        return productList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {

        if (groupPosition == 0){
            final DetailInfo detailInfo = (DetailInfo) getChild(groupPosition, childPosition);
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.attend_workshops, null);
            Button btnNext = convertView.findViewById(R.id.btnNext);
            LinearLayout linearMain = convertView.findViewById(R.id.linearMain);
            TextView txtDescription = convertView.findViewById(R.id.txtDescription);
            txtDescription.setText(Html.fromHtml(detailInfo.getName()));
            linearMain.setBackgroundResource(R.color.light_gray);
            txtDescription.setText(Html.fromHtml(detailInfo.getName()));
            btnNext.setVisibility(View.GONE);

        } else if (groupPosition == 1){
            final DetailInfo detailInfo = (DetailInfo) getChild(groupPosition, childPosition);
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.attend_workshops, null);
            Button btnNext = convertView.findViewById(R.id.btnNext);
            TextView txtDescription = convertView.findViewById(R.id.txtDescription);
            txtDescription.setText(detailInfo.getName());
            btnNext.setText(context.getResources().getString(R.string.enrolNowToStart));
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LessonLog = "";
                    ActivityLog = detailInfo.getName();PagenameLog = "Enrol Now button";
                    getLogEvent(context);
                    EnrolNow(CatId);
                }
            });
        } else if (groupPosition == 2){
            final DetailInfo detailInfo = (DetailInfo) getChild(groupPosition, childPosition);
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.starter_kit, null);
            Button btnNext = convertView.findViewById(R.id.btnNext);
            TextView txtDescription = convertView.findViewById(R.id.txtDescription);
            ImageView imgStaterKit = convertView.findViewById(R.id.imgStaterKit);
            txtDescription.setText(detailInfo.getName().split("~")[0]);
            btnNext.setText(context.getResources().getString(R.string.enrolGetYours));
            Glide.with(context)
                    .load(detailInfo.getName().split("~")[1])
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.ic_launcher)
                    .into(imgStaterKit);

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LessonLog = "";
                    ActivityLog = detailInfo.getName();PagenameLog = "Enrol Now button";
                    getLogEvent(context);
                    EnrolNow(CatId);
                }
            });
        } else if (groupPosition == 3){
            final DetailInfo detailInfo = (DetailInfo) getChild(groupPosition, childPosition);
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.learn_expert_faculty_activity, null);
            recyclerView = convertView.findViewById(R.id.listItems);
            Button btnNext = convertView.findViewById(R.id.btnNext);
            btnNext.setText(context.getResources().getString(R.string.enrolNowToStart));
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LessonLog = "";
                    ActivityLog = detailInfo.getName();PagenameLog = "Enrol Now button";
                    getLogEvent(context);
                    EnrolNow(CatId);
                }
            });
            try {
                JSONArray array = new JSONArray(detailInfo.getName());
                facultyArrayList.clear();
                for (int i = 0; i<array.length(); i++){
                    JSONObject objectAns = array.getJSONObject(i);
                    LessonsDataModel dataModel = new LessonsDataModel();

                    dataModel.setFaculty_name(objectAns.getString("faculty_name"));
                    dataModel.setFaculty_description(objectAns.getString("faculty_description"));
                    dataModel.setFaculty_image(objectAns.getString("faculty_image"));

                    facultyArrayList.add(dataModel);
                }
                if (facultyArrayList.size() != 0){
                    learnFromExpertFacult = new LearnFromExpertFacult(context,facultyArrayList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL, false));
                    recyclerView.addItemDecoration(new GridSpacingItemDecoration(1,15,false));
                    recyclerView.setAdapter(learnFromExpertFacult);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (groupPosition == 4){
            final DetailInfo detailInfo = (DetailInfo) getChild(groupPosition, childPosition);
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.certified_faculty, null);
            Button btnNext = convertView.findViewById(R.id.btnNext);
            TextView txtDescription = convertView.findViewById(R.id.txtDescription);
            ImageView imgStaterKit = convertView.findViewById(R.id.imgFaculty);
            txtDescription.setText(detailInfo.getName().split("~")[0]);
            btnNext.setText(context.getResources().getString(R.string.enrolGetCertified));
            Glide.with(context)
                    .load(detailInfo.getName().split("~")[1])
                    .into(imgStaterKit);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LessonLog = "";
                    ActivityLog = detailInfo.getName();PagenameLog = "Enrol Now button";
                    getLogEvent(context);
                    EnrolNow(CatId);
                }
            });
        }else if (groupPosition == 5){
            final DetailInfo detailInfo = (DetailInfo) getChild(groupPosition, childPosition);
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.attend_workshops, null);
            Button btnNext = convertView.findViewById(R.id.btnNext);
            TextView txtDescription = convertView.findViewById(R.id.txtDescription);
            txtDescription.setText(detailInfo.getName());
            btnNext.setText(context.getResources().getString(R.string.enrolAttend));
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LessonLog = "";
                    ActivityLog = detailInfo.getName();PagenameLog = "Enrol Now button";
                    getLogEvent(context);
                    EnrolNow(CatId);
                }
            });
        } else if (groupPosition == 6) {
            final DetailInfo detailInfo = (DetailInfo) getChild(groupPosition, childPosition);
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.watch_live_webinars, null);
            Button btnNext = convertView.findViewById(R.id.btnNext);
            TextView txtDescription = convertView.findViewById(R.id.txtDescription);
            txtDescription.setText(detailInfo.getName());
            btnNext.setText(context.getResources().getString(R.string.enrolWatch));
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LessonLog = "";
                    ActivityLog = detailInfo.getName();PagenameLog = "Enrol Now button";
                    getLogEvent(context);
                    EnrolNow(CatId);
                }
            });
        } else if (groupPosition == 7) {
            final DetailInfo detailInfo = (DetailInfo) getChild(groupPosition, childPosition);
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.gain_course_counselling, null);
            Button btnNext = convertView.findViewById(R.id.btnNext);
            TextView txtDescription = convertView.findViewById(R.id.txtDescription);
            txtDescription.setText(detailInfo.getName());
            btnNext.setText(context.getResources().getString(R.string.enrolStartCourse));
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LessonLog = "";
                    ActivityLog = detailInfo.getName();PagenameLog = "Enrol Now button";
                    getLogEvent(context);
                    EnrolNow(CatId);
                }
            });
        } else if (groupPosition == 8) {
            final DetailInfo detailInfo = (DetailInfo) getChild(groupPosition, childPosition);
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.career_options, null);
            Button btnNext = convertView.findViewById(R.id.btnNext);
            TextView txtDescription = convertView.findViewById(R.id.txtDescription);
            txtDescription.setText(detailInfo.getName());
            btnNext.setText(context.getResources().getString(R.string.enrolBeginCareer));
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LessonLog = "";
                    ActivityLog = detailInfo.getName();PagenameLog = "Enrol Now button";
                    getLogEvent(context);
                    EnrolNow(CatId);
                }
            });
        }else if (groupPosition == 9) {
            DetailInfo detailInfo = (DetailInfo) getChild(groupPosition, childPosition);
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.lessons_faqs, null);
            recyclerView = convertView.findViewById(R.id.listItems);
            try {
                JSONArray faqArray = new JSONArray(detailInfo.getName().split("~")[0]);
                faqsArrayList.clear();
                ansArrayList.clear();
                for (int i = 0; i < faqArray.length(); i++) {
                    JSONObject objectQns = faqArray.getJSONObject(i);
                    LessonsDataModel dataModel = new LessonsDataModel();

                    dataModel.setLessonId(objectQns.getString("fid"));
                    dataModel.setLesson_title(objectQns.getString("faq_title"));

                    faqsArrayList.add(dataModel);
                }
                JSONArray ansArray = new JSONArray(detailInfo.getName().split("~")[1]);
                for (int i = 0; i < ansArray.length(); i++) {
                    JSONObject objectAns = ansArray.getJSONObject(i);
                    LessonsDataModel dataModel = new LessonsDataModel();

                    dataModel.setLessonId(objectAns.getString("fid"));
                    dataModel.setLesson_title(objectAns.getString("faq_content"));

                    ansArrayList.add(dataModel);
                }
                if (ansArrayList.size() != 0) {
                    faqsAdapter = new CoursesFaqsAdapter(context, faqsArrayList, ansArrayList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                    recyclerView.setAdapter(faqsAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        ArrayList<DetailInfo> productList = deptList.get(groupPosition).getProductList();
        return productList.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return deptList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return deptList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        final HeaderInfo headerInfo = (HeaderInfo) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.expandable_list_group, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.listTitle);
        heading.setText(headerInfo.getName().trim());

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void EnrolNow(int catId){
        handler.removeCallbacksAndMessages(null);
        new AppsFlyerEventsHelper(context).EventEnroll();
        Intent intent = new Intent(context,EnrolNowActivity.class);
        sharedPrefsUtils.setSharedPrefBoolean(ApiConstants.isFromCourse, true);
        intent.putExtra("getCourseId",catId);
        context.startActivity(intent);
        ((Activity)context).finish();
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
}

