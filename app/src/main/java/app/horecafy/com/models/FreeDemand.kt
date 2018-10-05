package app.horecafy.com.models


data class FreeDemand(val hiddenId: Long? = null,
                      val id: String? = null,
                      val customerId: Long,
                      val demandText: String,
                      val createdOn: String? = null)