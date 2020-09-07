package com.mankovskaya.mviexample.model.feature

import androidx.lifecycle.MutableLiveData
import org.joda.time.DateTime
import java.util.*
import kotlin.random.Random

class MockChatCreator {

    fun getTextMessages(count: Int): List<Message.TextMessage> {
        return generateSequence {
            Message.TextMessage(
                Random.nextBoolean(),
                getRandomString(Random.nextInt(100)),
                DateTime.now().plusMinutes(Random.nextInt(0, 100)),
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