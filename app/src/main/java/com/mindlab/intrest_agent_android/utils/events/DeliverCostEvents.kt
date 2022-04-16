package com.mindlab.intrest_agent_android.utils.events

import com.mindlab.intrest_agent_android.data.network.response.DeliverCostResponse

/**
 * Created by Alireza Nezami on 1/5/2022.
 */

sealed class DeliverCostEvents() {
    data class ShowEditDialog(val show: Boolean, val item: DeliverCostResponse.Embedded.Cost) : DeliverCostEvents()
    data class ShowAddDialog(val show: Boolean) : DeliverCostEvents()

}