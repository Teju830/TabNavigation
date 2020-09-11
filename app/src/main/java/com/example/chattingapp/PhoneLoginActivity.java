package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {
    private EditText phoneNumber,code;
    private Button sendVerCode,verifyBtn;
    private DatabaseReference rootRef;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerification;
    private ProgressDialog loadingbar;
 private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
      mAuth=FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
      loadingbar=new ProgressDialog(this);
        phoneNumber=(EditText)findViewById(R.id.writePhone_number);
        code=(EditText)findViewById(R.id.verification_code);
        sendVerCode=(Button)findViewById(R.id.send_code_btn);
        verifyBtn=(Button)findViewById(R.id.verify_btn);

        sendVerCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone="+91"+phoneNumber.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    Toast.makeText(PhoneLoginActivity.this, "Please enter your Phone Number..", Toast.LENGTH_SHORT).show();
                }
                else{
                    loadingbar.setTitle("Phone Verification");
                    loadingbar.setMessage("Please wait,While we are authenticating your phone..");

                    loadingbar.setCanceledOnTouchOutside(false);
                    loadingbar.show();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phone,60, TimeUnit.SECONDS,PhoneLoginActivity.this,callbacks
                    );
                }
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phoneNumber.setVisibility(View.INVISIBLE);

                sendVerCode.setVisibility(View.INVISIBLE);
                String VerificationCode=code.getText().toString();
                if(TextUtils.isEmpty(VerificationCode)){
                    Toast.makeText(PhoneLoginActivity.this, "Please write verification code..", Toast.LENGTH_SHORT).show();
                }else {

                    loadingbar.setTitle("Verification Code");
                    loadingbar.setMessage("Please wait,While we are verifying your verification code..");

                    loadingbar.setCanceledOnTouchOutside(false);
                    loadingbar.show();

                    PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(mVerification,VerificationCode);
                    signInWithPhoneauthCredential(phoneAuthCredential);
                }

            }
        });

        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneauthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                loadingbar.dismiss();
                Toast.makeText(PhoneLoginActivity.this, "Invalid Phone Number..please,enter your phone number with country code..", Toast.LENGTH_SHORT).show();
                code.setVisibility(View.INVISIBLE);
                phoneNumber.setVisibility(View.VISIBLE);
                verifyBtn.setVisibility(View.INVISIBLE);
                sendVerCode.setVisibility(View.VISIBLE);

            }
            public void onCodeSent(String VerificationId,PhoneAuthProvider.ForceResendingToken token){

                mVerification=VerificationId;
                mResendToken=token;
                loadingbar.dismiss();
                Toast.makeText(PhoneLoginActivity.this, "code has been sent to your given phone number..", Toast.LENGTH_SHORT).show();
                code.setVisibility(View.VISIBLE);
                phoneNumber.setVisibility(View.INVISIBLE);
                verifyBtn.setVisibility(View.VISIBLE);
                sendVerCode.setVisibility(View.INVISIBLE);

            }
        };
    }

    private void signInWithPhoneauthCredential(PhoneAuthCredential credential){
     mAuth.signInWithCredential(credential)
             .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful()){
                         String currentUserid=mAuth.getCurrentUser().getUid();
                         rootRef.child("Users").child(currentUserid).setValue("");
                         loadingbar.dismiss();
                         Toast.makeText(PhoneLoginActivity.this, "Congratulations,you're logged in successfully..", Toast.LENGTH_SHORT).show();
                         sendUserToMainActivity();
                 }else {
                         String msg=task.getException().toString();
                         Toast.makeText(PhoneLoginActivity.this, "Error:"+msg, Toast.LENGTH_SHORT).show();
                     }
             }
             });



    }

    private void sendUserToMainActivity() {
        Intent intent=new Intent(PhoneLoginActivity.this,MainActivity.class);
        startActivity(intent);

    }
}