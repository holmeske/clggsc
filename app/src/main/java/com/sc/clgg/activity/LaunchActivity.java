package com.sc.clgg.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sc.clgg.R;

/**
 * @author lvke
 */
public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.scale_in, R.anim.alpha_out);
        finish();
    }

}
