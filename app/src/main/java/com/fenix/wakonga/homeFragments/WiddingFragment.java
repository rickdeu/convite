package com.fenix.wakonga.homeFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fenix.wakonga.R;


public class WiddingFragment extends Fragment {

    public WiddingFragment() {

    }


    public static WiddingFragment newInstance() {
        WiddingFragment fragment = new WiddingFragment();

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
        return inflater.inflate(R.layout.fragment_wedding, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


}
