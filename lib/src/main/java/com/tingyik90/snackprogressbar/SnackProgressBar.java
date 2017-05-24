package com.tingyik90.snackprogressbar;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SnackProgressBar {

    /* static variables */
    private static final int TYPE_ACTION = 100;
    private static final int TYPE_DETERMINATE = 200;
    private static final int TYPE_INDETERMINATE = 300;
    private static final int TYPE_MESSAGE = 400;
    private static final String DEBUG_TAG = "SnackProgressBar";
    private static final int DEFAULT_COLOR = -1;

    /* view variables */
    private View masterLayout;
    private View overlayLayout;
    private View mainLayout;
    private View snackBarLayout;
    private TextView messageText;
    private TextView progressText;
    private TextView actionText;
    private ProgressBar determinateProgressBar;
    private ProgressBar indeterminateProgressBar;

    /* variables */
    private final Builder builder;
    private final int heightSingle;
    private final int heightMulti;
    private OnActionClickListener onActionClickListener = null;
    private boolean resetAfterDismiss;
    private boolean showProgressPercentage;
    private int type;

    /* custom class for type */
    public static class Type {

        public static final Type ACTION;
        public static final Type DETERMINATE;
        public static final Type INDETERMINATE;
        public static final Type MESSAGE;
        private int type;

        static {
            ACTION = new Type(SnackProgressBar.TYPE_ACTION);
            DETERMINATE = new Type(SnackProgressBar.TYPE_DETERMINATE);
            INDETERMINATE = new Type(SnackProgressBar.TYPE_INDETERMINATE);
            MESSAGE = new Type(SnackProgressBar.TYPE_MESSAGE);
        }

        private Type(int type) {
            this.type = type;
        }
    }

    /* builder */
    public static class Builder {

        private Activity activity;
        private OnActionClickListener onActionClickListener = null;
        private boolean allowUserInput = false;
        private boolean resetAfterDismiss = false;
        private boolean showProgressPercentage = true;
        private float alpha = 0.8f;
        private int actionTextColor = DEFAULT_COLOR;
        private int messageTextColor = DEFAULT_COLOR;
        private int progressBarColor = DEFAULT_COLOR;
        private int snackBarColor = DEFAULT_COLOR;
        private int progress = 0;
        private int progressMax = 100;
        private int type;
        private String action = "";
        private String message = "";

        public Builder(Activity activity, Type type, String message) {
            this.activity = activity;
            this.type = type.type;
            this.message = message;
        }

        public Builder setOnActionClickListener(OnActionClickListener onActionClickListener) {
            this.onActionClickListener = onActionClickListener;
            return this;
        }

        public Builder allowUserInput(boolean allowUserInput) {
            this.allowUserInput = allowUserInput;
            return this;
        }

        public Builder resetAfterDismiss(boolean resetAfterDismiss) {
            this.resetAfterDismiss = resetAfterDismiss;
            return this;
        }

        public Builder showProgressPercentage(boolean showProgressPercentage) {
            this.showProgressPercentage = showProgressPercentage;
            return this;
        }

        public Builder setOverlayLayoutAlpha(float alpha) {
            this.alpha = alpha;
            return this;
        }

        public Builder setActionTextColor(int resId) {
            this.actionTextColor = resId;
            return this;
        }


        public Builder setMessageTextColor(int resId) {
            this.messageTextColor = resId;
            return this;
        }

        public Builder setProgressBarColor(int resId) {
            this.progressBarColor = resId;
            return this;
        }

        public Builder setSnackBarColor(int resId) {
            this.snackBarColor = resId;
            return this;
        }

        public Builder setProgress(int progress) {
            this.progress = progress;
            return this;
        }

        public Builder setProgressMax(int progressMax) {
            this.progressMax = progressMax;
            return this;
        }

        public Builder setActionText(String action) {
            this.action = action.toUpperCase();
            return this;
        }

        public SnackProgressBar build() {
            return new SnackProgressBar(this);
        }
    }

    /* interface */
    public interface OnActionClickListener {
        void onActionClick();
    }

    /* constructors */
    private SnackProgressBar(Builder builder) {
        // variables
        this.builder = builder;
        Activity activity = builder.activity;
        this.onActionClickListener = builder.onActionClickListener;
        this.resetAfterDismiss = builder.resetAfterDismiss;
        this.type = builder.type;
        heightSingle = (int) activity.getResources().getDimension(R.dimen.snackBar_height_single);
        heightMulti = (int) activity.getResources().getDimension(R.dimen.snackBar_height_multi);
        // delete old view if present
        View oldView = activity.findViewById(R.id.snackProgressBar_layout_master);
        if (oldView != null) {
            ((ViewGroup) oldView.getParent()).removeView(oldView);
        }
        // create new view
        View contentView = View.inflate(activity, R.layout.snackprogressbar, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        activity.addContentView(contentView, layoutParams);
        masterLayout = activity.findViewById(R.id.snackProgressBar_layout_master);
        overlayLayout = activity.findViewById(R.id.snackProgressBar_layout_overlay);
        mainLayout = activity.findViewById(R.id.snackProgressBar_layout_main);
        snackBarLayout = activity.findViewById(R.id.snackProgressBar_layout_snackBar);
        messageText = (TextView) activity.findViewById(R.id.snackProgressBar_txt_message);
        progressText = (TextView) activity.findViewById(R.id.snackProgressBar_txt_progress);
        actionText = (TextView) activity.findViewById(R.id.snackProgressBar_txt_action);
        determinateProgressBar = (ProgressBar) activity.findViewById(R.id.snackProgressBar_progressbar_determinate);
        indeterminateProgressBar = (ProgressBar) activity.findViewById(R.id.snackProgressBar_progressbar_indeterminate);
        // initiate view
        setType(builder.type);
        setMessage(builder.message);
        setOnActionClickListener(builder.onActionClickListener);
        allowUserInput(builder.allowUserInput);
        resetAfterDismiss(builder.resetAfterDismiss);
        showProgressPercentage(builder.showProgressPercentage);
        setOverlayLayoutAlpha(builder.alpha);
        if (builder.actionTextColor != DEFAULT_COLOR) {
            setActionTextColor(activity, builder.actionTextColor);
        }
        if (builder.messageTextColor != DEFAULT_COLOR) {
            setMessageTextColor(activity, builder.messageTextColor);
        }
        if (builder.progressBarColor != DEFAULT_COLOR) {
            setProgressBarColor(activity, builder.progressBarColor);
        }
        if (builder.snackBarColor != DEFAULT_COLOR) {
            setSnackBarColor(activity, builder.snackBarColor);
        }
        setProgress(builder.progress);
        setProgressMax(builder.progressMax);
        setAction(builder.action);
        // set onClickListener
        actionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionClickListener != null) {
                    onActionClickListener.onActionClick();
                }
            }
        });
    }

    public void show() {
        // animate hide first if visible
        if (mainLayout.getVisibility() == View.VISIBLE) {
            mainLayout.setVisibility(View.GONE);
        }
        masterLayout.setVisibility(View.VISIBLE);
        mainLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mainLayout.removeOnLayoutChangeListener(this);
                adjustSnackBarHeight();
            }
        });
        mainLayout.setVisibility(View.VISIBLE);

    }

    public void dismiss() {
        mainLayout.setVisibility(View.GONE);
        masterLayout.setVisibility(View.GONE);
        if (resetAfterDismiss) {
            resetView();
        }
        setProgress(0);
    }

    private void resetView() {
        setType(builder.type);
        setMessage(builder.message);
        setOnActionClickListener(builder.onActionClickListener);
        allowUserInput(builder.allowUserInput);
        resetAfterDismiss(builder.resetAfterDismiss);
        showProgressPercentage(builder.showProgressPercentage);
        setOverlayLayoutAlpha(builder.alpha);
        setProgressMax(builder.progressMax);
        setAction(builder.action);
    }

    /* set listener */
    public void setOnActionClickListener(OnActionClickListener onActionClickListener) {
        if (this.onActionClickListener == null) {
            this.onActionClickListener = onActionClickListener;
        }
    }

    public void setType(Type type) {
        setType(type.type);
    }

    private void setType(int type) {
        this.type = type;
        showProgressPercentage(showProgressPercentage);
        switch (type) {
            case TYPE_ACTION:
                actionText.setVisibility(View.VISIBLE);
                determinateProgressBar.setVisibility(View.GONE);
                indeterminateProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_DETERMINATE:
                actionText.setVisibility(View.GONE);
                determinateProgressBar.setVisibility(View.VISIBLE);
                indeterminateProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_INDETERMINATE:
                actionText.setVisibility(View.GONE);
                determinateProgressBar.setVisibility(View.GONE);
                indeterminateProgressBar.setVisibility(View.VISIBLE);
                break;
            case TYPE_MESSAGE:
                actionText.setVisibility(View.GONE);
                determinateProgressBar.setVisibility(View.GONE);
                indeterminateProgressBar.setVisibility(View.GONE);
                break;
        }
    }

    public void setMessage(final String message) {
        messageText.setText(message);
        adjustSnackBarHeight();
    }

    private void adjustSnackBarHeight() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) snackBarLayout.getLayoutParams();
        Log.d(DEBUG_TAG, "line count = " + messageText.getLineCount());
        layoutParams.height = messageText.getLineCount() < 2 ? heightSingle : heightMulti;
        snackBarLayout.setLayoutParams(layoutParams);
    }

    public void allowUserInput(boolean allow) {
        if (allow) {
            overlayLayout.setVisibility(View.GONE);
        } else {
            overlayLayout.setVisibility(View.VISIBLE);
        }
    }

    public void resetAfterDismiss(boolean resetAfterDismiss) {
        this.resetAfterDismiss = resetAfterDismiss;
    }

    public void showProgressPercentage(boolean showProgressPercentage) {
        this.showProgressPercentage = showProgressPercentage;
        if (showProgressPercentage && type == TYPE_DETERMINATE) {
            progressText.setVisibility(View.VISIBLE);
        } else {
            progressText.setVisibility(View.GONE);
        }
    }

    public void setOverlayLayoutAlpha(float alpha) {
        overlayLayout.setAlpha(alpha);
    }

    public void setActionTextColor(Context context, int resId) {
        actionText.setTextColor(ContextCompat.getColor(context, resId));
    }

    public void setMessageTextColor(Context context, int resId) {
        messageText.setTextColor(ContextCompat.getColor(context, resId));
    }

    public void setProgressBarColor(Context context, int resId) {
        determinateProgressBar.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(context, resId), android.graphics.PorterDuff.Mode.SRC_IN);
        indeterminateProgressBar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(context, resId), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void setSnackBarColor(Context context, int resId) {
        snackBarLayout.setBackgroundColor(ContextCompat.getColor(context, resId));
    }

    public void setProgressMax(int max) {
        determinateProgressBar.setMax(max);
    }

    public void setProgress(int progress) {
        determinateProgressBar.setProgress(progress);
        int progress100 = (int) (progress / (float) determinateProgressBar.getMax() * 100);
        String progressString = progress100 + "%";
        progressText.setText(progressString);
    }

    public void setAction(final String action) {
        actionText.setText(action);
    }

}
