package com.shrey.kc.kcui.workerActivities;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.Log;

import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.KCBackupRequest;
import com.shrey.kc.kcui.entities.KCReadRequest;
import com.shrey.kc.kcui.entities.KCWriteRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.objects.CommunicationFactory;
import com.shrey.kc.kcui.objects.RuntimeDynamicDataHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class AsyncCall extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_READ = "com.shrey.kc.kcui.workerActivities.action.READ";
    public static final String ACTION_ADD = "com.shrey.kc.kcui.workerActivities.action.ADD";
    public static final String ACTION_FETCH_TAGS = "com.shrey.kc.kcui.workerActivities.action.FETCH_TAGS";
    public static final String ACTION_SUGGEST = "com.shrey.kc.kcui.workerActivities.action.SUGGEST";
    public static final String ACTION_BACKUP = "com.shrey.kc.kcui.workerActivities.action.BACKUP";

    public AsyncCall() {
        super("AsyncCall");
    }

    public static void startActionRead(Context context, KCReadRequest readRequest) {
        Intent intent = new Intent(context, AsyncCall.class);
        intent.setAction(ACTION_READ);
        intent.putExtra("request", readRequest);
        context.startService(intent);
    }

    public static void startActionAdd(Context context, KCWriteRequest writeRequest) {
        Intent intent = new Intent(context, AsyncCall.class);
        intent.setAction(ACTION_ADD);
        intent.putExtra("request", writeRequest);
        context.startService(intent);
    }

    public static void startActionFetchTags(Context context, KCAccessRequest request) {
        Intent intent = new Intent(context, AsyncCall.class);
        intent.setAction(ACTION_FETCH_TAGS);
        intent.putExtra("request", request);
        context.startService(intent);
    }

    public static void startActionSuggest(Context context, String partial) {
        Intent intent = new Intent(context, AsyncCall.class);
        intent.setAction(ACTION_SUGGEST);
        intent.putExtra("request", partial);
        context.startService(intent);
    }

    public static void startActionBackup(Context context, KCBackupRequest request) {
        Intent intent = new Intent(context, AsyncCall.class);
        intent.setAction(ACTION_BACKUP);
        intent.putExtra("request", request);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Object request = intent.getSerializableExtra("request");
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_READ:
                    handleActionFind((KCReadRequest)request);
                    break;
                case ACTION_ADD:
                    handleActionAdd((KCWriteRequest)request);
                    break;
                case ACTION_FETCH_TAGS:
                    handleActionFetchTags((KCAccessRequest)request);
                    break;
                case ACTION_SUGGEST:
                    handleActionSuggest((String)request);
                    break;
                case ACTION_BACKUP:
                    handleActionBackup((KCBackupRequest)request);
                    break;
                default:
                    break;
            }
        }
    }

    private void handleActionFind(KCReadRequest readRequest) {
        NodeResult result = null;
        try {
            result = CommunicationFactory.getInstance().getExecutor("FIND").executeRequest(readRequest);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.setAction(ACTION_READ);
        intent.putExtra("result", result);
        sendBroadcast(intent);
    }

    private void handleActionAdd(KCWriteRequest writeRequest) {
        NodeResult result = null;
        try {
            result = CommunicationFactory.getInstance().getExecutor("ADD").executeRequest(writeRequest);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        broadcastResult(ACTION_ADD, result);
    }

    private void handleActionFetchTags(KCAccessRequest writeRequest) {

        NodeResult result = null;
        try {
            result = CommunicationFactory.getInstance().getExecutor("USER_TAGS").executeRequest(writeRequest);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        broadcastResult(ACTION_FETCH_TAGS, result);
    }

    private void handleActionSuggest(String partial) {
        NodeResult result = new NodeResult();
        Log.d(this.getClass().getName(), "suggesting...");
        if(RuntimeDynamicDataHolder.getRuntimeData().getUserTags() == null) {
            // start activity to fetch tags

            try {
                NodeResult result2 = CommunicationFactory.getInstance().getExecutor("USER_TAGS").executeRequest(KCAccessRequest.constructRequest());
                String[] tags = (String[]) result2.getResult().get("Tags");
                //fillViewsWithTags(tags);
                // save them in memory as well
                ArrayList<String> rtd = new ArrayList<>();
                for(String tag: tags) {
                    rtd.add(tag);
                }
                RuntimeDynamicDataHolder.getRuntimeData().setUserTags(rtd);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        ArrayList<String> acw = RuntimeDynamicDataHolder.getRuntimeData().getAutocompleteWords(partial);
        HashMap<String, Object> ir = new HashMap<String, Object>();
        ir.put("suggestions", acw);
        //Log.d(this.getClass().getName(), acw.toString());
        result.setResult(ir);
        broadcastResult(ACTION_SUGGEST, result);
    }

    private void handleActionBackup(KCBackupRequest request) {
        try {
            CommunicationFactory.INSTANCE.getExecutor("BACKUP").executeRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    //--------

    private void broadcastResult(String action, NodeResult result) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("result", result);
        sendBroadcast(intent);
    }
}
