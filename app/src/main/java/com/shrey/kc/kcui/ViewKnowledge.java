package com.shrey.kc.kcui;

import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shrey.kc.kcui.activities.KCUIActivity;
import com.shrey.kc.kcui.adaptors.KnowledgeCardAdapter;
import com.shrey.kc.kcui.entities.KnowledgeOrTag;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.objects.RuntimeConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewKnowledge extends KCUIActivity {

    public static boolean isActive = false;
    private KnowledgeCardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ViewKnowledge.class.getName(), "started activity");
        setContentView(R.layout.activity_view_knowledge);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //registerListeners();
        //addListeners(layout1, action);
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

    private void addListeners(ConstraintLayout root, String action) {
        Button deleteButton = root.findViewById(R.id.delete_knowledge_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void inflateCardsAndDisplay() {
        Intent cardDataIntent = getIntent();
        ArrayList<String> knows = cardDataIntent.getStringArrayListExtra("knowledges");
        String tag = cardDataIntent.getStringExtra("tag");
        List<KnowledgeOrTag> kot = knows.stream().map(know -> new KnowledgeOrTag(tag, know, false)).collect(Collectors.toList());
        String currentTag = cardDataIntent.getStringExtra("tag");
        Log.d(ViewKnowledge.class.getName(), "displaying knowledges for " + currentTag);

        //LinearLayout ll = findViewById(R.id.root_vertical_container);
        cardAdapter = new KnowledgeCardAdapter(kot, RuntimeConstants.INSTANCE.LARGE_HEIGHT,
                false);
        RecyclerView recyclerView = findViewById(R.id.scroll_knowledge_all);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();
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

    @Override
    public void handleBroadcastResult(NodeResult result, String action) {

    }
}
