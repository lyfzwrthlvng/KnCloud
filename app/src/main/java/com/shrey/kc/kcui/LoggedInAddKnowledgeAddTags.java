package com.shrey.kc.kcui;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.shrey.kc.kcui.objects.RuntimeConstants;
import com.shrey.kc.kcui.workerActivities.AsyncCall;
import com.shrey.kc.kcui.workerActivities.ServiceBcastReceiver;

import java.util.ArrayList;
import java.util.Arrays;

import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.KeyEvent.KEYCODE_SPACE;

public class LoggedInAddKnowledgeAddTags extends AppCompatActivity {

    ServiceBcastReceiver serviceBcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_add_knowledge_add_tags);
    }

    @Override
    protected void onStart() {
        serviceBcastReceiver = new ServiceBcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AsyncCall.ACTION_FETCH_TAGS);
        registerReceiver(serviceBcastReceiver, intentFilter);
        super.onStart();
        setListeners();
    }

    private void setListeners() {
        final EditText et = findViewById(R.id.add_tag_text_tags_text);
        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText et = (EditText)v;
                if(keyCode == KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    // return this result to previous activity
                    // put result in intent and finish!
                    String tagsGlued = et.getText().toString();
                    ArrayList<String> tags = new ArrayList<>(Arrays.asList(tagsGlued.split(" ")));
                    Intent resultIntent = new Intent();
                    resultIntent.putStringArrayListExtra("tagsForKnowledge", tags);
                    //setResult(2,intent);
                    setResult(RuntimeConstants.INSTANCE.STARTED_ACTIVITY_RESULT_GOOD,resultIntent);
                    finish();
                } else if(keyCode == KEYCODE_SPACE && event.getAction() == KeyEvent.ACTION_UP) {
                    // add this to list of tags

                }
                return true;
            }
        });

        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    et.setText(null);
                }
            }
        });

        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText(null);
            }
        });
    }
}
