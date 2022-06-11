package com.example.weatherapplication.di

import com.example.weatherapplication.model.ModelRepo
import com.example.weatherapplication.model.ModelRepository
import org.koin.dsl.module

/**
 * ModelModule
 *
 */
val modelModule = module {
    // Provide instance of [ModelRepo].
    single<ModelRepo> {
        ModelRepository(get())
    }
}