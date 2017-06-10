package com.tingyik90.snackprogressbar;

import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Manager class handling all the SnackProgressBars added.
 * <p>
 * This class queues the SnackProgressBars to be shown.
 * It will dismiss the SnackProgressBar according to its desired duration before showing the next in queue.
 * </p>
 */
public class SnackProgressBarManager {

    @IntDef({LENGTH_LONG, LENGTH_SHORT, LENGTH_INDEFINITE})
    @IntRange(from = 1)
    @Retention(SOURCE)
    @interface ShowDuration {}

    /**
     * Show the SnackProgressBar indefinitely.
     * Note that this will be changed to LENGTH_SHORT and dismissed
     * if there is another SnackProgressBar in queue before and after.
     */
    public static final int LENGTH_INDEFINITE = BaseTransientBottomBar.LENGTH_INDEFINITE;
    /**
     * Show the SnackProgressBar for a short period of time.
     */
    public static final int LENGTH_SHORT = BaseTransientBottomBar.LENGTH_SHORT;
    /**
     * Show the SnackProgressBar for a long period of time.
     */
    public static final int LENGTH_LONG = BaseTransientBottomBar.LENGTH_LONG;

    /**
     * Default SnackProgressBar background color as per Material Design.
     */
    public static final int BACKGROUND_COLOR_DEFAULT = R.color.background;
    /**
     * Default message text color as per Material Design.
     */
    public static final int MESSAGE_COLOR_DEFAULT = R.color.textWhitePrimary;
    /**
     * Default action text color as per Material Design i.e. R.color.colorAccent.
     */
    public static final int ACTION_COLOR_DEFAULT = R.color.colorAccent;
    /**
     * Default progressBar color as per Material Design i.e. R.color.colorAccent.
     */
    public static final int PROGRESSBAR_COLOR_DEFAULT = R.color.colorAccent;

    /**
     * Interface definition for a callback to be invoked when the SnackProgressBar is shown or dismissed.
     */
    public interface OnDisplayListener {
        /**
         * Called when the SnackProgressBar is shown.
         *
         * @param onDisplayId OnDisplayId assigned to the SnackProgressBar which is shown.
         */
        void onShown(int onDisplayId);

        /**
         * Called when the SnackProgressBar is dismissed.
         *
         * @param onDisplayId OnDisplayId assigned to the SnackProgressBar which is shown.
         */
        void onDismissed(int onDisplayId);
    }

    private OnDisplayListener onDisplayListener;
    private static final int ON_DISPLAY_ID_DEFAULT = -1;

    /**
     * Registers a callback to be invoked when the SnackProgressBar is shown or dismissed.
     *
     * @param onDisplayListener The callback that will run. This value may be null.
     */
    public SnackProgressBarManager setOnDisplayListener(OnDisplayListener onDisplayListener) {
        this.onDisplayListener = onDisplayListener;
        return this;
    }

    /* variables */
    // queue related
    private HashMap<Integer, SnackProgressBar> storedBars = new HashMap<>();
    private ArrayList<SnackProgressBar> queueBars = new ArrayList<>();
    private ArrayList<Integer> queueOnDisplayIds = new ArrayList<>();
    private ArrayList<Integer> queueDurations = new ArrayList<>();
    private int currentQueue = 0;
    private SnackProgressBar currentBar = null;
    private SnackProgressBarCore currentCore = null;
    // views related
    private ViewGroup parentView;
    private View overlayLayout;
    private View[] viewsToMove;
    private int backgroundColor = BACKGROUND_COLOR_DEFAULT;
    private int messageTextColor = MESSAGE_COLOR_DEFAULT;
    private int actionTextColor = ACTION_COLOR_DEFAULT;
    private int progressBarColor = PROGRESSBAR_COLOR_DEFAULT;
    private float overlayLayoutAlpha = 0.8f;

