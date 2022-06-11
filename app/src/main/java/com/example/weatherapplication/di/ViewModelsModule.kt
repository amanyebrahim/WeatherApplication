package com.example.weatherapplication.di

import com.example.weatherapplication.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * ViewModelsModule
 *
 */

val viewModelsModule = module {

    viewModel {
        MainViewModel(get())
    }

}