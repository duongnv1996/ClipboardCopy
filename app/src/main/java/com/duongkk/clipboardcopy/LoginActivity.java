package com.duongkk.clipboardcopy;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.duongkk.clipboardcopy.application.AppController;
import com.duongkk.clipboardcopy.utils.CommonUtils;
import com.duongkk.clipboardcopy.utils.Constant;
import com.duongkk.clipboardcopy.utils.SharedPref;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.edt_email)
    EditText mEdtEmail;
    @Bind(R.id.edt_password)
    EditText mEdtPassword;
    @Bind(R.id.btn_login)
    Button mBtnLogin;
    @Bind(R.id.btn_goto_register)
    Button mBtnGoReg;
    @Bind(R.id.btn_resetpass)
    Button mBtnResetPass;
    @Bind(R.id.progressBar)
    ProgressBar mProgress;
    @Bind(R.id.logo)
    ImageView mImgLogo;

    private FirebaseAuth mAuth;
    private Firebase mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE}, 100);
        } else {
            AppController.getInstance().getImei();
        }

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null && !SharedPref.getInstance(this).getString(Constant.KEY_URL_ID, "").equals("")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mImgLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_up_logo));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            AppController.getInstance().getImei();
        }else{
            Toast.makeText(this, R.string.right,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.CODE_REG && resultCode == RESULT_OK && data != null) {
            String email = data.getStringExtra(Constant.KEY_EMAIL);
            String pass = data.getStringExtra(Constant.KEY_PASS);
            mEdtEmail.setText(email);
            mEdtPassword.setText(pass);
        }
        if (requestCode == Constant.CODE_RESET && resultCode == RESULT_OK) {
            new MaterialDialog.Builder(LoginActivity.this)
                    .title(R.string.success)
                    .content(R.string.sent_email)
                    .show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_goto_register: {
                startActivityForResult(new Intent(this, RegisterActivity.class), Constant.CODE_REG);
                break;
            }
            case R.id.btn_resetpass: {
                startActivityForResult(new Intent(this, ForgotPasswordActivity.class), Constant.CODE_RESET);
                break;
            }
            case R.id.btn_login: {
                CommonUtils.hideKeyBroad(this, mEdtPassword);
                String email = mEdtEmail.getText().toString();
                String pass = mEdtPassword.getText().toString();

                if (email.isEmpty()) {
                    new MaterialDialog.Builder(this)
                            .title(R.string.missing_data)
                            .content(R.string.please_enter_email)
                            .show();
                    return;
                }
                if (!email.contains("@")) {
                    new MaterialDialog.Builder(this)
                            .title(R.string.missing_data)
                            .content(R.string.emaill_incorrectly)
                            .show();
                    return;
                }
                if (pass.isEmpty()) {
                    new MaterialDialog.Builder(this)
                            .title(R.string.missing_data)
                            .content(R.string.enter_pass)
                            .show();
                    return;
                }
                if (pass.length() < 6) {
                    new MaterialDialog.Builder(this)
                            .title(R.string.missing_data)
                            .content(R.string.pass_incorrectly)
                            .show();
                    return;
                }
                mProgress.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mProgress.setVisibility(View.GONE);
                                //Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                if (!task.isSuccessful()) {
                                    new MaterialDialog.Builder(LoginActivity.this)
                                            .title(R.string.auth_failt)
                                            .content(task.getException().getMessage())
                                            .show();
                                } else {
                                    final String id = getGreatIdString();

                                    mRoot = new Firebase(Constant.URL_ROOT_FINAL+"users/" + id + "/");
                                    mRoot.child("todo").setValue("1", new Firebase.CompletionListener() {
                                        @Override
                                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                            if(firebaseError==null){
                                                SharedPref.getInstance(getBaseContext()).putString(Constant.KEY_URL_ID, "users/" + id + "/");

                                                Intent intent = new Intent(LoginActivity.this, GuideConnectActivity.class);
                                                intent.putExtra(Constant.KEY_FINISH, false);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                new MaterialDialog.Builder(LoginActivity.this)
                                                        .title(R.string.auth_failt)
                                                        .content(R.string.check_connect)
                                                        .show();
                                            }
                                        }
                                    });

                                }
                            }
                        });
                break;
            }
        }


    }

    private String getGreatIdString() {
        String id = mAuth.getCurrentUser().getEmail();
        id = id.replace(".", "");
        id = id.replace("#", "");
        id = id.replace("$", "");
        id = id.replace("]", "");
        return id;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgress.setVisibility(View.GONE);
    }
}
