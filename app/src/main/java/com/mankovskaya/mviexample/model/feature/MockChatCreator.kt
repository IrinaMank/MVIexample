package com.mankovskaya.mviexample.model.feature

import org.joda.time.DateTime
import java.util.*
import kotlin.random.Random

class MockChatCreator {

    fun getTextMessages(count: Int): List<Message.TextMessage> {
        return generateSequence {
            Message.TextMessage(
                getRandomString(Random.nextInt(100)),
                DateTime.now().plusMinutes(Random.nextInt(0, 100)),
                UUID.randomUUID().toString()
            )
        }.take(count).toList()
    }

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}