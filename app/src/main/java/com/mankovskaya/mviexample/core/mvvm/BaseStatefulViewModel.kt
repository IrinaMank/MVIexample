package com.mankovskaya.mviexample.core.mvvm

import com.mankovskaya.mviexample.ui.widget.StateAction
import com.mankovskaya.mviexample.ui.widget.StateViewModel

abstract class BaseStatefulViewModel<State, Action, Event>(initialState: State) :
    BaseViewModel<State, Action, Event>(initialState) {
    val stateViewModel = StateViewModel()

    protected fun sendStateAction(action: StateAction) {
        stateViewModel.reactOnAction(action)
    }

}
