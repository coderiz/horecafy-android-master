package app.horecafy.com.models

import java.io.Serializable


data class ReviewOffersProductDetails(val id: String? = null,
                                      var name: String? = null,
                                      var isVisible: Boolean? = false) : Serializable