package com.tingyik90.snackprogressbar;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Layout class for SnackProgressBar.
 */
class SnackProgressBarLayout extends LinearLayout implements BaseTransientBottomBar.ContentViewCallback {

    /* variables */
    static final int ACTION_DOWN = 123;
    static final int SWIPE_OUT = 456;
    static final int SWIPE_IN = 789;
    static final int ANIMATION_DURATION = 250;                          // animation duration as per BaseTransientBottomBar

    private static final float START_ALPHA_SWIPE_DISTANCE = 0.1f;       // as per Behavior in BaseTransientBottomBar
    private static final float END_ALPHA_SWIPE_DISTANCE = 0.6f;         // as per Behavior in BaseTransientBottomBar
    private static final float SWIPE_OUT_VELOCITY = 800f;

    private int heightSingle;
    private int heightMulti;
    private boolean isCoordinatorLayout;
    private boolean swipeToDismiss;

    private View backgroundLayout;
    private View mainLayout;
    private View actionNextLineLayout;
    private View[] viewsToMove;
    private ImageView iconImage;
    private TextView messageText;
    private TextView actionText;
    private TextView actionNextLineText;
    private TextView progressText;
    private ProgressBar determinateProgressBar;
    private ProgressBar indeterminateProgressBar;

    /**
     * Interface definition for a callback to be invoked when the SnackProgressBar is touched.
     */
    interface OnBarTouchListener {
        /**
         * Called when the SnackProgressBar is touched.
         *
         * @param event Type of touch event.
         */
        void onTouch(int event);
    }

    private OnBarTouchListener onBarTouchListener;

    /**
     * Registers a callback to be invoked when the SnackProgressBar is touched.
     *
     * @param onBarTouchListener The callback that will run. This value may be null.
     */
    void setOnBarTouchListener(OnBarTouchListener onBarTouchListener) {
        this.onBarTouchListener = onBarTouchListener;
    }

    /**
     * Default constructor.
     */
    public SnackProgressBarLayout(Context context) {
        super(context);
    }

    /**
     * Default constructor.
     */
    public SnackProgressBarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Passes the view (e.g. FloatingActionButton) to move up or down as SnackProgressBar is shown or dismissed.
     *
     * @param viewsToMove Views to animate when the SnackProgressBar is shown or dismissed.
     */
    void setViewsToMove(View[] viewsToMove) {
        this.viewsToMove = viewsToMove;
    }

