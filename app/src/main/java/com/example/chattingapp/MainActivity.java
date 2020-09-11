package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
     private Toolbar toolbar;
     private ViewPager viewPager;
     private TabLayout tab;
     private TabsAccesser tabsAccesser;
     private FirebaseUser currentUser;
     private FirebaseAuth mAuth;
     private DatabaseReference rootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
  rootRef= FirebaseDatabase.getInstance().getReference();
        toolbar=(Toolbar) findViewById(R.id.main_page_toolbar);
         setSupportActionBar(toolbar);
       getSupportActionBar().setTitle("whatsapp");

       viewPager=(ViewPager)findViewById(R.id.main_tabs_viewer);
       tab=(TabLayout)findViewById(R.id.main_tabs);
       tabsAccesser=(new TabsAccesser(getSupportFragmentManager()));
       viewPager.setAdapter(tabsAccesser);
       tab.setupWithViewPager(viewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser==null){
            sendUserToLoginActivity();
        }else{
            verifyUserExistance();
        }

    }

    private void verifyUserExistance() {
    String useruid=mAuth.getCurrentUser().getUid();
    rootRef.child("Users").child(useruid).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.child("name").exists()){
                Toast.makeText(MainActivity.this, "Welcome..", Toast.LENGTH_SHORT).show();
            }
            else{
                sendUserToSetttingsActivity();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

    }

    private void sendUserToLoginActivity() {

        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       super.onCreateOptionsMenu(menu);
       getMenuInflater().inflate(R.menu.options_menu,menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
    if(item.getItemId()== R.id.main_logout){
        mAuth.signOut();
        sendUserToLoginActivity();
    }if(item.getItemId()== R.id.main_findfrnds){

        }
        if(item.getItemId()== R.id.main_createGroup){
        requestNewGroup();
        }
        if(item.getItemId()== R.id.main_settings){
        sendUserToSetttingsActivity();
        }

return true;
    }

    private void requestNewGroup() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Enter Group Name :");
        final EditText groupname=new EditText(MainActivity.this);
        groupname.setHint("e.g Android Team");
        builder.setView(groupname);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String grounme=groupname.getText().toString();
                if(TextUtils.isEmpty(grounme)){
                    Toast.makeText(MainActivity.this, "Please,write your group name..", Toast.LENGTH_SHORT).show();
                }else {
                    CreateGroupName(grounme);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();

    }

    private void CreateGroupName(final String grounme) {
    rootRef.child("Group").child(grounme).setValue("")
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(MainActivity.this, grounme+" group created Successfully..", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void sendUserToSetttingsActivity() {
        Intent Sintent=new Intent(MainActivity.this,SettingsActivity.class);
        Sintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(Sintent);
        finish();

    }
}