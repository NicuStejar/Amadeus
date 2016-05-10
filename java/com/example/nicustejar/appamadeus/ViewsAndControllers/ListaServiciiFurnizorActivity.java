package com.example.nicustejar.appamadeus.ViewsAndControllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.nicustejar.appamadeus.AppManager.AppConfig;
import com.example.nicustejar.appamadeus.AppManager.SessionManager;
import com.example.nicustejar.appamadeus.Models.ListaFurnizori;
import com.example.nicustejar.appamadeus.R;

/**
 * Activity for selecting the desired service of the current Energy supplier
 * Selects the service and redirects to {@link CitireActivity}*/
public class ListaServiciiFurnizorActivity extends AppCompatActivity {

    // Model
    private ListaFurnizori listaFurnizori;

    // Views
    private ListView furnizoriListView;
    private Button logOutButton;

    // Connection to the corresponding data base
    private SessionManager sessionManager;

    // Bundles (for securing the Query)
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Loading components
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_servicii_furnizor);

        // Data for security received from log in
        Bundle bundle = getIntent().getExtras();
        this.username = bundle.getString(AppConfig.BUNDLE_USERNAME);
        this.password = bundle.getString(AppConfig.BUNDLE_PASS);

        // Loading more components
        this.furnizoriListView = (ListView) findViewById(R.id.furnizoriListView);
        this.logOutButton = (Button) findViewById(R.id.logOutButton);

        // Controller for the ListView
        this.listaFurnizori = new ListaFurnizori();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, listaFurnizori.getLista());
        this.furnizoriListView.setAdapter(adapter);
        this.furnizoriListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private AdapterView<?> parent;
            private View view;
            private int position;
            private long id;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                this.parent = parent;
                this.view = view;
                this.position = position;
                this.id = id;
                switch (position) {
                    case 0:
                        Intent run = new Intent(ListaServiciiFurnizorActivity.this, CitireActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(AppConfig.BUNDLE_USERNAME, username);
                        bundle.putString(AppConfig.BUNDLE_PASS, password);
                        run.putExtras(bundle);
                        startActivity(run);
                        break;
                }
            }
        });
    }

    public void onClickLogOut(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        ListaServiciiFurnizorActivity.this.sessionManager = new SessionManager(getApplicationContext());
                        sessionManager.setLogin(false);
                        Intent back = new Intent(ListaServiciiFurnizorActivity.this, LoginActivity.class);
                        startActivity(back);
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Doriti sa iesiti?")
                .setPositiveButton("Da", dialogClickListener)
                .setNegativeButton("Nu", dialogClickListener).show();
    }
}
