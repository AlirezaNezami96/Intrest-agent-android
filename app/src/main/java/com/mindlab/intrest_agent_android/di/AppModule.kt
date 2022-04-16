package com.mindlab.intrest_agent_android.di

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.mindlab.intrest_agent_android.presentation.MyApp
import com.mindlab.intrest_agent_android.utils.AppDispatchers
import com.mindlab.intrest_agent_android.utils.AppDispatchersImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Created by Alireza Nezami on 12/13/2021.
 */

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class AppCoroutineScope

@Module
@InstallIn(SingletonComponent::class)
 abstract class AppModule {

    @Binds
    @Singleton
    abstract fun appDispatchers(impl: AppDispatchersImpl): AppDispatchers

    internal companion object {

        @Singleton
        @Provides
        fun provideApplicationContext(@ApplicationContext app: Context): Context {
            return app as MyApp
        }

        @Provides
        @Singleton
        fun appScope(appDispatchers: AppDispatchers) =
            CoroutineScope(appDispatchers.io + SupervisorJob())
    }
}