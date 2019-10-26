package com.fenix.wakonga.homeFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fenix.wakonga.R;
import com.google.android.material.card.MaterialCardView;


public class HomeFragment extends Fragment {

    private MaterialCardView mWidding;
    private MaterialCardView mServices;
    private MaterialCardView mParties;
    private MaterialCardView mInvities;
    private MaterialCardView mAfterParty;
    private MaterialCardView mBirthDays;
    private MaterialCardView mBaptism;
    private MaterialCardView mReligious;
    private MaterialCardView mChildrens;
    private MaterialCardView mOthers;

    private FragmentManager mFragmentManager;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //initialize variables
        mWidding = rootView.findViewById(R.id.casamentos);
        mServices = rootView.findViewById(R.id.servicos);
        mParties = rootView.findViewById(R.id.festas);
        mInvities = rootView.findViewById(R.id.convites);
        mAfterParty = rootView.findViewById(R.id.after_party);
        mBirthDays = rootView.findViewById(R.id.aniversarios);
        mBaptism = rootView.findViewById(R.id.baptismo);
        mReligious = rootView.findViewById(R.id.Religiosos);
        mChildrens = rootView.findViewById(R.id.infantil);
        mOthers = rootView.findViewById(R.id.outros);

        mWidding.setOnClickListener(view -> {
            Fragment widdingFragment = new WiddingFragment();
            transactFragment(widdingFragment);
        });

        mServices.setOnClickListener(view -> {
            Fragment serviceFragment = new ServiceFragment();
            transactFragment(serviceFragment);
        });

        mParties.setOnClickListener(view -> {
            Fragment partyFragment = new PartyFragment();
            transactFragment(partyFragment);
        });

        mInvities.setOnClickListener(view -> {
            Fragment invityFragment = new InvityFragment();
            transactFragment(invityFragment);
        });

        mAfterParty.setOnClickListener(view -> {
            Fragment afterPartyFragment = new AfterPartyFragment();
            transactFragment(afterPartyFragment);
        });

        mBirthDays.setOnClickListener(view -> {
            Fragment birthDayFragment = new BirthDayFragment();
            transactFragment(birthDayFragment);
        });

        mBaptism.setOnClickListener(view -> {
            Fragment baptismFragment = new BaptismFragment();
            transactFragment(baptismFragment);
        });

        mReligious.setOnClickListener(view -> {
            Fragment religiousFragment = new ReligiousFragment();
            transactFragment(religiousFragment);
        });

        mChildrens.setOnClickListener(view -> {
            Fragment kidsFragment = new KidsFragment();
            transactFragment(kidsFragment);
        });

        mOthers.setOnClickListener(view -> {
            Fragment othersFragment = new OthersFragment();
            transactFragment(othersFragment);
        });


        return rootView;
    }

    private void transactFragment(Fragment fragment) {
        mFragmentManager.beginTransaction().replace(R.id.parent_home,
                fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


}
