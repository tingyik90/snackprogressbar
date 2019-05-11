package com.tingyik90.snackprogressbar

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.snackbar.BaseTransientBottomBar
import java.lang.ref.WeakReference
import java.util.*

/**
 * Manager class handling all the SnackProgressBars added.
 *
 * This class queues the SnackProgressBars to be shown.
 * It will dismiss the SnackProgressBar according to its desired duration before showing the next in queue.
 *
 * In the constructor, provide a view to search for a suitable parent view to hold the SnackProgressBar.
 * If possible, it should be the root view of the activity and can be any type of layout.
 * If a CoordinatorLayout is provided, the FloatingActionButton will animate with the SnackProgressBar.
 * Else, [setViewsToMove] needs to be called to select the views to be animated.
 * Note that [setViewsToMove] can still be used for CoordinatorLayout to move other views.
 *
 * Swipe to dismiss behaviour can be set via [SnackProgressBar.setSwipeToDismiss].
 * This is provided via [BaseTransientBottomBar] for CoordinatorLayout, but provided via
 * [SnackProgressBarLayout] for other layout types.
 *
 * Starting v6.0, you can provide a [LifecycleOwner] of an activity / fragment.
 * This provides a quick way to automatically [dismissAll] and remove [onDisplayListener] during onDestroy of
 * the activity / fragment to prevent memory leak.
 *
 * @constructor Create an instance of SnackProgressBarManager.
 * @param providedView View provided to search for parent view to hold the SnackProgressBar.
 * If possible, it should be the root view of the activity and can be any type of layout.
 * The constructor will loop and crawl up the view hierarchy to find a suitable parent view.
 * @param lifecycleOwner The activity / fragment that this SnackProgressBarManager is attached to.
 * This provides a quick way to automatically [dismissAll] and remove [onDisplayListener] during onDestroy of
 * the activity / fragment to prevent memory leak.
 */
@Keep
class SnackProgressBarManager(providedView: View, lifecycleOwner: LifecycleOwner? = null) : LifecycleObserver {

    /* definition */
    @Retention(AnnotationRetention.SOURCE)
    @IntRange(from = 1)
    annotation class OneUp

    // init
    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    /* companion object */
    // @Keep annotation is required to prevent proguard from removing the companion object.
    // See https://stackoverflow.com/a/53863656/3584439.
    @Keep
    companion object {

        @Retention(AnnotationRetention.SOURCE)
        @IntDef(LENGTH_LONG, LENGTH_SHORT, LENGTH_INDEFINITE)
        @IntRange(from = 1)
        annotation class ShowDuration

        /**
         * Show the SnackProgressBar indefinitely.
         * Note that this will be changed to LENGTH_SHORT and dismissed
         * if there is another SnackProgressBar in queue before and after.
         */
        const val LENGTH_INDEFINITE = BaseTransientBottomBar.LENGTH_INDEFINITE
        /**
         * Show the SnackProgressBar for a short period of time.
         */
        const val LENGTH_SHORT = BaseTransientBottomBar.LENGTH_SHORT
        /**
         * Show the SnackProgressBar for a long period of time.
         */
        const val LENGTH_LONG = BaseTransientBottomBar.LENGTH_LONG
        /**
         * Default SnackProgressBar background color as per Material Design.
         */
        @JvmField
        val BACKGROUND_COLOR_DEFAULT = R.color.background
        /**
         * Default message text color as per Material Design.
         */
        @JvmField
        val MESSAGE_COLOR_DEFAULT = R.color.textWhitePrimary
        /**
         * Default action text color as per Material Design i.e. R.color.colorAccent.
         */
        @JvmField
        val ACTION_COLOR_DEFAULT = R.color.colorAccent
        /**
         * Default progressBar color as per Material Design i.e. R.color.colorAccent.
         */
        @JvmField
        val PROGRESSBAR_COLOR_DEFAULT = R.color.colorAccent
        /**
         * Default progressText color as per Material Design.
         */
        @JvmField
        val PROGRESSTEXT_COLOR_DEFAULT = R.color.textWhitePrimary
        /**
         * Default overlayLayout color i.e. android.R.color.white.
         */
        @JvmField
        val OVERLAY_COLOR_DEFAULT = android.R.color.white
        /**
         * Default displayId
         */
        private const val NO_DISPLAY_ID = -1

    }

