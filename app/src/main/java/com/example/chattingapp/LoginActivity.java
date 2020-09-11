package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseUser currentUser;
   private EditText emaillogin,pswrdLogin;
   private TextView forgetPswrd,needAccount;
   private Button loginBtn,loginPhone;
    private FirebaseAuth mAuth;

    private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgetPswrd=(TextView)findViewById(R.id.Forget_pswrd);
        needAccount=(TextView)findViewById(R.id.need_new_acnt);
        emaillogin=(EditText)findViewById(R.id.login_email);
        pswrdLogin=(EditText)findViewById(R.id.login_pswrd);
        loginBtn=(Button)findViewById(R.id.login_btn);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        loadingbar=new ProgressDialog(this);
    loginPhone=(Button)findViewById(R.id.login_using_mobile);
    loginBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
      alloUserToLogin();
        }
    });
    loginPhone.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
         Intent intent=new Intent(LoginActivity.this,PhoneLoginActivity.class);
        startActivity(intent);
        }
    });
        forgetPswrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        needAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToRegisterActivity();
            }
        });

    }

    private void alloUserToLogin() {
        String email=emaillogin.getText().toString();
        String pswrd=pswrdLogin.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please write your mailid", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(pswrd)){
            Toast.makeText(this, "Please write your Password", Toast.LENGTH_SHORT).show();
        }else{
            loadingbar.setTitle("Sign in");
            loadingbar.setMessage("Please wait...");
            loadingbar.setCanceledOnTouchOutside(true);
      mAuth.signInWithEmailAndPassword(email,pswrd)
              .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          sendUserToMainActivity();
                          Toast.makeText(LoginActivity.this, "Loggedin successfully..", Toast.LENGTH_SHORT).show();
                          loadingbar.dismiss();
                      }else{
                          String messag=task.getException().toString();
                          Toast.makeText(LoginActivity.this, "Error:"+messag, Toast.LENGTH_SHORT).show();
                          loadingbar.dismiss();
                      }
                  }
              });loadingbar.show();
        }}



    private void sendUserToMainActivity() {
        Intent mintent=new Intent(LoginActivity.this,MainActivity.class);
        mintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mintent);
        finish();
    }

    private void sendUserToRegisterActivity() {

        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

}