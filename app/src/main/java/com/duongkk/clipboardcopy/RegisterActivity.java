package com.duongkk.clipboardcopy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.duongkk.clipboardcopy.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.edt_email)
     EditText mEdtEmail;
    @Bind(R.id.edt_password)
    EditText mEdtPassword;
    @Bind(R.id.edt_repassword)
    EditText mEdtRePassword;
    @Bind(R.id.btn_register)
     Button mBtnReg;
    @Bind(R.id.btn_goto_login)
     Button mBtnLogin;
    @Bind(R.id.btn_resetpass)
     Button mBtnResetPass;
    @Bind(R.id.progressBar)
     ProgressBar mProgress;


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:{
                CommonUtils.hideKeyBroad(this,mEdtPassword);
                String email = mEdtEmail.getText().toString();
                String pass = mEdtPassword.getText().toString();
                String repass = mEdtRePassword.getText().toString();

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
                if(pass.isEmpty()){
                    new MaterialDialog.Builder(this)
                            .title(R.string.missing_data)
                            .content(R.string.enter_pass)
                            .show();
                    return;
                }
                if(pass.length()<6){
                    new MaterialDialog.Builder(this)
                            .title(R.string.missing_data)
                            .content(R.string.pass_incorrectly)
                            .show();
                    return;
                }
                if(!pass.equals(repass)){
                    new MaterialDialog.Builder(this)
                            .title(R.string.missing_data)
                            .content(R.string.repass_incorrectly)
                            .show();
                    return;
                }

                mProgress.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mProgress.setVisibility(View.GONE);
                                //Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                if (!task.isSuccessful()) {
                                    new MaterialDialog.Builder(RegisterActivity.this)
                                            .title(R.string.auth_failt)
                                            .content(task.getException().getMessage())
                                            .show();
                                } else {
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
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