    /**
     * Constructor. If possible, the root view of the activity should be provided and can be any type of layout.
     * The constructor will loop and crawl up the view hierarchy to find a suitable parent view if non-root view is provided.
     * <p>
     * If a CoordinatorLayout is provided, the FloatingActionButton will animate with the SnackProgressBar.
     * Else, {@link #setViewsToMove(View[])} needs to be called to select the views to be animated.
     * Note that {@link #setViewsToMove(View[])} can still be used for CoordinatorLayout to move other views.
     * </p>
     * <p>
     * Swipe to dismiss behaviour can be set via {@link SnackProgressBar#setSwipeToDismiss(boolean)}.
     * This is provided via {@link BaseTransientBottomBar} for CoordinatorLayout, but provided via
     * {@link SnackProgressBarLayout} for other layout types.
     * </p>
     *
     * @param view View to hold the SnackProgressBar.
     */
    public SnackProgressBarManager(View view) {
        parentView = findSuitableParent(view);
        if (parentView == null) {
            throw new IllegalArgumentException("No suitable parent found from the given view. "
                    + "Please provide a valid view.");
        }
    }

    /**
     * Stores a SnackProgressBar into SnackProgressBarManager to perform further action.
     * The SnackProgressBar is uniquely identified by the storeId and will be overwritten
     * by another SnackProgressBar with the same storeId.
     *
     * @param snackProgressBar SnackProgressBar to be added.
     * @param storeId          StoreId of the SnackProgressBar to be added.
     * @see SnackProgressBar
     * @see #getSnackProgressBar(int)
     */
    public void put(@NonNull SnackProgressBar snackProgressBar, @IntRange(from = 1) int storeId) {
        storedBars.put(storeId, snackProgressBar);
    }

    /**
     * Retrieves the SnackProgressBar that was previously added into SnackProgressBarManager.
     *
     * @param storeId StoreId of the SnackProgressBar stored in SnackProgressBarManager.
     * @return SnackProgressBar of the storeId. Can be null.
     * @see #put(SnackProgressBar, int)
     */
    public SnackProgressBar getSnackProgressBar(@IntRange(from = 1) int storeId) {
        return storedBars.get(storeId);
    }

    /**
     * Retrieves the SnackProgressBar that is currently or was showing.
     *
     * @return SnackProgressBar that is currently or was showing. Return null if nothing was ever shown.
     */
    public SnackProgressBar getLastShown() {
        return currentBar;
    }

    /**
     * Shows the SnackProgressBar based on its storeId with the specified duration.
     * If another SnackProgressBar is already showing, this SnackProgressBar will be queued
     * and shown accordingly after those queued are dismissed.
     *
     * @param storeId  StoreId of the SnackProgressBar stored in SnackProgressBarManager.
     * @param duration Duration to show the SnackProgressBar of either
     *                 {@link #LENGTH_SHORT}, {@link #LENGTH_LONG}, {@link #LENGTH_INDEFINITE}
     *                 or any positive millis.
     * @see #put(SnackProgressBar, int)
     */
    public void show(@IntRange(from = 1) int storeId, @ShowDuration int duration) {
        SnackProgressBar snackProgressBar = storedBars.get(storeId);
        if (snackProgressBar != null) {
            addToQueue(snackProgressBar, duration, ON_DISPLAY_ID_DEFAULT);
        } else {
            throw new IllegalArgumentException("SnackProgressBar with storeId = " + storeId + " is not found!");
        }
    }

    /**
     * Shows the SnackProgressBar based on its storeId with the specified duration.
     * If another SnackProgressBar is already showing, this SnackProgressBar will be queued
     * and shown accordingly after those queued are dismissed.
     *
     * @param storeId     StoreId of the SnackProgressBar stored in SnackProgressBarManager.
     * @param duration    Duration to show the SnackProgressBar of either
     *                    {@link #LENGTH_SHORT}, {@link #LENGTH_LONG}, {@link #LENGTH_INDEFINITE}
     *                    or any positive millis.
     * @param onDisplayId OnDisplayId attached to the SnackProgressBar when implementing the OnDisplayListener.
     * @see #put(SnackProgressBar, int)
     */
    public void show(@IntRange(from = 1) int storeId, @ShowDuration int duration, @IntRange(from = 1) int onDisplayId) {
        SnackProgressBar snackProgressBar = storedBars.get(storeId);
        if (snackProgressBar != null) {
            show(snackProgressBar, duration, onDisplayId);
        } else {
            throw new IllegalArgumentException("SnackProgressBar with storeId = " + storeId + " is not found!");
        }
    }

