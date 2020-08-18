package com.mankovskaya.mviexample.ui.chat

import com.mankovskaya.mviexample.R
import com.mankovskaya.mviexample.model.base.BaseRecyclerViewAdapter
import com.mankovskaya.mviexample.model.feature.Message

class ChatAdapter : BaseRecyclerViewAdapter() {

    private val items: MutableList<Message> = mutableListOf()

    override fun getLayoutIdForPosition(position: Int) = when (items[position]) {
        is Message.DateMessage -> R.layout.item_chat_date
        else -> R.layout.item_chat_message
    }

    override fun getViewModel(position: Int): Any? = items[position]

    override fun getItemCount() = items.size

    open fun setItems(items: List<Message>, notify: Boolean) {
        clear()
        addItems(items, notify)
    }

    open fun clear() = items.clear()

    open fun addItems(newItems: List<Message>, notify: Boolean) {
        if (newItems.isNotEmpty()) items.addAll(newItems)
        if (notify) notifyDataSetChanged()
    }

}