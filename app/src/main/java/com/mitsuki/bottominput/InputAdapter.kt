package com.mitsuki.bottominput

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class InputAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var extend: String? = null
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
                inputView?.setOnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        onSend?.invoke(v.text.toString())
                        true
                    } else {
                        false
                    }
                }
                extendView?.setOnClickListener {
                    extend = if (extend == null) "各种扩展菜单" else null
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
    }

    class ExtendViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_extend, parent, false)
        )
}