[![](https://jitpack.io/v/tingyik90/snackprogressbar.svg)](https://jitpack.io/#tingyik90/snackprogressbar)

# SnackProgressBar
Enhanced Snackbar for Android.

## Features
1. Four types of SnackProgressBar are available (see image below).
2. Supports up to 2 lines of message as per Material Design.
3. Supports long action text by moving it to next line as per Material Design.
4. Supports swipe to dimiss behaviour even without providing CoordinatorLayout.
5. Additional views can be added to animate with the SnackProgressBar.
6. Provides OverlayLayout to prevent user input.
7. Provides a queue system.
8. Icon can be added.

![Image of SnackProgressBar](http://i.imgur.com/EZtGKbal.png)
![Demo of SnackProgressBar](http://i.imgur.com/gIN2W2m.gif)

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
    snackProgressBarManager = new SnackProgressBarManager(rootView)
            // (optional) set the view which will animate with SnackProgressBar e.g. FAB when CoordinatorLayout is not used.
            // use setViewsToMove instead if multiple views need to be animated
            .setViewToMove(floatingActionButton)
            // (optional) change progressBar color, default = R.color.colorAccent
            .setProgressBarColor(R.color.colorAccent)
            // (optional) register onDisplayListener
            .setOnDisplayListener(new SnackProgressBarManager.OnDisplayListener() {
                @Override
                public void onShown(int onDisplayId) {
                    // Do something...
                }
                @Override
                public void onDismissed(int onDisplayId) {
                    // Do something...
                }
            });
}
```

### SnackProgressBar
Create a SnackProgressBar by calling the following examples.
```java
SnackProgressBar actionType = new SnackProgressBar(
        SnackProgressBar.TYPE_ACTION, "TYPE_ACTION - Swipe to dismiss.")
        // (required) set action button
        .setAction("DISMISS", new SnackProgressBar.OnActionClickListener() {
        @Override
        public void onActionClick() {
                // Do something...
            }
        })
        // (optional) allow user swipe to dismiss, default = FALSE
        .setSwipeToDismiss(true);

SnackProgressBar determinateType = new SnackProgressBar(
        SnackProgressBar.TYPE_DETERMINATE, "TYPE_DETERMINATE.")
        // (optional) set max progress, default = 100
        .setProgressMax(100)
        // (optional) show percentage, default = TRUE
        .setShowProgressPercentage(true);

SnackProgressBar indeterminateType = new SnackProgressBar(
        SnackProgressBar.TYPE_INDETERMINATE, "TYPE_INDETERMINATE.");

SnackProgressBar messageType = new SnackProgressBar(
        SnackProgressBar.TYPE_MESSAGE, "TYPE_MESSAGE - 0.")
        // (optional) allow user input, default = FALSE
        .setAllowUserInput(true)
        // (optional) allow user swipe to dismiss, default = FALSE
        .setSwipeToDismiss(true)
        // (optional) set icon (can be used on all types)
        .setIconResource(R.mipmap.ic_launcher);
```

### Show
Show the SnackProgressBar by calling:
```java
// LENGTH_SHORT, LENGTH_LONG, LENGTH_INDEFINITE or other positive millis can be used
snackProgressBarManager.show(snackProgressBar, SnackProgressBarManager.LENGTH_LONG);  
```

To enable callback for `OnShown` and `OnDismissed`, include a `onDisplayId`.
```java
int onDisplayId = 100;
snackProgressBarManager.show(snackProgressBar, SnackProgressBarManager.LENGTH_LONG, onDisplayId);
```

Or you can add the SnackProgressBar into memory and call it later.
```java
// storeId must be unique for each SnackProgressBar, else it will be overwritten
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
snackProgressBar.setMessage("TYPE_INDETERMINATE - " + i);
// calling updateTo() will not hide and show again the SnackProgressBar
snackProgressBarManager.updateTo(snackProgressBar);
```

### Dismiss
Call `dismiss()` to dismiss the currently showing SnackProgressBar. The next SnackProgressBar in queue will be shown.
Call `clearAll()` to dismiss the currently showing SnackProgressBar and clear all other SnackProgressBars in queue.

### Set Progress
For `TYPE_DETERMINATE`, call `setProgress()` to set the progress of ProgressBar.

## JavaDoc
For further information, see https://tingyik90.github.io/snackprogressbar/javadoc/.

Watch the demo video at https://youtu.be/g5lLQtrthUc.

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
    compile 'com.github.tingyik90:snackprogressbar:3.0'
}
```
NOTE: The latest java version is v2.2 and may include unfixed bugs.

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
