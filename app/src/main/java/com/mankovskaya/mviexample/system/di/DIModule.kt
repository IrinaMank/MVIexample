package com.mankovskaya.mviexample.system.di

import com.mankovskaya.mviexample.model.base.ResourceManager
import com.mankovskaya.mviexample.model.feature.ChatViewModel
import com.mankovskaya.mviexample.model.feature.LoginViewModel
import com.mankovskaya.mviexample.model.feature.MockChatCreator
import com.mankovskaya.mviexample.model.feature.SignUpFirstStepViewModel
import com.mankovskaya.mviexample.model.mock.AuthMockService
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { AuthMockService() }
    single { MockChatCreator() }
    single { ResourceManager(androidContext()) }
    viewModel { LoginViewModel(get()) }
    viewModel { ChatViewModel(get()) }
    viewModel { SignUpFirstStepViewModel(get(), get()) }
}