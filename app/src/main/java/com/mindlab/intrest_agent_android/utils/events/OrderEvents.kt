package com.mindlab.intrest_agent_android.utils.events

/**
 * Created by Alireza Nezami on 1/5/2022.
 */

sealed class OrderEvents() {
    data class ChangeTab(val index: Int) : OrderEvents()
    class IncomingRefresh : OrderEvents()
    class InProgressRefresh : OrderEvents()
    class DeliverRefresh : OrderEvents()
    data class AcceptOrder(val id: Int) : OrderEvents()
    data class RejectOrder(val id: Int, val rejectReason: String) : OrderEvents()
    data class PriceAdjustment(val id: Int) : OrderEvents()
    data class DelayOrder(val id: Int, val time: Int) : OrderEvents()
    data class PreparedOrder(val id: Int) : OrderEvents()
    class DeliverByCourier(val id: Int) : OrderEvents()
    class PickupByCustomer(val id: Int) : OrderEvents()
}