package com.tingyik90.demo

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tingyik90.snackprogressbar.SnackProgressBar
import com.tingyik90.snackprogressbar.SnackProgressBarLayout
import com.tingyik90.snackprogressbar.SnackProgressBarManager
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : AppCompatActivity() {

    // Always have one instance of SnackProgressBarManager only in the activity.
    //
    // In the constructor, provide a view to search for a suitable parent view to hold the SnackProgressBar.
    // If possible, it should be the root view of the activity and can be any type of layout.
    // If a CoordinatorLayout is provided, the FloatingActionButton will animate with the SnackProgressBar.
    // Else, [setViewsToMove] needs to be called to select the views to be animated.
    // Note that [setViewsToMove] can still be used for CoordinatorLayout to move other views.
    //
    // Starting v6.0, you can provide a LifecycleOwner of an activity / fragment.
    // This provides a quick way to automatically dismissAll and remove onDisplayListener during onDestroy of
    // the activity / fragment to prevent memory leak.
    private val snackProgressBarManager by lazy { SnackProgressBarManager(mainLayout, this) }
    private var queue = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        val floatingActionButton = findViewById<View>(R.id.fab)
        snackProgressBarManager
            // (Optional) Set the view which will animate with SnackProgressBar e.g. FAB when CoordinatorLayout is not used
            .setViewToMove(floatingActionButton)
            // (Optional) Change progressBar color, default = R.color.colorAccent
            .setProgressBarColor(android.R.color.holo_green_light)
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
                    // Starting v6.0, both snackProgressBarLayout and overlayLayout are exposed.
                    // You can edit them directly without reflection e.g.
                    /*
                    println(snackProgressBarLayout.messageText.text)
                     */
                    // You can also grab its parent so that the view floats above bottom navigation bar etc.
                    /*
                    val view = snackProgressBarLayout.parent as View
                    val params = view.layoutParams as ViewGroup.MarginLayoutParams
                    params.setMargins(0,0,0, 200)
                    view.layoutParams = params
                     */
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

    fun normalBtnClick(view: View) {
        // Create a normal snackProgressBar
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
        // Dismiss after duration LENGTH_LONG, assign an onDisplayId for callback. See above setOnDisplayListener()
        snackProgressBarManager.show(normalType, SnackProgressBarManager.LENGTH_LONG, 5000)
        queue++
    }

    fun normalWithActionBtnClick(view: View) {
        // (Optional) Assign storeId and store in SnackProgressBarManager. This is for easy recall only.
        createSnackProgressBarAndStoreInManager()
        // Call to show via storeId for 4 seconds
        snackProgressBarManager.show(100, 4000)
    }

    private fun createSnackProgressBarAndStoreInManager() {
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
        snackProgressBarManager.put(normalTypeWithAction, 100)
    }

    fun horizontalBtnClick(view: View) {
        val horizontalType =
            SnackProgressBar(SnackProgressBar.TYPE_HORIZONTAL, "TYPE_HORIZONTAL - Loading...")
                // (Optional) Set the type of progressBar, default = FALSE
                .setIsIndeterminate(false)
                // (Optional) Set max progress, default = 100
                .setProgressMax(100)
                // (Optional) Show percentage, default = FALSE
                .setShowProgressPercentage(true)
        // Show indefinitely
        snackProgressBarManager.show(horizontalType, SnackProgressBarManager.LENGTH_INDEFINITE)
        // Update progress
        object : CountDownTimer(5000, 50) {
            var i = 0

            override fun onTick(millisUntilFinished: Long) {
                i++
                snackProgressBarManager.setProgress(i)
            }

            override fun onFinish() {
                snackProgressBarManager.dismiss()
            }
        }.start()
    }

    fun horizontalWithActionBtnClick(view: View) {
        val horizontalTypeWithAction =
            SnackProgressBar(SnackProgressBar.TYPE_HORIZONTAL, "TYPE_HORIZONTAL - Loading...")
                .setIsIndeterminate(true)
                .setAction("DISMISS", object : SnackProgressBar.OnActionClickListener {
                    override fun onActionClick() {
                        Toast.makeText(applicationContext, "Action Clicked!", Toast.LENGTH_SHORT).show()
                    }
                })
        // Show for LENGTH_LONG
        snackProgressBarManager.show(horizontalTypeWithAction, SnackProgressBarManager.LENGTH_LONG)
    }

    fun circularBtnClick(view: View) {
        val circularType =
            SnackProgressBar(SnackProgressBar.TYPE_CIRCULAR, "TYPE_CIRCULAR - Loading...")
                .setIsIndeterminate(false)
                .setProgressMax(100)
                .setShowProgressPercentage(true)
        // Show indefinitely
        snackProgressBarManager.show(circularType, SnackProgressBarManager.LENGTH_INDEFINITE)
        // Update progress
        object : CountDownTimer(5000, 50) {
            var i = 0

            override fun onTick(millisUntilFinished: Long) {
                i++
                snackProgressBarManager.setProgress(i)
            }

            override fun onFinish() {
                snackProgressBarManager.dismiss()
            }
        }.start()
    }

    fun circularWithActionBtnClick(view: View) {
        // This type of layout is not recommended, simply because it is ugly.
        val circularTypeWithAction =
            SnackProgressBar(SnackProgressBar.TYPE_CIRCULAR, "TYPE_CIRCULAR - Loading...")
                .setIsIndeterminate(true)
                .setAction("DISMISS", object : SnackProgressBar.OnActionClickListener {
                    override fun onActionClick() {
                        Toast.makeText(applicationContext, "Action Clicked!", Toast.LENGTH_SHORT).show()
                    }
                })
        snackProgressBarManager.show(circularTypeWithAction, SnackProgressBarManager.LENGTH_LONG)
    }

    // After clicking normalBtn multiple times, click this to clear all queued SnackProgressBars
    fun clearBtnClick(view: View) {
        // clear all queued SnackProgressBars
        snackProgressBarManager.dismissAll()
    }

    override fun onStop() {
        super.onStop()
        // IMPORTANT: Call disable in onPause / onStop / onDestroy to prevent leak due to Handler not GCed.
        // If you provided a lifeCycleOwner, then this is automatically called in onDestroy.
        // This sample app includes LeakCanary for leak test.
        /*
        snackProgressBarManager.disable();
         */
    }

}
