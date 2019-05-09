[![](https://jitpack.io/v/tingyik90/snackprogressbar.svg)](https://jitpack.io/#tingyik90/snackprogressbar)

# SnackProgressBar
Enhanced Snackbar with ProgressBar for Android.

## Important
Please do not use **v6.0**. It is a broken compilation due to new R8 shrinker. `minifyEnabled` has been turned to `false` since **v6.1**.

## Versions
- **v6.1** does not break migration from **v5.0**. Added the following features:
  - SnackProgressBarLayout is now public, so you can directly edit the layout in `OnDisplayListener.onLayoutInflated`.
  - WeakReference is now used for views to prevent memory leak.
  - LifeCycle Architecture is used to call `SnackProgressBarManager.disable()` automatically in `OnDestroy` to prevent memory leak.
- **v5.0** is a migration to androidx from **v4.1**. There is no change in all methods. Only use this if you have migrated.
- **v4.1** is a huge leap from **v3.4** and offers much better flexibility. Snackbar types changed to `TYPE_NORMAL`, `TYPE_HORIZONTAL`, `TYPE_CIRCULAR`.
- **v3.4** is using Snackbar types of `TYPE_ACTION`, `TYPE_MESSAGE`, `TYPE_DETERMINATE`, `TYPE_INDETERMINATE`.

## Features
1. Two types of ProgressBar (TYPE_HORIZONTAL and TYPE_CIRULAR) are available (see image below). It can also be used as a normal SnackBar.
2. Supports multi-lines of message.
3. Supports long action text by moving it to next line as per Material Design.
4. Supports swipe to dimiss behaviour even without providing CoordinatorLayout. (Or you can remove this behaviour for CoordinatorLayout)
5. Additional views can be added to animate with the SnackProgressBar.
6. Provides OverlayLayout to prevent user input.
7. Provides a queue system.
8. Icon can be added.
9. Supports bundle in SnackProgressBar to carry information.
10. Supports changing element color and text size.

<img src="https://i.imgur.com/9C3A4UZl.png" width="500">

**NOTE:** This library is still following the old Material Design Specifications.

<img src="https://i.imgur.com/AriEeV2.png" width="500">

Watch the demo video at https://youtu.be/dbawFbr6iPk.

## Getting Started

### SnackProgressBarManager
Start by creating an instance of SnackProgressBarManager in your activity.
If possible, the root view of the activity should be provided and can be any type of layout.
Starting v6.0, you can provide a LifecycleOwner of an activity / fragment.
This will call `SnackProgressBarManager.disable()` in `OnDestroy` to prevent memory leak.

```kotlin
private val snackProgressBarManager by lazy { SnackProgressBarManager(mainLayout, lifecycleOwner = this) }

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_demo)
    val floatingActionButton = findViewById<View>(R.id.fab)
    snackProgressBarManager
        // (Optional) Set the view which will animate with SnackProgressBar e.g. FAB when CoordinatorLayout is not used
        .setViewToMove(floatingActionButton)
        // (Optional) Change progressBar color, default = R.color.colorAccent
        .setProgressBarColor(R.color.colorAccent)
        // (Optional) Change background color, default = BACKGROUND_COLOR_DEFAULT (#FF323232)
        .setBackgroundColor(SnackProgressBarManager.BACKGROUND_COLOR_DEFAULT)
        // (Optional) Change text size, default = 14sp
        .setTextSize(14f)
        // (Optional) Set max lines, default = 2
        .setMessageMaxLines(2)
        // (Optional) Register onDisplayListener
        .setOnDisplayListener(object : SnackProgressBarManager.OnDisplayListener {
            override fun onLayoutInflated(
                snackProgressBarLayout: SnackProgressBarLayout,
                overlayLayout: FrameLayout,
                snackProgressBar: SnackProgressBar,
                onDisplayId: Int
            ) {
                // In v6.0, both snackProgressBarLayout and overlayLayout are exposed.
                // You can edit them directly without reflection.
            }

            override fun onShown(snackProgressBar: SnackProgressBar, onDisplayId: Int) {
                // We have assigned onDisplayId = 5000 for normalType SnackProgressBar
                if (onDisplayId == 5000) {
                    // You can retrieve the attached information here
                    val bundle = snackProgressBar.getBundle()
                    if (bundle != null) {
                        val queueNo = bundle.getInt("queue")
                        val toast = "Showing queue $queueNo!"
                        Toast.makeText(applicationContext, toast, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onDismissed(snackProgressBar: SnackProgressBar, onDisplayId: Int) {
                // Do something
            }
        })
}  
```

### SnackProgressBar
Create a SnackProgressBar by calling the following examples.
```kotlin
// TYPE_NORMAL
val normalType =
    SnackProgressBar(SnackProgressBar.TYPE_NORMAL, "TYPE_NORMAL - $queue")
        // (Optional) allow user input, default = FALSE
        .setAllowUserInput(true)
        // (Optional) allow user swipe to dismiss, default = FALSE
        .setSwipeToDismiss(true)
        // (Optional) set icon
        .setIconResource(R.mipmap.ic_launcher)
// Create a bundle and attach to snackProgressBar which can be retrieved via OnDisplayListener
val bundle = Bundle()
bundle.putInt("queue", queue)
normalType.putBundle(bundle)

// TYPE_NORMAL with action
val normalTypeWithAction =
    SnackProgressBar(
        SnackProgressBar.TYPE_NORMAL,
        "TYPE_NORMAL - If the message and action are too long, a higher layout is used."
    ).setAction("LONG ACTION TEXT", object : SnackProgressBar.OnActionClickListener {
        // (Required) Set action button
        override fun onActionClick() {
            Toast.makeText(applicationContext, "Action Clicked!", Toast.LENGTH_SHORT).show()
        }
    })

// TYPE_HORIZONTAL
val horizontalType =
    SnackProgressBar(SnackProgressBar.TYPE_HORIZONTAL, "TYPE_HORIZONTAL - Loading...")
        // (Optional) Set the type of progressBar, default = FALSE
        .setIsIndeterminate(false)
        // (Optional) Set max progress, default = 100
        .setProgressMax(100)
        // (Optional) Show percentage, default = FALSE
        .setShowProgressPercentage(true)

// TYPE_CIRCULAR with action
// This type of layout is not recommended, simply because it is ugly.
val circularTypeWithAction =
    SnackProgressBar(SnackProgressBar.TYPE_CIRCULAR, "TYPE_CIRCULAR - Loading...")
        .setIsIndeterminate(true)
        .setAction("DISMISS", object : SnackProgressBar.OnActionClickListener {
            override fun onActionClick() {
                Toast.makeText(applicationContext, "Action Clicked!", Toast.LENGTH_SHORT).show()
            }
        })
```
Note that `action` can be inserted in every type of SnackProgressBar.

### Show
Show the SnackProgressBar by calling:
```kotlin
// LENGTH_SHORT, LENGTH_LONG, LENGTH_INDEFINITE or other positive millis can be used
snackProgressBarManager.show(snackProgressBar, SnackProgressBarManager.LENGTH_LONG)
```

You can include an `onDisplayId` when calling `show()`.
```kotlin
val onDisplayId = 100
snackProgressBarManager.show(snackProgressBar, SnackProgressBarManager.LENGTH_LONG, onDisplayId)
```

Or you can add the SnackProgressBar into memory and call it later.
```kotlin
// It is stored in HashMap, so storeId must be unique for each SnackProgressBar, else it will be overwritten
val storeId = 100
snackProgressBarManager.put(snackProgressBar, storeId)
snackProgressBarManager.show(storeId, SnackProgressBarManager.LENGTH_LONG)
```

Calling `show()` will put the SnackProgressBar into a queue, which will be shown after those in queue before it has been dismissed
(by user action or set showDuration).

Note: If LENGTH_INDEFINITE is specified for the queued SnackProgressBar, adding a new SnackProgressBar into the queue will cause 
the previous SnackProgressBar to use LENGTH_SHORT instead, dismissed and then show the new SnackProgressBar.

### Update
Calling `show()` will always animate the hiding and showing of SnackProgressBar between queue. Use `updateTo()` instead to modify the
displayed SnackProgressBar without animation. To modify the currently showing SnackProgressBar:
```kotlin
// Get the currently showing indeterminateType and change the message
val snackProgressBar = snackProgressBarManager.getLastShown()
snackProgressBar.setMessage("new message")
// Calling updateTo() will not hide and show again the SnackProgressBar
snackProgressBarManager.updateTo(snackProgressBar)
```

### Dismiss
Call `snackProgressBarManager.dismiss()` to dismiss the currently showing SnackProgressBar. The next SnackProgressBar in queue will be shown.
Call `snackProgressBarManager.dismissAll()` to dismiss the currently showing SnackProgressBar and clear all other SnackProgressBars in queue.

### Set Progress
Call `snackProgressBarManager.setProgress()` to set the progress of ProgressBar.

### Disable
**IMPORTANT:** To avoid memory leak, call `SnackProgressBarManager.disable()` in onDestroy manually.
Else, provide a `LifecycleOwner` in the constructor of `SnackProgressBarManager` to automatically call this method.

## JavaDoc
For further information, see https://tingyik90.github.io/snackprogressbar/lib/.

See demo at https://github.com/tingyik90/snackprogressbar/blob/master/app/src/main/java/com/tingyik90/demo/DemoActivity.java

## Download
In the project Gradle:
```Gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

In the app Gradle:
```Gradle
dependencies {
    implementation 'com.github.tingyik90:snackprogressbar:version'
}
```

## License
Copyright 2017-2019 Saw Ting Yik

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
