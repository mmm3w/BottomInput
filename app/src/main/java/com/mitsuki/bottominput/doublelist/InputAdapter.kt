package com.mitsuki.bottominput.doublelist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mitsuki.bottominput.R
import com.mitsuki.bottominput.hideSoftKeyboard

class InputAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var extend: String? = null
        set(value) {
            if (field != value) {
                if (value != null && field == null) {
                    notifyItemInserted(1)
                } else if (value == null && field != null) {
                    notifyItemRemoved(1)
                } else if (value != null && field != null) {
                    notifyItemChanged(1)
                }
                field = value
            }
        }


    var onSend: ((String) -> Unit)? = null


    override fun getItemCount(): Int {
        if (extend == null) return 1
        return 2
    }


    override fun getItemViewType(position: Int): Int {
        if (position == 0) return 0
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> InputViewHolder(parent).apply {
                inputView?.apply {
                    setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) extend = null
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

                extendView?.setOnClickListener {
                    clearEditInputStatus()
                    it.postDelayed({ extend = if (extend == null) "各种扩展菜单" else null }, 200)
                }
            }
            else -> ExtendViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    class InputViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_input, parent, false)
        ) {
        val inputView: EditText? = itemView.findViewById<EditText>(R.id.input_edit)
        val extendView: ImageView? = itemView.findViewById<ImageView>(R.id.input_extend)

        fun clearEditInputStatus() {
            inputView?.apply {
                hideSoftKeyboard()
                clearFocus()
            }
        }
    }

    class ExtendViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_extend, parent, false)
        )
}