package com.mankovskaya.mviexample.ui.chat

import androidx.recyclerview.widget.DiffUtil
import com.mankovskaya.mviexample.model.feature.Message


class MessageDiffUtil(
    private val newMessages: List<Message>,
    private val oldMessages: List<Message>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldMessages.size
    }

    override fun getNewListSize(): Int {
        return newMessages.size
    }

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldMessages[oldItemPosition] === newMessages[newItemPosition]
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return when {
            oldMessages[oldItemPosition]::class != newMessages[newItemPosition]::class -> false
            oldMessages[oldItemPosition].id == newMessages[newItemPosition].id -> true
            else -> false
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}