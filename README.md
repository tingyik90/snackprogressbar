[![](https://jitpack.io/v/tingyik90/snackprogressbar.svg)](https://jitpack.io/#tingyik90/snackprogressbar)

# SnackProgressBar
Enhanced Snackbar for Android.

## Important
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

![Image of SnackProgressBar](https://i.imgur.com/9C3A4UZl.png)

Watch the demo video at https://youtu.be/dbawFbr6iPk.

## Getting Started

### SnackProgressBarManager
Start by creating an instance of SnackProgressBarManager in your activity.
If possible, the root view of the activity should be provided and can be any type of layout.

```java
private SnackProgressBarManager snackProgressBarManager;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // always have one instance of SnackProgressBarManager only in the activity
    snackProgressBarManager = new SnackProgressBarManager(view)
            // (optional) set the view which will animate with SnackProgressBar e.g. FAB when CoordinatorLayout is not used
            .setViewToMove(floatingActionButton)
            // (optional) change progressBar color, default = R.color.colorAccent
            .setProgressBarColor(R.color.colorAccent)
            // (optional) change background color, default = BACKGROUND_COLOR_DEFAULT (#FF323232)
            .setBackgroundColor(SnackProgressBarManager.BACKGROUND_COLOR_DEFAULT)
            // (optional) change text size, default = 14sp
            .setTextSize(14)
            // (optional) set max lines, default = 2
            .setMessageMaxLines(2)
            // (optional) register onDisplayListener
            .setOnDisplayListener(new SnackProgressBarManager.OnDisplayListener() {
                @Override
                public void onShown(SnackProgressBar snackProgressBar, int onDisplayId) {
                    // do something
                }

                @Override
                public void onDismissed(SnackProgressBar snackProgressBar, int onDisplayId) {
                    // do something
                }
            });
}
```

### SnackProgressBar
Create a SnackProgressBar by calling the following examples.
```java
// TYPE_NORMAL
SnackProgressBar normalType = new SnackProgressBar(
        SnackProgressBar.TYPE_NORMAL, "TYPE_NORMAL - " + queue)
        // (optional) allow user input, default = FALSE
        .setAllowUserInput(true)
        // (optional) allow user swipe to dismiss, default = FALSE
        .setSwipeToDismiss(true)
        // (optional) set icon
        .setIconResource(R.mipmap.ic_launcher);
// create a bundle and attach to snackProgressBar which can be retrieved via OnDisplayListener
Bundle bundle = new Bundle();
bundle.putInt("queue", queue);
normalType.putBundle(bundle);

// TYPE_NORMAL with action
SnackProgressBar normalTypeWithAction = new SnackProgressBar(
        SnackProgressBar.TYPE_NORMAL, "TYPE_NORMAL - If the action text is too long, a higher layout is used.")
        // set action button
        .setAction("DISMISS", new SnackProgressBar.OnActionClickListener() {
            @Override
            public void onActionClick() {
                // do something
            }
        });

// TYPE_HORIZONTAL
SnackProgressBar horizontalType = new SnackProgressBar(
        SnackProgressBar.TYPE_HORIZONTAL, "TYPE_HORIZONTAL - Loading...")
        // (optional) set the type of progressBar, default = FALSE
        .setIsIndeterminate(false)
        // (optional) set max progress, default = 100
        .setProgressMax(100)
        // (optional) show percentage, default = FALSE
        .setShowProgressPercentage(true);

// TYPE_CIRCULAR with action
SnackProgressBar circularTypeWithAction = new SnackProgressBar(
        SnackProgressBar.TYPE_CIRCULAR, "TYPE_CIRCULAR - Loading...")
        .setIsIndeterminate(true)
        .setAction("DISMISS", new SnackProgressBar.OnActionClickListener() {
            @Override
            public void onActionClick() {
                // do something
            }
        });
```
Note that `action` can be inserted in every type of SnackProgressBar.

### Show
Show the SnackProgressBar by calling:
```java
// LENGTH_SHORT, LENGTH_LONG, LENGTH_INDEFINITE or other positive millis can be used
snackProgressBarManager.show(snackProgressBar, SnackProgressBarManager.LENGTH_LONG);  
```

To enable callback for `OnShown` and `OnDismissed`, include a `onDisplayId` when calling `show()`.
```java
int onDisplayId = 100;
snackProgressBarManager.show(snackProgressBar, SnackProgressBarManager.LENGTH_LONG, onDisplayId);
```

Or you can add the SnackProgressBar into memory and call it later.
```java
// it is stored in HashMap, so storeId must be unique for each SnackProgressBar, else it will be overwritten
int storeId = 100;
snackProgressBarManager.put(snackProgressBar, storeId);
snackProgressBarManager.show(storeId, SnackProgressBarManager.LENGTH_LONG);
```

Calling `show()` will put the SnackProgressBar into a queue, which will be shown after those in queue before it has been dismissed
(by user action or set showDuration).

Note: If LENGTH_INDEFINITE is specified for the queued SnackProgressBar, adding a new SnackProgressBar into the queue will cause 
the previous SnackProgressBar to use LENGTH_SHORT instead, dismissed and then show the new SnackProgressBar.

### Update
Calling `show()` will always animate the hiding and showing of SnackProgressBar between queue. Use `updateTo()` instead to modify the
displayed SnackProgressBar without animation. To modify the currently showing SnackProgressBar:
```java
// get the currently showing indeterminateType and change the message
SnackProgressBar snackProgressBar = snackProgressBarManager.getLastShown();
snackProgressBar.setMessage("new message");
// calling updateTo() will not hide and show again the SnackProgressBar
snackProgressBarManager.updateTo(snackProgressBar);
```

### Dismiss
Call `snackProgressBarManager.dismiss()` to dismiss the currently showing SnackProgressBar. The next SnackProgressBar in queue will be shown.
Call `snackProgressBarManager.dismissAll()` to dismiss the currently showing SnackProgressBar and clear all other SnackProgressBars in queue.

### Set Progress
Call `snackProgressBarManager.setProgress()` to set the progress of ProgressBar.

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
NOTE: The latest java version is v2.2 and may include unfixed bugs. Higher versions are in Kotlin. The DemoActivity is still in Java.

## License
Copyright 2017 Saw Ting Yik

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
