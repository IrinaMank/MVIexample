package com.mankovskaya.mviexample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import com.mankovskaya.mviexample.R
import com.mankovskaya.mviexample.databinding.FragmentLoginBinding
import com.mankovskaya.mviexample.model.feature.LoginAction
import com.mankovskaya.mviexample.model.feature.LoginEvent
import com.mankovskaya.mviexample.model.feature.LoginViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        val view: View = binding.root
        binding.lifecycleOwner = this
        binding.viewModel = loginViewModel
        binding.stateViewModel = loginViewModel.stateViewModel
        binding.loginButton.setOnClickListener { loginViewModel.reactOnAction(LoginAction.Login) }
        binding.nameEditText.doOnTextChanged { text, _, _, _ ->
            loginViewModel.reactOnAction(LoginAction.EmailChanged(text?.toString() ?: ""))
        }
        binding.passwordEditText.doOnTextChanged { text, _, _, _ ->
            loginViewModel.reactOnAction(LoginAction.PasswordChanged(text?.toString() ?: ""))
        }
        loginViewModel.liveEvent.observe(requireContext() as LifecycleOwner) { event ->
            when (event) {
                is LoginEvent.Toast -> {
                    Toast.makeText(requireContext(), event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        return view
    }

}