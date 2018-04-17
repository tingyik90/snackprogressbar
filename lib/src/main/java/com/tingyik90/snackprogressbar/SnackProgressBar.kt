package com.tingyik90.snackprogressbar

import android.graphics.Bitmap
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.IntDef
import android.support.annotation.IntRange
import android.support.annotation.Keep
import com.tingyik90.snackprogressbar.SnackProgressBar.Companion.TYPE_ACTION
import com.tingyik90.snackprogressbar.SnackProgressBar.Companion.TYPE_DETERMINATE
import com.tingyik90.snackprogressbar.SnackProgressBar.Companion.TYPE_INDETERMINATE
import com.tingyik90.snackprogressbar.SnackProgressBar.Companion.TYPE_MESSAGE

/**
 * Main class containing the display information of SnackProgressBar to be displayed
 * via SnackProgressBarManager.
 *
 * @property type SnackProgressBar of either
 *  [TYPE_ACTION], [TYPE_DETERMINATE], [TYPE_INDETERMINATE] or [TYPE_MESSAGE]
 * @property message Message of SnackProgressBar.
 */
@Keep
class SnackProgressBar(@Type private var type: Int, private var message: String) {

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(TYPE_ACTION, TYPE_DETERMINATE, TYPE_INDETERMINATE, TYPE_MESSAGE)
    annotation class Type

    companion object {
        /**
         * SnackProgressBar layout with message and action button.
         */
        const val TYPE_ACTION = 100
        /**
         * SnackProgressBar layout with message, determinate progressBar and progress percentage.
         */
        const val TYPE_DETERMINATE = 200
        /**
         * SnackProgressBar layout with message and indeterminate progressBar.
         */
        const val TYPE_INDETERMINATE = 300
        /**
         * SnackProgressBar layout with message only.
         */
        const val TYPE_MESSAGE = 400

        internal const val DEFAULT_ICON_RES_ID = -1
    }

    /**
     * Interface definition for a callback to be invoked when an action is clicked.
     */
    interface OnActionClickListener {
        /**
         * Called when an action is clicked.
         */
        fun onActionClick()
    }

    /* variables */
    private var action: String = ""
    private var iconBitmap: Bitmap? = null
    private var iconResId: Int = DEFAULT_ICON_RES_ID
    private var progressMax: Int = 100
    private var allowUserInput: Boolean = false
    private var swipeToDismiss: Boolean = false
    private var showProgressPercentage: Boolean = true
    private var onActionClickListener: OnActionClickListener? = null
    private var bundle: Bundle? = null

    /**
     * Internal constructor for duplicating SnackProgressBar.
     */
    internal constructor (type: Int,
                          message: String,
                          action: String,
                          iconBitmap: Bitmap?,
                          iconResId: Int,
                          progressMax: Int,
                          allowUserInput: Boolean,
                          swipeToDismiss: Boolean,
                          showProgressPercentage: Boolean,
                          onActionClickListener: OnActionClickListener?,
                          bundle: Bundle?) : this(type, message) {
        this.action = action
        this.iconBitmap = iconBitmap
        this.iconResId = iconResId
        this.progressMax = progressMax
        this.allowUserInput = allowUserInput
        this.swipeToDismiss = swipeToDismiss
        this.showProgressPercentage = showProgressPercentage
        this.onActionClickListener = onActionClickListener
        this.bundle = bundle
    }

    /**
     * Sets the type of SnackProgressBar.
     *
     * @param type SnackProgressBar of either
     * [TYPE_ACTION], [TYPE_DETERMINATE], [TYPE_INDETERMINATE] or [TYPE_MESSAGE]
     */
    fun setType(type: Int) {
        this.type = type
    }

    internal fun getType(): Int {
        return type
    }

    /**
     * Sets the message of SnackProgressBar.
     *
     * @param message Message of SnackProgressBar.
     */
    fun setMessage(message: String): SnackProgressBar {
        this.message = message
        return this
    }

    internal fun getMessage(): String {
        return message
    }

    /**
     * Sets the action of SnackProgressBar. Only will be shown for TYPE_ACTION.
     *
     * @param action Action to be displayed.
     */
    fun setAction(action: String, onActionClickListener: OnActionClickListener?): SnackProgressBar {
        if (type == TYPE_ACTION) {
            this.action = action
            this.onActionClickListener = onActionClickListener
        }
        return this
    }

