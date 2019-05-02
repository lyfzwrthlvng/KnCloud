package com.shrey.kc.kcui.workerActivities;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.KCReadRequest;
import com.shrey.kc.kcui.entities.KCWriteRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.objects.CommunicationFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class AsyncCall extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_READ = "com.shrey.kc.kcui.workerActivities.action.READ";
    public static final String ACTION_ADD = "com.shrey.kc.kcui.workerActivities.action.ADD";
    public static final String ACTION_FETCH_TAGS = "com.shrey.kc.kcui.workerActivities.action.FETCH_TAGS";

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

    public static void startActionFetchTags(Context context, KCWriteRequest writeRequest) {
        Intent intent = new Intent(context, AsyncCall.class);
        intent.setAction(ACTION_FETCH_TAGS);
        intent.putExtra("request", writeRequest);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        KCAccessRequest request = (KCAccessRequest)intent.getSerializableExtra("request");
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
                    handleActionFetchTags((KCWriteRequest)request);
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

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
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

    private void handleActionFetchTags(KCWriteRequest writeRequest) {
        NodeResult result = null;
        try {
            result = CommunicationFactory.getInstance().getExecutor("FETCH_TAGS").executeRequest(writeRequest);
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

    private void broadcastResult(String action, NodeResult result) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("result", result);
        sendBroadcast(intent);
    }
}
