package com.mitsuki.bottominput

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView


fun View.showSoftKeyboard() {
    if (requestFocus()) {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun View.hideSoftKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun RecyclerView.addOnScrollListenerBy(
    onScrollStateChanged: ((recyclerView: RecyclerView, newState: Int) -> Unit)? = null,
    onScrolled: ((recyclerView: RecyclerView, dx: Int, dy: Int) -> Unit)? = null
) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            onScrollStateChanged?.invoke(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            onScrolled?.invoke(recyclerView, dx, dy)
        }
    })
}

@Suppress("DEPRECATION")
val Activity.screenWidth: Int
    get() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            DisplayMetrics().apply { display?.getRealMetrics(this) }.widthPixels
        else
            DisplayMetrics().apply { windowManager.defaultDisplay.getRealMetrics(this) }.widthPixels

@Suppress("DEPRECATION")
val Activity.screenHeight: Int
    get() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            DisplayMetrics().apply { display?.getRealMetrics(this) }.heightPixels
        else
            DisplayMetrics().apply { windowManager.defaultDisplay.getRealMetrics(this) }.heightPixels


