package app.horecafy.com.models


data class SearchProductResponse(val hiddenId: Long? = null,
                                 val id: String? = null,
                                 val customerId: String? = null,
                                 val productName: String? = null,
                                 val brand: String? = null,
                                 val consumption: String? = null,
                                 val targetPrice: String? = null,
                                 val createdOn: String? = null,
                                 val visible: Boolean = true)