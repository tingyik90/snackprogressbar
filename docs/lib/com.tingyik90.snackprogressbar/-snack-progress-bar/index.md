[lib](../../index.md) / [com.tingyik90.snackprogressbar](../index.md) / [SnackProgressBar](./index.md)

# SnackProgressBar

`class SnackProgressBar`

SnackProgressBar is the holder for information to be displayed via SnackProgressBarManager.

### Types

| Name | Summary |
|---|---|
| [OnActionClickListener](-on-action-click-listener/index.md) | Interface definition for a callback to be invoked when an action is clicked.`interface OnActionClickListener` |

### Annotations

| Name | Summary |
|---|---|
| [SnackProgressBarType](-snack-progress-bar-type/index.md) | `annotation class SnackProgressBarType` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Creates a SnackProgressBar.`SnackProgressBar(type: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Functions

| Name | Summary |
|---|---|
| [getBundle](get-bundle.md) | Gets the additional bundle of SnackProgressBar. This value may be null.`fun getBundle(): `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`?` |
| [putBundle](put-bundle.md) | Sets the additional bundle of SnackProgressBar, which can be retrieved later.`fun putBundle(bundle: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`): `[`SnackProgressBar`](./index.md) |
| [setAction](set-action.md) | Sets the action of SnackProgressBar. The action can be clicked to trigger [OnActionClickListener](-on-action-click-listener/index.md).`fun setAction(action: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, onActionClickListener: OnActionClickListener?): `[`SnackProgressBar`](./index.md) |
| [setAllowUserInput](set-allow-user-input.md) | Sets whether user input is allowed. Setting to FALSE will display an OverlayLayout which blocks user input.`fun setAllowUserInput(allowUserInput: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`SnackProgressBar`](./index.md) |
| [setIconBitmap](set-icon-bitmap.md) | Sets the icon of SnackProgressBar. Only a bitmap or a resId can be specified at any one time.`fun setIconBitmap(bitmap: `[`Bitmap`](https://developer.android.com/reference/android/graphics/Bitmap.html)`): `[`SnackProgressBar`](./index.md) |
| [setIconResource](set-icon-resource.md) | Sets the icon of SnackProgressBar. Only a bitmap or a resId can be specified at any one time.`fun setIconResource(iconResId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBar`](./index.md) |
| [setIsIndeterminate](set-is-indeterminate.md) | Sets whether the ProgressBar is indeterminate.`fun setIsIndeterminate(isIndeterminate: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`SnackProgressBar`](./index.md) |
| [setMessage](set-message.md) | Sets the message of SnackProgressBar.`fun setMessage(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SnackProgressBar`](./index.md) |
| [setProgressMax](set-progress-max.md) | Sets the max progress for determinate ProgressBar.`fun setProgressMax(progressMax: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBar`](./index.md) |
| [setShowProgressPercentage](set-show-progress-percentage.md) | Sets whether to show progress in percentage.`fun setShowProgressPercentage(showProgressPercentage: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`SnackProgressBar`](./index.md) |
| [setSwipeToDismiss](set-swipe-to-dismiss.md) | Sets whether user can swipe the SnackProgressBar to dismiss it.`fun setSwipeToDismiss(swipeToDismiss: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`SnackProgressBar`](./index.md) |
| [setType](set-type.md) | Sets the type of SnackProgressBar.`fun setType(type: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [toString](to-string.md) | Returns a string representation of the SnackProgressBar.`fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [TYPE_CIRCULAR](-t-y-p-e_-c-i-r-c-u-l-a-r.md) | SnackProgressBar layout with message and circular ProgressBar.`const val TYPE_CIRCULAR: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [TYPE_HORIZONTAL](-t-y-p-e_-h-o-r-i-z-o-n-t-a-l.md) | SnackProgressBar layout with message and horizontal ProgressBar.`const val TYPE_HORIZONTAL: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [TYPE_NORMAL](-t-y-p-e_-n-o-r-m-a-l.md) | SnackProgressBar layout with message only.`const val TYPE_NORMAL: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
