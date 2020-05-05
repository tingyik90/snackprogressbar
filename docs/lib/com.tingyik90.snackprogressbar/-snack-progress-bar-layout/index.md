[lib](../../index.md) / [com.tingyik90.snackprogressbar](../index.md) / [SnackProgressBarLayout](./index.md)

# SnackProgressBarLayout

`class SnackProgressBarLayout : `[`LinearLayout`](https://developer.android.com/reference/android/widget/LinearLayout.html)`, ContentViewCallback`

Layout class for SnackProgressBar.

### Types

| Name | Summary |
|---|---|
| [OnBarTouchListener](-on-bar-touch-listener/index.md) | Interface definition for a callback to be invoked when the SnackProgressBar is touched.`interface OnBarTouchListener` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SnackProgressBarLayout(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`)`<br>`SnackProgressBarLayout(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, attrs: `[`AttributeSet`](https://developer.android.com/reference/android/util/AttributeSet.html)`?)`<br>`SnackProgressBarLayout(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, attrs: `[`AttributeSet`](https://developer.android.com/reference/android/util/AttributeSet.html)`?, defStyleAttr: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)`<br>`SnackProgressBarLayout(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, attrs: `[`AttributeSet`](https://developer.android.com/reference/android/util/AttributeSet.html)`?, defStyleAttr: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, defStyleRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [actionNextLineText](action-next-line-text.md) | `val actionNextLineText: `[`TextView`](https://developer.android.com/reference/android/widget/TextView.html) |
| [actionText](action-text.md) | `val actionText: `[`TextView`](https://developer.android.com/reference/android/widget/TextView.html) |
| [backgroundLayout](background-layout.md) | `val backgroundLayout: `[`SnackProgressBarLayout`](./index.md) |
| [circularDeterminateProgressBar](circular-determinate-progress-bar.md) | `val circularDeterminateProgressBar: `[`ProgressBar`](https://developer.android.com/reference/android/widget/ProgressBar.html) |
| [circularIndeterminateProgressBar](circular-indeterminate-progress-bar.md) | `val circularIndeterminateProgressBar: `[`ProgressBar`](https://developer.android.com/reference/android/widget/ProgressBar.html) |
| [horizontalProgressBar](horizontal-progress-bar.md) | `val horizontalProgressBar: `[`ProgressBar`](https://developer.android.com/reference/android/widget/ProgressBar.html) |
| [iconImage](icon-image.md) | `val iconImage: `[`ImageView`](https://developer.android.com/reference/android/widget/ImageView.html) |
| [mainLayout](main-layout.md) | `val mainLayout: `[`LinearLayout`](https://developer.android.com/reference/android/widget/LinearLayout.html) |
| [messageText](message-text.md) | `val messageText: `[`TextView`](https://developer.android.com/reference/android/widget/TextView.html) |
| [progressText](progress-text.md) | `val progressText: `[`TextView`](https://developer.android.com/reference/android/widget/TextView.html) |
| [progressTextCircular](progress-text-circular.md) | `val progressTextCircular: `[`TextView`](https://developer.android.com/reference/android/widget/TextView.html) |
| [roundedBackgroundLayout](rounded-background-layout.md) | `val roundedBackgroundLayout: `[`LinearLayout`](https://developer.android.com/reference/android/widget/LinearLayout.html) |

### Functions

| Name | Summary |
|---|---|
| [animateContentIn](animate-content-in.md) | Animation as per original Snackbar class`fun animateContentIn(delay: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, duration: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [animateContentOut](animate-content-out.md) | Animation as per original Snackbar class`fun animateContentOut(delay: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, duration: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onAttachedToWindow](on-attached-to-window.md) | onAttachedToWindow`fun onAttachedToWindow(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onMeasure](on-measure.md) | onMeasure for determining actionNextLine and message height`fun onMeasure(widthMeasureSpec: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, heightMeasureSpec: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onSizeChanged](on-size-changed.md) | Animation for when updateTo() is called by SnackProgressBarManager and size changed`fun onSizeChanged(w: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, h: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, oldW: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, oldH: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [useRoundedCornerBackground](use-rounded-corner-background.md) | Sets whether to use rounded corner background for SnackProgressBar according to new Material Design. Note that the background cannot be changed after being shown.`fun useRoundedCornerBackground(useRoundedCornerBackground: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
