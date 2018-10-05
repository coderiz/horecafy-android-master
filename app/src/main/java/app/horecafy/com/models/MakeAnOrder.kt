package app.horecafy.com.models


data class MakeAnOrder(val customerId: Long,
                       val wholesalerId: Long,
                       val deliveryDate: String,
                       val products: String,
                       val deliveryTime: String,
                       val comments: String)