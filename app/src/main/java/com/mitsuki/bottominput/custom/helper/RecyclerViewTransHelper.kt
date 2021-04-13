package com.mitsuki.bottominput.custom.helper

import android.animation.Animator
import android.animation.ObjectAnimator
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewTransHelper(
    lifecycleOwner: LifecycleOwner,
    private val recyclerView: RecyclerView
) :
    RecyclerView.AdapterDataObserver(), LifecycleObserver {

    private var lastReferHeight = 0

    init {
        recyclerView.adapter?.registerAdapterDataObserver(this)
        lifecycleOwner.lifecycle.addObserver(this)
    }

    private fun lastBottom(): Int {
        return recyclerView.adapter?.let { adapter ->
            if (adapter.itemCount <= 0) {
                recyclerView.height
            } else {
                (recyclerView.layoutManager as? LinearLayoutManager)?.let { layoutManager ->
                    val view =
                        layoutManager.findViewByPosition(layoutManager.findLastVisibleItemPosition())
                    recyclerView.height - (view?.bottom ?: 0)
                } ?: throw  IllegalStateException()
            }
        } ?: throw  IllegalStateException()
    }

    fun transAnimator(referHeight: Int): Animator {
        lastReferHeight = referHeight
        return ObjectAnimator.ofFloat(
            recyclerView,
            "translationY",
            recyclerView.translationY,
            (lastBottom() - referHeight).coerceAtMost(0).toFloat()
        )
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        recyclerView.postDelayed({ transAnimator(lastReferHeight).start() }, 200)
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        recyclerView.postDelayed({ transAnimator(lastReferHeight).start() }, 200)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onLifeDestroy() {
        recyclerView.adapter?.unregisterAdapterDataObserver(this)
    }
}