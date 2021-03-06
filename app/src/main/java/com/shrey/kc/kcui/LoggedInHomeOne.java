package com.shrey.kc.kcui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.DriveScopes;
import com.shrey.kc.kcui.activities.KCUIActivity;
import com.shrey.kc.kcui.adaptors.KnowledgeCardAdapter;
import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.KCBackupRequest;
import com.shrey.kc.kcui.entities.KCDriveFileDownloadRequest;
import com.shrey.kc.kcui.entities.KCReadRequest;
import com.shrey.kc.kcui.entities.KCWriteRequest;
import com.shrey.kc.kcui.entities.KnowledgeOrTag;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.entities.User;
import com.shrey.kc.kcui.objects.CurrentUserInfo;
import com.shrey.kc.kcui.objects.LocalDBHolder;
import com.shrey.kc.kcui.objects.RuntimeConstants;
import com.shrey.kc.kcui.objects.RuntimeDynamicDataHolder;
import com.shrey.kc.kcui.objects.ViewConfigHolder;
import com.shrey.kc.kcui.workerActivities.AsyncCall;
import com.shrey.kc.kcui.workerActivities.ServiceBcastReceiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import de.blox.graphview.BaseGraphAdapter;
import de.blox.graphview.Graph;
import de.blox.graphview.GraphView;
import de.blox.graphview.ViewHolder;
import de.blox.graphview.tree.BuchheimWalkerAlgorithm;
import de.blox.graphview.tree.BuchheimWalkerConfiguration;

import static android.view.View.GONE;
import static com.shrey.kc.kcui.workerActivities.AsyncCall.startActionVerifyDB;

public class LoggedInHomeOne extends KCUIActivity {

    ServiceBcastReceiver serviceBcastReceiver;
    HashMap<String, View> inflatedRoots = new HashMap<>();
    ProgressDialog dialog;

    private final int RC_DRIVE_PERM = 9002;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_add_knowledge:
                    XmlResourceParser addLayout = getResources().getLayout(R.layout.activity_logged_in_add_knowledge);
                    inflateLayout(addLayout, AsyncCall.ACTION_ADD);
                    return true;
                case R.id.navigation_search_tags:
                    XmlResourceParser searchTagsLayout = getResources().getLayout(R.layout.activity_search_tags);
                    inflateLayout(searchTagsLayout, AsyncCall.ACTION_READ);
                    return true;
                case R.id.navigation_view_all_tags:
                    //XmlResourceParser allTagsLayout = getResources().getLayout(R.layout.activity_view_tags);
                    XmlResourceParser allTagsLayout = getResources().getLayout(ViewConfigHolder.INSTANCE.getLayoutForMenu(R.id.navigation_view_all_tags));
                    String action = ViewConfigHolder.INSTANCE.getActionForMenu(R.id.navigation_view_all_tags);
                    inflateLayout(allTagsLayout, action);
                    // now add stuff to this layout! can do it only if anything has changed, do everytime for now
                    AsyncCall.startActionFetchTags(getApplicationContext(), KCAccessRequest.constructRequest());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        boolean justSignedIn = getIntent().getBooleanExtra("justSignedIn", false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_home_one);