    /**
     * Updates the color of the layout.
     *
     * @param backgroundColor  R.color id.
     * @param messageTextColor R.color id.
     * @param actionTextColor  R.color id.
     * @param progressBarColor R.color id.
     */
    void setColor(@ColorRes int backgroundColor, @ColorRes int messageTextColor,
                  @ColorRes int actionTextColor, @ColorRes int progressBarColor) {
        backgroundLayout.setBackgroundColor(ContextCompat.getColor(getContext(), backgroundColor));
        messageText.setTextColor(ContextCompat.getColor(getContext(), messageTextColor));
        actionText.setTextColor(ContextCompat.getColor(getContext(), actionTextColor));
        actionNextLineText.setTextColor(ContextCompat.getColor(getContext(), actionTextColor));
        determinateProgressBar.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(getContext(), progressBarColor), android.graphics.PorterDuff.Mode.SRC_IN);
        indeterminateProgressBar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(getContext(), progressBarColor), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    /**
     * Sets whether user can swipe to dismiss.
     *
     * @param swipeToDismiss Whether user can swipe to dismiss.
     * @see #configureSwipeToDismiss()
     */
    void setSwipeToDismiss(boolean swipeToDismiss) {
        this.swipeToDismiss = swipeToDismiss;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // register variables
        heightSingle = (int) getResources().getDimension(R.dimen.snackProgressBar_height_single);  // height as per Material Design
        heightMulti = (int) getResources().getDimension(R.dimen.snackProgressBar_height_multi);   // height as per Material Design
        backgroundLayout = findViewById(R.id.snackProgressBar_layout_background);
        mainLayout = findViewById(R.id.snackProgressBar_layout_main);
        actionNextLineLayout = findViewById(R.id.snackProgressBar_layout_actionNextLine);
        iconImage = (ImageView) findViewById(R.id.snackProgressBar_img_icon);
        messageText = (TextView) findViewById(R.id.snackProgressBar_txt_message);
        actionText = (TextView) findViewById(R.id.snackProgressBar_txt_action);
        actionNextLineText = (TextView) findViewById(R.id.snackProgressBar_txt_actionNextLine);
        progressText = (TextView) findViewById(R.id.snackProgressBar_txt_progress);
        determinateProgressBar = (ProgressBar) findViewById(R.id.snackProgressBar_progressbar_determinate);
        indeterminateProgressBar = (ProgressBar) findViewById(R.id.snackProgressBar_progressbar_indeterminate);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        boolean remeasure = false;
        boolean isMultiLine = messageText.getLayout().getLineCount() > 1;
        boolean hasAction = !actionText.getText().toString().isEmpty();
        // put the action into next line if width is more than 25% of total width
        boolean isActionNextLine = (float) actionText.getMeasuredWidth() / backgroundLayout.getMeasuredWidth() > 0.25f;
        if (hasAction) {
            if (isActionNextLine) {
                if (actionNextLineLayout.getVisibility() != VISIBLE) {
                    actionText.setVisibility(GONE);
                    actionNextLineLayout.setVisibility(VISIBLE);
                    remeasure = true;
                }
            } else {
                if (actionText.getVisibility() != VISIBLE) {
                    actionText.setVisibility(VISIBLE);
                    actionNextLineLayout.setVisibility(GONE);
                    remeasure = true;
                }
            }
        } else {
            if (actionNextLineLayout.getVisibility() == VISIBLE || actionText.getVisibility() == VISIBLE) {
                actionText.setVisibility(GONE);
                actionNextLineLayout.setVisibility(GONE);
                remeasure = true;
            }
        }
        // set layout height according to message length
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mainLayout.getLayoutParams();
        if (isMultiLine) {
            if (layoutParams.height != heightMulti) {
                layoutParams.height = heightMulti;
                mainLayout.setLayoutParams(layoutParams);
                remeasure = true;
            }
        } else {
            if (layoutParams.height != heightSingle) {
                layoutParams.height = heightSingle;
                mainLayout.setLayoutParams(layoutParams);
                remeasure = true;
            }
        }
        if (remeasure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // clear the padding of the parent that hold this view
        View parentView = (View) getParent();
        parentView.setPadding(0, 0, 0, 0);
        // check if it is CoordinatorLayout and configure swipe to dismiss
        isCoordinatorLayout = parentView.getParent() instanceof CoordinatorLayout;
        configureSwipeToDismiss();
    }

    // animation for when updateTo() is called by SnackProgressBarManager
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (viewsToMove != null
                && oldh != 0        // oldh = 0 when first shown, only animate when the size is changed after being shown
                && oldh != h) {     // don't animate if it is the same size
            for (View viewToMove : viewsToMove) {
                ViewCompat.animate(viewToMove).translationYBy(oldh - h).setDuration(ANIMATION_DURATION).start();
            }
        }
    }

