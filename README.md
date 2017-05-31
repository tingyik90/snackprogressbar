# SnackProgressBar
Enhanced Snackbar with ProgressBar for Android. Start by creating an instance of SnackProgressBarManager in your activity.

```java
private SnackProgressBarManager snackProgressBarManager;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // always only have one instance of SnackProgressBarManager in the activity
    snackProgressBarManager = new SnackProgressBarManager(this)
            .setViewToMove(floatingActionButton)    // (optional) set the view which will animate with SnackProgressBar
            .setOnActionClickListener(this);        // (optional) set the action click listener
}
```

Four types of SnackProgressBar are available.
![Image of SnackProgressBar](http://i.imgur.com/xGOnwVp.png)

Create a SnackProgressBar by calling the following examples.
```java
SnackProgressBar actionType = new SnackProgressBar(
            SnackProgressBar.TYPE_ACTION, "TYPE_ACTION - Swipe to dismiss.")
            .setAction("DISMISS")       // (required) set action button
            .setSwipeToDismiss(true);   // (optional) allow user swipe to dismiss, default = FALSE

SnackProgressBar determinateType = new SnackProgressBar(
            SnackProgressBar.TYPE_DETERMINATE, "TYPE_DETERMINATE.")
            .setProgressMax(100)                // (optional) set max progress, default = 100
            .setShowProgressPercentage(true);   // (optional) show percentage, default = TRUE

SnackProgressBar indeterminateType = new SnackProgressBar(
            SnackProgressBar.TYPE_INDETERMINATE, "TYPE_INDETERMINATE.");

SnackProgressBar messageType = new SnackProgressBar(
            SnackProgressBar.TYPE_MESSAGE, "TYPE_MESSAGE.")
            .setAllowUserInput(true)    // (optional) allow user input, default = FALSE
            .setSwipeToDismiss(true);   // (optional) allow user swipe to dismiss, default = FALSE
```

Show the SnackProgressBar by calling:
```java
snackProgressBarManager.show(snackProgressBar, SnackProgressBarManager.LENGTH_LONG);  // or LENGTH_SHORT or LENGTH_INDEFINITE
```

Or you can add the SnackProgressBar into memory and call it later.
```java
int id = 100;
snackProgressBarManager.add(snackProgressBar, id);   // id must be unique for each SnackProgressBar, else it will be overwritten
snackProgressBarManager.show(id);
```

Calling `show()` will put the SnackProgressBar into a queue, which will be shown after those in queue before it has been dismissed
(by user action or set duration). Note: If LENGTH_INDEFINITE is specified for the queued SnackProgressBar, adding a new SnackProgressBar
into the queue will cause the previous SnackProgressBar to use LENGTH_SHORT instead, dismissed and then show the new SnackProgressBar.

Calling `show()` will always animate the hiding and showing of SnackProgressBar between queue. Use `update()` instead to modify the
displayed SnackProgressBar without animation. To modify the currently showing SnackProgressBar:
```java
// get the currently showing SnackProgressBar and change the message
snackProgressBarManager.getLastShowing().setMessage("TYPE_INDETERMINATE - " + i);
snackProgressBarManager.updateLastShowing();

// or if another SnackProgressBar is already created, modify directly with
snackProgressBarManager.update(snackProgressBar);
```

Call `dismiss()` to dismiss the currently showing SnackProgressBar. The next SnackProgressBar in queue will be shown.
Call `clearAll()` to dismiss the currently showing SnackProgressBar and clear all other SnackProgressBars in queue.

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
    compile 'com.github.tingyik90:snackprogressbar:1.1'
}
```

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
