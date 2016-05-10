package com.example.nicustejar.appamadeus.ViewsAndControllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nicustejar.appamadeus.BackEndLogic.LogInManager;
import com.example.nicustejar.appamadeus.AppManager.AppConfig;
import com.example.nicustejar.appamadeus.AppManager.SessionManager;
import com.example.nicustejar.appamadeus.R;

/**
 * Activity for entering the log in data
 * */
public class LoginActivity extends AppCompatActivity {

    // Views
    private Button buttonLogin;
    private Button buttonLinkToRegisterScreen;
    private EditText inputUserName;
    private EditText inputPassword;
    private ProgressDialog progressDialog;

    // ACK from the server
    private String responseMessage;

    // Connection to the corresponding data base
    private LogInManager logInManager;

    // Session manager to mange the user log in on current device
    private SessionManager session;
    private boolean isLoggedIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Loading components
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // By default the user is not logged in
        this.isLoggedIn = false;

        // Loading more components
        this.inputUserName = (EditText) findViewById(R.id.userName);
        this.inputPassword = (EditText) findViewById(R.id.password);
        this.buttonLogin = (Button) findViewById(R.id.btnLogin);
        this.buttonLinkToRegisterScreen = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setCancelable(false);

        // Session manager
        this.session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (this.session.isLoggedIn()) {
            // User is already logged in. Take him to ListaServiciiFurnizorActivity
            Intent intent = new Intent(LoginActivity.this, ListaServiciiFurnizorActivity.class);
            startActivity(intent);
            finish();
        }

        // LoginActivity button Click Event
        this.buttonLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String userName = inputUserName.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!userName.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(userName, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                }
            }

        });

        // Link to Register Screen
        this.buttonLinkToRegisterScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * Checks if the input data is valid
     * @param userName username
     * @param password pasword
     */
    private void checkLogin(String userName, String password) {
        // Starts loading dialog
        this.progressDialog.setMessage("Conectare...");
        showDialog();

        // Tries to connect to the server and after the Thread is finished, we stop the loading dialog
        this.logInManager = new LogInManager(userName, password, this);
        this.logInManager.run();
        hideDialog();

        // If the server logged us in
        if (this.isLoggedIn) {
            // Change the login status and display corresponding message
            this.session.setLogin(true);

            // Go to the next Activity
            Intent intent = new Intent(LoginActivity.this, ListaServiciiFurnizorActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(AppConfig.BUNDLE_USERNAME, userName);
            bundle.putString(AppConfig.BUNDLE_PASS, password);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        Toast.makeText(getApplicationContext(), this.responseMessage, Toast.LENGTH_LONG).show();
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    /**
     * Set's the server verification respone
     * @param isLoggedIn true if the user data was ok, false otherwise
     * */
    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    /**
     * Sets the server's response message
     * @param responseMessage the server's response
     * */
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
