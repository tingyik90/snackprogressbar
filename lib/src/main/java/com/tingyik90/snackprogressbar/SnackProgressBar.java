package com.tingyik90.snackprogressbar;

import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class SnackProgressBar {

    @Retention(SOURCE)
    @IntDef({TYPE_ACTION, TYPE_DETERMINATE, TYPE_INDETERMINATE, TYPE_MESSAGE})
    public @interface Type {
    }

    public static final int TYPE_ACTION = 100;
    public static final int TYPE_DETERMINATE = 200;
    public static final int TYPE_INDETERMINATE = 300;
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

    private static final String DEBUG_TAG = "SnackProgressBar";

    /* constructors */
    public SnackProgressBar(@Type int type, @NonNull String message, @IntRange(from = 1) int id) {
        this.id = id;
        this.type = type;
        this.message = message;
    }

    public boolean isAllowUserInput() {
        return allowUserInput;
    }

    public SnackProgressBar setAllowUserInput(boolean allowUserInput) {
        this.allowUserInput = allowUserInput;
        return this;
    }

    public boolean isSwipeToDismiss() {
        return swipeToDismiss;
    }

    public SnackProgressBar setSwipeToDismiss(boolean swipeToDismiss) {
        this.swipeToDismiss = swipeToDismiss;
        return this;
    }

    public boolean isShowProgressPercentage() {
        return showProgressPercentage;
    }

    public SnackProgressBar setShowProgressPercentage(boolean showProgressPercentage) {
        this.showProgressPercentage = showProgressPercentage;
        return this;
    }

    public int getId() {
        return id;
    }

    public SnackProgressBar setId(@IntRange(from = 1) int id) {
        this.id = id;
        return this;
    }

    public int getType() {
        return type;
    }

    public SnackProgressBar setType(int type) {
        this.type = type;
        return this;
    }

    public int getProgressMax() {
        return progressMax;
    }

    public SnackProgressBar setProgressMax(@IntRange(from = 1) int progressMax) {
        this.progressMax = progressMax;
        return this;
    }

    public String getAction() {
        return action;
    }

    public SnackProgressBar setAction(@NonNull String action) {
        this.action = action;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public SnackProgressBar setMessage(@NonNull String message) {
        this.message = message;
        return this;
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
