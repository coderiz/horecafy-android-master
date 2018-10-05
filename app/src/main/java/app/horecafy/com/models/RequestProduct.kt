package app.horecafy.com.models


data class RequestProduct(val customerId: Long,
                          val productName: String,
                          val brand: String,
                          val consumption: String,
                          val targetPrice: String,
                          val allowCall: String)