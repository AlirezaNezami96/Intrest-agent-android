package com.mindlab.intrest_agent_android.utils.events

/**
 * Created by Alireza Nezami on 1/5/2022.
 */

sealed class MenuEvents() {
    data class CategoryLoading(val show: Boolean) : MenuEvents()
    data class SubCategoryLoading(val show: Boolean) : MenuEvents()
    data class FoodLoading(val show: Boolean) : MenuEvents()

    data class FoodUpdated(val updated: Boolean) : MenuEvents()
}