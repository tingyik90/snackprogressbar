[lib](../../index.md) / [com.tingyik90.snackprogressbar](../index.md) / [SnackProgressBarManager](index.md) / [updateTo](./update-to.md)

# updateTo

`fun updateTo(storeId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Updates the currently showing SnackProgressBar without dismissing it to the SnackProgressBar
with the corresponding storeId i.e. updating without animation.
Note: This does not change the queue.

### Parameters

`storeId` - OneUp of the SnackProgressBar stored in SnackProgressBarManager.`fun updateTo(snackProgressBar: `[`SnackProgressBar`](../-snack-progress-bar/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Updates the currently showing SnackProgressBar without dismissing it to the new SnackProgressBar
i.e. updating without animation.
Note: This does not change the queue.

### Parameters

`snackProgressBar` - SnackProgressBar to be updated to.