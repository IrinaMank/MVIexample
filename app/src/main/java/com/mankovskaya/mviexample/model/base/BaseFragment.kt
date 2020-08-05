package com.mankovskaya.mviexample.model.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe

abstract class BaseFragment<VM : BaseViewModel<*, *, *>> : Fragment() {
    abstract val fragmentViewModel: VM

    protected fun listenToEvents(listener: (Any?) -> Unit) {
        fragmentViewModel.liveEvent.observe(requireContext() as LifecycleOwner) { event ->
            listener(event)
        }
    }
}