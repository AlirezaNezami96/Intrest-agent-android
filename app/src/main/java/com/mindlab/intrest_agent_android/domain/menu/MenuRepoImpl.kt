package com.mindlab.intrest_agent_android.domain.menu

import com.mindlab.intrest_agent_android.data.network.api.MenuApi
import com.mindlab.intrest_agent_android.data.network.body.FoodAvailabilityBody
import com.mindlab.intrest_agent_android.data.network.body.FoodPriceBody
import com.mindlab.intrest_agent_android.data.network.body.FoodTimeBody
import com.mindlab.intrest_agent_android.data.network.response.ChangeFoodDetailResponse
import com.mindlab.intrest_agent_android.data.network.response.FoodEdibleResponse
import com.mindlab.intrest_agent_android.data.network.response.MenuResponse
import com.mindlab.intrest_agent_android.domain.utils.ResponseValidator
import com.mindlab.intrest_agent_android.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Alireza Nezami on 12/20/2021.
 */
class MenuRepoImpl
@Inject constructor
    (
    private val menuApi: MenuApi,
//    private val userLocalSource: UserLocalSource,
    private val responseValidator: ResponseValidator,
) : MenuRepo {

    override fun getMenu(): Flow<Resource<List<MenuResponse>>> = flow {
        val response = menuApi.getIncomingPaging(8)
        emit(responseValidator.validateResponse(response))
    }

    override fun adjustFoodPrice(
        price: Int,
        foodId: Int
    ): Flow<Resource<ChangeFoodDetailResponse>> = flow {
        val response = menuApi.changePrice(
            foodId,
            FoodPriceBody(
                price
            )
        )
        emit(responseValidator.validateResponse(response))
    }

    override fun changeFoodAvailability(
        foodId: Int,
        availability: Boolean
    )
            : Flow<Resource<FoodEdibleResponse>> = flow {
        val response = menuApi.changeAvailability(
            foodId,
            FoodAvailabilityBody(availability)
        )
        emit(responseValidator.validateResponse(response))
    }

    override fun adjustPreparationTime(
        time: Int,
        foodId: Int
    ): Flow<Resource<FoodEdibleResponse>> = flow {
        val response = menuApi.changeTime(
            foodId,
            FoodTimeBody(
                time * 60
            )
        )
        emit(responseValidator.validateResponse(response))

    }
}