    // animation as per original Snackbar class
    @Override
    public void animateContentIn(int delay, int duration) {
        ViewCompat.setAlpha(messageText, 0f);
        ViewCompat.animate(messageText).alpha(1f).setDuration(duration)
                .setStartDelay(delay).start();

        if (actionText.getVisibility() == VISIBLE) {
            ViewCompat.setAlpha(actionText, 0f);
            ViewCompat.animate(actionText).alpha(1f).setDuration(duration)
                    .setStartDelay(delay).start();
        }

        if (actionNextLineText.getVisibility() == VISIBLE) {
            ViewCompat.setAlpha(actionNextLineText, 0f);
            ViewCompat.animate(actionNextLineText).alpha(1f).setDuration(duration)
                    .setStartDelay(delay).start();
        }

        if (progressText.getVisibility() == VISIBLE) {
            ViewCompat.setAlpha(progressText, 0f);
            ViewCompat.animate(progressText).alpha(1f).setDuration(duration)
                    .setStartDelay(delay).start();
        }

        if (determinateProgressBar.getVisibility() == VISIBLE) {
            ViewCompat.setAlpha(determinateProgressBar, 0f);
            ViewCompat.animate(determinateProgressBar).alpha(1f).setDuration(duration)
                    .setStartDelay(delay).start();
        }

        if (indeterminateProgressBar.getVisibility() == VISIBLE) {
            ViewCompat.setAlpha(indeterminateProgressBar, 0f);
            ViewCompat.animate(indeterminateProgressBar).alpha(1f).setDuration(duration)
                    .setStartDelay(delay).start();
        }

        if (viewsToMove != null) {
            for (View viewToMove : viewsToMove) {
                ViewCompat.animate(viewToMove).translationY(-1 * getMeasuredHeight())
                        .setDuration(ANIMATION_DURATION).start();
            }
        }

    }

    // animation as per original Snackbar class
    @Override
    public void animateContentOut(int delay, int duration) {
        ViewCompat.setAlpha(messageText, 1f);
        ViewCompat.animate(messageText).alpha(0f).setDuration(duration)
                .setStartDelay(delay).start();

        if (actionText.getVisibility() == VISIBLE) {
            ViewCompat.setAlpha(actionText, 1f);
            ViewCompat.animate(actionText).alpha(0f).setDuration(duration)
                    .setStartDelay(delay).start();
        }

        if (actionNextLineText.getVisibility() == VISIBLE) {
            ViewCompat.setAlpha(actionNextLineText, 1f);
            ViewCompat.animate(actionNextLineText).alpha(0f).setDuration(duration)
                    .setStartDelay(delay).start();
        }

        if (progressText.getVisibility() == VISIBLE) {
            ViewCompat.setAlpha(progressText, 1f);
            ViewCompat.animate(progressText).alpha(0f).setDuration(duration)
                    .setStartDelay(delay).start();
        }

        if (determinateProgressBar.getVisibility() == VISIBLE) {
            ViewCompat.setAlpha(determinateProgressBar, 1f);
            ViewCompat.animate(determinateProgressBar).alpha(0f).setDuration(duration)
                    .setStartDelay(delay).start();
        }

        if (indeterminateProgressBar.getVisibility() == VISIBLE) {
            ViewCompat.setAlpha(indeterminateProgressBar, 1f);
            ViewCompat.animate(indeterminateProgressBar).alpha(0f).setDuration(duration)
                    .setStartDelay(delay).start();
        }

        if (viewsToMove != null) {
            for (View viewToMove : viewsToMove) {
                ViewCompat.animate(viewToMove).translationY(0).setDuration(ANIMATION_DURATION).start();
            }
        }
    }

