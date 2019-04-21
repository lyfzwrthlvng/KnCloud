package com.shrey.kc.kcui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shrey.kc.kcui.objects.RuntimeConstants;

public class LoggedInAddKnowledge extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_add_knowledge);
        setListeners();
    }

    private void setListeners() {
        Button atb = findViewById(R.id.add_tag_text_button);
        final EditText kt = findViewById(R.id.add_tag_text);
        atb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddKnowledge", kt.getText().toString());
                // start an activity to tags for this knowledge
                Intent collectTagsIntent = new Intent(LoggedInAddKnowledge.this, LoggedInAddKnowledgeAddTags.class);
                startActivityForResult(collectTagsIntent, RuntimeConstants.INSTANCE.START_ACTIVITY_FOR_TAGS);
            }
        });
    }
}
