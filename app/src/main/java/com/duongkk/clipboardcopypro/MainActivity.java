package com.duongkk.clipboardcopypro;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.duongkk.clipboardcopypro.databases.DatabaseHandler;
import com.duongkk.clipboardcopypro.fragments.BaseFragment;
import com.duongkk.clipboardcopypro.fragments.ChatFragment;
import com.duongkk.clipboardcopypro.fragments.FavouriteFragment;
import com.duongkk.clipboardcopypro.fragments.SettingFragment;
import com.duongkk.clipboardcopypro.models.Message;
import com.duongkk.clipboardcopypro.service.ClipboardListener;
import com.duongkk.clipboardcopypro.service.ReceiverOff;
import com.duongkk.clipboardcopypro.utils.CommonUtils;
import com.duongkk.clipboardcopypro.utils.Constant;
import com.duongkk.clipboardcopypro.utils.SharedPref;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAnalytics mFirebaseAnalytics;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    Intent intent;
    TabLayout tabLayout;
    int[] tabIcons = {R.drawable.ic_chat_tab, R.drawable.ic_fav_tab, R.drawable.ic_setting_tab};
    public DatabaseHandler db;
    public List<Message> listMessages;
    ReceiverOff receiver = new ReceiverOff();
    BroadcastReceiver receiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (CommonUtils.YES_ACTION.equals(action)) {
                SharedPref.getInstance(context).putBoolean(Constant.KEY_ON_SERVICE, false);
                stopMyService();
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(1);
            }
        }
    };



//    private InterstitialAd mInterstitialAd;
    private MaterialDialog mDialogUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        int themePos = SharedPref.getInstance(this).getInt(Constant.KEY_THEME,0);
//        applyTheme(themePos);
        setContentView(R.layout.activity_main);


        mDialogUpdate =new MaterialDialog.Builder(this)
                .title(R.string.title_update)
                .content(getString(R.string.msg_update))
                .positiveText(getString(R.string.dimiss))
                .positiveColor(Color.GRAY)
                .build();
        if(!SharedPref.getInstance(this).getBoolean(Constant.KEY_DIALOG_UPDATE_OPENED,false)){
            mDialogUpdate.show();
            SharedPref.getInstance(this).putBoolean(Constant.KEY_DIALOG_UPDATE_OPENED,true);
        }
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, SharedPref.getInstance(this).getString(Constant.KEY_URL_ID,""));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, SharedPref.getInstance(this).getString(Constant.KEY_URL_ID,""));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        db = new DatabaseHandler(this);
        listMessages = new ArrayList<>();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        tabLayout.getTabAt(0).getIcon().setAlpha(255);
                        tabLayout.getTabAt(1).getIcon().setAlpha(128);
                        tabLayout.getTabAt(2).getIcon().setAlpha(128);
                        break;
                    case 1:
                        tabLayout.getTabAt(0).getIcon().setAlpha(128);
                        tabLayout.getTabAt(1).getIcon().setAlpha(255);
                        tabLayout.getTabAt(2).getIcon().setAlpha(128);
                        break;
                    case 2:
                        tabLayout.getTabAt(0).getIcon().setAlpha(128);
                        tabLayout.getTabAt(1).getIcon().setAlpha(128);
                        tabLayout.getTabAt(2).getIcon().setAlpha(255);
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                BaseFragment fragment = ((SectionsPagerAdapter) mViewPager.getAdapter()).getFragment(position);
                if(fragment==null) return;

                if(position==1 || position ==0){
                    fragment.onResume();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupTabIcons();
        intent = new Intent(this, ClipboardListener.class);


//        IntentFilter filter = new IntentFilter();
//        filter.addAction(CommonUtils.YES_ACTION);
//        registerReceiver(receiver1,filter);
        startMyService();
//        Night mode
//        if (savedInstanceState == null) {
//        int theme = SharedPref.getInstance(this).getInt(Constant.KEY_THEME,  AppCompatDelegate.MODE_NIGHT_AUTO);
//        applyTheme(theme);
//        }


//        Ads
//        MobileAds.initialize(getApplicationContext(),"ca-app-pub-4447279115464296~4239207165");
//
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-4447279115464296/4099606366");
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                requestNewInterstitial();
//                //beginPlayingGame();
//
//            }
//        });
//
//        requestNewInterstitial();
//        mInterstitialAd.setAdListener(new AdListener(){
//            public void onAdLoaded(){
//                displayInterstitial();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2 * 1000);
        }
    }
    //    public void displayInterstitial() {
//        if (mInterstitialAd.isLoaded())
//         mInterstitialAd.show();
//
//    }

//    private void requestNewInterstitial() {
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .build();
//
//        mInterstitialAd.loadAd(adRequest);
//
//
//    }
    public boolean needToUpdateChatFragment;
    public void updateViewPager(){
        needToUpdateChatFragment = true;
        mSectionsPagerAdapter.notifyDataSetChanged();
        setupTabIcons();
        tabLayout.setupWithViewPager(mViewPager);

    }
    @Override
    protected void onStop() {
        if(mDialogUpdate.isShowing()) mDialogUpdate.hide();
        super.onStop();
    }

    public void applyTheme(int theme) {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    public void startMyService() {
        if (SharedPref.getInstance(this).getBoolean(Constant.KEY_ON_SERVICE, true))
            startService(intent);
    }

    public List<Message> getListMessages() {
        return listMessages;
    }

    public void setListMessages(List<Message> listMessages) {
        this.listMessages = listMessages;
    }

    public DatabaseHandler getDb() {
        return db;
    }

    public void setDb(DatabaseHandler db) {
        this.db = db;
    }

    public void stopMyService() {
        stopService(intent);

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search:{
                startSearchActivity();
                break;
            }
        }
    }

    public void startSearchActivity() {
        Intent intent =new Intent(MainActivity.this,SearchActivity.class);
        Bundle bundle= new Bundle();
        //bundle.putParcelableArrayList(Constant.KEY_MSG,listMessages);
        intent.putExtra(Constant.KEY_BUNDLE,bundle);
        startActivity(intent);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        HashMap<Integer, String> mFragmentTags = new HashMap<Integer, String>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                // record the fragment tag here.
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }



        public BaseFragment getFragment(int position) {
//            String tag = mFragmentTags.get(position);
//            if (tag == null)
//                return null;
            if (position == 1) return new FavouriteFragment();
            if (position == 0) return new ChatFragment();
            return null;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {

                    return new ChatFragment();

                }
                case 1: {
                    FavouriteFragment favouriteFragment = new FavouriteFragment();
                    return favouriteFragment;

                }
                case 2: {

                    return new SettingFragment();

                }
                default:

                    return new ChatFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }


        @Override
        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "ChatFragment";
//                case 1:
//                    return "FavouriteFragment";
//                case 2:
//                    return "SettingFragment";
//            }
            return null;
        }
    }
}
