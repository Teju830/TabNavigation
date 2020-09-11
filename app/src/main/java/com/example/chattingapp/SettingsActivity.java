package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private EditText setname,setstatus;
    private Button updatefield;
    private CircleImageView imageView;


    private String  currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth= FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        rootRef= FirebaseDatabase.getInstance().getReference();


        setname=(EditText)findViewById(R.id.setName);
        setstatus=(EditText)findViewById(R.id.setDes);
        imageView=(CircleImageView)findViewById(R.id.profile_image);
         updatefield=(Button)findViewById(R.id.updte_fields);

         updatefield.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
    updateSettings();
             }
         });
         retrieveUserInfo();
    }

    private void retrieveUserInfo() {
    rootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists() && snapshot.hasChild("name") && snapshot.hasChild("image")){
                String username=snapshot.child("name").getValue().toString();
                String userstatus=snapshot.child("status").getValue().toString();
                String userimage=snapshot.child("image").getValue().toString();

                setname.setText(username);
                setstatus.setText(userstatus);

            }else if (snapshot.exists() && snapshot.hasChild("name")){
                String username=snapshot.child("name").getValue().toString();
                String userstatus=snapshot.child("status").getValue().toString();


                setname.setText(username);
                setstatus.setText(userstatus);
            }else {
                Toast.makeText(SettingsActivity.this, "Please Update your profile...", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });    }

    private void updateSettings() {
    String name=setname.getText().toString();
    String status=setstatus.getText().toString();
    if(TextUtils.isEmpty(name)){
        Toast.makeText(this, "please write your name", Toast.LENGTH_SHORT).show();
    }else if(TextUtils.isEmpty(status)){
        Toast.makeText(this, "put your status", Toast.LENGTH_SHORT).show();
    }else {
        HashMap<String,String>setfiledsMap=new HashMap<>();
        setfiledsMap.put("uid",currentUserId);
        setfiledsMap.put("name",name);
        setfiledsMap.put("status",status);
        rootRef.child("Users").child(currentUserId).setValue(setfiledsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                 sendUserToMainActivity();
                    Toast.makeText(SettingsActivity.this, "Profile updated successfully..", Toast.LENGTH_SHORT).show();
                }else {
                    String msg=task.getException().toString();
                    Toast.makeText(SettingsActivity.this, "Error:"+msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    }
    private void sendUserToMainActivity() {
        Intent mintent=new Intent(SettingsActivity.this,MainActivity.class);
        mintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mintent);
        finish();
    }
}