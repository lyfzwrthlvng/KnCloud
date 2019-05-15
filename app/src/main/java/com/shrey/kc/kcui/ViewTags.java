package com.shrey.kc.kcui;

import android.app.ActionBar;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shrey.kc.kcui.entities.KCReadRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.objects.RuntimeConstants;
import com.shrey.kc.kcui.workerActivities.AsyncCall;
import com.shrey.kc.kcui.workerActivities.ServiceBcastReceiver;

import java.util.ArrayList;

public class ViewTags extends AppCompatActivity {

    // we sometimes get duplicate broadcasts, this to avoid starting the activity multiple times
    public static boolean isActive = false;
    ServiceBcastReceiver serviceBcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tags);
        fillViewsWithTags();

        ActionBar actionBar = getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Log.d(ViewTags.class.getName(), "back");
        setResult(RuntimeConstants.INSTANCE.STARTED_ACTIVITY_RESULT_NOOP);
        finish();
        return true;
    }

    private void fillViewsWithTags() {
        Intent cardDataIntent = getIntent();
        ArrayList<String> tags = cardDataIntent.getStringArrayListExtra("Tags");
        if(tags == null) {
            // perhaps fillup a default card saying no result
            return;
        }
        ScrollView sv = findViewById(R.id.root_vertical_container_for_tags);
        LinearLayout lv = sv.findViewById(R.id.root_vertical_container);
        for(String tag: tags) {
            CardView cv = (CardView) getLayoutInflater().inflate(getResources().getLayout(R.layout.knowledge_card), null);
            TextView tv = cv.findViewById(R.id.text_view_in_card);
            tv.setText(tag);

            // add on click listener for the cardView
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardView cv = (CardView)v;
                    TextView tvIn = cv.findViewById(R.id.text_view_in_card);
                    // search knowledge for this tag
                    ArrayList<String> al = new ArrayList<String>();
                    al.add(tvIn.getText().toString());
                    KCReadRequest readRequest = KCReadRequest.constructRequest(al);
                    AsyncCall.startActionRead(getApplicationContext(), readRequest);
                }
            });
            // populate it in the container
            lv.addView(cv);
        }


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
}
