package com.shrey.kc.kcui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.workerActivities.AsyncCall;

public class SearchTags extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tags);
    }

    private void performFetchAllTags(View v) {
        Log.i(LoggedInHome.class.getName(), "Fetching all tags for user");
        KCAccessRequest accessRequest = KCAccessRequest.constructRequest();
        AsyncCall.startActionFetchTags(getApplicationContext(), accessRequest);
    }
}
