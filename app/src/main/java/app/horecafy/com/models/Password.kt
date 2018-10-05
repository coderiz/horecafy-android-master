package app.horecafy.com.models


data class Password(val hiddenId: Long? = null,
                    val id: String? = null,
                    val VAT: String,
                    val typeUser: Boolean? = null,
                    val userId: String? = null,
                    val token: String? = null,
                    val name: String? = null,
                    val email: String? = null,
                    val createdOn: String? = null)