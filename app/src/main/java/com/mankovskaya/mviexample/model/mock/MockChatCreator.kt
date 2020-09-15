package com.mankovskaya.mviexample.model.mock

import androidx.lifecycle.MutableLiveData
import com.mankovskaya.mviexample.model.data.chat.Message
import com.mankovskaya.mviexample.model.data.common.User
import org.joda.time.DateTime
import java.util.*
import kotlin.random.Random

class MockChatCreator {

    fun getTextMessages(count: Int): List<Message.TextMessage> {
        return generateSequence {
            val isMine = Random.nextBoolean()
            val randomUser = Random.nextBoolean()

            val user = when {
                isMine -> "https://picsum.photos/id/237/200/300"
                randomUser -> "https://picsum.photos/id/1023/200/300"
                else -> "https://picsum.photos/id/1014/200/300"
            }
            val text = when {
                isMine -> "Mine text"
                randomUser -> "green"
                else -> "blue"
            }
            Message.TextMessage(
                isMine,
                text,
                DateTime.now().plusMinutes(Random.nextInt(0, 100)),
                User(user),
                UUID.randomUUID().toString()
            )
        }.take(count).toList()
    }

    fun getNotRandomTextMessages(pageNumber: Int, pageSize: Int): List<Message.TextMessage> {
        var count = pageNumber * pageSize
        return generateSequence {
            Message.TextMessage(
                Random.nextBoolean(),
                count.toString(),
                DateTime.now().plusMinutes(count),
                User("https://picsum.photos/200/300"),
                UUID.randomUUID().toString()
            ).also { count-- }
        }.take(pageNumber * pageSize).toList()
    }

    fun listenToNewMessages() = MutableLiveData<Message.TextMessage>()

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}