package com.mindlab.intrest_agent_android.utils

/**
 * Created by Alireza Nezami on 1/5/2022.
 */
enum class OrderContentType {
    INCOMING,
    IN_PROGRESS,
    DELIVER,
    HISTORY,
}

object DeliverType{
    val PICKUP =2
    val DELIVER =1
}

enum class OrderActionType {
    CONTACT,
    ACCEPT,
    REJECT,
    PRICE_ADJ,
    OPEN_DELAY,
    PREPARE,
    DELIVER,
}
