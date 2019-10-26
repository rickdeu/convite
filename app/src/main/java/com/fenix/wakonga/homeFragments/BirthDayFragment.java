package com.fenix.wakonga.homeFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fenix.wakonga.R;


public class BirthDayFragment extends Fragment {


    public BirthDayFragment() {
        // Required empty public constructor
    }


    public static BirthDayFragment newInstance(String param1, String param2) {
        BirthDayFragment fragment = new BirthDayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_birth_day, container, false);
    }

}
