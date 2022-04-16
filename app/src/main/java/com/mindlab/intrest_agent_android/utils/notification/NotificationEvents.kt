package com.mindlab.intrest_agent_android.utils.notification

/**
 * Created by Alireza Nezami on 1/5/2022.
 */

sealed class NotificationEvents() {
    data class ChangeTab(val status: NotificationStatus) : NotificationEvents()

}