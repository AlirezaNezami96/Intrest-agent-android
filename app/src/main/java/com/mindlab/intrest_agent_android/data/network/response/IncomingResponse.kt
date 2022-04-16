package com.mindlab.intrest_agent_android.data.network.response


import com.squareup.moshi.Json

data class IncomingResponse(
    @Json(name = "content")
    val content: List<Content>,

) {
    data class Content(
        @Json(name = "id")
        val id: Int,
        @Json(name = "lastStatusDate")
        val lastStatusDate: String,
        @Json(name = "description")
        val description: String,
        @Json(name = "trackingNumber")
        val trackingNumber: Int = 0,
        @Json(name = "serveType")
        val serveType: Int,
        @Json(name = "customer")
        val customer: Customer,
        @Json(name = "serveTypeTitle")
        val serveTypeTitle: String? = "",
        @Json(name = "lastOrderStatus")
        val lastOrderStatus: String = "",
        @Json(name = "orderItems")
        val orderItems: List<OrderItem>,
        @Json(name = "invoice")
        val invoice: Invoice?,
        @Json(name = "deliveryNote")
        val deliveryNote: String?,
        @Json(name = "deliveryType")
        val deliveryType: String?,
        @Json(name = "preparedTime")
        val preparedTime: String
    ) {
        data class Invoice(
            @Json(name = "id")
            val id: Int,
            @Json(name = "subtotal")
            val subtotal: Double?,
            @Json(name = "tax")
            val tax: Double?,
            @Json(name = "adjustedAmountTax")
            val adjustedAmountTax: Double?,
            @Json(name = "deliveryFee")
            val deliveryFee: Double?,
            @Json(name = "serviceFee")
            val serviceFee: Double?,
            @Json(name = "tip")
            val tip: Double?,
            @Json(name = "discount")
            val discount: Double?,
            @Json(name = "restaurantPortion")
            val restaurantPortion: Double?,
            @Json(name = "commissionFee")
            val commissionFee: Double?,
            @Json(name = "total")
            val total: Double?,
            @Json(name = "adjustedAmount")
            val adjustedAmount: Double?,
        )

        data class Customer(
            @Json(name = "id")
            val id: Int,
            @Json(name = "fullName")
            val fullName: String,
            @Json(name = "mobileNo")
            val mobileNo: String?,
            @Json(name = "email")
            val email: String,
            @Json(name = "username")
            val username: String,
        )

        data class OrderItem(
            @Json(name = "id")
            val id: Int,
            @Json(name = "quantity")
            val quantity: Int,
            @Json(name = "itemPrice")
            val itemPrice: Double,
            @Json(name = "food")
            val food: Food,
        ) {
            data class Food(
                @Json(name = "id")
                val id: Int,
                @Json(name = "name")
                val name: String
            )
        }

    }
}