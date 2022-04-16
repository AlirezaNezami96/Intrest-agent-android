package com.mindlab.intrest_agent_android.utils

/**
 * Created by Alireza Nezami on 12/11/2021.
 */
object Constants {
    const val INVALID_CHAR= "Invalid character "

    /**
     * Max char lengths
     */
    const val MAX_USERNAME_CHAR_LENGTH = 12
    const val MAX_MESSAGE_CHAR_LENGTH = Int.MAX_VALUE
    const val MAX_NUMBER_CHAR_LENGTH = 6

    const val RESTAURANT_LIST = "User"
    const val EMAIL_TOO_SHORT_ERROR = "Username is too short"
    const val PASSWORD_TOO_SHORT_ERROR = "Password is too short"

    /**
     * Price
     */
    const val PRICE_CAN_NOT_BE_EMPTY = "Price can't be empty"
    const val PRICE_CAN_NOT_BE_ZERO = "Price can't be zero"
    const val PRICE_CAN_NOT_BE_NEGATIVE = "Price can't be negative"

    /**
     * Price
     */
    const val RADIUS_CAN_NOT_BE_EMPTY = "Radius can't be empty"
    const val RADIUS_CAN_NOT_BE_ZERO = "Radius can't be zero"
    const val RADIUS_CAN_NOT_BE_NEGATIVE = "Radius can't be negative"

    /**
     * Time
     */
    const val TIME_CAN_NOT_BE_EMPTY = "Time can't be empty"
    const val TIME_CAN_NOT_BE_ZERO = "Time can't be zero"
    const val TIME_CAN_NOT_BE_NEGATIVE = "Time can't be negative"

    /**
     * Tax
     */
    const val TAX_CAN_NOT_BE_EMPTY = "Tax can't be empty"
    const val TAX_CAN_NOT_BE_NEGATIVE = "Tax can't be negative"

    const val ORDER_STATUS_ACCEPT = "ACCEPT_RESTAURANT"
    const val ORDER_STATUS_REJECT = "REJECT_RESTAURANT"
    const val ORDER_STATUS_PREPARED = "PREPARED"
    const val ORDER_STATUS_DELIVER_TO_COURIER = "DELIVER_TO_COURIER"

    const val DEFAULT_PAGE_INDEX = 0
    const val DEFAULT_PAGE_SIZE = 10
    const val DEFAULT_PRE_FETCH_DISTANCE = 5

    const val BASE_URL = "http://185.231.180.191:8088/"




}