package com.mindlab.intrest_agent_android.di

import androidx.datastore.core.Serializer
import androidx.paging.PagingConfig
import com.mindlab.intrest_agent_android.BuildConfig
import com.mindlab.intrest_agent_android.data.local.order.OrderLocal
import com.mindlab.intrest_agent_android.data.local.order.OrderLocalSerializer
import com.mindlab.intrest_agent_android.data.local.user.UserLocal
import com.mindlab.intrest_agent_android.data.local.user.UserLocalSerializer
import com.mindlab.intrest_agent_android.data.network.api.*
import com.mindlab.intrest_agent_android.data.network.interceptor.AuthInterceptor
import com.mindlab.intrest_agent_android.data.network.response.LoginResponse
import com.mindlab.intrest_agent_android.domain.utils.ResponseValidator
import com.mindlab.intrest_agent_android.utils.Constants
import com.mindlab.intrest_agent_android.utils.Constants.DEFAULT_PAGE_SIZE
import com.mindlab.intrest_agent_android.utils.Constants.DEFAULT_PRE_FETCH_DISTANCE
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
@Module(includes = [AppModule::class])
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @ExperimentalSerializationApi
    @Binds
    @Singleton
    abstract fun userLocalSerializer(): Serializer<UserLocal>

    @ExperimentalSerializationApi
    @Binds
    @Singleton
    abstract fun orderLocalSerializer(impl: OrderLocalSerializer): Serializer<OrderLocal>

    internal companion object {

        @Provides
        @Singleton
        fun client(
            authInterceptor: AuthInterceptor,
            httpLoggingInterceptor: HttpLoggingInterceptor,
        ): OkHttpClient =
            OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(authInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build()


        @Provides
        @Singleton
        fun retrofit(client: OkHttpClient, moshi: Moshi): Retrofit =
            Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

        @Provides
        @Singleton
        fun moshi(): Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        @Provides
        @Singleton
        fun httpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        @ExperimentalStdlibApi
        @Provides
        fun loginResponseAdapter(moshi: Moshi): JsonAdapter<LoginResponse> = moshi.adapter()

        @Singleton
        @Provides
        fun provideLoginApi(retrofit: Retrofit): LoginApi =
            retrofit.create(LoginApi::class.java)

        @Singleton
        @Provides
        fun provideOrdersApi(retrofit: Retrofit): OrdersApi =
            retrofit.create(OrdersApi::class.java)

        @Singleton
        @Provides
        fun provideHistoryApi(retrofit: Retrofit): HistoryApi =
            retrofit.create(HistoryApi::class.java)

        @Singleton
        @Provides
        fun provideCostApi(retrofit: Retrofit): CostApi =
            retrofit.create(CostApi::class.java)

        @Singleton
        @Provides
        fun provideMenuApi(retrofit: Retrofit): MenuApi =
            retrofit.create(MenuApi::class.java)

        @Singleton
        @Provides
        fun provideSettingsApi(retrofit: Retrofit): SettingsApi =
            retrofit.create(SettingsApi::class.java)

        @Singleton
        @Provides
        fun provideResponseValidator(retrofit: Retrofit): ResponseValidator =
            ResponseValidator(retrofit)

        @Singleton
        @Provides
        fun providePagingConfig(): PagingConfig =
            PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                prefetchDistance = DEFAULT_PRE_FETCH_DISTANCE,
                initialLoadSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false
            )
    }
}