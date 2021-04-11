package com.mitsuki.bottominput.custom

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.mitsuki.armory.extend.navigationBarHeight
import com.mitsuki.armory.extend.statusBarHeight
import com.mitsuki.bottominput.screenHeight
import com.mitsuki.bottominput.screenWidth

class InputMeasurePopupWindow(private val activity: AppCompatActivity) : PopupWindow(),
    ViewTreeObserver.OnGlobalLayoutListener, LifecycleObserver {

    private val currentDisplayRect by lazy { Rect() }
    private val lastDisplayRect by lazy { Rect() }
    private val originalRect by lazy { Rect() }
    private val mDeviationHeight by lazy {
        activity.statusBarHeight().coerceAtMost(activity.navigationBarHeight())
    }
//    private var maxHeight = 0

    init {
        val contentView = View(activity)
        width = 0
        height = ViewGroup.LayoutParams.MATCH_PARENT
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        inputMethodMode = INPUT_METHOD_NEEDED
        contentView.viewTreeObserver.addOnGlobalLayoutListener(this)
        setContentView(contentView)


        activity.window.decorView.post {
            showAtLocation(
                activity.window.decorView,
                Gravity.NO_GRAVITY,
                0,
                0
            )
        }

        activity.lifecycle.addObserver(this)
    }

    private val context: Context get() = contentView.context

    override fun onGlobalLayout() {
        obtainOriginalRect()
        /**
         * 1.横屏模式在切入后台，再切回前台，部分手机会产生视图旋转。触发布局调整。
         * 2.底部导航栏状态变更会触发布局调整。
         * 3.有些时候我们并不会把app设置为全屏，计算键盘高度并不总等于物理高减可视底。
         * 4.有些机型点开软键盘后会频繁触发onGlobalLayout。
         */

        /**
         * 总共三块内容
         * 一块是屏幕高度
         * 一块是当前显示区域
         * 一块是上次显示区域
         *
         */
        //部分设备后台旋转事件过滤
        val min = originalRect.bottom.coerceAtMost(originalRect.right)
        val max = originalRect.bottom.coerceAtLeast(originalRect.right)
        //正方形分辨率
        if (max.toDouble() / min.toDouble() >= 1.2) {
            when (activity.requestedOrientation) {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT -> {
                    if (originalRect.right > originalRect.bottom) return
                }
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE -> {
                    if (originalRect.bottom > originalRect.right) return
                }
            }
        }

        contentView.getWindowVisibleDisplayFrame(currentDisplayRect)


        //主要两个点
        //一是可能存在物理导航栏，它并不会显示在屏幕中，但是存在高度
        //二是状态栏、导航栏的改变(例如显示隐藏)导致的布局变化，最终g


        //过滤重复事件
        if (currentDisplayRect.bottom == lastDisplayRect.bottom) return

        //屏幕旋转情况下的导航栏高度
        val isShowNavigation =
            (0 == (contentView.systemUiVisibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION));
        val navigationBarHeight = when (activity.windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 -> if (isShowNavigation) context.navigationBarHeight() else 0
            else -> 0
        }


        //物理导航栏，有高度但是不显示
        //排除了导航栏的高度
        //一个屏幕的真实高度排除了可能存在的导航栏的高度
        val excludeNavigation = originalRect.bottom - navigationBarHeight
        //当前的高度差，>=0的时候说明输入法隐藏中
        val currentHeightDiff = currentDisplayRect.bottom - excludeNavigation
        //前后两次显示矩阵的高度差
        val aroundHeightDiff = currentDisplayRect.bottom - lastDisplayRect.bottom

        if (
            (currentHeightDiff >= 0 && aroundHeightDiff <= mDeviationHeight) ||//两次高度变动属于状态栏或导航栏的变动
            (currentHeightDiff >= 0 && currentDisplayRect.bottom < excludeNavigation) || //当前的高度在最低线内部
            (currentHeightDiff < 0 && currentDisplayRect.bottom >= (originalRect.bottom - mDeviationHeight) && excludeNavigation != 0) //存在一个最低的显示高度
        ) {
            //这些都是状态栏或者导航栏发生变化的事件过滤
            return
        }

        val keyboardHeight =
            if (currentHeightDiff >= 0) originalRect.bottom - lastDisplayRect.bottom
            else originalRect.bottom - currentDisplayRect.bottom


//        Log.e("asdf", "originalRect : $originalRect")
//        Log.e("asdf", "currentDisplayRect : $currentDisplayRect")
//        Log.e("asdf", "lastDisplayRect : $lastDisplayRect")
//        Log.e("asdf", "navigationBarHeight : $navigationBarHeight")
//        Log.e("asdf", "keyboardHeight : $keyboardHeight")
//        Log.e("asdf", "=====================================================")


        lastDisplayRect.set(currentDisplayRect)
    }

    private fun obtainOriginalRect() {
        originalRect.set(0, 0, activity.screenWidth, activity.screenHeight)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onLifeDestroy() {
        dismiss()
        contentView.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }
}