[lib](../../index.md) / [com.tingyik90.snackprogressbar](../index.md) / [SnackProgressBarManager](index.md) / [disable](./disable.md)

# disable

`fun disable(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Call [dismissAll](dismiss-all.md) and remove [onDisplayListener](#) to prevent memory leak.
This is called automatically during [Lifecycle.Event.ON_DESTROY](#) of activity / fragment
if a [LifecycleOwner](#) is provided.