    /**
     * Shows the SnackProgressBar with the specified duration.
     * If another SnackProgressBar is already showing, this SnackProgressBar will be queued
     * and shown accordingly after those queued are dismissed.
     *
     * @param snackProgressBar SnackProgressBar to be shown.
     * @param duration         Duration to show the SnackProgressBar of either
     *                         {@link #LENGTH_SHORT}, {@link #LENGTH_LONG}, {@link #LENGTH_INDEFINITE}
     *                         or any positive millis.
     */
    public void show(@NonNull SnackProgressBar snackProgressBar, @ShowDuration int duration) {
        addToQueue(snackProgressBar, duration, ON_DISPLAY_ID_DEFAULT);
    }

    /**
     * Shows the SnackProgressBar with the specified duration.
     * If another SnackProgressBar is already showing, this SnackProgressBar will be queued
     * and shown accordingly after those queued are dismissed.
     *
     * @param snackProgressBar SnackProgressBar to be shown.
     * @param duration         Duration to show the SnackProgressBar of either
     *                         {@link #LENGTH_SHORT}, {@link #LENGTH_LONG}, {@link #LENGTH_INDEFINITE}
     *                         or any positive millis.
     * @param onDisplayId      OnDisplayId attached to the SnackProgressBar when implementing the OnDisplayListener.
     */
    public void show(@NonNull SnackProgressBar snackProgressBar, @ShowDuration int duration, @IntRange(from = 1) int onDisplayId) {
        addToQueue(snackProgressBar, duration, onDisplayId);
    }

    /**
     * Updates the currently showing SnackProgressBar without dismissing it to the SnackProgressBar
     * with the corresponding storeId i.e. updating without animation.
     * Note: This does not change the queue.
     *
     * @param storeId StoreId of the SnackProgressBar stored in SnackProgressBarManager.
     */
    public void updateTo(@IntRange(from = 1) int storeId) {
        SnackProgressBar snackProgressBar = storedBars.get(storeId);
        if (snackProgressBar != null) {
            updateTo(snackProgressBar);
        } else {
            throw new IllegalArgumentException("SnackProgressBar with storeId = " + storeId + " is not found!");
        }
    }

    /**
     * Updates the currently showing SnackProgressBar without dismissing it to the new SnackProgressBar
     * i.e. updating without animation.
     * Note: This does not change the queue.
     *
     * @param snackProgressBar SnackProgressBar to be updated to.
     */
    public void updateTo(@NonNull SnackProgressBar snackProgressBar) {
        if (currentCore != null) {
            currentCore.updateTo(snackProgressBar);
        }
    }

    /**
     * Dismisses the currently showing SnackProgressBar and show the next in queue.
     */
    public void dismiss() {
        if (currentCore != null) {
            currentCore.dismiss();
            nextQueue();
        }
    }

    /**
     * Dismisses all the SnackProgressBar that is in queue.
     * The currently shown SnackProgressBar will also be dismissed.
     */
    public void dismissAll() {
        // reset queue before dismissing currentCore to prevent next in queue from showing
        resetQueue();
        if (currentCore != null) {
            currentCore.dismiss();
        }
    }

    /**
     * Passes the view (e.g. FloatingActionButton) to move up or down as SnackProgressBar is shown or dismissed.
     * <p>
     * This is not required for FloatingActionButton if a CoordinatorLayout is provided, but will be required for
     * other layout types. This can be used to animate any view besides FloatingActionButton as well.
     * </p>
     *
     * @param viewToMove View to be animated along with the SnackProgressBar.
     */
    public SnackProgressBarManager setViewToMove(View viewToMove) {
        View[] viewsToMove = {viewToMove};
        setViewsToMove(viewsToMove);
        return this;
    }

