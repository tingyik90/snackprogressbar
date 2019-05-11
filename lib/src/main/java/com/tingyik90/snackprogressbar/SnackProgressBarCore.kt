package com.tingyik90.snackprogressbar

import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.IntRange
import androidx.annotation.Keep
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.tingyik90.snackprogressbar.SnackProgressBar.Companion.TYPE_CIRCULAR
import com.tingyik90.snackprogressbar.SnackProgressBar.Companion.TYPE_HORIZONTAL
import com.tingyik90.snackprogressbar.SnackProgressBar.Companion.TYPE_NORMAL

/**
 * Core class constructing the SnackProgressBar.
 *
 * @constructor Create a SnackProgressBarCore.
 * @param parentView View to hold the SnackProgressBarLayout.
 * @param snackProgressBarLayout [SnackProgressBarLayout] to be displayed.
 * @param overlayLayout OverlayLayout to be displayed.
 * @param showDuration Duration to show the SnackProgressBarLayout.
 * @param snackProgressBar [SnackProgressBar] which is attached to the SnackProgressBarLayout.
 */
@Keep
internal class SnackProgressBarCore private constructor(
    private val parentView: ViewGroup,
    val snackProgressBarLayout: SnackProgressBarLayout,
    val overlayLayout: FrameLayout,
    private var showDuration: Int,
    private var snackProgressBar: SnackProgressBar
) : BaseTransientBottomBar<SnackProgressBarCore>(parentView, snackProgressBarLayout, snackProgressBarLayout) {

    /* variables */
    // Duration as per SnackbarManager
    private val shortDurationMillis = 1500
    private val longDurationMillis = 2750
    private val handler = Handler()
    private val runnable = Runnable { dismiss() }

    /* companion object */
    @Keep
    companion object {

        /**
         * Prepares SnackProgressBarCore.
         *
         * @param parentView       View to hold the SnackProgressBar, prepared by SnackProgressBarManager
         * @param snackProgressBar SnackProgressBar to be shown.
         * @param showDuration     Duration to show the SnackProgressBar.
         * @param viewsToMove      View to be animated along with the SnackProgressBar.
         */
        internal fun make(
            parentView: ViewGroup, snackProgressBar: SnackProgressBar,
            showDuration: Int, viewsToMove: Array<View>?
        ): SnackProgressBarCore {
            // Get inflater from parent
            val inflater = LayoutInflater.from(parentView.context)
            // Add overlayLayout as background
            val overlayLayout = inflater.inflate(R.layout.overlay, parentView, false) as FrameLayout
            // Starting v6.0, assign unique view id to overlayLayout.
            // There has been cases where overlayLayout is not correctly removed when the first (with overLay)
            // and second (without overLay) snackProgressBar are updated too quickly. This maybe due to removing the
            // incorrect child from parent view.
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                overlayLayout.id = View.generateViewId()
            }
            parentView.addView(overlayLayout)
            // Inflate SnackProgressBarLayout and pass viewsToMove
            val snackProgressBarLayout = inflater.inflate(
                R.layout.snackprogressbar, parentView, false
            ) as SnackProgressBarLayout
            snackProgressBarLayout.setViewsToMove(viewsToMove)
            // Create SnackProgressBarCore
            val snackProgressBarCore = SnackProgressBarCore(
                parentView,
                snackProgressBarLayout,
                overlayLayout,
                showDuration,
                snackProgressBar
            )
            snackProgressBarCore.updateTo(snackProgressBar)
            return snackProgressBarCore
        }

    }

    /**
     * Gets the attached snackProgressBar.
     */
    internal fun getSnackProgressBar(): SnackProgressBar {
        return snackProgressBar
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
    internal fun setOverlayLayout(overlayColor: Int, overlayLayoutAlpha: Float): SnackProgressBarCore {
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
     * @param progressTextColor R.color id.
     */
    internal fun setColor(
        backgroundColor: Int,
        messageTextColor: Int,
        actionTextColor: Int,
        progressBarColor: Int,
        progressTextColor: Int
    ): SnackProgressBarCore {
        snackProgressBarLayout.setColor(
            backgroundColor,
            messageTextColor,
            actionTextColor,
            progressBarColor,
            progressTextColor
        )
        return this
    }

    /**
     * Sets the text size of SnackProgressBar.
     *
     * @param px Font size in pixels.
     */
    internal fun setTextSize(px: Float): SnackProgressBarCore {
        snackProgressBarLayout.setTextSize(px)
        return this
    }

    /**
     * Sets the max lines for message.
     *
     * @param maxLines Number of lines.
     */
    internal fun setMaxLines(maxLines: Int): SnackProgressBarCore {
        snackProgressBarLayout.setMaxLines(maxLines)
        return this
    }

    /**
     * Sets the progress for SnackProgressBar.
     *
     * @param progress Progress of the ProgressBar.
     */
    internal fun setProgress(@IntRange(from = 0) progress: Int): SnackProgressBarCore {
        val progressBar =
            when (snackProgressBar.getType()) {
                TYPE_HORIZONTAL -> snackProgressBarLayout.horizontalProgressBar
                TYPE_CIRCULAR -> snackProgressBarLayout.circularDeterminateProgressBar
                else -> null
            }
        if (progressBar != null) {
            progressBar.progress = progress
            val progress100 = (progress.toFloat() / progressBar.max * 100).toInt()
            var progressString = progress100.toString()
            snackProgressBarLayout.progressTextCircular.text = progressString
            // Include % for progressText
            progressString += "%"
            snackProgressBarLayout.progressText.text = progressString
        }
        return this
    }

    /**
     * Show the SnackProgressBar
     */
    override fun show() {
        // Show overLayLayout
        showOverlayLayout()
        // Use default SnackManager if it is CoordinatorLayout
        if (parentView is CoordinatorLayout) {
            duration = showDuration
        }
        // Else, set up own handler for dismiss countdown
        else {
            setOnBarTouchListener()
            // Disable SnackManager by stopping countdown
            duration = LENGTH_INDEFINITE
            // Assign the actual showDuration if dismiss is required
            if (showDuration != LENGTH_INDEFINITE) {
                when (showDuration) {
                    LENGTH_SHORT -> showDuration = shortDurationMillis
                    LENGTH_LONG -> showDuration = longDurationMillis
                }
                handler.postDelayed(runnable, showDuration.toLong())
            }
        }
        super.show()
    }

    /**
     * Sets the layout based on SnackProgressBar type.
     * Note: Layout positioning for action is handled by [SnackProgressBarLayout]
     */
    private fun setType(): SnackProgressBarCore {
        // Update view
        when (snackProgressBar.getType()) {
            TYPE_NORMAL -> {
                snackProgressBarLayout.horizontalProgressBar.visibility = View.GONE
                snackProgressBarLayout.circularDeterminateProgressBar.visibility = View.GONE
                snackProgressBarLayout.circularIndeterminateProgressBar.visibility = View.GONE
            }
            TYPE_HORIZONTAL -> {
                snackProgressBarLayout.horizontalProgressBar.visibility = View.VISIBLE
                snackProgressBarLayout.circularDeterminateProgressBar.visibility = View.GONE
                snackProgressBarLayout.circularIndeterminateProgressBar.visibility = View.GONE
                snackProgressBarLayout.horizontalProgressBar.isIndeterminate = snackProgressBar.isIndeterminate()
            }
            TYPE_CIRCULAR -> {
                snackProgressBarLayout.horizontalProgressBar.visibility = View.GONE
                if (snackProgressBar.isIndeterminate()) {
                    snackProgressBarLayout.circularDeterminateProgressBar.visibility = View.GONE
                    snackProgressBarLayout.circularIndeterminateProgressBar.visibility = View.VISIBLE
                } else {
                    snackProgressBarLayout.circularDeterminateProgressBar.visibility = View.VISIBLE
                    snackProgressBarLayout.circularIndeterminateProgressBar.visibility = View.GONE
                }
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
        when {
            iconBitmap != null -> {
                snackProgressBarLayout.iconImage.setImageBitmap(iconBitmap)
                snackProgressBarLayout.iconImage.visibility = View.VISIBLE
            }
            iconResId != SnackProgressBar.DEFAULT_ICON_RES_ID -> {
                snackProgressBarLayout.iconImage.setImageResource(iconResId)
                snackProgressBarLayout.iconImage.visibility = View.VISIBLE
            }
            else -> snackProgressBarLayout.iconImage.visibility = View.GONE
        }
        return this
    }

    /**
     * Sets the action to be displayed.
     */
    private fun setAction(): SnackProgressBarCore {
        val action = snackProgressBar.getAction()
        val onActionClickListener = snackProgressBar.getOnActionClickListener()
        // Set the text
        snackProgressBarLayout.actionText.text = action.toUpperCase()
        snackProgressBarLayout.actionNextLineText.text = action.toUpperCase()
        // Set the onClickListener
        val onClickListener = View.OnClickListener {
            onActionClickListener?.onActionClick()
            dismiss()
        }
        snackProgressBarLayout.actionText.setOnClickListener(onClickListener)
        snackProgressBarLayout.actionNextLineText.setOnClickListener(onClickListener)
        return this
    }

    /**
     * Sets whether to show progressText.
     */
    private fun showProgressPercentage(): SnackProgressBarCore {
        if (snackProgressBar.isShowProgressPercentage()) {
            if (snackProgressBar.getType() == TYPE_CIRCULAR) {
                snackProgressBarLayout.progressText.visibility = View.GONE
                snackProgressBarLayout.progressTextCircular.visibility = View.VISIBLE
            } else {
                snackProgressBarLayout.progressText.visibility = View.VISIBLE
                snackProgressBarLayout.progressTextCircular.visibility = View.GONE
            }
        } else {
            snackProgressBarLayout.progressText.visibility = View.GONE
            snackProgressBarLayout.progressTextCircular.visibility = View.GONE
        }
        return this
    }

    /**
     * Sets the max progress for progressBar.
     */
    private fun setProgressMax(): SnackProgressBarCore {
        snackProgressBarLayout.horizontalProgressBar.max = snackProgressBar.getProgressMax()
        snackProgressBarLayout.circularDeterminateProgressBar.max = snackProgressBar.getProgressMax()
        return this
    }

    /**
     * Sets whether user can swipe to dismiss.
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
                        // When user touched the SnackProgressBar, stop the dismiss countdown
                        handler.removeCallbacks(runnable)
                    SnackProgressBarLayout.SWIPE_OUT ->
                        // Once the SnackProgressBar is swiped out, dismiss after animation ends
                        handler.postDelayed(runnable, SnackProgressBarLayout.ANIMATION_DURATION)
                    SnackProgressBarLayout.SWIPE_IN ->
                        // Once the SnackProgressBar is swiped in, resume dismiss countdown
                        if (showDuration != SnackProgressBarManager.LENGTH_INDEFINITE) {
                            handler.postDelayed(runnable, showDuration.toLong())
                        }
                }
            }
        })
    }
}