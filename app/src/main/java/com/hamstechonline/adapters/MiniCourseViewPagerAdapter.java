package com.hamstechonline.adapters;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hamstechonline.fragments.LessonsFragment;
import com.hamstechonline.fragments.MiniCourcesTab;
import com.hamstechonline.fragments.MiniLessonsFragment;
import com.hamstechonline.fragments.MuchMoreFragment;

public class MiniCourseViewPagerAdapter extends FragmentStatePagerAdapter {

    MiniLessonsFragment lf2 = MiniLessonsFragment.newInstance("LessonsFragment, intance");
    MiniCourcesTab lf1 = MiniCourcesTab .newInstance("MuchMoreFragment, intance");

    public MiniCourseViewPagerAdapter(FragmentManager fm) {
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
