package com.mankovskaya.mviexample.model.feature

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.mankovskaya.mviexample.model.base.BaseStatefulViewModel
import com.mankovskaya.mviexample.model.base.State
import com.mankovskaya.mviexample.model.base.StateReducer
import de.hdodenhof.circleimageview.CircleImageView
import org.joda.time.DateTime
import org.joda.time.Minutes
import java.util.*


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

data class User(
    val avatar: String
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
            mockChatCreator.getTextMessages(200).sortedBy { it.time }.mapMessages(),
            false
        )
    ) {
    override val stateReducer = ChatReducer()

    private val user = User("https://picsum.photos/id/237/200/300")

    inner class ChatReducer : StateReducer<ChatState, ChatAction>() {
        override fun reduce(state: ChatState, action: ChatAction): ChatState {
            return when (action) {
                is ChatAction.MessageSent -> {
                    state.copy(
                        messages = state.messages.addMessage(action.text, user),
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

@BindingAdapter("app:imgUrl")
fun setProfilePicture(imageView: CircleImageView, imgUrl: String) {
    Glide.with(imageView.context)
        .load(imgUrl)
        .dontAnimate()
        .into(imageView)
}
