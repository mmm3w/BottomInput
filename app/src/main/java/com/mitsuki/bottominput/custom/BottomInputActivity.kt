package com.mitsuki.bottominput.custom

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.mitsuki.bottominput.R
import com.mitsuki.bottominput.hideSoftKeyboard

/**
 * 默认状态
 *
 * 弹出键盘状态
 *
 * 弹出菜单状态
 *
 * 弹出emoji状态
 *
 * 各个状态之间可以随意切换
 *
 * 其中一个麻烦的时候各个fragment之间切换时候的高度变换
 * 尝试先不好了输入法顶起的情况，就是直接是
 *
 *
 * 关于高度控制的两种方案，
 * 一种是由fragment内部决定
 * 一种是外部决定，内部直接使用match
 *
 * 或者直接三种，
 * 适配输入法的高度
 * 外部指定高度
 * 内部指定高度
 *
 * 外部指定则是遵循外部，内部指定则外部使用内部的高度
 * 适配输入法的高度也是变相的指定外部的高度
 *
 * 大致流程是
 *
 *
 *
 *
 *
 */

class BottomInputActivity : AppCompatActivity() {

    private var mInputLayout: LinearLayout? = null
    private var mExtendView: View? = null
    private var mEmojiView: View? = null
    private var mInputView: EditText? = null

    private var mLastDiff = 0

    private lateinit var measure: InputMeasurePopupWindow


    /**
     * Extended menu
     *
     *
     */
    private var mMenu: Menu? = null
        set(value) {
            if (value != field) {
                when (value) {
                    Menu.Normal -> {
                        //展示normal
                        supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            replace(
                                R.id.input_extend_container,
                                MenuFragment::class.java,
                                null
                            )
                        }
                    }
                    Menu.Emoji -> {
                        //展示emoji
                        supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            replace(
                                R.id.input_extend_container,
                                EmojiFragment::class.java,
                                null
                            )
                        }
                    }
                    null -> {
                        //隐藏菜单

                    }
                    else -> {

                    }
                }
                field = value
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_input)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        measure = InputMeasurePopupWindow(this)

        findViewById<View>(R.id.input_extend_container)?.apply {
            layoutParams = layoutParams.apply {
                height = 526
            }
        }

        mInputView = findViewById<EditText>(R.id.input_edit)?.apply {

        }

//        mEmojiView = findViewById<View>(R.id.input_emoji)?.apply {
//            setOnClickListener {
//                mInputView?.hideSoftKeyboard()
//                mMenu = Menu.Emoji
//            }
//        }
//
        mExtendView = findViewById<View>(R.id.input_extend)?.apply {
            setOnClickListener {
                mInputView?.hideSoftKeyboard()
//                mMenu = Menu.Normal
            }
        }

        findViewById<View>(R.id.input_hn)?.setOnClickListener {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }


        findViewById<View>(R.id.input_hnl)?.setOnClickListener {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }

        findViewById<View>(R.id.input_default)?.setOnClickListener {
            window.decorView.systemUiVisibility = 0
        }

    }


    enum class Menu {
        Normal, Emoji
    }
}