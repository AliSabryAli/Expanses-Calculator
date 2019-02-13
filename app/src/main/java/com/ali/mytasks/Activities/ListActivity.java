package com.ali.mytasks.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ali.mytasks.Data.DataBaseHandler;
import com.ali.mytasks.Model.TaskModel;
import com.ali.mytasks.R;
import com.ali.mytasks.UI.RecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecycleViewAdapter adapter;
    private List<TaskModel> modelItems;
    private List<TaskModel> modelList;
    private TaskModel taskModel;
    private DataBaseHandler db;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private EditText etEnterTask;
    private EditText etTaskNo;
    private Button btSave;

    private TextView tvTotal;
    private List<TaskModel> models;
    private List<Double> priceList;
    private double total;

    public static final String TAG = "List_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });

        // set recycleView
        db = new DataBaseHandler(this);
        recyclerView = findViewById(R.id.recycleViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        modelItems = new ArrayList<>();
        modelList = new ArrayList<>();

        //get all items from DB
        modelList = db.getAllItems();

        for (TaskModel model : modelList) {
            taskModel = new TaskModel();
            taskModel.setName(model.getName());
            taskModel.setNo(model.getNo());
            taskModel.setDateItemAdded(model.getDateItemAdded());
            taskModel.setId(model.getId());
            modelItems.add(taskModel);
        }
        adapter = new RecycleViewAdapter(this, modelItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            int count = db.getCount();
            if (count > 0) {
                createPopupTotal();
            } else {
                Toast.makeText(ListActivity.this, "No Items...", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    Toast.makeText(ListActivity.this, "empty field", Toast.LENGTH_SHORT).show();
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

        updateRecycleView();

        Snackbar.make(view, "Item Saved", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();

            }
        }, 1200);//Wait 1sec
    }

    //Update RecycleView After add new item
    private void updateRecycleView() {
        //get all items from DB
        modelList.clear();
        modelItems.clear();
        modelList = db.getAllItems();

        for (TaskModel model : modelList) {
            taskModel = new TaskModel();
            taskModel.setName(model.getName());
            taskModel.setNo(model.getNo());
            taskModel.setDateItemAdded(model.getDateItemAdded());
            taskModel.setId(model.getId());

            modelItems.add(taskModel);

        }
        adapter = new RecycleViewAdapter(this, modelItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    ///////////Show Total Alert Dialogs ///////////////
    public void createPopupTotal() {
        total = 0;
        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.total_popup, null);
        tvTotal = view.findViewById(R.id.tvTotalID);

        models = new ArrayList<>();
        priceList = new ArrayList<>();

        //get all item from DB
        db = new DataBaseHandler(this);
        models = db.getAllItems();

        for (TaskModel allTasks : models) {
            taskModel = new TaskModel();
            Double itemPrice = Double.parseDouble(allTasks.getNo());
            Log.i("Price item : ", " " + itemPrice);
            priceList.add(itemPrice);
            total = total + itemPrice;
        }
        tvTotal.setText(total + " AED");

        alertDialogBuilder.setView(view);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}



