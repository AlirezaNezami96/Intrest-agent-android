@file:Suppress("BlockingMethodInNonBlockingContext")

package com.mindlab.intrest_agent_android.data.local.order

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import com.mindlab.intrest_agent_android.data.local.user.UserLocal
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.protobuf.ProtoBuf

import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@ExperimentalSerializationApi
class OrderLocalSerializer @Inject constructor(
    private val aead: Aead
) : Serializer<OrderLocal> {
    override suspend fun readFrom(input: InputStream): OrderLocal {
        return try {
            val encryptedInput = input.readBytes()

            val decryptedInput = if (encryptedInput.isNotEmpty()) {
                aead.decrypt(encryptedInput, null)
            } else {
                encryptedInput
            }

            ProtoBuf.decodeFromByteArray(OrderLocal.serializer(), decryptedInput)
        } catch (e: SerializationException) {
            throw CorruptionException("Error deserializing proto", e)
        }
    }

    override suspend fun writeTo(order: OrderLocal, output: OutputStream) {
        val byteArray = ProtoBuf.encodeToByteArray(OrderLocal.serializer(), order)
        val encryptedBytes = aead.encrypt(byteArray, null)

        output.write(encryptedBytes)
    }

    override val defaultValue: OrderLocal =
        OrderLocal()
}
