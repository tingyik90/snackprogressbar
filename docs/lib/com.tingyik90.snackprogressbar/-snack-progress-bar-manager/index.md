[lib](../../index.md) / [com.tingyik90.snackprogressbar](../index.md) / [SnackProgressBarManager](./index.md)

# SnackProgressBarManager

`class SnackProgressBarManager`

Manager class handling all the SnackProgressBars added.

### Types

| Name | Summary |
|---|---|
| [OnDisplayListener](-on-display-listener/index.md) | `interface OnDisplayListener`<br>Interface definition for a callback to be invoked when the SnackProgressBar is shown or dismissed. |

### Annotations

| Name | Summary |
|---|---|
| [OneUp](-one-up/index.md) | `annotation class OneUp` |
| [ShowDuration](-show-duration/index.md) | `annotation class ShowDuration` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SnackProgressBarManager(view: `[`View`](https://developer.android.com/reference/android/view/View.html)`)`<br>If possible, the root view of the activity should be provided and can be any type of layout. The constructor will loop and crawl up the view hierarchy to find a suitable parent view if non-root view is provided. |

### Functions

| Name | Summary |
|---|---|
| [dismiss](dismiss.md) | `fun dismiss(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Dismisses the currently showing SnackProgressBar and show the next in queue. |
| [dismissAll](dismiss-all.md) | `fun dismissAll(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Dismisses all the SnackProgressBar that is in queue. The currently shown SnackProgressBar will also be dismissed. |
| [getLastShown](get-last-shown.md) | `fun getLastShown(): `[`SnackProgressBar`](../-snack-progress-bar/index.md)`?`<br>Retrieves the SnackProgressBar that is currently or was showing. |
| [getSnackProgressBar](get-snack-progress-bar.md) | `fun getSnackProgressBar(storeId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBar`](../-snack-progress-bar/index.md)`?`<br>Retrieves the SnackProgressBar that was previously added into SnackProgressBarManager. |
| [put](put.md) | `fun put(snackProgressBar: `[`SnackProgressBar`](../-snack-progress-bar/index.md)`, storeId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Stores a SnackProgressBar into SnackProgressBarManager to perform further action. The SnackProgressBar is uniquely identified by the storeId and will be overwritten by another SnackProgressBar with the same storeId. |
| [setActionTextColor](set-action-text-color.md) | `fun setActionTextColor(colorId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBarManager`](./index.md)<br>Sets the action text color. |
| [setBackgroundColor](set-background-color.md) | `fun setBackgroundColor(colorId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBarManager`](./index.md)<br>Sets the SnackProgressBar background color. |
| [setMessageMaxLines](set-message-max-lines.md) | `fun setMessageMaxLines(maxLines: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBarManager`](./index.md)<br>Sets the max lines for message. |
| [setMessageTextColor](set-message-text-color.md) | `fun setMessageTextColor(colorId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBarManager`](./index.md)<br>Sets the message text color. |
| [setOnDisplayListener](set-on-display-listener.md) | `fun setOnDisplayListener(onDisplayListener: `[`OnDisplayListener`](-on-display-listener/index.md)`?): `[`SnackProgressBarManager`](./index.md)<br>Registers a callback to be invoked when the SnackProgressBar is shown or dismissed. |
| [setOverlayLayoutAlpha](set-overlay-layout-alpha.md) | `fun setOverlayLayoutAlpha(alpha: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`): `[`SnackProgressBarManager`](./index.md)<br>Sets the transparency of the overlayLayout which blocks user input. |
| [setOverlayLayoutColor](set-overlay-layout-color.md) | `fun setOverlayLayoutColor(colorId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBarManager`](./index.md)<br>Sets the overlayLayout color. |
| [setProgress](set-progress.md) | `fun setProgress(progress: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBarManager`](./index.md)<br>Sets the progress for SnackProgressBar that is currently showing. It will also update the progress in % if it is shown. |
| [setProgressBarColor](set-progress-bar-color.md) | `fun setProgressBarColor(colorId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBarManager`](./index.md)<br>Sets the ProgressBar color. |
| [setProgressTextColor](set-progress-text-color.md) | `fun setProgressTextColor(colorId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBarManager`](./index.md)<br>Sets the ProgressText color. |
| [setTextSize](set-text-size.md) | `fun setTextSize(sp: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`): `[`SnackProgressBarManager`](./index.md)<br>Sets the text size of SnackProgressBar. |
| [setViewToMove](set-view-to-move.md) | `fun setViewToMove(viewToMove: `[`View`](https://developer.android.com/reference/android/view/View.html)`): `[`SnackProgressBarManager`](./index.md)<br>Passes the view (e.g. FloatingActionButton) to move up or down as SnackProgressBar is shown or dismissed. |
| [setViewsToMove](set-views-to-move.md) | `fun setViewsToMove(viewsToMove: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`View`](https://developer.android.com/reference/android/view/View.html)`>): `[`SnackProgressBarManager`](./index.md)<br>Passes the views (e.g. FloatingActionButton) to move up or down as SnackProgressBar is shown or dismissed. |
| [show](show.md) | `fun show(storeId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, duration: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`fun show(storeId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, duration: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, onDisplayId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Shows the SnackProgressBar based on its storeId with the specified duration. If another SnackProgressBar is already showing, this SnackProgressBar will be queued and shown accordingly after those queued are dismissed.`fun show(snackProgressBar: `[`SnackProgressBar`](../-snack-progress-bar/index.md)`, duration: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`fun show(snackProgressBar: `[`SnackProgressBar`](../-snack-progress-bar/index.md)`, duration: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, onDisplayId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Shows the SnackProgressBar with the specified duration. If another SnackProgressBar is already showing, this SnackProgressBar will be queued and shown accordingly after those queued are dismissed. |
| [updateTo](update-to.md) | `fun updateTo(storeId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Updates the currently showing SnackProgressBar without dismissing it to the SnackProgressBar with the corresponding storeId i.e. updating without animation. Note: This does not change the queue.`fun updateTo(snackProgressBar: `[`SnackProgressBar`](../-snack-progress-bar/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Updates the currently showing SnackProgressBar without dismissing it to the new SnackProgressBar i.e. updating without animation. Note: This does not change the queue. |

### Companion Object Properties

| Name | Summary |
|---|---|
| [ACTION_COLOR_DEFAULT](-a-c-t-i-o-n_-c-o-l-o-r_-d-e-f-a-u-l-t.md) | `val ACTION_COLOR_DEFAULT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Default action text color as per Material Design i.e. R.color.colorAccent. |
| [BACKGROUND_COLOR_DEFAULT](-b-a-c-k-g-r-o-u-n-d_-c-o-l-o-r_-d-e-f-a-u-l-t.md) | `val BACKGROUND_COLOR_DEFAULT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Default SnackProgressBar background color as per Material Design. |
| [LENGTH_INDEFINITE](-l-e-n-g-t-h_-i-n-d-e-f-i-n-i-t-e.md) | `const val LENGTH_INDEFINITE: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Show the SnackProgressBar indefinitely. Note that this will be changed to LENGTH_SHORT and dismissed if there is another SnackProgressBar in queue before and after. |
| [LENGTH_LONG](-l-e-n-g-t-h_-l-o-n-g.md) | `const val LENGTH_LONG: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Show the SnackProgressBar for a long period of time. |
| [LENGTH_SHORT](-l-e-n-g-t-h_-s-h-o-r-t.md) | `const val LENGTH_SHORT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Show the SnackProgressBar for a short period of time. |
| [MESSAGE_COLOR_DEFAULT](-m-e-s-s-a-g-e_-c-o-l-o-r_-d-e-f-a-u-l-t.md) | `val MESSAGE_COLOR_DEFAULT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Default message text color as per Material Design. |
| [OVERLAY_COLOR_DEFAULT](-o-v-e-r-l-a-y_-c-o-l-o-r_-d-e-f-a-u-l-t.md) | `val OVERLAY_COLOR_DEFAULT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Default overlayLayout color i.e. android.R.color.white. |
| [PROGRESSBAR_COLOR_DEFAULT](-p-r-o-g-r-e-s-s-b-a-r_-c-o-l-o-r_-d-e-f-a-u-l-t.md) | `val PROGRESSBAR_COLOR_DEFAULT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Default progressBar color as per Material Design i.e. R.color.colorAccent. |
| [PROGRESSTEXT_COLOR_DEFAULT](-p-r-o-g-r-e-s-s-t-e-x-t_-c-o-l-o-r_-d-e-f-a-u-l-t.md) | `val PROGRESSTEXT_COLOR_DEFAULT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Default progressText color as per Material Design. |
