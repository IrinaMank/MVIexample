package com.mankovskaya.mviexample.model.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mankovskaya.mviexample.ui.widget.StateAction
import com.mankovskaya.mviexample.ui.widget.StateViewModel

abstract class BaseViewModel<State, Action, Event>(
    private val initialState: State
) : ViewModel() {
    private val stateRelay: MutableLiveData<State> = MutableLiveData(initialState)
    abstract val stateReducer: StateReducer<State, Action>
    val liveEvent = LiveEvent<Event>()

    fun getStateRelay(): LiveData<State> = stateRelay

    fun reactOnAction(action: Action) {
        stateRelay.value = stateReducer.reduce(stateRelay.value ?: initialState, action)
    }

    fun postEvent(event: Event) {
        liveEvent.postValue(event)
    }

}

abstract class BaseStatefulViewModel<State, Action, Event>(initialState: State) :
    BaseViewModel<State, Action, Event>(initialState) {
    val stateViewModel = StateViewModel()

    protected fun sendStateAction(action: StateAction) {
        stateViewModel.reactOnAction(action)
    }
}