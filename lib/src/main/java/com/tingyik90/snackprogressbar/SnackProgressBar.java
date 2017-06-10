package com.tingyik90.snackprogressbar;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Main class containing the display information of SnackProgressBar to be displayed
 * via SnackProgressBarManager.
 */
public class SnackProgressBar {

    @Retention(SOURCE)
    @IntDef({TYPE_ACTION, TYPE_DETERMINATE, TYPE_INDETERMINATE, TYPE_MESSAGE})
    @interface Type {}

    /**
     * SnackProgressBar layout with message and action button.
     */
    public static final int TYPE_ACTION = 100;
    /**
     * SnackProgressBar layout with message, determinate progressBar and progress percentage.
     */
    public static final int TYPE_DETERMINATE = 200;
    /**
     * SnackProgressBar layout with message and indeterminate progressBar.
     */
    public static final int TYPE_INDETERMINATE = 300;
    /**
     * SnackProgressBar layout with message only.
     */
    public static final int TYPE_MESSAGE = 400;

    static final int DEFAULT_ICON_RES_ID = -1;

    /**
     * Interface definition for a callback to be invoked when an action is clicked.
     */
    public interface OnActionClickListener {
        /**
         * Called when an action is clicked.
         */
        void onActionClick();
    }

    /* variables */
    private int type;
    private String message = "";
    private String action = "";
    private Bitmap iconBitmap = null;
    private int iconResId = DEFAULT_ICON_RES_ID;
    private int progressMax = 100;
    private boolean allowUserInput = false;
    private boolean swipeToDismiss = false;
    private boolean showProgressPercentage = true;
    private OnActionClickListener onActionClickListener = null;

    /**
     * Constructor.
     *
     * @param type    SnackProgressBar of either
     *                {@link #TYPE_ACTION}, {@link #TYPE_DETERMINATE}, {@link #TYPE_INDETERMINATE} or {@link #TYPE_MESSAGE}
     * @param message Message of SnackProgressBar.
     */
    public SnackProgressBar(@Type int type, @NonNull String message) {
        this.type = type;
        this.message = message;
    }

    /**
     * Internal constructor for duplicating SnackProgressBar.
     */
    SnackProgressBar(int type, String message, String action, Bitmap iconBitmap, int iconResId, int progressMax,
                     boolean allowUserInput, boolean swipeToDismiss, boolean showProgressPercentage,
                     OnActionClickListener onActionClickListener) {
        this.type = type;
        this.message = message;
        this.action = action;
        this.iconBitmap = iconBitmap;
        this.iconResId = iconResId;
        this.progressMax = progressMax;
        this.allowUserInput = allowUserInput;
        this.swipeToDismiss = swipeToDismiss;
        this.showProgressPercentage = showProgressPercentage;
        this.onActionClickListener = onActionClickListener;
    }

    /**
     * Sets the type of SnackProgressBar.
     *
     * @param type SnackProgressBar of either
     *             {@link #TYPE_ACTION}, {@link #TYPE_DETERMINATE}, {@link #TYPE_INDETERMINATE} or {@link #TYPE_MESSAGE}
     */
    public void setType(int type) {
        this.type = type;
    }

    int getType() {
        return type;
    }

    /**
     * Sets the message of SnackProgressBar.
     *
     * @param message Message of SnackProgressBar.
     */
    public SnackProgressBar setMessage(@NonNull String message) {
        this.message = message;
        return this;
    }

    String getMessage() {
        return message;
    }

    /**
     * Sets the action of SnackProgressBar. Only will be shown for TYPE_ACTION.
     *
     * @param action Action to be displayed.
     */
    public SnackProgressBar setAction(@NonNull String action, OnActionClickListener onActionClickListener) {
        if (type == TYPE_ACTION) {
            this.action = action;
            this.onActionClickListener = onActionClickListener;
        }
        return this;
    }

    String getAction() {
        return action;
    }

    OnActionClickListener getOnActionClickListener() {
        return onActionClickListener;
    }

    /**
     * Sets the icon of SnackProgressBar.
     *
     * @param bitmap Bitmap of icon.
     */
    public SnackProgressBar setIconBitmap(@NonNull Bitmap bitmap) {
        iconBitmap = bitmap;
        iconResId = DEFAULT_ICON_RES_ID;
        return this;
    }

    Bitmap getIconBitmap() {
        return iconBitmap;
    }

    /**
     * Sets the icon of SnackProgressBar.
     *
     * @param iconResId The resource identifier of the icon to be displayed.
     */
    public SnackProgressBar setIconResource(@DrawableRes int iconResId) {
        iconBitmap = null;
        this.iconResId = iconResId;
        return this;
    }

    int getIconResource() {
        return iconResId;
    }

    /**
     * Sets the max progress for determinate ProgressBar. Only will be shown for TYPE_DETERMINATE.
     *
     * @param progressMax Max progress for determinate ProgressBar. Default = 100.
     */
    public SnackProgressBar setProgressMax(@IntRange(from = 1) int progressMax) {
        if (type == TYPE_DETERMINATE) {
            this.progressMax = progressMax;
        }
        return this;
    }

    int getProgressMax() {
        return progressMax;
    }

    /**
     * Sets whether user input is allowed. Setting to FALSE will display an OverlayLayout which blocks user input.
     *
     * @param allowUserInput Whether to allow user input. Default = FALSE.
     */
    public SnackProgressBar setAllowUserInput(boolean allowUserInput) {
        this.allowUserInput = allowUserInput;
        return this;
    }

    boolean isAllowUserInput() {
        return allowUserInput;
    }

    /**
     * Sets whether user can swipe to dismiss.
     * Swipe to dismiss only works for TYPE_ACTION and TYPE_MESSAGE.
     *
     * @param swipeToDismiss Whether user can swipe to dismiss. Default = FALSE.
     */
    public SnackProgressBar setSwipeToDismiss(boolean swipeToDismiss) {
        switch (type) {
            // don't allow swipe to dismiss
            case SnackProgressBar.TYPE_DETERMINATE:
            case SnackProgressBar.TYPE_INDETERMINATE:
                swipeToDismiss = false;
                break;
        }
        this.swipeToDismiss = swipeToDismiss;
        return this;
    }

    boolean isSwipeToDismiss() {
        return swipeToDismiss;
    }

    /**
     * Sets whether to show progress in percentage. Only will be shown for TYPE_DETERMINATE.
     *
     * @param showProgressPercentage Whether to show progressText. Default = TRUE.
     */
    public SnackProgressBar setShowProgressPercentage(boolean showProgressPercentage) {
        if (type == TYPE_DETERMINATE) {
            this.showProgressPercentage = showProgressPercentage;
        }
        return this;
    }

    boolean isShowProgressPercentage() {
        return showProgressPercentage;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("SnackProgressBar{")
                .append("type=").append(type)
                .append(", message='").append(message).append("\'");
        if (iconBitmap != null) {
            stringBuilder.append(", iconBitmap=").append(iconBitmap.toString());
        }
        if (iconResId != DEFAULT_ICON_RES_ID) {
            stringBuilder.append(", iconResId=").append(iconResId);
        }
        if (type == TYPE_DETERMINATE) {
            stringBuilder.append(", progressMax=").append(progressMax);
            stringBuilder.append(", showProgressPercentage=").append(showProgressPercentage);
        }
        stringBuilder.append(", allowUserInput=").append(allowUserInput)
                .append(", swipeToDismiss=").append(swipeToDismiss)
                .append("}");
        return stringBuilder.toString();
    }
}
