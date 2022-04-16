package com.mindlab.intrest_agent_android.di

import androidx.paging.PagingConfig
import com.mindlab.intrest_agent_android.data.network.api.*
import com.mindlab.intrest_agent_android.domain.cost.DeliverCostRepo
import com.mindlab.intrest_agent_android.domain.cost.DeliverCostRepoImpl
import com.mindlab.intrest_agent_android.domain.history.HistoryRepo
import com.mindlab.intrest_agent_android.domain.history.HistoryRepoImpl
import com.mindlab.intrest_agent_android.domain.login.LoginRepo
import com.mindlab.intrest_agent_android.domain.login.LoginRepoImpl
import com.mindlab.intrest_agent_android.domain.menu.MenuRepo
import com.mindlab.intrest_agent_android.domain.menu.MenuRepoImpl
import com.mindlab.intrest_agent_android.domain.orders.local.OrderLocalSource
import com.mindlab.intrest_agent_android.domain.orders.remote.OrdersRepo
import com.mindlab.intrest_agent_android.domain.orders.remote.OrdersRepoImpl
import com.mindlab.intrest_agent_android.domain.settings.SettingsRepo
import com.mindlab.intrest_agent_android.domain.settings.SettingsRepoImpl
import com.mindlab.intrest_agent_android.domain.user.UserLocalSource
import com.mindlab.intrest_agent_android.domain.utils.ResponseValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Alireza Nezami on 1/9/2022.
 */
@Module(includes = [DataModule::class, StoreModule::class])
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideOrdersRepository(
        ordersApi: OrdersApi,
        userLocalSource: UserLocalSource,
        orderLocalSource: OrderLocalSource,
        responseValidator: ResponseValidator,
        pagingConfig: PagingConfig
    ): OrdersRepo = OrdersRepoImpl(
        ordersApi,
        userLocalSource,
        orderLocalSource,
        responseValidator,
        pagingConfig
    )

    @Singleton
    @Provides
    fun provideHistoryRepository(
        ordersApi: OrdersApi,
        pagingConfig: PagingConfig,
        userLocalSource: UserLocalSource
    ): HistoryRepo = HistoryRepoImpl(
        ordersApi,
        pagingConfig,
        userLocalSource
    )

    @Singleton
    @Provides
    fun provideMenuRepository(
        menuApi: MenuApi,
        responseValidator: ResponseValidator,
    ): MenuRepo = MenuRepoImpl(
        menuApi,
        responseValidator
    )

    @Singleton
    @Provides
    fun provideLoginRepository(
        loginApi: LoginApi,
        userLocalSource: UserLocalSource,
        responseValidator: ResponseValidator,
    ): LoginRepo =
        LoginRepoImpl(
            loginApi,
            userLocalSource,
            responseValidator
        )

    @Singleton
    @Provides
    fun provideDeliverCostRepo(
        costApi: CostApi,
        userLocalSource: UserLocalSource,
        responseValidator: ResponseValidator,
    ): DeliverCostRepo =
        DeliverCostRepoImpl(
            costApi,
            userLocalSource,
            responseValidator,
        )

    @Singleton
    @Provides
    fun provideSettingsRepo(
        settingsApi: SettingsApi,
        userLocalSource: UserLocalSource,
        orderLocalSource: OrderLocalSource,
        responseValidator: ResponseValidator,
    ): SettingsRepo =
        SettingsRepoImpl(
            settingsApi,
            userLocalSource,
            orderLocalSource,
            responseValidator,
        )

}