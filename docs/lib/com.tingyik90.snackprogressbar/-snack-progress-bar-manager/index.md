[lib](../../index.md) / [com.tingyik90.snackprogressbar](../index.md) / [SnackProgressBarManager](./index.md)

# SnackProgressBarManager

`class SnackProgressBarManager : LifecycleObserver`

Manager class handling all the SnackProgressBars added.

This class queues the SnackProgressBars to be shown.
It will dismiss the SnackProgressBar according to its desired duration before showing the next in queue.

In the constructor, provide a view to search for a suitable parent view to hold the SnackProgressBar.
If possible, it should be the root view of the activity and can be any type of layout.
If a CoordinatorLayout is provided, the FloatingActionButton will animate with the SnackProgressBar.
Else, [setViewsToMove](set-views-to-move.md) needs to be called to select the views to be animated.
Note that [setViewsToMove](set-views-to-move.md) can still be used for CoordinatorLayout to move other views.

Swipe to dismiss behaviour can be set via [SnackProgressBar.setSwipeToDismiss](../-snack-progress-bar/set-swipe-to-dismiss.md).
This is provided via [BaseTransientBottomBar](#) for CoordinatorLayout, but provided via
[SnackProgressBarLayout](../-snack-progress-bar-layout/index.md) for other layout types.

Starting v6.0, you can provide a [LifecycleOwner](#) of an activity / fragment.
This provides a quick way to automatically [dismissAll](dismiss-all.md) and remove [onDisplayListener](#) during onDestroy of
the activity / fragment to prevent memory leak.

### Types

| Name | Summary |
|---|---|
| [OnDisplayListener](-on-display-listener/index.md) | Interface definition for a callback to be invoked when the SnackProgressBar is shown or dismissed.`interface OnDisplayListener` |

### Annotations

| Name | Summary |
|---|---|
| [OneUp](-one-up/index.md) | `annotation class OneUp` |
| [ShowDuration](-show-duration/index.md) | `annotation class ShowDuration` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Create an instance of SnackProgressBarManager.`SnackProgressBarManager(providedView: `[`View`](https://developer.android.com/reference/android/view/View.html)`, lifecycleOwner: LifecycleOwner? = null)` |

### Functions

| Name | Summary |
|---|---|
| [disable](disable.md) | Call [dismissAll](dismiss-all.md) and remove [onDisplayListener](#) to prevent memory leak. This is called automatically during [Lifecycle.Event.ON_DESTROY](#) of activity / fragment if a [LifecycleOwner](#) is provided.`fun disable(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [dismiss](dismiss.md) | Dismisses the currently showing SnackProgressBar and show the next in queue.`fun dismiss(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [dismissAll](dismiss-all.md) | Dismisses all the SnackProgressBar that is in queue. The currently shown SnackProgressBar will also be dismissed.`fun dismissAll(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getLastShown](get-last-shown.md) | Retrieves the SnackProgressBar that is currently or was showing.`fun getLastShown(): `[`SnackProgressBar`](../-snack-progress-bar/index.md)`?` |
| [getSnackProgressBar](get-snack-progress-bar.md) | Retrieves the SnackProgressBar that was previously added into SnackProgressBarManager.`fun getSnackProgressBar(storeId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBar`](../-snack-progress-bar/index.md)`?` |
| [put](put.md) | Stores a SnackProgressBar into SnackProgressBarManager to perform further action. The SnackProgressBar is uniquely identified by the storeId and will be overwritten by another SnackProgressBar with the same storeId.`fun put(snackProgressBar: `[`SnackProgressBar`](../-snack-progress-bar/index.md)`, storeId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setActionTextColor](set-action-text-color.md) | Sets the action text color.`fun setActionTextColor(colorId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBarManager`](./index.md) |
| [setBackgroundColor](set-background-color.md) | Sets the SnackProgressBar background color.`fun setBackgroundColor(colorId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBarManager`](./index.md) |
| [setMessageMaxLines](set-message-max-lines.md) | Sets the max lines for message.`fun setMessageMaxLines(maxLines: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBarManager`](./index.md) |
| [setMessageTextColor](set-message-text-color.md) | Sets the message text color.`fun setMessageTextColor(colorId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBarManager`](./index.md) |
| [setOnDisplayListener](set-on-display-listener.md) | Registers a callback to be invoked when the SnackProgressBar is shown or dismissed.`fun setOnDisplayListener(onDisplayListener: OnDisplayListener?): `[`SnackProgressBarManager`](./index.md) |
| [setOverlayLayoutAlpha](set-overlay-layout-alpha.md) | Sets the transparency of the overlayLayout which blocks user input.`fun setOverlayLayoutAlpha(alpha: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`): `[`SnackProgressBarManager`](./index.md) |
| [setOverlayLayoutColor](set-overlay-layout-color.md) | Sets the overlayLayout color.`fun setOverlayLayoutColor(colorId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBarManager`](./index.md) |
| [setProgress](set-progress.md) | Sets the progress for SnackProgressBar that is currently showing. It will also update the progress in % if it is shown.`fun setProgress(progress: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBarManager`](./index.md) |
| [setProgressBarColor](set-progress-bar-color.md) | Sets the ProgressBar color.`fun setProgressBarColor(colorId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBarManager`](./index.md) |
| [setProgressTextColor](set-progress-text-color.md) | Sets the ProgressText color.`fun setProgressTextColor(colorId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBarManager`](./index.md) |
| [setTextSize](set-text-size.md) | Sets the text size of SnackProgressBar.`fun setTextSize(sp: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`): `[`SnackProgressBarManager`](./index.md) |
| [setViewsToMove](set-views-to-move.md) | Passes the views (e.g. FloatingActionButton) to move up or down as SnackProgressBar is shown or dismissed.`fun setViewsToMove(viewsToMove: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`View`](https://developer.android.com/reference/android/view/View.html)`>): `[`SnackProgressBarManager`](./index.md) |
| [setViewToMove](set-view-to-move.md) | Passes the view (e.g. FloatingActionButton) to move up or down as SnackProgressBar is shown or dismissed.`fun setViewToMove(viewToMove: `[`View`](https://developer.android.com/reference/android/view/View.html)`): `[`SnackProgressBarManager`](./index.md) |
| [show](show.md) | Shows the SnackProgressBar based on its storeId with the specified duration. If another SnackProgressBar is already showing, this SnackProgressBar will be queued and shown accordingly after those queued are dismissed.`fun show(storeId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, duration: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`fun show(storeId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, duration: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, onDisplayId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Shows the SnackProgressBar with the specified duration. If another SnackProgressBar is already showing, this SnackProgressBar will be queued and shown accordingly after those queued are dismissed.`fun show(snackProgressBar: `[`SnackProgressBar`](../-snack-progress-bar/index.md)`, duration: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`fun show(snackProgressBar: `[`SnackProgressBar`](../-snack-progress-bar/index.md)`, duration: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, onDisplayId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [updateTo](update-to.md) | Updates the currently showing SnackProgressBar without dismissing it to the SnackProgressBar with the corresponding storeId i.e. updating without animation. Note: This does not change the queue.`fun updateTo(storeId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Updates the currently showing SnackProgressBar without dismissing it to the new SnackProgressBar i.e. updating without animation. Note: This does not change the queue.`fun updateTo(snackProgressBar: `[`SnackProgressBar`](../-snack-progress-bar/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [useRoundedCornerBackground](use-rounded-corner-background.md) | Sets whether to use rounded corner background for SnackProgressBar according to new Material Design. Note that the background cannot be changed after being shown.`fun useRoundedCornerBackground(useRoundedCornerBackground: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`SnackProgressBarManager`](./index.md) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [ACTION_COLOR_DEFAULT](-a-c-t-i-o-n_-c-o-l-o-r_-d-e-f-a-u-l-t.md) | Default action text color as per Material Design i.e. R.color.colorAccent.`val ACTION_COLOR_DEFAULT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [BACKGROUND_COLOR_DEFAULT](-b-a-c-k-g-r-o-u-n-d_-c-o-l-o-r_-d-e-f-a-u-l-t.md) | Default SnackProgressBar background color as per Material Design.`val BACKGROUND_COLOR_DEFAULT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [LENGTH_INDEFINITE](-l-e-n-g-t-h_-i-n-d-e-f-i-n-i-t-e.md) | Show the SnackProgressBar indefinitely. Note that this will be changed to LENGTH_SHORT and dismissed if there is another SnackProgressBar in queue before and after.`const val LENGTH_INDEFINITE: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [LENGTH_LONG](-l-e-n-g-t-h_-l-o-n-g.md) | Show the SnackProgressBar for a long period of time.`const val LENGTH_LONG: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [LENGTH_SHORT](-l-e-n-g-t-h_-s-h-o-r-t.md) | Show the SnackProgressBar for a short period of time.`const val LENGTH_SHORT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [MESSAGE_COLOR_DEFAULT](-m-e-s-s-a-g-e_-c-o-l-o-r_-d-e-f-a-u-l-t.md) | Default message text color as per Material Design.`val MESSAGE_COLOR_DEFAULT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [OVERLAY_COLOR_DEFAULT](-o-v-e-r-l-a-y_-c-o-l-o-r_-d-e-f-a-u-l-t.md) | Default overlayLayout color i.e. android.R.color.white.`val OVERLAY_COLOR_DEFAULT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [PROGRESSBAR_COLOR_DEFAULT](-p-r-o-g-r-e-s-s-b-a-r_-c-o-l-o-r_-d-e-f-a-u-l-t.md) | Default progressBar color as per Material Design i.e. R.color.colorAccent.`val PROGRESSBAR_COLOR_DEFAULT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [PROGRESSTEXT_COLOR_DEFAULT](-p-r-o-g-r-e-s-s-t-e-x-t_-c-o-l-o-r_-d-e-f-a-u-l-t.md) | Default progressText color as per Material Design.`val PROGRESSTEXT_COLOR_DEFAULT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
