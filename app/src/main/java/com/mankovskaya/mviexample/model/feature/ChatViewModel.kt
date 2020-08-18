package com.mankovskaya.mviexample.model.feature

import com.mankovskaya.mviexample.model.base.BaseStatefulViewModel
import com.mankovskaya.mviexample.model.base.State
import com.mankovskaya.mviexample.model.base.StateReducer
import org.joda.time.DateTime
import org.joda.time.Minutes


sealed class Message {
    data class TextMessage(
        val text: String,
        val time: DateTime
    ) : Message()

    data class DateMessage(val date: DateTime) : Message()
}

data class ChatState(
    val messages: List<Message>
) : State

class ChatViewModel(
    private val mockChatCreator: MockChatCreator
) :
    BaseStatefulViewModel<ChatState, Unit, Unit>(
        ChatState(
            mockChatCreator.getTextMessages(50).sortedBy { it.time }.mapMessages()
        )
    ) {
    override val stateReducer = ChatReducer()

    inner class ChatReducer : StateReducer<ChatState, Unit>() {
        override fun reduce(state: ChatState, action: Unit): ChatState {
            return state
        }

    }

}

fun List<Message.TextMessage>.dkfj(): Map<DateTime, List<Message.TextMessage>> {
    val hashMap = mutableMapOf<DateTime, MutableList<Message.TextMessage>>()
    if (isEmpty()) return hashMapOf()
    var previousMessageDateTime: DateTime = this[0].time
    hashMap[previousMessageDateTime] = mutableListOf(this[0])
    this.drop(1).forEach { message ->
        if (Minutes.minutesBetween(previousMessageDateTime, message.time).minutes > 60) {
            hashMap[message.time] = mutableListOf(message)
            previousMessageDateTime = message.time
        } else {
            hashMap[previousMessageDateTime]?.add(message)
        }
    }
    return hashMap

}


fun List<Message.TextMessage>.mapMessages(): List<Message> {
    val result = mutableListOf<Message>()
    if (isEmpty()) return mutableListOf()
    var previousMessageDateTime: DateTime = this[0].time
    result.add(Message.DateMessage(previousMessageDateTime))
    result.add(this[0])
    this.drop(1).forEach { message ->
        if (Minutes.minutesBetween(previousMessageDateTime, message.time).minutes > 60) {
            result.add(Message.DateMessage(message.time))
            result.add(message)
            previousMessageDateTime = message.time
        } else {
            result.add(message)
        }
    }
    return result
}