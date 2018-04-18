[lib](../../index.md) / [com.tingyik90.snackprogressbar](../index.md) / [SnackProgressBarManager](index.md) / [setViewsToMove](./set-views-to-move.md)

# setViewsToMove

`fun setViewsToMove(viewsToMove: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`View`](https://developer.android.com/reference/android/view/View.html)`>): `[`SnackProgressBarManager`](index.md)

Passes the views (e.g. FloatingActionButton) to move up or down as SnackProgressBar is shown or dismissed.

This is not required for FloatingActionButton if a CoordinatorLayout is provided, but will be required for
other layout types. This can be used to animate any views besides FloatingActionButton as well.

### Parameters

`viewsToMove` - Views to be animated along with the SnackProgressBar.