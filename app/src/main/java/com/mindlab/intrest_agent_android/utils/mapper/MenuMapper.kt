package com.mindlab.intrest_agent_android.utils.mapper

import com.mindlab.intrest_agent_android.data.model.Menu
import com.mindlab.intrest_agent_android.data.network.response.MenuResponse

/**
 * Created by Alireza Nezami on 1/9/2022.
 */
object MenuMapper {

    fun categoryMapper(list: List<MenuResponse.Category>): List<Menu> {
        val newList: MutableList<Menu> = mutableListOf(Menu(title = "Choose a menu"))
        list.map {
            newList.add(
                Menu(
                    id = it.id,
                    parentId = it.parent.id,
                    title = it.title
                )
            )
        }
        return newList
    }
}