        LocalDBHolder.INSTANCE.getSetLocalDB(getApplicationContext(), false);
        LocalDBHolder.INSTANCE.setDatabasePath(getDatabasePath("local-kc-db"));
        if(justSignedIn) {
            Log.d(LoggedInHomeOne.class.getName(), "First time sign in, will check for backup file");
            LocalDBHolder.INSTANCE.setInitWell(false);
        }else{
            LocalDBHolder.INSTANCE.setInitWell(true);
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setChecked(true);

        // by default
        XmlResourceParser addLayout = getResources().getLayout(R.layout.activity_logged_in_add_knowledge);
        inflateLayout(addLayout, AsyncCall.ACTION_ADD);
        ViewConfigHolder.INSTANCE.setLayoutForMenu(R.id.navigation_view_all_tags, R.layout.activity_view_tags);
        ViewConfigHolder.INSTANCE.setActionForMenu(R.id.navigation_view_all_tags, AsyncCall.ACTION_FETCH_TAGS);

        if(justSignedIn && ensureSignIn()) {
            Log.d(LoggedInHomeOne.class.getName(), "checking if user had previously backed up file on cloud");
            KCDriveFileDownloadRequest req = KCDriveFileDownloadRequest.getDownloadRequest("dummy", getDatabasePath("local-kc-db"));
            dialog = ProgressDialog.show(LoggedInHomeOne.this, "",
                    "Please wait while we look for your old notes", true);
            startActionVerifyDB(getApplicationContext(), req);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_backup:
                if (ensureSignIn()) {
                    // Execute
                    KCBackupRequest request = KCBackupRequest.getBackupRequest(getDatabasePath("local-kc-db"));
                    AsyncCall.startActionBackup(getApplicationContext(), request);
                    item.setIcon(getDrawable(R.drawable.ic_action_backup_progress));
                }
                break;
            case R.id.action_logout:
                if(GoogleSignIn.hasPermissions(CurrentUserInfo.INSTANCE.getUserInfo().getUser().getAccountInfo(),
                        new Scope(DriveScopes.DRIVE_FILE))) {
                    signOut();
                } else {
                    ensureSignIn();
                }
            default:
                break;
        }

        return true;
    }

    private boolean ensureSignIn() {
        if(!GoogleSignIn.hasPermissions(CurrentUserInfo.INSTANCE.getUserInfo().getUser().getAccountInfo(),
                new Scope(DriveScopes.DRIVE_FILE))) {
            GoogleSignIn.requestPermissions(LoggedInHomeOne.this, RC_DRIVE_PERM,
                    CurrentUserInfo.getUserInfo().getUser().getAccountInfo(),
                    new Scope(DriveScopes.DRIVE_FILE));
        }
        if(!GoogleSignIn.hasPermissions(CurrentUserInfo.INSTANCE.getUserInfo().getUser().getAccountInfo(),
                new Scope(DriveScopes.DRIVE_FILE))) {
            Log.e(LoggedInHomeOne.class.getName(),"Still don't have permission :(");
            return false;
        }
        GoogleAccountCredential credential =
                GoogleAccountCredential.usingOAuth2(
                        getApplicationContext(), Collections.singleton(DriveScopes.DRIVE_FILE));
        CurrentUserInfo.INSTANCE.setAuthAccount(credential);
        return true;
    }

