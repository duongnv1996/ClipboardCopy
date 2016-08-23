package com.duongkk.clipboardcopy.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duongkk.clipboardcopy.LoginActivity;
import com.duongkk.clipboardcopy.MainActivity;
import com.duongkk.clipboardcopy.R;
import com.duongkk.clipboardcopy.utils.Constant;
import com.duongkk.clipboardcopy.utils.SharedPref;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener,OnCheckedChangeListener{

     private LinearLayout mLogout;
    @Bind(R.id.ll_on)
    LinearLayout mLayoutOn;
    @Bind(R.id.sw_on)
    SwitchCompat mSwitchOn;
    @Bind(R.id.tv_name)
    TextView mTvName;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        mLogout = (LinearLayout) view.findViewById(R.id.ll_logout);
        mLogout.setOnClickListener(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            mTvName.setText(auth.getCurrentUser().getEmail());
        }


        mSwitchOn.setChecked(SharedPref.getInstance(getContext()).getBoolean(Constant.KEY_ON_SERVICE,true));
        mSwitchOn.setOnCheckedChangeListener(this);
        mLayoutOn.setOnClickListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_logout:{
                logOut();
                break;
            }
            case R.id.ll_on:{
                mSwitchOn.setChecked(!mSwitchOn.isChecked());
                break;
            }
        }
    }

     void logOut(){
        SharedPref.getInstance(getContext()).putString(Constant.KEY_URL_ID,"");
         ((MainActivity)getActivity()).stopMyService();
      //  getActivity().stopService(new Intent(getActivity(),ClipboardListener.class));
        startActivity(new Intent(getActivity(),LoginActivity.class));
        getActivity().finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.sw_on:{
                SharedPref.getInstance(getContext()).putBoolean(Constant.KEY_ON_SERVICE,b);
                if(b){
                    ((MainActivity)getActivity()).startMyService();
                }else{
                    ((MainActivity)getActivity()).stopMyService();
                }
                break;
            }
        }
    }
}
