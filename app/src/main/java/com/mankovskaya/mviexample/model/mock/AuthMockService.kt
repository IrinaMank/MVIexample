package com.mankovskaya.mviexample.model.mock

import io.reactivex.Completable
import java.util.concurrent.TimeUnit

class AuthMockService {

    fun login(email: String?, password: String?): Completable {
        return Completable.error(Throwable(message = "Message string")).delay(5, TimeUnit.SECONDS)
    }
}