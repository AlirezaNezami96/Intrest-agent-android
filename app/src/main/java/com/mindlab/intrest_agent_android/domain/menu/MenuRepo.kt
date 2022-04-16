package com.mindlab.intrest_agent_android.domain.menu

import com.mindlab.intrest_agent_android.data.network.response.ChangeFoodDetailResponse
import com.mindlab.intrest_agent_android.data.network.response.ChangeOrderResponse
import com.mindlab.intrest_agent_android.data.network.response.FoodEdibleResponse
import com.mindlab.intrest_agent_android.data.network.response.MenuResponse
import com.mindlab.intrest_agent_android.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
interface MenuRepo {

    fun getMenu(): Flow<Resource<List<MenuResponse>>>

    fun adjustFoodPrice(price: Int, foodId: Int):Flow<Resource<ChangeFoodDetailResponse>>

    fun adjustPreparationTime(time: Int, foodId: Int):Flow<Resource<FoodEdibleResponse>>

    fun changeFoodAvailability(foodId: Int, availability: Boolean):Flow<Resource<FoodEdibleResponse>>


}