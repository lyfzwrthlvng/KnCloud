package com.shrey.kc.kcui.adaptors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.inject.Singleton;

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
    public Map<String, Object> nonBlockingServerCall(final URL backend, final String method, final Map<String, Object> request) throws ExecutionException, InterruptedException {
        Future<Map<String, Object>> future = threadPoolExecutor.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                HttpURLConnection connection = (HttpURLConnection) backend.openConnection();
                connection.setRequestMethod(method);
                connection.setRequestProperty( "Content-Type", "application/json");
                OutputStream os = connection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                osw.write(new Gson().toJson(request));
                osw.close();
                os.close();
                int resp = connection.getResponseCode();
                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder outputBuilder = new StringBuilder();
                String output;
                while((output = br.readLine()) != null) {
                    outputBuilder.append(output);
                }
                Type tt = new TypeToken<HashMap<String, Object>>(){}.getType();
                return (Map<String, Object>) new Gson().fromJson(outputBuilder.toString(), tt);
            }
        });
        return future.get();
    }
}
