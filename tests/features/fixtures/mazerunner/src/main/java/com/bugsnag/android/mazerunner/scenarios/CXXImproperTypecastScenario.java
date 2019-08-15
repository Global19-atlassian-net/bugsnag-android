package com.bugsnag.android.mazerunner.scenarios;


import android.content.Context;

import com.bugsnag.android.Configuration;

import androidx.annotation.NonNull;

public class CXXImproperTypecastScenario extends Scenario {

    static {
        System.loadLibrary("bugsnag-ndk");
        System.loadLibrary("monochrome");
        System.loadLibrary("entrypoint");
    }

    public native void crash();

    public CXXImproperTypecastScenario(@NonNull Configuration config, @NonNull Context context) {
        super(config, context);
    }

    @Override
    public void run() {
        super.run();
        crash();
    }
}
