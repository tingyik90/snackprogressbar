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
 * Manager class handling all the SnackProgressBars added.
 * It adds a RelativeLayout on top of the current activity.
 * <p>
 * There is only ONE instance of this layout (called "MainLayout" here).
 * All actions of showing, hiding and updating the SnackProgressBar are
 * reflected via this layout i.e. no new layout drawn.
 * </p>
 * <p>
 * This class queues the SnackProgressBars to be shown.
 * It will dismiss the SnackProgressBar according to its desired duration before showing the next in queue.
 * </p>
 */
public class SnackProgressBarManager {

    @IntDef({LENGTH_LONG, LENGTH_SHORT, LENGTH_INDEFINITE})
    @Retention(SOURCE)
    @interface Duration {}

    /**
     * Show the SnackProgressBar indefinitely.
     * Note that this will be changed to LENGTH_LONG and dismissed
     * if there is another SnackProgressBar in queue after.
     */
    public static final int LENGTH_INDEFINITE = 0;
    /**
     * Show the SnackProgressBar for a short period of time.
     */
    public static final int LENGTH_SHORT = 1000;
    /**
     * Show the SnackProgressBar for a long period of time.
     */
    public static final int LENGTH_LONG = 2750;

    /**
     * Default snackbar background color as per Material Design.
     */
    public static final int SNACKBAR_COLOR_DEFAULT = R.color.snackBar_background;
    /**
     * Default message text color as per Material Design.
     */
    public static final int MESSAGE_COLOR_DEFAULT = R.color.textWhitePrimary;
    /**
     * Default action text color as per Material Design i.e. R.color.colorAccent.
     */
    public static final int ACTION_COLOR_DEFAULT = R.color.colorAccent;
    /**
     * Default progressBar color as per Material Design i.e. R.color.colorAccent.
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
    private ArrayList<SnackProgressBar> queueBars = new ArrayList<>();
    private ArrayList<Long> queueDurations = new ArrayList<>();
    private ArrayList<Boolean> queueDone = new ArrayList<>();
    private int type;
    private int currentQueue = 0;
    private SnackProgressBar currentBar = null;
    // action related
    private boolean allowUserInput;
    private boolean swipeToDismiss;
    // animation related
    private static final float SWIPE_OUT_VELOCITY = 800f;
    private static final long ANIMATION_DURATION = 300;
    private boolean toShow = false;

    /**
     * Interface for passing user click on action button.
     */
    public interface OnActionClickListener {
        void onActionClick();
    }

    /**
     * Constructor
     *
     * @param activity Activity to inflate the SnackProgressBar RelativeLayout in.
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
        overlayLayout = activity.findViewById(R.id.snackProgressBar_layout_overlay);
        mainLayout = activity.findViewById(R.id.snackProgressBar_layout_main);
        snackBarLayout = activity.findViewById(R.id.snackProgressBar_layout_snackBar);
        messageText = (TextView) activity.findViewById(R.id.snackProgressBar_txt_message);
        progressText = (TextView) activity.findViewById(R.id.snackProgressBar_txt_progress);
        actionText = (TextView) activity.findViewById(R.id.snackProgressBar_txt_action);
        determinateProgressBar = (ProgressBar) activity.findViewById(R.id.snackProgressBar_progressbar_determinate);
        indeterminateProgressBar = (ProgressBar) activity.findViewById(R.id.snackProgressBar_progressbar_indeterminate);
        // hide view
        mainLayout.setTranslationY(heightSingle);
        // set listeners
        setActionTextClickListener();
        setOnTouchListener();
    }

    /**
     * Add a SnackProgressBar into SnackProgressBarManager's HashMap to perform further action.
     * The SnackProgressBar is uniquely identified with the SnackProgressBar's id.
     * The first SnackProgressBar will be overwritten if another SnackProgressBar with the same id
     * is added to the list.
     *
     * @param snackProgressBar SnackProgressBar to be added.
     * @see SnackProgressBar
     */
    public void add(@NonNull SnackProgressBar snackProgressBar, @IntRange(from = 1) int id) {
        snackProgressBars.put(id, snackProgressBar);
    }

