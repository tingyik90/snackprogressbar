package com.tingyik90.demo;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

}