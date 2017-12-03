[lib](../../index.md) / [com.tingyik90.snackprogressbar](../index.md) / [SnackProgressBar](.)

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
| [Type](-type/index.md) | `annotation class Type` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SnackProgressBar(type: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, action: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, iconBitmap: `[`Bitmap`](https://developer.android.com/reference/android/graphics/Bitmap.html)`?, iconResId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, progressMax: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, allowUserInput: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, swipeToDismiss: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, showProgressPercentage: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, onActionClickListener: `[`OnActionClickListener`](-on-action-click-listener/index.md)`?)`<br>Internal constructor for duplicating SnackProgressBar.`SnackProgressBar(type: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)`<br>Main class containing the display information of SnackProgressBar to be displayed via SnackProgressBarManager. |

### Functions

| Name | Summary |
|---|---|
| [setAction](set-action.md) | `fun setAction(action: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, onActionClickListener: `[`OnActionClickListener`](-on-action-click-listener/index.md)`?): SnackProgressBar`<br>Sets the action of SnackProgressBar. Only will be shown for TYPE_ACTION. |
| [setAllowUserInput](set-allow-user-input.md) | `fun setAllowUserInput(allowUserInput: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): SnackProgressBar`<br>Sets whether user input is allowed. Setting to FALSE will display an OverlayLayout which blocks user input. |
| [setIconBitmap](set-icon-bitmap.md) | `fun setIconBitmap(bitmap: `[`Bitmap`](https://developer.android.com/reference/android/graphics/Bitmap.html)`): SnackProgressBar`<br>Sets the icon of SnackProgressBar. Only a bitmap or a resId can be specified at any one time. |
| [setIconResource](set-icon-resource.md) | `fun setIconResource(iconResId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): SnackProgressBar`<br>Sets the icon of SnackProgressBar. Only a bitmap or a resId can be specified at any one time. |
| [setMessage](set-message.md) | `fun setMessage(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): SnackProgressBar`<br>Sets the message of SnackProgressBar. |
| [setProgressMax](set-progress-max.md) | `fun setProgressMax(progressMax: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): SnackProgressBar`<br>Sets the max progress for determinate ProgressBar. Only will be shown for TYPE_DETERMINATE. |
| [setShowProgressPercentage](set-show-progress-percentage.md) | `fun setShowProgressPercentage(showProgressPercentage: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): SnackProgressBar`<br>Sets whether to show progress in percentage. Only will be shown for TYPE_DETERMINATE. |
| [setSwipeToDismiss](set-swipe-to-dismiss.md) | `fun setSwipeToDismiss(swipeToDismiss: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): SnackProgressBar`<br>Sets whether user can swipe to dismiss. Swipe to dismiss only works for TYPE_ACTION and TYPE_MESSAGE. |
| [setType](set-type.md) | `fun setType(type: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Sets the type of SnackProgressBar. |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [TYPE_ACTION](-t-y-p-e_-a-c-t-i-o-n.md) | `const val TYPE_ACTION: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>SnackProgressBar layout with message and action button. |
| [TYPE_DETERMINATE](-t-y-p-e_-d-e-t-e-r-m-i-n-a-t-e.md) | `const val TYPE_DETERMINATE: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>SnackProgressBar layout with message, determinate progressBar and progress percentage. |
| [TYPE_INDETERMINATE](-t-y-p-e_-i-n-d-e-t-e-r-m-i-n-a-t-e.md) | `const val TYPE_INDETERMINATE: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>SnackProgressBar layout with message and indeterminate progressBar. |
| [TYPE_MESSAGE](-t-y-p-e_-m-e-s-s-a-g-e.md) | `const val TYPE_MESSAGE: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>SnackProgressBar layout with message only. |