    /**
     * Passes the views (e.g. FloatingActionButton) to move up or down as SnackProgressBar is shown or dismissed.
     * <p>
     * This is not required for FloatingActionButton if a CoordinatorLayout is provided, but will be required for
     * other layout types. This can be used to animate any views besides FloatingActionButton as well.
     * </p>
     *
     * @param viewsToMove Views to be animated along with the SnackProgressBar.
     */
    public SnackProgressBarManager setViewsToMove(View[] viewsToMove) {
        this.viewsToMove = viewsToMove;
        return this;
    }

    /**
     * Sets the transparency of the OverlayLayout which blocks user input.
     *
     * @param overlayLayoutAlpha Alpha between 0f to 1f. Default = 0.8f.
     */
    public SnackProgressBarManager setOverlayLayoutAlpha(@FloatRange(from = 0f, to = 1f) float overlayLayoutAlpha) {
        this.overlayLayoutAlpha = overlayLayoutAlpha;
        // update the UI now if applicable
        if (overlayLayout != null) {
            overlayLayout.setAlpha(overlayLayoutAlpha);
        }
        return this;
    }

    /**
     * Sets the SnackProgressBar background color.
     *
     * @param colorId R.color id.
     * @see #BACKGROUND_COLOR_DEFAULT
     */
    public SnackProgressBarManager setBackgroundColor(@ColorRes int colorId) {
        backgroundColor = colorId;
        // update the UI now if applicable
        if (currentCore != null) {
            currentCore.setColor(backgroundColor, messageTextColor, actionTextColor, progressBarColor);
        }
        return this;
    }

    /**
     * Sets the message text color.
     *
     * @param colorId R.color id.
     * @see #MESSAGE_COLOR_DEFAULT
     */
    public SnackProgressBarManager setMessageTextColor(@ColorRes int colorId) {
        messageTextColor = colorId;
        // update the UI now if applicable
        if (currentCore != null) {
            currentCore.setColor(backgroundColor, messageTextColor, actionTextColor, progressBarColor);
        }
        return this;
    }

    /**
     * Sets the action text color.
     *
     * @param colorId R.color id.
     * @see #ACTION_COLOR_DEFAULT
     */
    public SnackProgressBarManager setActionTextColor(@ColorRes int colorId) {
        actionTextColor = colorId;
        // update the UI now if applicable
        if (currentCore != null) {
            currentCore.setColor(backgroundColor, messageTextColor, actionTextColor, progressBarColor);
        }
        return this;
    }

    /**
     * Sets the ProgressBar color.
     *
     * @param colorId R.color id.
     * @see #PROGRESSBAR_COLOR_DEFAULT
     */
    public SnackProgressBarManager setProgressBarColor(@ColorRes int colorId) {
        progressBarColor = colorId;
        // update the UI now if applicable
        if (currentCore != null) {
            currentCore.setColor(backgroundColor, messageTextColor, actionTextColor, progressBarColor);
        }
        return this;
    }

    /**
     * Sets the progress for SnackProgressBar of TYPE_DETERMINATE that is currently showing.
     * It will also update the progress in % if it is shown.
     *
     * @param progress Progress of the ProgressBar.
     */
    public SnackProgressBarManager setProgress(@IntRange(from = 0) int progress) {
        if (currentCore != null) {
            currentCore.setProgress(progress);
        }
        return this;
    }

    /**
     * Adds the SnackProgressBar to queue.
     *
     * @param snackProgressBar SnackProgressBar to be added to queue.
     * @param duration         Duration to show the SnackProgressBar of either
     *                         {@link #LENGTH_SHORT}, {@link #LENGTH_LONG}, {@link #LENGTH_INDEFINITE}
     *                         or any positive millis.
     */
    private void addToQueue(SnackProgressBar snackProgressBar, int duration, int callbackId) {
        // get the queue number as the last of queue list
        int queue = queueBars.size();
        // create a new object
        SnackProgressBar queueBar = new SnackProgressBar(
                snackProgressBar.getType(),
                snackProgressBar.getMessage(),
                snackProgressBar.getAction(),
                snackProgressBar.getIconBitmap(),
                snackProgressBar.getIconResource(),
                snackProgressBar.getProgressMax(),
                snackProgressBar.isAllowUserInput(),
                snackProgressBar.isSwipeToDismiss(),
                snackProgressBar.isShowProgressPercentage(),
                snackProgressBar.getOnActionClickListener());
        // put in queue
        queueBars.add(queueBar);
        queueOnDisplayIds.add(callbackId);
        queueDurations.add(duration);
        // play queue if first item
        if (queue == 0) {
            playQueue(queue);
        }
    }

