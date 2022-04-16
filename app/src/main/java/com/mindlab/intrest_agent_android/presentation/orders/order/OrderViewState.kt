package com.mindlab.intrest_agent_android.presentation.orders.order

import com.mindlab.intrest_agent_android.data.network.response.IncomingResponse
import kotlin.random.Random

/**
 * Created by Alireza Nezami on 1/26/2022.
 */
sealed class OrderViewState() {

    object Nothing : OrderViewState()

    data class PriceAdjustment(
        val item: IncomingResponse.Content.Invoice,
        val trackingNumber: Int,
        val show: Boolean,
        val random: Int = Random.nextInt()
    ) : OrderViewState(){
       fun getInvoiceItem(): IncomingResponse.Content.Invoice = item

       @JvmName("getTrackingNumber1")
       fun getTrackingNumber() : Int = trackingNumber

    }

    fun isAdjustmentPriceOpen(): Boolean = when (this) {
        is PriceAdjustment -> show
        Nothing -> false
    }
}