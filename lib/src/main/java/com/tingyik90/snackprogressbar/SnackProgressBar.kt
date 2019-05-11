package com.tingyik90.snackprogressbar

import android.graphics.Bitmap
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import androidx.annotation.Keep
import com.tingyik90.snackprogressbar.SnackProgressBar.Companion.TYPE_CIRCULAR
import com.tingyik90.snackprogressbar.SnackProgressBar.Companion.TYPE_HORIZONTAL
import com.tingyik90.snackprogressbar.SnackProgressBar.Companion.TYPE_NORMAL

/**
 * SnackProgressBar is the holder for information to be displayed via SnackProgressBarManager.
 *
 * @constructor Creates a SnackProgressBar.
 * @param type SnackProgressBar of either [TYPE_NORMAL], [TYPE_HORIZONTAL] or [TYPE_CIRCULAR]
 * @param message Message of the SnackProgressBar.
 */
@Keep
class SnackProgressBar(@SnackProgressBarType private var type: Int, private var message: String) {

    /* definition */
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(TYPE_NORMAL, TYPE_HORIZONTAL, TYPE_CIRCULAR)
    annotation class SnackProgressBarType

    /* companion object */
    @Keep
    companion object {

        /**
         * SnackProgressBar layout with message only.
         */
        const val TYPE_NORMAL = 100
        /**
         * SnackProgressBar layout with message and horizontal ProgressBar.
         */
        const val TYPE_HORIZONTAL = 200
        /**
         * SnackProgressBar layout with message and circular ProgressBar.
         */
        const val TYPE_CIRCULAR = 300
        /**
         * Placeholder for no icon.
         */
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

    /* parameters */
    private var action: String = ""
    private var onActionClickListener: OnActionClickListener? = null
    private var iconBitmap: Bitmap? = null
    private var iconResId: Int = DEFAULT_ICON_RES_ID
    private var progressMax: Int = 100
    private var allowUserInput: Boolean = false
    private var swipeToDismiss: Boolean = false
    private var isIndeterminate: Boolean = false
    private var showProgressPercentage: Boolean = false
    private var bundle: Bundle? = null

    /* constructor */
    internal constructor (
        type: Int,
        message: String,
        action: String,
        onActionClickListener: OnActionClickListener?,
        iconBitmap: Bitmap?,
        iconResId: Int,
        progressMax: Int,
        allowUserInput: Boolean,
        swipeToDismiss: Boolean,
        isIndeterminate: Boolean,
        showProgressPercentage: Boolean,
        bundle: Bundle?
    ) : this(type, message) {
        this.action = action
        this.onActionClickListener = onActionClickListener
        this.iconBitmap = iconBitmap
        this.iconResId = iconResId
        this.progressMax = progressMax
        this.allowUserInput = allowUserInput
        this.swipeToDismiss = swipeToDismiss
        this.isIndeterminate = isIndeterminate
        this.showProgressPercentage = showProgressPercentage
        this.bundle = bundle
    }

    /**
     * Sets the type of SnackProgressBar.
     *
     * @param type SnackProgressBar of either [TYPE_NORMAL], [TYPE_HORIZONTAL] or [TYPE_CIRCULAR]
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
     * @param message Message of be displayed.
     * @return This SnackProgressBar.
     */
    fun setMessage(message: String): SnackProgressBar {
        this.message = message
        return this
    }

    internal fun getMessage(): String {
        return message
    }

    /**
     * Sets the action of SnackProgressBar. The action can be clicked to trigger [OnActionClickListener].
     *
     * @param action Action to be displayed.
     * @return This SnackProgressBar.
     */
    fun setAction(action: String, onActionClickListener: OnActionClickListener?): SnackProgressBar {
        this.action = action
        this.onActionClickListener = onActionClickListener
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
     * @param bitmap Bitmap of the icon to be displayed.
     * @return This SnackProgressBar.
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
     * @return This SnackProgressBar.
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
     * Sets the max progress for determinate ProgressBar.
     *
     * @param progressMax Max progress for determinate ProgressBar. Default = 100.
     * @return This SnackProgressBar.
     */
    fun setProgressMax(@IntRange(from = 1) progressMax: Int): SnackProgressBar {
        this.progressMax = progressMax
        return this
    }

    internal fun getProgressMax(): Int {
        return progressMax
    }

    /**
     * Sets whether user input is allowed. Setting to FALSE will display an OverlayLayout which blocks user input.
     *
     * @param allowUserInput Whether to allow user input. Default = FALSE.
     * @return This SnackProgressBar.
     */
    fun setAllowUserInput(allowUserInput: Boolean): SnackProgressBar {
        this.allowUserInput = allowUserInput
        return this
    }

    internal fun isAllowUserInput(): Boolean {
        return allowUserInput
    }

    /**
     * Sets whether user can swipe the SnackProgressBar to dismiss it.
     *
     * @param swipeToDismiss Whether user can swipe to dismiss. Default = FALSE.
     * @return This SnackProgressBar.
     */
    fun setSwipeToDismiss(swipeToDismiss: Boolean): SnackProgressBar {
        this.swipeToDismiss = swipeToDismiss
        return this
    }

    internal fun isSwipeToDismiss(): Boolean {
        return swipeToDismiss
    }

    /**
     * Sets whether the ProgressBar is indeterminate.
     *
     * @param isIndeterminate Whether the ProgressBar is indeterminate. Default = FALSE.
     * @return This SnackProgressBar.
     */
    fun setIsIndeterminate(isIndeterminate: Boolean): SnackProgressBar {
        this.isIndeterminate = isIndeterminate
        return this
    }

    internal fun isIndeterminate(): Boolean {
        return isIndeterminate
    }

    /**
     * Sets whether to show progress in percentage.
     *
     * @param showProgressPercentage Whether to show progress in percentage. Default = FALSE.
     * @return This SnackProgressBar.
     */
    fun setShowProgressPercentage(showProgressPercentage: Boolean): SnackProgressBar {
        this.showProgressPercentage = showProgressPercentage
        return this
    }

    internal fun isShowProgressPercentage(): Boolean {
        return showProgressPercentage
    }

    /**
     * Sets the additional bundle of SnackProgressBar, which can be retrieved later.
     *
     * @param bundle Any additional information to be attached via [Bundle].
     * @return This SnackProgressBar.
     */
    fun putBundle(bundle: Bundle): SnackProgressBar {
        this.bundle = bundle
        return this
    }

    /**
     * Gets the additional bundle of SnackProgressBar. This value may be null.
     *
     * @return Any additional information attached to this SnackProgressBar via [Bundle].
     */
    fun getBundle(): Bundle? {
        return bundle
    }

    /**
     * Returns a string representation of the SnackProgressBar.
     */
    override fun toString(): String {
        val typeString = when (type) {
            TYPE_CIRCULAR -> "TYPE_CIRCULAR"
            TYPE_HORIZONTAL -> "TYPE_HORIZONTAL"
            else -> "TYPE_NORMAL"
        }
        val hasIcon = iconBitmap != null && iconResId != DEFAULT_ICON_RES_ID
        val hasBundle = bundle != null
        return "SnackProgressBar(type='$typeString', " +
                "message='$message', " +
                "action='$action', " +
                "hasIcon=$hasIcon, " +
                "progressMax=$progressMax, " +
                "allowUserInput=$allowUserInput, " +
                "swipeToDismiss=$swipeToDismiss, " +
                "isIndeterminate=$isIndeterminate, " +
                "showProgressPercentage=$showProgressPercentage, " +
                "hasBundle=$hasBundle)"
    }

}