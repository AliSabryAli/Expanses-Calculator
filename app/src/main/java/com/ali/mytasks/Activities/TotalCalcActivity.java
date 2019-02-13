package com.ali.mytasks.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ali.mytasks.Data.DataBaseHandler;
import com.ali.mytasks.Model.TaskModel;
import com.ali.mytasks.R;

import java.util.ArrayList;
import java.util.List;

public class TotalCalcActivity extends AppCompatActivity {

    private TextView tvTotal;
    private DataBaseHandler db;
    private TaskModel taskModel;
    private List<TaskModel> models;
    private List<Double> priceList;
    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_calc);

        tvTotal = findViewById(R.id.tvTotalID);

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

    }
}
