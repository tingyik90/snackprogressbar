package com.tingyik90.demo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tingyik90.snackprogressbar.SnackProgressBar;

public class DemoActivity extends AppCompatActivity implements SnackProgressBar.OnActionClickListener {

    private SnackProgressBar snackProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        snackProgressBar = new SnackProgressBar.Builder(this, SnackProgressBar.Type.ACTION, "Click \"SHOW\" to allow user input again.")
                .setActionText("SHOW")
                .allowUserInput(false)
                .resetAfterDismiss(true)
                .setOnActionClickListener(this)
                .build();
        snackProgressBar.show();
    }

    public void actionBtnClick(View view) {
        snackProgressBar.dismiss();
        snackProgressBar.show();
    }

    public void determinateBtnClick(View view) {
        snackProgressBar.dismiss();
        snackProgressBar.setType(SnackProgressBar.Type.DETERMINATE);
        snackProgressBar.setMessage("Loading for 5 seconds…");
        snackProgressBar.showProgressPercentage(true);
        snackProgressBar.setProgressMax(100);
        snackProgressBar.setProgress(0);
        snackProgressBar.show();
        new CountDownTimer(5000, 50) {
            int i = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                snackProgressBar.setProgress(i);
            }

            @Override
            public void onFinish() {
                snackProgressBar.dismiss();
            }
        }.start();
    }

    public void indeterminateBtnClick(View view) {
        snackProgressBar.dismiss();
        snackProgressBar.setType(SnackProgressBar.Type.INDETERMINATE);
        snackProgressBar.setMessage("Loading for 3 seconds…");
        snackProgressBar.show();
        new CountDownTimer(3000, 50) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                snackProgressBar.dismiss();
            }
        }.start();
    }

    public void messageBtnClick(View view) {
        snackProgressBar.dismiss();
        snackProgressBar.setType(SnackProgressBar.Type.MESSAGE);
        snackProgressBar.allowUserInput(true);
        snackProgressBar.setMessage("If the message is long, it will automatically increase the height of the SnackBar.");
        snackProgressBar.show();
    }

    @Override
    public void onActionClick() {
        snackProgressBar.allowUserInput(true);
    }
}
