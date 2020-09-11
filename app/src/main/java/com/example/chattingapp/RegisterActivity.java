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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
  private EditText emailReg,pswrdRed;
  private Button createaccoun;  private TextView haveanAcnt;
  private FirebaseAuth mAuth;
  private DatabaseReference rootRef;

  private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
rootRef= FirebaseDatabase.getInstance().getReference();
        haveanAcnt=(TextView)findViewById(R.id.have_an_acnt);
        emailReg=(EditText)findViewById(R.id.register_email);
        pswrdRed=(EditText)findViewById(R.id.register_pswrd);
        createaccoun=(Button)findViewById(R.id.register_btn);
        mAuth=FirebaseAuth.getInstance();
       loadingbar=new ProgressDialog(this);
        createaccoun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           createUserAccount();
            }
        });
        haveanAcnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToLoginActivity();
            }
        });
    }

    private void createUserAccount() {
        String email=emailReg.getText().toString();
        String pswrd=pswrdRed.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please write your mailid", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(pswrd)){
            Toast.makeText(this, "Please write Password", Toast.LENGTH_SHORT).show();
        }else{
            loadingbar.setTitle("Creating New Account");
            loadingbar.setMessage("Please wait,While we are creating new account for you..");
            loadingbar.setCanceledOnTouchOutside(true);
            mAuth.createUserWithEmailAndPassword(email,pswrd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String currentUserid=mAuth.getCurrentUser().getUid();
                                rootRef.child("Users").child(currentUserid).setValue("");

                                sendUserToMainActivity();
                                Toast.makeText(RegisterActivity.this, "Account created successfully..", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            }else {
                                String messag=task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error:"+messag, Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            }
                        }
                    });loadingbar.show();
        }
    }

    private void sendUserToMainActivity() {
        Intent mintent=new Intent(RegisterActivity.this,MainActivity.class);
        mintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mintent);
        finish();
    }

    private void sendUserToLoginActivity() {
        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}