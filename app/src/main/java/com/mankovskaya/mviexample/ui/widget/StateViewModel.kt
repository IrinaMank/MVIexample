package com.mankovskaya.mviexample.ui.widget

import com.mankovskaya.mviexample.model.base.Action
import com.mankovskaya.mviexample.model.base.BaseViewModel
import com.mankovskaya.mviexample.model.base.State
import com.mankovskaya.mviexample.model.base.StateReducer

data class ProgressState(
    val isLoading: Boolean,
    val error: ErrorState?
) : State

data class ErrorState(
    val message: String,
    val buttonAction: (() -> Unit)?
)

sealed class StateAction : Action {
    object ProgressStarted : StateAction()
    object ProgressSuccess : StateAction()
    data class ErrorOccurred(val error: ErrorState) : StateAction()
    object ButtonClicked : StateAction()
}

class StateViewModel : BaseViewModel<ProgressState, StateAction, Nothing>(
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