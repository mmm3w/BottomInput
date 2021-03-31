package com.mitsuki.bottominput

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.mitsuki.bottominput.custom.BottomInputActivity
import com.mitsuki.bottominput.doublelist.DoubleListActivity
import com.mitsuki.bottominput.fulldialog.FullScreenDialogActivity

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

        findViewById<Button>(R.id.btn_fsd)?.setOnClickListener {
            startActivity(Intent(this, FullScreenDialogActivity::class.java))
        }

        findViewById<Button>(R.id.btn_bi)?.setOnClickListener {
            startActivity(Intent(this, BottomInputActivity::class.java))
        }


    }
}