package com.hamstechonline.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.hamstechonline.R;

public class BuzzDetailsFragment extends Fragment {

    View view;

    public static Fragment newInstance() {
        return new BuzzDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.buzz_details_activity, container, false);

        return view;
    }
}
