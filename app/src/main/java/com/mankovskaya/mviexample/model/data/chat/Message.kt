package com.mankovskaya.mviexample.model.data.chat

import com.mankovskaya.mviexample.model.data.common.User
import org.joda.time.DateTime

sealed class Message(open val id: String) {
    data class TextMessage(
        val isMine: Boolean,
        val text: String,
        val time: DateTime,
        val owner: User,
        override val id: String
    ) : Message(id)

    data class DateMessage(val date: DateTime, override val id: String) : Message(id)
}
