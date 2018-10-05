package app.horecafy.com.models

import java.io.Serializable

data class Offer(var hiddenId: Long = -1,
                 var id: String? = null,
                 var customerId: Long,
                 var demandId: Long,
                 var wholesalerId: Long,
                 var quantyPerMonth: Int,
                 var typeOfFormatId: Int,
                 var offerPrice: Double,
                 var brand: String,
                 var fomat: String,
                 var comments: String?,
                 var createdOn: String? = null,
                 var borrado: Boolean = false,
                 var approvedByCustomer: String? = null,
                 var sentToCustomer: String? = null,
                 var rejected: Boolean? = false,
                 var category: Category? = null,
                 var family: Family? = null,
                 var demand: Demand? = null,
                 var wholesaler: Wholesaler? = null): Serializable