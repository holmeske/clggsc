package com.sc.clgg.activity.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sc.clgg.R;
import com.sc.clgg.activity.basic.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterSucceedActivity extends AppCompatActivity {

    @OnClick(R.id.tv_ok)
    void c() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register_succeed);
        setTitle(getString(R.string.new_user_register));
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
