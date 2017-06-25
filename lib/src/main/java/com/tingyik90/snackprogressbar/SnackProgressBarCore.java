package com.tingyik90.snackprogressbar;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Core class constructing the SnackProgressBar.
 */
class SnackProgressBarCore extends BaseTransientBottomBar<SnackProgressBarCore> {

    /* variables */
    private static final int SHORT_DURATION_MS = 1500;
    private static final int LONG_DURATION_MS = 2750;
    private int duration;
    private boolean useDefaultHandler = true;
    private SnackProgressBar snackProgressBar;
    private Handler handler = new Handler();
    private Runnable runnable;
    private ViewGroup parentView;
    private View overlayLayout;
    private SnackProgressBarLayout snackProgressBarLayout;

    /**
     * Super constructor.
     */
    private SnackProgressBarCore(@NonNull ViewGroup parent, @NonNull View content, @NonNull ContentViewCallback contentViewCallback) {
        super(parent, content, contentViewCallback);
    }

    /**
     * Prepares SnackProgressBarCore.
     *
     * @param parentView       View to hold the SnackProgressBar.
     * @param snackProgressBar SnackProgressBar to be shown.
     * @param duration         Duration to show the SnackProgressBar.
     * @param viewsToMove      View to be animated along with the SnackProgressBar.
     */
    static SnackProgressBarCore make(@NonNull ViewGroup parentView, SnackProgressBar snackProgressBar,
                                     int duration, View[] viewsToMove) {
        // get inflater from parent
        final LayoutInflater inflater = LayoutInflater.from(parentView.getContext());
        // add overlayLayout as background
        final View overlayLayout = inflater.inflate(R.layout.overlay, parentView, false);
        parentView.addView(overlayLayout);
        // inflate SnackProgressBarLayout and pass viewsToMove
        final SnackProgressBarLayout snackProgressBarLayout = (SnackProgressBarLayout) inflater.inflate(
                R.layout.snackprogressbar, parentView, false);
        snackProgressBarLayout.setViewsToMove(viewsToMove);
        // create SnackProgressBarCore
        SnackProgressBarCore snackProgressBarCore = new SnackProgressBarCore(
                parentView, snackProgressBarLayout, snackProgressBarLayout);
        snackProgressBarCore.snackProgressBar = snackProgressBar;
        snackProgressBarCore.parentView = parentView;
        snackProgressBarCore.overlayLayout = overlayLayout;
        snackProgressBarCore.snackProgressBarLayout = snackProgressBarLayout;
        snackProgressBarCore.useDefaultHandler = parentView instanceof CoordinatorLayout;
        snackProgressBarCore.duration = duration;
        snackProgressBarCore.updateTo(snackProgressBar);
        return snackProgressBarCore;
    }

    /**
     * Updates the SnackProgressBar without dismissing it to the new SnackProgressBar.
     *
     * @param snackProgressBar SnackProgressBar to be updated to.
     */
    void updateTo(@NonNull SnackProgressBar snackProgressBar) {
        this.snackProgressBar = snackProgressBar;
        setType();
        setIcon();
        setAction();
        showProgressPercentage();
        setProgressMax();
        setSwipeToDismiss();
        setMessage();
        // only toggle overlayLayout visibility if already shown
        if (isShown()) {
            showOverlayLayout();
        }
    }

    /**
     * Updates the color and alpha of overlayLayout.
     *
     * @param overlayColor       R.color id.
     * @param overlayLayoutAlpha Alpha between 0f to 1f. Default = 0.8f.
     */
    SnackProgressBarCore setOverlayLayout(int overlayColor, float overlayLayoutAlpha) {
        overlayLayout.setBackgroundColor(ContextCompat.getColor(getContext(), overlayColor));
        overlayLayout.setAlpha(overlayLayoutAlpha);
        return this;
    }

    /**
     * Updates the color of the layout.
     *
     * @param backgroundColor  R.color id.
     * @param messageTextColor R.color id.
     * @param actionTextColor  R.color id.
     * @param progressBarColor R.color id.
     */
    SnackProgressBarCore setColor(int backgroundColor, int messageTextColor, int actionTextColor, int progressBarColor) {
        snackProgressBarLayout.setColor(backgroundColor, messageTextColor, actionTextColor, progressBarColor);
        return this;
    }

    /**
     * Sets the progress for SnackProgressBar.
     *
     * @param progress Progress of the ProgressBar.
     */
    SnackProgressBarCore setProgress(@IntRange(from = 0) int progress) {
        ProgressBar determinateProgressBar = snackProgressBarLayout.getDeterminateProgressBar();
        determinateProgressBar.setProgress(progress);
        int progress100 = (int) (progress / (float) determinateProgressBar.getMax() * 100);
        String progressString = progress100 + "%";
        snackProgressBarLayout.getProgressText().setText(progressString);
        return this;
    }

