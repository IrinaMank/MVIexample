package com.mankovskaya.mviexample.model.feature

import com.mankovskaya.mviexample.R
import com.mankovskaya.mviexample.core.android.ResourceManager
import com.mankovskaya.mviexample.core.mvvm.BaseStatefulViewModel
import com.mankovskaya.mviexample.core.mvvm.StateReducer
import com.mankovskaya.mviexample.model.mock.AuthMockService
import com.mankovskaya.mviexample.model.mock.MockConstants.PASSWORD_MIN_LENGTH
import com.mankovskaya.mviexample.ui.widget.ErrorState
import com.mankovskaya.mviexample.ui.widget.StateAction
import io.reactivex.android.schedulers.AndroidSchedulers

data class SignUpFirstStepState(
    val email: String?,
    val emailError: String?,
    val emailCorrect: Boolean,
    val password: String?,
    val passwordError: String?,
    val passwordCorrect: Boolean
)

sealed class SignUpFirstStepAction {
    data class EmailChanged(val email: String) : SignUpFirstStepAction()
    data class PasswordChanged(val password: String) : SignUpFirstStepAction()
    object SignUp : SignUpFirstStepAction()
    object SuccessfulSignUp : SignUpFirstStepAction()
    data class SignUpError(val error: String) : SignUpFirstStepAction()
}

class SignUpFirstStepViewModel(
    private val authMockService: AuthMockService,
    private val resourceManager: ResourceManager
) : BaseStatefulViewModel<SignUpFirstStepState, SignUpFirstStepAction, Unit>(
    SignUpFirstStepState(
        null,
        null,
        false,
        null,
        null,
        false
    )
) {
    override val stateReducer: StateReducer<SignUpFirstStepState, SignUpFirstStepAction> =
        FirstStepStateReducer()

    inner class FirstStepStateReducer :
        StateReducer<SignUpFirstStepState, SignUpFirstStepAction>() {
        override fun reduce(
            state: SignUpFirstStepState,
            action: SignUpFirstStepAction
        ): SignUpFirstStepState {
            return when (action) {
                is SignUpFirstStepAction.EmailChanged -> {
                    state.copy(email = action.email, emailError = null, emailCorrect = false)
                }
                is SignUpFirstStepAction.PasswordChanged -> {
                    state.copy(password = action.password)
                }
                is SignUpFirstStepAction.SignUp -> {
                    if (state.emailCorrect && state.passwordCorrect) {
                        signUp(state.email!!, state.password!!)
                        sendStateAction(StateAction.ProgressStarted)
                        state
                    } else {
                        val emailError = validateEmail(state.email).takeIf { it }
                            .run { resourceManager.getString(R.string.sign_up_email_error) }
                        val passwordError = validatePassword(state.password)?.getResourceId()
                            ?.run { resourceManager.getString(this) }
                        state.copy(
                            emailError = emailError,
                            passwordError = passwordError
                        )
                    }
                }
                is SignUpFirstStepAction.SuccessfulSignUp -> {
                    sendStateAction(StateAction.ProgressSuccess)
                    state
                }
                is SignUpFirstStepAction.SignUpError -> {
                    sendStateAction(StateAction.ErrorOccurred(ErrorState(action.error) {
                        signUp(
                            state.email,
                            state.password
                        )
                    }))
                    state
                }
            }
        }
    }

    private fun signUp(email: String?, password: String?) {
        authMockService
            .login(email, password)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    reactOnAction(SignUpFirstStepAction.SuccessfulSignUp)
                }, {
                    reactOnAction(
                        SignUpFirstStepAction.SignUpError(
                            it.localizedMessage ?: "Error"
                        )
                    )
                }
            ).subscribeUntilDestroy()
    }

    private val emailRegex =
        "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

    private fun validateEmail(email: String?): Boolean =
        email?.matches(Regex.fromLiteral(emailRegex)) ?: false

    private fun validatePassword(password: String?): PasswordError? =
        when {
            password == null || password.isBlank() -> PasswordError.PasswordEmpty
            password.length < PASSWORD_MIN_LENGTH -> PasswordError.PasswordTooShort
            !password.matches(Regex.fromLiteral("^(?=.*[A-Za-z])")) -> PasswordError.PasswordNoLetters
            !password.matches(Regex.fromLiteral("(?=.*?[0-9])")) -> PasswordError.PasswordNoNumbers
            else -> null
        }

    fun PasswordError.getResourceId(): Int =
        when (this) {
            PasswordError.PasswordTooShort -> R.string.sign_up_password_error_too_short
            PasswordError.PasswordEmpty -> R.string.sign_up_password_error_empty
            PasswordError.PasswordNoLetters -> R.string.sign_up_password_error_no_letters
            PasswordError.PasswordNoNumbers -> R.string.sign_up_password_error_no_numbers
        }

    sealed class PasswordError {
        object PasswordEmpty : PasswordError()
        object PasswordTooShort : PasswordError()
        object PasswordNoLetters : PasswordError()
        object PasswordNoNumbers : PasswordError()
    }

}