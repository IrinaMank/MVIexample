package com.mankovskaya.mviexample.model.feature

import com.mankovskaya.mviexample.model.base.BaseStatefulViewModel
import com.mankovskaya.mviexample.model.base.State
import com.mankovskaya.mviexample.model.base.StateReducer
import org.joda.time.DateTime
import org.joda.time.Minutes
import java.util.*


sealed class Message(open val id: String) {
    data class TextMessage(
        val isMine: Boolean,
        val text: String,
        val time: DateTime,
        override val id: String
    ) : Message(id)

    data class DateMessage(val date: DateTime, override val id: String) : Message(id)
}

data class MessageSection(
    val date: DateTime,
    val messages: List<Message>
)

data class ChatState(
    val messages: List<Message>,
    val shouldScrollToEnd: Boolean
) : State

sealed class ChatAction {
    data class MessageSent(val text: String) : ChatAction()
}

class ChatViewModel(
    private val mockChatCreator: MockChatCreator
) :
    BaseStatefulViewModel<ChatState, ChatAction, Unit>(
        ChatState(
            mockChatCreator.getTextMessages(20).sortedBy { it.time }.mapMessages(),
            false
        )
    ) {
    override val stateReducer = ChatReducer()

    inner class ChatReducer : StateReducer<ChatState, ChatAction>() {
        override fun reduce(state: ChatState, action: ChatAction): ChatState {
            return when (action) {
                is ChatAction.MessageSent -> {
                    state.copy(
                        messages = state.messages.addMessage(action.text),
                        shouldScrollToEnd = true
                    )
                }
            }
        }

    }

}

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

fun List<Message>.addMessage(text: String): List<Message> {
    val message = Message.TextMessage(true, text, DateTime.now(), UUID.randomUUID().toString())
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