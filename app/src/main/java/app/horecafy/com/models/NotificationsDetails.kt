package app.horecafy.com.models


data class NotificationsDetails(val hiddenId: Long? = null,
                                val id: String? = null,
                                val zipcode: String? = null,
                                val typeOfBusinessId: Long? = null,
                                val customerId: Long? = null,
                                val wholesalerId: Long? = null,
                                val timeslot: String? = null,
                                val visitDate: String? = null,
                                val status: Boolean = false,
                                val createdOn: String? = null,
                                val borrado: Boolean = false,
                                val comments: String? = null,
                                val images: String? = null,
                                val video: String? = null,
                                val Customer: NotificationsSubDetails? = null,
                                val Wholesaler: NotificationsSubDetails? = null)