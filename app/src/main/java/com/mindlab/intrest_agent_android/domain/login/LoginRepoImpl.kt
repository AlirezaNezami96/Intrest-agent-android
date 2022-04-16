package com.mindlab.intrest_agent_android.domain.login

import com.mindlab.intrest_agent_android.data.local.user.UserLocal
import com.mindlab.intrest_agent_android.data.model.Menu
import com.mindlab.intrest_agent_android.data.model.User
import com.mindlab.intrest_agent_android.data.network.api.LoginApi
import com.mindlab.intrest_agent_android.data.network.body.LoginBody
import com.mindlab.intrest_agent_android.domain.user.UserLocalSource
import com.mindlab.intrest_agent_android.domain.utils.ResponseValidator
import com.mindlab.intrest_agent_android.utils.Resource
import com.mindlab.intrest_agent_android.utils.Status
import kotlinx.coroutines.flow.*
import javax.inject.Inject


/**
 * Created by Alireza Nezami on 12/13/2021.
 */
class LoginRepoImpl
@Inject constructor(
    private val loginApi: LoginApi,
    private val userLocalSource: UserLocalSource,
    private val responseValidator: ResponseValidator,
) : LoginRepo {

    private val userFlow by lazy {
        userLocalSource.user()
            .map { userLocal ->
                userLocal?.let {
                    User(
                        it.accessToken,
                        it.refreshToken,
                        it.expiresIn
                    )
                }
            }
            .distinctUntilChanged()
    }

    override fun user() = userFlow

    override suspend fun login(loginBody: LoginBody): Flow<Resource<Any>> = flow {
        val loginResponse = loginApi.login(loginBody)
        responseValidator.validateResponse(loginResponse).apply {
            if (this.status != Status.SUCCESS) emit(this)
            else {
                loginResponse.body()?.let { loginResponse ->
                    userLocalSource.save(
                        UserLocal(
                            accessToken = loginResponse.accessToken,
                            refreshToken = loginResponse.refreshToken,
                            expiresIn = loginResponse.expiresIn,
                            username = loginBody.username
                        )
                    )

                    getCurrentUser().collect { userResource ->
                        emit(userResource)
                    }
                }
            }
        }
    }

    override suspend fun saveRestaurant(item: Menu): Flow<Resource<Any>> = flow {
        userLocalSource.updateRestaurant(
            UserLocal(
                restaurantName = item.title,
                id = item.id,
            )
        )

        getLogo().collect { logoResource ->
            emit(logoResource)
        }
    }

    private suspend fun getCurrentUser(): Flow<Resource<Any>> = flow {
        val response = loginApi.currentUser()
        responseValidator.validateResponse(response).apply {
            if (this.status != Status.SUCCESS) emit(this)
            else if (this.data != null && this.data.user.restaurants.size > 1) {
                emit(this)
            } else {
                this.data?.let { userResponse ->
                    userLocalSource.updateRestaurant(
                        UserLocal(
                            restaurantName = userResponse.user.restaurants[0].name,
                            id = userResponse.user.restaurants[0].id,
                            isLoggedIn = true
                        )
                    )

                    getLogo().collect { logoResource ->
                        emit(logoResource)
                    }
                }
            }
        }
    }

    private suspend fun getLogo(): Flow<Resource<Any>> = flow {
        userLocalSource.user().collect { user ->
            user?.let {
                val response = loginApi.getLogo(user.id)
                responseValidator.validateResponse(response).apply {
                    if (this.status != Status.SUCCESS) emit(this)
                    else {
                        this.data?.let { response ->
                            if (response.embedded.logos != null && response.embedded.logos.size > 0) {
                                userLocalSource.updateImage(
                                    UserLocal(
                                        image = response.embedded.logos[0].logoImage
                                    )
                                )
                                emit(Resource.success(true))
                            } else emit(Resource.success(true))
                        } ?: emit(Resource.success(true))
                    }
                }
            }
        }

    }


}