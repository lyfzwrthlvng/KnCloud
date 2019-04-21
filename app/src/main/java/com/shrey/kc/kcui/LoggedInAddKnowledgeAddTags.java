package com.shrey.kc.kcui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;

import static android.view.KeyEvent.KEYCODE_ENTER;

public class LoggedInAddKnowledgeAddTags extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_add_knowledge_add_tags);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setListeners();
    }

    private void setListeners() {
        EditText et = findViewById(R.id.add_tag_text_tags_text);
        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText et = (EditText)v;
                if(keyCode == KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    // return this result to previous activity
                    // put result in intent and finish!
                    String tagsGlued = et.getText().toString();
                    ArrayList<String> tags = (ArrayList<String>) Arrays.asList(tagsGlued.split(","));
                    Intent resultIntent = new Intent();
                    resultIntent.putStringArrayListExtra("tagsForKnowledge", tags);
                    //setResult(2,intent);
                    setResult(0,resultIntent);
                    finish();
                }
                return true;
            }
        });
    }
}
