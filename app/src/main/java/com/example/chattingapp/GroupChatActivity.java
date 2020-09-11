package com.example.chattingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

public class GroupChatActivity extends AppCompatActivity {
     private Toolbar mtoolbar;
     private ImageButton sendmsgBtn;
     private EditText userMsg;
     private ScrollView scrollView;
     private TextView displayText;
     private String currentGroupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        currentGroupName=getIntent().getExtras().get("groupName").toString();
        Toast.makeText(this, currentGroupName, Toast.LENGTH_SHORT).show();
    initializeFields();
    }

    private void initializeFields() {
    mtoolbar=(Toolbar)findViewById(R.id.group_chat_barLayout);
    setSupportActionBar(mtoolbar);
    getSupportActionBar().setTitle(currentGroupName);
    sendmsgBtn=(ImageButton)findViewById(R.id.send_button);
    displayText=(TextView)findViewById(R.id.group_chat_textdisplay);
    userMsg=(EditText)findViewById(R.id.input_group_msg);
    scrollView=(ScrollView)findViewById(R.id.my_scrolll_view);
    }
}