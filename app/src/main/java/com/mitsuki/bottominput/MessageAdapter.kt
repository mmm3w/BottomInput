package com.mitsuki.bottominput

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(private val mData: List<String>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    override fun getItemCount(): Int = mData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.messageView?.text = mData[position]
    }

    class ViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        ) {
        val messageView: TextView? = itemView.findViewById(R.id.message_content)
    }
}