package app.horecafy.com.models


data class Wholesaler(val hiddenId: Long? = null,
                      val id: String? = null,
                      val VAT: String,
                      val email: String,
                      val password: String,
                      var name: String,
                      val typeOfBusinessId: Int,
                      val contactName: String? = null,
                      val contactEmail: String? = null,
                      val contactMobile: String? = null,
                      val address: String? = null,
                      val city: String? = null,
                      val zipCode: String? = null,
                      val province: String? = null,
                      val country: String? = null,
                      val createdOn: String? = null,
                      val visible: Boolean = true)