    /* parameters */
    private val storedBars = HashMap<Int, SnackProgressBar>()
    private val queueBars = ArrayList<SnackProgressBar>()
    private val queueOnDisplayIds = ArrayList<Int>()
    private val queueDurations = ArrayList<Int>()
    private var currentQueue = 0
    private var currentCore: SnackProgressBarCore? = null
    private var parentView = WeakReference(findSuitableParent(providedView))
    private var viewsToMove: Array<WeakReference<View>>? = null
    private var backgroundColor = BACKGROUND_COLOR_DEFAULT
    private var messageTextColor = MESSAGE_COLOR_DEFAULT
    private var actionTextColor = ACTION_COLOR_DEFAULT
    private var progressBarColor = PROGRESSBAR_COLOR_DEFAULT
    private var progressTextColor = PROGRESSTEXT_COLOR_DEFAULT
    private var overlayColor = OVERLAY_COLOR_DEFAULT
    private var textSize = parentView.get()?.resources?.getDimension(R.dimen.text_body) ?: 0f
    private var maxLines = 2
    private var overlayLayoutAlpha = 0.8f
    private var onDisplayListener: OnDisplayListener? = null

    /**
     * Interface definition for a callback to be invoked when the SnackProgressBar is shown or dismissed.
     */
    interface OnDisplayListener {
        /**
         * Called when the SnackProgressBar view is inflated.
         *
         * @param snackProgressBarLayout The view of the SnackProgressBar.
         * @param overlayLayout The view of the OverlayLayout.
         * @param snackProgressBar The SnackProgressBar attached.
         * @param onDisplayId OnDisplayId assigned to the SnackProgressBar which is shown.
         */
        fun onLayoutInflated(
            snackProgressBarLayout: SnackProgressBarLayout,
            overlayLayout: FrameLayout,
            snackProgressBar: SnackProgressBar,
            onDisplayId: Int
        ) {
        }

        /**
         * Called when the SnackProgressBar is shown.
         *
         * @param snackProgressBar The SnackProgressBar attached.
         * @param onDisplayId OnDisplayId assigned to the SnackProgressBar which is shown.
         */
        fun onShown(snackProgressBar: SnackProgressBar, onDisplayId: Int) {}

        /**
         * Called when the SnackProgressBar is dismissed.
         *
         * @param snackProgressBar The SnackProgressBar attached.
         * @param onDisplayId OnDisplayId assigned to the SnackProgressBar which is shown.
         */
        fun onDismissed(snackProgressBar: SnackProgressBar, onDisplayId: Int) {}

    }

    /**
     * Registers a callback to be invoked when the SnackProgressBar is shown or dismissed.
     *
     * @param onDisplayListener The callback that will run. This value may be null.
     */
    fun setOnDisplayListener(onDisplayListener: OnDisplayListener?): SnackProgressBarManager {
        this.onDisplayListener = onDisplayListener
        return this
    }

    /**
     * Stores a SnackProgressBar into SnackProgressBarManager to perform further action.
     * The SnackProgressBar is uniquely identified by the storeId and will be overwritten
     * by another SnackProgressBar with the same storeId.
     *
     * @param snackProgressBar SnackProgressBar to be added.
     * @param storeId          StoreId of the SnackProgressBar to be added.
     * @see SnackProgressBar
     *
     * @see .getSnackProgressBar
     */
    fun put(snackProgressBar: SnackProgressBar, @OneUp storeId: Int) {
        storedBars[storeId] = snackProgressBar
    }

    /**
     * Retrieves the SnackProgressBar that was previously added into SnackProgressBarManager.
     *
     * @param storeId StoreId of the SnackProgressBar stored in SnackProgressBarManager.
     * @return SnackProgressBar of the storeId. Can be null.
     * @see .put
     */
    fun getSnackProgressBar(@OneUp storeId: Int): SnackProgressBar? {
        return storedBars[storeId]
    }

