package com.ali.mytasks.Activities;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ali.mytasks.Data.DataBaseHandler;
import com.ali.mytasks.Model.TaskModel;
import com.ali.mytasks.R;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private EditText etEnterTask;
    private EditText etTaskNo;
    private Button btSave;
    private DataBaseHandler db;
    public static final String TAG = "Main_Activity";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DataBaseHandler(this);

        byPassActivity();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });
    }


    ///////////Alert Dialogs ///////////////
    public void createPopupDialog() {

        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        etEnterTask = view.findViewById(R.id.etAddTaskID);
        etTaskNo = view.findViewById(R.id.etNoID);
        btSave = view.findViewById(R.id.btSaveID);
        alertDialogBuilder.setView(view);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: save data to DB - Go To NEXT SCREEN
                if (!etEnterTask.getText().toString().isEmpty() &&
                        !etTaskNo.getText().toString().isEmpty()) {
                    saveToDB(view);
                } else {
                    Toast.makeText(MainActivity.this, "empty field", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveToDB(View view) {
        TaskModel taskModel = new TaskModel();
        String newItemName = etEnterTask.getText().toString();
        String newItemPrice = etTaskNo.getText().toString();
        taskModel.setName(newItemName);
        taskModel.setNo(newItemPrice);
        //save to DB
        db.addItem(taskModel);
        Log.i(TAG, "Item ID : " + String.valueOf(db.getCount()));
        Snackbar.make(view, "Item Saved", Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                //Start new activity
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                finish();
            }
        }, 1200);//Wait 1sec
    }

    public void byPassActivity() {
        //check DB if empty , if not go to ListActivity
        if (db.getCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }

    }
}
