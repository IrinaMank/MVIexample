package com.mankovskaya.mviexample.ui.chat

import androidx.recyclerview.widget.DiffUtil
import com.mankovskaya.mviexample.R
import com.mankovskaya.mviexample.model.base.BaseRecyclerViewAdapter
import com.mankovskaya.mviexample.model.feature.Message

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

    open fun setItems(items: List<Message>) {
        val oldItems = this.items.toList()
        clear()
        addItems(items, false)
        val diffResult = DiffUtil.calculateDiff(MessageDiffUtil(items, oldItems))
        diffResult.dispatchUpdatesTo(this)
    }

    open fun clear() = items.clear()

    open fun addItems(newItems: List<Message>, notify: Boolean) {
        if (newItems.isNotEmpty()) items.addAll(newItems)
        if (notify) notifyDataSetChanged()
    }

}