    /**
     * Retrieves the SnackProgressBar that is currently or was showing.
     *
     * @return SnackProgressBar that is currently or was showing. Return null if nothing was ever shown.
     */
    fun getLastShown(): SnackProgressBar? {
        return currentCore?.getSnackProgressBar()
    }

    /**
     * Shows the SnackProgressBar based on its storeId with the specified duration.
     * If another SnackProgressBar is already showing, this SnackProgressBar will be queued
     * and shown accordingly after those queued are dismissed.
     *
     * @param storeId  StoreId of the SnackProgressBar stored in SnackProgressBarManager.
     * @param duration Duration to show the SnackProgressBar of either
     * [LENGTH_SHORT], [LENGTH_LONG], [LENGTH_INDEFINITE] or any positive millis.
     * @see .put
     */
    fun show(@OneUp storeId: Int, @ShowDuration duration: Int) {
        val snackProgressBar = storedBars[storeId]
        snackProgressBar?.run { addToQueue(this, duration, NO_DISPLAY_ID) }
            ?: throw IllegalArgumentException("SnackProgressBar with storeId = $storeId is not found!")
    }

    /**
     * Shows the SnackProgressBar based on its storeId with the specified duration.
     * If another SnackProgressBar is already showing, this SnackProgressBar will be queued
     * and shown accordingly after those queued are dismissed.
     *
     * @param storeId     StoreId of the SnackProgressBar stored in SnackProgressBarManager.
     * @param duration    Duration to show the SnackProgressBar of either
     * [LENGTH_SHORT], [LENGTH_LONG], [LENGTH_INDEFINITE] or any positive millis.
     * @param onDisplayId OnDisplayId attached to the SnackProgressBar when implementing the OnDisplayListener.
     * @see .put
     */
    fun show(@OneUp storeId: Int, @ShowDuration duration: Int, @OneUp onDisplayId: Int) {
        val snackProgressBar = storedBars[storeId]
        snackProgressBar?.run { show(this, duration, onDisplayId) }
            ?: throw IllegalArgumentException("SnackProgressBar with storeId = $storeId is not found!")
    }

    /**
     * Shows the SnackProgressBar with the specified duration.
     * If another SnackProgressBar is already showing, this SnackProgressBar will be queued
     * and shown accordingly after those queued are dismissed.
     *
     * @param snackProgressBar SnackProgressBar to be shown.
     * @param duration         Duration to show the SnackProgressBar of either
     * [LENGTH_SHORT], [LENGTH_LONG], [LENGTH_INDEFINITE] or any positive millis.
     */
    fun show(snackProgressBar: SnackProgressBar, @ShowDuration duration: Int) {
        addToQueue(snackProgressBar, duration, NO_DISPLAY_ID)
    }

    /**
     * Shows the SnackProgressBar with the specified duration.
     * If another SnackProgressBar is already showing, this SnackProgressBar will be queued
     * and shown accordingly after those queued are dismissed.
     *
     * @param snackProgressBar SnackProgressBar to be shown.
     * @param duration         Duration to show the SnackProgressBar of either
     * [LENGTH_SHORT], [LENGTH_LONG], [LENGTH_INDEFINITE] or any positive millis.
     * @param onDisplayId      OnDisplayId attached to the SnackProgressBar when implementing the OnDisplayListener.
     */
    fun show(snackProgressBar: SnackProgressBar, @ShowDuration duration: Int, @OneUp onDisplayId: Int) {
        addToQueue(snackProgressBar, duration, onDisplayId)
    }

    /**
     * Updates the currently showing SnackProgressBar without dismissing it to the SnackProgressBar
     * with the corresponding storeId i.e. updating without animation.
     * Note: This does not change the queue.
     *
     * @param storeId StoreId of the SnackProgressBar stored in SnackProgressBarManager.
     */
    fun updateTo(@OneUp storeId: Int) {
        val snackProgressBar = storedBars[storeId]
        snackProgressBar?.run { updateTo(this) }
            ?: throw IllegalArgumentException("SnackProgressBar with storeId = $storeId is not found!")
    }

