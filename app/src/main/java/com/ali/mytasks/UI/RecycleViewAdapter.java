package com.ali.mytasks.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ali.mytasks.Activities.DetailsActivity;
import com.ali.mytasks.Activities.MainActivity;
import com.ali.mytasks.Data.DataBaseHandler;
import com.ali.mytasks.Model.TaskModel;
import com.ali.mytasks.R;

import java.util.List;
import java.util.zip.Inflater;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private Context context;
    private List<TaskModel> modelItems;

    private android.support.v7.app.AlertDialog.Builder alertDialogBuilder;
    private android.support.v7.app.AlertDialog alertDialog;
    private EditText etEnterTask;
    private EditText etTaskNo;
    private Button btSave;

    public RecycleViewAdapter(Context context, List<TaskModel> modelItems) {
        this.context = context;
        this.modelItems = modelItems;
    }


    @NonNull
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.ViewHolder holder, int position) {

        TaskModel taskModel = modelItems.get(position);
        holder.itemName.setText(taskModel.getName());
        holder.itemPrice.setText(taskModel.getNo());
        holder.dateAdd.setText(taskModel.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return modelItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemName;
        public TextView itemPrice;
        public TextView dateAdd;
        public Button btEdit;
        public Button btDelete;
        public int id;

        public ViewHolder(@NonNull View view, final Context ctx) {
            super(view);
            context = ctx;
            itemName = view.findViewById(R.id.tvNameID);
            itemPrice = view.findViewById(R.id.tvPriceID);
            dateAdd = view.findViewById(R.id.tvDateID);
            btEdit = view.findViewById(R.id.btEditID);
            btDelete = view.findViewById(R.id.btDeleteID);

            btDelete.setOnClickListener(this);
            btEdit.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //go to next screen
                    int pos = getAdapterPosition();
                    TaskModel taskModel = modelItems.get(pos);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("name", taskModel.getName());
                    intent.putExtra("price", taskModel.getNo());
                    intent.putExtra("date", taskModel.getDateItemAdded());
                    intent.putExtra("id", taskModel.getId());
                    context.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btEditID:
                    int pos = getAdapterPosition();
                    TaskModel task = modelItems.get(pos);
                    editItem(task);
                    break;
                case R.id.btDeleteID:
                    pos = getAdapterPosition();
                    task = modelItems.get(pos);
                    deletItem(task.getId());
                    break;

            }

        }

        //delete item
        public void deletItem(final int index) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            // Add the buttons
            builder.setMessage(R.string.dialog_msg)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            DataBaseHandler db = new DataBaseHandler(context);
                            db.deleteItem(index);
                            modelItems.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            dialog.dismiss();
                        }
                    });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    dialog.dismiss();
                }
            });
            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        public void editItem(final TaskModel taskModel) {

            alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.popup, null);
            etEnterTask = view.findViewById(R.id.etAddTaskID);
            etTaskNo = view.findViewById(R.id.etNoID);
            btSave = view.findViewById(R.id.btSaveID);
            alertDialogBuilder.setView(view);
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            // Edit Header title
            final TextView textView = view.findViewById(R.id.tvTitleID);
            textView.setText("Edit Item");

            //Old data
            etEnterTask.setText(taskModel.getName());
            etTaskNo.setText(taskModel.getNo());

            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: save data to DB - Go To NEXT SCREEN
                    if (!etEnterTask.getText().toString().isEmpty() &&
                            !etTaskNo.getText().toString().isEmpty()) {
                        DataBaseHandler db = new DataBaseHandler(context);

                        taskModel.setName(etEnterTask.getText().toString());
                        taskModel.setNo(etTaskNo.getText().toString());

                        db.updateItem(taskModel);
                        notifyItemChanged(getAdapterPosition(), taskModel);
                        alertDialog.dismiss();

                    } else {
                        Toast.makeText(context, "empty field", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}
