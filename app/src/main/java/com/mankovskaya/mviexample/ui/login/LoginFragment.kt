package com.mankovskaya.mviexample.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.mankovskaya.mviexample.R
import com.mankovskaya.mviexample.core.android.BaseFragment
import com.mankovskaya.mviexample.databinding.FragmentLoginBinding
import com.mankovskaya.mviexample.model.feature.LoginAction
import com.mankovskaya.mviexample.model.feature.LoginEvent
import com.mankovskaya.mviexample.model.feature.LoginViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment<LoginViewModel>() {
    override val fragmentViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        val view: View = binding.root
        with(binding) {
            lifecycleOwner = this@LoginFragment
            viewModel = fragmentViewModel
            stateViewModel = fragmentViewModel.stateViewModel
            loginButton.setOnClickListener { fragmentViewModel.reactOnAction(LoginAction.Login) }
            emailEditText.setOnFocusChangeListener { _, isFocused ->
                if (!isFocused) {
                    fragmentViewModel.reactOnAction(LoginAction.EmailInputFinished)
                }
            }
            emailEditText.doOnTextChanged { text, _, _, _ ->
                fragmentViewModel.reactOnAction(LoginAction.EmailChanged(text?.toString() ?: ""))
            }
            passwordEditText.doOnTextChanged { text, _, _, _ ->
                fragmentViewModel.reactOnAction(LoginAction.PasswordChanged(text?.toString() ?: ""))
            }
        }
        listenToEvents { event ->
            when (event) {
                is LoginEvent.Toast -> {
                    Toast.makeText(requireContext(), event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        return view
    }

}