    /**
     * Updates the currently showing SnackProgressBar without dismissing it to the new SnackProgressBar
     * i.e. updating without animation.
     * Note: This does not change the queue.
     *
     * @param snackProgressBar SnackProgressBar to be updated to.
     */
    fun updateTo(snackProgressBar: SnackProgressBar) {
        currentCore?.updateTo(snackProgressBar)
    }

    /**
     * Dismisses the currently showing SnackProgressBar and show the next in queue.
     */
    fun dismiss() {
        currentCore?.dismiss()
        nextQueue()
    }

    /**
     * Dismisses all the SnackProgressBar that is in queue.
     * The currently shown SnackProgressBar will also be dismissed.
     */
    fun dismissAll() {
        // reset queue before dismissing currentCore to prevent next in queue from showing
        resetQueue()
        currentCore?.dismiss()
    }

    /**
     * Call [dismissAll] and remove [onDisplayListener] to prevent memory leak.
     * This is called automatically during [Lifecycle.Event.ON_DESTROY] of activity / fragment
     * if a [LifecycleOwner] is provided.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disable() {
        onDisplayListener = null
        dismissAll()
    }

    /**
     * Passes the view (e.g. FloatingActionButton) to move up or down as SnackProgressBar is shown or dismissed.
     *
     * This is not required for FloatingActionButton if a CoordinatorLayout is provided, but will be required for
     * other layout types. This can be used to animate any view besides FloatingActionButton as well.
     *
     * @param viewToMove View to be animated along with the SnackProgressBar.
     */
    fun setViewToMove(viewToMove: View): SnackProgressBarManager {
        val viewsToMove = arrayOf(viewToMove)
        setViewsToMove(viewsToMove)
        return this
    }

    /**
     * Passes the views (e.g. FloatingActionButton) to move up or down as SnackProgressBar is shown or dismissed.
     *
     * This is not required for FloatingActionButton if a CoordinatorLayout is provided, but will be required for
     * other layout types. This can be used to animate any views besides FloatingActionButton as well.
     *
     * @param viewsToMove Views to be animated along with the SnackProgressBar.
     */
    fun setViewsToMove(viewsToMove: Array<View>): SnackProgressBarManager {
        this.viewsToMove = viewsToMove.map { WeakReference(it) }.toTypedArray()
        return this
    }

