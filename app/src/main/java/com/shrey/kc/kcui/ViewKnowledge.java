package com.shrey.kc.kcui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shrey.kc.kcui.objects.RuntimeConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ViewKnowledge extends AppCompatActivity {

    public static boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ViewKnowledge.class.getName(), "started activity");
        setContentView(R.layout.activity_view_knowledge);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inflateCardsAndDisplay();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(getParent() == null) {
            // err, maybe goto home
            //startActivity(new Intent(null, LoggedInHome.class));
            Log.w(LoggedInAddKnowledge.class.getName(), "parent is not there@");

        }
        //Log.d(LoggedInAddKnowledge.class.getName(), "back to:" + getParent().getLocalClassName());
        // finish without anything
        //Intent knowledgeResultIntent = new Intent();
        setResult(RuntimeConstants.INSTANCE.STARTED_ACTIVITY_RESULT_NOOP);
        finish();
        return true;
    }

    private void inflateCardsAndDisplay() {
        Intent cardDataIntent = getIntent();
        ArrayList<String> knows = cardDataIntent.getStringArrayListExtra("knowledges");

        LinearLayout ll = findViewById(R.id.root_vertical_container);
        ll.removeAllViews();
        // TODO? can we ever cross this? will it create issues?
        int id = 99;
        for(String param: knows) {
            Log.d(ViewKnowledge.class.getName(), param);
            CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.knowledge_card,null);
            id += 1;
            cardView.setId(id);
            TextView tv = cardView.findViewById(R.id.text_view_in_card);
            TextView tvp = cardView.findViewById(R.id.text_view_in_card_placeholder);
            String text = param;
            if(text.length() > 20) {
                tv.setCompoundDrawables(null, null, new ClipDrawable(getDrawable(android.R.drawable.status_bar_item_app_background),0,0),null);
            }
            tv.setText(text);
            ll.addView(cardView);
            final ViewKnowledge activityRef = this;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView thisTv = (TextView)v;
                    Intent detailsOfKnowledge = new Intent(activityRef, KnowledgeDetails.class);
                    detailsOfKnowledge.putExtra("knowledge", thisTv.getText());
                    startActivity(detailsOfKnowledge);
                }
            });

        }
        ll.setFocusable(true);
        ll.requestFocus();

    }

    @Override
    public void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isActive = false;
    }
}