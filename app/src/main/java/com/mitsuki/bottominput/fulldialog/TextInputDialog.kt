package com.mitsuki.bottominput.fulldialog

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.view.isVisible
import com.mitsuki.bottominput.R
import com.mitsuki.bottominput.hideSoftKeyboard

class TextInputDialog(context: Context) : Dialog(context, R.style.InputDialog) {

    var onSend: ((String) -> Unit)? = null

    private var mEditView: EditText? = null
    private var extendContent: View? = null

    private var mLastDiff = 0

    var isShowExtend = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_input)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        mEditView = findViewById<EditText>(R.id.input_edit)?.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    //可以通过focus控制，也可以通过OnLayoutChange回调控制
                    if (isShowExtend) {
                        extendContent?.isVisible = false
                        isShowExtend = false
                    }
                }
            }
            setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    onSend?.invoke(v.text.toString())
                    v.text = ""
                    true
                } else {
                    false
                }
            }
        }

        extendContent = findViewById<View>(R.id.extend_content)

        findViewById<View>(R.id.input_extend)?.apply {
            setOnClickListener {
                if (isShowExtend) {
                    isShowExtend = false
                    extendContent?.isVisible = false
                    dismiss()
                } else {
                    isShowExtend = true
                    mEditView?.hideSoftKeyboard()
                    mEditView?.clearFocus()
                    //延迟显示一定程度上提示交互动画体验
                    extendContent?.postDelayed({
                        extendContent?.isVisible = true
                    }, 200)
                }
            }
        }

        findViewById<View>(R.id.input_outside)?.setOnClickListener {
            if (it.id != R.id.input_layout) dismiss()
        }

        findViewById<View>(R.id.input_layout).apply {
            addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                Rect().also { rect ->
                    window?.decorView?.apply {
                        getWindowVisibleDisplayFrame(rect)
                        val heightDifference = rootView.height - rect.bottom
                        if (heightDifference <= 0 && mLastDiff > 0) {
                            dismiss()
                        }
                        mLastDiff = heightDifference
                    }
                }
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
        mLastDiff = 0
    }


    override fun show() {
        super.show()
        isShowExtend = false
        extendContent?.isVisible = false
        mEditView?.apply {
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }
    }
}