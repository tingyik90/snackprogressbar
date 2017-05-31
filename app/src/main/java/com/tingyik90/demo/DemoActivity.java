package com.tingyik90.demo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tingyik90.snackprogressbar.SnackProgressBar;
import com.tingyik90.snackprogressbar.SnackProgressBarManager;

public class DemoActivity extends AppCompatActivity implements SnackProgressBarManager.OnActionClickListener {

    private SnackProgressBarManager snackProgressBarManager;
    private SnackProgressBar determinateType;
    private SnackProgressBar indeterminateType;
    private int queue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        View floatingActionButton = findViewById(R.id.fab);
        // always only have one instance of SnackProgressBarManager in the activity
        snackProgressBarManager = new SnackProgressBarManager(this)
                .setViewToMove(floatingActionButton)    // (optional) set the view which will animate with SnackProgressBar
                .setOnActionClickListener(this)         // (optional) set the action click listener
                .setProgressBarColor(R.color.colorAccent);   // (optional) change progressbar color

        // create SnackProgressBar
        SnackProgressBar actionType = new SnackProgressBar(
                SnackProgressBar.TYPE_ACTION, "Action type. Click or swipe to dismiss.", 100)
                .setAction("DISMISS")       // (required) set action button
                .setSwipeToDismiss(true);   // (optional) allow user swipe to dismiss, default = FALSE

        determinateType = new SnackProgressBar(
                SnackProgressBar.TYPE_DETERMINATE, "Loading for 5 seconds...", 200)
                .setProgressMax(100)               // (optional) set max progress, default = 100
                .setShowProgressPercentage(true);   // (optional) show percentage, default = TRUE

        indeterminateType = new SnackProgressBar(
                SnackProgressBar.TYPE_INDETERMINATE, "Loading for 2 seconds", 300);

        SnackProgressBar messageType = new SnackProgressBar(
                SnackProgressBar.TYPE_MESSAGE, "This is a message type SnackProgressBar.", 400)
                .setAllowUserInput(true)    // (optional) allow user input, default = FALSE
                .setSwipeToDismiss(true);   // (optional) allow user swipe to dismiss, default = FALSE

        // add to SnackProgressBarManager
        snackProgressBarManager.add(actionType);
        snackProgressBarManager.add(determinateType);
        snackProgressBarManager.add(messageType);
    }

    public void actionBtnClick(View view) {
        // can call to show via id
        snackProgressBarManager.show(100);
    }

    public void determinateBtnClick(View view) {
        // or call to show via SnackProgressBar itself
        snackProgressBarManager.show(determinateType);
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
        // or call to show without adding it first
        snackProgressBarManager.show(indeterminateType);
        new CountDownTimer(8000, 2000) {

            int i = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                // get the currently showing indeterminateType and change the message
                snackProgressBarManager.getLastShowing().setMessage("Now loading another 2 seconds... (" + i + ")");
                // calling updateTo() will not hide the SnackProgressBar and show it again as animation
                snackProgressBarManager.updateLastShowing();
            }

            @Override
            public void onFinish() {
                // change it to messageType which was added before
                snackProgressBarManager.show(400);
                /* If you've clicked on "Message" button, then the message shown will be as shown below.
                * Else, it will be the original "This is a message type SnackProgressBar."*/
            }
        }.start();
    }

    public void messageBtnClick(View view) {
        // grab the stored SnackProgressBar and set the message.
        // note that this is changing the stored SnackProgressBar itself
        snackProgressBarManager.getSnackProgressBar(400).setMessage("This is a message number " + queue + ". " +
                "If the message is too long, the height of snackbar will increase.");
        // change will not be reflected until show() / updateTo() is called, dismiss after duration LENGTH_LONG
        snackProgressBarManager.show(400, SnackProgressBarManager.LENGTH_LONG);
        queue++;
        /* click multiple times to look at the effect of queue. */
    }

    public void clearBtnClick(View view) {
        // clear all queued SnackProgressBars
        snackProgressBarManager.clearAll();
        /* after clicking messageBtn multiple times, click this to clear all queued SnackProgressBars */
    }

    // implements SnackProgressBarManager.OnActionClickListener
    @Override
    public void onActionClick() {
        snackProgressBarManager.dismiss();
    }
}
