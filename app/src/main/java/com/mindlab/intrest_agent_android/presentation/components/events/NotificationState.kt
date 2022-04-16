package com.mindlab.intrest_agent_android.presentation.components.events

/**
 * Created by Alireza Nezami on 1/24/2022.
 */
sealed class NotificationState {
    data class Success(val message: String)

    data class Error(val message: String)
}