    private void signOut() {
        Log.d(LoggedInHomeOne.class.getName(), "starting sign out");
        CurrentUserInfo.INSTANCE.getSignInClient().signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //CurrentUserInfo.INSTANCE.setUser(null);
                        //CurrentUserInfo.INSTANCE.setAuthAccount(null);
                        Toast
                                .makeText(getApplicationContext(), "You will be signed out when you restart the app, you can log back in anytime to see your content", Toast.LENGTH_LONG)
                                .show();
                        //((MenuItem)findViewById(R.id.action_logout)).setIcon(R.drawable.ic_action_logout);
                    }
                });
    }

    private boolean inflateLayout(XmlResourceParser layout, String action) {
        boolean inflated = false;
        ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.container_root);
        ConstraintLayout layout1 = null;
        if(inflatedRoots.containsKey(action)) {
            layout1 = (ConstraintLayout) inflatedRoots.get(action);
        } else {
            layout1 = (ConstraintLayout) getLayoutInflater().inflate(layout, cl, false);
            inflatedRoots.put(action, layout1);
            // assign listeners as well
            // >>>
            // this could be composition of action Adders etc
            addListeners(layout1, action);
            inflated = true;
        }
        ConstraintLayout child = getPreviousInflatedLayout(cl);
        if(child != null) {
            cl.removeView(child);
        }
        cl.addView(layout1);
        return inflated;
    }

    private ConstraintLayout getPreviousInflatedLayout(ConstraintLayout thisRoot) {
        if(thisRoot.getChildCount() < 1) {
            return null;
        }
        int childCount = thisRoot.getChildCount();
        for(int x=0; x<childCount; x++) {
            View tv = thisRoot.getChildAt(x);
            if(tv.getId() == R.id.cl) {
                return (ConstraintLayout)tv;
            }
        }
        return null;
    }

    @Override
    public void handleBroadcastResult(NodeResult result, String action) {
        // Add cards, inside a new cardLayout
        Log.d(this.getClass().getName(), "handling action: " + action);
        if(action.equalsIgnoreCase(AsyncCall.ACTION_FETCH_TAGS) && action != null) {
            String[] tags = (String[]) result.getResult().get("Tags");
            //fillViewsWithTags(tags);
            // save them in memory as well
            ArrayList<String> rtd = new ArrayList<>();
            for(String tag: tags) {
                rtd.add(tag);
            }
            RuntimeDynamicDataHolder.getRuntimeData().setUserTags(rtd);
            fillViewsWithTags();
        } else if(action.equalsIgnoreCase(AsyncCall.ACTION_READ)) {
            fillUpKnowledgeCards(result);
        } else if(action.equalsIgnoreCase(AsyncCall.ACTION_ADD)) {
            if(result == null) {
                makeToastOfFailure();
            } else {
                makeToastOfSuccess("Note saved!");
            }
        } else if(action.equalsIgnoreCase(AsyncCall.ACTION_SEARCH) && result != null) {
//            Log.d("suggestion","Suggested words: " + result.getResult().get("suggestions").toString());
            ArrayList<String> rtd = (ArrayList<String>)result.getResult().get("suggestions");
            fillViewsWithTagsSuggested(rtd);
        } else if(action.equalsIgnoreCase(AsyncCall.ACTION_BACKUP)) {
            // invalidating menu, so we can change the color!
            invalidateOptionsMenu();
            if(result == null || !result.getResult().get("backupResult").equals("true")) {
                if(result.getResult().get("backupResult").equals("noop")) {
                    makeToastOfSuccess("Nothing to backup!");
                } else {
                    makeToastOfFailure();
                }
            } else {
                makeToastOfSuccess("Data backed up to your google drive!");
            }
        } else if(action.equalsIgnoreCase(AsyncCall.ACTION_FETCH_GRAPH)) {
            if(result == null || result.getResult().get("Graph") != null) {
                RuntimeDynamicDataHolder.getRuntimeData().setGraph((Graph)result.getResult().get("Graph"));
                XmlResourceParser allTagsLayout = getResources().getLayout(ViewConfigHolder.INSTANCE.getLayoutForMenu(R.id.navigation_view_all_tags));
                inflateLayout(allTagsLayout, action);
            }
        } else if(action.equalsIgnoreCase(AsyncCall.VERIFY_DB)) {
            dialog.dismiss();            //findViewById(R.id.progress_bar).setVisibility(View.GONE);
            if(result == null || result.getResult() == null) {
            } else {
                Log.d(LoggedInHomeOne.class.getName(), "downloaded, updating ref now");
                LocalDBHolder.INSTANCE.getSetLocalDB(getApplicationContext(), true);
                LocalDBHolder.INSTANCE.setDatabasePath(getDatabasePath("local-kc-db"));
                LocalDBHolder.INSTANCE.setInitWell(true);
                makeToastOfSuccess("Yayy! downloaded previously backed up knowledge from your drive!");
            }
        }

    }

    @Override
    protected void onStart() {
        serviceBcastReceiver = new ServiceBcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AsyncCall.ACTION_ADD);
        intentFilter.addAction(AsyncCall.ACTION_READ);
        intentFilter.addAction(AsyncCall.ACTION_FETCH_TAGS);
        intentFilter.addAction(AsyncCall.ACTION_SEARCH);
        intentFilter.addAction(AsyncCall.ACTION_BACKUP);
        intentFilter.addAction(AsyncCall.VERIFY_DB);
        registerReceiver(serviceBcastReceiver, intentFilter);
        super.onStart();
        Log.i("LOGGED_IN_HOME", getApplicationContext().getPackageName());
    }

    @Override
    protected void onStop() {
        unregisterReceiver(serviceBcastReceiver);
        super.onStop();
    }

    private void fillAllTags(NodeResult result) {
        if(result == null || result.getResult() == null) {
            makeToastOfFailure();
            return;
        }
        HashMap<String, Object> resp = result.getResult();
        Log.d("apicall", resp.toString());
        String[] tags = (String[]) resp.get("Tags");
        ArrayList<String> tagsArray = new ArrayList<>();
        for(String tag: tags) {
            tagsArray.add(tag);
        }
        // fill up tags
        fillViewsWithTags(tags);

        return;
    }

    private void fillViewsWithTags(String[] tags) {
        // Keep the tags somewhere in memory, for now, we might wanna load in parts later TODO

        Log.i(LoggedInHomeOne.class.getName(), "filling up with tags");
        if (tags == null) {
            // perhaps fillup a default card saying no result
            return;
        }
        ScrollView sv = findViewById(R.id.root_vertical_container_for_tags);
        LinearLayout lv = sv.findViewById(R.id.root_vertical_container);
        if(lv.getChildCount() == tags.length) {
            // had already done that
            return;
        }
        // remove all for now and add again
        // TODO: make it better
        lv.removeAllViews();
        for (String tag : tags) {
            CardView cv = (CardView) getLayoutInflater().inflate(getResources().getLayout(R.layout.knowledge_card), null);
            TextView tv = cv.findViewById(R.id.text_view_in_card);
            tv.setText(tag);

            // add on click listener for the cardView
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardView cv = (CardView) v;
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

    private void fillViewsWithTagsSuggested(ArrayList<String> tags) {
        // Keep the tags somewhere in memory, for now, we might wanna load in parts later TODO

        Log.i(LoggedInHomeOne.class.getName(), "filling up with tags");
        LinearLayout sv = findViewById(R.id.root_vertical_container_for_tags);
        LinearLayout lv = sv.findViewById(R.id.root_vertical_container);

        if(tags == null) {
            tags = new ArrayList<>();
        }

        List<KnowledgeOrTag> knowledgeOrTagsList = tags.stream()
                .map(know -> new KnowledgeOrTag(know, "", true))
                .collect(Collectors.toList());

        KnowledgeCardAdapter cardAdapter = new KnowledgeCardAdapter(knowledgeOrTagsList,
                RuntimeConstants.INSTANCE.SMALL_HEIGHT, true);
        RecyclerView recyclerView = findViewById(R.id.scroll_knowledge_all);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();
    }

    private void fillViewsWithTags() {
        ArrayList<String> allTheTags = RuntimeDynamicDataHolder.getRuntimeData().getUserTagsSorted();
        // Keep the tags somewhere in memory, for now, we might wanna load in parts later TODO

        Log.i(LoggedInHomeOne.class.getName(), "filling up with tags");
        if (allTheTags == null) {
            // perhaps fillup a default card saying no result
            return;
        }
        LinearLayout lv = findViewById(R.id.root_vertical_container_for_tags);

        List<KnowledgeOrTag> knowledgeOrTagsList = allTheTags.stream()
                .map(know -> new KnowledgeOrTag(know, "", true))
                .collect(Collectors.toList());

        KnowledgeCardAdapter cardAdapter = new KnowledgeCardAdapter(knowledgeOrTagsList,
                RuntimeConstants.INSTANCE.SMALL_HEIGHT, true);
        RecyclerView recyclerView = findViewById(R.id.scroll_knowledge_all);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();

        /*
        //LinearLayout lv = sv.findViewById(R.id.root_vertical_container);
        if(lv.getChildCount() == allTheTags.size()) {
            // had already done that
            return;
        }
        // remove all for now and add again
        // TODO: make it better
        lv.removeAllViews();
        for (String tag : allTheTags) {
            CardView cv = (CardView) getLayoutInflater().inflate(getResources().getLayout(R.layout.knowledge_card), null);
            TextView tv = cv.findViewById(R.id.text_view_in_card);
            tv.setText(tag);

            // add on click listener for the cardView
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardView cv = (CardView) v;
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
         */
    }

    // add listeners to layouts inflated
    private void addListeners(ConstraintLayout root, String action) {
        if(action.equalsIgnoreCase(AsyncCall.ACTION_FETCH_TAGS)) {

        } else if(action.equalsIgnoreCase(AsyncCall.ACTION_ADD)) {
            Button atb = root.findViewById(R.id.add_tag_text_button);
            final EditText kt = root.findViewById(R.id.add_tag_text);
            atb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("AddKnowledge", kt.getText().toString());
                    // Send it to tag suggestions, we'll receive broadcast on next activity
                    KCWriteRequest writeRequest = new KCWriteRequest();
                    writeRequest.setUserkey(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().getEmail());
                    writeRequest.setPassKey("dummy");
                    writeRequest.setUserId(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().hashCode());
                    //AsyncCall.startActionFetchTags(getApplicationContext(),
                    //        KCWriteRequest.constructRequest(kt.getText().toString(), ""));
                    // start an activity to tags for this knowledge
                    Intent collectTagsIntent = new Intent(LoggedInHomeOne.this, LoggedInAddKnowledgeAddTags.class);
                    startActivityForResult(collectTagsIntent, RuntimeConstants.INSTANCE.START_ACTIVITY_FOR_TAGS);
                }
            });
            kt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        kt.setText(null);
                    }
                }
            });
            kt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.toString().trim().length()==0) {
                        atb.setVisibility(GONE);
                    }else{
                        atb.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if(action.equalsIgnoreCase(AsyncCall.ACTION_READ)) {
            final EditText et = root.findViewById(R.id.text_search);

            et.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText et = (EditText)v;
                    et.setText(null);
                    //et.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
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

            /*
            et.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    EditText etf = (EditText) v;
                    String current = etf.getText().toString();
                    AsyncCall.startActionSuggest(getApplicationContext(), current);
                    return false;
                }
            });
            */

            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() > 0) {
                        AsyncCall.startActionSuggest(getApplicationContext(), s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            // ---- button search
            Button searchButton = root.findViewById(R.id.button_search);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText et = findViewById(R.id.text_search);
                    performSearchAction(findViewById(R.id.text_search));
                }
            });
            // display the cloud

        } else if(action.equals(AsyncCall.ACTION_FETCH_GRAPH)) {
            GraphView graphView = root.findViewById(R.id.graph);
            final BaseGraphAdapter<ViewHolder> adapter = new BaseGraphAdapter<ViewHolder>(RuntimeDynamicDataHolder.INSTANCE.getGraph()) {

                @NonNull
                @Override
                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.graph_node, parent, false);
                    return new SimpleViewHolder(view);
                }

                @Override
                public void onBindViewHolder(ViewHolder viewHolder, Object data, int position) {
                    ((SimpleViewHolder)viewHolder).textView.setText(data.toString());
                }
            };
            graphView.setAdapter(adapter);
            final BuchheimWalkerConfiguration configuration = new BuchheimWalkerConfiguration.Builder()
                    .setSiblingSeparation(100)
                    .setLevelSeparation(300)
                    .setSubtreeSeparation(300)
                    .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
                    .build();
            adapter.setAlgorithm(new BuchheimWalkerAlgorithm(configuration));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RuntimeConstants.INSTANCE.START_ACTIVITY_FOR_TAGS &&
                resultCode == RuntimeConstants.INSTANCE.STARTED_ACTIVITY_RESULT_GOOD) {
            final EditText kt = findViewById(R.id.add_tag_text);
            String knowledge = kt.getText().toString();
            Log.i(LoggedInHomeOne.class.getName(), "Adding tag knowledge " + knowledge);
            for (String tag : data.getStringArrayListExtra("tagsForKnowledge")) {
                KCWriteRequest request = new KCWriteRequest();
                request.setKeyword(tag);
                request.setValue(knowledge);
                request.setUserId(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().hashCode());
                request.setPassKey("dummy");
                request.setUserkey(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().getEmail());
                // send request to server
                AsyncCall.startActionAdd(getApplicationContext(), request);
            }
        } else if(requestCode == RC_DRIVE_PERM) {
            GoogleAccountCredential credential =
                    GoogleAccountCredential.usingOAuth2(
                            getApplicationContext(), Collections.singleton(DriveScopes.DRIVE_FILE));
            CurrentUserInfo.INSTANCE.setAuthAccount(credential);
        }
    }

    private void fillUpKnowledgeCards(NodeResult result) {
        //TODO: bad coding assuming activity, get this from activity itself
        if(result == null || result.getResult() == null) {
            makeToastOfFailure();
            return;
        }
        HashMap<String, Object> resp = result.getResult();
        Log.d("apicall", resp.toString());
        ArrayList<LinkedHashMap> knows = (ArrayList<LinkedHashMap>) resp.get("Knowledge");
        String currentTag = ((String[]) resp.get("tag"))[0];
        int id = 999;
        ArrayList<String> knowledges = new ArrayList<>();
        for(LinkedHashMap param: knows) {
            if(param.get("cloud") == null) {
                continue;
            }
            knowledges.add(param.get("cloud").toString());
        }
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //inputManager.toggleSoftInput(0, 0);
        Intent showKnowledgeIntent = new Intent(LoggedInHomeOne.this, ViewKnowledge.class);
        showKnowledgeIntent.putStringArrayListExtra("knowledges", knowledges);
        showKnowledgeIntent.putExtra("tag",currentTag);
        Log.d("apicall", currentTag  + " << fetched knowledges for");
        startActivityForResult(showKnowledgeIntent, 0);
        return;
    }

    private void performSearchAction(View v) {
        Log.i(LoggedInHomeOne.class.getName(), "Performing search action");
        final EditText editText = (EditText) v;
        //editText.setBackground(getDrawable(R.drawable.rounded_corners_activity));
        //editText.setBackgroundResource(R.drawable.rounded_corners_activity);

        String userKey = CurrentUserInfo.getUserInfo().getUser().getAccountInfo().getEmail();
        String keyword = editText.getText().toString();

        KCReadRequest request = new KCReadRequest();
        ArrayList<String> keywords = new ArrayList<>();
        keywords.add(keyword);
        request.setKeywordList(keywords);
        request.setUserkey(userKey);
        request.setPassKey("dummy");
        request.setUserId(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().hashCode());
        AsyncCall.startActionRead(getApplicationContext(), request);
    }

    private void displayAsCloud(HashMap<String, Integer> cloudOfUse) {
        int maxTextSize = 50;
        Integer totalCount = 0;
        for(Integer vv: cloudOfUse.values()) {
            totalCount += vv;
        }
        HashMap<String, Double> ratios = new HashMap<>();
        for(String cloudKey: cloudOfUse.keySet()) {
            ratios.put(cloudKey, (cloudOfUse.get(cloudKey)*1.0) / (totalCount*1.0));
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_backup);
        item.setIcon(getDrawable(R.drawable.ic_action_backup));
        return super.onPrepareOptionsMenu(menu);
    }
}
