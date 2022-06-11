package com.example.weatherapplication.root

import androidx.multidex.MultiDexApplication
import com.example.weatherapplication.di.apiModule
import com.example.weatherapplication.di.modelModule
import com.example.weatherapplication.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import kotlin.time.ExperimentalTime


class App : MultiDexApplication() {
    //region Lifecycle methods
    override fun onCreate() {
        super.onCreate()

        // Initialize Koin.
        initKoin()
    }
    //endregion
    private fun initKoin() {
        startKoin {
            // Use the Android context given there.
            androidContext(this@App)

            // Module list.
            modules(getKoinModules())
        }
    }

    /**
     * Get a list of dependency injection used in application.
     */
    @OptIn(ExperimentalTime::class)
    private fun getKoinModules(): List<Module> {
        return listOf(
            apiModule,
            modelModule,
            viewModelsModule
        )
    }

    //endregion
}