package com.mitsuki.bottominput.fulldialog

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mitsuki.bottominput.R
import com.mitsuki.bottominput.screenWidth

class FullScreenDialogActivity : AppCompatActivity() {

    private val inputDialog by lazy {
        TextInputDialog(this).apply {
            onSend =
                { Toast.makeText(this@FullScreenDialogActivity, it, Toast.LENGTH_SHORT).show() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_dialog)

        title = "FullScreenDialog"

        findViewById<View>(R.id.input_layout)?.apply {
            //show dialog
            setOnClickListener {
                inputDialog.window?.attributes?.apply {
                    width = screenWidth
                    inputDialog.window?.attributes = this
                }
                inputDialog.setCancelable(true)
                inputDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
                inputDialog.show()
            }
        }

    }
}