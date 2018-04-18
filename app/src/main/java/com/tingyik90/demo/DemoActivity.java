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
                // (optional) change background color, default = BACKGROUND_COLOR_DEFAULT (#FF323232)
                .setBackgroundColor(SnackProgressBarManager.BACKGROUND_COLOR_DEFAULT)
                // (optional) change text size, default = 14sp
                .setTextSize(14)
                // (optional) set max lines, default = 2
                .setMessageMaxLines(2)
                // (optional) register onDisplayListener
                .setOnDisplayListener(new SnackProgressBarManager.OnDisplayListener() {
                    @Override
                    public void onShown(SnackProgressBar snackProgressBar, int onDisplayId) {
                        Bundle bundle = snackProgressBar.getBundle();
                        if (bundle != null) {
                            int queueNo = bundle.getInt("queue");
                            String toast = "Showing queue " + queueNo + "!";
                            Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDismissed(SnackProgressBar snackProgressBar, int onDisplayId) {
                        Bundle bundle = snackProgressBar.getBundle();
                        if (bundle != null) {
                            int queueNo = bundle.getInt("queue");
                            String toast = "Dismissing queue " + queueNo + "!";
                            Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        /* create SnackProgressBar */
        SnackProgressBar normalTypeWithAction = new SnackProgressBar(
                SnackProgressBar.TYPE_NORMAL, "TYPE_NORMAL - If the action text is too long, a higher layout is used.")
                // (required) set action button
                .setAction("DISMISS", new SnackProgressBar.OnActionClickListener() {
                    @Override
                    public void onActionClick() {
                        Toast.makeText(getApplicationContext(), "Action Clicked!", Toast.LENGTH_SHORT).show();
                    }
                });

        SnackProgressBar horizontalType = new SnackProgressBar(
                SnackProgressBar.TYPE_HORIZONTAL, "TYPE_HORIZONTAL - Loading...")
                // (optional) set the type of progressBar, default = FALSE
                .setIsIndeterminate(false)
                // (optional) set max progress, default = 100
                .setProgressMax(100)
                // (optional) show percentage, default = FALSE
                .setShowProgressPercentage(true);

        SnackProgressBar horizontalTypeWithAction = new SnackProgressBar(
                SnackProgressBar.TYPE_HORIZONTAL, "TYPE_HORIZONTAL - Loading...")
                .setIsIndeterminate(true)
                .setAction("DISMISS", new SnackProgressBar.OnActionClickListener() {
                    @Override
                    public void onActionClick() {
                        Toast.makeText(getApplicationContext(), "Action Clicked!", Toast.LENGTH_SHORT).show();
                    }
                });

        // assign storeId and store in SnackProgressBarManager (this is for easy calling only, not a required step)
        snackProgressBarManager.put(normalTypeWithAction, 100);
        snackProgressBarManager.put(horizontalType, 200);
        snackProgressBarManager.put(horizontalTypeWithAction, 300);
    }

    public void normalBtnClick(View view) {
        // create a normal snackProgressBar
        SnackProgressBar normalType = new SnackProgressBar(
                SnackProgressBar.TYPE_NORMAL, "TYPE_NORMAL - " + queue)
                // (optional) allow user input, default = FALSE
                .setAllowUserInput(true)
                // (optional) allow user swipe to dismiss, default = FALSE
                .setSwipeToDismiss(true)
                // (optional) set icon
                .setIconResource(R.mipmap.ic_launcher);
        // create a bundle and attach to snackProgressBar which can be retrieved via OnDisplayListener
        Bundle bundle = new Bundle();
        bundle.putInt("queue", queue);
        normalType.putBundle(bundle);
        // dismiss after duration LENGTH_LONG, assign an onDisplayId for callback.
        snackProgressBarManager.show(normalType, SnackProgressBarManager.LENGTH_LONG, 5000);
        queue++;
    }

    public void normalWithActionBtnClick(View view) {
        // call to show via storeId for 4 seconds
        snackProgressBarManager.show(100, 4000);
    }

    public void horizontalBtnClick(View view) {
        // call to show via storeId indefinitely, update the progress
        snackProgressBarManager.show(200, SnackProgressBarManager.LENGTH_INDEFINITE);
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

    public void horizontalWithActionBtnClick(View view) {
        // call to show via storeId for LENGTH_LONG
        snackProgressBarManager.show(300, SnackProgressBarManager.LENGTH_LONG);
    }

    public void circularBtnClick(View view) {
        SnackProgressBar circularType = new SnackProgressBar(
                SnackProgressBar.TYPE_CIRCULAR, "TYPE_CIRCULAR - Loading...")
                .setIsIndeterminate(false)
                .setProgressMax(100)
                .setShowProgressPercentage(true);
        snackProgressBarManager.show(circularType, SnackProgressBarManager.LENGTH_INDEFINITE);
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

    public void circularWithActionBtnClick(View view) {
        // this type of layout is not recommended, simply because it is ugly.
        SnackProgressBar circularTypeWithAction = new SnackProgressBar(
                SnackProgressBar.TYPE_CIRCULAR, "TYPE_CIRCULAR - Loading...")
                .setIsIndeterminate(true)
                .setAction("DISMISS", new SnackProgressBar.OnActionClickListener() {
                    @Override
                    public void onActionClick() {
                        Toast.makeText(getApplicationContext(), "Action Clicked!", Toast.LENGTH_SHORT).show();
                    }
                });
        snackProgressBarManager.show(circularTypeWithAction, SnackProgressBarManager.LENGTH_LONG);
    }

    /* after clicking normalBtn multiple times, click this to clear all queued SnackProgressBars */
    public void clearBtnClick(View view) {
        // clear all queued SnackProgressBars
        snackProgressBarManager.dismissAll();
    }
}
