package com.tingyik90.snackprogressbar;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Manager class handling all the SnackProgressBars added into queue.
 * It adds a RelativeLayout on top of the current activity.
 * There is only ONE instance of this layout.
 * All actions of showing, hiding and updating the SnackProgressBar are
 * reflected via this layout i.e. no new layout drawn.
 */
public class SnackProgressBarManager {

    /**
     * @hide
     */
    @Retention(SOURCE)
    @IntDef({LENGTH_LONG, LENGTH_SHORT, LENGTH_INDEFINITE})
    public @interface Duration {
    }

    /**
     * Show the Snackbar indefinitely.
     */
    public static final int LENGTH_INDEFINITE = 0;
    /**
     * Show the Snackbar for a short period of time.
     */
    public static final int LENGTH_SHORT = 1000;
    /**
     * Show the Snackbar for a long period of time.
     */
    public static final int LENGTH_LONG = 2750;


    /**
     * Default snackbar background color as per Material Design
     */
    public static final int SNACKBAR_COLOR_DEFAULT = R.color.snackBar_background;
    /**
     * Default message text color as per Material Design
     */
    public static final int MESSAGE_COLOR_DEFAULT = R.color.textWhitePrimary;
    /**
     * Default action text color as per Material Design i.e. R.color.colorAccent
     */
    public static final int ACTION_COLOR_DEFAULT = R.color.colorAccent;
    /**
     * Default progressBar color as per Material Design i.e. R.color.colorAccent
     */
    public static final int PROGRESSBAR_COLOR_DEFAULT = R.color.colorAccent;

    /* views */
    private View overlayLayout;
    private View mainLayout;
    private View snackBarLayout;
    private View viewToMove;
    private TextView messageText;
    private TextView progressText;
    private TextView actionText;
    private ProgressBar determinateProgressBar;
    private ProgressBar indeterminateProgressBar;
    private final int heightSingle, heightMulti;

    /* variables */
    private Context context;
    private OnActionClickListener onActionClickListener = null;
    // queue related
    private HashMap<Integer, SnackProgressBar> snackProgressBars = new HashMap<>();
    private ArrayList<Integer> snackProgressBarsToShow = new ArrayList<>();
    private ArrayList<Long> snackProgressBarDuration = new ArrayList<>();
    private ArrayList<Boolean> snackProgressBarDone = new ArrayList<>();
    private int type;
    private int isShowingQueue = 0;
    private int isShowingId = 0;
    // action related
    private boolean allowUserInput;
    private boolean swipeToDismiss;
    private boolean showProgressPercentage;
    // animation related
    private static final long ANIMATION_DURATION = 300;
    private static final String DEBUG_TAG = "SnackProgressBar";

    /**
     * Interface for passing user click on action
     */
    public interface OnActionClickListener {
        void onActionClick();
    }