    /**
     * Configures swipe to dismiss behaviour.
     */
    private void configureSwipeToDismiss() {
        if (swipeToDismiss) {
            // attach touch listener if it is not a CoordinatorLayout to allow extra features
            if (!isCoordinatorLayout) {
                setOnTouchListener();
            }
        } else {
            // remove default behaviour specified in BaseTransientBottomBar for CoordinatorLayout
            if (isCoordinatorLayout) {
                ViewGroup parentView = (ViewGroup) getParent();
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) parentView.getLayoutParams();
                layoutParams.setBehavior(null);
            }
        }
    }

    /**
     * Sets onTouchListener to allow swipe to dismiss behaviour for layouts other than CoordinatorLayout.
     */
    private void setOnTouchListener() {
        backgroundLayout.setOnTouchListener(new View.OnTouchListener() {
            // variables
            private float startX, endX;
            private VelocityTracker velocityTracker;
            private View parentView = (View) getParent();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int index = event.getActionIndex();
                int pointerId = event.getPointerId(index);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // callback onBarTouchListener
                        if (onBarTouchListener != null) {
                            onBarTouchListener.onTouch(ACTION_DOWN);
                        }
                        // track initial coordinate
                        startX = event.getRawX();
                        // track velocity
                        velocityTracker = VelocityTracker.obtain();
                        velocityTracker.addMovement(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // track velocity
                        velocityTracker.addMovement(event);
                        // track move coordinate
                        float moveX = event.getRawX();
                        // set translationX
                        float deltaX = moveX - startX;
                        parentView.setTranslationX(deltaX);
                        // animate alpha as per behaviour specified in BaseTransientBottomBar for CoordinatorLayout
                        float totalWidth = parentView.getMeasuredWidth();
                        float fractionTravelled = deltaX / totalWidth;
                        if (fractionTravelled < START_ALPHA_SWIPE_DISTANCE) {
                            parentView.setAlpha(1f);
                        } else if (fractionTravelled > END_ALPHA_SWIPE_DISTANCE) {
                            parentView.setAlpha(0f);
                        } else {
                            parentView.setAlpha((fractionTravelled - START_ALPHA_SWIPE_DISTANCE)
                                    / (END_ALPHA_SWIPE_DISTANCE - START_ALPHA_SWIPE_DISTANCE));
                        }
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
                        if (Math.abs(endX - startX) / parentView.getWidth() > 0.5) {
                            toSwipeOut = true;
                        }
                        // swipe out if velocity is high
                        if (Math.abs(velocity) > SWIPE_OUT_VELOCITY) {
                            toSwipeOut = true;
                        }
                        if (toSwipeOut) {
                            swipeOut(endX - startX);
                        } else {
                            // else, return to original position
                            swipeIn(endX - startX);
                        }
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Swipe out animation.
     *
     * @param deltaX Difference between position of ACTION_DOWN and ACTION_UP.
     *               Positive value means user swiped to right.
     */
    private void swipeOut(float deltaX) {
        // callback onBarTouchListener
        if (onBarTouchListener != null) {
            onBarTouchListener.onTouch(SWIPE_OUT);
        }
        View parentView = (View) getParent();
        float direction = deltaX > 0f ? 1f : -1f;
        ViewCompat.animate(parentView)
                .translationX(direction * parentView.getWidth())
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(ANIMATION_DURATION)
                .setListener(null)      // remove listener that is attached in animateViewIn() of BaseTransientBottomBar
                .start();
        ViewCompat.animate(parentView)
                .alpha(0f)
                .setDuration(ANIMATION_DURATION)
                .start();
    }

    /**
     * Animation for swipe in.
     *
     * @param deltaX Difference between position of ACTION_DOWN and ACTION_UP.
     *               Zero means a user click.
     */
    private void swipeIn(float deltaX) {
        View parentView = (View) getParent();
        // animate if the layout has moved
        if (deltaX > 0f) {
            ViewCompat.animate(parentView)
                    .translationX(0f)
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .setDuration(ANIMATION_DURATION)
                    .setListener(null)      // remove listener that is attached in animateViewIn() of BaseTransientBottomBar
                    .start();
            ViewCompat.animate(parentView)
                    .alpha(1f)
                    .setDuration(ANIMATION_DURATION)
                    .start();
        } else {
            // else just make sure the layout is at correct position
            parentView.setTranslationX(0f);
            parentView.setAlpha(1f);
        }
        // callback onBarTouchListener
        if (onBarTouchListener != null) {
            onBarTouchListener.onTouch(SWIPE_IN);
        }
    }

    /* getters */
    View getBackgroundLayout() {
        return backgroundLayout;
    }

    View getMainLayout() {
        return mainLayout;
    }

    View getActionNextLineLayout() {
        return actionNextLineLayout;
    }

    ImageView getIconImage() {
        return iconImage;
    }

    TextView getMessageText() {
        return messageText;
    }

    TextView getActionText() {
        return actionText;
    }

    TextView getActionNextLineText() {
        return actionNextLineText;
    }

    TextView getProgressText() {
        return progressText;
    }

    ProgressBar getDeterminateProgressBar() {
        return determinateProgressBar;
    }

    ProgressBar getIndeterminateProgressBar() {
        return indeterminateProgressBar;
    }
}
