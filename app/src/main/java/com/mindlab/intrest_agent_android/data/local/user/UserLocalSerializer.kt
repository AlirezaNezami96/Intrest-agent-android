@file:Suppress("BlockingMethodInNonBlockingContext")

package com.mindlab.intrest_agent_android.data.local.user

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.protobuf.ProtoBuf

import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@ExperimentalSerializationApi
class UserLocalSerializer @Inject constructor(
    private val aead: Aead
) : Serializer<UserLocal> {
    override suspend fun readFrom(input: InputStream): UserLocal {
        return try {
            val encryptedInput = input.readBytes()

            val decryptedInput = if (encryptedInput.isNotEmpty()) {
                aead.decrypt(encryptedInput, null)
            } else {
                encryptedInput
            }

            ProtoBuf.decodeFromByteArray(UserLocal.serializer(), decryptedInput)
        } catch (e: SerializationException) {
            throw CorruptionException("Error deserializing proto", e)
        }
    }

    override suspend fun writeTo(user: UserLocal, output: OutputStream) {
        val byteArray = ProtoBuf.encodeToByteArray(UserLocal.serializer(), user)
        val encryptedBytes = aead.encrypt(byteArray, null)

        output.write(encryptedBytes)
    }

    override val defaultValue: UserLocal =
        UserLocal()
}
