[lib](../../index.md) / [com.tingyik90.snackprogressbar](../index.md) / [SnackProgressBarManager](index.md) / [show](./show.md)

# show

`fun show(storeId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, duration: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Shows the SnackProgressBar based on its storeId with the specified duration.
If another SnackProgressBar is already showing, this SnackProgressBar will be queued
and shown accordingly after those queued are dismissed.

### Parameters

`storeId` - OneUp of the SnackProgressBar stored in SnackProgressBarManager.

`duration` - Duration to show the SnackProgressBar of either
[LENGTH_SHORT](-l-e-n-g-t-h_-s-h-o-r-t.md), [LENGTH_LONG](-l-e-n-g-t-h_-l-o-n-g.md), [LENGTH_INDEFINITE](-l-e-n-g-t-h_-i-n-d-e-f-i-n-i-t-e.md)
or any positive millis.`fun show(storeId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, duration: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, onDisplayId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Shows the SnackProgressBar based on its storeId with the specified duration.
If another SnackProgressBar is already showing, this SnackProgressBar will be queued
and shown accordingly after those queued are dismissed.

### Parameters

`storeId` - OneUp of the SnackProgressBar stored in SnackProgressBarManager.

`duration` - Duration to show the SnackProgressBar of either
[LENGTH_SHORT](-l-e-n-g-t-h_-s-h-o-r-t.md), [LENGTH_LONG](-l-e-n-g-t-h_-l-o-n-g.md), [LENGTH_INDEFINITE](-l-e-n-g-t-h_-i-n-d-e-f-i-n-i-t-e.md)
or any positive millis.

`onDisplayId` - OnDisplayId attached to the SnackProgressBar when implementing the OnDisplayListener.`fun show(snackProgressBar: `[`SnackProgressBar`](../-snack-progress-bar/index.md)`, duration: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Shows the SnackProgressBar with the specified duration.
If another SnackProgressBar is already showing, this SnackProgressBar will be queued
and shown accordingly after those queued are dismissed.

### Parameters

`snackProgressBar` - SnackProgressBar to be shown.

`duration` - Duration to show the SnackProgressBar of either
[LENGTH_SHORT](-l-e-n-g-t-h_-s-h-o-r-t.md), [LENGTH_LONG](-l-e-n-g-t-h_-l-o-n-g.md), [LENGTH_INDEFINITE](-l-e-n-g-t-h_-i-n-d-e-f-i-n-i-t-e.md)
or any positive millis.`fun show(snackProgressBar: `[`SnackProgressBar`](../-snack-progress-bar/index.md)`, duration: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, onDisplayId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Shows the SnackProgressBar with the specified duration.
If another SnackProgressBar is already showing, this SnackProgressBar will be queued
and shown accordingly after those queued are dismissed.

### Parameters

`snackProgressBar` - SnackProgressBar to be shown.

`duration` - Duration to show the SnackProgressBar of either
[LENGTH_SHORT](-l-e-n-g-t-h_-s-h-o-r-t.md), [LENGTH_LONG](-l-e-n-g-t-h_-l-o-n-g.md), [LENGTH_INDEFINITE](-l-e-n-g-t-h_-i-n-d-e-f-i-n-i-t-e.md)
or any positive millis.

`onDisplayId` - OnDisplayId attached to the SnackProgressBar when implementing the OnDisplayListener.