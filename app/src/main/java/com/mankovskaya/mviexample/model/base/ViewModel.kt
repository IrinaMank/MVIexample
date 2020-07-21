package com.mankovskaya.mviexample.model.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<State, Action>(
    private val initialState: State
) : ViewModel() {
    private val stateRelay: MutableLiveData<State> = MutableLiveData(initialState)
    abstract val stateReducer: StateReducer<State, Action>

    fun getStateRelay(): LiveData<State> = stateRelay

    fun reactOnAction(action: Action) {
        stateRelay.value = stateReducer.reduce(stateRelay.value ?: initialState, action)
    }

}