[lib](../../index.md) / [com.tingyik90.snackprogressbar](../index.md) / [SnackProgressBar](./index.md)

# SnackProgressBar

`class SnackProgressBar`

Main class containing the display information of SnackProgressBar to be displayed
via SnackProgressBarManager.

### Types

| Name | Summary |
|---|---|
| [OnActionClickListener](-on-action-click-listener/index.md) | `interface OnActionClickListener`<br>Interface definition for a callback to be invoked when an action is clicked. |

### Annotations

| Name | Summary |
|---|---|
| [SnackProgressBarType](-snack-progress-bar-type/index.md) | `annotation class SnackProgressBarType` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SnackProgressBar(type: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)`<br>Main class containing the display information of SnackProgressBar to be displayed via SnackProgressBarManager. |

### Functions

| Name | Summary |
|---|---|
| [getBundle](get-bundle.md) | `fun getBundle(): `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`?`<br>Gets the additional bundle of SnackProgressBar. This value may be null. |
| [putBundle](put-bundle.md) | `fun putBundle(bundle: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`): `[`SnackProgressBar`](./index.md)<br>Sets the additional bundle of SnackProgressBar. |
| [setAction](set-action.md) | `fun setAction(action: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, onActionClickListener: `[`OnActionClickListener`](-on-action-click-listener/index.md)`?): `[`SnackProgressBar`](./index.md)<br>Sets the action of SnackProgressBar. |
| [setAllowUserInput](set-allow-user-input.md) | `fun setAllowUserInput(allowUserInput: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`SnackProgressBar`](./index.md)<br>Sets whether user input is allowed. Setting to FALSE will display an OverlayLayout which blocks user input. |
| [setIconBitmap](set-icon-bitmap.md) | `fun setIconBitmap(bitmap: `[`Bitmap`](https://developer.android.com/reference/android/graphics/Bitmap.html)`): `[`SnackProgressBar`](./index.md)<br>Sets the icon of SnackProgressBar. Only a bitmap or a resId can be specified at any one time. |
| [setIconResource](set-icon-resource.md) | `fun setIconResource(iconResId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBar`](./index.md)<br>Sets the icon of SnackProgressBar. Only a bitmap or a resId can be specified at any one time. |
| [setIsIndeterminate](set-is-indeterminate.md) | `fun setIsIndeterminate(isIndeterminate: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`SnackProgressBar`](./index.md)<br>Sets whether the progressBar is indeterminate. |
| [setMessage](set-message.md) | `fun setMessage(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SnackProgressBar`](./index.md)<br>Sets the message of SnackProgressBar. |
| [setProgressMax](set-progress-max.md) | `fun setProgressMax(progressMax: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SnackProgressBar`](./index.md)<br>Sets the max progress for determinate ProgressBar. |
| [setShowProgressPercentage](set-show-progress-percentage.md) | `fun setShowProgressPercentage(showProgressPercentage: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`SnackProgressBar`](./index.md)<br>Sets whether to show progress in percentage. |
| [setSwipeToDismiss](set-swipe-to-dismiss.md) | `fun setSwipeToDismiss(swipeToDismiss: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`SnackProgressBar`](./index.md)<br>Sets whether user can swipe to dismiss. |
| [setType](set-type.md) | `fun setType(type: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Sets the type of SnackProgressBar. |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [TYPE_CIRCULAR](-t-y-p-e_-c-i-r-c-u-l-a-r.md) | `const val TYPE_CIRCULAR: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>SnackProgressBar layout with message and circular progressBar. |
| [TYPE_HORIZONTAL](-t-y-p-e_-h-o-r-i-z-o-n-t-a-l.md) | `const val TYPE_HORIZONTAL: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>SnackProgressBar layout with message and horizontal progressBar. |
| [TYPE_NORMAL](-t-y-p-e_-n-o-r-m-a-l.md) | `const val TYPE_NORMAL: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>SnackProgressBar layout with message only. |