    internal fun getAction(): String {
        return action
    }

    internal fun getOnActionClickListener(): OnActionClickListener? {
        return onActionClickListener
    }

    /**
     * Sets the icon of SnackProgressBar. Only a bitmap or a resId can be specified at any one time.
     *
     * @param bitmap Bitmap of icon.
     */
    fun setIconBitmap(bitmap: Bitmap): SnackProgressBar {
        iconBitmap = bitmap
        iconResId = DEFAULT_ICON_RES_ID
        return this
    }

    internal fun getIconBitmap(): Bitmap? {
        return iconBitmap
    }

    /**
     * Sets the icon of SnackProgressBar. Only a bitmap or a resId can be specified at any one time.
     *
     * @param iconResId The resource identifier of the icon to be displayed.
     */
    fun setIconResource(@DrawableRes iconResId: Int): SnackProgressBar {
        iconBitmap = null
        this.iconResId = iconResId
        return this
    }

    internal fun getIconResource(): Int {
        return iconResId
    }

    /**
     * Sets the max progress for determinate ProgressBar. Only will be shown for TYPE_DETERMINATE.
     *
     * @param progressMax Max progress for determinate ProgressBar. Default = 100.
     */
    fun setProgressMax(@IntRange(from = 1) progressMax: Int): SnackProgressBar {
        if (type == TYPE_DETERMINATE) {
            this.progressMax = progressMax
        }
        return this
    }

    internal fun getProgressMax(): Int {
        return progressMax
    }

    /**
     * Sets whether user input is allowed. Setting to FALSE will display an OverlayLayout which blocks user input.
     *
     * @param allowUserInput Whether to allow user input. Default = FALSE.
     */
    fun setAllowUserInput(allowUserInput: Boolean): SnackProgressBar {
        this.allowUserInput = allowUserInput
        return this
    }

    internal fun isAllowUserInput(): Boolean {
        return allowUserInput
    }

    /**
     * Sets whether user can swipe to dismiss.
     * Swipe to dismiss only works for TYPE_ACTION and TYPE_MESSAGE.
     *
     * @param swipeToDismiss Whether user can swipe to dismiss. Default = FALSE.
     */
    fun setSwipeToDismiss(swipeToDismiss: Boolean): SnackProgressBar {
        this.swipeToDismiss = swipeToDismiss
        when (type) {
        // don't allow swipe to dismiss
            SnackProgressBar.TYPE_DETERMINATE, SnackProgressBar.TYPE_INDETERMINATE -> this.swipeToDismiss = false
        }
        return this
    }

    internal fun isSwipeToDismiss(): Boolean {
        return swipeToDismiss
    }

    /**
     * Sets whether to show progress in percentage. Only will be shown for TYPE_DETERMINATE.
     *
     * @param showProgressPercentage Whether to show progressText. Default = TRUE.
     */
    fun setShowProgressPercentage(showProgressPercentage: Boolean): SnackProgressBar {
        if (type == TYPE_DETERMINATE) {
            this.showProgressPercentage = showProgressPercentage
        }
        return this
    }

    internal fun isShowProgressPercentage(): Boolean {
        return showProgressPercentage
    }

    /**
     * Sets the additional bundle of SnackProgressBar.
     *
     * @param bundle Bundle of SnackProgressBar.
     */
    fun setBundle(bundle: Bundle): SnackProgressBar {
        this.bundle = bundle
        return this
    }

    /**
     * Gets the additional bundle of SnackProgressBar. This value may be null.
     *
     */
    fun getBundle(): Bundle? {
        return bundle
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder("SnackProgressBar{")
                .append("type=").append(type)
                .append(", message='").append(message).append("\'")
        if (iconBitmap != null) {
            stringBuilder.append(", iconBitmap=").append(iconBitmap.toString())
        }
        if (iconResId != DEFAULT_ICON_RES_ID) {
            stringBuilder.append(", iconResId=").append(iconResId)
        }
        if (type == TYPE_DETERMINATE) {
            stringBuilder.append(", progressMax=").append(progressMax)
            stringBuilder.append(", showProgressPercentage=").append(showProgressPercentage)
        }
        stringBuilder.append(", allowUserInput=").append(allowUserInput)
                .append(", swipeToDismiss=").append(swipeToDismiss)
                .append("}")
        return stringBuilder.toString()
    }
}