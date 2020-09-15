package com.mankovskaya.mviexample.model.ext

import com.mankovskaya.mviexample.model.data.chat.Message
import com.mankovskaya.mviexample.model.data.common.User
import org.joda.time.DateTime
import org.joda.time.Minutes
import java.util.*

fun List<Message.TextMessage>.mapMessages(): List<Message> {
    val result = mutableListOf<Message>()
    if (isEmpty()) return mutableListOf()
    var previousMessageDateTime: DateTime = this[0].time
    result.add(Message.DateMessage(previousMessageDateTime, UUID.randomUUID().toString()))
    result.add(this[0])
    this.drop(1).forEach { message ->
        if (Minutes.minutesBetween(previousMessageDateTime, message.time).minutes > 60) {
            result.add(Message.DateMessage(message.time, UUID.randomUUID().toString()))
            result.add(message)
            previousMessageDateTime = message.time
        } else {
            result.add(message)
        }
    }
    return result
}

fun List<Message>.addMessage(text: String, user: User): List<Message> {
    val message =
        Message.TextMessage(true, text, DateTime.now(), user, UUID.randomUUID().toString())
    val last = this.lastOrNull { it is Message.TextMessage } as? Message.TextMessage
        ?: return listOf(message)
    val result = this.toMutableList()
    if (Minutes.minutesBetween(last.time, message.time).minutes > 60) {
        result.add(Message.DateMessage(message.time, UUID.randomUUID().toString()))
        result.add(message)
    } else {
        result.add(message)
    }
    return result

}