package com.mankovskaya.mviexample.core.android

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import com.mankovskaya.mviexample.core.mvvm.BaseViewModel

abstract class BaseFragment<VM : BaseViewModel<*, *, *>> : Fragment() {
    abstract val fragmentViewModel: VM

    protected fun listenToEvents(listener: (Any?) -> Unit) {
        fragmentViewModel.liveEvent.observe(requireContext() as LifecycleOwner) { event ->
            listener(event)
        }
    }
}