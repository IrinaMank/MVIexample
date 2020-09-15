package com.mankovskaya.mviexample.model.feature

import com.mankovskaya.mviexample.core.mvvm.BaseStatefulViewModel
import com.mankovskaya.mviexample.core.mvvm.StateReducer
import com.mankovskaya.mviexample.model.data.chat.Message
import com.mankovskaya.mviexample.model.data.common.User
import com.mankovskaya.mviexample.model.ext.addMessage
import com.mankovskaya.mviexample.model.ext.mapMessages
import com.mankovskaya.mviexample.model.mock.MockChatCreator
import com.mankovskaya.mviexample.model.mock.MockConstants

data class ChatState(
    val messages: List<Message>,
    val shouldScrollToEnd: Boolean
)

sealed class ChatAction {
    data class MessageSent(val text: String) : ChatAction()
}

class ChatViewModel(mockChatCreator: MockChatCreator) :
    BaseStatefulViewModel<ChatState, ChatAction, Unit>(
        ChatState(
            mockChatCreator.getTextMessages(MockConstants.MESSAGES_COUNT).sortedBy { it.time }
                .mapMessages(),
            false
        )
    ) {
    override val stateReducer = ChatReducer()

    private val user = User(MockConstants.USER_AVATAR_RANDOM)

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
