package com.mindlab.intrest_agent_android.presentation.components.events

/**
 * Created by Alireza Nezami on 1/24/2022.
 */
sealed class ViewModelState<C> {

    data class Loading<C>(val refreshing: Boolean = false) : ViewModelState<C>()

    data class SuccessAction<C>(val message: String?,val random:Int = 0) : ViewModelState<C>()

    data class Error<C>(val message: String?,val random:Int = 0) : ViewModelState<C>()

    data class Loaded<C>(
        val content: C,
        val message: String? = null,
        val refreshing: Boolean = false
    ) : ViewModelState<C>()

    fun loading(): Boolean = this is Loading && this.refreshing

    fun isSuccessful(): Boolean = this is SuccessAction

    fun refreshingContent(): Boolean = when (this) {
        is Loading -> this.refreshing
        is Loaded<*> -> this.refreshing
        else -> false
    }

    fun containAnyError(): Boolean = message() != null


    fun content(): C? = when (this) {
        is Loaded -> this.content
        else -> null
    }

    fun withContentIfLoaded(newContent: (C) -> C): ViewModelState<C> = when (this) {
        is Loaded -> Loaded(
            content = newContent(this.content),
            message = this.message,
            refreshing = this.refreshing
        )
        is Loading -> this
        is Error -> this
        is SuccessAction -> this
    }

    fun message(): String? = when (this) {
        is Error -> message
        is Loaded -> message
        is Loading -> null
        is SuccessAction -> message
    }

    fun hasContent(): Boolean = content() != null

    fun markAsRefreshing(refreshing: Boolean = false): ViewModelState<C> = when (this) {
        is Loading -> Loading(refreshing)
        is Error -> if (refreshing) Loading(refreshing) else this
        is Loaded -> copy(refreshing = refreshing)
        is SuccessAction->Loading(refreshing)
    }

    fun <E>withError(error:String): ViewModelState<C> = when (this) {
        is Loading -> Error(error)
        is Error -> Error(error)
        is Loaded -> Loaded(message = error, content = this.content, refreshing = this.refreshing)
        is SuccessAction -> Error(error)
    }

    fun removeError(): ViewModelState<C> = when (this) {
        is Loading -> this
        is Error -> Loading()
        is Loaded -> Loaded(content = this.content, refreshing = this.refreshing, message = null)
        is SuccessAction-> Loading()
    }
}
