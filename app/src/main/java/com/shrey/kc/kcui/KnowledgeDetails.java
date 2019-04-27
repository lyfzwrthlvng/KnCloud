package com.shrey.kc.kcui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.shrey.kc.kcui.objects.RuntimeConstants;

public class KnowledgeDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fillUpText();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Log.d(LoggedInAddKnowledge.class.getName(), "back");
        // finish without anything
        //Intent knowledgeResultIntent = new Intent();
        setResult(RuntimeConstants.INSTANCE.STARTED_ACTIVITY_RESULT_NOOP);
        finish();
        return true;
    }

    private void fillUpText() {
        Intent intent = getIntent();
        String knowledge = intent.getStringExtra("knowledge");
        TextView tv = findViewById(R.id.text_knowledge_details);
        tv.setText(knowledge);
    }
}
