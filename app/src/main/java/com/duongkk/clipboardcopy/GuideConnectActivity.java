package com.duongkk.clipboardcopy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.duongkk.clipboardcopy.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuideConnectActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.code_connect)
    TextView mTvCode;
    @Bind(R.id.img_guide)
    ImageView mImgGuide;
    boolean finishable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent =getIntent();
        if(intent!=null) finishable = intent.getBooleanExtra(Constant.KEY_FINISH,true);
        setContentView(R.layout.activity_guide_connect);
        ButterKnife.bind(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            mTvCode.setText(String.format(getString(R.string.code_user_setting),auth.getCurrentUser().getUid().toString()));
        }
       // mImgGuide.startAnimation(AnimationUtils.loadAnimation(this,R.anim.scale_up));
       // mTvCode.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in));

    }

    @Override
    public void onClick(View view) {
       if(finishable) {
           finish();
       }else{
           startActivity(new Intent(this,MainActivity.class));
           finish();
       }
    }
}