    /**
     * Constructor
     *
     * @param activity activity to inflate the layout in
     */
    public SnackProgressBarManager(@NonNull Activity activity) {
        this.context = activity.getApplicationContext();
        heightSingle = (int) activity.getResources().getDimension(R.dimen.snackBar_height_single);  // height as per Material Design
        heightMulti = (int) activity.getResources().getDimension(R.dimen.snackBar_height_multi);    // height as per Material Design
        // create new view matching parent
        View contentView = View.inflate(activity, R.layout.snackprogressbar, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        activity.addContentView(contentView, layoutParams);
        overlayLayout = contentView.findViewById(R.id.snackProgressBar_layout_overlay);
        mainLayout = contentView.findViewById(R.id.snackProgressBar_layout_main);
        snackBarLayout = contentView.findViewById(R.id.snackProgressBar_layout_snackBar);
        messageText = (TextView) contentView.findViewById(R.id.snackProgressBar_txt_message);
        progressText = (TextView) contentView.findViewById(R.id.snackProgressBar_txt_progress);
        actionText = (TextView) contentView.findViewById(R.id.snackProgressBar_txt_action);
        determinateProgressBar = (ProgressBar) contentView.findViewById(R.id.snackProgressBar_progressbar_determinate);
        indeterminateProgressBar = (ProgressBar) contentView.findViewById(R.id.snackProgressBar_progressbar_indeterminate);
        // hide view
        mainLayout.setTranslationY(heightSingle);
        // set listeners
        setActionTextClickListener();
        setOnTouchListener();
    }

    /**
     * Add a SnackProgressBar into queue
     * @param snackProgressBar SnackProgressBar to add into queue
     */
    public void add(@NonNull SnackProgressBar snackProgressBar) {
        snackProgressBars.put(snackProgressBar.getId(), snackProgressBar);
    }

    public void show(@IntRange(from = 1) int id) {
        show(id, LENGTH_INDEFINITE);
    }

    public void show(@IntRange(from = 1) final int id, @Duration final long duration) {
        SnackProgressBar snackProgressBar = snackProgressBars.get(id);
        if (snackProgressBar != null) {
            show(snackProgressBar, duration);
        } else {
            throw new IllegalArgumentException("SnackProgressBar with id = " + id + " is not found!");
        }
    }

    public void show(@NonNull SnackProgressBar snackProgressBar) {
        show(snackProgressBar, LENGTH_INDEFINITE);
    }

    public void show(@NonNull final SnackProgressBar snackProgressBar, @Duration final long duration) {
        if (!snackProgressBars.containsKey(snackProgressBar.getId())) {
            snackProgressBars.put(snackProgressBar.getId(), snackProgressBar);
        }
        addToQueue(snackProgressBar, duration);
    }

    public void update() {
        if (isShowingId > 0) {
            update(isShowingId);
        }
    }

    public void update(@IntRange(from = 1) int id) {
        SnackProgressBar snackProgressBar = snackProgressBars.get(id);
        if (snackProgressBar != null) {
            update(snackProgressBar);
        } else {
            throw new IllegalArgumentException("SnackProgressBar with id = " + id + " is not found!");
        }
    }

    public void update(@NonNull SnackProgressBar snackProgressBar) {
        add(snackProgressBar);
        setAllowUserInput(snackProgressBar.isAllowUserInput());
        setSwipeToDismiss(snackProgressBar.getType(), snackProgressBar.isSwipeToDismiss());
        showProgressPercentage(snackProgressBar.isShowProgressPercentage());
        setType(snackProgressBar.getType());
        setProgressMax(snackProgressBar.getProgressMax());
        setAction(snackProgressBar.getAction());
        setMessage(snackProgressBar.getMessage());
    }

    public void dismiss() {
        overlayLayout.setVisibility(View.GONE);
        hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetMainLayout();
                nextQueue();
            }
        }, ANIMATION_DURATION);
    }

    public SnackProgressBarManager setOnActionClickListener(OnActionClickListener onActionClickListener) {
        this.onActionClickListener = onActionClickListener;
        return this;
    }

    public SnackProgressBarManager setViewToMove(View view) {
        this.viewToMove = view;
        return this;
    }

    public SnackProgressBarManager setOverlayLayoutAlpha(@FloatRange(from = 0, to = 1) float alpha) {
        overlayLayout.setAlpha(alpha);
        return this;
    }

    public SnackProgressBarManager setActionTextColor(int resId) {
        actionText.setTextColor(ContextCompat.getColor(context, resId));
        return this;
    }

    public SnackProgressBarManager setMessageTextColor(int resId) {
        messageText.setTextColor(ContextCompat.getColor(context, resId));
        return this;
    }

    public SnackProgressBarManager setProgressBarColor(int resId) {
        determinateProgressBar.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(context, resId), android.graphics.PorterDuff.Mode.SRC_IN);
        indeterminateProgressBar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(context, resId), android.graphics.PorterDuff.Mode.SRC_IN);
        return this;
    }

    public SnackProgressBarManager setSnackBarColor(int resId) {
        snackBarLayout.setBackgroundColor(ContextCompat.getColor(context, resId));
        return this;
    }

    public SnackProgressBarManager setProgress(@IntRange(from = 0) int progress) {
        determinateProgressBar.setProgress(progress);
        int progress100 = (int) (progress / (float) determinateProgressBar.getMax() * 100);
        String progressString = progress100 + "%";
        progressText.setText(progressString);
        return this;
    }

    public SnackProgressBarManager setAllowUserInput(boolean allowUserInput) {
        this.allowUserInput = allowUserInput;
        showOverlayLayout();
        return this;
    }

    public SnackProgressBar getSnackProgressBar(@IntRange(from = 1) int id) {
        return snackProgressBars.get(id);
    }

    public SnackProgressBar getLastShown() {
        return snackProgressBars.get(isShowingId);
    }

    public boolean isVisible() {
        return mainLayout.getTranslationY() < mainLayout.getHeight();
    }

    public void clearAll() {
        resetQueue();
        hide();
        overlayLayout.setVisibility(View.GONE);
    }

    private void addToQueue(SnackProgressBar snackProgressBar, long duration) {
        int queue = snackProgressBarsToShow.size();
        snackProgressBarsToShow.add(snackProgressBar.getId());
        snackProgressBarDuration.add(duration);
        snackProgressBarDone.add(false);
        if (queue == 0) {
            playQueue(queue);
        }
    }

    private void playQueue(final int queue) {
        Log.d(DEBUG_TAG, "playQueue = " + queue);
        if (queue < snackProgressBarsToShow.size()) {
            boolean isPreviousSnackBarDone = true;
            if (queue > 0) isPreviousSnackBarDone = snackProgressBarDone.get(queue - 1);
            if (isPreviousSnackBarDone) {
                long animationDelay = 0L;
                if (queue == 0 && isVisible()) {
                    Log.d(DEBUG_TAG, "hide");
                    hide();
                    animationDelay = ANIMATION_DURATION;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(DEBUG_TAG, "playQueue start");
                        int id = snackProgressBarsToShow.get(queue);
                        long duration = snackProgressBarDuration.get(queue);
                        isShowingQueue = queue;
                        isShowingId = id;
                        update(id);
                        showMainLayout();
                        showOverlayLayout();
                        if (duration == LENGTH_INDEFINITE) {
                            if (queue < snackProgressBarsToShow.size() - 1) {
                                Log.d(DEBUG_TAG, "update duration short");
                                duration = LENGTH_SHORT;
                            }
                        }
                        if (duration != LENGTH_INDEFINITE) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(DEBUG_TAG, "hide = " + queue);
                                    if (isShowingQueue == queue) {
                                        hide();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.d(DEBUG_TAG, "done = " + queue);
                                                nextQueue();
                                            }
                                        }, ANIMATION_DURATION);
                                    }
                                }
                            }, duration);
                        } else {
                            Log.d(DEBUG_TAG, "reset 1 = " + queue);
                            resetQueue();
                        }
                    }
                }, animationDelay);
            }
        } else {
            Log.d(DEBUG_TAG, "reset 2 = " + queue);
            resetQueue();
        }
    }

    private void nextQueue() {
        if (isShowingQueue < snackProgressBarsToShow.size()) {
            snackProgressBarDone.set(isShowingQueue, true);
            playQueue(isShowingQueue + 1);
        }
    }

    private void resetQueue() {
        isShowingQueue = 0;
        snackProgressBarsToShow.clear();
        snackProgressBarDuration.clear();
        snackProgressBarDone.clear();
    }

    private void hide() {
        if (isVisible()) {
            mainLayout.animate().translationY(mainLayout.getHeight());
        }
        if (viewToMove != null && viewToMove.getTranslationY() != 0) {
            viewToMove.animate().translationY(0);
        }
    }

    private void showMainLayout() {
        mainLayout.animate().translationY(0);
        if (viewToMove != null) {
            viewToMove.animate().translationY(-1 * mainLayout.getHeight());
        }
    }

    private void showOverlayLayout() {
        if (allowUserInput) {
            overlayLayout.setVisibility(View.GONE);
        } else {
            overlayLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setType(int type) {
        // update type
        this.type = type;
        // check view if isVisible
        boolean isVisible = isVisible();
        // animate hide first
        if (isVisible) {
            mainLayout.setVisibility(View.GONE);
        }
        // update view
        showProgressPercentage(showProgressPercentage);
        switch (type) {
            case SnackProgressBar.TYPE_ACTION:
                actionText.setVisibility(View.VISIBLE);
                determinateProgressBar.setVisibility(View.GONE);
                indeterminateProgressBar.setVisibility(View.GONE);
                break;
            case SnackProgressBar.TYPE_DETERMINATE:
                actionText.setVisibility(View.GONE);
                determinateProgressBar.setVisibility(View.VISIBLE);
                indeterminateProgressBar.setVisibility(View.GONE);
                break;
            case SnackProgressBar.TYPE_INDETERMINATE:
                actionText.setVisibility(View.GONE);
                determinateProgressBar.setVisibility(View.GONE);
                indeterminateProgressBar.setVisibility(View.VISIBLE);
                break;
            case SnackProgressBar.TYPE_MESSAGE:
                actionText.setVisibility(View.GONE);
                determinateProgressBar.setVisibility(View.GONE);
                indeterminateProgressBar.setVisibility(View.GONE);
                break;
        }
        // show again if isVisible
        if (isVisible) {
            mainLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setMessage(final String message) {
        messageText.setText(message);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) snackBarLayout.getLayoutParams();
        layoutParams.height = messageText.getLineCount() < 2 ? heightSingle : heightMulti;
        snackBarLayout.setLayoutParams(layoutParams);
    }

    private void setSwipeToDismiss(int type, boolean swipeToDismiss) {
        switch (type) {
            case SnackProgressBar.TYPE_ACTION:
            case SnackProgressBar.TYPE_MESSAGE:
                this.swipeToDismiss = swipeToDismiss;
                break;
            case SnackProgressBar.TYPE_DETERMINATE:
            case SnackProgressBar.TYPE_INDETERMINATE:
                this.swipeToDismiss = false;
                break;
        }
    }

    private void showProgressPercentage(boolean show) {
        this.showProgressPercentage = show;
        if (showProgressPercentage && type == SnackProgressBar.TYPE_DETERMINATE) {
            progressText.setVisibility(View.VISIBLE);
        } else {
            progressText.setVisibility(View.GONE);
        }
    }

    private void setProgressMax(int progressMax) {
        determinateProgressBar.setMax(progressMax);
    }

    private void setAction(final String action) {
        actionText.setText(action);
    }

    private void setActionTextClickListener() {
        actionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionClickListener != null) {
                    onActionClickListener.onActionClick();
                }
            }
        });
    }

    private void setOnTouchListener() {
        messageText.setOnTouchListener(new View.OnTouchListener() {

            private float startX, endX;
            private VelocityTracker velocityTracker;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (swipeToDismiss) {
                    int index = event.getActionIndex();
                    int pointerId = event.getPointerId(index);
                    int action = event.getActionMasked();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // track initial coordinate
                            startX = event.getRawX();
                            // track velocity
                            velocityTracker = VelocityTracker.obtain();
                            velocityTracker.addMovement(event);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            // track velocity
                            velocityTracker.addMovement(event);
                            // animate layout
                            float moveX = event.getRawX();
                            mainLayout.setTranslationX(moveX - startX);
                            mainLayout.setAlpha(1 - Math.abs(moveX - startX) / mainLayout.getWidth());
                            break;
                        case MotionEvent.ACTION_UP:
                            // track final coordinate
                            endX = event.getRawX();
                            // get velocity and return resources
                            velocityTracker.computeCurrentVelocity(1000);
                            float velocity = Math.abs(VelocityTrackerCompat.getXVelocity(velocityTracker, pointerId));
                            velocityTracker.recycle();
                            velocityTracker = null;
                            // animate layout
                            boolean toSwipeOut = false;
                            // swipe out if layout moved more than half of the screen
                            if (Math.abs(endX - startX) / mainLayout.getWidth() > 0.5) {
                                toSwipeOut = true;
                            }
                            // swipe out if velocity is high
                            if (Math.abs(velocity) > 1000) {
                                toSwipeOut = true;
                            }
                            // determine swipe out direction
                            if (toSwipeOut) {
                                if (endX - startX > 100) {
                                    swipeOut(true);
                                } else if (endX - startX < -100) {
                                    swipeOut(false);
                                }
                            } else {
                                // else, return to original
                                swipeIn();
                            }
                            break;
                    }
                }
                return true;
            }
        });
    }

    private void swipeOut(boolean toRight) {
        float direction = toRight ? 1 : -1;
        overlayLayout.setVisibility(View.GONE);
        mainLayout.animate().translationX(direction * mainLayout.getWidth());
        mainLayout.animate().alpha(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // return to hide position
                if (viewToMove != null) {
                    viewToMove.animate().translationY(0);
                }
                resetMainLayout();
                nextQueue();
            }
        }, ANIMATION_DURATION);
    }

    private void resetMainLayout() {
        mainLayout.setTranslationX(0);
        mainLayout.setTranslationY(mainLayout.getHeight());
        mainLayout.setAlpha(1f);
        setProgress(0);
    }

    private void swipeIn() {
        mainLayout.animate().translationX(0);
        mainLayout.animate().alpha(1);
    }
}
