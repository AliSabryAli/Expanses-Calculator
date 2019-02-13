package com.ali.mytasks.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.ali.mytasks.R;

public class DetailsActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvPrice;
    private TextView tvDate;
    private Button btSave;
    private Button btDelete;

    private int getID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvName = findViewById(R.id.tvNameIDDet);
        tvDate = findViewById(R.id.tvDateIDDet);
        tvPrice = findViewById(R.id.tvPriceIDDet);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tvName.setText(bundle.getString("name"));
            tvPrice.setText(bundle.getString("price"));
            tvDate.setText(bundle.getString("date"));
            getID = bundle.getInt("id");
        }
    }
}
