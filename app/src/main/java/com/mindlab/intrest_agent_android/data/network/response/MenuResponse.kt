package com.mindlab.intrest_agent_android.data.network.response


import com.squareup.moshi.Json

//data class MenuResponse(
//    val items: List<MenuResponseItem>
//) {
data class MenuResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "foods")
    val foods: List<Food>?,
    @Json(name = "categories")
    val categories: List<Category>?,
//        @Json(name = "parent")
//        val parent: Parent
) {
    data class Food(
        @Json(name = "id")
        val id: Int,
        @Json(name = "name")
        val name: String,

        @Json(name = "preparationTime")
        var preparationTime: String,
        @Json(name = "price")
        var price: Double,
        @Json(name = "active")
        val active: Boolean,
    )

    data class Category(
        @Json(name = "id")
        val id: Int,
        @Json(name = "title")
        val title: String,
        @Json(name = "foods")
        val foods: List<Food>?,
//            @Json(name = "categories")
//            val categories: Any,
        @Json(name = "parent")
        val parent: Parent
    ) {
        data class Parent(
            @Json(name = "id")
            val id: Int,
            @Json(name = "title")
            val title: String,
//                @Json(name = "foods")
//                val foods: Any,
//                @Json(name = "categories")
//                val categories: Any,
//                @Json(name = "parent")
//                val parent: Any
        )
    }

    data class Parent(
        @Json(name = "id")
        val id: Int,
        @Json(name = "title")
        val title: String,
        @Json(name = "foods")
        val foods: Any,
        @Json(name = "categories")
        val categories: Any,
        @Json(name = "parent")
        val parent: Any
    )
}