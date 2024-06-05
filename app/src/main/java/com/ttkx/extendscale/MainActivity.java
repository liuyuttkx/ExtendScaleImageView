package com.ttkx.extendscale;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExtendScaleImageView iv = findViewById(R.id.scaleIv);
        iv.setScaleType(ExtendScaleImageView.ExtendScalType.TOP_CROP);
        iv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
    }
}