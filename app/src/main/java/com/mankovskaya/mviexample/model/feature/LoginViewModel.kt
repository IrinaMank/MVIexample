package com.mankovskaya.mviexample.model.feature

import com.mankovskaya.mviexample.model.base.*
import com.mankovskaya.mviexample.model.mock.AuthMockService
import com.mankovskaya.mviexample.ui.widget.ErrorState
import com.mankovskaya.mviexample.ui.widget.StateAction
import io.reactivex.android.schedulers.AndroidSchedulers

data class LoginState(
    val email: String?,
    val emailError: String?,
    val emailCorrect: Boolean,
    val password: String?
) : State

sealed class LoginEvent : Event {
    data class Toast(val message: String) : LoginEvent()
}

sealed class LoginAction : Action {
    data class EmailChanged(val email: String) : LoginAction()
    object EmailInputFinished : LoginAction()
    data class PasswordChanged(val password: String) : LoginAction()
    object Login : LoginAction()
    object SuccessfulLogin : LoginAction()
    data class LoginError(val error: String) : LoginAction()
}

class LoginViewModel(private val authMockService: AuthMockService) :
    BaseStatefulViewModel<LoginState, LoginAction, LoginEvent>(
        LoginState(
            null,
            null,
            false,
            null
        )
    ) {
    override val stateReducer = LoginReducer()

    inner class LoginReducer : StateReducer<LoginState, LoginAction>() {
        override fun reduce(state: LoginState, action: LoginAction): LoginState {
            return when (action) {
                is LoginAction.EmailChanged -> {
                    state.copy(email = action.email, emailError = null, emailCorrect = false)
                }
                is LoginAction.EmailInputFinished -> {
                    //state.copy(emailError = "Please insert correct email", emailCorrect = false)
                    state.copy(emailCorrect = true, emailError = null)
                }
                is LoginAction.PasswordChanged -> {
                    state.copy(password = action.password)
                }
                is LoginAction.Login -> {
                    login(state.email, state.password)
                    sendStateAction(StateAction.ProgressStarted)
                    state
                }
                is LoginAction.SuccessfulLogin -> {
                    sendStateAction(StateAction.ProgressSuccess)
                    state.also { navigateNext() }
                }
                is LoginAction.LoginError -> {
                    //liveEvent.postValue(LoginEvent.Toast("Error sdksdskd"))
                    sendStateAction(StateAction.ErrorOccurred(ErrorState(action.error) {
                        login(
                            state.email,
                            state.password
                        )
                    }))
                    state
                }
            }
        }
    }

    private fun login(email: String?, password: String?) {
        authMockService
            .login(email, password)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    reactOnAction(LoginAction.SuccessfulLogin)
                }, {
                    reactOnAction(
                        LoginAction.LoginError(
                            it.localizedMessage ?: "Error"
                        )
                    )
                }
            ).subscribeUntilDestroy()
    }

    private fun navigateNext() {

    }

}