package com.ali.mytasks.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ali.mytasks.Model.TaskModel;
import com.ali.mytasks.Util.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {

    private Context context;
    //TAG
    public static final String TAG = "DB_Handler_Activity";

    public DataBaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.KEY_NAME + " TEXT,"
                + Constants.KEY_NO + " TEXT," + Constants.KEY_DATE + " LONG" + ");";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    /**
     * CURD OPERATIONS (READ,UPDATE,DELETE,CREATE ) METHODES
     */

    //ADD ITEm
    public void addItem(TaskModel task) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_NAME, task.getName());
        values.put(Constants.KEY_NO, task.getNo());
        values.put(Constants.KEY_DATE, java.lang.System.currentTimeMillis());

        db.insert(Constants.TABLE_NAME, null, values);
        Log.i(TAG, "Row Saved in DB");
        db.close();
    }

    //GET ITEM
    public TaskModel getItem(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.KEY_ID,
                        Constants.DB_NAME, Constants.KEY_NO, Constants.KEY_DATE}, Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        TaskModel taskModel = new TaskModel();
        taskModel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        taskModel.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_NAME)));
        taskModel.setNo(cursor.getString(cursor.getColumnIndex(Constants.KEY_NO)));
        //convert Date
        DateFormat dateFormat = DateFormat.getDateInstance();
        String formate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE))).getTime());
        taskModel.setDateItemAdded(formate);

        return taskModel;
    }

    //GET All Item
    public List<TaskModel> getAllItems() {

        SQLiteDatabase db = this.getReadableDatabase();
        List<TaskModel> taskModelList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.KEY_ID,
                        Constants.KEY_NAME, Constants.KEY_NO, Constants.KEY_DATE}, null, null, null, null,
                Constants.KEY_DATE + " DESC");
        if (cursor.moveToFirst()) {
            do {
                TaskModel taskModel = new TaskModel();
                taskModel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                taskModel.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_NAME)));
                taskModel.setNo(cursor.getString(cursor.getColumnIndex(Constants.KEY_NO)));
                //convert Date
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE))).getTime());
                taskModel.setDateItemAdded(formate);

                taskModelList.add(taskModel);
            } while (cursor.moveToNext());
        }
        return taskModelList;
    }

    // Update
    public int updateItem(TaskModel task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_NAME, task.getName());
        values.put(Constants.KEY_NO, task.getNo());
        values.put(Constants.KEY_DATE, java.lang.System.currentTimeMillis()); //get system time

        int index = db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "= ?", new String[]{String.valueOf(task.getId())});
        Log.i(TAG, "Row Updated in DB");
        db.close();
        return index;
    }

    // Delete Item
    public void deleteItem(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=?", new String[]{String.valueOf(id)});
        Log.i(TAG, "Row Deleted in DB");
        db.close();
    }

    // Get count
    public int getCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQ = "SELECT * FROM " + Constants.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQ, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;

    }
}
