package com.hamstechonline.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.hamstechonline.fragments.LessonsFragment;
import com.hamstechonline.fragments.MuchMoreFragment;

public class CourseViewPagerAdapter extends FragmentStatePagerAdapter {

    LessonsFragment lf1 = LessonsFragment .newInstance("LessonsFragment, intance");
    MuchMoreFragment lf2 = MuchMoreFragment .newInstance("MuchMoreFragment, intance");

    public CourseViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:  return lf1;
            default:return lf2;
        }
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }
}
