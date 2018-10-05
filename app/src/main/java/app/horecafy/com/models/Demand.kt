package app.horecafy.com.models

import java.io.Serializable

data class Demand(var hiddenId: Long = -1,
                  var id: String? = null,
                  var customerId: Long,
                  var customerName: String? = null,
                  var zipCode: String? = null,
                  var familyId: Int,
                  var familyName: String? = null,
                  var quantyPerMonth: Int?,
                  var typeOfFormatId: Int,
                  var targetPrice: Double?,
                  var brand: String,
                  var format: String,
                  var comments: String?,
                  var createdOn: String? = null,
                  var borrado: Boolean = false,
                  var sentTo: String? = null,
                  var family: Family?): Serializable