package com.shrey.kc.kcui;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.shrey.kc.kcui.activities.KCUIActivity;
import com.shrey.kc.kcui.adaptors.KnowledgeCardAdapter;
import com.shrey.kc.kcui.entities.KCReadRequest;
import com.shrey.kc.kcui.entities.KnowledgeOrTag;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.objects.RuntimeConstants;
import com.shrey.kc.kcui.workerActivities.AsyncCall;
import com.shrey.kc.kcui.workerActivities.ServiceBcastReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ViewTags extends KCUIActivity {

    // we sometimes get duplicate broadcasts, this to avoid starting the activity multiple times
    public static boolean isActive = false;
    ServiceBcastReceiver serviceBcastReceiver;
    private KnowledgeCardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tags);
        fillViewsWithTags();

        ActionBar actionBar = getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(ViewTags.class.getName(), "back");
        setResult(RuntimeConstants.INSTANCE.STARTED_ACTIVITY_RESULT_NOOP);
        finish();
        return true;
    }

    private void fillViewsWithTags() {
        Intent cardDataIntent = getIntent();
        ArrayList<String> tags = cardDataIntent.getStringArrayListExtra("Tags");
        if (tags == null) {
            // perhaps fillup a default card saying no result
            return;
        }
        ScrollView sv = findViewById(R.id.root_vertical_container_for_tags);
        LinearLayout lv = sv.findViewById(R.id.root_vertical_container);

        List<KnowledgeOrTag> knowledgeOrTagsList = tags.stream()
                .map(know -> new KnowledgeOrTag(know, "", true))
                .collect(Collectors.toList());

        cardAdapter = new KnowledgeCardAdapter(knowledgeOrTagsList);
        RecyclerView recyclerView = findViewById(R.id.scroll_knowledge_all);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();
    }

    // we sometimes get duplicate broadcasts, this to avoid starting the activity multiple times
    @Override
    public void onStart() {
        isActive = true;
        serviceBcastReceiver = new ServiceBcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AsyncCall.ACTION_READ);
        registerReceiver(serviceBcastReceiver, intentFilter);
        super.onStart();
        Log.i(ViewTags.class.getName(), getApplicationContext().getPackageName());
    }

    @Override
    public void onStop() {
        isActive = false;
        unregisterReceiver(serviceBcastReceiver);
        super.onStop();
    }

    @Override
    public void handleBroadcastResult(NodeResult result, String action) {
        fillUpKnowledgeCards(result);
    }

    private void fillUpKnowledgeCards(NodeResult result) {
        if (result == null || result.getResult() == null) {
            makeToastOfFailure();
            return;
        }
        HashMap<String, Object> resp = result.getResult();
        Log.d("apicall", resp.toString());
        ArrayList<LinkedHashMap> knows = (ArrayList<LinkedHashMap>) resp.get("Knowledge");
        int id = 999;
        ArrayList<String> knowledges = new ArrayList<>();
        for (LinkedHashMap param : knows) {
            if (param.get("cloud") == null) {
                continue;
            }
            knowledges.add(param.get("cloud").toString());
        }
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        Intent showKnowledgeIntent = new Intent(ViewTags.this, ViewKnowledge.class);
        showKnowledgeIntent.putStringArrayListExtra("knowledges", knowledges);
        startActivityForResult(showKnowledgeIntent, 0);
        return;
    }
}
