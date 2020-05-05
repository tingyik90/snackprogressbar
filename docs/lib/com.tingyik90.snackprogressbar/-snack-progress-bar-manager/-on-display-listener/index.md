[lib](../../../index.md) / [com.tingyik90.snackprogressbar](../../index.md) / [SnackProgressBarManager](../index.md) / [OnDisplayListener](./index.md)

# OnDisplayListener

`interface OnDisplayListener`

Interface definition for a callback to be invoked when the SnackProgressBar is shown or dismissed.

### Functions

| Name | Summary |
|---|---|
| [onDismissed](on-dismissed.md) | Called when the SnackProgressBar is dismissed.`open fun onDismissed(snackProgressBar: `[`SnackProgressBar`](../../-snack-progress-bar/index.md)`, onDisplayId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onLayoutInflated](on-layout-inflated.md) | Called when the SnackProgressBar view is inflated.`open fun onLayoutInflated(snackProgressBarLayout: `[`SnackProgressBarLayout`](../../-snack-progress-bar-layout/index.md)`, overlayLayout: `[`FrameLayout`](https://developer.android.com/reference/android/widget/FrameLayout.html)`, snackProgressBar: `[`SnackProgressBar`](../../-snack-progress-bar/index.md)`, onDisplayId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onShown](on-shown.md) | Called when the SnackProgressBar is shown.`open fun onShown(snackProgressBar: `[`SnackProgressBar`](../../-snack-progress-bar/index.md)`, onDisplayId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
