package com.mankovskaya.mviexample.model.feature

import com.mankovskaya.mviexample.model.base.Action
import com.mankovskaya.mviexample.model.base.BaseViewModel
import com.mankovskaya.mviexample.model.base.State
import com.mankovskaya.mviexample.model.base.StateReducer
import com.mankovskaya.mviexample.model.mock.AuthMockService
import io.reactivex.android.schedulers.AndroidSchedulers

data class LoginState(
    val email: String?,
    val password: String?,
    val loading: Boolean,
    val error: String?
) : State

sealed class LoginAction : Action {
    data class EmailChanged(val email: String) : LoginAction()
    data class PasswordChanged(val password: String) : LoginAction()
    object Login : LoginAction()
    object SuccessfulLogin : LoginAction()
    data class LoginError(val error: String) : LoginAction()
}

class LoginViewModel(private val authMockService: AuthMockService) :
    BaseViewModel<LoginState, LoginAction>(
        LoginState(
            "first email",
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
                    state.copy(email = action.email)
                }
                is LoginAction.PasswordChanged -> {
                    state.copy(password = action.password)
                }
                is LoginAction.Login -> {
                    authMockService
                        .login(state.email, state.password)
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
                        )
                    state.copy(loading = true)
                }
                is LoginAction.SuccessfulLogin -> {
                    state.copy(loading = false).also { navigateNext() }
                }
                is LoginAction.LoginError -> {
                    state.copy(loading = false, error = action.error)
                }
            }
        }
    }

    private fun navigateNext() {

    }

}