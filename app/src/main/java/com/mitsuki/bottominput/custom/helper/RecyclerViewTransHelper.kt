package com.mitsuki.bottominput.custom.helper

import android.animation.Animator
import android.animation.ObjectAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewTransHelper(private val recyclerView: RecyclerView) :
    RecyclerView.AdapterDataObserver() {

    private fun lastBottom(): Int {
        return recyclerView.adapter?.let { adapter ->
            if (adapter.itemCount <= 0) {
                0
            } else {
                (recyclerView.layoutManager as? LinearLayoutManager)?.let { layoutManager ->
                    val view =
                        layoutManager.findViewByPosition(layoutManager.findLastVisibleItemPosition())
                    recyclerView.height - (view?.bottom ?: 0)
                } ?: throw  IllegalStateException()
            }
        } ?: throw  IllegalStateException()
    }

    fun transAnimator(referHeight: Int): Animator? {
        return ObjectAnimator.ofFloat(
            recyclerView,
            "translationY",
            recyclerView.translationY,
            -(referHeight - lastBottom()).coerceAtLeast(0).toFloat()
        )
    }
}