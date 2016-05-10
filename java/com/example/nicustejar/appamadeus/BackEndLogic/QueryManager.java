package com.example.nicustejar.appamadeus.BackEndLogic;

import android.util.Log;

import com.example.nicustejar.appamadeus.AppManager.AppConfig;
import com.example.nicustejar.appamadeus.ViewsAndControllers.CitireActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Connects to the web server (via REST). Sends the user's query and accepts the server's response.
 */
public class QueryManager {

    // Values to send
    private String id_client;
    private String id_contor;
    private String index_contor;

    // Activity to respond to
    private CitireActivity citireActivity;

    /**
     * Creates a new QueryManager to talk to server
     * @param idClient the client's id
     * @param idContor the current meter id
     * @param indexContor the current meter reading
     * @param citireActivity the activity to which the QueryManager must send the server's response
     * */
    public QueryManager(String idClient, String idContor, String indexContor, CitireActivity citireActivity) {
        this.id_client = idClient;
        this.id_contor = idContor;
        this.index_contor = indexContor;
        this.citireActivity = citireActivity;
    }

    /**
     * Does the main work: creates a new Runnable, runs it on the UI thread and connects to the web
     * server to check the validity of data
     */
    public void run() {
        // Since the thread is started from CitireActivity, it's a good solution to use runOnUiThread
        this.citireActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Vars for connection and response
                HttpURLConnection connection;
                OutputStreamWriter request = null;
                URL url = null;

                String response = null;
                String parameters = "UserId=" + "1" + "&meter_id=" + id_contor + "&measure_index=" + index_contor;

                try {
                    // POST the login info
                    url = new URL(AppConfig.URL_ADD_MEASURE);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestMethod("POST");

                    // Get response from server
                    request = new OutputStreamWriter(connection.getOutputStream());
                    request.write(parameters);
                    request.flush();
                    request.close();

                    // Process response
                    String line = "";
                    InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    response = sb.toString();
                    isr.close();
                    reader.close();

                    // Check response and inform the CitireActivity of result
                    if ((response != null) && (response.contains(AppConfig.QUERY_SUCCES))) {
                        citireActivity.setResponseMessage(AppConfig.QUERY_SUCCES_MESSAGE);
                    }
                    else if ((response == null) || (response.contains(AppConfig.QUERY_FAIL))) {
                        citireActivity.setResponseMessage(AppConfig.QUERY_FAIL_MESSAGE);
                    }
                } catch(Exception e) {
                    Log.d("thread", e.toString());
                    citireActivity.setResponseMessage(AppConfig.LOG_IN_FAIL_MESSAGE);
                }
            }
        });
    }

}
