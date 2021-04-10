package com.mitsuki.bottominput.custom

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.mitsuki.armory.extend.navigationBarHeight
import com.mitsuki.bottominput.screenHeight
import com.mitsuki.bottominput.screenWidth

class InputMeasurePopupWindow(private val activity: AppCompatActivity) : PopupWindow(),
    ViewTreeObserver.OnGlobalLayoutListener, LifecycleObserver {

    private val currentDisplayRect by lazy { Rect() }
    private val oldDisplayRect by lazy { Rect() }
    private val originalRect by lazy {
        Rect().apply { set(0, 0, context.screenWidth, context.screenHeight) }
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



        //不光是底部栏，顶栏状态变更也要考虑



        //过滤重复事件
        if (currentDisplayRect.bottom == oldDisplayRect.bottom) return

        //屏幕旋转情况下的导航栏高度
        val isShowNavigation =
            (0 == (contentView.systemUiVisibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION));
        val navigationBarHeight = when (activity.windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 -> if (isShowNavigation) context.navigationBarHeight() else 0
            else -> 0
        }




        //物理导航栏，有高度但是不显示
        //排除了导航栏的高度
        val excludeNavigation = originalRect.bottom - navigationBarHeight
        val ddd = currentDisplayRect.bottom - excludeNavigation








        Log.e("asdf", "navigationBarHeight : $navigationBarHeight")
        Log.e("asdf", "currentDisplayRect : $currentDisplayRect")

        //虚拟导航栏的显示隐藏


        val h = originalRect.bottom - navigationBarHeight
        val hh = currentDisplayRect.bottom - h
        val hhh = currentDisplayRect.bottom - oldDisplayRect.bottom

        val g = originalRect.bottom - navigationBarHeight
//        val gg = originalRect.bottom -
//        if ((hh >= 0 && hhh))






        //计算一个底部navigation的高度，通过navigation是否显示和屏幕的方向综合计算这个高度
        //0和180的时候，navigation显示时就直接有这个高度，90和270直接高度0
//
//
//        //StatusBar显示隐藏事件处理
//
//        //当前高度-屏幕高度
//        val hhh = currentDisplayRect.bottom - originalRect.bottom + 0// 可能存在导航栏的高度
//        val h = currentDisplayRect.bottom - originalRect.bottom
//        //当前高度-上次高度
//        val hh = currentDisplayRect.bottom - oldDisplayRect.bottom
//
//
//
//        Log.e("asdf", "$h")
//

//        val hhhh = if (hhh >= 0) hh else h
//        Log.e("asdf", "$hhhh")


//        if (h >= 0)


//
//        if (currentDisplayRect.bottom > maxHeight) {
//            maxHeight = currentDisplayRect.bottom
//        }
////        val screenHeight: Int = DensityUtil.getScreenHeight(context)
////        //键盘的高度
//        val keyboardHeight = maxHeight - currentDisplayRect.bottom
//        val visible = keyboardHeight > context.screenHeight / 4
//        Log.e("asdf", "keyboardHeight : $keyboardHeight")


        oldDisplayRect.set(currentDisplayRect)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onLifeDestroy() {
        dismiss()
    }
}