package com.mitsuki.bottominput.custom

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.mitsuki.bottominput.R
import com.mitsuki.bottominput.custom.helper.FragmentHelper
import com.mitsuki.bottominput.custom.helper.InputMeasurePopupWindow
import com.mitsuki.bottominput.custom.helper.TransAnimate
import com.mitsuki.bottominput.hideSoftKeyboard

class BottomInputActivity : AppCompatActivity() {

    private var mExtendView: View? = null
    private var mEmojiView: View? = null
    private var mInputView: EditText? = null

    private val fragmentHelper = FragmentHelper(2) {
        when (it) {
            0 -> MenuFragment()
            1 -> EmojiFragment()
            else -> throw  IllegalStateException()
        }
    }

    private val transHelper by lazy { TransAnimate(MyReferValue(this)) }
    private lateinit var measure: InputMeasurePopupWindow

    private var mKeyboardHeight = 0

    private var mMenu: Menu? = null
        set(value) {
            if (value != field) {
                field = value
                when (value) {
                    Menu.Normal -> {
                        //展示normal
                        mInputView?.hideSoftKeyboard()
                        supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            replace(
                                R.id.input_extend_container,
                                fragmentHelper.obtainFragment(0),
                                null
                            )
                            transHelper.transHeight(if (mKeyboardHeight == 0) 500 else mKeyboardHeight)
                        }
                    }
                    Menu.Emoji -> {
                        //展示emoji
                        mInputView?.hideSoftKeyboard()
                        supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            replace(
                                R.id.input_extend_container,
                                fragmentHelper.obtainFragment(1),
                                null
                            )
                            transHelper.transHeight(1000)
                        }
                    }
                    Menu.KeyBoard -> {
                        transHelper.transHeight(if (mKeyboardHeight == 0) 500 else mKeyboardHeight)
                    }
                    null -> {
                        //隐藏菜单
                        transHelper.transHeight(0)
                    }
                    else -> {

                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_input)

        mInputView = findViewById<EditText>(R.id.input_edit)

        measure = InputMeasurePopupWindow(this).apply {
            onKeyBoardEvent = { isShow: Boolean, keyboardHeight: Int ->
                if (mKeyboardHeight != keyboardHeight) {
                    //在设置中修改输入法的高度后可能会有两次该回调，最后一次的高度是正确的
                    mKeyboardHeight = keyboardHeight
                    if (isShow && mMenu == Menu.KeyBoard) {
                        transHelper.transHeight(mKeyboardHeight)
                    }
                }
                if (isShow) {
                    mMenu = Menu.KeyBoard
                } else {
                    if (mMenu == Menu.KeyBoard) mMenu = null
                }
            }
        }

        mEmojiView = findViewById<View>(R.id.input_emoji)?.apply {
            setOnClickListener { mMenu = Menu.Emoji }
        }

        mExtendView = findViewById<View>(R.id.input_extend)?.apply {
            setOnClickListener { mMenu = Menu.Normal }
        }

    }


    enum class Menu {
        Normal, Emoji, KeyBoard
    }


}