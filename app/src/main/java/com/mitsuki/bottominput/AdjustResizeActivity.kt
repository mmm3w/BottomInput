package com.mitsuki.bottominput

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdjustResizeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_mode)

        title = "AdjustResize"

        findViewById<TextView>(R.id.main_text)?.text = "AdjustResize"
    }
}