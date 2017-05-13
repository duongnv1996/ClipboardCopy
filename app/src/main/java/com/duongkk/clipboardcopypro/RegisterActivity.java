package com.duongkk.clipboardcopypro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.duongkk.clipboardcopypro.utils.CommonUtils;
import com.duongkk.clipboardcopypro.utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
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

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_goto_login: {
                finish();
                break;
            }
            case R.id.btn_register: {
                CommonUtils.hideKeyBroad(this, mEdtPassword);
                final String email = mEdtEmail.getText().toString();
                final String pass = mEdtPassword.getText().toString();
                String repass = mEdtRePassword.getText().toString();

                if (email.isEmpty()) {
                    new MaterialDialog.Builder(this)
                            .title(R.string.missing_data)
                            .content(R.string.please_enter_email)
                            .positiveText(getString(R.string.dimiss))                             .positiveColor(Color.GRAY)

                            .show();
                    return;
                }
                if (!email.contains("@")) {
                    new MaterialDialog.Builder(this)
                            .title(R.string.missing_data)
                            .content(R.string.emaill_incorrectly)
                            .positiveText(getString(R.string.dimiss))                             .positiveColor(Color.GRAY)

                            .show();
                    return;
                }
                if (pass.isEmpty()) {
                    new MaterialDialog.Builder(this)
                            .title(R.string.missing_data)
                            .content(R.string.enter_pass)
                            .positiveText(getString(R.string.dimiss))                             .positiveColor(Color.GRAY)

                            .show();
                    return;
                }
                if (pass.length() < 6) {
                    new MaterialDialog.Builder(this)
                            .title(R.string.missing_data)
                            .content(R.string.pass_incorrectly)
                            .positiveText(getString(R.string.dimiss))                             .positiveColor(Color.GRAY)

                            .show();
                    return;
                }
                if (!pass.equals(repass)) {
                    new MaterialDialog.Builder(this)
                            .title(R.string.missing_data)
                            .content(R.string.repass_incorrectly)
                            .positiveText(getString(R.string.dimiss))                             .positiveColor(Color.GRAY)

                            .show();
                    return;
                }

                mProgress.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mProgress.setVisibility(View.GONE);
                                //Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                if (!task.isSuccessful()) {
                                    new MaterialDialog.Builder(RegisterActivity.this)
                                            .title(R.string.auth_failt)
                                            .content(task.getException().getMessage())
                                            .positiveText(getString(R.string.dimiss))                             .positiveColor(Color.GRAY)

                                            .show();
                                } else {
                                    //Firebase root = new Firebase(Constant.URL_ROOT + "users/"+mAuth.getCurrentUser().getUid()+"/");


                                            Toast.makeText(RegisterActivity.this, R.string.success_reg, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent();
                                            intent.putExtra(Constant.KEY_EMAIL, email);
                                            intent.putExtra(Constant.KEY_PASS, pass);
                                            setResult(RESULT_OK, intent);
                                            finish();

                                    //  startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

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
