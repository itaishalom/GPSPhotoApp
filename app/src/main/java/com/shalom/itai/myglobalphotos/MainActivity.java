package com.shalom.itai.myglobalphotos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements GpsView {

    private String[] permissions = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    private static final int REQUESTS = 100;
    private RecyclerView recyclerView;
    private GpsPhotoRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_actitivty2);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView = (RecyclerView) findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        ActivityCompat.requestPermissions(this, permissions, REQUESTS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUESTS:
                boolean[] arrOfPermissions = new boolean[grantResults.length];
                for (int i = 0; i < arrOfPermissions.length; i++) {
                    arrOfPermissions[i] = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (!arrOfPermissions[i]) {
                        popUpForRequest();
                        return;
                    }
                }
        }
        startApp();
    }

    /**
     * Starts the application. Mainly, initiate the adapter and sets it to the recycler view
     */
    private void startApp() {
        adapter = new GpsPhotoRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Requests permission.
     */
    private void popUpForRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.permission_requested_message)
                .setTitle(R.string.alert_dialog_Permission_Request);
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUESTS);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void notifyDataSetChange() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public Context getViewContext() {
        return this;
    }
}
