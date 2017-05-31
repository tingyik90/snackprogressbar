package com.tingyik90.snackprogressbar;

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

    /* variables */
    private boolean allowUserInput = false;
    private boolean swipeToDismiss = false;
    private boolean showProgressPercentage = true;
    private int id;
    private int type;
    private int progressMax = 100;
    private String action = "";
    private String message = "";

    /**
     * Constructor.
     *
     * @param type    SnackProgressBar of either
     *                {@link #TYPE_ACTION}, {@link #TYPE_DETERMINATE}, {@link #TYPE_INDETERMINATE} or {@link #TYPE_MESSAGE}
     * @param message Message of SnackProgressBar.
     * @param id      Unique id for the SnackProgressBar. See {@link SnackProgressBarManager#add(SnackProgressBar)}.
     */
    public SnackProgressBar(@Type int type, @NonNull String message, @IntRange(from = 1) int id) {
        this.type = type;
        this.message = message;
        this.id = id;
    }

    /* Internal constructor for duplicating SnackProgressBar */
    SnackProgressBar(int type, @NonNull String message, int id,
                     boolean allowUserInput, boolean swipeToDismiss, boolean showProgressPercentage,
                     int progressMax, String action) {
        this.type = type;
        this.message = message;
        this.id = id;
        this.allowUserInput = allowUserInput;
        this.swipeToDismiss = swipeToDismiss;
        this.showProgressPercentage = showProgressPercentage;
        this.progressMax = progressMax;
        this.action = action;
    }

    /**
     * Set whether user input is allowed. Setting to TRUE will display the OverlayLayout which blocks user input.
     *
     * @param allowUserInput Whether to allow user input. Default = FALSE.
     */
    public SnackProgressBar setAllowUserInput(boolean allowUserInput) {
        this.allowUserInput = allowUserInput;
        return this;
    }

    public boolean isAllowUserInput() {
        return allowUserInput;
    }

    /**
     * Set whether user can swipe to dismiss.
     * This only works for TYPE_ACTION and TYPE_MESSAGE.
     *
     * @param swipeToDismiss Whether user can swipe to dismiss. Default = FALSE.
     */
    public SnackProgressBar setSwipeToDismiss(boolean swipeToDismiss) {
        this.swipeToDismiss = swipeToDismiss;
        return this;
    }

    public boolean isSwipeToDismiss() {
        return swipeToDismiss;
    }

    /**
     * Set whether to show progressText. Only will be shown for TYPE_DETERMINATE.
     *
     * @param showProgressPercentage Whether to show progressText. Default = TRUE.
     */
    public SnackProgressBar setShowProgressPercentage(boolean showProgressPercentage) {
        this.showProgressPercentage = showProgressPercentage;
        return this;
    }

    public boolean isShowProgressPercentage() {
        return showProgressPercentage;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    /**
     * Set the max progress for progressBar. Only will be shown for TYPE_DETERMINATE.
     *
     * @param progressMax Max progress for progressBar. Default = 100.
     */
    public SnackProgressBar setProgressMax(@IntRange(from = 1) int progressMax) {
        this.progressMax = progressMax;
        return this;
    }

    public int getProgressMax() {
        return progressMax;
    }

    /**
     * Set action. Only will be shown for TYPE_ACTION.
     *
     * @param action Action to be displayed.
     */
    public SnackProgressBar setAction(@NonNull String action) {
        this.action = action;
        return this;
    }

    public String getAction() {
        return action;
    }

    /**
     * Set message.
     *
     * @param message Message of SnackProgressBar.
     */
    public SnackProgressBar setMessage(@NonNull String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "SnackProgressBar{" +
                "allowUserInput=" + allowUserInput +
                ", swipeToDismiss=" + swipeToDismiss +
                ", showProgressPercentage=" + showProgressPercentage +
                ", id=" + id +
                ", type=" + type +
                ", progressMax=" + progressMax +
                ", action='" + action + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