    /**
     * Show the SnackProgressBar based on its id with {@link #LENGTH_INDEFINITE}.
     * If another SnackProgressBar is already showing in the MainLayout,
     * this SnackProgressBar will be queued and shown accordingly after those queued are dismissed.
     *
     * @param id id of the SnackProgressBar to be shown (must be already added to HashMap).
     * @see #add(SnackProgressBar, int)
     */
    public void show(@IntRange(from = 1) int id) {
        show(id, LENGTH_INDEFINITE);
    }

    /**
     * Show the SnackProgressBar based on its id with the specified duration.
     * If another SnackProgressBar is already showing in the MainLayout,
     * this SnackProgressBar will be queued and shown accordingly after those queued are dismissed.
     *
     * @param id       id of the SnackProgressBar to be shown (must be already added to HashMap).
     * @param duration duration to show the SnackProgressBar of either
     *                 {@link #LENGTH_SHORT}, {@link #LENGTH_LONG}, {@link #LENGTH_INDEFINITE}.
     * @see #add(SnackProgressBar, int)
     */
    public void show(@IntRange(from = 1) final int id, @Duration final long duration) {
        SnackProgressBar snackProgressBar = snackProgressBars.get(id);
        if (snackProgressBar != null) {
            show(snackProgressBar, duration);
        } else {
            throw new IllegalArgumentException("SnackProgressBar with id = " + id + " is not found!");
        }
    }

    /**
     * Show the SnackProgressBar based on its id with {@link #LENGTH_INDEFINITE}.
     * If another SnackProgressBar is already showing in the MainLayout,
     * this SnackProgressBar will be queue and shown accordingly after those queued are dismissed.
     *
     * @param snackProgressBar SnackProgressBar to be shown.
     */
    public void show(@NonNull SnackProgressBar snackProgressBar) {
        show(snackProgressBar, LENGTH_INDEFINITE);
    }

    /**
     * Show the SnackProgressBar based on its id with the specified duration.
     * If another SnackProgressBar is already showing in the MainLayout,
     * this SnackProgressBar will be queued and shown accordingly after those queued are dismissed.
     *
     * @param snackProgressBar SnackProgressBar to be shown.
     * @param duration         duration to show the SnackProgressBar of either
     *                         {@link #LENGTH_SHORT}, {@link #LENGTH_LONG}, {@link #LENGTH_INDEFINITE}.
     */
    public void show(@NonNull SnackProgressBar snackProgressBar, @Duration long duration) {
        addToQueue(snackProgressBar, duration);
    }

    /**
     * Update the last showing SnackProgressBar.
     */
    public void updateLastShowing() {
        updateTo(currentBar);
    }

    /**
     * This will update the MainLayout without dismissing it e.g. updating message without animation.
     * <p>
     * Note: This does not change the queue.
     * </p>
     *
     * @param id id of the SnackProgressBar to be updated to (must be already added to HashMap).
     */
    public void updateTo(@IntRange(from = 1) int id) {
        SnackProgressBar snackProgressBar = snackProgressBars.get(id);
        if (snackProgressBar != null) {
            updateTo(snackProgressBar);
        } else {
            throw new IllegalArgumentException("SnackProgressBar with id = " + id + " is not found!");
        }
    }

    /**
     * This will update the MainLayout without dismissing it e.g. updating message without animation.
     * <p>
     * Note: This does not change the queue.
     * </p>
     *
     * @param snackProgressBar SnackProgressBar to be updated to.
     */
    public void updateTo(@NonNull SnackProgressBar snackProgressBar) {
        setType(snackProgressBar.getType());
        showProgressPercentage(snackProgressBar.isShowProgressPercentage());
        setAction(snackProgressBar.getAction());
        setAllowUserInput(snackProgressBar.isAllowUserInput());
        setSwipeToDismiss(snackProgressBar.getType(), snackProgressBar.isSwipeToDismiss());
        setProgressMax(snackProgressBar.getProgressMax());
        setMessage(snackProgressBar.getMessage());
    }

