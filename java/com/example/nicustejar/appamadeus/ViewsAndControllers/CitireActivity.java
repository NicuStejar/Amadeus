package com.example.nicustejar.appamadeus.ViewsAndControllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicustejar.appamadeus.BackEndLogic.QueryManager;
import com.example.nicustejar.appamadeus.AppManager.AppConfig;
import com.example.nicustejar.appamadeus.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * Activity for entering the values of the measurement
 * Stars the reading of id and reading of index
 */
public class CitireActivity extends AppCompatActivity {

    // Views
    private Button scaneazaIdButton, scaneazaIndexButton, trimiteButton;
    private TextView scaneazaIdContorTextView, scaneazaIndexContorTextView;
    private ProgressDialog progressDialog;

    // ACK message from server
    private String responseMessage;

    // Connection do the corresponding data base
    private QueryManager queryManager;

    // Data to send
    private String idContor, indexContor;

    // Bundles (for securing the Query)
    private String username, password;

    //TODO sterge-ma
    private EditText editTextIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Loading components
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citire);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Furnizor_0 gaze naturale");
        setSupportActionBar(toolbar);

        // Data for security received from log in
        Bundle bundle = getIntent().getExtras();
        this.username = bundle.getString(AppConfig.BUNDLE_USERNAME);
        this.password = bundle.getString(AppConfig.BUNDLE_PASS);

        // Loading more components
        this.scaneazaIdButton = (Button) findViewById(R.id.scaneazaIDbutton);
        this.scaneazaIndexButton = (Button) findViewById(R.id.scaneazaIndexButton);
        this.trimiteButton = (Button) findViewById(R.id.trimiteButton);
        this.scaneazaIdContorTextView = (TextView) findViewById(R.id.idContorTextView);
        this.scaneazaIndexContorTextView = (TextView) findViewById(R.id.indexContorTextView);
        this.idContor = "-";
        this.indexContor = "-";
        this.scaneazaIdContorTextView.setText(this.idContor);
        this.scaneazaIndexContorTextView.setText(this.indexContor);

        // Progress dialog
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setCancelable(false);

        //TODO sterge-ma
        this.editTextIndex = (EditText) findViewById(R.id.editTextIndex);

        // Reading the id
        final Activity that = this;
        this.scaneazaIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(that).initiateScan();
            }
        });

        // Reading the index
    }

    // When pressing the send button
    public void onClickTrimite(View view) {
        //TODO sterge-ma
        this.indexContor = this.editTextIndex.getText().toString();

        // check if data was measured
        if (!(this.indexContor.matches("-")) && !(this.idContor.matches("-"))) {
            this.queryManager = new QueryManager(this.username, this.idContor, this.indexContor, this);

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            CitireActivity.this.progressDialog.setMessage("Trimitere...");
                            showDialog();
                            CitireActivity.this.queryManager.run();
                            hideDialog();
                            Toast.makeText(getApplicationContext(), CitireActivity.this.responseMessage, Toast.LENGTH_LONG).show();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(
                    "ID = " + this.idContor + "\n" + "Index = " + this.indexContor + "\n" + "Trimiteti?")
                    .setPositiveButton("Da", dialogClickListener)
                    .setNegativeButton("Nu", dialogClickListener).show();
        }
        // else alert the user to enter all data
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Eroare");
            alertDialog.setMessage("Scaneaza atat ID-ul cat si indexul!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AppConfig.BUNDLE_ID, this.idContor);
        outState.putString(AppConfig.BUNDLE_INDEX, this.indexContor);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.idContor = savedInstanceState.getString(AppConfig.BUNDLE_ID);
        this.indexContor = savedInstanceState.getString(AppConfig.BUNDLE_INDEX);
        this.scaneazaIdContorTextView.setText(this.idContor);
        this.scaneazaIndexContorTextView.setText(this.indexContor);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Log.d("requestcode", Integer.toString(requestCode));
            switch (requestCode) {
                case IntentIntegrator.REQUEST_CODE: {
                    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    this.idContor = scanResult.getContents();
                    this.scaneazaIdContorTextView.setText(this.idContor);
                }
            }
        }
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
     * Sets the server's response message
     * @param responseMessage the server's response
     * */
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

}
