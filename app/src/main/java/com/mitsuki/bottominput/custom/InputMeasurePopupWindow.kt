package com.mitsuki.bottominput.custom

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.*
import android.widget.PopupWindow
import com.mitsuki.bottominput.screenHeight
import com.mitsuki.bottominput.screenWidth

class InputMeasurePopupWindow(activity: Activity) : PopupWindow(),
    ViewTreeObserver.OnGlobalLayoutListener {

    private val currentDisplayRect = Rect()
    private val oldDisplayRect = Rect()
    private val originalRect = Rect()
    private var maxHeight = 0

    init {
        val contentView = View(activity)
        width = 0
        height = ViewGroup.LayoutParams.MATCH_PARENT
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        inputMethodMode = INPUT_METHOD_NEEDED
        contentView.viewTreeObserver.addOnGlobalLayoutListener(this)
        setContentView(contentView)

        originalRect.set(0, 0, activity.screenWidth, activity.screenHeight)

        activity.window.decorView.post {
            showAtLocation(
                activity.window.decorView,
                Gravity.NO_GRAVITY,
                0,
                0
            )
        }
    }

    private val context: Context get() = contentView.context

    override fun onGlobalLayout() {
        /**
         * 1.横屏模式在切入后台，再切回前台，部分手机会产生视图旋转。触发布局调整。
         * 2.底部导航栏状态变更会触发布局调整。
         * 3.有些时候我们并不会把app设置为全屏，计算键盘高度并不总等于物理高减可视底。
         * 4.有些机型点开软键盘后会频繁触发onGlobalLayout。
         */

        //屏幕旋转的问题
        //StatusBar显示隐藏事件处理

        Log.e("asdf", "onGlobalLayout")
        contentView.getWindowVisibleDisplayFrame(currentDisplayRect)
        Log.e("asdf", "$currentDisplayRect")

        if (currentDisplayRect.bottom == oldDisplayRect.bottom) {
            //过滤重复事件
            return
        }

        //计算一个底部navigation的高度，通过navigation是否显示和屏幕的方向综合计算这个高度
        //0和180的时候，navigation显示时就直接有这个高度，90和270直接高度0


        if (currentDisplayRect.bottom > maxHeight) {
            maxHeight = currentDisplayRect.bottom
        }
//        val screenHeight: Int = DensityUtil.getScreenHeight(context)
//        //键盘的高度
        val keyboardHeight = maxHeight - currentDisplayRect.bottom
        val visible = keyboardHeight > context.screenHeight / 4
        Log.e("asdf", "keyboardHeight : $keyboardHeight")



        oldDisplayRect.set(currentDisplayRect)
    }
}