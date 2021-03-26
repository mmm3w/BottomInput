package com.mitsuki.bottominput

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DoubleListActivity : AppCompatActivity() {

    private val mMessagePool by lazy { arrayListOf<String>() }

    private val mMainAdapter by lazy { MessageAdapter(mMessagePool) }

    private val mInput by lazy { InputAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_double_list)

        findViewById<RecyclerView>(R.id.message_pool)?.apply {
            layoutManager = LinearLayoutManager(this@DoubleListActivity)
            adapter = mMainAdapter
        }

        findViewById<RecyclerView>(R.id.message_input)?.apply {
            layoutManager = LinearLayoutManager(this@DoubleListActivity)
            adapter = mInput
        }
    }

}