package com.mindlab.intrest_agent_android.domain.utils

import com.mindlab.intrest_agent_android.data.network.response.ErrorResponse
import com.mindlab.intrest_agent_android.utils.Resource
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Alireza Nezami on 12/26/2021.
 */
class ResponseValidator
@Inject constructor(
    private val retrofit: Retrofit
) {

    fun <T> validateResponse(response: Response<T>): Resource<T> {
        try {
            val body = response.body()
            if (response.isSuccessful && body != null) {
                return Resource.success(body)
            } else if (response.code() == 204 || response.code() == 205) {
                return Resource.successWithoutContent(null)
            } else if (response.errorBody() != null) {
                val errorConverter: Converter<ResponseBody, ErrorResponse> =
                    retrofit.responseBodyConverter(
                        ErrorResponse::class.java, arrayOfNulls<Annotation>(0)
                    )
                try {
                    val error: ErrorResponse? = errorConverter.convert(response.errorBody()!!)
                    Timber.d("ErrorResponse  ${error?.message}")
                    error?.message?.let {
                        return Resource.error(
                            it, null
                        )
                    } ?: return Resource.error(
                        "unknown", null
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    return Resource.error(
                        e.message.toString(), null
                    )
                }
            } else return Resource.notFound(
                null, null
            )
        } catch (e: Exception) {
            Timber.d("exception is: ${e.message.toString()}")
            return Resource.error(
                e.message.toString(), null
            )
        }
    }
}