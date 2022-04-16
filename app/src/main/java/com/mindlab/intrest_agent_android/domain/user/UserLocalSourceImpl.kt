package com.mindlab.intrest_agent_android.domain.user

import androidx.datastore.core.DataStore
import com.mindlab.intrest_agent_android.data.local.user.UserLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class UserLocalSourceImpl
@Inject constructor(
    private val dataStore: DataStore<UserLocal>
) : UserLocalSource {
    override fun user(): Flow<UserLocal?> = dataStore.data
        .onEach { Timber.d("User=$it") }
        .map { if (it != userLocalNull) it else null }
        .catch { cause: Throwable ->
            if (cause is IOException) {
                emit(null)
            } else {
                throw cause
            }
        }

    override suspend fun save(userLocal: UserLocal?) {
        dataStore.updateData {
            if (userLocal === null) userLocalNull
            else userLocal
        }
    }

    override suspend fun updateToken(userLocal: UserLocal) {
        dataStore.updateData {
            UserLocal(
                username = it.username,
                accessToken = userLocal.accessToken,
                refreshToken = userLocal.refreshToken,
                expiresIn = userLocal.expiresIn,
                id = it.id,
                image = it.image,
                restaurantName = it.restaurantName
            )
        }
    }

    override suspend fun updateRestaurant(userLocal: UserLocal) {
        dataStore.updateData {
            UserLocal(
                username = it.username,
                accessToken = it.accessToken,
                refreshToken = it.refreshToken,
                expiresIn = it.expiresIn,
                id = userLocal.id,
                restaurantName = userLocal.restaurantName,
                image = it.image,
            )
        }
    }

    override suspend fun updateImage(userLocal: UserLocal) {
        dataStore.updateData {
            UserLocal(
                username = it.username,
                accessToken = it.accessToken,
                refreshToken = it.refreshToken,
                expiresIn = it.expiresIn,
                id = it.id,
                restaurantName = it.restaurantName,
                image = userLocal.image,
            )
        }
    }

    override suspend fun clear() {
        dataStore.updateData { userLocal ->
            userLocal.clear()
        }
    }


    private val userLocalNull: UserLocal = UserLocal("", "", "", 0)
}
