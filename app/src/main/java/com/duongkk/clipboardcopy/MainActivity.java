package com.duongkk.clipboardcopy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.duongkk.clipboardcopy.fragments.ChatFragment;
import com.duongkk.clipboardcopy.fragments.FavouriteFragment;
import com.duongkk.clipboardcopy.fragments.SettingFragment;
import com.duongkk.clipboardcopy.service.ClipboardListener;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        startService(new Intent(this, ClipboardListener.class));
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           switch (position){
               case 0:{
                   return new ChatFragment();

               }
               case 1:{
                   return new FavouriteFragment();

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
            switch (position) {
                case 0:
                    return "ChatFragment";
                case 1:
                    return "FavouriteFragment";
                case 2:
                    return "SettingFragment";
            }
            return null;
        }
    }
}