    /**
     * Sets the text size of SnackProgressBar.
     *
     * @param sp Font size in scale-independent pixels.
     */
    fun setTextSize(sp: Float): SnackProgressBarManager {
        val parentView = parentView.get()
        if (parentView != null) {
            textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, parentView.resources.displayMetrics)
            currentCore?.setTextSize(textSize)
        }
        return this
    }

    /**
     * Sets the max lines for message.
     *
     * @param maxLines Number of lines.
     */
    fun setMessageMaxLines(maxLines: Int): SnackProgressBarManager {
        this.maxLines = maxLines
        currentCore?.setMaxLines(maxLines)
        return this
    }

    /**
     * Sets the transparency of the overlayLayout which blocks user input.
     *
     * @param alpha Alpha between 0f to 1f. Default = 0.8f.
     */
    fun setOverlayLayoutAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float): SnackProgressBarManager {
        overlayLayoutAlpha = alpha
        currentCore?.setOverlayLayout(overlayColor, overlayLayoutAlpha)
        return this
    }

    /**
     * Sets the overlayLayout color.
     *
     * @param colorId R.color id.
     * @see OVERLAY_COLOR_DEFAULT
     */
    fun setOverlayLayoutColor(@ColorRes colorId: Int): SnackProgressBarManager {
        overlayColor = colorId
        // Update the UI now if applicable
        currentCore?.setOverlayLayout(overlayColor, overlayLayoutAlpha)
        return this
    }

    /**
     * Sets the SnackProgressBar background color.
     *
     * @param colorId R.color id.
     * @see BACKGROUND_COLOR_DEFAULT
     */
    fun setBackgroundColor(@ColorRes colorId: Int): SnackProgressBarManager {
        backgroundColor = colorId
        // Update the UI now if applicable
        currentCore?.setColor(backgroundColor, messageTextColor, actionTextColor, progressBarColor, progressTextColor)
        return this
    }

    /**
     * Sets the message text color.
     *
     * @param colorId R.color id.
     * @see MESSAGE_COLOR_DEFAULT
     */
    fun setMessageTextColor(@ColorRes colorId: Int): SnackProgressBarManager {
        messageTextColor = colorId
        // Update the UI now if applicable
        currentCore?.setColor(backgroundColor, messageTextColor, actionTextColor, progressBarColor, progressTextColor)
        return this
    }

    /**
     * Sets the action text color.
     *
     * @param colorId R.color id.
     * @see ACTION_COLOR_DEFAULT
     */
    fun setActionTextColor(@ColorRes colorId: Int): SnackProgressBarManager {
        actionTextColor = colorId
        // Update the UI now if applicable
        currentCore?.setColor(backgroundColor, messageTextColor, actionTextColor, progressBarColor, progressTextColor)
        return this
    }

    /**
     * Sets the ProgressBar color.
     *
     * @param colorId R.color id.
     * @see PROGRESSBAR_COLOR_DEFAULT
     */
    fun setProgressBarColor(@ColorRes colorId: Int): SnackProgressBarManager {
        progressBarColor = colorId
        // Update the UI now if applicable
        currentCore?.setColor(backgroundColor, messageTextColor, actionTextColor, progressBarColor, progressTextColor)
        return this
    }

    /**
     * Sets the ProgressText color.
     *
     * @param colorId R.color id.
     * @see PROGRESSTEXT_COLOR_DEFAULT
     */
    fun setProgressTextColor(@ColorRes colorId: Int): SnackProgressBarManager {
        progressTextColor = colorId
        // update the UI now if applicable
        currentCore?.setColor(backgroundColor, messageTextColor, actionTextColor, progressBarColor, progressTextColor)
        return this
    }

    /**
     * Sets the progress for SnackProgressBar that is currently showing.
     * It will also update the progress in % if it is shown.
     *
     * @param progress Progress of the ProgressBar.
     */
    fun setProgress(@IntRange(from = 0) progress: Int): SnackProgressBarManager {
        currentCore?.setProgress(progress)
        return this
    }

    /**
     * Adds the SnackProgressBar to queue.
     *
     * @param snackProgressBar SnackProgressBar to be added to queue.
     * @param duration         Duration to show the SnackProgressBar of either
     * [LENGTH_SHORT], [LENGTH_LONG], [LENGTH_INDEFINITE] or any positive millis.
     * @param onDisplayId      OnDisplayId attached to the SnackProgressBar when implementing the OnDisplayListener.
     */
    private fun addToQueue(snackProgressBar: SnackProgressBar, duration: Int, onDisplayId: Int) {
        // Get the queue number as the last of queue list
        val queue = queueBars.size
        // Create a new object
        val queueBar = SnackProgressBar(
            snackProgressBar.getType(),
            snackProgressBar.getMessage(),
            snackProgressBar.getAction(),
            snackProgressBar.getOnActionClickListener(),
            snackProgressBar.getIconBitmap(),
            snackProgressBar.getIconResource(),
            snackProgressBar.getProgressMax(),
            snackProgressBar.isAllowUserInput(),
            snackProgressBar.isSwipeToDismiss(),
            snackProgressBar.isIndeterminate(),
            snackProgressBar.isShowProgressPercentage(),
            snackProgressBar.getBundle()
        )
        // Put in queue
        queueBars.add(queueBar)
        queueOnDisplayIds.add(onDisplayId)
        queueDurations.add(duration)
        // Play queue if first item
        if (queue == 0) {
            playQueue(queue)
        }
    }

    /**
     * Plays the queue.
     *
     * @param queue Queue number of SnackProgressBar.
     */
    private fun playQueue(queue: Int) {
        // Check if queue number is bounded
        if (queue < queueBars.size) {
            val parentView = parentView.get()
            if (parentView != null) {
                // Get the variables
                currentQueue = queue
                val snackProgressBar = queueBars[queue]
                val onDisplayId = queueOnDisplayIds[queue]
                var duration = queueDurations[queue]
                // Change duration to LENGTH_SHORT if is not last item
                if (duration == LENGTH_INDEFINITE) {
                    if (queue < queueBars.size - 1) {
                        duration = LENGTH_SHORT
                    }
                }
                // Create SnackProgressBarCore
                val finalDuration = duration
                val finalViewsToMove = mutableListOf<View>()
                viewsToMove?.forEach {
                    val view = it.get()
                    if (view != null) {
                        finalViewsToMove.add(view)
                    }
                }
                val snackProgressBarCore =
                    SnackProgressBarCore.make(parentView, snackProgressBar, duration, finalViewsToMove.toTypedArray())
                        .setOverlayLayout(overlayColor, overlayLayoutAlpha)
                        .setColor(
                            backgroundColor,
                            messageTextColor,
                            actionTextColor,
                            progressBarColor,
                            progressTextColor
                        )
                        .setTextSize(textSize)
                        .setMaxLines(maxLines)
                        .addCallback(object : BaseTransientBottomBar.BaseCallback<SnackProgressBarCore>() {
                            override fun onShown(snackProgressBarCore: SnackProgressBarCore) {
                                // set current
                                currentCore = snackProgressBarCore
                                // callback onDisplayListener
                                onDisplayListener?.onShown(snackProgressBarCore.getSnackProgressBar(), onDisplayId)
                            }

                            override fun onDismissed(snackProgressBarCore: SnackProgressBarCore, event: Int) {
                                // remove overlayLayout
                                snackProgressBarCore.removeOverlayLayout()
                                // reset current
                                currentCore = null
                                // callback onDisplayListener
                                onDisplayListener?.onDismissed(snackProgressBarCore.getSnackProgressBar(), onDisplayId)
                                // play next if this item is dismissed automatically later
                                if (finalDuration != LENGTH_INDEFINITE) {
                                    nextQueue()
                                }
                            }
                        })
                // Reset queue if this is last item in queue with LENGTH_INDEFINITE
                if (duration == LENGTH_INDEFINITE) {
                    resetQueue()
                }
                // Allow users to manipulate the view
                onDisplayListener?.onLayoutInflated(
                    snackProgressBarCore.snackProgressBarLayout,
                    snackProgressBarCore.overlayLayout,
                    snackProgressBarCore.getSnackProgressBar(),
                    onDisplayId
                )
                snackProgressBarCore.show()
            } else {
                // ParentView has been GCed, don't show anymore
                dismissAll()
            }
        } else {
            // Else, queue is done
            resetQueue()
        }
    }

    /**
     * Play the next item in queue.
     */
    private fun nextQueue() {
        playQueue(currentQueue + 1)
    }

    /**
     * Reset queue.
     */
    private fun resetQueue() {
        currentQueue = 0
        queueBars.clear()
        queueOnDisplayIds.clear()
        queueDurations.clear()
    }

    /**
     * Loop and crawl up the view hierarchy to find a suitable parent view.
     *
     * @param providedView View provided to search for parent view to hold the SnackProgressBar.
     * If possible, it should be the root view of the activity and can be any type of layout.
     * @return Suitable parent to hold the SnackProgressBar using the view provided.
     */
    private fun findSuitableParent(providedView: View): ViewGroup {
        var view: View? = providedView
        var fallback: ViewGroup? = null
        do {
            if (view is CoordinatorLayout) {
                // Use the CoordinatorLayout found
                return view
            } else if (view is FrameLayout) {
                if (view.id == android.R.id.content) {
                    // Use the content view since CoordinatorLayout not found
                    return view
                } else {
                    // Non-content view, but use it as fallback
                    fallback = view
                }
            }
            if (view != null) {
                // Loop and crawl up the view hierarchy and try to find a parent
                val parent = view.parent
                view = if (parent is View) parent else null
            }
        } while (view != null)
        // Use fallback since CoordinatorLayout and other alternative not found
        fallback?.run { return this }
            ?: throw IllegalArgumentException("No suitable parent found from the given view. " + "Please provide a valid view.")
    }

}