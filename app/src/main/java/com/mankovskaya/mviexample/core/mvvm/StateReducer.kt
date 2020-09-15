package com.mankovskaya.mviexample.core.mvvm


abstract class StateReducer<State, Action> {

    abstract fun reduce(state: State, action: Action): State
}
