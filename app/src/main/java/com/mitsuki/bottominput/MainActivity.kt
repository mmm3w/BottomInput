package com.mitsuki.bottominput

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_adr)?.setOnClickListener {
            startActivity(Intent(this, AdjustResizeActivity::class.java))
        }

        findViewById<Button>(R.id.btn_adp)?.setOnClickListener {
            startActivity(Intent(this, AdjustPanActivity::class.java))
        }

        findViewById<Button>(R.id.btn_dl)?.setOnClickListener {
            startActivity(Intent(this, DoubleListActivity::class.java))
        }


    }
}