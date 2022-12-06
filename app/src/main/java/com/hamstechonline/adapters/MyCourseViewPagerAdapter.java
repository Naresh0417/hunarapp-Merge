package com.hamstechonline.adapters;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hamstechonline.fragments.AssignmentFragment;
import com.hamstechonline.fragments.Assignments_Downloads_fragment;
import com.hamstechonline.fragments.MiniCourcesTab;
import com.hamstechonline.fragments.MiniLessonsFragment;
import com.hamstechonline.fragments.TopicsFragment;

public class MyCourseViewPagerAdapter extends FragmentStatePagerAdapter {

    TopicsFragment lf1 = TopicsFragment.newInstance("LessonsFragment, intance");
    Assignments_Downloads_fragment lf2 = Assignments_Downloads_fragment .newInstance("MuchMoreFragment, intance");

    public MyCourseViewPagerAdapter(FragmentManager fm) {
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
