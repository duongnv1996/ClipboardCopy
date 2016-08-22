package com.duongkk.clipboardcopy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.duongkk.clipboardcopy.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.edt_email)
    EditText mEdtEmail;
    @Bind(R.id.progressBar)
    ProgressBar mProgress;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:{
               finish();
                break;
            }
            case R.id.btn_reset:{
                CommonUtils.hideKeyBroad(this,mEdtEmail);
                String email = mEdtEmail.getText().toString();

                if(email.isEmpty()){
                    new MaterialDialog.Builder(this)
                            .title(R.string.missing_data)
                            .content(R.string.please_enter_email)
                            .show();
                    return;
                }
                if(!email.contains("@")){
                    new MaterialDialog.Builder(this)
                            .title(R.string.missing_data)
                            .content(R.string.emaill_incorrectly)
                            .show();
                    return;
                }
                mProgress.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mProgress.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            new MaterialDialog.Builder(ForgotPasswordActivity.this)
                                    .title(R.string.auth_failt)
                                    .content(task.getException().getMessage())
                                    .show();
                        } else {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });

                break;
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgress.setVisibility(View.GONE);
    }
}
