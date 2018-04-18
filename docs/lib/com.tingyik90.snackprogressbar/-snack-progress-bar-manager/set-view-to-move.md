[lib](../../index.md) / [com.tingyik90.snackprogressbar](../index.md) / [SnackProgressBarManager](index.md) / [setViewToMove](./set-view-to-move.md)

# setViewToMove

`fun setViewToMove(viewToMove: `[`View`](https://developer.android.com/reference/android/view/View.html)`): `[`SnackProgressBarManager`](index.md)

Passes the view (e.g. FloatingActionButton) to move up or down as SnackProgressBar is shown or dismissed.

This is not required for FloatingActionButton if a CoordinatorLayout is provided, but will be required for
other layout types. This can be used to animate any view besides FloatingActionButton as well.

### Parameters

`viewToMove` - View to be animated along with the SnackProgressBar.