    /**
     * Dismiss the currently showing SnackProgressBar.
     * The next SnackProgressBar in queue will be shown.
     */
    public void dismiss() {
        overlayLayout.setVisibility(View.GONE);
        hideMainLayout();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetMainLayout();
                nextQueue();
            }
        }, ANIMATION_DURATION);
    }

    /**
     * Clear all the SnackProgressBar that is in queue to be shown.
     * The currently shown SnackProgressBar will also be dismissed.
     */
    public void clearAll() {
        resetQueue();
        hideMainLayout();
        overlayLayout.setVisibility(View.GONE);
    }

    /**
     * Attach actionClickListener.
     *
     * @param onActionClickListener OnActionClickListener.
     */
    public SnackProgressBarManager setOnActionClickListener(OnActionClickListener onActionClickListener) {
        this.onActionClickListener = onActionClickListener;
        return this;
    }

    /**
     * Pass the view (usually FloatingActionButton) to move up or down as SnackProgressBar is shown or dismissed.
     *
     * @param view View to be animated along with the SnackProgressBar.
     */
    public SnackProgressBarManager setViewToMove(View view) {
        this.viewToMove = view;
        return this;
    }

    /**
     * Set the transparency of the OverlayLayout which blocks user input.
     *
     * @param alpha Alpha between 0.0 to 1.0. Default = 0.8.
     */
    public SnackProgressBarManager setOverlayLayoutAlpha(@FloatRange(from = 0, to = 1) float alpha) {
        overlayLayout.setAlpha(alpha);
        return this;
    }

    /**
     * Set the action button text color.
     *
     * @param colorId R.color id.
     * @see #ACTION_COLOR_DEFAULT
     */
    public SnackProgressBarManager setActionTextColor(int colorId) {
        actionText.setTextColor(ContextCompat.getColor(context, colorId));
        return this;
    }

    /**
     * Set the message text color.
     *
     * @param colorId R.color id.
     * @see #MESSAGE_COLOR_DEFAULT
     */
    public SnackProgressBarManager setMessageTextColor(int colorId) {
        messageText.setTextColor(ContextCompat.getColor(context, colorId));
        return this;
    }

    /**
     * Set the progressBar color.
     *
     * @param colorId R.color id.
     * @see #PROGRESSBAR_COLOR_DEFAULT
     */
    public SnackProgressBarManager setProgressBarColor(int colorId) {
        determinateProgressBar.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(context, colorId), android.graphics.PorterDuff.Mode.SRC_IN);
        indeterminateProgressBar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(context, colorId), android.graphics.PorterDuff.Mode.SRC_IN);
        return this;
    }

    /**
     * Set the snackbar background color.
     *
     * @param colorId R.color id.
     * @see #SNACKBAR_COLOR_DEFAULT
     */
    public SnackProgressBarManager setSnackBarColor(int colorId) {
        snackBarLayout.setBackgroundColor(ContextCompat.getColor(context, colorId));
        return this;
    }

    /**
     * Set the progress for SnackProgressBar of TYPE_DETERMINATE.
     * It will also update the progress text in %.
     *
     * @param progress Progress of the progressBar.
     */
    public SnackProgressBarManager setProgress(@IntRange(from = 0) int progress) {
        determinateProgressBar.setProgress(progress);
        int progress100 = (int) (progress / (float) determinateProgressBar.getMax() * 100);
        String progressString = progress100 + "%";
        progressText.setText(progressString);
        return this;
    }

    /**
     * Set whether user input is allowed. Setting to TRUE will display the OverlayLayout which blocks user input.
     *
     * @param allowUserInput Whether to allow user input.
     */
    public SnackProgressBarManager setAllowUserInput(boolean allowUserInput) {
        this.allowUserInput = allowUserInput;
        showOverlayLayout();
        return this;
    }

    /**
     * Retrieve the SnackProgressBar that was previously added.
     *
     * @param id id of the SnackProgressBar contained in SnackProgressBarManager.
     * @return SnackProgressBar
     */
    public SnackProgressBar getSnackProgressBar(@IntRange(from = 1) int id) {
        return snackProgressBars.get(id);
    }

    /**
     * Retrieve the SnackProgressBar that is currently showing.
     *
     * @return SnackProgressBar that is currently showing. Return null if nothing is showing.
     */
    public SnackProgressBar getLastShowing() {
        return currentBar;
    }

    /**
     * Check if the MainLayout is visible.
     */
    public boolean isVisible() {
        return mainLayout.getTranslationY() < mainLayout.getHeight();
    }

    /**
     * Adds the SnackProgressBar to queue. It is added as a new object.
     *
     * @param snackProgressBar SnackProgressBar to be added to queue
     * @param duration         duration to show the SnackProgressBar of either
     *                         {@link #LENGTH_SHORT}, {@link #LENGTH_LONG}, {@link #LENGTH_INDEFINITE}.
     */
    private void addToQueue(SnackProgressBar snackProgressBar, long duration) {
        // get the queue number as the last of queue list
        int queue = queueBars.size();
        // add queue
        SnackProgressBar queueBar = new SnackProgressBar(
                snackProgressBar.getType(),
                snackProgressBar.getMessage(),
                snackProgressBar.isAllowUserInput(),
                snackProgressBar.isSwipeToDismiss(),
                snackProgressBar.isShowProgressPercentage(),
                snackProgressBar.getProgressMax(),
                snackProgressBar.getAction());
        queueBars.add(queueBar);
        queueDurations.add(duration);
        queueDone.add(false);
        // start play queue if first item
        if (queue == 0) {
            playQueue(queue);
        }
    }

    /**
     * Play the queue.
     *
     * @param queue queue number of SnackProgressBar added to queue.
     */
    private void playQueue(final int queue) {
        // check if queue number is bounded
        if (queue < queueBars.size()) {
            // check if previous queue is done, only check for 2nd queue and above
            boolean isPreviousSnackBarDone = true;
            if (queue > 0) {
                isPreviousSnackBarDone = queueDone.get(queue - 1);
            }
            if (isPreviousSnackBarDone) {
                // hide the previous item and add the delay if required
                long animationDelay = 0L;
                if (queue == 0 && isVisible()) {
                    hideMainLayout();
                    animationDelay = ANIMATION_DURATION;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // update MainLayout and show accordingly
                        SnackProgressBar snackProgressBar = queueBars.get(queue);
                        long duration = queueDurations.get(queue);
                        currentBar = snackProgressBar;
                        currentQueue = queue;
                        toShow = true;
                        updateTo(snackProgressBar);
                        // change indefinite duration to short if there is next item in queue
                        if (duration == LENGTH_INDEFINITE) {
                            if (queue < queueBars.size() - 1) {
                                duration = LENGTH_SHORT;
                            }
                        }
                        // hide the item if is not last item
                        if (duration != LENGTH_INDEFINITE) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // only hide if the queue is showing (possibly dismissed by user)
                                    if (currentQueue == queue) {
                                        hideMainLayout();
                                        // show next queue after hide animation
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                nextQueue();
                                            }
                                        }, ANIMATION_DURATION);
                                    }
                                }
                            }, duration);
                        } else {
                            // else, queue is done
                            resetQueue();
                        }
                    }
                }, animationDelay);
            }
        } else {
            // else, queue is done
            resetQueue();
        }
    }

    /**
     * Play the next in queue.
     */
    private void nextQueue() {
        // if bounded, set current queue as done and move to next
        if (currentQueue < queueBars.size()) {
            queueDone.set(currentQueue, true);
            playQueue(currentQueue + 1);
        }
    }

    /**
     * Reset queue.
     */
    private void resetQueue() {
        currentQueue = 0;
        queueBars.clear();
        queueDurations.clear();
        queueDone.clear();
    }

    /**
     * Show MainLayout.
     */
    private void showMainLayout() {
        mainLayout.requestLayout();
        mainLayout.animate().translationY(0);
        // move view to higher location
        if (viewToMove != null) {
            viewToMove.animate().translationY(-1 * mainLayout.getHeight());
        }
        toShow = false;
    }

    /**
     * Hide MainLayout.
     */
    private void hideMainLayout() {
        if (isVisible()) {
            mainLayout.animate().translationY(mainLayout.getHeight());
        }
        // move view to original location
        if (viewToMove != null && viewToMove.getTranslationY() != 0) {
            viewToMove.animate().translationY(0);
        }
    }

    /**
     * Show overlayLayout.
     */
    private void showOverlayLayout() {
        if (allowUserInput) {
            overlayLayout.setVisibility(View.GONE);
        } else {
            overlayLayout.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Animation for swipe out.
     *
     * @param toRight Direction of the animation.
     */
    private void swipeOut(boolean toRight) {
        float direction = toRight ? 1 : -1;
        overlayLayout.setVisibility(View.GONE);
        mainLayout.animate().translationX(direction * mainLayout.getWidth());
        mainLayout.animate().alpha(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // similar to dismiss(), play next in queue
                if (viewToMove != null) {
                    viewToMove.animate().translationY(0);
                }
                resetMainLayout();
                nextQueue();
            }
        }, ANIMATION_DURATION);
    }

    /**
     * Animation for swipe in.
     */
    private void swipeIn() {
        mainLayout.animate().translationX(0);
        mainLayout.animate().alpha(1);
    }

    /**
     * Reset mainLayout to hide position below screen.
     */
    private void resetMainLayout() {
        mainLayout.setTranslationX(0);
        mainLayout.setTranslationY(mainLayout.getHeight());
        mainLayout.setAlpha(1f);
        setProgress(0);
    }

    /**
     * Set the MainLayout view.
     *
     * @param type Type of SnackProgressBar.
     */
    private void setType(int type) {
        // update type
        this.type = type;
        // update view
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
    }

    /**
     * Set messageText.
     *
     * @param message Message of SnackProgressBar.
     */
    private void setMessage(final String message) {
        final int oldLineCount = messageText.getLineCount();
        messageText.setText(message);
        // set post to measure line count correctly
        messageText.post(new Runnable() {
            @Override
            public void run() {
                final int newLineCount = messageText.getLineCount();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) messageText.getLayoutParams();
                layoutParams.height = newLineCount < 2 ? heightSingle : heightMulti;
                messageText.setLayoutParams(layoutParams);
                // set post delay so that layout is drawn correctly before showing
                mainLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (toShow) {
                            showMainLayout();
                            showOverlayLayout();
                        } else if (isVisible() && oldLineCount != newLineCount) {
                            showMainLayout();
                        }
                    }
                }, 15);
            }
        });
    }

    /**
     * Set whether user can swipe to dismiss.
     * This only works for TYPE_ACTION and TYPE_MESSAGE.
     *
     * @param type           Type of SnackProgressBar.
     * @param swipeToDismiss Whether user can swipe to dismiss.
     */
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

    /**
     * Set whether to show progressText. Only will be shown for TYPE_DETERMINATE.
     *
     * @param showProgressPercentage Whether to show progressText.
     */
    private void showProgressPercentage(boolean showProgressPercentage) {
        if (showProgressPercentage && type == SnackProgressBar.TYPE_DETERMINATE) {
            progressText.setVisibility(View.VISIBLE);
        } else {
            progressText.setVisibility(View.GONE);
        }
    }

    /**
     * Set the max progress for progressBar. Only will be shown for TYPE_DETERMINATE.
     *
     * @param progressMax Max progress for progressBar.
     */
    private void setProgressMax(int progressMax) {
        determinateProgressBar.setMax(progressMax);
    }

    /**
     * Set actionText in upper case. Only will be shown for TYPE_ACTION.
     *
     * @param action Action to be displayed.
     */
    private void setAction(final String action) {
        actionText.setText(action.toUpperCase());
    }

    /**
     * Set onActionClickListener
     */
    private void setActionTextClickListener() {
        actionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pass click via interface
                if (onActionClickListener != null) {
                    onActionClickListener.onActionClick();
                }
            }
        });
    }

    /**
     * Set touch listener of messageText.
     */
    private void setOnTouchListener() {
        messageText.setOnTouchListener(new View.OnTouchListener() {

            private float startX, endX;
            private VelocityTracker velocityTracker;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // only track if allow swipe to dismiss
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
                            if (Math.abs(velocity) > SWIPE_OUT_VELOCITY) {
                                toSwipeOut = true;
                            }
                            // determine swipe out direction
                            if (toSwipeOut) {
                                if (endX - startX > 0) {
                                    swipeOut(true);
                                } else {
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
}
