package com.mindlab.intrest_agent_android.presentation

import android.app.Application
import com.mindlab.intrest_agent_android.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by Alireza Nezami on 12/11/2021.
 */
@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}