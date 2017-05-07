package com.duongkk.clipboardcopy.fragments;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import me.grantland.widget.AutofitHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener,OnCheckedChangeListener,AdapterView.OnItemSelectedListener{

     private LinearLayout mLogout;
    @Bind(R.id.ll_on)
    LinearLayout mLayoutOn;
    @Bind(R.id.sw_on)
    SwitchCompat mSwitchOn;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.ll_upgrade)
    LinearLayout mLayoutUpgrade;
    @Bind(R.id.ll_delete)
    LinearLayout mLayoutDelete;
    LinearLayout mLayoutConnect;
    LinearLayout mLayoutNotify;
    LinearLayout mLayoutTheme;
    @Bind(R.id.ll_rateapp)
    LinearLayout mLayoutRate;
    @Bind(R.id.ll_contact)
    LinearLayout mlayoutContact;
    @Bind(R.id.ll_moreapp)
   LinearLayout mLayoutMoreApp;
    @Bind(R.id.tv_theme)
    TextView mTvTheme;
    @Bind(R.id.cb_noti)
    CheckBox mCbNotify;

    @Bind(R.id.tv_on)
    TextView mTvSW;

    @Bind(R.id.ll_notification_coppied)
    LinearLayout mLayoutNotiCopied;
    @Bind(R.id.sp_noti)
    AppCompatSpinner spOptions;
    private String[] mOptionsNoti;
   private ArrayAdapter<String> mAdapterOption;

     private Firebase mRoot;
    int choice;
    String[] themes;
    NotificationManager notificationManager;
    int posNotiDefault;
    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoot = new Firebase(Constant.URL_ROOT_FINAL+SharedPref.getInstance(getContext()).getString(Constant.KEY_URL_ID,""));
        themes = getContext().getResources().getStringArray(R.array.item_theme);
        notificationManager  = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        mLogout = (LinearLayout) view.findViewById(R.id.ll_logout);
        mLayoutConnect = (LinearLayout) view.findViewById(R.id.ll_connect);
        mLayoutNotify = (LinearLayout) view.findViewById(R.id.ll_notification);
        mLayoutTheme = (LinearLayout) view.findViewById(R.id.ll_theme);
        AutofitHelper.create(mTvName);
        mLogout.setOnClickListener(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            String id = auth.getCurrentUser().getEmail();
            id = id.replace(".", "");
            id = id.replace("#", "");
            id = id.replace("$", "");
            id = id.replace("]", "");
            mTvName.setText(id);
        }
        boolean on= SharedPref.getInstance(getContext()).getBoolean(Constant.KEY_ON_SERVICE,true);
        if(on){
            mTvSW.setText(getContext().getResources().getString(R.string.copy_copy_is_active));
        }else{
            mTvSW.setText(getContext().getResources().getString(R.string.copy_copy_is_off));
        }
        mSwitchOn.setChecked(on);
        mCbNotify.setChecked(SharedPref.getInstance(getContext()).getBoolean(Constant.KEY_NOTIFY,true));

        mSwitchOn.setOnCheckedChangeListener(this);
        mCbNotify.setOnCheckedChangeListener(this);
        mLayoutOn.setOnClickListener(this);
        mLayoutDelete.setOnClickListener(this);
        mLayoutUpgrade.setOnClickListener(this);
        mLayoutConnect.setOnClickListener(this);
        mLayoutNotify.setOnClickListener(this);
        mLayoutTheme.setOnClickListener(this);
        mLayoutRate.setOnClickListener(this);
        mlayoutContact.setOnClickListener(this);
        mLayoutMoreApp.setOnClickListener(this);
        mLayoutNotiCopied.setOnClickListener(this);
        mOptionsNoti = getContext().getResources().getStringArray(R.array.item_noti);
        mAdapterOption = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,mOptionsNoti);
        mAdapterOption.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOptions.setAdapter(mAdapterOption);
        posNotiDefault = SharedPref.getInstance(getContext()).getInt(Constant.KEY_NOTIFY_WHEN_COPIED,0);
        spOptions.setSelection(posNotiDefault);
        spOptions.setOnItemSelectedListener(this);

/*        night mode
        int modeType;
        modeType = AppCompatDelegate.getDefaultNightMode();
        choice= modeType;
        int theme = SharedPref.getInstance(getContext()).getInt(Constant.KEY_THEME,0);
        choice = theme;
       // ((MainActivity)getActivity()).applyTheme(theme);
        mTvTheme.setText(themes[theme]);

        */




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
            case R.id.ll_contact:{
                CommonUtils.shareEmail("duongkk.dev@gmail.com",getContext());
                break;
            }
            case R.id.ll_moreapp:{
                CommonUtils.launchMoreAppMarket(getContext());
                break;
            }
            case R.id.ll_rateapp:{
                CommonUtils.launchMarket(getContext(),getContext().getPackageName());
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
                        Toast.makeText(getContext(), R.string.deleted,Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case R.id.ll_upgrade:{
                CommonUtils.launchMarket(getContext(),getContext().getPackageName()+"pro");
                break;
            }
            case R.id.ll_notification_coppied:{
                spOptions.performClick();
                break;
            }
            case R.id.ll_theme:{
            /*   new MaterialDialog.Builder(getContext()).title(R.string.night_mode_scheule)
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

                break;*/
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
                    mTvSW.setText(getContext().getResources().getString(R.string.copy_copy_is_active));
                    ((MainActivity)getActivity()).startMyService();
                }else{
                    ((MainActivity)getActivity()).stopMyService();
                    mTvSW.setText(getContext().getResources().getString(R.string.copy_copy_is_off));

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
                    notificationManager.cancel(1);      // 1 is id of notification in CommonUtils.java
                }
                break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        if(pos == posNotiDefault) return;
        SharedPref.getInstance(getContext()).putInt(Constant.KEY_NOTIFY_WHEN_COPIED,pos);

        switch (pos){
            case 0:{

                notificationManager.cancel(2);
                return;
            }
            case 1:{
                Toast.makeText(getContext(),getString(R.string.enabled_notification_copying),Toast.LENGTH_LONG).show();
                return;
            }
            case 2:{
                CommonUtils.createNotificationWithMsg(getContext(),getString(R.string.enabled_notification_copying));
                return;
            }
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
