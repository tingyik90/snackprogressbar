package com.tingyik90.snackprogressbar

import android.os.Handler
import android.support.annotation.IntRange
import android.support.annotation.Keep
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Core class constructing the SnackProgressBar.
 */
@Keep
internal class SnackProgressBarCore private constructor(
        parent: ViewGroup,
        content: View,
        contentViewCallback: ContentViewCallback
) : BaseTransientBottomBar<SnackProgressBarCore>(parent, content, contentViewCallback) {

    /* variables */
    private val shortDurationMillis = 1500L          // as per SnackbarManager
    private val longDurationMillis = 2750L           // as per SnackbarManager
    private val handler = Handler()
    private val runnable = Runnable { dismiss() }

    private var showDuration = 0L
    private var useDefaultHandler = true
    private lateinit var snackProgressBar: SnackProgressBar
    private lateinit var parentView: ViewGroup
    private lateinit var overlayLayout: View
    private lateinit var snackProgressBarLayout: SnackProgressBarLayout

    companion object {
        /**
         * Prepares SnackProgressBarCore.
         *
         * @param parentView       View to hold the SnackProgressBar.
         * @param snackProgressBar SnackProgressBar to be shown.
         * @param showDuration     Duration to show the SnackProgressBar.
         * @param viewsToMove      View to be animated along with the SnackProgressBar.
         */
        internal fun make(parentView: ViewGroup, snackProgressBar: SnackProgressBar,
                          showDuration: Long, viewsToMove: Array<View>?): SnackProgressBarCore {
            // get inflater from parent
            val inflater = LayoutInflater.from(parentView.context)
            // add overlayLayout as background
            val overlayLayout = inflater.inflate(R.layout.overlay, parentView, false)
            parentView.addView(overlayLayout)
            // inflate SnackProgressBarLayout and pass viewsToMove
            val snackProgressBarLayout = inflater.inflate(
                    R.layout.snackprogressbar, parentView, false) as SnackProgressBarLayout
            snackProgressBarLayout.setViewsToMove(viewsToMove)
            // create SnackProgressBarCore
            val snackProgressBarCore = SnackProgressBarCore(
                    parentView, snackProgressBarLayout, snackProgressBarLayout)
            snackProgressBarCore.snackProgressBar = snackProgressBar
            snackProgressBarCore.parentView = parentView
            snackProgressBarCore.overlayLayout = overlayLayout
            snackProgressBarCore.snackProgressBarLayout = snackProgressBarLayout
            snackProgressBarCore.useDefaultHandler = parentView is CoordinatorLayout
            snackProgressBarCore.showDuration = showDuration
            snackProgressBarCore.updateTo(snackProgressBar)
            return snackProgressBarCore
        }
    }

    /**
     * Updates the SnackProgressBar without dismissing it to the new SnackProgressBar.
     *
     * @param snackProgressBar SnackProgressBar to be updated to.
     */
    internal fun updateTo(snackProgressBar: SnackProgressBar) {
        this.snackProgressBar = snackProgressBar
        setType()
        setIcon()
        setAction()
        showProgressPercentage()
        setProgressMax()
        setSwipeToDismiss()
        setMessage()
        // only toggle overlayLayout visibility if already shown
        if (isShown) {
            showOverlayLayout()
        }
    }

    /**
     * Updates the color and alpha of overlayLayout.
     *
     * @param overlayColor       R.color id.
     * @param overlayLayoutAlpha Alpha between 0f to 1f. Default = 0.8f.
     */
    fun setOverlayLayout(overlayColor: Int, overlayLayoutAlpha: Float): SnackProgressBarCore {
        overlayLayout.setBackgroundColor(ContextCompat.getColor(context, overlayColor))
        overlayLayout.alpha = overlayLayoutAlpha
        return this
    }

    /**
     * Updates the color of the layout.
     *
     * @param backgroundColor  R.color id.
     * @param messageTextColor R.color id.
     * @param actionTextColor  R.color id.
     * @param progressBarColor R.color id.
     */
    fun setColor(backgroundColor: Int, messageTextColor: Int, actionTextColor: Int, progressBarColor: Int): SnackProgressBarCore {
        snackProgressBarLayout.setColor(backgroundColor, messageTextColor, actionTextColor, progressBarColor)
        return this
    }

    /**
     * Sets the progress for SnackProgressBar.
     *
     * @param progress Progress of the ProgressBar.
     */
    fun setProgress(@IntRange(from = 0) progress: Int): SnackProgressBarCore {
        val determinateProgressBar = snackProgressBarLayout.determinateProgressBar
        determinateProgressBar.progress = progress
        val progress100 = (progress.toFloat() / determinateProgressBar.max * 100).toInt()
        val progressString = progress100.toString() + "%"
        snackProgressBarLayout.progressText.text = progressString
        return this
    }

    override fun show() {
        // show overLayLayout
        showOverlayLayout()
        // use default SnackManager if it is CoordinatorLayout
        if (useDefaultHandler) {
            duration = showDuration.toInt()
        }
        // else, set up own handler for dismiss countdown
        else {
            setOnBarTouchListener()
            // disable SnackManager by stopping countdown
            duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            // assign the actual showDuration if dismiss is required
            if (showDuration != SnackProgressBarManager.LENGTH_INDEFINITE) {
                when (showDuration) {
                    SnackProgressBarManager.LENGTH_SHORT -> showDuration = shortDurationMillis
                    SnackProgressBarManager.LENGTH_LONG -> showDuration = longDurationMillis
                }
                handler.postDelayed(runnable, showDuration)
            }
        }
        super.show()
    }

    /**
     * Sets the layout based on SnackProgressBar type.
     * Note: Layout for action is handled by [SnackProgressBarLayout]
     */
    private fun setType(): SnackProgressBarCore {
        val determinateProgressBar = snackProgressBarLayout.determinateProgressBar
        val indeterminateProgressBar = snackProgressBarLayout.indeterminateProgressBar
        // update view
        val type = snackProgressBar.getType()
        when (type) {
            SnackProgressBar.TYPE_ACTION, SnackProgressBar.TYPE_MESSAGE -> {
                determinateProgressBar.visibility = View.GONE
                indeterminateProgressBar.visibility = View.GONE
            }
            SnackProgressBar.TYPE_DETERMINATE -> {
                determinateProgressBar.visibility = View.VISIBLE
                indeterminateProgressBar.visibility = View.GONE
            }
            SnackProgressBar.TYPE_INDETERMINATE -> {
                determinateProgressBar.visibility = View.GONE
                indeterminateProgressBar.visibility = View.VISIBLE
            }
        }
        return this
    }

    /**
     * Sets the icon of SnackProgressBar.
     */
    private fun setIcon(): SnackProgressBarCore {
        val iconBitmap = snackProgressBar.getIconBitmap()
        val iconResId = snackProgressBar.getIconResource()
        val iconImage = snackProgressBarLayout.iconImage
        when {
            iconBitmap != null -> {
                iconImage.setImageBitmap(iconBitmap)
                iconImage.visibility = View.VISIBLE
            }
            iconResId != SnackProgressBar.DEFAULT_ICON_RES_ID -> {
                iconImage.setImageResource(iconResId)
                iconImage.visibility = View.VISIBLE
            }
            else -> iconImage.visibility = View.GONE
        }
        return this
    }

    /**
     * Set the action to be displayed. Only will be shown for TYPE_ACTION.
     */
    private fun setAction(): SnackProgressBarCore {
        val type = snackProgressBar.getType()
        val action = snackProgressBar.getAction()
        val onActionClickListener = snackProgressBar.getOnActionClickListener()
        if (type == SnackProgressBar.TYPE_ACTION) {
            // set the text
            val actionText = snackProgressBarLayout.actionText
            val actionNextLineText = snackProgressBarLayout.actionNextLineText
            actionText.text = action.toUpperCase()
            actionNextLineText.text = action.toUpperCase()
            // set the onClickListener
            val onClickListener = View.OnClickListener {
                onActionClickListener?.onActionClick()
                dismiss()
            }
            actionText.setOnClickListener(onClickListener)
            actionNextLineText.setOnClickListener(onClickListener)
        }
        return this
    }

    /**
     * Set whether to show progressText. Only will be shown for TYPE_DETERMINATE.
     */
    private fun showProgressPercentage(): SnackProgressBarCore {
        val type = snackProgressBar.getType()
        val showProgressPercentage = snackProgressBar.isShowProgressPercentage()
        val progressText = snackProgressBarLayout.progressText
        if (showProgressPercentage && type == SnackProgressBar.TYPE_DETERMINATE) {
            progressText.visibility = View.VISIBLE
        } else {
            progressText.visibility = View.GONE
        }
        return this
    }

    /**
     * Set the max progress for progressBar.
     */
    private fun setProgressMax(): SnackProgressBarCore {
        snackProgressBarLayout.determinateProgressBar.max = snackProgressBar.getProgressMax()
        return this
    }

    /**
     * Set whether user can swipe to dismiss.
     * This only works for TYPE_ACTION and TYPE_MESSAGE.
     */
    private fun setSwipeToDismiss(): SnackProgressBarCore {
        snackProgressBarLayout.setSwipeToDismiss(snackProgressBar.isSwipeToDismiss())
        return this
    }

    /**
     * Sets the message of SnackProgressBar.
     */
    private fun setMessage(): SnackProgressBarCore {
        snackProgressBarLayout.messageText.text = snackProgressBar.getMessage()
        return this
    }

    /**
     * Shows the overlayLayout based on whether user input is allowed.
     */
    private fun showOverlayLayout(): SnackProgressBarCore {
        if (!snackProgressBar.isAllowUserInput()) {
            overlayLayout.visibility = View.VISIBLE
        } else {
            overlayLayout.visibility = View.GONE
        }
        return this
    }

    /**
     * Removes the overlayLayout
     */
    internal fun removeOverlayLayout() {
        parentView.removeView(overlayLayout)
    }

    /**
     * Registers a callback to be invoked when the SnackProgressBar is touched.
     * This is only applicable when swipe to dismiss behaviour is true and CoordinatorLayout is not used.
     */
    private fun setOnBarTouchListener() {
        snackProgressBarLayout.setOnBarTouchListener(object : SnackProgressBarLayout.OnBarTouchListener {
            override fun onTouch(event: Int) {
                when (event) {
                    SnackProgressBarLayout.ACTION_DOWN ->
                        // when user touched the SnackProgressBar, stop the dismiss countdown
                        handler.removeCallbacks(runnable)
                    SnackProgressBarLayout.SWIPE_OUT ->
                        // once the SnackProgressBar is swiped out, dismiss after animation ends
                        handler.postDelayed(runnable, SnackProgressBarLayout.ANIMATION_DURATION)
                    SnackProgressBarLayout.SWIPE_IN ->
                        // once the SnackProgressBar is swiped in, resume dismiss countdown
                        if (showDuration != SnackProgressBarManager.LENGTH_INDEFINITE) {
                            handler.postDelayed(runnable, showDuration)
                        }
                }
            }
        })
    }
}