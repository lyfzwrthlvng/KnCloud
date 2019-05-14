package com.shrey.kc.kcui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shrey.kc.kcui.entities.KCReadRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.workerActivities.AsyncCall;

import java.util.ArrayList;

public class ViewTags extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tags);


    }

    private void fillViewsWithTags() {
        Intent cardDataIntent = getIntent();
        ArrayList<String> knows = cardDataIntent.getStringArrayListExtra("knowledges");
        if(result == null || result.getResult() == null || result.getResult().get("Tags") == null) {
            // perhaps fillup a default card saying no result
            return;
        }
        String[] tags = (String[]) result.getResult().get("Tags");
        ScrollView sv = findViewById(R.id.root_vertical_container_for_tags);
        for(String tag: tags) {
            CardView cv = (CardView) getLayoutInflater().inflate(getResources().getLayout(R.layout.knowledge_card), null);
            TextView tv = cv.findViewById(R.id.text_view_in_card);
            tv.setText(tag);
            sv.addView(cv);
            // add on click listener for the cardView
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // search knowledge for this tag

                    AsyncCall.startActionRead(getApplicationContext(), readRequest);
                }
            });
        }


    }
}
