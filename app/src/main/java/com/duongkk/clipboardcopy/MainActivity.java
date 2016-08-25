package com.duongkk.clipboardcopy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.duongkk.clipboardcopy.databases.DatabaseHandler;
import com.duongkk.clipboardcopy.fragments.ChatFragment;
import com.duongkk.clipboardcopy.fragments.FavouriteFragment;
import com.duongkk.clipboardcopy.fragments.SettingFragment;
import com.duongkk.clipboardcopy.models.Message;
import com.duongkk.clipboardcopy.service.ClipboardListener;
import com.duongkk.clipboardcopy.utils.Constant;
import com.duongkk.clipboardcopy.utils.SharedPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    Intent intent;
    TabLayout tabLayout;
    int[] tabIcons ={R.drawable.ic_chat_tab,R.drawable.ic_fav_tab,R.drawable.ic_setting_tab};

   public  DatabaseHandler db;
    public List<Message> listMessages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if(checkSelfPermission(Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
//            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},100);
//        }
        setContentView(R.layout.activity_main);
        db=new DatabaseHandler(this);
        listMessages=new ArrayList<>();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
         tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                if(position==1){
                    Fragment fragment = ((SectionsPagerAdapter)mViewPager.getAdapter()).getFragment(position);

                    if (position ==1 && fragment != null)
                    {

                        fragment.onResume();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupTabIcons();
        intent = new Intent(this, ClipboardListener.class);

        startMyService();
    }
    public void startMyService(){
        if(SharedPref.getInstance(this).getBoolean(Constant.KEY_ON_SERVICE,true))
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

    public void stopMyService(){
        stopService(intent);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
         HashMap<Integer,String> mFragmentTags = new HashMap<Integer,String>();
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
        public Fragment getFragment(int position) {
//            String tag = mFragmentTags.get(position);
//            if (tag == null)
//                return null;
            if(position==1) return new FavouriteFragment();
            return null;
        }
        @Override
        public Fragment getItem(int position) {
           switch (position){
               case 0:{

                   return new ChatFragment();

               }
               case 1:{
                   FavouriteFragment favouriteFragment = new FavouriteFragment();
                   return favouriteFragment;

               }
               case 2:{

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
