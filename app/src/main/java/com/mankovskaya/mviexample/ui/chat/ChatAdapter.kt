package com.mankovskaya.mviexample.ui.chat

import androidx.recyclerview.widget.DiffUtil
import com.mankovskaya.mviexample.R
import com.mankovskaya.mviexample.core.android.BaseRecyclerViewAdapter
import com.mankovskaya.mviexample.model.data.chat.Message

class ChatAdapter : BaseRecyclerViewAdapter() {

    private val items: MutableList<Message> = mutableListOf()

    override fun getLayoutIdForPosition(position: Int): Int {
        val item = items[position]
        return when {
            item is Message.DateMessage -> R.layout.item_chat_date
            item is Message.TextMessage && item.isMine -> R.layout.item_chat_message_outcome
            else -> R.layout.item_chat_message_income
        }
    }

    override fun getViewModel(position: Int): Any? = items[position]

    override fun getItemCount() = items.size

    fun setItems(items: List<Message>) {
        val oldItems = this.items.toList()
        clear()
        addItems(items, false)
        val diffResult = DiffUtil.calculateDiff(MessageDiffUtil(items, oldItems))
        diffResult.dispatchUpdatesTo(this)
    }

    fun clear() = items.clear()

    fun addItems(newItems: List<Message>, notify: Boolean) {
        if (newItems.isNotEmpty()) items.addAll(newItems)
        if (notify) notifyDataSetChanged()
    }

}