package com.mitsuki.bottominput

import android.app.Activity

val Activity.screenHeight: Int
    get() = windowManager.currentWindowMetrics.bounds.height()