    @Override
    public void show() {
        // show overLayLayout
        showOverlayLayout();
        // use default SnackManager if it is CoordinatorLayout
        if (useDefaultHandler) {
            setDuration(duration);
        }
        // else, set up own handler for dismiss countdown
        else {
            setOnBarTouchListener();
            // disable SnackManager by stopping countdown
            setDuration(LENGTH_INDEFINITE);
            // prepare dismiss runnable
            runnable = new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            };
            // assign the actual duration if dismiss is required
            if (duration != LENGTH_INDEFINITE) {
                switch (duration) {
                    case LENGTH_SHORT:
                        duration = SHORT_DURATION_MS;
                        break;
                    case LENGTH_LONG:
                        duration = LONG_DURATION_MS;
                        break;
                }
                handler.postDelayed(runnable, duration);
            }
        }
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        // remove overlayLayout after dismissed
        parentView.removeView(overlayLayout);
    }

    /**
     * Sets the layout based on SnackProgressBar type.
     * Note: Layout for action is handled by {@link SnackProgressBarLayout}
     */
    private SnackProgressBarCore setType() {
        ProgressBar determinateProgressBar = snackProgressBarLayout.getDeterminateProgressBar();
        ProgressBar indeterminateProgressBar = snackProgressBarLayout.getIndeterminateProgressBar();
        // update view
        int type = snackProgressBar.getType();
        switch (type) {
            case SnackProgressBar.TYPE_ACTION:
            case SnackProgressBar.TYPE_MESSAGE:
                determinateProgressBar.setVisibility(View.GONE);
                indeterminateProgressBar.setVisibility(View.GONE);
                break;
            case SnackProgressBar.TYPE_DETERMINATE:
                determinateProgressBar.setVisibility(View.VISIBLE);
                indeterminateProgressBar.setVisibility(View.GONE);
                break;
            case SnackProgressBar.TYPE_INDETERMINATE:
                determinateProgressBar.setVisibility(View.GONE);
                indeterminateProgressBar.setVisibility(View.VISIBLE);
                break;
        }
        return this;
    }

    /**
     * Sets the icon of SnackProgressBar.
     */
    private SnackProgressBarCore setIcon() {
        Bitmap iconBitmap = snackProgressBar.getIconBitmap();
        int iconResId = snackProgressBar.getIconResource();
        ImageView iconImage = snackProgressBarLayout.getIconImage();
        if (iconBitmap != null) {
            iconImage.setImageBitmap(iconBitmap);
            iconImage.setVisibility(View.VISIBLE);
        } else if (iconResId != SnackProgressBar.DEFAULT_ICON_RES_ID) {
            iconImage.setImageResource(iconResId);
            iconImage.setVisibility(View.VISIBLE);
        } else {
            iconImage.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * Set the action to be displayed. Only will be shown for TYPE_ACTION.
     */
    private SnackProgressBarCore setAction() {
        int type = snackProgressBar.getType();
        String action = snackProgressBar.getAction();
        final SnackProgressBar.OnActionClickListener onActionClickListener = snackProgressBar.getOnActionClickListener();
        if (type == SnackProgressBar.TYPE_ACTION) {
            // set the text
            TextView actionText = snackProgressBarLayout.getActionText();
            TextView actionNextLineText = snackProgressBarLayout.getActionNextLineText();
            actionText.setText(action.toUpperCase());
            actionNextLineText.setText(action.toUpperCase());
            // set the onClickListener
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onActionClickListener != null) {
                        onActionClickListener.onActionClick();
                    }
                    dismiss();
                }
            };
            actionText.setOnClickListener(onClickListener);
            actionNextLineText.setOnClickListener(onClickListener);
        }
        return this;
    }

    /**
     * Set whether to show progressText. Only will be shown for TYPE_DETERMINATE.
     */
    private SnackProgressBarCore showProgressPercentage() {
        int type = snackProgressBar.getType();
        boolean showProgressPercentage = snackProgressBar.isShowProgressPercentage();
        TextView progressText = snackProgressBarLayout.getProgressText();
        if (showProgressPercentage && type == SnackProgressBar.TYPE_DETERMINATE) {
            progressText.setVisibility(View.VISIBLE);
        } else {
            progressText.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * Set the max progress for progressBar.
     */
    private SnackProgressBarCore setProgressMax() {
        int progressMax = snackProgressBar.getProgressMax();
        snackProgressBarLayout.getDeterminateProgressBar().setMax(progressMax);
        return this;
    }

    /**
     * Set whether user can swipe to dismiss.
     * This only works for TYPE_ACTION and TYPE_MESSAGE.
     */
    private SnackProgressBarCore setSwipeToDismiss() {
        boolean swipeToDismiss = snackProgressBar.isSwipeToDismiss();
        snackProgressBarLayout.setSwipeToDismiss(swipeToDismiss);
        return this;
    }

    /**
     * Sets the message of SnackProgressBar.
     */
    private SnackProgressBarCore setMessage() {
        String message = snackProgressBar.getMessage();
        snackProgressBarLayout.getMessageText().setText(message);
        return this;
    }

    /**
     * Shows the overlayLayout based on whether user input is allowed.
     */
    private SnackProgressBarCore showOverlayLayout() {
        boolean isAllowUserInput = snackProgressBar.isAllowUserInput();
        if (!isAllowUserInput) {
            overlayLayout.setVisibility(View.VISIBLE);
        } else {
            overlayLayout.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * Registers a callback to be invoked when the SnackProgressBar is touched.
     * This is only applicable when swipe to dismiss behaviour is true and CoordinatorLayout is not used.
     */
    private void setOnBarTouchListener() {
        snackProgressBarLayout.setOnBarTouchListener(new SnackProgressBarLayout.OnBarTouchListener() {
            @Override
            public void onTouch(int event) {
                switch (event) {
                    case SnackProgressBarLayout.ACTION_DOWN:
                        // when user touched the SnackProgressBar, stop the dismiss countdown
                        handler.removeCallbacks(runnable);
                        break;
                    case SnackProgressBarLayout.SWIPE_OUT:
                        // once the SnackProgressBar is swiped out, dismiss after animation ends
                        handler.postDelayed(runnable, SnackProgressBarLayout.ANIMATION_DURATION);
                        break;
                    case SnackProgressBarLayout.SWIPE_IN:
                        // once the SnackProgressBar is swiped in, resume dismiss countdown
                        if (duration != LENGTH_INDEFINITE) {
                            handler.postDelayed(runnable, duration);
                        }
                        break;
                }
            }
        });
    }
}
