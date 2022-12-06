package com.hamstechonline.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.hamstechonline.R;
import com.hamstechonline.activities.RegistrationActivity;
import com.hamstechonline.activities.SuccessStoryActivity;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.homepage.SuccessStory;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class StoriesSliderCardPagerAdapter extends PagerAdapter {

    Context context;
    List<SuccessStory> successStories;
    LogEventsActivity logEventsActivity;
    String mobile,fullname;
    UserDataBase userDataBase;

    public StoriesSliderCardPagerAdapter(Context context, List<SuccessStory> successStories) {
        this.context = context;
        this.successStories = successStories;
        logEventsActivity = new LogEventsActivity();
        userDataBase = new UserDataBase(context);
        try {
            mobile = userDataBase.getUserMobileNumber(1);
            fullname = userDataBase.getUserName(1);
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return successStories.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.stories_slider_adapter, container, false);
        container.addView(view);
        bind(view,position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void bind(View view, final int position) {
        ImageView image = view.findViewById(R.id.imageview);
        RelativeLayout cardView = view.findViewById(R.id.cardView);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtDescription = view.findViewById(R.id.txtDescription);

        Glide.with(context)
                .asBitmap()
                .load(successStories.get(position).getImage())
                //.placeholder(R.drawable.duser1)
                .into(image);
        txtTitle.setText(successStories.get(position).getTitle());
        txtDescription.setText(successStories.get(position).getDescription());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogEvent(context);
                new AppsFlyerEventsHelper(context).EventSuccessStories();
                Intent nsdcCourses = new Intent(context, SuccessStoryActivity.class);
                context.startActivity(nsdcCourses);
            }
        });
    }
    public void getLogEvent(Context context) {
        JSONObject data = new JSONObject();
        try {
            data.put("apikey", context.getResources().getString(R.string.lblApiKey));
            data.put("appname", "Dashboard");
            data.put("mobile", mobile);
            data.put("fullname", fullname);
            data.put("email", UserDataConstants.userMail);
            data.put("category", "");
            data.put("course", "");
            data.put("lesson", "");
            data.put("activity", "Student success slides");
            data.put("pagename", "Home page");
            logEventsActivity.LogEventsActivity(context, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
