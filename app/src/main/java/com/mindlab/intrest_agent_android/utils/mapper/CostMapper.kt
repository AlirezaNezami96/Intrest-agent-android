package com.mindlab.intrest_agent_android.utils.mapper

import com.mindlab.intrest_agent_android.data.network.body.EditDeliverCostBody
import com.mindlab.intrest_agent_android.data.network.response.DeliverCostResponse

/**
 * Created by Alireza Nezami on 1/23/2022.
 */
object CostMapper {

    fun map(
        list: List<DeliverCostResponse.Embedded.Cost>,
        restaurantId: Int
    ): List<EditDeliverCostBody> {
      return  list.map {
            EditDeliverCostBody(
                id = if (it.id != 0) it.id else null,
                lowerRadius = it.lowerRadius,
                upperRadius = it.upperRadius,
                cost = it.cost,
                restaurant = EditDeliverCostBody.Restaurant(restaurantId)
            )
        }
    }
}