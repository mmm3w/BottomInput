package com.mitsuki.bottominput

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DoubleListActivity : AppCompatActivity() {

    private val mMessagePool by lazy { arrayListOf<String>() }

    private val mMainAdapter by lazy { MessageAdapter(mMessagePool) }

    private val mInput by lazy { InputAdapter() }

    private var mMessageInput: RecyclerView? = null

    private var mMessageView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_double_list)

        mMessageView = findViewById<RecyclerView>(R.id.message_pool)?.apply {
            layoutManager = LinearLayoutManager(this@DoubleListActivity)
            adapter = mMainAdapter
            addOnScrollListenerBy(onScrollStateChanged = { view: RecyclerView, newState: Int ->
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    (mMessageInput?.findViewHolderForAdapterPosition(0) as? InputAdapter.InputViewHolder)?.apply {
                        clearEditInputStatus()
                    }
                    mInput.extend = null
                }
            })
        }

        mMessageInput = findViewById<RecyclerView>(R.id.message_input)?.apply {
            layoutManager = object : LinearLayoutManager(this@DoubleListActivity) {
                override fun canScrollHorizontally(): Boolean = false
                override fun canScrollVertically(): Boolean = false
            }
            adapter = mInput
            isNestedScrollingEnabled = false
        }

        mInput.onSend = {
            mMessagePool.add(it)
            mMainAdapter.notifyItemInserted(mMessagePool.lastIndex)
            mMessageView?.smoothScrollToPosition(mMessagePool.lastIndex)
        }
    }

}