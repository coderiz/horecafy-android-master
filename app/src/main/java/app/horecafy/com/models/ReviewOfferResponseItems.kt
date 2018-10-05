package app.horecafy.com.models

import java.io.Serializable


data class ReviewOfferResponseItems(val hiddenId: Long? = null,
                                    val id: String? = null,
                                    val WholeSaler: ReviewOfferWholesaler,
                                    val Demand: ReviewOfferDemand,
                                    val quantyPerMonth: Int,
                                    val TypeOfFormat: ReviewOfferTypeofFormat,
                                    val offerPrice: Double,
                                    val brand: String,
                                    val images: String,
                                    val video: String,
                                    val fomat: String,
                                    val comments: String,
                                    val Product: ReviewOfferProduct,
                                    val createdOn: String,
                                    val approvedByCustomer: String,
                                    val borrado: Boolean,
                                    val sentToCustomer: String,
                                    val rejected: String) : Serializable