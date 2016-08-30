package com.duongkk.clipboardcopy;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.duongkk.clipboardcopy.application.AppController;
import com.duongkk.clipboardcopy.utils.Constant;
import com.duongkk.clipboardcopy.utils.SharedPref;
import com.firebase.client.Firebase;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuideConnectActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.code_connect)
    TextView mTvCode;
    @Bind(R.id.img_guide)
    ImageView mImgGuide;
    boolean finishable;
    private InterstitialAd mInterstitialAd;
    private Firebase mRoot;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            AppController.getInstance().getImei();
        }else{
            Toast.makeText(this, R.string.right,Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE},100);
        }else{
            AppController.getInstance().getImei();
        }
        Intent intent =getIntent();
        if(intent!=null) finishable = intent.getBooleanExtra(Constant.KEY_FINISH,true);
        setContentView(R.layout.activity_guide_connect);
        ButterKnife.bind(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            String id = auth.getCurrentUser().getEmail();
            id = id.replace(".", "");
            id = id.replace("#", "");
            id = id.replace("$", "");
            id = id.replace("]", "");
            mTvCode.setText(String.format(getString(R.string.code_user_setting),id));
        }
       // mImgGuide.startAnimation(AnimationUtils.loadAnimation(this,R.anim.scale_up));
       // mTvCode.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in));
//        Ads
        MobileAds.initialize(getApplicationContext(),"ca-app-pub-4447279115464296~4239207165");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4447279115464296/4099606366");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                //beginPlayingGame();

            }
        });

        requestNewInterstitial();
        mInterstitialAd.setAdListener(new AdListener(){
            public void onAdLoaded(){
                //displayInterstitial();
            }
        });


        mRoot = new Firebase(Constant.URL_ROOT_FINAL+ SharedPref.getInstance(this).getString(Constant.KEY_URL_ID,""));
        mRoot.child("todo").setValue("1");
    }


    public void displayInterstitial() {
        if (mInterstitialAd.isLoaded())
            mInterstitialAd.show();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);


    }

    @Override
    public void onClick(View view) {
       if(finishable) {
           displayInterstitial();
           finish();
       }else{
           startActivity(new Intent(this,MainActivity.class));
           finish();
       }
    }
}
