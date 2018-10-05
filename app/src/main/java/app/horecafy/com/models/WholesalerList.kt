package app.horecafy.com.models


data class WholesalerList(var id: Int = -1,
                          var wholesalerId: Long,
                          var familyId: Int,
                          var brand: String,
                          var comments: String? = null,
                          var createdOn: String? = null,
                          var family: Family? = null)