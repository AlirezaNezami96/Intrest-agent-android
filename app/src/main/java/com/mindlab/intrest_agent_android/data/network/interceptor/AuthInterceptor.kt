package com.mindlab.intrest_agent_android.data.network.interceptor


import com.mindlab.intrest_agent_android.data.local.user.UserLocal
import com.mindlab.intrest_agent_android.data.network.api.LoginApi
import com.mindlab.intrest_agent_android.data.network.api.LoginApi.Factory.CUSTOM_HEADER
import com.mindlab.intrest_agent_android.data.network.api.LoginApi.Factory.NO_AUTH
import com.mindlab.intrest_agent_android.data.network.body.RefreshTokenBody
import com.mindlab.intrest_agent_android.domain.user.UserLocalSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.net.HttpURLConnection.HTTP_OK
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import javax.inject.Inject
import javax.inject.Provider


class AuthInterceptor @Inject constructor(
    private val userLocalSource: UserLocalSource,
    private val apiService: Provider<LoginApi>,
) : Interceptor {
    private val mutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request().also { Timber.d("[1] $it") }

        if (NO_AUTH in req.headers.values(CUSTOM_HEADER)) {
            return chain.proceedWithToken(req, null)
        }

//        if (WITH_ID in req.headers.values(CUSTOM_HEADER)) {
//            val restaurantId =
//                runBlocking {
//                    userLocalSource.user().first()?.id
//                }
//            val originalHttpUrl: HttpUrl = req.url
//            val newUrl = originalHttpUrl.newBuilder()
//                .host(originalHttpUrl.toUrl().toString().replace(DEFAULT_RESTAURANT_ID, restaurantId.toString()))
//                .build()
//
//            req = req.newBuilder()
//                .url(newUrl)
//                .build()
//        }


        val token =
            runBlocking {
                userLocalSource.user().first()?.accessToken
            }.also { Timber.d("[2] $req $it") }
        val res = chain.proceedWithToken(req, token)

        if (res.code != HTTP_UNAUTHORIZED || token == null) {
            return res
        }

        Timber.d("[3] $req")

        val newToken: String? = runBlocking {
            mutex.withLock {
                val user =
                    userLocalSource.user().first().also { Timber.d("[4] $req $it") }
                val maybeUpdatedToken = user?.accessToken

                when {
                    user == null || maybeUpdatedToken == null -> null.also { Timber.d("[5-1] $req") } // already logged out!
                    maybeUpdatedToken != token -> maybeUpdatedToken.also { Timber.d("[5-2] $req") } // refreshed by another request
                    else -> {
                        Timber.d("[5-3] $req")

                        val refreshTokenRes =
                            apiService.get()
                                .refreshToken(RefreshTokenBody(user.refreshToken))
                                .also {
                                    Timber.d("[6] $req $it")
                                }

                        val code = refreshTokenRes.code()
                        if (code == HTTP_OK) {
                            refreshTokenRes.body()?.let {
                                userLocalSource.updateToken(
                                    UserLocal(
                                        accessToken = it.accessToken,
                                        refreshToken = it.refreshToken,
                                        expiresIn = it.expiresIn,
                                    )
                                )
                            }
                            refreshTokenRes.body()?.accessToken

                        } else if (code == HTTP_UNAUTHORIZED) {
                            Timber.d("[7-2] $req")
                            userLocalSource.save(null)
                            null
                        } else {
                            Timber.d("[7-3] $req")
                            null
                        }
                    }
                }
            }
        }

        return if (newToken !== null) chain.proceedWithToken(req, newToken) else res
    }

    private fun Interceptor.Chain.proceedWithToken(req: Request, token: String?): Response =
        req.newBuilder()
            .apply {
                if (token !== null) {
                    addHeader("Authorization", "Bearer $token")
                    addHeader("Accept", "*/*")
                }
            }
            .removeHeader(CUSTOM_HEADER)
            .build()
            .let(::proceed)
}
