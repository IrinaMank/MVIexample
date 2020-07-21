package com.mankovskaya.mviexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.mankovskaya.mviexample.databinding.ActivityMainBinding
import com.mankovskaya.mviexample.model.feature.LoginAction
import com.mankovskaya.mviexample.model.feature.LoginViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = loginViewModel
        binding.loginButton.setOnClickListener { loginViewModel.reactOnAction(LoginAction.Login) }
        binding.nameEditText.doOnTextChanged { text, _, _, _ ->
            loginViewModel.reactOnAction(LoginAction.EmailChanged(text?.toString() ?: ""))
        }
        binding.passwordEditText.doOnTextChanged { text, _, _, _ ->
            loginViewModel.reactOnAction(LoginAction.PasswordChanged(text?.toString() ?: ""))
        }
    }
}