package com.mindlab.intrest_agent_android.utils

/**
 * Created by Alireza Nezami on 11/15/2021.
 */
data class Resource<out T>(val status: Status, val data: T?, val message: String?)  {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }
        fun <T> successWithoutContent(data: T?): Resource<T> {
            return Resource(Status.SUCCESS_NO_CONTENT, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> logOut( data: T?): Resource<T> {
            return Resource(Status.LOG_OUT, null,null)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> notFound(msg: String?, data: T?): Resource<T> {
            return Resource(Status.NOT_FOUND, data, msg)
        }

        fun <T> noNetwork(msg: String?, data: T?): Resource<T> {
            return Resource(Status.NO_NETWORK, data, msg)
        }
        fun <T> shouldLogout( data: T?): Resource<T> {
            return Resource(Status.SHOULD_LOGOUT,data,null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    NOT_FOUND,
    NO_NETWORK,
    LOG_OUT,
    SUCCESS_NO_CONTENT,
    SHOULD_LOGOUT
}