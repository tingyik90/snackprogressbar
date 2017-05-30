package com.tingyik90.demo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.tingyik90.snackprogressbar.SnackProgressBar;
import com.tingyik90.snackprogressbar.SnackProgressBarManager;

public class DemoActivity extends AppCompatActivity implements SnackProgressBarManager.OnActionClickListener {

    private View fab;
    private SnackProgressBarManager snackProgressBarManager;
    SnackProgressBar snackProgressBar;
    private static final String DEBUG_TAG = "SnackProgressBar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        fab = findViewById(R.id.fab);
        snackProgressBarManager = new SnackProgressBarManager(this);
        snackProgressBarManager.setViewToMove(fab);
        snackProgressBarManager.setOnActionClickListener(this);
        snackProgressBar = new SnackProgressBar(SnackProgressBar.TYPE_INDETERMINATE, "Testing", 100)
                .setAllowUserInput(true);
        snackProgressBarManager.add(snackProgressBar);
        Snackbar snackbar;
    }

    public void actionBtnClick(View view) {
        snackProgressBarManager.show(100, SnackProgressBarManager.LENGTH_LONG);
    }

    public void determinateBtnClick(View view) {
        snackProgressBarManager.getLastShown().setMessage("Loading1");
        snackProgressBarManager.update();
    }

    public void indeterminateBtnClick(View view) {
        snackProgressBarManager.show(
                new SnackProgressBar(SnackProgressBar.TYPE_MESSAGE, "Testing Message", 200)
                        .setAllowUserInput(true),
                SnackProgressBarManager.LENGTH_LONG);
    }

    public void messageBtnClick(View view) {
        snackProgressBarManager.clearAll();
    }

    @Override
    public void onActionClick() {
        snackProgressBarManager.dismiss();
    }
}
