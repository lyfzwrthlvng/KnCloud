package com.shrey.kc.kcui.adaptors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.KCWriteRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.workerActivities.GenericCallbackHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.inject.Singleton;

import static java.lang.Math.floor;

@Singleton
public class ServerCaller {

    ExecutorService threadPoolExecutor;

    public ServerCaller() {
        threadPoolExecutor = Executors.newFixedThreadPool(10);
    }

    public ExecutorService getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ExecutorService threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @SuppressWarnings("unchecked")
    public NodeResult nonBlockingServerCall(final URL backend, final String method,
                                                     final KCAccessRequest request)
            throws ExecutionException, InterruptedException {
        Future<NodeResult> future = threadPoolExecutor.submit(new Callable<NodeResult>() {
            @Override
            public NodeResult call() throws Exception {
                try {
                    HttpURLConnection connection = (HttpURLConnection) backend.openConnection();

                    connection.setRequestMethod(method);
                    connection.setRequestProperty("Content-Type", "application/json");
                    OutputStream os = connection.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    osw.write(new Gson().toJson(request));
                    osw.close();
                    os.close();
                    int resp = connection.getResponseCode();
                    if (floor(resp / 100) != 2) {
                        // FAILURE!!!
                        throw new ExecutionException("bad mojo!", null);
                    }
                    InputStream is = connection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    StringBuilder outputBuilder = new StringBuilder();
                    String output;
                    while ((output = br.readLine()) != null) {
                        outputBuilder.append(output);
                    }
                    Type tt = new TypeToken<HashMap<String, Object>>() {}.getType();
                    // callback with result when done
                    NodeResult result = new NodeResult();
                    result.setResult((HashMap<String, Object>) new Gson().fromJson(outputBuilder.toString(), tt));
                    return result;
                } catch (Exception e) {
                    //callbackHelper.callbackWithResult(null);
                    e.printStackTrace();
                }
                return null;
            }
        });

        return future.get();
    }
}
