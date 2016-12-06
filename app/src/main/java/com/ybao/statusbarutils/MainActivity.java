package com.ybao.statusbarutils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ybao.statusbar.StatusBarUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(t);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));
        int pT = StatusBarUtils.setImmerseLayout(this, true, true);
        t.setPadding(0, pT, 0, 0);
    }
}
