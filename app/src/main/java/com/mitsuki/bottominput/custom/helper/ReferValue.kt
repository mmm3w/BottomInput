package com.mitsuki.bottominput.custom.helper

import android.view.View

interface ReferValue {

    fun translationYTarget(): View

    fun updateTargetHeight(tH: Int)

    fun updateTargetTranslationY(tY: Float)

    fun referTranslationY(): Float

    fun referHeight(): Float

    val currentDisplayHeight get() = referHeight() - referTranslationY()
}