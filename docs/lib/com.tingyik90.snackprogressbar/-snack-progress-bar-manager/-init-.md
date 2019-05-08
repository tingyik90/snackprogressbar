[lib](../../index.md) / [com.tingyik90.snackprogressbar](../index.md) / [SnackProgressBarManager](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`SnackProgressBarManager(providedView: `[`View`](https://developer.android.com/reference/android/view/View.html)`, lifecycleOwner: LifecycleOwner? = null)`

Create an instance of SnackProgressBarManager.

### Parameters

`providedView` - View provided to search for parent view to hold the SnackProgressBar.
If possible, it should be the root view of the activity and can be any type of layout.
The constructor will loop and crawl up the view hierarchy to find a suitable parent view.

`lifecycleOwner` - The activity / fragment that this SnackProgressBarManager is attached to.
This provides a quick way to automatically [dismissAll](dismiss-all.md) and remove [onDisplayListener](#) during onDestroy of
the activity / fragment to prevent memory leak.

**Constructor**
Create an instance of SnackProgressBarManager.

