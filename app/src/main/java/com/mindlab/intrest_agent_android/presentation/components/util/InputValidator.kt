package com.mindlab.intrest_agent_android.presentation.components.util

import com.mindlab.intrest_agent_android.utils.Constants


object InputValidator {

    fun getNameErrorIdOrNull(input: String): String? {
        return when {
            input.length < 5 -> Constants.EMAIL_TOO_SHORT_ERROR
            else -> null
        }
    }

    fun getPasswordErrorIdOrNull(input: String): String? {
        return when {
            input.length < 1 -> Constants.PASSWORD_TOO_SHORT_ERROR
            else -> null
        }
    }

    fun getPriceAdjErrorIdOrNull(input: String): String? {
        return try {
            when {
                input.isEmpty() -> Constants.PRICE_CAN_NOT_BE_EMPTY
                input.toInt() == 0 -> Constants.PRICE_CAN_NOT_BE_ZERO
                input.toInt() < 0 -> Constants.PRICE_CAN_NOT_BE_NEGATIVE
                else -> null
            }
        } catch (e: Exception) {
            Constants.INVALID_CHAR
        }
    }

    fun getLowerRadiusErrorIdOrNull(input: String): String? {
        return try {
            when {
                input.isEmpty() -> Constants.RADIUS_CAN_NOT_BE_EMPTY
                input.toInt() < 0 -> Constants.RADIUS_CAN_NOT_BE_NEGATIVE
                else -> null
            }
        } catch (e: Exception) {
            Constants.INVALID_CHAR
        }
    }

    fun getLowerUpperErrorIdOrNull(input: String): String? {
        return try {
            when {
                input.isEmpty() -> Constants.RADIUS_CAN_NOT_BE_EMPTY
                input.toInt() == 0 -> Constants.RADIUS_CAN_NOT_BE_ZERO
                input.toInt() < 0 -> Constants.RADIUS_CAN_NOT_BE_NEGATIVE
                else -> null
            }
        } catch (e: Exception) {
            Constants.INVALID_CHAR
        }
    }

    fun getTimeErrorIdOrNull(input: String): String? {
        return try {
            when {
                input.isEmpty() -> Constants.TIME_CAN_NOT_BE_EMPTY
                input.toInt() == 0 -> Constants.TIME_CAN_NOT_BE_ZERO
                input.toInt() < 0 -> Constants.TIME_CAN_NOT_BE_NEGATIVE
                else -> null
            }
        } catch (e: Exception) {
            Constants.INVALID_CHAR
        }

    }

    fun getTaxRateErrorIdOrNull(input: String): String? {
        return try {
            when {
                input.isEmpty() -> Constants.TAX_CAN_NOT_BE_EMPTY
                input.toInt() < 0 -> Constants.TAX_CAN_NOT_BE_NEGATIVE
                else -> null
            }
        } catch (e: Exception) {
            Constants.INVALID_CHAR
        }
    }

}
