package com.mindlab.intrest_agent_android.utils.extention

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.mindlab.intrest_agent_android.data.network.response.IncomingResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.util.*

fun Context.toast(messageId: Int) {
    Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show()
}

fun <T> SavedStateHandle.getStateFlow(
    scope: CoroutineScope,
    key: String,
    initialValue: T
): MutableStateFlow<T> {
    val liveData = getLiveData(key, initialValue)
    val stateFlow = MutableStateFlow(initialValue)

    val observer = Observer<T> { value -> if (value != stateFlow.value) stateFlow.value = value }
    liveData.observeForever(observer)

    stateFlow.onCompletion {
        withContext(Dispatchers.Main.immediate) {
            liveData.removeObserver(observer)
        }
    }.onEach { value ->
        withContext(Dispatchers.Main.immediate) {
            if (liveData.value != value) liveData.value = value
        }
    }.launchIn(scope)

    return stateFlow
}

fun Int.toSerialNumber(): String {
    val length = this.toString().length
    var result: String = this.toString()
    for (i in 1..(4 - length)) {
        result = "0$result"
    }
    return result
}

fun String.substringDate(): String {
    return this
        .substringAfter("T", "")
        .substringBefore(".", "")
        .substring(0, 5)
}

fun List<IncomingResponse.Content.OrderItem>.calcTotalPrice(): Double {
    var total = 0.0
    forEach {
        total += (it.itemPrice)
    }
    return total
}

fun Int.showExceeded(): String {
    return if (this > 99) "+99"
    else this.toString()
}

fun String.toTimeStamp(): String {
    return this.replace(" ", "T")
}

fun String.toFullDate(): String {
    return try {
        this
            .replace("-", "/")
            .replace("T", " - ")
            .substring(0, 18)
    } catch (e: Exception) {
        this
    }
}

fun <T> MutableList<T>.addToFirst(item: T) {
    this.add(0, item)
}

fun <T> MutableList<out T>.removeAndReturn(element: T?): List<T> {
    this.remove(element)
    return this
}

fun <T> MutableList<T>.addAndReturn(element: T): List<T> {
    this.add(0, element)
    return this
}

fun String?.removeUnderlines(): String {
    return this.toString().replace("_", " ").lowercase(Locale.getDefault())
}

fun String.toReadableDuration(): String = this.substring(2)
    .lowercase(Locale.getDefault()).replace(Regex("[hms](?!\$)")) { "${it.value} " }
    .replace("h", " Hour")
    .replace("m", " Minute")
    .replace("s", " Second")

fun Double.toPrice(): String = "$$this"

public inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
    while (true) {
        val prevValue = value
        val nextValue = function(prevValue)
        if (compareAndSet(prevValue, nextValue)) {
            return
        }
    }
}

fun String.fromBase64toBitmap(): Bitmap {
    val imageBytes = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}
