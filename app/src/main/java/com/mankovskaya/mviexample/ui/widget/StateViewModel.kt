package com.mankovskaya.mviexample.ui.widget

import com.mankovskaya.mviexample.core.mvvm.BaseViewModel
import com.mankovskaya.mviexample.core.mvvm.StateReducer

data class ProgressState(
    val isLoading: Boolean,
    val error: ErrorState?
)

data class ErrorState(
    val message: String,
    val buttonAction: (() -> Unit)?
)

sealed class StateAction {
    object ProgressStarted : StateAction()
    object ProgressSuccess : StateAction()
    data class ErrorOccurred(val error: ErrorState) : StateAction()
    object ButtonClicked : StateAction()
}

class StateViewModel : BaseViewModel<ProgressState, StateAction, Unit>(
    ProgressState(false, null)
) {
    override val stateReducer: StateReducer<ProgressState, StateAction> = ProgressStateReducer()

    inner class ProgressStateReducer : StateReducer<ProgressState, StateAction>() {
        override fun reduce(state: ProgressState, action: StateAction): ProgressState {
            return when (action) {
                is StateAction.ProgressStarted -> state.copy(isLoading = true)
                is StateAction.ProgressSuccess -> state.copy(isLoading = false)
                is StateAction.ErrorOccurred -> state.copy(isLoading = false, error = action.error)
                is StateAction.ButtonClicked -> {
                    state.error?.buttonAction?.invoke()
                    state.copy(isLoading = true, error = null)
                }
            }
        }

    }

}