package com.shrey.kc.kcui.executors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GetKnowledgeExecutor implements GenericExecutor {

    @Override
    public Map<String, Object> executeRequest(Map<String, Object> request) throws IOException {
        URL backend = new URL("http://localhost:9090/search/query");
        HttpURLConnection connection = (HttpURLConnection) backend.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty( "Content-Type", "application/json");
        connection.setRequestProperty( "charset", "utf-8");
        OutputStream os = connection.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        osw.write(new Gson().toJson(request));
        int resp = connection.getResponseCode();
        InputStream is = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder outputBuilder = new StringBuilder();
        String output;
        while((output = br.readLine()) != null) {
            outputBuilder.append(output);
        }
        Type tt = new TypeToken<Map<String, Object>>(){}.getType();
        return (Map<String, Object>) new Gson().fromJson(outputBuilder.toString(), tt.getClass());
    }
}