    /**
     * Plays the queue.
     *
     * @param queue Queue number of SnackProgressBar.
     */
    private void playQueue(int queue) {
        // check if queue number is bounded
        if (queue < queueBars.size()) {
            // get the variables
            currentQueue = queue;
            currentBar = queueBars.get(queue);
            final int onDisplayId = queueOnDisplayIds.get(queue);
            int duration = queueDurations.get(queue);
            // change duration to LENGTH_SHORT if is not last item
            if (duration == LENGTH_INDEFINITE) {
                if (queue < queueBars.size() - 1) {
                    duration = LENGTH_SHORT;
                }
            }
            // add overlayLayout if does not allow user input
            if (!currentBar.isAllowUserInput()) {
                LayoutInflater inflater = LayoutInflater.from(parentView.getContext());
                overlayLayout = inflater.inflate(R.layout.overlay, parentView, false);
                overlayLayout.setAlpha(overlayLayoutAlpha);
                parentView.addView(overlayLayout);
            }
            // create SnackProgressBarCore
            currentCore = SnackProgressBarCore.make(parentView, currentBar, duration, viewsToMove);
            currentCore.setColor(backgroundColor, messageTextColor, actionTextColor, progressBarColor);
            final int finalDuration = duration;
            currentCore.addCallback(new BaseTransientBottomBar.BaseCallback<SnackProgressBarCore>() {
                @Override
                public void onShown(SnackProgressBarCore transientBottomBar) {
                    // callback onDisplayListener
                    if (onDisplayListener != null && onDisplayId != ON_DISPLAY_ID_DEFAULT) {
                        onDisplayListener.onShown(onDisplayId);
                    }
                }

                @Override
                public void onDismissed(SnackProgressBarCore transientBottomBar, int event) {
                    // reset currentCore
                    currentCore = null;
                    // remove overlayLayout
                    if (overlayLayout != null) {
                        parentView.removeView(overlayLayout);
                        overlayLayout = null;
                    }
                    // callback onDisplayListener
                    if (onDisplayListener != null && onDisplayId != ON_DISPLAY_ID_DEFAULT) {
                        onDisplayListener.onDismissed(onDisplayId);
                    }
                    // play next if this item is dismissed automatically later
                    if (finalDuration != LENGTH_INDEFINITE) {
                        nextQueue();
                    }
                }
            });
            // reset queue if this is last item in queue with LENGTH_INDEFINITE
            if (duration == LENGTH_INDEFINITE) {
                resetQueue();
            }
            currentCore.show();
        } else {
            // else, queue is done
            resetQueue();
        }
    }

    /**
     * Play the next item in queue.
     */
    private void nextQueue() {
        playQueue(currentQueue + 1);
    }

    /**
     * Reset queue.
     */
    private void resetQueue() {
        currentQueue = 0;
        queueBars.clear();
        queueOnDisplayIds.clear();
        queueDurations.clear();
    }

    /**
     * Loop and crawl up the view hierarchy to find a suitable parent view.
     *
     * @param view View to hold the SnackProgressBar.
     *             If possible, it should be the root view of the activity and can be any type of layout.
     * @return Suitable parent view of the activity.
     */
    private ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;
        do {
            if (view instanceof CoordinatorLayout) {
                // use the CoordinatorLayout found
                return (ViewGroup) view;
            } else if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    // use the content view since CoordinatorLayout not found
                    return (ViewGroup) view;
                } else {
                    // Non-content view, but use it as fallback
                    fallback = (ViewGroup) view;
                }
            }
            if (view != null) {
                // loop and crawl up the view hierarchy and try to find a parent
                ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);
        // use fallback since CoordinatorLayout and other alternative not found
        return fallback;
    }
}
