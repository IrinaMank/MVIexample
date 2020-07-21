package com.mankovskaya.mviexample.system.di

import com.mankovskaya.mviexample.model.feature.LoginViewModel
import com.mankovskaya.mviexample.model.mock.AuthMockService
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { AuthMockService() }
    viewModel { LoginViewModel(get()) }
}