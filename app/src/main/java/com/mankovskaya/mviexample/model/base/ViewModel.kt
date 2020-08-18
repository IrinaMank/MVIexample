package com.mankovskaya.mviexample.model.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mankovskaya.mviexample.ui.widget.StateAction
import com.mankovskaya.mviexample.ui.widget.StateViewModel
import io.reactivex.disposables.Disposable

abstract class BaseViewModel<State, Action, Event>(
    private val initialState: State
) : ViewModel() {
    private val stateRelay: MutableLiveData<State> = MutableLiveData(initialState)
    protected abstract val stateReducer: StateReducer<State, Action>
    private val disposables = Disposables()
    val liveEvent = LiveEvent<Event>()

    override fun onCleared() {
        disposables.disposeAll()
        super.onCleared()
    }

    fun getStateRelay(): LiveData<State> = stateRelay

    fun reactOnAction(action: Action) {
        stateRelay.value = stateReducer.reduce(stateRelay.value ?: initialState, action)
    }

    protected fun postEvent(event: Event) {
        liveEvent.postValue(event)
    }

    protected fun Disposable.subscribeUntilDestroy() {
        disposables.add(this)
    }

}

abstract class BaseStatefulViewModel<State, Action, Event>(initialState: State) :
    BaseViewModel<State, Action, Event>(initialState) {
    val stateViewModel = StateViewModel()

    protected fun sendStateAction(action: StateAction) {
        stateViewModel.reactOnAction(action)
    }
}