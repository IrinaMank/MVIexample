package com.mankovskaya.mviexample.model.base


abstract class StateReducer<State, Action> {

    abstract fun reduce(state: State, action: Action): State
}