package com.example.nicustejar.appamadeus.BackEndLogic;

import android.util.Log;

import com.example.nicustejar.appamadeus.AppManager.AppConfig;
import com.example.nicustejar.appamadeus.ViewsAndControllers.LoginActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Connects to the web server (via REST). Sends the user's login info and accepts the server's response.
 */
public class LogInManager {

    // Values to check
    private String username;
    private String password;

    // Activity to respond to
    private LoginActivity loginActivity;

    /**
     * Creates a new LogInManager to talk to server
     *
     * @param username      the username of current user to connect
     * @param password      the password of current user to connect
     * @param loginActivity the activity to which the LogInManager must send the server's respone
     */
    public LogInManager(String username, String password, LoginActivity loginActivity) {
        this.username = username;
        this.password = password;
        this.loginActivity = loginActivity;
    }

    /**
     * Does the main work: creates a new Runnable, runs it on the UI thread and connects to the web
     * server to check the validity of data
     */
    public void run() {
        // Since the thread is started from LoginActivity, it's a good solution to use runOnUiThread
        this.loginActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Vars for connection and response
                HttpURLConnection connection;
                OutputStreamWriter request = null;
                URL url = null;

                String response = null;
                String parameters = "email=" + username + "&password=" + password;

                try {
                    // POST the login info
                    url = new URL(AppConfig.URL_LOGIN);
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

                    // Check response and inform the LogInActivity of result
                    if ((response != null) && (response.contains(AppConfig.LOG_IN_SUCCES))) {
                        loginActivity.setResponseMessage(AppConfig.LOG_IN_SUCCES_MESSAGE);
                        loginActivity.setLoggedIn(true);
                    } else if ((response == null) || (response.contains(AppConfig.LOG_IN_FAIL))) {
                        loginActivity.setResponseMessage(AppConfig.LOG_IN_FAIL_MESSAGE);
                        loginActivity.setLoggedIn(false);
                    }
                } catch (Exception e) {
                    Log.d("thread", e.toString());
                    loginActivity.setResponseMessage(AppConfig.LOG_IN_FAIL_MESSAGE);
                    loginActivity.setLoggedIn(false);
                }
            }
        });

    }

}
