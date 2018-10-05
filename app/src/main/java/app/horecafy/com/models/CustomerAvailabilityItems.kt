package app.horecafy.com.models

import java.io.Serializable


data class CustomerAvailabilityItems(var Day: String,
                                     var TimeSlot: String,
                                     var IsAvailable: Boolean) : Serializable