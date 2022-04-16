package com.mindlab.intrest_agent_android.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.mindlab.intrest_agent_android.data.local.order.OrderLocal
import com.mindlab.intrest_agent_android.data.local.user.UserLocal
import com.mindlab.intrest_agent_android.domain.orders.local.OrderLocalSource
import com.mindlab.intrest_agent_android.domain.orders.local.OrderLocalSourceImpl
import com.mindlab.intrest_agent_android.domain.user.UserLocalSource
import com.mindlab.intrest_agent_android.domain.user.UserLocalSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Alireza Nezami on 1/19/2022.
 */
@Module(includes = [DataModule::class])
@InstallIn(SingletonComponent::class)
class StoreModule {

    @Provides
    @Singleton
    fun aead(@ApplicationContext context: Context): Aead {
        AeadConfig.register()

        return AndroidKeysetManager
            .Builder()
            .withSharedPref(context, KEYSET_NAME, PREF_FILE_NAME)
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
            .getPrimitive(Aead::class.java)
    }

    @Provides
    @Singleton
    fun userLocalSource(dataStore: DataStore<UserLocal>): UserLocalSource =
        UserLocalSourceImpl(
            dataStore
        )

    @Provides
    @Singleton
    fun provideOrderLocalSource(dataStore: DataStore<OrderLocal>): OrderLocalSource =
        OrderLocalSourceImpl(
            dataStore
        )

    @Provides
    @Singleton
    fun dataStore(
        @ApplicationContext applicationContext: Context,
        serializer: Serializer<UserLocal>
    ): DataStore<UserLocal> = DataStoreFactory.create(
        serializer = serializer,
        produceFile = { applicationContext.dataStoreFile("user") },
    )

    @Provides
    @Singleton
    fun provideOrderDataStore(
        @ApplicationContext applicationContext: Context,
        serializer: Serializer<OrderLocal>
    ): DataStore<OrderLocal> = DataStoreFactory.create(
        serializer = serializer,
        produceFile = { applicationContext.dataStoreFile("order") },
    )


    companion object{
        private const val KEYSET_NAME = "keyset"
        private const val PREF_FILE_NAME = "keyset_prefs"
        private const val MASTER_KEY_URI = "android-keystore://master_key"
    }
}