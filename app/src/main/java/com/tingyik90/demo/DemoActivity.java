package com.tingyik90.demo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tingyik90.snackprogressbar.SnackProgressBar;
import com.tingyik90.snackprogressbar.SnackProgressBarManager;

public class DemoActivity extends AppCompatActivity {

    /* variables */
    private int queue = 1;
    private SnackProgressBarManager snackProgressBarManager;
    private SnackProgressBar determinateType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        View view = findViewById(R.id.mainLayout);
        View floatingActionButton = findViewById(R.id.fab);
        // always have one instance of SnackProgressBarManager only in the activity
        snackProgressBarManager = new SnackProgressBarManager(view)
                // (optional) set the view which will animate with SnackProgressBar e.g. FAB when CoordinatorLayout is not used
                .setViewToMove(floatingActionButton)
                // (optional) change progressBar color, default = R.color.colorAccent
                .setProgressBarColor(R.color.colorAccent)
                // (optional) register onDisplayListener
                .setOnDisplayListener(new SnackProgressBarManager.OnDisplayListener() {
                    @Override
                    public void onShown(int onDisplayId) {
                        Toast.makeText(getApplicationContext(),
                                "SnackProgressBar(" + onDisplayId + ") shown!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDismissed(int onDisplayId) {
                        Toast.makeText(getApplicationContext(),
                                "SnackProgressBar(" + onDisplayId + ") dismissed!", Toast.LENGTH_SHORT).show();
                    }
                });

        /* create SnackProgressBar */
        SnackProgressBar actionType = new SnackProgressBar(
                SnackProgressBar.TYPE_ACTION, "TYPE_ACTION - Swipe to dismiss.")
                // (required) set action button
                .setAction("DISMISS", new SnackProgressBar.OnActionClickListener() {
                    @Override
                    public void onActionClick() {
                        Toast.makeText(getApplicationContext(), "Action Clicked!", Toast.LENGTH_SHORT).show();
                    }
                })
                // (optional) allow user swipe to dismiss, default = FALSE
                .setSwipeToDismiss(true);

        determinateType = new SnackProgressBar(
                SnackProgressBar.TYPE_DETERMINATE, "TYPE_DETERMINATE.")
                // (optional) set max progress, default = 100
                .setProgressMax(100)
                // (optional) show percentage, default = TRUE
                .setShowProgressPercentage(true);

        SnackProgressBar messageType = new SnackProgressBar(
                SnackProgressBar.TYPE_MESSAGE, "TYPE_MESSAGE - 0.")
                // (optional) allow user input, default = FALSE
                .setAllowUserInput(true)
                // (optional) allow user swipe to dismiss, default = FALSE
                .setSwipeToDismiss(true)
                // (optional) set icon
                .setIconResource(R.mipmap.ic_launcher);

        // assign storeId and store in SnackProgressBarManager
        snackProgressBarManager.put(actionType, 100);
        snackProgressBarManager.put(messageType, 300);
    }

    public void actionBtnClick(View view) {
        // call to show via storeId, dismiss after duration LENGTH_SHORT
        snackProgressBarManager.show(100, SnackProgressBarManager.LENGTH_SHORT);
    }

    public void actionLongBtnClick(View view) {
        SnackProgressBar actionLongType = new SnackProgressBar(
                SnackProgressBar.TYPE_ACTION, "TYPE_ACTION - If the action text is too long, a higher layout is used.")
                .setAction("LONG ACTION NAME", null)
                .setSwipeToDismiss(true);
        // call to show 4 seconds
        snackProgressBarManager.show(actionLongType, 4000);
    }

    public void determinateBtnClick(View view) {
        // or call to show via SnackProgressBar itself
        snackProgressBarManager.show(determinateType, SnackProgressBarManager.LENGTH_INDEFINITE);
        new CountDownTimer(5000, 50) {

            int i = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                snackProgressBarManager.setProgress(i);
            }

            @Override
            public void onFinish() {
                snackProgressBarManager.dismiss();
            }
        }.start();
    }

    public void indeterminateBtnClick(View view) {
        SnackProgressBar indeterminateType = new SnackProgressBar(SnackProgressBar.TYPE_INDETERMINATE, "TYPE_INDETERMINATE.");
        snackProgressBarManager.show(indeterminateType, SnackProgressBarManager.LENGTH_INDEFINITE);
        new CountDownTimer(8000, 2000) {

            int i = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                // get the currently showing indeterminateType and change the message
                SnackProgressBar snackProgressBar = snackProgressBarManager.getLastShown();
                if (snackProgressBar != null) {
                    snackProgressBar.setMessage("TYPE_INDETERMINATE - " + i);
                    // calling updateTo() will not hide and show again the SnackProgressBar
                    snackProgressBarManager.updateTo(snackProgressBar);
                }
            }

            @Override
            public void onFinish() {
                snackProgressBarManager.dismiss();
            }
        }.start();
    }

    /* click multiple times to look at the effect of queue. */
    public void messageBtnClick(View view) {
        // grab the stored SnackProgressBar and set the message.
        SnackProgressBar snackProgressBar = snackProgressBarManager.getSnackProgressBar(300);
        if (snackProgressBar != null) {
            snackProgressBar.setMessage("TYPE_MESSAGE - " + queue + ". " +
                    "The height of SnackProgressBar is increased.");
            // call to show via storeId, dismiss after duration LENGTH_LONG, assign an onDisplayId for callback.
            snackProgressBarManager.show(300, SnackProgressBarManager.LENGTH_LONG, queue);
            queue++;
        }
    }

    /* after clicking messageBtn multiple times, click this to clear all queued SnackProgressBars */
    public void clearBtnClick(View view) {
        // clear all queued SnackProgressBars
        snackProgressBarManager.dismissAll();
    }
}
