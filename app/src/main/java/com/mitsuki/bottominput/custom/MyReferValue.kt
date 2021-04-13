package com.mitsuki.bottominput.custom

import android.app.Activity
import android.view.View
import com.mitsuki.bottominput.R
import com.mitsuki.bottominput.custom.helper.ReferValue

class MyReferValue(activity: Activity) : ReferValue {

    private val extendContainer: View by lazy { activity.findViewById(R.id.input_extend_container) }

    private val inputLayout: View by lazy { activity.findViewById<View>(R.id.input_bottom) }


    override fun translationYTarget(): View {
        return inputLayout
    }

    override fun updateTargetHeight(tH: Int) {
        extendContainer.apply {
            layoutParams = layoutParams.apply { height = tH }
        }
    }

    override fun updateTargetTranslationY(tY: Float) {
        inputLayout.translationY = tY
    }

    override fun referTranslationY(): Float {
        return inputLayout.translationY
    }

    override fun referHeight(): Float {
        return extendContainer.height.toFloat()
    }
}