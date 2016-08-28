package com.duongkk.clipboardcopy.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.duongkk.clipboardcopy.GuideConnectActivity;
import com.duongkk.clipboardcopy.LoginActivity;
import com.duongkk.clipboardcopy.MainActivity;
import com.duongkk.clipboardcopy.R;
import com.duongkk.clipboardcopy.utils.CommonUtils;
import com.duongkk.clipboardcopy.utils.Constant;
import com.duongkk.clipboardcopy.utils.SharedPref;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener,OnCheckedChangeListener{

     private LinearLayout mLogout;
    @Bind(R.id.ll_on)
    LinearLayout mLayoutOn;
    @Bind(R.id.sw_on)
    SwitchCompat mSwitchOn;
    @Bind(R.id.tv_name)
    TextView mTvName;

    @Bind(R.id.ll_delete)
    LinearLayout mLayoutDelete;
    LinearLayout mLayoutConnect;
    LinearLayout mLayoutNotify;
    LinearLayout mLayoutTheme;
    @Bind(R.id.tv_theme)
    TextView mTvTheme;
    @Bind(R.id.cb_noti)
    CheckBox mCbNotify;
    private Firebase mRoot;
    int choice;
    String[] themes;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoot = new Firebase(Constant.URL_ROOT_FINAL+SharedPref.getInstance(getContext()).getString(Constant.KEY_URL_ID,""));
        themes = getContext().getResources().getStringArray(R.array.item_theme);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        mLogout = (LinearLayout) view.findViewById(R.id.ll_logout);
        mLayoutConnect = (LinearLayout) view.findViewById(R.id.ll_connect);
        mLayoutNotify = (LinearLayout) view.findViewById(R.id.ll_notification);
        mLayoutTheme = (LinearLayout) view.findViewById(R.id.ll_theme);

        mLogout.setOnClickListener(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            mTvName.setText(auth.getCurrentUser().getUid().toString());
        }
        mSwitchOn.setChecked(SharedPref.getInstance(getContext()).getBoolean(Constant.KEY_ON_SERVICE,true));
        mCbNotify.setChecked(SharedPref.getInstance(getContext()).getBoolean(Constant.KEY_NOTIFY,true));

        mSwitchOn.setOnCheckedChangeListener(this);
        mCbNotify.setOnCheckedChangeListener(this);
        mLayoutOn.setOnClickListener(this);
        mLayoutDelete.setOnClickListener(this);
        mLayoutConnect.setOnClickListener(this);
        mLayoutNotify.setOnClickListener(this);
        mLayoutTheme.setOnClickListener(this);
        int modeType;
        modeType = AppCompatDelegate.getDefaultNightMode();
        choice= modeType;
        int theme = SharedPref.getInstance(getContext()).getInt(Constant.KEY_THEME,0);
        choice = theme;
       // ((MainActivity)getActivity()).applyTheme(theme);
        mTvTheme.setText(themes[theme]);
//        if (modeType  == AppCompatDelegate.MODE_NIGHT_AUTO) {
//            RLog.e("Default Mode: Auto");
//            mTvTheme.setText(themes[0]);
//
//        } else if (modeType == AppCompatDelegate.MODE_NIGHT_YES) {
//            RLog.e("Default Mode: Night");
//             mTvTheme.setText(themes[2]);
//        } else if (modeType == AppCompatDelegate.MODE_NIGHT_NO) {
//            RLog.e("Default Mode: Day");
//             mTvTheme.setText(themes[1]);
//        }else {
//             RLog.e("Default Mode: follow");
//             mTvTheme.setText(themes[3]);
//        }
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
            case R.id.ll_notification:{
                mCbNotify.setChecked(!mCbNotify.isChecked());
                break;
            }
            case R.id.ll_connect:{
                Intent intent =new Intent(getActivity(), GuideConnectActivity.class);
                intent.putExtra(Constant.KEY_FINISH,true);
               startActivity(intent);
                break;
            }
            case R.id.ll_delete:{
                mRoot.removeValue(new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        Toast.makeText(getContext(),"Deleted!",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case R.id.ll_theme:{
               new MaterialDialog.Builder(getContext()).title(R.string.night_mode_scheule)
                       .items(R.array.item_theme)
                       .itemsCallbackSingleChoice(choice, new MaterialDialog.ListCallbackSingleChoice() {
                           @Override
                           public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                   // Now recreate for it to take effect
                               mTvTheme.setText(themes[which]);
                               SharedPref.getInstance(getContext()).putInt(Constant.KEY_THEME,which);
                               switch (which){
                                   case 0:{
                                       ((MainActivity)getActivity()).applyTheme(AppCompatDelegate.MODE_NIGHT_AUTO);

                                       break;
                                   }
                                   case 1:{
                                       ((MainActivity)getActivity()).applyTheme(AppCompatDelegate.MODE_NIGHT_NO);

                                       break;
                                   }
                                   case 2:{
                                       ((MainActivity)getActivity()).applyTheme(AppCompatDelegate.MODE_NIGHT_YES);

                                       break;
                                   }
                                   case 3:{
                                       ((MainActivity)getActivity()).applyTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

                                       break;
                                   }
                               }
                               return false;
                           }
                       })
               .show();

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
            case R.id.cb_noti:{
                SharedPref.getInstance(getContext()).putBoolean(Constant.KEY_NOTIFY,b);
                if(b){
                    if(mSwitchOn.isChecked()) CommonUtils.showNotification(getContext());
                    //CommonUtils.showNotification(getContext());
                    //((MainActivity)getActivity()).startMyService();
                }else{
                    //((MainActivity)getActivity()).stopMyService();
                }
                break;
            }
        }
    }
}
