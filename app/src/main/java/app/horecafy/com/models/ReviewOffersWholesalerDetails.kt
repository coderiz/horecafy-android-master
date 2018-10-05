package app.horecafy.com.models

import java.io.Serializable


data class ReviewOffersWholesalerDetails(val hiddenId: Long? = null,
                                         val id: String? = null,
                                         var name: String? = null,
                                         val productsOffered: Int) : Serializable